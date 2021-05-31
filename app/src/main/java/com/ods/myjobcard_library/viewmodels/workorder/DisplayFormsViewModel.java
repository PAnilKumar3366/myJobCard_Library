package com.ods.myjobcard_library.viewmodels.workorder;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ResponseMasterModel;
import com.ods.myjobcard_library.entities.forms.FormAssignmentSetModel;
import com.ods.myjobcard_library.entities.forms.FormListObject;
import com.ods.myjobcard_library.entities.forms.FormSetModel;
import com.ods.myjobcard_library.entities.forms.ManualFormAssignmentSetModel;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.myjobcard_library.utils.ManualCheckSheetData;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayFormsViewModel extends BaseViewModel {

    private boolean isTaskType = false;
    private final MutableLiveData<ArrayList<FormListObject>> formItems = new MutableLiveData<ArrayList<FormListObject>>();
    private final MutableLiveData<ArrayList<FormListObject>> formFilledItems = new MutableLiveData<ArrayList<FormListObject>>();
    private final MutableLiveData<ArrayList<FormListObject>> generalFormItems = new MutableLiveData<ArrayList<FormListObject>>();
    private MutableLiveData<ArrayList<ManualFormAssignmentSetModel>> manualCheckSheetLiveData = new MutableLiveData<>();

    String orderType, wo_Number, opr_Num, equipmentCat, funcLocCat, controlKey, taskListType, group, groupCounter, internalCounter;
    List<FormListObject> responseMasterModel = new ArrayList<>();
    FormSetModel formSetModel;
    private ArrayList<FormAssignmentSetModel> list = new ArrayList<>();
    private ArrayList<FormSetModel> list2 = new ArrayList<>();
    private ArrayList<FormListObject> formItemsList = new ArrayList<>();
    private ArrayList<FormListObject> generalFormItemsList = new ArrayList<>();
    private ArrayList<FormListObject> formFilledItemsList = new ArrayList<>();
    private ArrayList<ManualFormAssignmentSetModel> dummyList = new ArrayList<>();
    public DisplayFormsViewModel(@NonNull Application application) {
        super(application);
    }

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
        /*taskListType = "";
        group = "";
        groupCounter = "";
        internalCounter = "";*/
        wo_Number = workOrder.getWorkOrderNum();
        if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
            opr_Num = workOrder.getCurrentOperation().getOperationNum();
        } else {
            opr_Num = "";
        }
        getOrderType(workOrder, typeValue);
        /*if (!isTaskType)*/
        getFormItemsList(list);
        formItems.setValue(formItemsList);
    }

    public void setGeneralFormType() {
        getGeneralFormItemsList();
    }

    public MutableLiveData<ArrayList<ManualFormAssignmentSetModel>> getManualCheckSheetLiveData() {
        manualCheckSheetLiveData.setValue(ManualCheckSheetData.getInstance().getManualCheckSheetList());
        return manualCheckSheetLiveData;
    }

    protected void getOrderType(WorkOrder workOrder, String type) {
        if (type.equals(ZAppSettings.FormAssignmentType.WorkOrderLevel.Value)) {
            orderType = workOrder.getOrderType();
           /* equipmentCat = "";
            funcLocCat = "";
            controlKey = "";*/
            list = FormAssignmentSetModel.getFormAssignmentData_OrderType(orderType);
        } else if (type.equals(ZAppSettings.FormAssignmentType.OperationLevel.Value)) {
            /*controlKey = ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED ? workOrder.getCurrentOperation().getControlKey() : "";
            orderType = workOrder.getOrderType();
            *//*equipmentCat = "";
            funcLocCat = "";*//*
            list=FormAssignmentSetModel.getFormAssignmentData_OperationType(orderType,controlKey);*/
            if(!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED){
                list.clear();
                ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, workOrder.getWorkOrderNum());
                ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                for (Operation operation : totalOperations) {
                    controlKey = operation.getControlKey();
                    orderType = operation.getOrderType();
                    list.addAll(FormAssignmentSetModel.getFormAssignmentData_OperationType(orderType,controlKey));
                    //list=FormAssignmentSetModel.getFormAssignmentData_OperationType(orderType,controlKey);
                }
            }
            else {
                orderType = workOrder.getOrderType();
                controlKey = workOrder.getCurrentOperation().getControlKey();
                list=FormAssignmentSetModel.getFormAssignmentData_OperationType(orderType,controlKey);
            }
        } else if (type.equals(ZAppSettings.FormAssignmentType.Equipment.Value)) {
            //funcLocCat = "";
            if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)
                equipmentCat = workOrder.getEquipCategory();
            else
                equipmentCat = workOrder.getCurrentOperation().getEquipCategory().isEmpty() ? workOrder.getEquipCategory() : workOrder.getCurrentOperation().getEquipCategory();
            list=FormAssignmentSetModel.getFormAssignmentData_EquipmentType(equipmentCat);
        } else if (type.equals(ZAppSettings.FormAssignmentType.FuncLoc.Value)) {
           // equipmentCat = "";
            if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)
                funcLocCat = workOrder.getFuncLocCategory();
            else
                funcLocCat = workOrder.getCurrentOperation().getFuncLocCategory().isEmpty() ? workOrder.getFuncLocCategory() : workOrder.getCurrentOperation().getFuncLocCategory();
            list=FormAssignmentSetModel.getFormAssignmentData_FunctionalLocType(funcLocCat);
        } else if (type.equals(ZAppSettings.FormAssignmentType.TaskListType.Value)) {
            /*equipmentCat = "";
            funcLocCat = "";*/
            if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
               // isTaskType = true;
                ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, workOrder.getWorkOrderNum());
                ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                for (Operation operation : totalOperations) {
                    orderType = operation.getOrderType();
                    controlKey = operation.getControlKey();
                    taskListType = operation.getTaskListType();
                    group = operation.getGroup();
                    groupCounter = operation.getGroupCounter();
                    internalCounter = operation.getInternalCounter();
                    list=FormAssignmentSetModel.getFormAssignmentData_TaskListType(orderType,controlKey,taskListType,group,groupCounter,internalCounter);
                }
            } else {
                orderType = workOrder.getCurrentOperation().getOrderType();
                controlKey = workOrder.getCurrentOperation().getControlKey();
                taskListType = workOrder.getCurrentOperation().getTaskListType();
                group = workOrder.getCurrentOperation().getGroup();
                groupCounter = workOrder.getCurrentOperation().getGroupCounter();
                internalCounter = workOrder.getCurrentOperation().getInternalCounter();
                list=FormAssignmentSetModel.getFormAssignmentData_TaskListType(orderType,controlKey,taskListType,group,groupCounter,internalCounter);
            }
        } else if (type.equals(ZAppSettings.FormAssignmentType.None.Value)) {
            if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)
                list=FormAssignmentSetModel.getFormAssignmentData_OrderType(workOrder.getOrderType());
            else
                list=FormAssignmentSetModel.getFormAssignmentData_OperationType(workOrder.getOrderType(),workOrder.getCurrentOperation().getControlKey());
        }
    }

    private void getFormItemsList(ArrayList<FormAssignmentSetModel> list) {
        //list = FormAssignmentSetModel.getFormAssignmentData(orderType, controlKey, equipmentCat, funcLocCat, taskListType, group, groupCounter, internalCounter);

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

    /**
     * this is method prepares the dummy data.
     *
     * @param existedList newly added list from Add new CheckSheet
     */
    public void setManualListLiveData(ArrayList<ManualFormAssignmentSetModel> existedList) {
        try {
            if (existedList.size() > 0) {
                dummyList.clear();
                ManualCheckSheetData.getInstance().getManualCheckSheetList().clear();
            }
            dummyList.addAll(existedList);
            if (dummyList.size() == 0) {
                String[] FormName = {"CreateNotification", "InspectionForm", "Covid-Form"};
                String[] Version = {"1.0", "2.0", "3.0"};
                String[] Mandatory = {"X", "X", ""};
                String[] MultipleSub = {"X", "X", "X"};
                String[] Occur = {"4", "5", "3"};
                for (int i = 0; i < 3; i++)
                    dummyList.add(new ManualFormAssignmentSetModel(Version[i], FormName[i], Mandatory[i], MultipleSub[i], Occur[i]));
            }
            ManualCheckSheetData.getInstance().setManualCheckSheetList(dummyList);
            manualCheckSheetLiveData.setValue(dummyList);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            manualCheckSheetLiveData.setValue(new ArrayList<ManualFormAssignmentSetModel>());
        }
    }

}
