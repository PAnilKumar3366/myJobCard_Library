package com.ods.myjobcard_library.viewmodels.workorder;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.UnAssignedOperation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.myjobcard_library.viewmodels.UnAssignedOperationsHelper;
import com.ods.myjobcard_library.viewmodels.WorkOrderHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;
import java.util.List;

public class OperationViewModel extends BaseViewModel {

    private static final String TAG = "OperationViewModel";
    private MutableLiveData<ArrayList<Operation>> operationlistLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Operation>> wosoperationlistLiveData = new MutableLiveData<>();
    private MutableLiveData<Operation> currentOperation = new MutableLiveData<>();
    private MutableLiveData<WorkOrder> currentOpetationWorkOrder = new MutableLiveData<WorkOrder>();
    protected Boolean fetchUnAssingedOpr = false;
    private MutableLiveData<ArrayList<UnAssignedOperation>> UnAssignedOperationList = new MutableLiveData<>();
    private UnAssignedOperationsHelper unAssignedOperationsHelper;
    protected MutableLiveData<UnAssignedOperation> selectedUnOpr = new MutableLiveData<>();
    public int getTotalOprCount;


    public OperationViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<UnAssignedOperation> getSelectedUnOpr() {
        return selectedUnOpr;
    }

    public Boolean getFetchUnAssingedOpr() {
        return fetchUnAssingedOpr;
    }

    public MutableLiveData<ArrayList<UnAssignedOperation>> getUnAssignedOperationList() {
        return UnAssignedOperationList;
    }

    public MutableLiveData<WorkOrder> getCurrentOpetationWorkOrder() {
        return currentOpetationWorkOrder;
    }

    public int getGetTotalOprCount() {
        return Operation.getAllOperationCount();
    }

    public void setCurrentOpetationWorkOrder(String orderNum) {
        WorkOrder currOrder = null;
        WorkOrderHelper workOrderHelper = new WorkOrderHelper();
        try {
            ZODataEntity zoDataEntity = workOrderHelper.fetchSingleWorkOrder(orderNum);
            currOrder = onFetchSingleWoEntity(zoDataEntity);
            WorkOrder.setCurrWo(currOrder);
            currentOpetationWorkOrder.setValue(currOrder);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
       /* ResponseObject result = WorkOrder.getWorkOrders(ZAppSettings.FetchLevel.Single, workOrdernum, null);
        if (result != null && !result.isError()) {
            ArrayList<WorkOrder> orders = (ArrayList<WorkOrder>) result.Content();
            if (orders != null && orders.size() > 0) {
                currentOpetationWorkOrder.setValue(orders.get(0));
                WorkOrder.setCurrWo(orders.get(0));
            }
        }*/
    }

    public MutableLiveData<ArrayList<Operation>> getOperationlistLiveData() {
        return operationlistLiveData;
    }

    public void setOperationlistLiveData(WorkOrder workOrder) {
        ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, workOrder.getRefId());
        ArrayList<Operation> orders = (ArrayList<Operation>) result.Content();
        if (orders != null && orders.size() > 0) {
            operationlistLiveData.setValue(orders);
        }
    }

    public MutableLiveData<List<Operation>> getWosOperationlistLiveData() {
        return wosoperationlistLiveData;
    }

    public void fetchAllOperations(boolean fetchUnAssingedOpr) {

    }

    public void setAllWorkOrdersOprerationLiveData(List<WorkOrder> workOrderList) {
        ArrayList<Operation> operation = null;
        List<Operation> wowithOpr = new ArrayList<>();
       /* operation = Operation.getAllOperations(fetchUnAssingedOpr);
        wosoperationlistLiveData.setValue(operation);*/
        try {
            for (int i = 0; i < workOrderList.size(); i++) {
                ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, workOrderList.get(i).getRefId());
                operation = (ArrayList<Operation>) result.Content();
                wowithOpr.addAll(operation);
            }

        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            Log.e(TAG, "setAllWorkOrdersOprerationLiveData: ", e);
        }
        wosoperationlistLiveData.setValue(wowithOpr);
    }

    public MutableLiveData<Operation> getCurrentOperation() {
        return currentOperation;
    }

    public void setCurrentOperation(String workOrderNum, String operationNum, String subOperationNum) {
        try {
            Operation operation=fetchSingleOperation(workOrderNum,operationNum,subOperationNum);
            currentOperation.setValue(operation);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        /*Operation operation = Operation.getOperation(workOrderNum, operationNum, subOperationNum);

        if (operation != null) {
            if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                if (workOrderNum.equals(WorkOrder.getCurrWo().getWorkOrderNum())) {
                    WorkOrder.getCurrWo().setCurrentOperation(operation);
                }
            }
            currentOperation.setValue(operation);
        }*/
    }

    /**
     * This method is trigger form UI module and it is used to fetch the UnAssignedOperations from UnAssingedOperationSet entity.
     * After fetching the unAssignedOperations from Offline it set the resulted list into LiveData.
     */
    public void setFetchUnAssingedOprList() {
        if (unAssignedOperationsHelper == null)
            unAssignedOperationsHelper = new UnAssignedOperationsHelper();
        ArrayList<UnAssignedOperation> unAssignedOperationsList = onFetchUnAssignedOperationsList(unAssignedOperationsHelper.fetchUnAssignedOprlist());
        UnAssignedOperationList.setValue(unAssignedOperationsList);
    }

    /**
     * This method is trigger form UI module and it is used to fetch the UnAssignedOperation from UnAssingedOperationSet entity.
     * After fetching the unAssignedOperation from Offline it set the resulted objected into LiveData.
     *
     * @param OrderNum
     * @param OprNum
     */
    public void setCurrentUnAssignedOpr(String OrderNum, String OprNum, String subOprNum) {
        if (unAssignedOperationsHelper == null)
            unAssignedOperationsHelper = new UnAssignedOperationsHelper();
        UnAssignedOperation unAssignedOperation = onFetchSinlgeUnAssignedOpr(unAssignedOperationsHelper.fetchSingleUnAssignedOpr(OrderNum, OprNum, subOprNum));
        selectedUnOpr.setValue(unAssignedOperation);
    }

    /**
     * This method is used to convert the ZODataEntities into UnAssigned Operations
     *
     * @param zoDataEntities List of ZODataUNAssignedOperationsEntities.
     * @return List of UnAssignedOperations.
     */
    protected ArrayList<UnAssignedOperation> onFetchUnAssignedOperationsList(ArrayList<ZODataEntity> zoDataEntities) {
        ArrayList<UnAssignedOperation> unAssignedOperations = new ArrayList<>();
        try {
            for (ZODataEntity zoDataEntity : zoDataEntities) {
                UnAssignedOperation unAssignedOperation = new UnAssignedOperation(zoDataEntity);
                unAssignedOperations.add(unAssignedOperation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            Log.e(TAG, "onFetchUnAssignedOperationsList: ", e);
        }

        return unAssignedOperations;
    }

    /**
     * This method is used to convert the ZODataEntity into UnAssigned Operation Object
     *
     * @param zoDataEntity
     * @return UnAssignedOperation.
     */
    protected UnAssignedOperation onFetchSinlgeUnAssignedOpr(ZODataEntity zoDataEntity) {
        UnAssignedOperation unAssignedOperation = null;
        if (zoDataEntity != null)
            unAssignedOperation = new UnAssignedOperation(zoDataEntity);
        return unAssignedOperation;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
    }
}
