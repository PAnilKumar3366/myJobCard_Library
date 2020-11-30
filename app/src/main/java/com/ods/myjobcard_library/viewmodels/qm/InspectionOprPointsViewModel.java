package com.ods.myjobcard_library.viewmodels.qm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.qmentities.InspectionPoint;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class InspectionOprPointsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<InspectionPoint>> inspectionOprPointsListLiveData = new MutableLiveData<>();

    public void setInspectionOprPointListLiveData(String InspLotNum, String InspOprNum) {
        inspectionOprPointsListLiveData.setValue(getInspOprPointsLiveData(InspLotNum, InspOprNum));
    }

    public MutableLiveData<ArrayList<InspectionPoint>> getInspectionOprPointsListLiveData() {
        return inspectionOprPointsListLiveData;
    }

    public ArrayList<InspectionPoint> getInspOprPointsLiveData(String inspLotNum, String inspOprNum) {
        try {
            ArrayList<InspectionPoint> inspOprPoints = InspectionPoint.getInspLotOprPoints(inspLotNum, inspOprNum);
            return inspOprPoints;
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return null;
    }
}