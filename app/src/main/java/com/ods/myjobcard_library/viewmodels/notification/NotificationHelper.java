package com.ods.myjobcard_library.viewmodels.notification;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.interfaces.BackgroundTaskInterface;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.ods.ods_sdk.utils.OnlineAsyncHelper;
import com.sap.client.odata.v4.EntityValue;
import com.sap.client.odata.v4.EntityValueList;

import java.util.ArrayList;
import java.util.Map;

/*Created By Anil Kumar*/


/**
 * This class helps the Notifications Related Operations like
 * fetching online Notifications, offline Notifications etc
 */
public class NotificationHelper {

    private BackgroundTaskInterface TaskInterface;

    private boolean fetchNOItems;

    public void setFetchNOItems(boolean fetchNOItems) {
        this.fetchNOItems = fetchNOItems;
    }

    private MutableLiveData<ResponseObject> onlineNoEntity = new MutableLiveData<>();

    public MutableLiveData<ResponseObject> getOnlineNoEntity() {
        return onlineNoEntity;
    }

    public NotificationHelper() {
    }

    public void setTaskInterface(BackgroundTaskInterface TaskInterface) {
        this.TaskInterface = TaskInterface;
    }

    /**
     * @param queryMap filed contains the Online search parameters and values as Key-Value Pair
     * @return final filter query
     */
    /*Preparing the final query for the online pendingList and returns to calling method.*/
    public String getQuery(Map<String, String> queryMap) {

        StringBuilder NoFilterQuery = null;
        try {
            NoFilterQuery = new StringBuilder();
            NoFilterQuery.append("?$filter=(OnlineSearch eq 'X' and ");
            if (queryMap.containsKey("Priority"))
                NoFilterQuery.append("Priority eq '").append(queryMap.get("Priority")).append("' and ");
            if (queryMap.containsKey("From"))
                NoFilterQuery.append("CreatedOn eq datetime'").append(queryMap.get("From")).append("' and ");
            if (queryMap.containsKey("To"))
                NoFilterQuery.append("ChangedOn eq datetime'").append(queryMap.get("To")).append("' and ");
            if (queryMap.containsKey("Plant"))
                NoFilterQuery.append("PlanningPlant eq '").append(queryMap.get("Plant")).append("' and ");
            if (queryMap.containsKey("EquipNum"))
                NoFilterQuery.append("Equipment eq '").append(queryMap.get("EquipNum")).append("' and ");
            if (queryMap.containsKey("FuncLocation"))
                NoFilterQuery.append("FunctionalLoc eq '").append(queryMap.get("FuncLocation")).append("' and ");
            if (queryMap.containsKey("MainWorkCtr"))
                NoFilterQuery.append("WorkCenter eq '").append(queryMap.get("MainWorkCtr")).append("' and ");
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

    /**
     * @param filterQuery which is the final query and pass the final query to the OnlineAsyncHelper.
     */
    /*Fetching the online notifications as ZODataEntity List and set the LiveData*/
    public void getNotificationsOnline(String filterQuery) {
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
                        onlineNoEntity.postValue(response);
                        //TaskInterface.onTaskPostExecute(entityList, false, "");
                    } else
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
}
