package com.ods.myjobcard_library.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.entities.transaction.NotificationItem;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.myjobcard_library.viewmodels.notification.NotificationHelper;
import com.ods.myjobcard_library.viewmodels.online.OnlineDataList;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.client.odata.v4.EntityValue;
import com.sap.client.odata.v4.EntityValueList;

import java.util.ArrayList;
import java.util.HashMap;

/*Created By Anil Kumar*/

/**
 * This helps to download the online orders and notifications along with child entities like Operations and NotificationItems.
 */
public class OnlineListViewModel extends BaseViewModel {
    private static final String TAG = "OnlinePendingListViewMo";
    private MutableLiveData<ArrayList<WorkOrder>> onlineWoList = new MutableLiveData<ArrayList<WorkOrder>>();
    private MutableLiveData<ArrayList<Notification>> onlineNotificationList = new MutableLiveData<>();
    private ArrayList<Operation> operations = new ArrayList<>();
    private ArrayList<NotificationItem> notificationItems = new ArrayList<>();
    private MutableLiveData<ResponseObject> onlineWOEntities = new MutableLiveData<>();

    private WorkOrderHelper workOrderHelper;
    private NotificationHelper notificationHelper;
    private LiveData<ResponseObject> OnlineWoResult, OnlineNoResult;
    private MutableLiveData<ArrayList<Operation>> onlineOperations = new MutableLiveData<>();
    private MutableLiveData<ResponseObject> updateWOResult = new MutableLiveData<>();
    private Observer<ResponseObject> WorkOrderObserver, NotificationObserver;
    private boolean fetchOpr, fetchNoItems, isObserveWO, isObserveNo;

    public OnlineListViewModel(@NonNull Application application) {
        super(application);
        workOrderHelper = new WorkOrderHelper();
        notificationHelper = new NotificationHelper();
    }

    public MutableLiveData<ResponseObject> getUpdateWOResult() {
        return updateWOResult;
    }

    public void setUpdateWOResult(MutableLiveData<ResponseObject> updateWOResult) {
        this.updateWOResult = updateWOResult;
    }

    /**
     * Setter method needs to set before  fetchWorkOdersOnline call
     *
     * @param fetchOpr this flag tells  whether operations are downloaded along with workorders or not
     */
    public void setFetchOpr(boolean fetchOpr) {
        this.fetchOpr = fetchOpr;
        workOrderHelper.setFetchOpr(fetchOpr);
    }

    /**
     * Setter method needs to set before  fetcNotificationsOnline call
     *
     * @param fetchNoItems this flag tells  whether notificationItems are downloaded along with Notifications or not
     */
    public void setFetchNoItems(boolean fetchNoItems) {
        this.fetchNoItems = fetchNoItems;
        notificationHelper.setFetchNOItems(fetchNoItems);
    }


    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public MutableLiveData<ArrayList<Operation>> getOnlineOperations() {
        return onlineOperations;
    }


    /**
     * @return Online work orders
     */
    public MutableLiveData<ArrayList<WorkOrder>> getOnlineWoList() {
        return onlineWoList;
    }

    public MutableLiveData<ArrayList<Notification>> getOnlineNotificationList() {
        return onlineNotificationList;
    }

    public void setOnlineOperations(MutableLiveData<ArrayList<Operation>> onlineOperations) {
        this.onlineOperations = onlineOperations;
    }

