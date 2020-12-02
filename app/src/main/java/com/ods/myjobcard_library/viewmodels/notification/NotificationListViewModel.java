package com.ods.myjobcard_library.viewmodels.notification;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class NotificationListViewModel extends NotificationBaseViewModel {

    private static final String TAG = "NotificationListBaseViewModel";
    private String filterQuery;
    private boolean areRecordsFiltered;
    private ArrayList<Notification> notificationSubSet = new ArrayList<>();
    private ArrayList<Notification> notifications;
    private String orderByCriteria = null;
    private int totalNotificationCount = 0;
    private MutableLiveData<ArrayList<Notification>> mNotificationsList = new MutableLiveData<>();
    private MutableLiveData<Integer> mTotalNotificationCount = new MutableLiveData<>();
    private MutableLiveData<Notification> mCurrentNotification = new MutableLiveData<Notification>();
    //  public CurrentNotificationLiveData mLiveData;
    private NotificationRepository mRepository;

    public NotificationListViewModel(@NonNull Application application) {
        super(application);
        // mLiveData=new CurrentNotificationLiveData(application);
        if (mRepository == null)
            mRepository = NotificationRepository.getInstance();
    }

    public MutableLiveData<Integer> getmTotalNotificationCount() {
        return mTotalNotificationCount;
    }

    public void setSortList(String orderByCriteria) {
        this.orderByCriteria = orderByCriteria;
        loadList(filterQuery);
    }

    private void loadList(final String filterQuery) {
        notifications = new ArrayList<>();
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
     /*           pDialog = new ProgressDialog(NotificationListActivity.this);
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.setMessage(getString(R.string.msg_fetching_notifications));
                pDialog.show();*/
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    ResponseObject result = Notification.getNotifications(ZAppSettings.FetchLevel.List, ZAppSettings.Hierarchy.HeaderOnly, null, orderByCriteria, false);
                    if (filterQuery != null && !filterQuery.isEmpty())
                        notificationSubSet = (ArrayList<Notification>) Notification.getFilteredNotifications(filterQuery, ZAppSettings.FetchLevel.List, orderByCriteria).Content();
                    if (!result.isError()) {
                        notifications = (ArrayList<Notification>) result.Content();
                        return true;
                    }

                } catch (Exception e) {
                    DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (!aBoolean)
                    notifications = new ArrayList<Notification>();
                setNotifications();

                /*if (searchEditText.getText().toString().isEmpty())
                    onDataLoadSuccess();
                else
                    refreshListAfterSearch(searchEditText.getText().toString());*/
            }
        }.execute();
    }

    private void setNotifications() {
        if (orderByCriteria != null)
            orderByCriteria = null;
        if (filterQuery != null && !filterQuery.isEmpty())
            mNotificationsList.setValue(notificationSubSet);
        else
            mNotificationsList.setValue(notifications);
        mTotalNotificationCount.setValue(notifications.size());
    }

    public LiveData<ArrayList<Notification>> getNotificationsList() {
        return mNotificationsList;
    }

    public void setNotificationsList(String filterQuery) {
        this.filterQuery = filterQuery;
        loadList(filterQuery);
    }

  /*  public void setCurrentNotification(String mNotifiNum) {
        ResponseObject result;
        try {
            result = Notification.getNotifications(ZAppSettings.FetchLevel.Single, null, mNotifiNum, null, false);
            if (!result.isError()) {
                Notification notification = null;
                notification = ((ArrayList<Notification>) result.Content()).get(0);
                if (!notification.isError() && notification != null) {
                    Notification.setCurrNotification(notification);
                    mCurrentNotification.setValue((notification));
                    mLiveData.setCurrentNotification(notification.getNotification());
                }
            }
        }
        catch (Exception e){
            Log.d(TAG, "setCurrentNotification: Exeception "+e.getMessage());

        }
    }*/

   /* public MutableLiveData<Notification> getCurrentNotification() {
        return mCurrentNotification=mRepository.getCurrentNotification();
    }

    public void setCurrentNotification(String notificationNum,boolean isWoNo) {
        mRepository.setCurrentNotification(notificationNum,isWoNo);
    }*/
}
