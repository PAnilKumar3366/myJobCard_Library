package com.ods.myjobcard_library.viewmodels.superviors;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ResponseMasterModel;
import com.ods.myjobcard_library.entities.forms.FormAssignmentSetModel;
import com.ods.myjobcard_library.entities.forms.FormListObject;
import com.ods.myjobcard_library.entities.forms.FormSetModel;
import com.ods.myjobcard_library.entities.supervisor.SupervisorWorkOrder;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class SupervisorViewModel extends BaseViewModel {

    private ArrayList<FormAssignmentSetModel> formAssignmentList = new ArrayList<>();
    private ArrayList<FormSetModel> formSetList = new ArrayList<>();
    private String orderType, workOrderNum, operationNum, equipmentCat, funcLocCat, controlKey;
    private MutableLiveData<ArrayList<FormListObject>> listOfForms = new MutableLiveData<>();
    private MutableLiveData<SupervisorWorkOrder> currWorkOrder = new MutableLiveData<>();

    public SupervisorViewModel(@NonNull Application application) {
        super(application);
        setCurrWorkOrder();
    }

    public MutableLiveData<ArrayList<FormListObject>> getListOfForms() {
        return listOfForms;
    }

    public void setListOfForms(SupervisorWorkOrder workOrder) {
        try {
            getOrderType(workOrder, ZAppSettings.FormAssignmentType.getFormAssignmentType(ZConfigManager.FORM_ASSIGNMENT_TYPE));
            operationNum = "";
            workOrderNum = workOrder.getWorkOrderNum();
            getFormsList();
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public MutableLiveData<SupervisorWorkOrder> getCurrWorkOrder() {
        return currWorkOrder;
    }

    public void setCurrWorkOrder() {
        SupervisorWorkOrder workOrder = SupervisorWorkOrder.getCurrentSupervisorWorkOrder();
        currWorkOrder.setValue(workOrder);
    }

    protected void getOrderType(SupervisorWorkOrder workOrder, String type) {
        if (type.equals(ZAppSettings.FormAssignmentType.WorkOrderLevel.Value)) {
            orderType = workOrder.getOrderType();
            equipmentCat = "";
            funcLocCat = "";
            controlKey = "";
        }/* else if (type.equals(ZAppSettings.FormAssignmentType.OperationLevel.Value)) {
            controlKey = ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED ? workOrder.getCurrentOperation().getControlKey() : "";
            orderType = workOrder.getOrderType();
            equipmentCat = "";
            funcLocCat = "";
        } else if (type.equals(ZAppSettings.FormAssignmentType.Equipment.Value)) {
            funcLocCat = "";
            if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)
                equipmentCat = workOrder.getEquipCategory();
            else
                equipmentCat = workOrder.getCurrentOperation().getEquipCategory().isEmpty() ? workOrder.getEquipCategory() : workOrder.getCurrentOperation().getEquipCategory();
        } else if (type.equals(ZAppSettings.FormAssignmentType.FuncLoc.Value)) {
            equipmentCat = "";
            if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)
                funcLocCat = workOrder.getFuncLocCategory();
            else
                funcLocCat = workOrder.getCurrentOperation().getFuncLocCategory().isEmpty() ? workOrder.getFuncLocCategory() : workOrder.getCurrentOperation().getFuncLocCategory();
        } else if (type.equals(ZAppSettings.FormAssignmentType.None.Value)) {
            if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)
                orderType = workOrder.getOrderType();
            else
                controlKey = workOrder.getCurrentOperation().getControlKey();
        }*/
    }

    private void getFormsList() {
        ArrayList<FormListObject> arrayList = new ArrayList<>();
        formAssignmentList = FormAssignmentSetModel.getFormAssignmentData(orderType, controlKey, equipmentCat, funcLocCat);

        for (FormAssignmentSetModel formAssignmentSetModel : formAssignmentList) {
            int filledForms = 0;
            String instanceId = null;
            String isDraft = "";
            ArrayList<ResponseMasterModel> listOfFilledForms = ResponseMasterModel.getFilledFormsForSupervisorView(workOrderNum);
            if (listOfFilledForms != null) {
                for (ResponseMasterModel response : listOfFilledForms) {
                    if (!(formAssignmentSetModel.getMultipleSub().equals("X"))) {
                        instanceId = response.getInstanceID();
                        isDraft = response.getIsDraft();
                    }
                }
                filledForms = listOfFilledForms.size();
            }

            formSetList = FormSetModel.getFormsData(formAssignmentSetModel.getFormID(), formAssignmentSetModel.getVersion(), false);
            for (FormSetModel currSet : formSetList) {
                FormListObject formObject = new FormListObject(currSet.getFormName(), formAssignmentSetModel.getFormID(),
                        formAssignmentSetModel.getVersion(), formAssignmentSetModel.getMandatory(), formAssignmentSetModel.getOccurInt(),
                        formAssignmentSetModel.getMultipleSub(), filledForms, instanceId, formAssignmentSetModel.isGridTheme());
                formObject.setIsDraft(isDraft);
                arrayList.add(formObject);
                break;
            }
        }
        listOfForms.setValue(arrayList);
    }
}
