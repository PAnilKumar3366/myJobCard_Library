package com.ods.myjobcard_library.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.appsettings.StatusCategory;
import com.ods.myjobcard_library.entities.forms.FormResponseApprovalStatus;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CheckSheetStatusViewModel extends BaseViewModel {

    private MutableLiveData<ArrayList<StatusCategory>> statusCategoryLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<StatusCategory>> validStatusesList = new MutableLiveData<>();
    private MutableLiveData<Boolean> updateStatus = new MutableLiveData<>();
    private MutableLiveData<String> statusSaveResult = new MutableLiveData<>();
    private CheckSheetStatusHelper statusHelper;
    private MutableLiveData<FormResponseApprovalStatus> currentApprovalStatus = new MutableLiveData<>();

    public CheckSheetStatusViewModel(@NonNull @NotNull Application application) {
        super(application);
        statusHelper = new CheckSheetStatusHelper();
    }

    public MutableLiveData<String> getStatusSaveResult() {
        return statusSaveResult;
    }

    public MutableLiveData<FormResponseApprovalStatus> getCurrentApprovalStatus() {
        return currentApprovalStatus;
    }

    public void setCurrentApprovalStatus(String formID, String versionID, String instanceID, String submittedBy, String counter, String approverID) {
        ZODataEntity zoDataEntity = statusHelper.getCheckSheetInstanceStatus(formID, versionID, instanceID, submittedBy, counter, approverID);
        currentApprovalStatus.setValue(onFetchApprovalStatus(zoDataEntity));
    }

    /**
     * fetching current status information by calling its helper instance
     *
     * @param status
     * @param objType
     */
    public void fetchCurrentStatusDetails(String status, String objType) {
        try {
            //taskHelper=new NotificationTaskHelper(status,objType);
            statusCategoryLiveData.setValue(statusHelper.deriveChecksheetStatus(status, objType));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**
     * getting the live data for current status details
     *
     * @return
     */
    public MutableLiveData<ArrayList<StatusCategory>> getStatusCategoryLiveData() {
        return statusCategoryLiveData;
    }

    /**
     * fetching all valid statuses by calling its helper instance
     */
    public void fetchValidStatuses() {
        try {
            validStatusesList.setValue(statusHelper.getAllowedStatus());
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    protected FormResponseApprovalStatus onFetchApprovalStatus(ZODataEntity zoDataEntity) {
        try {
            if (zoDataEntity != null)
                return new FormResponseApprovalStatus(zoDataEntity);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
            setError(e.getMessage());
        }
        return null;
    }

    public void saveInstanceStatus(FormResponseApprovalStatus approvalStatus) {
        ResponseObject result = statusHelper.saveInstanceStatus(approvalStatus);
        try {
            if (result != null) {
                if (!result.isError())
                    statusSaveResult.setValue("Success");
                else
                    setError(result.getMessage());
            } else
                setError("UnKnowError");
        } catch (Exception e) {
            e.printStackTrace();
            setError(e.getMessage());
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
    }
}
