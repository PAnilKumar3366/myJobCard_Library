package com.ods.myjobcard_library.viewmodels.workorder;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.StatusChangeLog;
import com.ods.myjobcard_library.entities.appsettings.StatusCategory;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class WorkOrderDetailViewModel extends ViewModel {

    private static final String TAG = "WorkOrderDetailViewMode";

    private MutableLiveData<WorkOrder> currentWorkOrder = new MutableLiveData<>();

    public LiveData<WorkOrder> getCurrentWorkOrder() {
        return currentWorkOrder;
    }

    public void setCurrentWorkOrder(String workOrderNum) {
        ResponseObject result = WorkOrder.getWorkOrders(ZAppSettings.FetchLevel.Single, workOrderNum, null);
        if (result != null && !result.isError()) {
            ArrayList<WorkOrder> orders = (ArrayList<WorkOrder>) result.Content();
            if (orders != null && orders.size() > 0) {
                WorkOrder currOrder = orders.get(0);
                if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                    Operation currOperation = null;
                    if (WorkOrder.getCurrWo() != null && WorkOrder.getCurrWo().getWorkOrderNum().equals(workOrderNum)) {
                        currOperation = WorkOrder.getCurrWo().getCurrentOperation();
                        if (currOperation != null) {
                            currOperation = Operation.getOperation(workOrderNum, currOperation.getOperationNum(), currOperation.getSubOperation());
                            if (currOperation != null)
                                currOrder.setCurrentOperation(currOperation);
                        }
                    }

                }
                WorkOrder.setCurrWo(currOrder);
                currentWorkOrder.setValue(currOrder);
            }
        }
    }

    public void setCurrentSelectedOperation(String selectedOperation, String subOperationNum) {

        Operation currOperation = Operation.getOperation(WorkOrder.getCurrWo().getWorkOrderNum(), selectedOperation, subOperationNum);
        if (currOperation != null)
            WorkOrder.getCurrWo().setCurrentOperation(currOperation);
        currentWorkOrder.setValue(WorkOrder.getCurrWo());
    }

    public void saveStatusChangeLog(StatusCategory status, String workOrderNum, String operation, GregorianCalendar changedTime, Location location) {
        StatusChangeLog.saveStatusChangeLog(workOrderNum, operation, status, changedTime, location);
    }

    public void updateStatusChangeLog(StatusChangeLog changeLog) {
        if (changeLog != null) {
            changeLog.setMode(ZAppSettings.EntityMode.Update);
            changeLog.setIsConsidered(true);
            changeLog.SaveToStore(false);
        }
    }

    public StatusChangeLog getStatusChangeLog(String workOrder, String operation, StatusCategory newStatus) {
        return StatusChangeLog.getObjectStatusTimeLog(workOrder, operation, newStatus);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
    }
}
