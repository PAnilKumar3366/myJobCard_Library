package com.ods.myjobcard_library.viewmodels.workorder;

import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ResponseMasterModel;
import com.ods.myjobcard_library.entities.forms.FormAssignmentSetModel;
import com.ods.myjobcard_library.entities.forms.FormListObject;
import com.ods.myjobcard_library.entities.forms.FormSetModel;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayFormsViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<FormListObject>> formItems = new MutableLiveData<ArrayList<FormListObject>>();
    private final MutableLiveData<ArrayList<FormListObject>> formFilledItems = new MutableLiveData<ArrayList<FormListObject>>();
    private final MutableLiveData<ArrayList<FormListObject>> generalFormItems = new MutableLiveData<ArrayList<FormListObject>>();

    String orderType, wo_Number, opr_Num, equipmentCat, funcLocCat, controlKey;
    List<FormListObject> responseMasterModel = new ArrayList<>();
    FormSetModel formSetModel;
    private ArrayList<FormAssignmentSetModel> list = new ArrayList<>();
    private ArrayList<FormSetModel> list2 = new ArrayList<>();
    private ArrayList<FormListObject> formItemsList = new ArrayList<>();
    private ArrayList<FormListObject> generalFormItemsList = new ArrayList<>();
    private ArrayList<FormListObject> formFilledItemsList = new ArrayList<>();

    public LiveData<ArrayList<FormListObject>> getFormItems() {
        return formItems;
    }

    public LiveData<ArrayList<FormListObject>> getFilledFormItems() {
        return formFilledItems;
    }

    public LiveData<ArrayList<FormListObject>> getGeneralFormItems() {
        return generalFormItems;
    }

    public void setOrderType(WorkOrder workOrder, String typeValue) {

        if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
            getOrderType(workOrder, typeValue);
            opr_Num = workOrder.getCurrentOperation().getOperationNum();
        } else {
            opr_Num = "";
            getOrderType(workOrder, typeValue);
        }
        wo_Number = workOrder.getWorkOrderNum();
        getFormItemsList();
    }

    public void setGeneralFormType() {
        getGeneralFormItemsList();
    }

    protected void getOrderType(WorkOrder workOrder, String type) {
        if (type.equals(ZAppSettings.FormAssignmentType.WorkOrderLevel.Value)) {
            orderType = workOrder.getOrderType();
            equipmentCat = "";
            funcLocCat = "";
            controlKey = "";
        } else if (type.equals(ZAppSettings.FormAssignmentType.OperationLevel.Value)) {
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
        }
    }

    private void getFormItemsList() {
        list = FormAssignmentSetModel.getFormAssignmentData(orderType, controlKey, equipmentCat, funcLocCat);

        Iterator<FormAssignmentSetModel> it1 = list.iterator();

        if (formItemsList.size() > 0)
            formItemsList.clear();

        while (it1.hasNext()) {
            int filledForms = 0;
//            String responseData = "";
            String instanceId = null;
            String isDraft = "";
            FormAssignmentSetModel f1 = it1.next();
            ArrayList<ResponseMasterModel> response = ResponseMasterModel.getResponseCaptureData(f1.getFormID(), f1.getVersion(), wo_Number, opr_Num, false, null);
            if (response != null) {
                Iterator<ResponseMasterModel> it = response.iterator();
                while (it.hasNext()) {
                    ResponseMasterModel res = it.next();
                    if (!(f1.getMultipleSub().equals("X"))) {
//                            String str = res.getResponseData();
//                        responseData = DisplayInsideFormList.convertHexToString(str);
                        instanceId = res.getInstanceID();
                        isDraft = res.getIsDraft();
                    }
                    /*if ((res.getFormID().equals(f1.getFormID()) && res.getVersion().equals(f1.getVersion())) && res.getWoNum().equals(workOrderNum)) {
                        filledForms++;
                    }*/
                }
                filledForms = response.size();
            }

            list2 = FormSetModel.getFormsData(f1.getFormID(), f1.getVersion(), false);
            Iterator<FormSetModel> it2 = list2.iterator();
            while (it2.hasNext()) {
                FormSetModel f2 = it2.next();
                FormListObject ob = new FormListObject(f2.getFormName(), f1.getFormID(),
                        f1.getVersion(), f1.getMandatory(), f1.getOccurInt(),
                        f1.getMultipleSub(), filledForms, instanceId, f1.isGridTheme());
                ob.setIsDraft(isDraft);
                formItemsList.add(ob);
                break;
            }
        }
        formItems.setValue(formItemsList);
    }

    private void getGeneralFormItemsList() {
        try {
            list = FormAssignmentSetModel.getGeneralFormAssignmentData();
            Iterator<FormAssignmentSetModel> it1 = list.iterator();

            if (generalFormItemsList.size() > 0)
                generalFormItemsList.clear();

            while (it1.hasNext()) {
                FormAssignmentSetModel f1 = it1.next();
                list2 = FormSetModel.getFormsData(f1.getFormID(), f1.getVersion(), false);
                Iterator<FormSetModel> it2 = list2.iterator();
                while (it2.hasNext()) {
                    FormSetModel f2 = it2.next();
                    FormListObject ob = new FormListObject(f2.getFormName(), f1.getFormID(),
                            f1.getVersion(), f1.getMandatory(), f1.getOccurInt(),
                            f1.getMultipleSub(), 0, "", f1.isGridTheme());
                    ob.setIsDraft("");
                    generalFormItemsList.add(ob);
                    break;
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        generalFormItems.setValue(generalFormItemsList);
    }

    public void setFilledFormlist(ArrayList<FormListObject> formlist) {
        Iterator<FormListObject> formListIterator = formlist.iterator();
        int totGeneralFormResponseCount;
        try {
            if (formFilledItemsList.size() > 0)
                formFilledItemsList.clear();

            while (formListIterator.hasNext()) {
                int filledForms = 0;
                String instanceId = null;
                String isDraft = "";
                FormListObject f1 = formListIterator.next();
                totGeneralFormResponseCount = ResponseMasterModel.getSubmittedFormsCount(f1.getFormid(), f1.getVersion());
                if (totGeneralFormResponseCount > 0) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            responseMasterModel = formlist.stream().filter(o -> o.getFormid().equals(f1.getFormid()) && o.getVersion().equals(f1.getVersion())).collect(Collectors.toList());
                        } else {
                            for (FormListObject responseMaster : formlist) {
                                if (responseMaster.getFormid().equals(f1.getFormid()) && responseMaster.getVersion().equals(f1.getVersion())) {
                                    responseMasterModel.add(responseMaster);
                                    break;
                                }
                            }
                        }
                        instanceId = responseMasterModel.get(0).getInstanceId();
                        isDraft = responseMasterModel.get(0).getIsDraft();
                        filledForms = totGeneralFormResponseCount;
                        list2 = FormSetModel.getFormsData(f1.getFormid(), f1.getVersion(), false);
                        formSetModel = list2.get(0);
                        FormListObject ob = new FormListObject(formSetModel.getFormName(), f1.getFormid(),
                                f1.getVersion(), f1.getMandatory(), f1.getOccur(),
                                f1.getMultipleSub(), filledForms, instanceId, f1.isGridTheme());
                        ob.setIsDraft(isDraft);
                        formFilledItemsList.add(ob);

                    } catch (Exception e) {
                        DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        formFilledItems.setValue(formFilledItemsList);
    }
}
