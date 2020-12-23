package com.ods.myjobcard_library.viewmodels.superviors;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
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
    private String orderType, workOrderNum, operationNum, equipmentCat, funcLocCat, controlKey,taskListType,group,groupCounter,internalCounter;
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
            orderType = workOrder.getOrderType();
            equipmentCat = "";
            funcLocCat = "";
            controlKey = "";
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

    private void getFormsList() {
        ArrayList<FormListObject> arrayList = new ArrayList<>();
        formAssignmentList = FormAssignmentSetModel.getFormAssignmentData(orderType, controlKey, equipmentCat, funcLocCat,taskListType,group,groupCounter,internalCounter);

        for (FormAssignmentSetModel formAssignmentSetModel : formAssignmentList) {
            int filledForms = 0;
            String instanceId = null;
            String isDraft = "";
            ArrayList<ResponseMasterModel> listOfFilledForms = ResponseMasterModel.getFilledFormsForSupervisorView(workOrderNum);
            if (listOfFilledForms != null && listOfFilledForms.size() > 0) {
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
