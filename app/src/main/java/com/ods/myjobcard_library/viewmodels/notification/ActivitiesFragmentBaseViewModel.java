package com.ods.myjobcard_library.viewmodels.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.NotificationActivity;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class ActivitiesFragmentBaseViewModel extends NotificationBaseViewModel {

    private MutableLiveData<ArrayList<NotificationActivity>> mActivitiesList = new MutableLiveData<>();
    private MutableLiveData<NotificationActivity> singleActivityItem = new MutableLiveData<>();

    public ActivitiesFragmentBaseViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<NotificationActivity> getSingleActivityItem() {
        return singleActivityItem;
    }

    public void setSingleActivityItem(String notification, String itemNum, String activityNo, boolean isWoNo) {

        ResponseObject result = NotificationActivity.getActivities(ZAppSettings.FetchLevel.Single,
                notification, itemNum, activityNo, isWoNo);
        NotificationActivity mActivity = null;
        if (!result.isError())
            mActivity = ((ArrayList<NotificationActivity>) result.Content()).get(0);
        singleActivityItem.setValue(mActivity);
    }

    public MutableLiveData<ArrayList<NotificationActivity>> getActivitiesList() {
        return mActivitiesList;
    }

    public void setActivitiesList(String notificationNum, String itemNum, boolean isWONotif) {

        ResponseObject result = NotificationActivity.getActivities(ZAppSettings.FetchLevel.List, notificationNum, itemNum, "", isWONotif);
        ArrayList<NotificationActivity> activities;
        if (!result.isError())
            activities = (ArrayList<NotificationActivity>) result.Content();
        else
            activities = new ArrayList<>();
        mActivitiesList.setValue(activities);
    }
}
