package com.ods.myjobcard_library.viewmodels.qm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.qmentities.InspectionLot;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class InspectionLotViewModel extends ViewModel {

    private MutableLiveData<ArrayList<InspectionLot>> inspectionLotLiveData = new MutableLiveData<>();

    public MutableLiveData<ArrayList<InspectionLot>> getInspectionLotLiveData() {
        return inspectionLotLiveData;
    }

    public void setInspectionLotLiveData(String workOrderNum) {
        inspectionLotLiveData.setValue(getInspectionLotLiveData(workOrderNum));
    }

    public ArrayList<InspectionLot> getInspectionLotLiveData(String woNum) {
        try {
            ArrayList<InspectionLot> inspectionLot = InspectionLot.getInspectionLot(woNum);
            return inspectionLot;
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return null;
    }
}
