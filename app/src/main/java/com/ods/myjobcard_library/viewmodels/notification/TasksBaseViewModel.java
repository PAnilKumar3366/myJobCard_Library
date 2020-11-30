package com.ods.myjobcard_library.viewmodels.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.NotificationTask;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class TasksBaseViewModel extends NotificationBaseViewModel {
    private MutableLiveData<ArrayList<NotificationTask>> notificationTaskList = new MutableLiveData<>();
    private MutableLiveData<NotificationTask> singleTaskItem = new MutableLiveData<>();

    public TasksBaseViewModel(@NonNull Application application) {
        super(application);
    }

    public void setNotificationTaskItem(String notification, String itemNum, String taskNum, boolean isWONotif) {
        ResponseObject result = NotificationTask.getTasks(ZAppSettings.FetchLevel.Single,
                notification, itemNum, taskNum, isWONotif);
        NotificationTask currSelectedTask = null;
        if (!result.isError())
            currSelectedTask = ((ArrayList<NotificationTask>) result.Content()).get(0);
        singleTaskItem.setValue(currSelectedTask);
    }

    public MutableLiveData<NotificationTask> getSingleTaskItem() {
        return singleTaskItem;
    }

    public void setNotificationTaskList(String notification, String itemNum, boolean isWONotif) {
        ResponseObject result = NotificationTask.getTasks(ZAppSettings.FetchLevel.List, notification, itemNum, "", isWONotif);
        ArrayList<NotificationTask> taskArrayList = new ArrayList<>();
        if (!result.isError())
            taskArrayList = (ArrayList<NotificationTask>) result.Content();
        notificationTaskList.setValue(taskArrayList);
    }

    public MutableLiveData<ArrayList<NotificationTask>> getNotificationTaskList() {
        return notificationTaskList;
    }
}
