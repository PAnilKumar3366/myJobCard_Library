package com.ods.myjobcard_library.viewmodels.workorder;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.StatusChangeLog;
import com.ods.myjobcard_library.entities.appsettings.StatusCategory;
import com.ods.myjobcard_library.entities.attachment.UploadAttachmentContent;
import com.ods.myjobcard_library.entities.attachment.WorkOrderAttachment;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class WorkOrderListViewModel extends BaseViewModel {

    private static final String TAG = "WorkOrderListViewModel";
    ArrayList<UploadAttachmentContent> uploadList;
    ArrayList<WorkOrderAttachment> downloadLists;
    ResponseObject result;
    private WorkOrdersListLiveData woLiveData;
    private MutableLiveData<WorkOrder> currentWorkOrder = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();


    public WorkOrderListViewModel(@NonNull Application application) {
        super(application);
        woLiveData = new WorkOrdersListLiveData(application);

    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(Boolean isLoading) {
        this.isLoading.setValue(isLoading);
    }

    public LiveData<List<WorkOrder>> getWorkOrderList() {
        return woLiveData;
    }

    public void setWOList() {
        woLiveData.loadData();
        //woLiveData.setFilterQuery(filterQueryWo);
        //woLiveData.loadData();
    }

    public void setFilterQuery(String filterQuery) {
        woLiveData.setFilterQuery(filterQuery);
        // woLiveData.loadData();
    }

    public ArrayList<WorkOrder> getWorkOrdersList() {
        return woLiveData.getWorkOrdersList();
    }

    public LiveData<WorkOrder> getCurrentWorkOrder() {
        return currentWorkOrder;
    }

    public void setCurrentWorkOrder(String workOrderNum) {
        try {
            Operation currentOpr = null;
            WorkOrder currentOrder = null;
            if (currentWorkOrder.getValue() != null && currentWorkOrder.getValue().getWorkOrderNum().equalsIgnoreCase(workOrderNum)) {
                currentOrder = currentWorkOrder.getValue();
                currentOpr = currentOrder.getCurrentOperation();
                    /*if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                        if (currentOrder.getCurrentOperation() != null)
                            currentOpr = currentOrder.getCurrentOperation();
                    }*/
            }
            currentOrder = fetchSingleWorkOrder(workOrderNum);
            if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED && currentOpr != null) {
                currentOpr = fetchSingleOperation(workOrderNum, currentOpr.getOperationNum(), currentOpr.getSubOperation());
                currentOrder.setCurrentOperation(currentOpr);
            }
            currentWorkOrder.setValue(currentOrder);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setCurrentWorkOrder: ", e);
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            currentWorkOrder.setValue(fetchSingleWorkOrder(workOrderNum));
        }
        /*WorkOrder currentOrder = fetchSingleWorkOrder(workOrderNum);
        currentWorkOrder.setValue(currentOrder);*/
       /* ResponseObject result = WorkOrder.getWorkOrders(ZAppSettings.FetchLevel.Single, workOrderNum, null);
        if (result != null && !result.isError()) {
            ArrayList<WorkOrder> orders = (ArrayList<WorkOrder>) result.Content();
            if (orders != null && orders.size() > 0) {
                WorkOrder currOrder = orders.get(0);
                if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                    Operation currOperation = null;
                    if (WorkOrder.getCurrWo() != null && workOrderNum.equals(WorkOrder.getCurrWo().getWorkOrderNum())) {
                        currOperation = WorkOrder.getCurrWo().getCurrentOperation();
                        if (currOperation != null) {
                            currOperation = Operation.getOperation(workOrderNum, currOperation.getOperationNum(), currOperation.getSubOperation());
                            if (currOperation != null)
                                currOrder.setCurrentOperation(currOperation);
                        }
                    } else {
                        ResponseObject results = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, workOrderNum);
                        ArrayList<Operation> operationArrayList = (ArrayList<Operation>) results.Content();
                        if (operationArrayList.size() > 0 && operationArrayList != null)
                            currOrder.setCurrentOperation(operationArrayList.get(0));

                    }
                }

                WorkOrder.setCurrWo(currOrder);
                currentWorkOrder.setValue(currOrder);
            }
        }*/
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
