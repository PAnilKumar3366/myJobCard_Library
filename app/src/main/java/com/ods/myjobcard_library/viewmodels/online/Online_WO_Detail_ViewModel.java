package com.ods.myjobcard_library.viewmodels.online;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;

import java.util.ArrayList;

public class Online_WO_Detail_ViewModel extends ViewModel {
    private MutableLiveData<WorkOrder> selectedWorkOrder = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Operation>> woOprList = new MutableLiveData<>();
    private MutableLiveData<WorkOrder> updatedWo = new MutableLiveData<>();

    public MutableLiveData<WorkOrder> getUpdatedWo() {
        return updatedWo;
    }

    public void setUpdatedWo(WorkOrder updatedWo) {
        this.updatedWo.setValue(updatedWo);
    }

    public MutableLiveData<WorkOrder> getSelectedWorkOrder() {
        return selectedWorkOrder;
    }

    public void setSelectedWorkOrder(WorkOrder workOrder) {
        if (workOrder != null) {
            workOrder.isOnline = true;
            WorkOrder.setCurrWo(workOrder);
        }
        selectedWorkOrder.setValue(workOrder);
    }

    public MutableLiveData<ArrayList<Operation>> getWoOprList() {
        return woOprList;
    }

    public void setWoOprList(ArrayList<Operation> woOprList) {
        this.woOprList.setValue(woOprList);
    }
}
