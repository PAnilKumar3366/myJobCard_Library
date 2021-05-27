package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.sap.smp.client.odata.ODataEntity;

import java.util.GregorianCalendar;

/**
 * this model is reading the data from the ManualFormAssignmentSet service.
 */
public class ManualFormAssignmentSetModel extends ZBaseEntity {

    private String FormID;
    private String Version;
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

    private String TaskListType;
    private String Group;
    private String GroupCounter;
    private String InternalCounter;

    private String ObjectCategory;
    private String ObjectNum;
    private String OprNum;


    public ManualFormAssignmentSetModel(String FormVersion, String FormName, String Mandatory) {
        this.Version = FormVersion;
        this.FormID = FormName;
        this.Mandatory = Mandatory;
    }

    public ManualFormAssignmentSetModel(ODataEntity entity) {
        create(entity);
    }

    public ManualFormAssignmentSetModel(ZODataEntity entity) {
        create(entity);
    }

    public String getFormID() {
        return FormID;
    }

    public void setFormID(String formID) {
        FormID = formID;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
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

    public String getTaskListType() {
        return TaskListType;
    }

    public void setTaskListType(String taskListType) {
        TaskListType = taskListType;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }

    public String getGroupCounter() {
        return GroupCounter;
    }

    public void setGroupCounter(String groupCounter) {
        GroupCounter = groupCounter;
    }

    public String getInternalCounter() {
        return InternalCounter;
    }

    public void setInternalCounter(String internalCounter) {
        InternalCounter = internalCounter;
    }

    public String getObjectCategory() {
        return ObjectCategory;
    }

    public void setObjectCategory(String objectCategory) {
        ObjectCategory = objectCategory;
    }

    public String getObjectNum() {
        return ObjectNum;
    }

    public void setObjectNum(String objectNum) {
        ObjectNum = objectNum;
    }

    public String getOprNum() {
        return OprNum;
    }

    public void setOprNum(String oprNum) {
        OprNum = oprNum;
    }
}
