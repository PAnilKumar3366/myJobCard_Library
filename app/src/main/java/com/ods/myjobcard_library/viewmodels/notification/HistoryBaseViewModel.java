package com.ods.myjobcard_library.viewmodels.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.entities.transaction.NotificationHistoryPending;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class HistoryBaseViewModel extends NotificationBaseViewModel {

    private ArrayList<NotificationHistoryPending> historyItems;
    private ArrayList<NotificationHistoryPending> pendingItems;
    private MutableLiveData<ArrayList<NotificationHistoryPending>> historyLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<NotificationHistoryPending>> pendingLiveData = new MutableLiveData<>();

    public HistoryBaseViewModel(@NonNull Application application) {
        super(application);
    }

    public void setHistoryLiveData(Notification notification, boolean isHistory) {
        ResponseObject result = NotificationHistoryPending.getNotifHistoryPendingItems(notification.getEquipment(), notification.getFunctionalLoc(), true);
        historyItems = new ArrayList<>();

        if (!result.isError())
            historyItems = (ArrayList<NotificationHistoryPending>) result.Content();
        historyLiveData.setValue(historyItems);
    }

    public MutableLiveData<ArrayList<NotificationHistoryPending>> getHistoryLiveData() {
        return historyLiveData;
    }

    public MutableLiveData<ArrayList<NotificationHistoryPending>> getPendingLiveData() {
        return pendingLiveData;
    }

    public void setPendingLiveData(Notification notification) {
        ResponseObject result = NotificationHistoryPending.getNotifHistoryPendingItems(notification.getEquipment(), notification.getFunctionalLoc(), false);
        if (!result.isError())
            pendingItems = (ArrayList<NotificationHistoryPending>) result.Content();
        else
            pendingItems = new ArrayList<>();
        pendingLiveData.setValue(pendingItems);
    }
}
