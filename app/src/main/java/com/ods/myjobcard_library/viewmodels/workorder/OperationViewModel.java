package com.ods.myjobcard_library.viewmodels.workorder;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.ods_sdk.entities.ResponseObject;
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

    public Boolean getFetchUnAssingedOpr() {
        return fetchUnAssingedOpr;
    }

    public void setFetchUnAssingedOpr(Boolean fetchUnAssingedOpr) {
        this.fetchUnAssingedOpr = fetchUnAssingedOpr;
    }

    public OperationViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<WorkOrder> getCurrentOpetationWorkOrder() {
        return currentOpetationWorkOrder;
    }

    public void setCurrentOpetationWorkOrder(String workOrdernum) {
        ResponseObject result = WorkOrder.getWorkOrders(ZAppSettings.FetchLevel.Single, workOrdernum, null);
        if (result != null && !result.isError()) {
            ArrayList<WorkOrder> orders = (ArrayList<WorkOrder>) result.Content();
            if (orders != null && orders.size() > 0) {
                currentOpetationWorkOrder.setValue(orders.get(0));
                WorkOrder.setCurrWo(orders.get(0));
            }
        }
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
        operation = Operation.getAllOperations(fetchUnAssingedOpr);
        wosoperationlistLiveData.setValue(operation);
        /*for (int i = 0; i < workOrderList.size(); i++) {
            ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, workOrderList.get(i).getRefId());
            operation = (ArrayList<Operation>) result.Content();
            wowithOpr.addAll(operation);
        }
        if (wowithOpr != null) {
            wosoperationlistLiveData.setValue(wowithOpr);
        }*/

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

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
    }
}
