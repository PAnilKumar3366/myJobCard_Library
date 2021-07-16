package com.ods.myjobcard_library.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.entities.forms.FormResponseApprovalStatus;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CheckSheetApprovalStatusViewModel extends BaseViewModel {

    MutableLiveData<ArrayList<FormResponseApprovalStatus>> approvalStatusLiveData = new MutableLiveData<>();
    CheckSheetApprovalStatusHelper helper;

    public CheckSheetApprovalStatusViewModel(@NonNull @NotNull Application application) {
        super(application);
        helper = new CheckSheetApprovalStatusHelper();
    }

    public void setApprovalStatusLiveData(String FormId, String FormVersion, String FormInstance, String counter, String submittedBy) {
        ArrayList<ZODataEntity> zoDataEntityArrayList = helper.fetchFormApprovalStatus(FormId, FormInstance, FormVersion, submittedBy, counter);
        approvalStatusLiveData.setValue(onFetchApproverEntities(zoDataEntityArrayList));
    }

    public MutableLiveData<ArrayList<FormResponseApprovalStatus>> getApprovalStatusLiveData() {
        return approvalStatusLiveData;
    }

    public void fetchDummyData() {
        FormResponseApprovalStatus statuone = new FormResponseApprovalStatus("Pre_Check_List", "005", "FE20201009150817", "Anil Kumar", "");
        FormResponseApprovalStatus statutwo = new FormResponseApprovalStatus("Health_Form_equip - Copy", "000", "FE20201009150888", "Anil Kumar", "");
        ArrayList<FormResponseApprovalStatus> list = new ArrayList<>();
        list.add(statuone);
        list.add(statutwo);
        approvalStatusLiveData.setValue(list);
    }

    protected ArrayList<FormResponseApprovalStatus> onFetchApproverEntities(ArrayList<ZODataEntity> zODataEntities) {
        ArrayList<FormResponseApprovalStatus> approverMasterList = new ArrayList<>();
        try {
            for (ZODataEntity entity : zODataEntities) {
                FormResponseApprovalStatus approverMasterData = new FormResponseApprovalStatus(entity);
                approverMasterList.add(approverMasterData);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return approverMasterList;
    }
}
