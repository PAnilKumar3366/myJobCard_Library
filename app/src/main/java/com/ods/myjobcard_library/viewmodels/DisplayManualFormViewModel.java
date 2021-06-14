package com.ods.myjobcard_library.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.forms.FormListObject;
import com.ods.myjobcard_library.entities.forms.FormSetModel;
import com.ods.myjobcard_library.entities.forms.ManualFormAssignmentSetModel;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class DisplayManualFormViewModel extends AndroidViewModel
{
    private final MutableLiveData<ArrayList<FormListObject>> formItems = new MutableLiveData<ArrayList<FormListObject>>();
    private final MutableLiveData<ArrayList<FormListObject>> formFilledItems = new MutableLiveData<ArrayList<FormListObject>>();
    private MutableLiveData<Boolean> postManualFormAssignment=new MutableLiveData<>();
    private ArrayList<ManualFormAssignmentSetModel> manualFormArraylist = new ArrayList<>();
    private ArrayList<FormSetModel> masterFormList = new ArrayList<>();
    private ArrayList<FormListObject> formItemsList = new ArrayList<>();
    private ManualFormAssignmentHelper manualFormAssignmentHelper;
    String woNum,oprNum,notification,notificationItem,notificationTask,equipment,functionalLocation;

    public DisplayManualFormViewModel(@NonNull Application application) {
        super(application);
        manualFormAssignmentHelper=new ManualFormAssignmentHelper();
    }


    /** fetching all Manual form assigned data the workorder or workorder's operation by calling through its helper instance
     * @param workOrder
     * @param formType
     */
    public void onFetchManualFormAssignedList(WorkOrder workOrder, String formType){
        try {
            if (formType.equals(ZAppSettings.FormAssignmentType.ManualAssignmentWO.Value)||formType.equals(ZAppSettings.FormAssignmentType.OrderTypeWithManualAssignWO.Value))
            {
                woNum=workOrder.getWorkOrderNum();
                oprNum="";
                /*notification="";
                notificationItem="";
                notificationTask="";
                equipment="";
                functionalLocation="";*/
                ArrayList<ZODataEntity> zoDataEntityArrayList=manualFormAssignmentHelper.getManualFormAssignmentData(woNum,oprNum);
                manualFormArraylist=onFetchManualFormEntities(zoDataEntityArrayList);
            }
            else if(formType.equals(ZAppSettings.FormAssignmentType.ManualAssignmentOPR.Value))
            {
                if(!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED){
                manualFormArraylist.clear();
                ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, workOrder.getWorkOrderNum());
                ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                for (Operation operation : totalOperations) {
                    woNum = operation.getWorkOrderNum();
                    oprNum = operation.getOperationNum();
                    ArrayList<ZODataEntity> zoDataEntityArrayList=manualFormAssignmentHelper.getManualFormAssignmentData(woNum,oprNum);
                    manualFormArraylist.addAll(onFetchManualFormEntities(zoDataEntityArrayList));
                }
            }
            else {
                woNum=workOrder.getWorkOrderNum();
                oprNum = workOrder.getCurrentOperation().getOperationNum();
                ArrayList<ZODataEntity> zoDataEntityArrayList=manualFormAssignmentHelper.getManualFormAssignmentData(woNum,oprNum);
                manualFormArraylist=onFetchManualFormEntities(zoDataEntityArrayList);
            }
            }
            if(manualFormArraylist.size()>0)
                formItemsList=manualFormAssignmentHelper.getManualFormItemsList(manualFormArraylist,woNum,oprNum);
            formItems.setValue(formItemsList);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public MutableLiveData<ArrayList<FormListObject>> getFormItems() {
        return formItems;
    }

    /** Converting the ZODataEntity list to ManualFormAssignment object
     * @param zODataEntities
     * @return
     */
    protected ArrayList<ManualFormAssignmentSetModel> onFetchManualFormEntities(ArrayList<ZODataEntity> zODataEntities) {
        manualFormArraylist = new ArrayList<>();
        try {
            for (ZODataEntity entity : zODataEntities) {
                ManualFormAssignmentSetModel capacityLevelData = new ManualFormAssignmentSetModel(entity);
                manualFormArraylist.add(capacityLevelData);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return manualFormArraylist;
    }

    public void setPostManualFormAssignment(ArrayList<ManualFormAssignmentSetModel> manualFormAssignmentSetList){
        try {
            postManualFormAssignment.setValue(manualFormAssignmentHelper.postManualFormAssignment(manualFormAssignmentSetList,true));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public MutableLiveData<Boolean> getPostManualFormAssignment() {
        return postManualFormAssignment;
    }
}
