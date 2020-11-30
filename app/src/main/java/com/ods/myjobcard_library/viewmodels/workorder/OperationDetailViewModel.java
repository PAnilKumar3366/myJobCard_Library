package com.ods.myjobcard_library.viewmodels.workorder;

import android.util.Log;
import android.view.Menu;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class OperationDetailViewModel extends ViewModel {
    private static final String TAG = "OperationDetailViewMode";
    private MutableLiveData<Operation> currentOperation = new MutableLiveData<Operation>();
    private MutableLiveData<WorkOrder> currentSelectedWO = new MutableLiveData<WorkOrder>();
    private Menu menu;

    public MutableLiveData<WorkOrder> getCurrentSelectedWO() {
        return currentSelectedWO;
    }

    public void setCurrentSelectedWO(String woNumber) {
        ResponseObject result = WorkOrder.getWorkOrders(ZAppSettings.FetchLevel.Single, woNumber, null);
        if (result != null && !result.isError()) {
            ArrayList<WorkOrder> orders = (ArrayList<WorkOrder>) result.Content();
            if (orders != null && orders.size() > 0) {
                WorkOrder currOrder = orders.get(0);
                if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                    Operation currOperation = null;
                    if (WorkOrder.getCurrWo() != null && WorkOrder.getCurrWo().getWorkOrderNum().equals(woNumber)) {
                        currOperation = WorkOrder.getCurrWo().getCurrentOperation();
                        if (currOperation != null) {
                            currOperation = Operation.getOperation(woNumber, currOperation.getOperationNum(), currOperation.getSubOperation());
                            if (currOperation != null)
                                currOrder.setCurrentOperation(currOperation);
                        }
                    }
                }
                WorkOrder.setCurrWo(currOrder);
                currentSelectedWO.setValue(currOrder);
            }
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public MutableLiveData<Operation> getCurrentOperation() {
        return currentOperation;
    }

    public void setCurrentOperation(String woNum, String oprNum, String subOprNum) {
        Operation operation = Operation.getOperation(woNum, oprNum, subOprNum);
        if (operation != null && !operation.isError()) {
            if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                if (woNum.equals(WorkOrder.getCurrWo().getWorkOrderNum())) {
                    WorkOrder.getCurrWo().setCurrentOperation(operation);
                }
            }
            currentOperation.setValue(operation);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
    }
}
