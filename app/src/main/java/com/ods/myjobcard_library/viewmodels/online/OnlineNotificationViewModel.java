package com.ods.myjobcard_library.viewmodels.online;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.entities.transaction.NotificationItem;

import java.util.ArrayList;

public class OnlineNotificationViewModel extends ViewModel {
    private MutableLiveData<Notification> notification = new MutableLiveData<>();
    private MutableLiveData<ArrayList<NotificationItem>> onLineitemsList = new MutableLiveData<>();
    private MutableLiveData<NotificationItem> singleItem = new MutableLiveData<>();

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
}
