package com.ods.myjobcard_library.viewmodels.workorder;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
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
 * This class Provides the  Work Order Related Operations like
 * fetching online workOrders, Update work orders etc
 */


public class WorkOrderHelper {

    private MutableLiveData<ResponseObject> updatedWoResult = new MutableLiveData<>();
    private boolean fetchOpr;
    private MutableLiveData<ResponseObject> onlineWoEntities = new MutableLiveData<>();

    public WorkOrderHelper() {
    }

    /**
     * this setter is called from ViewModel
     *
     * @param fetchOpr flag  relates to fetchOperations from online.
     */
    public void setFetchOpr(boolean fetchOpr) {
        this.fetchOpr = fetchOpr;
    }

    /**
     * @return returns the updatedWOResult Live Data Object
     */
    public MutableLiveData<ResponseObject> getUpdatedWoResult() {
        return updatedWoResult;
    }

    /**
     * @return returns the online Entity live data
     */
    public MutableLiveData<ResponseObject> getOnlineWoEntities() {
        return onlineWoEntities;
    }


    /**
     * Preparing the final query for the online pendingList and returns to calling method.
     *
     * @param queryMap filed contains the Online search parameters and values as Key-Value Pair
     * @return final filter query
     */
    /**/
    public String getQuery(Map<String, String> queryMap) {
        StringBuilder WoFilterQuery = null;
        try {
            String baseQuery = "?$filter=(OnlineSearch eq 'X' and ";
            WoFilterQuery = new StringBuilder();
            WoFilterQuery.append(baseQuery);
            if (queryMap.containsKey("Unassigned"))
                WoFilterQuery.append("Unassigned eq'").append(queryMap.get("Unassigned")).append("' and ");
            if (queryMap.containsKey("Priority"))
                WoFilterQuery.append("Priority eq '").append(queryMap.get("Priority")).append("' and ");
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
            if (fetchOpr)
                WoFilterQuery.append(")&$expand=NAVOPERA");
            else
                WoFilterQuery.append(")");
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
            return "";
        }
        return WoFilterQuery.toString();
    }


    /**
     * Fetching the online Work Orders as ZODataEntity List and set the LiveData
     *
     * @param filterQuery which is the final query and pass the final query to the OnlineAsyncHelper.
     */
    /**/
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
                        //TaskInterface.onTaskPostExecute(entityList, false, "");
                    }
                    onlineWoEntities.postValue(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    //TaskInterface.onTaskPostExecute(new ArrayList<ZODataEntity>(), true, e.getMessage());
                }
            }
        });
        helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * @param workOrder update workorder
     */
    public void UpdateWorkOrderOnline(WorkOrder workOrder) {
        saveOrderOnline(workOrder);
    }

    /**
     * this method creates a task in separate thread for updating the WorkOrder in online. Updates like Edit,Assign and Transfer.
     *
     * @param order update workorder
     */

    private void saveOrderOnline(WorkOrder order) {
        updatedWoResult = new MutableLiveData<>();
        OnlineAsyncHelper updateWO = new OnlineAsyncHelper(order, new OnlineAsyncHelper.Callbacks() {
            @Override
            public void onResult(ResponseObject responseObject) {
                updatedWoResult.postValue(responseObject);
            }
        });
        updateWO.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * This method internally call the SaveEnityOnline method.
     *
     * @param order Delete Order
     */
    public void deleteOrder(WorkOrder order) {

    }
}
