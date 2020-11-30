package com.ods.myjobcard_library.viewmodels.online;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.entities.transaction.NotificationItem;

public class OnlineNotfiItemDetailViewModel extends ViewModel {

    private MutableLiveData<NotificationItem> singleItem=new MutableLiveData<>();


    public MutableLiveData<NotificationItem> getSingleItem() {
        return singleItem;
    }

    public void setSingleItem(NotificationItem item) {
        singleItem.setValue(item);
    }
}
