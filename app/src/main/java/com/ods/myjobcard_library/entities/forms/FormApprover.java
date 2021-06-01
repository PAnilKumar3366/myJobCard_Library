package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.sap.smp.client.odata.ODataEntity;

import java.util.GregorianCalendar;

public class FormApprover extends ZBaseEntity {

    private String FormID;
    private String FormVersion;
    private String ObjectNumber;
    private String OperationNumber;
    private String ApproverID;
    private String ObjectCategory;
    private String FormStatus;
    private GregorianCalendar AssignedDate;
    private GregorianCalendar AssignedTime;
    private String AssignedBy;

    public FormApprover(ODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.FROM_APPROVER_ENTITY_SET);
        this.setEntityType(ZCollections.FORM_APPROVER_ENTITY_TYPE);
        this.addKeyFieldNames("FormID");
        this.addKeyFieldNames("FormVersion");
        this.addKeyFieldNames("ObjectNumber");
        this.addKeyFieldNames("OperationNumber");
        this.addKeyFieldNames("ApproverID");
    }

    public String getFormID() {
        return FormID;
    }

    public void setFormID(String formID) {
        FormID = formID;
    }

    public String getFormVersion() {
        return FormVersion;
    }

    public void setFormVersion(String formVersion) {
        FormVersion = formVersion;
    }

    public String getObjectNumber() {
        return ObjectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        ObjectNumber = objectNumber;
    }

    public String getOperationNumber() {
        return OperationNumber;
    }

    public void setOperationNumber(String operationNumber) {
        OperationNumber = operationNumber;
    }

    public String getApproverID() {
        return ApproverID;
    }

    public void setApproverID(String approverID) {
        ApproverID = approverID;
    }

    public String getObjectCategory() {
        return ObjectCategory;
    }

    public void setObjectCategory(String objectCategory) {
        ObjectCategory = objectCategory;
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

    public GregorianCalendar getAssignedTime() {
        return AssignedTime;
    }

    public void setAssignedTime(GregorianCalendar assignedTime) {
        AssignedTime = assignedTime;
    }

    public String getAssignedBy() {
        return AssignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        AssignedBy = assignedBy;
    }
}
