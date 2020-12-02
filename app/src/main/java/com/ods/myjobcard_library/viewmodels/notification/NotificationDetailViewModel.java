package com.ods.myjobcard_library.viewmodels.notification;

import android.app.Application;

import androidx.annotation.NonNull;

public class NotificationDetailViewModel extends NotificationBaseViewModel {
    public NotificationDetailViewModel(@NonNull Application application) {
        super(application);
    }

  /*  public void setCurrentNotification(String notificationNum,boolean isWoNo) {
        mRepository.setCurrentNotification(notificationNum,isWoNo);
    }

    public MutableLiveData<Notification> getCurrentNotification() {
        return currentNotification=mRepository.getCurrentNotification();
    }*/
}
