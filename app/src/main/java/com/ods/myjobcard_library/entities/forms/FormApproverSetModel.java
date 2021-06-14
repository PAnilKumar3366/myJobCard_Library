package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.sap.smp.client.odata.ODataEntity;

import java.sql.Time;
import java.util.GregorianCalendar;

public class FormApproverSetModel extends ZBaseEntity {

    private String FormID;
    private String Version;
    private String WorkOrderNum;
    private String OprNum;
    private String ApproverID;
    private String FormStatus;
    private GregorianCalendar AssignedDate;
    private Time AssignedTime;
    private String NotificationItem;
    private String NotificationTask;
    private String Equipment;
    private String FunctionalLocation;
    private String FormName;
    private String Notification;

    public FormApproverSetModel() {
        this.initializeEntityProperties();
    }

    public FormApproverSetModel(ZODataEntity entity) {
        create(entity);
        this.initializeEntityProperties();
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

    public String getFormName() {
        return FormName;
    }

    public void setFormName(String formName) {
        FormName = formName;
    }

    private String AssignedBy;

    public FormApproverSetModel(ODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.FROM_APPROVER_ENTITY_SET);
        this.setEntityType(ZCollections.FORM_APPROVER_ENTITY_TYPE);
        //this.setEntityResourcePath(ZCollections.FROM_APPROVER_ENTITY_SET);
        this.addKeyFieldNames("FormID");
        this.addKeyFieldNames("Version");
        this.addKeyFieldNames("WorkOrderNum");
        this.addKeyFieldNames("OprNum");
        this.addKeyFieldNames("ApproverID");
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

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getOprNum() {
        return OprNum;
    }

    public void setOprNum(String oprNum) {
        OprNum = oprNum;
    }

    public String getApproverID() {
        return ApproverID;
    }

    public void setApproverID(String approverID) {
        ApproverID = approverID;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }

    public String getFormStatus() {
        return FormStatus;
    }

    public void setFormStatus(String formStatus) {
        FormStatus = formStatus;
    }

    public GregorianCalendar getAssignedDate() {
        return AssignedDate;
    }

    public void setAssignedDate(GregorianCalendar assignedDate) {
        AssignedDate = assignedDate;
    }

    public Time getAssignedTime() {
        return AssignedTime;
    }

    public void setAssignedTime(Time assignedTime) {
        AssignedTime = assignedTime;
    }

    public String getAssignedBy() {
        return AssignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        AssignedBy = assignedBy;
    }
}
