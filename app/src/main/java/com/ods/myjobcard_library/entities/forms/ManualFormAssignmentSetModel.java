package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.ZCollections;
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
    private String FormAssignmentType;
    private String WorkOrderNum;
    private String OprNum;
    private String NotificationNum;
    private String ItemNum;
    private String TaskNum;
    private String Equipment;
    private String FunctionalLocation;
    private String Mandatory;
    private String MultipleSub;
    private String Occur;
    private String FormCategory;
    private boolean PostNotification;
    private String Theme;
    private String Stylesheet;
    private GregorianCalendar AssignedDate;
    private GregorianCalendar AssignedTime;
    private String AssignedBy;
    private String JobType;
    private String FlowSequence;
    private String ModifiedBy;
    private String FormName;
    private String TaskListType;
    private String Group;
    private String GroupCounter;
    private String InternalCounter;
    private String ObjectNum;


    public ManualFormAssignmentSetModel(String FormVersion, String FormName, String Mandatory, String MultipleSub, String Occur) {
        this.Version = FormVersion;
        this.FormID = FormName;
        this.Mandatory = Mandatory;
        this.Occur = Occur;
        this.MultipleSub = MultipleSub;
    }

    public ManualFormAssignmentSetModel(ODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    public ManualFormAssignmentSetModel(ZODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET);
        this.setEntityType(ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_TYPE);
        this.addKeyFieldNames("FormID");
        this.addKeyFieldNames("Version");
        this.addKeyFieldNames("FormAssignmentType");
        this.addKeyFieldNames("WorkOrderNum");
        this.addKeyFieldNames("OprNum");
        this.addKeyFieldNames("Notification");
        this.addKeyFieldNames("NotificationItem");
        this.addKeyFieldNames("NotificationTask");
        this.addKeyFieldNames("Equipment");
        this.addKeyFieldNames("FunctionalLocation");

    }

    public String getFormAssignmentType() {
        return FormAssignmentType;
    }

    public void setFormAssignmentType(String formAssignmentType) {
        FormAssignmentType = formAssignmentType;
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getNotificationNum() {
        return NotificationNum;
    }

    public void setNotificationNum(String notificationNum) {
        NotificationNum = notificationNum;
    }

    public String getItemNum() {
        return ItemNum;
    }

    public void setItemNum(String itemNum) {
        ItemNum = itemNum;
    }

    public String getTaskNum() {
        return TaskNum;
    }

    public void setTaskNum(String taskNum) {
        TaskNum = taskNum;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getFunctionalLocation() {
        return FunctionalLocation;
    }

    public void setFunctionalLocation(String functionalLocation) {
        FunctionalLocation = functionalLocation;
    }

    public boolean isPostNotification() {
        return PostNotification;
    }

    public void setPostNotification(boolean postNotification) {
        PostNotification = postNotification;
    }

    public String getFormName() {
        return FormName;
    }

    public void setFormName(String formName) {
        FormName = formName;
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

    public GregorianCalendar getAssignedDate() {
        return AssignedDate;
    }

    public void setAssignedDate(GregorianCalendar assignedDate) {
        AssignedDate = assignedDate;
    }

    public String getAssignedBy() {
        return AssignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        AssignedBy = assignedBy;
    }

    public GregorianCalendar getAssignedTime() {
        return AssignedTime;
    }

    public void setAssignedTime(GregorianCalendar assignedTime) {
        AssignedTime = assignedTime;
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

    public String getFormCategory() {
        return FormCategory;
    }

    public void setFormCategory(String formCategory) {
        FormCategory = formCategory;
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
    public boolean isGeneralForm() {
        return getFormCategory() != null && getFormCategory().equalsIgnoreCase("NonObject");
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
