package com.ods.myjobcard_library.viewmodels.online;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.NotifLongText;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.entities.transaction.NotificationItem;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.myjobcard_library.viewmodels.notification.NoLongTextHelper;
import com.ods.myjobcard_library.viewmodels.notification.NotificationHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.ConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;
import java.util.HashMap;

public class OnlineNotificationViewModel extends BaseViewModel {

    private MutableLiveData<Notification> notification = new MutableLiveData<>();
    private MutableLiveData<ArrayList<NotificationItem>> onLineitemsList = new MutableLiveData<>();
    private MutableLiveData<NotificationItem> singleItem = new MutableLiveData<>();

    private MutableLiveData<ResponseObject> longTextLiveData = new MutableLiveData<>();
    private Observer<ResponseObject> longtextObserver, updatedNoObserver;
    private MutableLiveData<ArrayList<String>> onlineNoLongText = new MutableLiveData<>();
    private MutableLiveData<ResponseObject> updatedNoResult = new MutableLiveData<>();
    private NoLongTextHelper longTextHelper;
    private NotificationHelper notificationHelper;

    public OnlineNotificationViewModel(@NonNull Application application) {
        super(application);
        longTextHelper = new NoLongTextHelper();
    }

    public MutableLiveData<ResponseObject> getLongTextLiveData() {
        return longTextLiveData;
    }

    public MutableLiveData<ResponseObject> getUpdatedNoResult() {
        return updatedNoResult;
    }


    public MutableLiveData<Notification> getNotification() {
        return notification;
    }

    public void setNotification(Notification notification, Boolean isWONotif) {
        Notification.setCurrNotification(notification);
        this.notification.setValue(notification);
    }

    public MutableLiveData<ArrayList<NotificationItem>> getOnLineitemsList() {
        return onLineitemsList;
    }

    public void setOnLineitemsList(ArrayList<NotificationItem> onLineitemsList) {
        this.onLineitemsList.setValue(onLineitemsList);
    }

    public MutableLiveData<NotificationItem> getSingleItem() {
        return singleItem;
    }

    public void setSingleItem(NotificationItem item) {
        this.singleItem.setValue(item);
    }

    public MutableLiveData<ArrayList<String>> getOnlineNoLongText() {
        return onlineNoLongText;
    }

    public void fetchOnlineWOLongText(HashMap<String, String> hashMapQuery) {
        try {
            String finalQuery = longTextHelper.getOnlineQuery(hashMapQuery);
            longTextLiveData = longTextHelper.getLongTextLiveData();
            if (!finalQuery.isEmpty())
                longTextHelper.getOnlineNOLongText(finalQuery);
            else
                setError("Query Error");
            longtextObserver = new Observer<ResponseObject>() {
                @Override
                public void onChanged(ResponseObject responseObject) {
                    if (!responseObject.isError()) {
                        ArrayList<ZODataEntity> zoDataEntities = new ArrayList<>();
                        zoDataEntities = (ArrayList<ZODataEntity>) responseObject.Content();
                        onFetchNOLongText(zoDataEntities);
                    } else
                        setError(responseObject.getMessage());
                    longTextLiveData.removeObserver(longtextObserver);
                }
            };
            longTextLiveData.observeForever(longtextObserver);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    private void onFetchNOLongText(ArrayList<ZODataEntity> zoDataEntities) {
        try {
            ArrayList<NotifLongText> longText = new ArrayList<>();
            ArrayList<String> woLongTextList = new ArrayList<>();
            for (ZODataEntity entity : zoDataEntities) {
                NotifLongText noLongText = new NotifLongText(entity);
                longText.add(noLongText);
            }
            for (NotifLongText noLongText : longText) {
                woLongTextList.add(noLongText.getTextLine());
            }
            onlineNoLongText.setValue(woLongTextList);
        } catch (Exception e) {
            e.printStackTrace();
            setError(e.getMessage());
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public void updateNotificationOnline(Notification notification) {
        try {
            if (notificationHelper == null)
                notificationHelper = new NotificationHelper();
            //updatedNoResult=notificationHelper.getUpdatedNoResult();
            notificationHelper.UpdateNotificationOnline(notification);
            updatedNoObserver = new Observer<ResponseObject>() {
                @Override
                public void onChanged(ResponseObject responseObject) {
                    updateNotification(responseObject);
                }
            };
            notificationHelper.getUpdatedNoResult().observeForever(updatedNoObserver);
        } catch (Exception e) {
            e.printStackTrace();
            updatedNoResult.postValue(new ResponseObject(ConfigManager.Status.Error, e.getMessage(), null));
        }
    }

    private void updateNotification(ResponseObject responseObject) {
        updatedNoResult.postValue(responseObject);
        updatedNoResult.removeObserver(updatedNoObserver);
    }
}
