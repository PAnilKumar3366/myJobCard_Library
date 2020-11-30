package com.ods.myjobcard_library.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class AssetMapViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<WorkOrder>> workOrderList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Notification>> notificationList = new MutableLiveData<>();

    public AssetMapViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ArrayList<WorkOrder>> getWorkOrderList() {
        return workOrderList;
    }

    public void setWorkOrderList(ZAppSettings.FetchLevel fetchLevel, String orderNum, String orderByCriteria) {
        ResponseObject result = WorkOrder.getWorkOrders(fetchLevel, orderNum, orderByCriteria);
        if (result != null && !result.isError())
            workOrderList.setValue((ArrayList<WorkOrder>) result.Content());
    }

    public LiveData<ArrayList<Notification>> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(ZAppSettings.FetchLevel fetchLevel, ZAppSettings.Hierarchy hierarchy, String notificationNum, String orderByCriteria, Boolean isWoNotif) {
        ResponseObject result = Notification.getNotifications(fetchLevel, hierarchy, notificationNum, orderByCriteria, isWoNotif);
        if (result != null && !result.isError()) {
            notificationList.setValue((ArrayList<Notification>) result.Content());
        }
    }
}
