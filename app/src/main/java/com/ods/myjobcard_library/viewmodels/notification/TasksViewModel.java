package com.ods.myjobcard_library.viewmodels.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.appsettings.StatusCategory;
import com.ods.myjobcard_library.entities.attachment.WorkOrderAttachment;
import com.ods.myjobcard_library.entities.transaction.NotificationTask;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class TasksViewModel extends NotificationBaseViewModel {
    private MutableLiveData<ArrayList<NotificationTask>> notificationTaskList = new MutableLiveData<>();
    private MutableLiveData<NotificationTask> singleTaskItem = new MutableLiveData<>();

    private MutableLiveData<StatusCategory> statusCategoryLiveData=new MutableLiveData<>();
    private MutableLiveData<ArrayList<StatusCategory>> validStatusesList=new MutableLiveData<>();
    private MutableLiveData<Boolean> updateStatus=new MutableLiveData<>();
    private NotificationTaskHelper taskHelper;
    private ArrayList<NotificationTask> notificationTaskArrayList;

    public TasksViewModel(@NonNull Application application) {
        super(application);
        taskHelper=new NotificationTaskHelper();
    }

    /**
     * fetching the single Notification Task or Notification Item Task by calling through helper instance
     * @param notification
     * @param itemNum
     * @param taskNum
     * @param isWONotif
     */
    public void onFetchNotificationTaskItem(String notification, String itemNum, String taskNum, boolean isWONotif) {
        try {

            ArrayList<ZODataEntity> zoDataEntityArrayList=taskHelper.getTasks(ZAppSettings.FetchLevel.Single,notification,itemNum,taskNum,isWONotif);
            ArrayList<NotificationTask> notificationTaskArrayList=onFetchNotificationTaskEntities(zoDataEntityArrayList,isWONotif);
            if(notificationTaskArrayList.size()>0)
                singleTaskItem.setValue(notificationTaskArrayList.get(0));

            /*ResponseObject result = NotificationTask.getTasks(ZAppSettings.FetchLevel.Single,
                    notification, itemNum, taskNum, isWONotif);
            NotificationTask currSelectedTask = null;
            if (!result.isError())
                currSelectedTask = ((ArrayList<NotificationTask>) result.Content()).get(0);
            singleTaskItem.setValue(currSelectedTask);*/
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**
     * Converting the ZODataEntity list to NotificationTask object
     *
     * @param zODataEntities
     * @return
     */
    protected ArrayList<NotificationTask> onFetchNotificationTaskEntities(ArrayList<ZODataEntity> zODataEntities,boolean isWONotif) {
        notificationTaskArrayList = new ArrayList<>();
        try {
            for (ZODataEntity entity : zODataEntities) {
                NotificationTask notificationTask = new NotificationTask(entity,isWONotif);
                notificationTaskArrayList.add(notificationTask);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return notificationTaskArrayList;
    }

    /**
     * getting livedata for single notification task or notification item task
     * @return
     */
    public MutableLiveData<NotificationTask> getSingleTaskItem() {
        return singleTaskItem;
    }

    /**
     * fetching all tasks for the notification task and notification item task by calling through its helper instance
     * @param notification
     * @param itemNum
     * @param isWONotif
     */
    public void onFetchNotificationTaskList(String notification, String itemNum, boolean isWONotif) {
        try {
            ArrayList<ZODataEntity> zoDataEntityArrayList=taskHelper.getTasks(ZAppSettings.FetchLevel.List, notification, itemNum, "", isWONotif);
            notificationTaskList.setValue(onFetchNotificationTaskEntities(zoDataEntityArrayList,isWONotif));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**
     * getting live data for all tasks for the notification  or notification item
     * @return
     */
    public MutableLiveData<ArrayList<NotificationTask>> getNotificationTaskList() {
        return notificationTaskList;
    }

    /**
     * fetching current status information by calling its helper instance
     * @param status
     * @param objType
     */
    public void fetchCurrentStatusDetails(String status,String objType){
        try {
            //taskHelper=new NotificationTaskHelper(status,objType);
            statusCategoryLiveData.setValue(taskHelper.deriveNotificationTaskStatus(status,objType));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**
     * getting the live data for current status details
     * @return
     */
    public MutableLiveData<StatusCategory> getStatusCategoryLiveData() {
        return statusCategoryLiveData;
    }

    /**
     * fetching all valid statuses by calling its helper instance
     */
    public void fetchValidStatuses(){
        try {
            validStatusesList.setValue(taskHelper.getAllowedStatus());
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**
     * getting livedata for the valid statuses
     * @return
     */
    public MutableLiveData<ArrayList<StatusCategory>> getValidStatusesList() {
        return validStatusesList;
    }

    /**
     * getting the live data for updated status result
     * @return
     */
    public MutableLiveData<Boolean> getUpdateStatus() {
        return updateStatus;
    }

    /**
     * posting for updating the status
     * @param status
     * @param autoFlush
     */
    public void updateTaskStatus(StatusCategory status,boolean autoFlush) {
        try {
            //taskHelper=new NotificationTaskHelper(task,status,autoFlush);
            updateStatus.setValue(taskHelper.updateStatus(getSingleTaskItem().getValue(),status,autoFlush));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**
     * getting task attachmnet count by calling its helper instance
     * @param isWONotif
     * @return
     */
    public int getTaskAttachmentCount(boolean isWONotif) {
        try {
            if (singleTaskItem.getValue() != null) {
                String objKey = singleTaskItem.getValue().getNotification() + singleTaskItem.getValue().getItem() + singleTaskItem.getValue().getTask();
                return taskHelper.getTotalNumAttachments(isWONotif, objKey);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return 0;
    }

}
