package com.ods.myjobcard_library.viewmodels.online;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.StoreHelpers.TableConfigSet;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.client.odata.v4.EntityValue;
import com.sap.client.odata.v4.EntityValueList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnlineWOListViewModel extends ViewModel {

    private static final String TAG = "OnlineWOListViewModel";
    private MutableLiveData<ArrayList<WorkOrder>> onlineWoList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Operation>> onlineOprList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<WorkOrder>> filterListLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Operation>> filterOprListLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    //Single Wo
    private MutableLiveData<WorkOrder> selectedWo = new MutableLiveData<>();
    //Single Opr
    private MutableLiveData<Operation> currentOpr = new MutableLiveData<>();
    //Updated Work order.
    private MutableLiveData<WorkOrder> updatedWorkorder = new MutableLiveData<>();
    private ArrayList<WorkOrder> workOrders = new ArrayList<>();
    private ArrayList<Operation> operations = new ArrayList<>();

    public MutableLiveData<Operation> getCurrentOpr() {
        return currentOpr;
    }

    public MutableLiveData<WorkOrder> getUpdatedWorkorder() {
        return updatedWorkorder;
    }

    public void setUpdatedWorkorder(WorkOrder updatedWorkorder) {
        this.updatedWorkorder.setValue(updatedWorkorder);
    }

    public void setCurrentOpr(Operation opr, String woNum) {
        opr.isOnline = true;
        if (opr.getWorkOrderNum().equals(woNum))
            WorkOrder.getCurrWo().setCurrentOperation(opr);
        currentOpr.setValue(opr);
    }

    public MutableLiveData<WorkOrder> getSelectedWo() {
        return selectedWo;
    }

    public void setSelectedWo(WorkOrder workOrder) {
        if (WorkOrder.getCurrWo() != null) {
            if (workOrder.getWorkOrderNum().equals(WorkOrder.getCurrWo().getWorkOrderNum()))
                workOrder = WorkOrder.getCurrWo();
        }
        WorkOrder.setCurrWo(workOrder);
        workOrder.isOnline = true;
        selectedWo.setValue(workOrder);
    }

    public ArrayList<WorkOrder> getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(ArrayList<WorkOrder> workOrders) {
        this.workOrders = workOrders;
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<Operation> operations) {
        this.operations = operations;
    }

    public MutableLiveData<ArrayList<WorkOrder>> getOnlineWoList() {
        return onlineWoList;
    }

    public void setOnlineWoList(MutableLiveData<ArrayList<WorkOrder>> onlineWoList) {
        this.onlineWoList = onlineWoList;
    }

    public void setOnlineWoList(ArrayList<WorkOrder> onlineList) {
        onlineWoList.setValue(onlineList);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(Boolean isLoading) {
        this.isLoading.setValue(isLoading);
    }

    public MutableLiveData<ArrayList<Operation>> getOnlineOprList() {
        return onlineOprList;
    }

    public void setOnlineOprList(MutableLiveData<ArrayList<Operation>> onlineOprList) {
        this.onlineOprList = onlineOprList;
    }

    public void setOnlineOprList(ArrayList<Operation> onlineOprList) {
        this.onlineOprList.setValue(onlineOprList);
    }

    public void fetchOnlineWorkOrders(String filterQuery, Boolean isWoSearch) {
        String entitySetName;
        if (isWoSearch)
            entitySetName = "WoHeaderSet";
        else entitySetName = "NotificationHeaderSet";
        //filterQueryWo = "?$filter=(OnlineSearch eq 'X' and CreatedOn eq datetime'2020-02-04T00:00:00' and ChangeDtForOrderMaster eq datetime'2020-02-06T00:00:00' and Plant eq 'GB01' and MainWorkCtr eq 'GB01_OP')&$expand=NAVOPERA";
        //filterQueryWo="?$filter=(OnlineSearch eq 'X' and CreatedOn eq datetime'2020-01-21T00:00:00' and ChangeDtForOrderMaster eq datetime'2020-01-22T00:00:00')&$expand=NAVOPERA";
        String resPath = entitySetName + filterQuery;
        try {
            new AsyncTask<Void, Void, ResponseObject>() {

                @Override
                protected ResponseObject doInBackground(Void... voids) {
                    ResponseObject result = DataHelper.getInstance().getEntitiesOnline(resPath, entitySetName, TableConfigSet.getStore(entitySetName));
                    return result;
                }

                @Override
                protected void onPostExecute(ResponseObject responseObject) {
                    super.onPostExecute(responseObject);

                    if (!responseObject.isError()) {

                        Log.d(TAG, "onPostExecute: " + responseObject.Content());
                        operations.clear();
                        workOrders.clear();
                        EntityValueList entityList = (EntityValueList) responseObject.Content();
                        EntityValueList oprEntityList;
                        ArrayList<WorkOrder> onlineworkOrders = new ArrayList<>();
                        ArrayList<Operation> workOrderOperations;
                        for (EntityValue entityValue : entityList) {

                            WorkOrder order = new WorkOrder(entityValue);
                            oprEntityList = entityValue.getEntityType().getProperty("NAVOPERA").getEntityList(entityValue);

                            workOrderOperations = new ArrayList<>();
                            for (EntityValue oprEntity : oprEntityList) {
                                workOrderOperations.add(new Operation(oprEntity));
                                operations.add(new Operation(oprEntity));
                            }
                            order.setWorkOrderOperations(workOrderOperations);
                            onlineworkOrders.add(order);

                            //oprEntityValueList.add( entityValue.getEntityType().getProperty("NAVOPERA").getEntityList(entityValue));
                            Log.d(TAG, "onPostExecute: WorkOrder" + onlineworkOrders.toString());
                        }
                        workOrders.addAll(onlineworkOrders);

                        //updateUI(onlineworkorders);
                    } else {
                        Log.d(TAG, "onPostExecute: " + responseObject.getMessage());
                    }
                    onlineWoList.setValue(workOrders);
                    onlineOprList.setValue(operations);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }

    }

    public MutableLiveData<ArrayList<WorkOrder>> getFilterListLiveData() {
        return filterListLiveData;
    }

    public void setFilterListLiveData(MutableLiveData<ArrayList<WorkOrder>> filterListLiveData) {
        this.filterListLiveData = filterListLiveData;
    }

    public void setFilterListLiveData(HashMap<String, ArrayList<String>> filterHashmap) {
        filterList(filterHashmap);
    }

    private void filterList(HashMap<String, ArrayList<String>> filterHashmap) {
        ArrayList<WorkOrder> filterList = new ArrayList<>();
        filterList.addAll(workOrders);
        List<WorkOrder> workOrderList = new ArrayList<>();
        try {
            for (WorkOrder order : workOrders) {
                if (filterHashmap.containsKey("Priority")) {
                    if (!filterHashmap.get("Priority").contains(order.getPriority())) {
                        filterList.remove(order);
                        continue;
                    }
                }
                if (filterHashmap.containsKey("Status")) {
                    if (!filterHashmap.get("Status").contains(order.getMobileObjStatus())) {
                        filterList.remove(order);
                        continue;
                    }
                }
                if (filterHashmap.containsKey("OrderType")) {
                    if (!filterHashmap.get("OrderType").contains(order.getOrderType())) {
                        filterList.remove(order);
                        continue;
                    }
                }
                if (filterHashmap.containsKey("Workcenter")) {
                    if (!filterHashmap.get("Workcenter").contains(order.getMainWorkCtr())) {
                        filterList.remove(order);
                        continue;
                    }
                }
                if (filterHashmap.containsKey("PersonNumber")) {
                    if (!order.getPersonResponsible().contains("00000000")) {
                        filterList.remove(order);
                        continue;
                    }
                }
                if (filterHashmap.containsKey("Person")) {
                    if (!filterHashmap.get("Person").contains(order.getPersonResponsible())) {
                        filterList.remove(order);
                    }
                }
            }
            filterListLiveData.setValue(filterList);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    private void operationsFilterList(HashMap<String, ArrayList<String>> filterHashmap) {
        ArrayList<Operation> filterList = new ArrayList<>(operations);
        try {
            for (Operation order : operations) {
                if (filterHashmap.containsKey("Plant")) {
                    if (!filterHashmap.get("Plant").contains(order.getPlant())) {
                        filterList.remove(order);
                        continue;
                    }
                }
                if (filterHashmap.containsKey("Status")) {
                    if (!filterHashmap.get("Status").contains(order.getMobileStatus())) {
                        filterList.remove(order);
                        continue;
                    }
                }
                if (filterHashmap.containsKey("OrderType")) {
                    if (!filterHashmap.get("OrderType").contains(order.getOrderType())) {
                        filterList.remove(order);
                        continue;
                    }
                }
                if (filterHashmap.containsKey("WorkCenter")) {
                    if (!filterHashmap.get("WorkCenter").contains(order.getWorkCenter())) {
                        filterList.remove(order);
                        continue;
                    }
                }
                if (filterHashmap.containsKey("PersonNumber")) {
                    if (!order.getPersonnelNo().contains("00000000")) {
                        filterList.remove(order);
                    }
                }
            }
            filterOprListLiveData.setValue(filterList);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public MutableLiveData<ArrayList<Operation>> getFilterOprListLiveData() {
        return filterOprListLiveData;
    }

    public void setFilterOprListLiveData(HashMap<String, ArrayList<String>> filterHashmap) {
        operationsFilterList(filterHashmap);
    }
}
