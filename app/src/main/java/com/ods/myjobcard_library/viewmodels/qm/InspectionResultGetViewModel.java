package com.ods.myjobcard_library.viewmodels.qm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.qmentities.InspectionResultsGet;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class InspectionResultGetViewModel extends ViewModel {

    private MutableLiveData<ArrayList<InspectionResultsGet>> inspResultGetList = new MutableLiveData<>();

    public void setInspectionResultGetLiveData(String inspLot, String inspOpr, String inspPoint) {
        inspResultGetList.setValue(getInspResultGetListLiveData(inspLot, inspOpr, inspPoint));
    }

    public MutableLiveData<ArrayList<InspectionResultsGet>> getInspectionResultGetListLiveData() {
        return inspResultGetList;
    }

    public ArrayList<InspectionResultsGet> getInspResultGetListLiveData(String inspLotNum, String inspOprNum, String inspPoint) {
        try {
            ArrayList<InspectionResultsGet> inspectionResultsGetlist = InspectionResultsGet.getInspResultGet(inspLotNum, inspOprNum, inspPoint);
            return inspectionResultsGetlist;
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return null;
    }
}