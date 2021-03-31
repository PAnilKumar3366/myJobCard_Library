package com.ods.myjobcard_library.viewmodels.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.appsettings.StatusCategory;
import com.ods.myjobcard_library.entities.transaction.NotificationTask;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class TasksViewModel extends NotificationBaseViewModel {
    private MutableLiveData<ArrayList<NotificationTask>> notificationTaskList = new MutableLiveData<>();
    private MutableLiveData<NotificationTask> singleTaskItem = new MutableLiveData<>();

    private MutableLiveData<StatusCategory> statusCategoryLiveData=new MutableLiveData<>();
    private MutableLiveData<ArrayList<StatusCategory>> validStatusesList=new MutableLiveData<>();
    private MutableLiveData<Boolean> updateStatus=new MutableLiveData<>();
    private NotificationTaskHelper taskHelper;

    public TasksViewModel(@NonNull Application application) {
        super(application);
        taskHelper=new NotificationTaskHelper();
    }

    public void setNotificationTaskItem(String notification, String itemNum, String taskNum, boolean isWONotif) {
        try {
            ResponseObject result = NotificationTask.getTasks(ZAppSettings.FetchLevel.Single,
                    notification, itemNum, taskNum, isWONotif);
            NotificationTask currSelectedTask = null;
            if (!result.isError())
                currSelectedTask = ((ArrayList<NotificationTask>) result.Content()).get(0);
            singleTaskItem.setValue(currSelectedTask);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public MutableLiveData<NotificationTask> getSingleTaskItem() {
        return singleTaskItem;
    }

    public void setNotificationTaskList(String notification, String itemNum, boolean isWONotif) {
        try {
            ResponseObject result = NotificationTask.getTasks(ZAppSettings.FetchLevel.List, notification, itemNum, "", isWONotif);
            ArrayList<NotificationTask> taskArrayList = new ArrayList<>();
            if (!result.isError())
                taskArrayList = (ArrayList<NotificationTask>) result.Content();
            notificationTaskList.setValue(taskArrayList);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public MutableLiveData<ArrayList<NotificationTask>> getNotificationTaskList() {
        return notificationTaskList;
    }

    public void fetchCurrentStatusDetails(String status,String objType){
        try {
            taskHelper=new NotificationTaskHelper(status,objType);
            statusCategoryLiveData.setValue(taskHelper.deriveNotificationTaskStatus());
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }
    public MutableLiveData<StatusCategory> getStatusCategoryLiveData() {
        return statusCategoryLiveData;
    }

    public void fetchValidStatuses(){
        try {
            validStatusesList.setValue(taskHelper.getAllowedStatus());
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public MutableLiveData<ArrayList<StatusCategory>> getValidStatusesList() {
        return validStatusesList;
    }

    public MutableLiveData<Boolean> getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(NotificationTask task,StatusCategory status,boolean autoFlush) {
        try {
            taskHelper=new NotificationTaskHelper(task,status,autoFlush);
            updateStatus.setValue(taskHelper.updateStatus(autoFlush));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }
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
