package com.ods.myjobcard_library.viewmodels.online;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;

import java.util.ArrayList;

public class OnlineOprViewModel extends ViewModel {
    WorkOrder workOrder_single = null;
    private MutableLiveData<Operation> currentOpr = new MutableLiveData<>();
    private MutableLiveData<WorkOrder> currentOprWo = new MutableLiveData<>();
    private MutableLiveData<Operation> updatedOpr = new MutableLiveData<>();

    public MutableLiveData<Operation> getUpdatedOpr() {
        return updatedOpr;
    }

    public void setUpdatedOpr(Operation updatedOpr) {
        this.updatedOpr.setValue(updatedOpr);
    }

    public MutableLiveData<WorkOrder> getCurrentOprWo() {
        return currentOprWo;
    }

    public void setCurrentOprWo(ArrayList<WorkOrder> woList, String woNum) {
        if (workOrder_single != null && workOrder_single.getWorkOrderNum().equalsIgnoreCase(woNum))
            WorkOrder.setCurrWo(workOrder_single);
        else {
            for (WorkOrder workOrder : woList) {
                if (workOrder.getWorkOrderNum().equalsIgnoreCase(woNum)) {
                    workOrder_single = workOrder;
                    WorkOrder.setCurrWo(workOrder_single);
                    break;
                }
            }
        }
        currentOprWo.setValue(WorkOrder.getCurrWo());
    }

    public MutableLiveData<Operation> getCurrentOpr() {
        return currentOpr;
    }

    public void setCurrentOpr(MutableLiveData<Operation> currentOpr) {
        this.currentOpr = currentOpr;
    }

    public void setCurrentOpr(Operation currentOpr) {
        if (WorkOrder.getCurrWo() != null) {
            currentOpr.isOnline = true;
            WorkOrder.getCurrWo().setCurrentOperation(currentOpr);
        }
        this.currentOpr.setValue(currentOpr);
    }
}
