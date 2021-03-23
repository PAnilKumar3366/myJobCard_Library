package com.ods.myjobcard_library.viewmodels.workorder;

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
 * This class helps the Work Order Related Operations like
 * fetching online workOrders, offline work orders etc
 */


public class WorkOrderHelper {

    public BackgroundTaskInterface TaskInterface;
    protected MutableLiveData<ResponseObject> onlineWoEntities = new MutableLiveData<>();

    public WorkOrderHelper() {
    }


    public WorkOrderHelper(BackgroundTaskInterface taskInterface) {
        TaskInterface = taskInterface;
    }

    public void setTaskInterface(BackgroundTaskInterface taskInterface) {
        TaskInterface = taskInterface;
    }

    /**
     * @param queryMap filed contains the Online search parameters and values as Key-Value Pair
     * @return final filter query
     */
    /*Preparing the final query for the online pendingList and returns to calling method.*/
    public String getQuery(Map<String, String> queryMap) {
        StringBuilder WoFilterQuery = null;
        try {
            String baseQuery = "?$filter=(OnlineSearch eq 'X' and ";
            WoFilterQuery = new StringBuilder();
            WoFilterQuery.append(baseQuery);
            if (queryMap.containsKey("Unassigned"))
                WoFilterQuery.append("Unassigned eq'").append(queryMap.get("Unassigned")).append("' and ");
            if (queryMap.containsKey("From"))
                WoFilterQuery.append("CreatedOn eq datetime'").append(queryMap.get("From")).append("' and ");
            if (queryMap.containsKey("To"))
                WoFilterQuery.append("ChangeDtForOrderMaster eq datetime'").append(queryMap.get("To")).append("' and ");
            if (queryMap.containsKey("Plant"))
                WoFilterQuery.append("Plant eq '").append(queryMap.get("Plant")).append("' and ");
            if (queryMap.containsKey("EquipNum"))
                WoFilterQuery.append("EquipNum eq '").append(queryMap.get("EquipNum")).append("' and ");
            if (queryMap.containsKey("FuncLocation"))
                WoFilterQuery.append("FuncLocation eq '").append(queryMap.get("FuncLocation")).append("' and ");
            if (queryMap.containsKey("MainWorkCtr"))
                WoFilterQuery.append("MainWorkCtr eq '").append(queryMap.get("MainWorkCtr")).append("' and ");
            String finalQuery = " " + WoFilterQuery.toString();
            WoFilterQuery.delete(finalQuery.length() - 6, WoFilterQuery.length());
            WoFilterQuery.append(")&$expand=NAVOPERA");
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
            return "";
        }
        return WoFilterQuery.toString();
    }


    public MutableLiveData<ResponseObject> getOnlineWoEntities() {
        return onlineWoEntities;
    }

    /**
     * @param filterQuery which is the final query and pass the final query to the OnlineAsyncHelper.
     */
    /*Fetching the online Work Orders as ZODataEntity List and set the CallBack*/
    public void getWorkOrdersOnline(String filterQuery) {
        ArrayList<ZODataEntity> entityList = new ArrayList<>();
        String resPath = ZCollections.WO_COLLECTION + filterQuery;
        OnlineAsyncHelper helper = new OnlineAsyncHelper(resPath, ZCollections.WO_COLLECTION, false, new OnlineAsyncHelper.Callbacks() {
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
                        onlineWoEntities.postValue(response);
                        //TaskInterface.onTaskPostExecute(entityList, false, "");
                    } else {
                        //TaskInterface.onTaskPostExecute(entityList, true, response.getMessage());
                        onlineWoEntities.postValue(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //TaskInterface.onTaskPostExecute(new ArrayList<ZODataEntity>(), true, e.getMessage());
                }
            }
        });
        helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
