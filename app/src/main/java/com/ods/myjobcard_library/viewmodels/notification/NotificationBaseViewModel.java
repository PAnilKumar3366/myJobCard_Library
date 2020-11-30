package com.ods.myjobcard_library.viewmodels.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class NotificationBaseViewModel extends BaseViewModel {

    private MutableLiveData<Notification> mCurrentNotification = new MutableLiveData<Notification>();

    public NotificationBaseViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Notification> getCurrentNotification() {
        return mCurrentNotification;
    }

    public void setCurrentNotification(String mNotifiNum, boolean isWONotifi) {
        ResponseObject result;
        try {
            result = Notification.getNotifications(ZAppSettings.FetchLevel.Single, null, mNotifiNum, null, isWONotifi);
            if (!result.isError()) {
                Notification notification = null;
                notification = ((ArrayList<Notification>) result.Content()).get(0);
                Notification.setCurrNotification(notification);
                mCurrentNotification.setValue((notification));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }
}
