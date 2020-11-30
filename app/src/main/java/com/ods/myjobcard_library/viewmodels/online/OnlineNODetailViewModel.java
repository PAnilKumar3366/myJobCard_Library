package com.ods.myjobcard_library.viewmodels.online;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.entities.transaction.NotificationItem;

import java.util.ArrayList;

public class OnlineNODetailViewModel extends ViewModel {

    private MutableLiveData<Notification> selectedNotification = new MutableLiveData<>();
    private MutableLiveData<ArrayList<NotificationItem>> notificationItemList = new MutableLiveData<>();

    public MutableLiveData<Notification> getSelectedNotification() {
        return selectedNotification;
    }

    public void setSelectedNotification(Notification selectedNotification) {
        this.selectedNotification.setValue(selectedNotification);
    }
}
