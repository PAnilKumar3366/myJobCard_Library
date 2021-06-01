package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.sap.smp.client.odata.ODataEntity;

import java.util.GregorianCalendar;

public class FormInstanceStatus extends ZBaseEntity {

    private String FormID;
    private String FormVersion;
    private String FormInstanceID;
    private String ApproverID;
    private String FormSubmittedBy;
    private String FormContentStatus;
    private String Remarks;
    private GregorianCalendar CreatedDate;
    private GregorianCalendar CreatedTime;

    public FormInstanceStatus(ODataEntity entity) {
        create(entity);
        initializeEntityProperties();
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

    public String getFormInstanceID() {
        return FormInstanceID;
    }

    public void setFormInstanceID(String formInstanceID) {
        FormInstanceID = formInstanceID;
    }

    public String getApproverID() {
        return ApproverID;
    }

    public void setApproverID(String approverID) {
        ApproverID = approverID;
    }

    public String getFormSubmittedBy() {
        return FormSubmittedBy;
    }

    public void setFormSubmittedBy(String formSubmittedBy) {
        FormSubmittedBy = formSubmittedBy;
    }

    public String getFormContentStatus() {
        return FormContentStatus;
    }

    public void setFormContentStatus(String formContentStatus) {
        FormContentStatus = formContentStatus;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public GregorianCalendar getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(GregorianCalendar createdDate) {
        CreatedDate = createdDate;
    }

    public GregorianCalendar getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(GregorianCalendar createdTime) {
        CreatedTime = createdTime;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.FORM_INSTANCE_STATUS_ENTITY_SET);
        this.setEntityType(ZCollections.FORM_INSTANCE_STATUS_ENTITY_TYPE);
        this.addKeyFieldNames("FormID");
        this.addKeyFieldNames("FormVersion");
        this.addKeyFieldNames("FormInstanceID");
        this.addKeyFieldNames("ApproverID");
        this.addKeyFieldNames("FormSubmittedBy");
    }


}
