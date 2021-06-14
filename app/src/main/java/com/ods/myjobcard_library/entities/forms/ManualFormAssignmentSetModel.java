package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.sap.smp.client.odata.ODataEntity;

import java.sql.Time;
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
    private String MultipleSub;
    private String Occur;
    private String Theme;
    private String Stylesheet;
    private String FormCategory;
    private boolean PostNotification;
    private GregorianCalendar AssignedDate;
    private Time AssignedTime;
    private String AssignedBy;
    private String FormName;
    private boolean Active;
    private String FormAssignmentType;
    private String WorkOrderNum;
    private String OprNum;
    private String Notification;
    private String NotificationItem;
    private String NotificationTask;
    private String Equipment;
    private String FunctionalLocation;

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
    public ManualFormAssignmentSetModel(){
        initializeEntityProperties();
    }
    private void initializeEntityProperties(){
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

    public String getTheme() {
        return Theme;
    }

    public void setTheme(String theme) {
        Theme = theme;
    }

    public boolean isGridTheme() {
        return getTheme().toLowerCase().contains("grid");
    }

    public String getOprNum() {
        return OprNum;
    }

    public void setOprNum(String oprNum) {
        OprNum = oprNum;
    }

    public String getFormCategory() {
        return FormCategory;
    }

    public void setFormCategory(String formCategory) {
        FormCategory = formCategory;
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

    public String getFormName() {
        return FormName;
    }

    public void setFormName(String formName) {
        FormName = formName;
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

    public Time getAssignedTime() {
        return AssignedTime;
    }

    public void setAssignedTime(Time assignedTime) {
        AssignedTime = assignedTime;
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

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }

    public String getNotificationItem() {
        return NotificationItem;
    }

    public void setNotificationItem(String notificationItem) {
        NotificationItem = notificationItem;
    }

    public String getNotificationTask() {
        return NotificationTask;
    }

    public void setNotificationTask(String notificationTask) {
        NotificationTask = notificationTask;
    }

    public boolean isPostNotification() {
        return PostNotification;
    }

    public void setPostNotification(boolean postNotification) {
        PostNotification = postNotification;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }
}