    /**
     * @param mapQuery filed contains online search query parameters with values
     */
    /*Fetching Online Pending Work orders list as ZODataEntity List */
    public void fetchWorkOrdersOnline(HashMap<String, String> mapQuery) {
        try {
            workOrderHelper = new WorkOrderHelper();
            workOrderHelper.setFetchOpr(fetchOpr);
            String finalQuery = workOrderHelper.getOnlineQuery(mapQuery);
            if (!finalQuery.isEmpty())
                workOrderHelper.getWorkOrderOnline(finalQuery);
            else
                setError("Error in Query");
            Log.d(TAG, "fetchWorkOrdersOnline: OnlineQuery" + finalQuery);
            isObserveWO = true;
            OnlineWoResult = workOrderHelper.getOnlineWoEntities();
            WorkOrderObserver = new Observer<ResponseObject>() {
                @Override
                public void onChanged(ResponseObject responseObject) {
                    if (isObserveWO && !responseObject.isError()) {
                        ArrayList<ZODataEntity> zoDataEntities = new ArrayList<>();
                        zoDataEntities = (ArrayList<ZODataEntity>) responseObject.Content();
                        onFetchOnlineWOList(zoDataEntities);
                    } else
                        setError(responseObject.getMessage());
                    isObserveWO = false;
                    //workOrderHelper.getOnlineWoEntities().removeObserver(objectObserver);
                }
            };
            OnlineWoResult.observeForever(WorkOrderObserver);
            /*workOrderHelper.setTaskInterface(new BackgroundTaskInterface() {
                @Override
                public void onTaskPostExecute(ArrayList<ZODataEntity> zoDataEntities, boolean isError, String errorMsg) {
                    if (!isError)
                        onFetchOnlineWOList(zoDataEntities);
                    else
                        setError(errorMsg);
                }

                @Override
                public void onTaskPreExecute() {
                }

                @Override
                public void onTaskProgressUpdate() {
                }
            });
            String finalQuery = workOrderHelper.getQuery(mapQuery);
            if (!finalQuery.isEmpty())
                workOrderHelper.getWorkOrdersOnline(finalQuery);
            else
                setError("Error in Query");*/
        } catch (IllegalArgumentException exception) {
            setError(exception.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            setError(e.getMessage());
        }

    }

    /**
     * @param mapQuery filed contains online search query parameters with values
     */
    /*Fetching the final online query and fetching online notifications pending list as ZODataEntity List*/
    public void fetchNotificationsOnline(HashMap<String, String> mapQuery) {
        try {
            notificationHelper = new NotificationHelper();
            notificationHelper.setFetchNOItems(fetchNoItems);
            OnlineNoResult = notificationHelper.getOnlineNoEntity();
            NotificationObserver = new Observer<ResponseObject>() {
                @Override
                public void onChanged(ResponseObject responseObject) {
                    if (!responseObject.isError()) {
                        ArrayList<ZODataEntity> zoDataEntities = new ArrayList<>();
                        zoDataEntities = (ArrayList<ZODataEntity>) responseObject.Content();
                        onFetchOnlineNOList(zoDataEntities);
                    } else
                        setError(responseObject.getMessage());
                }
            };
            OnlineNoResult.observeForever(NotificationObserver);
            String finalQuery = notificationHelper.getOnlineQuery(mapQuery);
            if (!finalQuery.isEmpty())
                notificationHelper.getOnlineNoEntities(finalQuery);
            else
                setError("Error in Query");
            Log.d(TAG, "fetchNotificationsOnline: OnlineQuery" + finalQuery.toString());
        } catch (IllegalArgumentException exception) {
            setError(exception.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**
     * converting the ZODataEntity list into WorkOrdersList and get the operations of workOrder.
     *
     * @param zoDataEntities ZODataEntity Contains the oDataEntity or EntityValue instance map to
     */
    /*Converting the ZODataEntity list to WorkOrder's list  */
    protected void onFetchOnlineWOList(ArrayList<ZODataEntity> zoDataEntities) {
        ArrayList<WorkOrder> onlineWo = new ArrayList<>();
        operations = new ArrayList<>();
        try {
            EntityValue entityValue;
            EntityValueList oprEntityList;
            for (ZODataEntity entity : zoDataEntities) {
                WorkOrder item = new WorkOrder(entity);
                entityValue = entity.getEntityValue();
                //ArrayList<Operation> workOrderOperations = new ArrayList<>();
                if (fetchOpr) {
                    oprEntityList = entityValue.getEntityType().getProperty("NAVOPERA").getEntityList(entityValue);//Extracting the WorkOrder Operations from WorkOrder
                    ArrayList<Operation> temp=new ArrayList<>();
                    for (EntityValue oprEntity : oprEntityList) {
                        //workOrderOperations.add(new Operation(oprEntity));
                        operations.add(new Operation(oprEntity));
                        temp.add(new Operation(oprEntity));
                    }
                    item.setWorkOrderOperations(temp);
                }
                onlineWo.add(item);
            }
            if (zoDataEntities.isEmpty())
                operations = new ArrayList<>();

            OnlineDataList.getInstance().setOnlineWorkOrderList(onlineWo);
            OnlineDataList.getInstance().setWorkOrdersOperationsList(operations);
            if (OnlineWoResult != null && WorkOrderObserver != null)
                OnlineWoResult.removeObserver(WorkOrderObserver);
            onlineWoList.postValue(onlineWo);
            if (fetchOpr)
                onlineOperations.postValue(operations);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**
     * @param zoDataEntities ZODataEntity Contains the oDataEntity or EntityValue instance map to
     */
    /*Converting the ZODataEntity List to Notification's List and get the Notificaction Items list from Notification*/
    protected void onFetchOnlineNOList(ArrayList<ZODataEntity> zoDataEntities) {
        ArrayList<Notification> onlineNoList = new ArrayList<>();
        try {
            EntityValue entityValue;
            EntityValueList noItemsEntityValues;
            ArrayList<NotificationItem> notificationItemsList=null;
            for (ZODataEntity entity : zoDataEntities) {
                Notification item = new Notification(entity);
                notificationItemsList = new ArrayList<>();
                entityValue = entity.getEntityValue();
                if (fetchNoItems) {
                    noItemsEntityValues = entity.getEntityValue().getEntityType().getProperty("NavNOItem").getEntityList(entityValue);//Extracting the NotificationItems from Notification
                    for (EntityValue itemEntity : noItemsEntityValues) {
                        NotificationItem notificationItem = new NotificationItem(itemEntity);
                        notificationItemsList.add(notificationItem);
                        notificationItems.add(notificationItem);
                    }
                }
                item.setNotificationItems(notificationItemsList);
                onlineNoList.add(item);
            }
            OnlineDataList.getInstance().setOnlineNotificationItems(notificationItems);
            OnlineDataList.getInstance().setOnLineNotifications(onlineNoList);
            onlineNotificationList.setValue(onlineNoList);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
    }


    /**
     * Remove  Global Observers when ever the the ViewModel data is cleared.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        if (OnlineWoResult != null && WorkOrderObserver != null)
            OnlineWoResult.removeObserver(WorkOrderObserver);
        if (OnlineNoResult != null && NotificationObserver != null)
            OnlineNoResult.removeObserver(NotificationObserver);
    }
}
