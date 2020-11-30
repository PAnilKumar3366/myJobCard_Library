package com.ods.myjobcard_library.viewmodels.qm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.qmentities.InspectionOperation;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class InspectionLotOprViewModel extends ViewModel {

    private MutableLiveData<ArrayList<InspectionOperation>> inspectionOprListLiveData = new MutableLiveData<>();

    public void setInspectionOprListLiveData(String inspLotNum, String orderNum, String oprNum) {
        inspectionOprListLiveData.setValue(getInspOperationsLiveData(inspLotNum, orderNum, oprNum));
    }

    public MutableLiveData<ArrayList<InspectionOperation>> getInspectionOprListLiveData() {
        return inspectionOprListLiveData;
    }

    public ArrayList<InspectionOperation> getInspOperationsLiveData(String inspectionLotNum, String OrderNum, String OprNum) {
        try {
            ArrayList<InspectionOperation> inspectionOperations = InspectionOperation.getInspLotOperations(inspectionLotNum, OrderNum, OprNum);
            return inspectionOperations;
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return null;
    }
}
