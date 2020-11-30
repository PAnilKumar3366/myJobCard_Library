package com.ods.myjobcard_library.viewmodels.notification;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class CurrentNotificationLiveData extends LiveData<Notification> {
    private static final String TAG = "CurrentNotificationLive";
    String mNotificationNum;
    Observer<Notification> notificationObserver;
    Context mOwner;
    Observer<Notification> mObserver = new Observer<Notification>() {
        @Override
        public void onChanged(@Nullable Notification notification) {
            Log.d(TAG, "onChanged: " + notification.getPriority());
        }
    };
    private LiveData<Notification> mCurrentNotification;

    public CurrentNotificationLiveData(Context context) {
        mOwner = context;
    }

    @Override
    protected void onActive() {
        super.onActive();
        observe((LifecycleOwner) mOwner, mObserver);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        removeObserver(mObserver);
    }

    public void setCurrentNotification(String mNotificationNum) {
        ResponseObject result;
        try {
            result = Notification.getNotifications(ZAppSettings.FetchLevel.Single, null, mNotificationNum, null, false);
            if (!result.isError()) {
                Notification notification = null;
                notification = ((ArrayList<Notification>) result.Content()).get(0);
                if (!notification.isError() && notification != null) {
                    Notification.setCurrNotification(notification);
                    setValue((notification));
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "setCurrentNotification: Exeception " + e.getMessage());
        }
    }

    @Nullable
    @Override
    public Notification getValue() {
        return super.getValue();
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super Notification> observer) {
        super.observe(owner, observer);
        Log.d(TAG, "observe: " + observer.toString());
    }
}
