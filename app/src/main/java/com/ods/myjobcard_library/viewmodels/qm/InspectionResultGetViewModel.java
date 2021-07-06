package com.ods.myjobcard_library.viewmodels.qm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.qmentities.InspectionResultsGet;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class InspectionResultGetViewModel extends ViewModel {

    private MutableLiveData<ArrayList<InspectionResultsGet>> inspResultGetList = new MutableLiveData<>();

    private MutableLiveData<ArrayList<InspectionResultsGet>> inspCharResultGetList = new MutableLiveData<>();

    public MutableLiveData<ArrayList<InspectionResultsGet>> getInspCharResultGetList() {
        return inspCharResultGetList;
    }

    public void setInspectionResultGetLiveData(String inspLot, String inspOpr, String inspPoint) {
        inspResultGetList.setValue(getInspResultGetListLiveData(inspLot, inspOpr, inspPoint));
    }

    public void setInspectionCharResultGetLiveData(String inspLot, String inspOpr, String inspPoint, String inspChar) {
        inspCharResultGetList.setValue(getInspCharResultGetListLiveData(inspLot, inspOpr, inspPoint, inspChar));
    }

    /**
     * This method is used to get the Characteristic results based on the Char number
     *
     * @param inspLotNum Inspection Lot Number
     * @param inspOprNum Inspection Operation Number
     * @param inspPoint  Inspection Point
     * @param inspChar   Inspection Characterstic
     * @return list of CharResults.
     */
    private ArrayList<InspectionResultsGet> getInspCharResultGetListLiveData(String inspLotNum, String inspOprNum, String inspPoint, String inspChar) {
        try {
            ArrayList<InspectionResultsGet> inspectionResultsGetlist = InspectionResultsGet.getInspCharResultGet(inspLotNum, inspOprNum, inspPoint, inspChar);
            return inspectionResultsGetlist;
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return new ArrayList<>();
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
        return new ArrayList<>();
    }

    /**
     * this method returns the Sorted results based on the submitted result date and and time.
     *
     * @param inspResults Characteristic Results
     * @return
     */
    private ArrayList<InspectionResultsGet> getSortedList(ArrayList<InspectionResultsGet> inspResults) {
        ArrayList<InspectionResultsGet> sortedList = new ArrayList<>();
        Collections.sort(inspResults, new Comparator<InspectionResultsGet>() {
            @Override
            public int compare(InspectionResultsGet t1, InspectionResultsGet t2) {
                return t1.getInspTime().compareTo(t2.getInspTime());
            }
        });
        return sortedList;
    }

}