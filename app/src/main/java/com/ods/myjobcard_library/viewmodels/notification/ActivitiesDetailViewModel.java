package com.ods.myjobcard_library.viewmodels.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.entities.transaction.NotificationActivity;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class ActivitiesDetailViewModel extends NotificationBaseViewModel {
    MutableLiveData<Notification> notification = new MutableLiveData<>();
    private MutableLiveData<NotificationActivity> singleActivity = new MutableLiveData<>();

    //private NotificationRepository mRepository;
    public ActivitiesDetailViewModel(@NonNull Application application) {
        super(application);
        //mRepository=NotificationRepository.getInstance();
    }

    public void setSingleActivity(String notification, String itemNum, String activityNum, boolean isWONotif) {
        ResponseObject result = NotificationActivity.getActivities(ZAppSettings.FetchLevel.Single,
                notification, itemNum, activityNum, isWONotif);
        NotificationActivity currSelectedActivity = null;
        if (!result.isError())
            currSelectedActivity = ((ArrayList<NotificationActivity>) result.Content()).get(0);
        singleActivity.setValue(currSelectedActivity);
    }

    public MutableLiveData<NotificationActivity> getSingleActivity() {
        return singleActivity;
    }
}
