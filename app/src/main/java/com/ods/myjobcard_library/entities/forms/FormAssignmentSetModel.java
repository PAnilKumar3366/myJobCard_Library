package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by kansal on 10/06/2016.
 * (Ruchi) this model is reading the data from the FormAssignmentSet service.
 */
public class FormAssignmentSetModel extends BaseEntity {

    private String FormID;
    private String ControlKey;
    private String Version;
    private String OrderType;
    private String JobType;
    private String Mandatory;
    private String FlowSequence;
    private String Category;
    private String MultipleSub;
    private String Occur;
    private GregorianCalendar CreatedOn;
    private String CreatedBy;
    private GregorianCalendar ModifiedOn;
    private String ModifiedBy;
    private String Theme;
    private String EquipCategory;
    private String FuncLocCategory;
    private String Stylesheet;

    public FormAssignmentSetModel(ODataEntity entity) {
        create(entity);
    }

    public static ArrayList<FormAssignmentSetModel> getFormAssignmentData(String orderType, String controlKey, String equipmentCat, String funcLocCat) {
        ArrayList<FormAssignmentSetModel> assignedForms = new ArrayList<>();
        try {
            String strResPath;
            String entitySetName = ZCollections.FORM_ASSIGNMENT_COLLECTION;
            if (!equipmentCat.isEmpty())
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter=EquipCategory eq '" + equipmentCat + "'&$orderby=FlowSequence asc,Mandatory desc";
            else if (!funcLocCat.isEmpty())
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter=FuncLocCategory eq '" + funcLocCat + "'&$orderby=FlowSequence asc,Mandatory desc";
            else if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED || !ZConfigManager.WO_OP_OBJS_DISPLAY.equalsIgnoreCase("x"))
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter= (ControlKey eq '' and OrderType eq '" + orderType + "')&$orderby=FlowSequence asc,Mandatory desc";
            else
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter= (ControlKey eq '" + controlKey + "' and OrderType eq '" + orderType + "')&$orderby=FlowSequence asc,Mandatory desc";


            ResponseObject result = getObjectsFromEntity(entitySetName, strResPath);
            if (!result.isError())
                return (ArrayList<FormAssignmentSetModel>) result.Content();
        } catch (Exception e) {
            DliteLogger.WriteLog(FormAssignmentSetModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return assignedForms;
    }

    //getting General FormItems List
    public static ArrayList<FormAssignmentSetModel> getGeneralFormAssignmentData() {
        ArrayList<FormAssignmentSetModel> assignedForms = new ArrayList<>();
        try {
            String strResPath = null;
            String entitySetName = ZCollections.FORM_ASSIGNMENT_COLLECTION;
            strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter=Category eq 'NonObject'&$orderby=FlowSequence asc";//Version eq '000'
            ResponseObject result = getObjectsFromEntity(entitySetName, strResPath);
            if (!result.isError())
                return (ArrayList<FormAssignmentSetModel>) result.Content();
        } catch (Exception e) {
            DliteLogger.WriteLog(FormAssignmentSetModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return assignedForms;
    }

    public static FormAssignmentSetModel getFormByCategory(String orderType, String category) {
        FormAssignmentSetModel form = null;
        try {
            String entitySetName = ZCollections.FORM_ASSIGNMENT_COLLECTION;
            String strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter= (" + (orderType != null && !orderType.isEmpty() ? ("OrderType eq '" + orderType + "' and ") : "") + "Category eq '" + category + "')&$orderby=Mandatory desc";
            ResponseObject result = getObjectsFromEntity(entitySetName, strResPath);
            if (!result.isError())
                form = ((ArrayList<FormAssignmentSetModel>) result.Content()).get(0);
        } catch (Exception e) {
            DliteLogger.WriteLog(FormAssignmentSetModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return form;
    }

    public static FormAssignmentSetModel getFromById(String formID, String version) {
        FormAssignmentSetModel form = null;
        try {
            String entitySetName = ZCollections.FORM_ASSIGNMENT_COLLECTION;
            //ControlKey eq '" + controlKey + "' and OrderType eq '" + orderType + "'
            String strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter= (" + "FormID eq '" + formID + "' and Version eq '" + version + "')";
            ResponseObject result = getObjectsFromEntity(entitySetName, strResPath);
            if (!result.isError())
                form = ((ArrayList<FormAssignmentSetModel>) result.Content()).get(0);
        } catch (Exception e) {
            DliteLogger.WriteLog(FormAssignmentSetModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return form;
    }

    public static ResponseObject getObjectsFromEntity(String entitySetName, String resPath) {
        ResponseObject result = null;
        try {
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (!result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                ArrayList<FormAssignmentSetModel> content = null;
                FormAssignmentSetModel formAssignmentSetModel;
                content = new ArrayList<FormAssignmentSetModel>();
                for (ODataEntity entity : entities) {
                    formAssignmentSetModel = new FormAssignmentSetModel(entity);
                    //result = wo.fromEntity(entity);
                    if (formAssignmentSetModel != null) {
                        content.add(formAssignmentSetModel);
                    } else {
                        //pending: log the error message
                    }
                }
                if (result == null) {
                    result = new ResponseObject(ZConfigManager.Status.Success);
                }
                result.setMessage("");
                result.setContent(content);
            }
        } catch (Exception e) {
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        if (result != null)
            return result;
        else
            return new ResponseObject(ZConfigManager.Status.Error);
    }

    public String getFormID() {
        return FormID;
    }

    public void setFormID(String formID) {
        FormID = formID;
    }

    public String getControlKey() {
        return ControlKey;
    }

    public void setControlKey(String controlKey) {
        ControlKey = controlKey;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getJobType() {
        return JobType;
    }

    public void setJobType(String jobType) {
        JobType = jobType;
    }

    public String getFlowSequence() {
        return FlowSequence;
    }

    public void setFlowSequence(String flowSequence) {
        FlowSequence = flowSequence;
    }

    public String getStylesheet() {
        return Stylesheet;
    }

    public void setStylesheet(String stylesheet) {
        Stylesheet = stylesheet;
    }

    public GregorianCalendar getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(GregorianCalendar createdOn) {
        CreatedOn = createdOn;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public GregorianCalendar getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(GregorianCalendar modifiedOn) {
        ModifiedOn = modifiedOn;
    }

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public String getMandatory() {
        return Mandatory;
    }

    public void setMandatory(String mandatory) {
        Mandatory = mandatory;
    }

    public String getMultipleSub() {
        return MultipleSub;
    }

    public void setMultipleSub(String multipleSub) {
        MultipleSub = multipleSub;
    }

    public String getOccur() {
        return Occur;
    }

    public void setOccur(String occur) {
        Occur = occur;
    }

    public int getOccurInt() {
        return Integer.parseInt(Occur);
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTheme() {
        return Theme;
    }

    public void setTheme(String theme) {
        Theme = theme;
    }

    public boolean isGridTheme() {
        return getTheme().toLowerCase().contains("grid");
    }
    // this method is reading the data from offline store

    public String getEquipCategory() {
        return EquipCategory;
    }

    public void setEquipCategory(String equipCategory) {
        EquipCategory = equipCategory;
    }

    public String getFuncLocCategory() {
        return FuncLocCategory;
    }

    public void setFuncLocCategory(String funcLocCategory) {
        FuncLocCategory = funcLocCategory;
    }

    public boolean isGeneralForm() {
        return getCategory() != null && getCategory().equalsIgnoreCase("NonObject");
    }
}
