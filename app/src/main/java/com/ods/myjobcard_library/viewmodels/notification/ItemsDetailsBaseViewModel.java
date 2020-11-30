package com.ods.myjobcard_library.viewmodels.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.entities.transaction.NotificationItem;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class ItemsDetailsBaseViewModel extends NotificationBaseViewModel {

    MutableLiveData<Notification> notification = new MutableLiveData<>();
    MutableLiveData<NotificationItem> singleItem = new MutableLiveData<>();

    //private NotificationRepository mRepository;
    public ItemsDetailsBaseViewModel(@NonNull Application application) {
        super(application);

    }

  /*  public void setNotification(String notification,boolean isWoNo) {
        mRepository.setCurrentNotification(notification,isWoNo);
    }

    public MutableLiveData<Notification> getNotification() {
        return notification=mRepository.getCurrentNotification();
    }*/

    public void setSingleItem(String notification, String itemNum, boolean isWONotif) {
        ResponseObject result = NotificationItem.getNotifItems(ZAppSettings.FetchLevel.Single, notification, itemNum, isWONotif);
        if (!result.isError())
            singleItem.setValue(((ArrayList<NotificationItem>) result.Content()).get(0));
    }

    public MutableLiveData<NotificationItem> getSingleItem() {
        return singleItem;
    }
}
