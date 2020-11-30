package com.ods.myjobcard_library.viewmodels.qm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.qmentities.InspectionCharacteristic;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class InspectionOprPointCharacteristicsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<InspectionCharacteristic>> inspOprPonitCharList = new MutableLiveData<>();

    public void setInspectionOprPointCharListLiveData(String inspLot, String inspOpr, String inspPoint) {
        inspOprPonitCharList.setValue(getInspOprPointsCharListLiveData(inspLot, inspOpr, inspPoint));
    }

    public MutableLiveData<ArrayList<InspectionCharacteristic>> getInspectionOprPointCharListLiveData() {
        return inspOprPonitCharList;
    }

    public ArrayList<InspectionCharacteristic> getInspOprPointsCharListLiveData(String inspLotNum, String inspOprNum, String inspPoint) {
        try {
            ArrayList<InspectionCharacteristic> inspOprPoints = InspectionCharacteristic.getInspOprCharList(inspLotNum, inspOprNum, inspPoint);
            return inspOprPoints;
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return null;
    }
}