package com.ods.myjobcard_library.viewmodels.online;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.entities.transaction.NotificationItem;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.StoreHelpers.TableConfigSet;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.client.odata.v4.EntityValue;
import com.sap.client.odata.v4.EntityValueList;

import java.util.ArrayList;
import java.util.HashMap;

public class OnlineNotifiListViewModel extends BaseViewModel {

    private static final String TAG = "OnlineNotifiListViewMod";
    public MutableLiveData<NotificationItem> singelItem = new MutableLiveData<>();
    public MutableLiveData<Notification> currentNotification = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Notification>> onlineNotifiListLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<NotificationItem>> OnlineItemlistLiveData = new MutableLiveData<>();
    private ArrayList<Notification> onlineNotificationsList = new ArrayList<>();
    private ArrayList<NotificationItem> onlineItemsList = new ArrayList<>();
    private MutableLiveData<ArrayList<Notification>> filterNotifications = new MutableLiveData<>();

    public OnlineNotifiListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Notification> getCurrentNotification() {
        return currentNotification;
    }

    public void setCurrentNotification(Notification notification) {
        Notification.setCurrNotification(notification);
        this.currentNotification.setValue(notification);
    }

    public MutableLiveData<NotificationItem> getSingelItem() {
        return singelItem;
    }

    public void setSingelItem(NotificationItem item) {
        this.singelItem.setValue(item);
    }

    public ArrayList<Notification> getOnlineNotificationsList() {
        return onlineNotificationsList;
    }

    public void setOnlineNotificationsList(ArrayList<Notification> onlineNotificationsList) {
        this.onlineNotificationsList = onlineNotificationsList;
    }

    public MutableLiveData<ArrayList<Notification>> getOnlineNotifiListLiveData() {
        return onlineNotifiListLiveData;
    }

    public void setOnlineNotifiListLiveData(ArrayList<Notification> onlineNotificationList) {
        onlineNotifiListLiveData.setValue(onlineNotificationList);
    }

    public void notificationFilter(HashMap<String, ArrayList<String>> filterHashmap) {
        ArrayList<Notification> filterList = new ArrayList<>();
        filterList.addAll(onlineNotificationsList);
        try {
            for (Notification notification : onlineNotificationsList) {
                if (filterHashmap.containsKey("Priority")) {
                    if (!filterHashmap.get("Priority").contains(notification.getPriority())) {
                        filterList.remove(notification);
                        continue;
                    }
                }
                if (filterHashmap.containsKey("NotificationType")) {
                    if (!filterHashmap.get("NotificationType").contains(notification.getNotificationType())) {
                        filterList.remove(notification);
                        continue;
                    }
                }
                if (filterHashmap.containsKey("Status")) {
                    if (!filterHashmap.get("Status").contains(notification.getUserStatus())) {
                        filterList.remove(notification);
                        continue;
                    }
                }
                if (filterHashmap.containsKey("MainWorkCenter")) {
                    if (!filterHashmap.get("MainWorkCenter").contains(notification.getWorkCenter())) {
                        filterList.remove(notification);
                        continue;
                    }
                }

                if (filterHashmap.containsKey("TechId")) {
                    ArrayList<String> techID = filterHashmap.get("TechId");
                    if (!isTwoNumericStringValuesEqual(techID.get(0), notification.getPartner())) {
                        filterList.remove(notification);
                        continue;
                    }
                }
                if (filterHashmap.containsKey("CreatedByMe")) {                           //Newley Added by Anil.
                    ArrayList<String> createdBy = filterHashmap.get("CreatedByMe");
                    if (createdBy.get(0).contains(notification.getEnteredBy().toUpperCase()))
                        filterList.remove(notification);
                }
            }
            filterNotifications.setValue(filterList);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }

    }

    private boolean isTwoNumericStringValuesEqual(String one, String two) {
        boolean result = false;
        try {
            if (Long.parseLong(one) == Long.parseLong(two))
                result = true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getLocalizedMessage());
            result = false;
        }
        return result;
    }

    public void fetchOnlineNotifications(String filterQuery) {
        String entitySet = "NotificationHeaderSet";
        //filterQueryWo="?$filter=(OnlineSearch eq 'X' and CreatedOn eq datetime'2019-12-23T00:00:00' and ChangedOn eq datetime'2019-12-31T00:00:00' and PlanningPlant eq 'GB01' and WorkCenter eq 'GB01_OP')&$expand=NavNOItem";
        String resPath = entitySet + filterQuery;
        try {
            new AsyncTask<Void, Void, ResponseObject>() {

                @Override
                protected ResponseObject doInBackground(Void... voids) {
                    ResponseObject result = DataHelper.getInstance().getEntitiesOnline(resPath, entitySet, TableConfigSet.getStore(entitySet));

                    return result;
                }

                @Override
                protected void onPostExecute(ResponseObject responseObject) {
                    super.onPostExecute(responseObject);

                    if (!responseObject.isError()) {

                        Log.d(TAG, "onPostExecute: " + responseObject.Content());
                        getNotificationsList(responseObject);
                        //updateUI(onlineworkorders);
                    } else {
                        Log.d(TAG, "onPostExecute: ");
                    }


                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            Log.e(TAG, "fetchOnlineData: Exception" + e.getMessage());
        }
    }

    private void getNotificationsList(ResponseObject responseObject) {
        if (!responseObject.isError()) {
            onlineItemsList.clear();
            onlineNotificationsList.clear();

            EntityValueList entityList = (EntityValueList) responseObject.Content();
            EntityValueList oprEntityList;
            ArrayList<Notification> notifications = new ArrayList<>();
            ArrayList<NotificationItem> notificationItems;
            for (EntityValue entityValue : entityList) {

                Notification notification = new Notification(entityValue);
                oprEntityList = entityValue.getEntityType().getProperty("NavNOItem").getEntityList(entityValue);

                notificationItems = new ArrayList<>();
                for (EntityValue oprEntity : oprEntityList) {
                    notificationItems.add(new NotificationItem(oprEntity));
                    onlineItemsList.add(new NotificationItem(oprEntity));
                    Log.d(TAG, "onPostExecute: Notification items" + notification.getNotification() + " item num");
                }
                notification.setNotificationItems(notificationItems);
                notifications.add(notification);

                //oprEntityValueList.add( entityValue.getEntityType().getProperty("NAVOPERA").getEntityList(entityValue));
                Log.d(TAG, "onPostExecute: Notification" + notification.toString());
            }
            onlineNotificationsList.addAll(notifications);
            OnlineDataList.getInstance().setOnLineNotifications(notifications);
            OnlineDataList.getInstance().setOnlineNotificationItems(onlineItemsList);
        } else {
            setError(responseObject.getMessage());
            Log.d(TAG, "onPostExecute: " + responseObject.getMessage());
        }
        onlineNotifiListLiveData.setValue(onlineNotificationsList);
    }

    public MutableLiveData<ArrayList<NotificationItem>> getOnlineItemlistLiveData() {
        return OnlineItemlistLiveData;
    }

    public void setOnlineItemlistLiveData(ArrayList<NotificationItem> onlineItemlistLiveData) {
        OnlineItemlistLiveData.setValue(onlineItemlistLiveData);
    }

    public MutableLiveData<ArrayList<Notification>> getFilterNotifications() {
        return filterNotifications;
    }

    public void setFilterNotifications(HashMap<String, ArrayList<String>> filterHashMap) {
        notificationFilter(filterHashMap);
    }
}
