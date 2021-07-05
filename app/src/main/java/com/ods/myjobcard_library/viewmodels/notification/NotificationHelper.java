package com.ods.myjobcard_library.viewmodels.notification;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.ConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.ods.ods_sdk.utils.OnlineAsyncHelper;
import com.sap.client.odata.v4.EntityValue;
import com.sap.client.odata.v4.EntityValueList;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*Created By Anil Kumar*/


/**
 * This class helps the Notifications Related Operations like
 * fetching online Notifications, offline Notifications etc
 */
public class NotificationHelper {



    private boolean fetchNOItems;

    private MutableLiveData<ResponseObject> updatedNoResult;

    public MutableLiveData<ResponseObject> getUpdatedNoResult() {
        return updatedNoResult;
    }

    public void setFetchNOItems(boolean fetchNOItems) {
        this.fetchNOItems = fetchNOItems;
    }

    private MutableLiveData<ResponseObject> onlineNoEntity = new MutableLiveData<>();

    public MutableLiveData<ResponseObject> getOnlineNoEntity() {
        return onlineNoEntity;
    }

    private ArrayList<ZODataEntity> zoDataNotificationListEntities;

    private ZODataEntity zoDataEntity;

    public NotificationHelper() {
    }

    /**
     * Preparing the final query for the online List and returns to calling method.
     *
     * @param queryMap filed contains the Online search parameters and values as Key-Value Pair
     * @return final filter query
     */
    /**/
    private String getQuery(Map<String, String> queryMap) {

        StringBuilder NoFilterQuery = null;
        try {
            NoFilterQuery = new StringBuilder();
            NoFilterQuery.append("?$filter=(OnlineSearch eq 'X' and ");
            if (queryMap.containsKey("Priority"))
                NoFilterQuery.append("Priority eq '").append(queryMap.get("Priority")).append("' and ");
            if (queryMap.containsKey("Plant"))
                NoFilterQuery.append("PlanningPlant eq '").append(queryMap.get("Plant")).append("' and ");
            if (queryMap.containsKey("EquipNum"))
                NoFilterQuery.append("Equipment eq '").append(queryMap.get("EquipNum")).append("' and ");
            if (queryMap.containsKey("FuncLocation"))
                NoFilterQuery.append("FunctionalLoc eq '").append(queryMap.get("FuncLocation")).append("' and ");
            if (queryMap.containsKey("MainWorkCtr"))
                NoFilterQuery.append("WorkCenter eq '").append(queryMap.get("MainWorkCtr")).append("' and ");
            if (queryMap.containsKey("EnteredBy"))
                NoFilterQuery.append("EnteredBy eq '").append(queryMap.get("EnteredBy")).append("' and ");
            if (queryMap.containsKey("From"))
                NoFilterQuery.append("CreatedOn eq datetime'").append(queryMap.get("From")).append("' and ");
            if (queryMap.containsKey("To"))
                NoFilterQuery.append("ChangedOn eq datetime'").append(queryMap.get("To")).append("' and ");

            String finalQuery = " " + NoFilterQuery.toString();
            NoFilterQuery.delete(finalQuery.length() - 6, NoFilterQuery.length());
            if (fetchNOItems)
                NoFilterQuery.append(")&$expand=NavNOItem");
            else
                NoFilterQuery.append(")");
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
            return "";
        }
        return NoFilterQuery.toString();
    }

    public String getOnlineQuery(Map<String, String> mapQuery) {
        return getQuery(mapQuery);
    }

