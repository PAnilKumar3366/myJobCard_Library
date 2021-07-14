package com.ods.myjobcard_library.viewmodels.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class NotificationBaseViewModel extends BaseViewModel {

    private MutableLiveData<Notification> mCurrentNotification = new MutableLiveData<Notification>();
    public NotificationHelper notificationHelper;
    public NotificationBaseViewModel(@NonNull Application application) {
        super(application);
        notificationHelper=new NotificationHelper();
    }

    public MutableLiveData<Notification> getCurrentNotification() {
        return mCurrentNotification;
    }

    public void setCurrentNotification(String mNotifiNum, boolean isWONotifi) {
        ResponseObject result;
        try {
            result = Notification.getNotifications(ZAppSettings.FetchLevel.Single, null, mNotifiNum, null, isWONotifi);
            if (!result.isError()) {
                Notification notification = null;
                notification = ((ArrayList<Notification>) result.Content()).get(0);
                Notification.setCurrNotification(notification);
                mCurrentNotification.setValue((notification));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /** fetching current selected notification zodata entity
     * @param notifiNum
     * @param isWONotifi
     */
    public void fetchSingleNotification(String notifiNum, boolean isWONotifi){
        try{
            ZODataEntity zoDataEntity=notificationHelper.getSingleNotification(notifiNum,isWONotifi);
            mCurrentNotification.setValue(onFetchSingleNotificationEntities(zoDataEntity,isWONotifi,true));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**Converting ZOData single Entity to single notification object
     * @param zoDataEntity
     * @param isWONotifi
     * @param fetchAddress
     * @return
     */
    protected Notification onFetchSingleNotificationEntities(ZODataEntity zoDataEntity,boolean isWONotifi,boolean fetchAddress){
        Notification notification = null;
        try {
            if(zoDataEntity!=null){
            notification=new Notification(zoDataEntity, isWONotifi, fetchAddress);
            Notification.setCurrNotification(notification);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return notification;
    }
}
