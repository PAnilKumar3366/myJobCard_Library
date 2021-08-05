package com.ods.myjobcard_library.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.forms.FormListObject;
import com.ods.myjobcard_library.entities.forms.FormSetModel;
import com.ods.myjobcard_library.entities.forms.ManualFormAssignmentSetModel;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.ConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class DisplayManualFormViewModel extends BaseViewModel {
    private final MutableLiveData<ArrayList<FormListObject>> formItems = new MutableLiveData<ArrayList<FormListObject>>();
    private final MutableLiveData<ArrayList<FormListObject>> formFilledItems = new MutableLiveData<ArrayList<FormListObject>>();
    private MutableLiveData<Boolean> postManualFormAssignment = new MutableLiveData<>();
    private ArrayList<ManualFormAssignmentSetModel> manualFormArraylist = new ArrayList<>();
    private ArrayList<FormSetModel> masterFormList = new ArrayList<>();
    private ArrayList<FormListObject> formItemsList = new ArrayList<>();
    private ManualFormAssignmentHelper manualFormAssignmentHelper;
    private MutableLiveData<ArrayList<ManualFormAssignmentSetModel>> manualForms = new MutableLiveData<>();

    String woNum, oprNum, notification, notificationItem, notificationTask, equipment, functionalLocation;

    private MutableLiveData<ManualFormAssignmentSetModel> editFormItem = new MutableLiveData<>();

    public MutableLiveData<ManualFormAssignmentSetModel> getEditFormItem() {
        return editFormItem;
    }

    public ArrayList<ManualFormAssignmentSetModel> getManualFormArraylist() {
        return manualFormArraylist;
    }

    public MutableLiveData<ArrayList<ManualFormAssignmentSetModel>> getManualForms() {
        return manualForms;
    }

    public void setManualForms(String orderNum, String oprNum) {
        if (manualFormAssignmentHelper == null)
            manualFormAssignmentHelper = new ManualFormAssignmentHelper();
        ArrayList<ManualFormAssignmentSetModel> manualFormItems = onFetchManualFormEntities(manualFormAssignmentHelper.getManualFormAssignmentData(orderNum, oprNum));
        if (manualFormItems != null && manualFormItems.size() > 0)
            try {
                this.manualForms.setValue(manualFormItems);
            } catch (Exception e) {
                e.printStackTrace();
                printErrorLog(getClass(), e.getMessage());

            }
    }

    public int getCheckSheetApproversCount(String formId, String ApproverID, String version, String woNumber, String oprNum) {
        FormsHelper formsHelper = new FormsHelper();
        return formsHelper.fetChApproversCount(formId, ApproverID, woNumber, oprNum, version);
    }

    public void setEditFormItem(String orderNum, String oprNum) {
        if (manualFormAssignmentHelper == null)
            manualFormAssignmentHelper = new ManualFormAssignmentHelper();
        ArrayList<ManualFormAssignmentSetModel> manualEditItem = onFetchManualFormEntities(manualFormAssignmentHelper.getManualFormAssignmentData(orderNum, oprNum));
        if (manualEditItem != null && manualEditItem.size() > 0)
            try {
                this.editFormItem.setValue(manualEditItem.get(0));
            } catch (Exception e) {
                e.printStackTrace();
                printErrorLog(getClass(), e.getMessage());

            }
    }

    public DisplayManualFormViewModel(@NonNull Application application) {
        super(application);
        manualFormAssignmentHelper = new ManualFormAssignmentHelper();
    }


    /**
     * fetching all Manual form assigned data the workorder or workorder's operation by calling through its helper instance
     *
     * @param workOrder
     * @param formType
     */
    public void onFetchManualFormAssignedList(WorkOrder workOrder, String formType) {
        try {
            if (formType.equals(ZAppSettings.FormAssignmentType.ManualAssignmentWO.Value)) {
                woNum = workOrder.getWorkOrderNum();
                oprNum = "";
                ArrayList<ZODataEntity> zoDataEntityArrayList = manualFormAssignmentHelper.getManualFormAssignmentData(woNum, oprNum);
                manualFormArraylist = onFetchManualFormEntities(zoDataEntityArrayList);
            } else if (formType.equals(ZAppSettings.FormAssignmentType.ManualAssignmentOPR.Value)||formType.equals(ZAppSettings.FormAssignmentType.TaskTypeWithManualAssignOPR.Value)) {
                if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                    manualFormArraylist.clear();
                    ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, workOrder.getWorkOrderNum());
                    ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                    ArrayList<ManualFormAssignmentSetModel> tempManualForms = new ArrayList<>();
                    for (Operation operation : totalOperations) {
                        woNum = operation.getWorkOrderNum();
                        oprNum = operation.getOperationNum();
                        ArrayList<ZODataEntity> zoDataEntityArrayList = manualFormAssignmentHelper.getManualFormAssignmentData(woNum, oprNum);
                        //tempManualForms.addAll(onFetchManualFormEntities(zoDataEntityArrayList));
                        /*if (tempManualForms.size() > 0)
                            manualFormArraylist.addAll(tempManualForms);*/
                        manualFormArraylist.addAll(onFetchManualFormEntities(zoDataEntityArrayList));
                    }
                } else {
                    woNum = workOrder.getWorkOrderNum();
                    oprNum = workOrder.getCurrentOperation().getOperationNum();
                    ArrayList<ZODataEntity> zoDataEntityArrayList = manualFormAssignmentHelper.getManualFormAssignmentData(woNum, oprNum);
                    manualFormArraylist = onFetchManualFormEntities(zoDataEntityArrayList);
                }
            }
            if (manualFormArraylist.size() > 0)
                formItemsList = manualFormAssignmentHelper.getManualFormItemsList(manualFormArraylist, woNum);
            formItems.setValue(formItemsList);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public MutableLiveData<ArrayList<FormListObject>> getFormItems() {
        return formItems;
    }

    /**
     * Converting the ZODataEntity list to ManualFormAssignment object
     *
     * @param zODataEntities
     * @return
     */
    protected ArrayList<ManualFormAssignmentSetModel> onFetchManualFormEntities(ArrayList<ZODataEntity> zODataEntities) {
        ArrayList<ManualFormAssignmentSetModel> manualFormArraylisttemp = new ArrayList<>();
        try {
            for (ZODataEntity entity : zODataEntities) {
                ManualFormAssignmentSetModel capacityLevelData = new ManualFormAssignmentSetModel(entity);
                manualFormArraylisttemp.add(capacityLevelData);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return manualFormArraylisttemp;
    }

    public void setPostManualFormAssignment(ArrayList<ManualFormAssignmentSetModel> manualFormAssignmentSetList) {
        try {
            postManualFormAssignment.setValue(manualFormAssignmentHelper.postManualFormAssignment(manualFormAssignmentSetList, true));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public MutableLiveData<Boolean> getPostManualFormAssignment() {
        return postManualFormAssignment;
    }

    public int getManualUnSubmittedForms(boolean mandatoryFormChk, String woNum, String oprNum) {
        try {
            int unSubmittedFormsCount = 0;
            String strResPath = "";
            String strMandatoryChk = "";
            unSubmittedFormsCount = 0;
            ResponseObject responseObject = null;
            Object rawData = null;

            String formAssignType = ZAppSettings.FormAssignmentType.getFormAssignmentType(ZConfigManager.FORM_ASSIGNMENT_TYPE);
            if (mandatoryFormChk)
                strMandatoryChk = " eq 'x'";
            else
                strMandatoryChk = " ne 'x'";

            switch (formAssignType) {
                case "6":
                    strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (WorkOrderNum eq '" + woNum + "' and OprNum eq '' and tolower(Mandatory)" + strMandatoryChk + ")";
                    break;
                case "7":
                case "10":
                    if(!oprNum.isEmpty())
                    {
                        strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (WorkOrderNum eq '" + woNum + "' and OprNum eq '" + oprNum + "' and tolower(Mandatory)" + strMandatoryChk + ")";
                        break;
                    }
                    else {
                        ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, WorkOrder.getCurrWo().getWorkOrderNum());
                        ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                        ArrayList<ManualFormAssignmentSetModel> contentList = new ArrayList<ManualFormAssignmentSetModel>();
                        manualFormArraylist = new ArrayList<>();
                        for (Operation operation : totalOperations) {
                            strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (WorkOrderNum eq '" + operation.getWorkOrderNum() + "' and OprNum eq '" + operation.getOperationNum() + "' and tolower(Mandatory)" + strMandatoryChk + ")";
                            ArrayList<ZODataEntity> zoDataEntityArrayList = manualFormAssignmentHelper.getManualUnSubmittedMandatoryForm(strResPath);
                            manualFormArraylist = onFetchManualFormEntities(zoDataEntityArrayList);
                            if (manualFormArraylist != null && manualFormArraylist.size() > 0) {
                                unSubmittedFormsCount = manualFormArraylist.size();
                                for (ManualFormAssignmentSetModel form : manualFormArraylist) {
                                    //if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED && ZConfigManager.WO_OP_OBJS_DISPLAY.equalsIgnoreCase("x"))
                                        strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + woNum + "' and OperationNum eq '" + operation.getOperationNum() + "')";
                                    /*else
                                        strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + woNum + "')";*/
                                    responseObject = DataHelper.getInstance().getEntities(ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION, strResPath);
                                    if (!responseObject.isError()) {
                                        rawData = responseObject.Content();
                                        if (Integer.parseInt(rawData.toString()) > 0) {
                                            unSubmittedFormsCount--;
                                        }
                                    }
                                }
                            }
                        }
                        return unSubmittedFormsCount;
                    }
                case "8":
                    strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (Equipment eq '" + WorkOrder.getCurrWo().getEquipNum() + "' and tolower(Mandatory)" + strMandatoryChk + ")";
                    break;
                case "9":
                    strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (FunctionalLocation eq '" + WorkOrder.getCurrWo().getFuncLocation() + "' and tolower(Mandatory)" + strMandatoryChk + ")";
                    break;
               /* case "10":
                    if(!oprNum.isEmpty())
                    {
                        strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (WorkOrderNum eq '" + woNum + "' and OprNum eq '" + oprNum + "' and tolower(Mandatory)" + strMandatoryChk + ")";
                        break;
                    }
                    else {
                        ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, WorkOrder.getCurrWo().getWorkOrderNum());
                        ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                        ArrayList<ManualFormAssignmentSetModel> contentList = new ArrayList<ManualFormAssignmentSetModel>();
                        for (Operation operation : totalOperations) {
                            strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (WorkOrderNum eq '" + operation.getWorkOrderNum() + "' and OprNum eq '" + operation.getOperationNum() + "' and tolower(Mandatory)" + strMandatoryChk + ")";
                            ArrayList<ZODataEntity> zoDataEntityArrayList = manualFormAssignmentHelper.getManualUnSubmittedMandatoryForm(strResPath);
                            manualFormArraylist = onFetchManualFormEntities(zoDataEntityArrayList);
                            if (manualFormArraylist != null && manualFormArraylist.size() > 0) {
                                unSubmittedFormsCount = manualFormArraylist.size();
                                for (ManualFormAssignmentSetModel form : manualFormArraylist) {
                                    if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED && ZConfigManager.WO_OP_OBJS_DISPLAY.equalsIgnoreCase("x"))
                                        strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + woNum + "' and OperationNum eq '" + oprNum + "')";
                                    else
                                        strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + woNum + "')";
                                    responseObject = DataHelper.getInstance().getEntities(ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION, strResPath);
                                    if (!responseObject.isError()) {
                                        rawData = responseObject.Content();
                                        if (Integer.parseInt(rawData.toString()) > 0) {
                                            unSubmittedFormsCount--;
                                        }
                                    }
                                }
                            }
                            return unSubmittedFormsCount;
                        }
                    }
                default:
                    strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (WorkOrderNum eq '" + woNum + "' and OprNum eq '' and tolower(Mandatory)" + strMandatoryChk + ")";
                    break;*/
            }

            if (manualFormArraylist.size() > 0)
                manualFormArraylist.clear();
            ArrayList<ZODataEntity> zoDataEntityArrayList = manualFormAssignmentHelper.getManualUnSubmittedMandatoryForm(strResPath);
            manualFormArraylist = onFetchManualFormEntities(zoDataEntityArrayList);
            if (manualFormArraylist != null && manualFormArraylist.size() > 0) {
                unSubmittedFormsCount = manualFormArraylist.size();
                for (ManualFormAssignmentSetModel form : manualFormArraylist) {
                    if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED && ZConfigManager.WO_OP_OBJS_DISPLAY.equalsIgnoreCase("x"))
                        strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + woNum + "' and OperationNum eq '" + oprNum + "')";
                    else
                        strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + woNum + "')";
                    responseObject = DataHelper.getInstance().getEntities(ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION, strResPath);
                    if (!responseObject.isError()) {
                        rawData = responseObject.Content();
                        if (Integer.parseInt(rawData.toString()) > 0) {
                            unSubmittedFormsCount--;
                        }
                    }
                }
            }
            return unSubmittedFormsCount;
        } catch (NumberFormatException e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return 0;
    }
    public int getTotalNumManualForms() {
        int formsCount = 0;
        ResponseObject responseObject = new ResponseObject(ConfigManager.Status.Error);
        String strResPath = "";
        Object rawData = null;
        String entitySetName=ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET;
        try {
            String formAssignType = ZAppSettings.FormAssignmentType.getFormAssignmentType(ZConfigManager.FORM_ASSIGNMENT_TYPE);
            switch (formAssignType) {
                case "6":
                    strResPath = entitySetName + "/$count?$filter=(WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '')";
                    break;
                case "7":
                case "10":
                     if(ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED){
                         strResPath = entitySetName+ "/$count?$filter=(WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '" + WorkOrder.getCurrWo().getCurrentOperation().getOperationNum() + "')";
                         break;
                     }
                    else {
                    ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, WorkOrder.getCurrWo().getWorkOrderNum());
                    ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                    for (Operation operation : totalOperations) {
                        strResPath = entitySetName + "/$count?$filter= (WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '" + operation.getOperationNum() + "')";
                        //responseObject = ManualFormAssignmentSetModel.getObjectsFromEntity(ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET, strResPath);
                        responseObject = DataHelper.getInstance().getEntities(ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET, strResPath);
                        if (!responseObject.isError()) {
                            rawData = responseObject.Content();
                            if (rawData != null) {
                                formsCount += Integer.valueOf(String.valueOf(rawData));
                            }
                        }
                    }
                    return formsCount;
                }
                case "8":
                    strResPath = entitySetName + "/$count?$filter=(Equipment eq '" + WorkOrder.getCurrWo().getEquipNum() + "')";
                    break;
                case "9":
                    strResPath = entitySetName + "/$count?$filter=(FunctionalLocation eq '" +  WorkOrder.getCurrWo().getFuncLocation() + "')";
                    break;
                default:
                    strResPath = entitySetName + "/$count?$filter=(WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '')";
                    break;
            }
            responseObject = DataHelper.getInstance().getEntities(entitySetName, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                formsCount = Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return formsCount;
    }

}
