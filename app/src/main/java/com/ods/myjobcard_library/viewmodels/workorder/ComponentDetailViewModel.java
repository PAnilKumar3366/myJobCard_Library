package com.ods.myjobcard_library.viewmodels.workorder;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.Components;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class ComponentDetailViewModel extends ViewModel {

    private static final String TAG = "ComponentDetailViewMode";
    private MutableLiveData<Components> selectedComponent = new MutableLiveData<>();
    private MutableLiveData<WorkOrder> currentSelectedWO = new MutableLiveData<WorkOrder>();

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
                    if (WorkOrder.getCurrWo() != null) {
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

    public MutableLiveData<Components> getSelectedComponent() {
        return selectedComponent;
    }

    public void setSelectedComponent(WorkOrder workOrder, String selectedComponentNum) {
        ResponseObject result = Components.getComponents(ZAppSettings.FetchLevel.Single, workOrder.getWorkOrderNum(), null, selectedComponentNum, null);
        if (!result.isError()) {
            Components currSelectedComp = ((ArrayList<Components>) result.Content()).get(0);
            selectedComponent.setValue(currSelectedComp);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
    }
}
