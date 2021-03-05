package com.ods.myjobcard_library.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.myjobcard_library.interfaces.BackgroundTaskInterface;
import com.ods.myjobcard_library.viewmodels.notification.NotificationHelper;
import com.ods.myjobcard_library.viewmodels.workorder.WorkOrderHelper;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;
import java.util.HashMap;

/*Created By Anil Kumar*/

public class OnlinePendingListViewModel extends BaseViewModel {
    private static final String TAG = "OnlinePendingListViewMo";
    private MutableLiveData<ArrayList<WorkOrder>> onlineWoList = new MutableLiveData<ArrayList<WorkOrder>>();
    private MutableLiveData<ArrayList<Notification>> onlineNotificationList = new MutableLiveData<>();

    public OnlinePendingListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<WorkOrder>> getOnlineWoList() {
        return onlineWoList;
    }

    public MutableLiveData<ArrayList<Notification>> getOnlineNotificationList() {
        return onlineNotificationList;
    }

    /*Fetching Online Pending Work orders list as ZODataEntity List */
    public void fetchWorkOrdersOnline(HashMap<String, String> mapQuery) {
        WorkOrderHelper workOrderHelper = new WorkOrderHelper();
        try {
            workOrderHelper.setTaskInterface(new BackgroundTaskInterface() {
                @Override
                public void onTaskPostExecute(ArrayList<ZODataEntity> zoDataEntities, boolean isError, String errorMsg) {
                    if (!isError)
                        onFetchOnlineWOList(zoDataEntities);
                    else
                        setError(errorMsg);
                }

                @Override
                public void onTaskPreExecute() {
                }

                @Override
                public void onTaskProgressUpdate() {
                }
            });
            String finalQuery = workOrderHelper.getQuery(mapQuery);
            if (!finalQuery.isEmpty())
                workOrderHelper.getWorkOrdersOnline(finalQuery);
            else
                setError("Error in Query");
        } catch (Exception e) {
            e.printStackTrace();
            setError(e.getMessage());
        }

    }

    /*Fetching the final online query and fetching online notifications pending list as ZODataEntity List*/
    public void fetchNotificationsOnline(HashMap<String, String> mapQuery) {
        try {
            NotificationHelper notificationHelper = new NotificationHelper();
            notificationHelper.setTaskInterface(new BackgroundTaskInterface() {
                @Override
                public void onTaskPostExecute(ArrayList<ZODataEntity> zoDataEntities, boolean isError, String errorMsg) {
                    if (!isError)
                        onFetchOnlineNOList(zoDataEntities);
                    else
                        setError(errorMsg);
                }

                @Override
                public void onTaskPreExecute() {

                }

                @Override
                public void onTaskProgressUpdate() {

                }
            });
            String finalQuery = notificationHelper.getQuery(mapQuery);
            if (!finalQuery.isEmpty())
                notificationHelper.getNotificationsOnline(finalQuery);
            else
                setError("Error in Query");
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /*Converting the ZODataEntity list to WorkOrder's list  */
    protected void onFetchOnlineWOList(ArrayList<ZODataEntity> zoDataEntities) {
        ArrayList<WorkOrder> onlineWo = new ArrayList<>();
        try {
            for (ZODataEntity entity : zoDataEntities) {
                WorkOrder item = new WorkOrder(entity);
                onlineWo.add(item);
            }
            onlineWoList.postValue(onlineWo);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /*Converting the ZODataEntity List to Notification's List */
    protected void onFetchOnlineNOList(ArrayList<ZODataEntity> zoDataEntities) {
        ArrayList<Notification> onlineNoList = new ArrayList<>();
        try {
            for (ZODataEntity entity : zoDataEntities) {
                Notification item = new Notification(entity);
                onlineNoList.add(item);
            }
            onlineNotificationList.setValue(onlineNoList);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
    }

    @Override
    public void onFetchEntitiesResult(ArrayList<ZODataEntity> entities) {
        //super.onFetchEntitiesResult(entities);

    }
}
