package com.ods.myjobcard_library.viewmodels.equipment;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.entities.ctentities.BreakdownReport;
import com.ods.myjobcard_library.entities.ctentities.Equipment;
import com.ods.myjobcard_library.entities.ctentities.EquipmentCharacteristics;
import com.ods.myjobcard_library.entities.ctentities.EquipmentClassificationSet;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class EquipmentDetailViewModel extends BaseViewModel {

    private MutableLiveData<Equipment> mCurrentEquipment = new MutableLiveData<>();
    private MutableLiveData<ArrayList<EquipmentClassificationSet>> mEquipmentClassificationList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<EquipmentCharacteristics>> mEquipmentCharacteristicsList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<BreakdownReport>> onlineBDRList = new MutableLiveData<>();
    private ArrayList<BreakdownReport> breakdownReports = new ArrayList<>();

    public EquipmentDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<EquipmentClassificationSet>> getEquipmentClassificationList() {
        return mEquipmentClassificationList;
    }

    public void setEquipmentClassifactionsList(Equipment equipment) {
        ArrayList<EquipmentClassificationSet> EquipmentClassificationList = new ArrayList<>();
        ResponseObject result = EquipmentClassificationSet.getEquipmentClassificationSet(equipment.getEquipment());
        if (!result.isError()) {
            EquipmentClassificationList = (ArrayList<EquipmentClassificationSet>) result.Content();
        }
        mEquipmentClassificationList.setValue(EquipmentClassificationList);
    }

    public MutableLiveData<Equipment> getmCurrentEquipment() {
        return mCurrentEquipment;
    }

    public void setmCurrentEquipment() {
        mCurrentEquipment.setValue(Equipment.getCurrEquipment());
    }

    public MutableLiveData<ArrayList<EquipmentCharacteristics>> getEquipmentCharacteristicsList() {
        return mEquipmentCharacteristicsList;
    }

    public void setEquipmentCharacteristicsList(EquipmentClassificationSet currentSet, String equipmentNum) {
        ArrayList<EquipmentCharacteristics> EqCharacteristicsList = new ArrayList<>();
        ResponseObject result = EquipmentCharacteristics.getEquipmentCharacteristics(currentSet.getClassType(), equipmentNum);
        if (!result.isError()) {
            EqCharacteristicsList = (ArrayList<EquipmentCharacteristics>) result.Content();
        }
        mEquipmentCharacteristicsList.setValue(EqCharacteristicsList);
    }

    public MutableLiveData<ArrayList<BreakdownReport>> getOnlineBreakDownReports() {
        return onlineBDRList;
    }


    public void setOnlineBreakDownReports() {
        onlineBDRList.setValue(new ArrayList<BreakdownReport>());
    }

}
