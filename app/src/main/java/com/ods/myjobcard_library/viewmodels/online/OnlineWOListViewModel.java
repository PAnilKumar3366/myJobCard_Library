package com.ods.myjobcard_library.viewmodels.online;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnlineWOListViewModel extends BaseViewModel {

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

    private MutableLiveData<ArrayList<String>> onlineWoLongText = new MutableLiveData<>();

    public MutableLiveData<Operation> getCurrentOpr() {
        return currentOpr;
    }


    public OnlineWOListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<String>> getOnlineWoLongText() {
        return onlineWoLongText;
    }

    public void setOnlineWoLongText(MutableLiveData<ArrayList<String>> onlineWoLongText) {
        this.onlineWoLongText = onlineWoLongText;
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

    public MutableLiveData<WorkOrder> getUpdatedWorkorder() {
        return updatedWorkorder;
    }

    public void setUpdatedWorkorder(WorkOrder updatedWorkorder) {
        this.updatedWorkorder.setValue(updatedWorkorder);
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
                        continue;
                    }
                }
                if (filterHashmap.containsKey("CreatedByMe")) {                           //Newley Added by Anil.
                    ArrayList<String> createdBy = filterHashmap.get("CreatedByMe");
                    if (createdBy.get(0).contains(order.getEnteredBy().toUpperCase()))
                        filterList.remove(order);
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

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
