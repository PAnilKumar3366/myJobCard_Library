package com.ods.myjobcard_library.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.forms.ApproverMasterData;
import com.ods.myjobcard_library.entities.forms.FormApproverSetModel;
import com.ods.myjobcard_library.entities.forms.FormListObject;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FormApproverListViewModel extends BaseViewModel {

    MutableLiveData<ArrayList<ApproverMasterData>> ApproversListLiveData = new MutableLiveData<>();
    MutableLiveData<ArrayList<ApproverMasterData>> ApproversSearchListLiveData = new MutableLiveData<>();
    MutableLiveData<WorkOrder> currentOrder = new MutableLiveData<>();
    MutableLiveData<ArrayList<FormApproverSetModel>> woFormApproversLiveData = new MutableLiveData<>();
    MutableLiveData<FormListObject> currentCheckSheet = new MutableLiveData<>();

    private ArrayList<ApproverMasterData> approverMasterDataList;
    private ApproverMasterHelper approverMasterHelper;

    public FormApproverListViewModel(@NonNull @NotNull Application application) {
        super(application);
        approverMasterDataList = new ArrayList<>();
        approverMasterHelper = new ApproverMasterHelper();
    }

    public MutableLiveData<FormListObject> getCurrentCheckSheet() {
        return currentCheckSheet;
    }

    public void setCurrentCheckSheet(FormListObject checksheet) {
        currentCheckSheet.setValue(checksheet);
    }

    public void setApproversListLiveData() {
        ArrayList<ZODataEntity> zoDataEntityArrayList = approverMasterHelper.fetchApproverList();
        ApproversListLiveData.setValue(onFetchApproverEntities(zoDataEntityArrayList));
        ;
    }

    public MutableLiveData<ArrayList<ApproverMasterData>> getApproversListLiveData() {
        return ApproversListLiveData;
    }

    public MutableLiveData<ArrayList<ApproverMasterData>> getApproversSearchListLiveData() {
        return ApproversSearchListLiveData;
    }

    public void setSearchList(String searchText, String searchKey) {
        ArrayList<ZODataEntity> zoDataEntityArrayList = approverMasterHelper.searchApproverList(searchText, searchKey);
        ApproversSearchListLiveData.setValue(onFetchApproverEntities(zoDataEntityArrayList));
    }

    public MutableLiveData<WorkOrder> getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(String woNum) {
        try {
            if (WorkOrder.getCurrWo() != null && WorkOrder.getCurrWo().equals(woNum))
                currentOrder.setValue(WorkOrder.getCurrWo());
            else {
                ResponseObject result = WorkOrder.getWorkOrders(ZAppSettings.FetchLevel.Single, woNum, "");
                ArrayList<WorkOrder> list = (ArrayList<WorkOrder>) result.Content();
                currentOrder.setValue(list.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**
     * Converting the ZODataEntity list to Approver object
     *
     * @param zODataEntities
     * @return
     */
    protected ArrayList<ApproverMasterData> onFetchApproverEntities(ArrayList<ZODataEntity> zODataEntities) {
        ArrayList<ApproverMasterData> approverMasterList = new ArrayList<>();
        try {
            for (ZODataEntity entity : zODataEntities) {
                ApproverMasterData approverMasterData = new ApproverMasterData(entity);
                approverMasterList.add(approverMasterData);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return approverMasterList;
    }

    public void setWoFormApproversLiveData(String FormID, String ApproverID, String WONum, String OprNum) {
        FormApproversHelper helper = new FormApproversHelper();
        ArrayList<FormApproverSetModel> woFormApproversList = onFetchWOApproverEntities(helper.getWOFormApproversList(FormID, ApproverID, WONum, OprNum));
        woFormApproversLiveData.setValue(woFormApproversList);
    }

    public MutableLiveData<ArrayList<FormApproverSetModel>> getWoFormApproversLiveData() {
        return woFormApproversLiveData;
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

    /**
     * Used to delete the CheckSheet FormApprover offline and Online.
     * This is called from UI and returns the Result as Response Object
     *
     * @param FormID     CheckSheet FormID
     * @param ApproverID Selected ApproverID
     * @param Wonum      WorkOrder Number
     * @param OprNum     Operation Number* @return
     */
    public ResponseObject deleteCheckSheetApprover(String FormID, String ApproverID, String Wonum, String OprNum) {
        FormApproversHelper helper = new FormApproversHelper();
        ResponseObject result = helper.deleteFormApprover(FormID, ApproverID, Wonum, OprNum);
        return result;
    }
}
