package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.sap.smp.client.odata.ODataEntity;

import java.util.GregorianCalendar;

public class FormResponseApprovalStatus extends ZBaseEntity {

    private String FormID;
    private String Version;
    private String FormInstanceID;
    private String ApproverID;
    private String FormSubmittedBy;
    private String FormInstanceStatus;
    private String Remarks;
    private GregorianCalendar CreatedDate;
    private GregorianCalendar CreatedTime;
    private String Counter;
    private String FormName;

    public FormResponseApprovalStatus(ODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
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

    public String getFormInstanceStatus() {
        return FormInstanceStatus;
    }

    public void setFormInstanceStatus(String formInstanceStatus) {
        FormInstanceStatus = formInstanceStatus;
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
