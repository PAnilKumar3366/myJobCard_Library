package com.ods.myjobcard_library;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.entities.ResponseMasterModel;
import com.ods.myjobcard_library.entities.forms.FormApproverSetModel;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.myjobcard_library.viewmodels.FormApproversHelper;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FormInstanceListViewModel extends BaseViewModel {

    FormApproversHelper formApproversHelper;
    MutableLiveData<ArrayList<FormApproverSetModel>> woFormApproversLiveData = new MutableLiveData<>();

    MutableLiveData<ResponseMasterModel> selectedInstance = new MutableLiveData<>();

    public MutableLiveData<ResponseMasterModel> getSelectedInstance() {
        return selectedInstance;
    }

    public void setSelectedInstance(ResponseMasterModel selectedInstance) {
        this.selectedInstance.setValue(selectedInstance);
    }

    public FormInstanceListViewModel(@NonNull @NotNull Application application) {
        super(application);
        formApproversHelper = new FormApproversHelper();
    }

    public MutableLiveData<ArrayList<FormApproverSetModel>> getWoFormApproversLiveData() {
        return woFormApproversLiveData;
    }

    public void setWoFormApproversLiveData(String FormID, String ApproverID, String WONum, String OprNum, String version) {
        FormApproversHelper helper = new FormApproversHelper();
        ArrayList<FormApproverSetModel> woFormApproversList = onFetchWOApproverEntities(helper.getWOFormApproversList(FormID, ApproverID, WONum, OprNum, version));
        woFormApproversLiveData.setValue(woFormApproversList);
    }

    /**
     * Converting the ZODataEntity list to Approver object
     *
     * @param zODataEntities
     * @return
     */
    protected ArrayList<FormApproverSetModel> onFetchWOApproverEntities(ArrayList<ZODataEntity> zODataEntities) {
        ArrayList<FormApproverSetModel> approverMasterList = new ArrayList<>();
        try {
            for (ZODataEntity entity : zODataEntities) {
                FormApproverSetModel approverMasterData = new FormApproverSetModel(entity);
                approverMasterList.add(approverMasterData);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return approverMasterList;
    }
}
