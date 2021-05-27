package com.ods.myjobcard_library.viewmodels.online;

import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.entities.transaction.NotificationItem;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;

import java.io.Serializable;
import java.util.ArrayList;

public class OnlineDataList implements Serializable {

    private static volatile OnlineDataList sSoleInstance;
    public ArrayList<NotificationItem> onlineNotificationItems = new ArrayList<>();
    public ArrayList<Operation> workOrdersOperationsList = new ArrayList<>();
    public ArrayList<WorkOrder> onlineWorkOrderList = new ArrayList<>();
    public ArrayList<Notification> onLineNotifications = new ArrayList<>();

    //private constructor.
    private OnlineDataList() {

        //Prevent form the reflection api.
        if (sSoleInstance != null) {
                throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static OnlineDataList getInstance() {
        if (sSoleInstance == null) { //if there is no instance available... create new one
            synchronized (OnlineDataList.class) {
                if (sSoleInstance == null) sSoleInstance = new OnlineDataList();
            }
        }

        return sSoleInstance;
    }

    public ArrayList<WorkOrder> getOnlineWorkOrderList() {
        return onlineWorkOrderList;
    }

    public void setOnlineWorkOrderList(ArrayList<WorkOrder> onlineWorkOrderList) {
        this.onlineWorkOrderList = onlineWorkOrderList;
    }

    public ArrayList<NotificationItem> getOnlineNotificationItems() {
        return onlineNotificationItems;
    }

    public void setOnlineNotificationItems(ArrayList<NotificationItem> onlineNotificationItems) {
        this.onlineNotificationItems = onlineNotificationItems;
    }

    public ArrayList<Operation> getWorkOrdersOperationsList() {
        return workOrdersOperationsList;
    }

    public void setWorkOrdersOperationsList(ArrayList<Operation> workOrdersOperations) {
        this.workOrdersOperationsList = workOrdersOperations;
    }

    public ArrayList<Notification> getOnLineNotifications() {
        return onLineNotifications;
    }

    public void setOnLineNotifications(ArrayList<Notification> onLineNotifications) {
        this.onLineNotifications = onLineNotifications;
    }

    //Make singleton from serialize and deserialize operation.
    protected OnlineDataList readResolve() {
        return getInstance();
    }
}
