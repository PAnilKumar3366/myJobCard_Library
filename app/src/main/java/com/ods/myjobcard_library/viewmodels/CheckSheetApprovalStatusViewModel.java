package com.ods.myjobcard_library.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.entities.forms.FormResponseApprovalStatus;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CheckSheetApprovalStatusViewModel extends BaseViewModel {

    MutableLiveData<ArrayList<FormResponseApprovalStatus>> checkSheetInstanceStatus = new MutableLiveData<>();
    MutableLiveData<ArrayList<String>> ReviewerCheckSheetLog = new MutableLiveData<>();
    CheckSheetApprovalStatusHelper helper;

    public CheckSheetApprovalStatusViewModel(@NonNull @NotNull Application application) {
        super(application);
        helper = new CheckSheetApprovalStatusHelper();
    }

    public MutableLiveData<ArrayList<String>> getReviewerCheckSheetLog() {
        return ReviewerCheckSheetLog;
    }

    public void setReviewerCheckSheetLog(String FormID, String FormVersion, String FormInstance, String ApproverID, String submittedBy) {
        try {
            ArrayList<ZODataEntity> zoDataEntityArrayList = helper.fetchFormApprovalStatus(FormID, FormInstance, FormVersion, submittedBy, "", ApproverID);
            ArrayList<FormResponseApprovalStatus> statusList = onFetchApproverEntities(zoDataEntityArrayList);
            ArrayList<String> logAuditTextList = new ArrayList<>();
            //<Status> on <Date> <Time> with remarks: <Remarks>
            for (FormResponseApprovalStatus status : statusList) {
                String logText = status.getFormContentStatus() + " on " + getReviewedTime(status) + " with Remarks: " + status.getRemarks();
                logAuditTextList.add(logText);
            }
            ReviewerCheckSheetLog.setValue(logAuditTextList);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            setError(e.getMessage());
        }
    }

    private String getReviewedTime(FormResponseApprovalStatus status) {
        String reviewedOn = "";
        try {
            if (status.getCreatedDate() != null)
                reviewedOn = ZCommon.getFormattedDate(status.getCreatedDate().getTime());
            if (status.getCreatedTime() != null)
                reviewedOn += " " + ZCommon.getFormattedTime(status.getCreatedTime());
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }

        return reviewedOn;
    }

    public void setCheckInstanceStatus(String FormId, String FormVersion, String FormInstance, String counter, String submittedBy, String approverID) {
        ArrayList<ZODataEntity> zoDataEntityArrayList = helper.fetchFormApprovalStatus(FormId, FormInstance, FormVersion, submittedBy, counter, approverID);
        checkSheetInstanceStatus.setValue(onFetchApproverEntities(zoDataEntityArrayList));
    }

    public MutableLiveData<ArrayList<FormResponseApprovalStatus>> getCheckSheetInstanceStatus() {
        return checkSheetInstanceStatus;
    }

    public void fetchDummyData() {
        FormResponseApprovalStatus statuone = new FormResponseApprovalStatus("Pre_Check_List", "005", "FE20201009150817", "Anil Kumar", "");
        FormResponseApprovalStatus statutwo = new FormResponseApprovalStatus("Health_Form_equip - Copy", "000", "FE20201009150888", "Anil Kumar", "");
        ArrayList<FormResponseApprovalStatus> list = new ArrayList<>();
        list.add(statuone);
        list.add(statutwo);
        checkSheetInstanceStatus.setValue(list);
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