    /**
     * Fetching  online notifications as ZODataEntity List and set the LiveData
     *
     * @param filterQuery which is the final query and pass the final query to the OnlineAsyncHelper.
     */
    /**/
    public void getOnlineNoEntities(String filterQuery) {
        ArrayList<ZODataEntity> entityList = new ArrayList<>();
        String resPath = ZCollections.NOTIFICATION_COLLECTION + filterQuery;
        OnlineAsyncHelper helper = new OnlineAsyncHelper(resPath, ZCollections.NOTIFICATION_COLLECTION, false, new OnlineAsyncHelper.Callbacks() {
            @Override
            public void onResult(ResponseObject response) {
                try {
                    if (response != null && !response.isError()) {
                        EntityValueList entities = (EntityValueList) response.Content();
                        for (EntityValue entity : entities) {
                            ZODataEntity item = new ZODataEntity(entity);
                            entityList.add(item);
                        }
                        response.setContent(entityList);

                        //TaskInterface.onTaskPostExecute(entityList, false, "");
                    }
                    onlineNoEntity.postValue(response);
                    //TaskInterface.onTaskPostExecute(entityList, true, response.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    //TaskInterface.onTaskPostExecute(entityList, true, e.getMessage());
                }
            }
        });
        helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void getOnlineNotifications(String finalQuery) {
        getOnlineNoEntities(finalQuery);
    }

    /**
     * This method calls the saveEntityOnline Method.
     *
     * @param notification Updated Notification.
     */
    public void UpdateNotificationOnline(Notification notification) {
        saveNotificationOnline(notification);
    }

    /**
     * This method updates the Notification in online  asynchronously
     *
     * @param notification Updated Notification.
     */
    private void saveNotificationOnline(Notification notification) {
        try {
            updatedNoResult = new MutableLiveData<>();
            OnlineAsyncHelper updateWO = new OnlineAsyncHelper(notification, new OnlineAsyncHelper.Callbacks() {
                @Override
                public void onResult(ResponseObject responseObject) {
                    updatedNoResult.postValue(responseObject);
                }
            });
            updateWO.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
            updatedNoResult.postValue(new ResponseObject(ConfigManager.Status.Error, e.getMessage(), null));
        }
    }

    /**
     * This method deletes the notifications in online.
     *
     * @param notification delete notification
     */
    public void deleteNotification(Notification notification) {

    }

    /** method for getting all notification based on the fetch level
     * @param fetchLevel
     * @param hierarchy
     * @param notificationNum
     * @param orderByCriteria
     * @param isForWO
     * @return
     */
    public ArrayList<ZODataEntity> getNotifications(ZAppSettings.FetchLevel fetchLevel, ZAppSettings.Hierarchy hierarchy, String notificationNum, String orderByCriteria, boolean isForWO) {
        ResponseObject result = null;
        String resourcePath = null;
        String strOrderBy = "&$orderby=";
        String strOrderByURI = null;
        String strEntitySet = null;
        ZConfigManager.Fetch_Object_Type notifType;
        boolean fetchAddress = false;

        try {
            if (orderByCriteria == null || orderByCriteria.isEmpty()) {
                orderByCriteria = "Notification";
            }
            strOrderByURI = strOrderBy + orderByCriteria;
            if (isForWO) {
                strEntitySet = ZCollections.WO_NOTIFICATION_COLLECTION;
                notifType = ZConfigManager.Fetch_Object_Type.WONotification;
            } else {
                strEntitySet = ZCollections.NOTIFICATION_COLLECTION;
                notifType = ZConfigManager.Fetch_Object_Type.Notification;
            }
            switch (fetchLevel) {
                case ListMap:
                    resourcePath = strEntitySet + "?$select=Notification,NotificationType,SystemStatus,Priority,ShortText,Breakdown,NotifDate,PostalCode,NotifTime,MobileStatus,Equipment,FunctionalLoc,TempID,Partner,PlannerGroup,MaintPlant,PlanningPlant,EnteredBy,LocationAddress,RequiredStartDate,RequiredEndDate" + strOrderByURI;
                    fetchAddress = true;
                    break;
                case List:
                    resourcePath = strEntitySet + "?$select=Notification,NotificationType,SystemStatus,Priority,ShortText,Breakdown,NotifDate,PostalCode,NotifTime,MobileStatus,Equipment,FunctionalLoc,TempID,Partner,PlannerGroup,MaintPlant,PlanningPlant,EnteredBy,RequiredStartDate,RequiredEndDate" + strOrderByURI;
                    break;
                case Header:
                    resourcePath = strEntitySet;
                    break;
                case Single:
                    if (notificationNum != null && notificationNum.length() > 0) {
//                        resourcePath = strEntitySet + "?$filter=(Notification%20eq%20%27"+ NotificationNum +"%27)&$expand="+strExpandQuery;
                        resourcePath = strEntitySet + "?$filter=(Notification%20eq%20%27" + notificationNum + "%27)";
                        fetchAddress = true;
                    }
                    break;
                case SingleWithItemCauses:
                    if (notificationNum != null && notificationNum.length() > 0) {
//                        resourcePath = strEntitySet + "?$filter=(Notification%20eq%20%27"+ NotificationNum +"%27)&$expand="+strExpandQuery;
                        resourcePath = strEntitySet + "?$filter=(Notification%20eq%20%27" + notificationNum + "%27)";
                        fetchAddress = true;
                    }
                    break;
                case All:
//                    resourcePath = strEntitySet + "?$expand="+strExpandQuery;
                    resourcePath = strEntitySet;
                    fetchAddress = true;
                    break;
                default:
//                    resourcePath = strEntitySet + "?$expand="+strExpandQuery;
                    resourcePath = strEntitySet;
                    fetchAddress = true;
                    break;
            }
            result = DataHelper.getInstance().getEntities(strEntitySet, resourcePath);
            zoDataNotificationListEntities=new ArrayList<>();
            if (result != null && !result.isError() && !fetchLevel.equals(ZAppSettings.FetchLevel.Count)) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    zoDataNotificationListEntities.add(zoDataEntity);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return zoDataNotificationListEntities;
    }

    /** method for getting notification by selecting required filtered properties
     * @param filterQuery
     * @param fetchLevel
     * @param orderByCriteria
     * @return
     */
    protected ArrayList<ZODataEntity> getFilteredNotifications(@NonNull String filterQuery, ZAppSettings.FetchLevel fetchLevel, String orderByCriteria) {
        ResponseObject result = null;
        String entitySetName = ZCollections.NOTIFICATION_COLLECTION;
        String resPath = entitySetName;
        String orderByUrl = "$orderby=";
        boolean fetchAddress = false;
        try {
            if (orderByCriteria == null || orderByCriteria.isEmpty()) {
                orderByCriteria = "Notification";
            }
            orderByUrl += orderByCriteria;
            if (!filterQuery.isEmpty()) {
                if (fetchLevel == ZAppSettings.FetchLevel.Count)
                    resPath += "/$count" + filterQuery + "&";
                else
                    resPath += filterQuery + "&";
            } else
                resPath += "?";
            switch (fetchLevel) {
                case ListMap:
                    resPath += "$select=Notification,NotificationType,SystemStatus,Priority,ShortText,Breakdown,NotifDate,PostalCode,NotifTime,MobileStatus,Equipment,FunctionalLoc,TempID,LocationAddress,Partner,PlannerGroup,MaintPlant,PlanningPlant,EnteredBy,RequiredStartDate,RequiredEndDate" + "&" + orderByUrl;
                    fetchAddress = true;
                    break;
                case List:
                    resPath += "$select=Notification,NotificationType,SystemStatus,Priority,ShortText,Breakdown,NotifDate,PostalCode,NotifTime,MobileStatus,Equipment,FunctionalLoc,TempID,Partner,EnteredBy,PlannerGroup,MaintPlant,PlanningPlant,RequiredStartDate,RequiredEndDate" + "&" + orderByUrl;
                    break;
                default:
                    resPath += orderByUrl;
                    fetchAddress = true;
                    break;
            }
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            zoDataNotificationListEntities=new ArrayList<>();
            if (result != null && !result.isError() && !fetchLevel.equals(ZAppSettings.FetchLevel.Count)) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    zoDataNotificationListEntities.add(zoDataEntity);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return zoDataNotificationListEntities;
    }

    /** fetching single odata notification entity from the offline store by passing selected notification number
     * @param notifiNum
     * @param isWONotifi
     * @return
     */
    public ZODataEntity getSingleNotification(String notifiNum,boolean isWONotifi){
        ResponseObject result = null;
        String resourcePath = null;
        String strEntitySet = null;
        try {
            if (isWONotifi)
                strEntitySet = ZCollections.WO_NOTIFICATION_COLLECTION;
            else
                strEntitySet = ZCollections.NOTIFICATION_COLLECTION;
            if (notifiNum != null && notifiNum.length() > 0) {
                resourcePath = strEntitySet + "?$filter=(Notification eq '" + notifiNum + "')";
            }
            result = DataHelper.getInstance().getEntities(strEntitySet, resourcePath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    zoDataEntity = new ZODataEntity(entity);
                }
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return zoDataEntity;
    }

}
