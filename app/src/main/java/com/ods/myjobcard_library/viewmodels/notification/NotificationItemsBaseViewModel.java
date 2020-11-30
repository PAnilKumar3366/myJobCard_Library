package com.ods.myjobcard_library.viewmodels.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.NotificationItem;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class NotificationItemsBaseViewModel extends NotificationBaseViewModel {

    private MutableLiveData<ArrayList<NotificationItem>> itemList = new MutableLiveData<>();
    private ArrayList<NotificationItem> items;
    private MutableLiveData<NotificationItem> currentItem = new MutableLiveData<>();

    public NotificationItemsBaseViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<NotificationItem>> getItemList() {
        return itemList;
    }

    public MutableLiveData<NotificationItem> getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(String notificationNum, String itemNum, boolean isWONotif) {
        ResponseObject result = NotificationItem.getNotifItems(ZAppSettings.FetchLevel.Single, notificationNum, itemNum, isWONotif);

        if (!result.isError())
            currentItem.setValue(((ArrayList<NotificationItem>) result.Content()).get(0));
        // this.currentItem = currentItem;
    }

    public void setItemList(String refId, Boolean isWONotif) {
        ResponseObject result = NotificationItem.getNotifItems(ZAppSettings.FetchLevel.List, refId, "", isWONotif);
        if (!result.isError())
            items = (ArrayList<NotificationItem>) result.Content();
        else
            items = new ArrayList<>();
        itemList.setValue(items);
    }
}
