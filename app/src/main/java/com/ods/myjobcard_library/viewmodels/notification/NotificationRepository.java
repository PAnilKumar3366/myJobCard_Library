package com.ods.myjobcard_library.viewmodels.notification;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class NotificationRepository {

    private static final String TAG = "NotificationRepository";
    private static NotificationRepository instance;
    private MutableLiveData<Notification> currentNotification = new MutableLiveData<>();

    public static NotificationRepository getInstance() {
        if (instance == null)
            instance = new NotificationRepository();
        return instance;
    }

    public MutableLiveData<Notification> getCurrentNotification() {
        return currentNotification;
    }

    public void setCurrentNotification(String notificationNum, boolean isForWo) {
        ResponseObject result;
        try {
            result = Notification.getNotifications(ZAppSettings.FetchLevel.Single, null, notificationNum, null, isForWo);
            if (!result.isError()) {
                Notification notification = null;
                notification = ((ArrayList<Notification>) result.Content()).get(0);
                if (!notification.isError() && notification != null) {
                    Notification.setCurrNotification(notification);
                    currentNotification.setValue((notification));

                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception" + e.getMessage());
        }
    }
}
