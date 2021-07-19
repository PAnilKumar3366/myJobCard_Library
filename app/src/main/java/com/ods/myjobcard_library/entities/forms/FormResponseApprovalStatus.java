package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.viewmodels.ApproverMasterHelper;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.sap.smp.client.odata.ODataEntity;

import java.sql.Time;
import java.util.GregorianCalendar;

public class FormResponseApprovalStatus extends ZBaseEntity {

    private String FormID;
    private String Version;
    private String FormInstanceID;
    private String ApproverID;
    private String FormSubmittedBy;
    private String FormContentStatus;
    private String Remarks;
    private GregorianCalendar CreatedDate;
    private Time CreatedTime;
    private String Counter;
    private String FormName;

    private String IterationRequired;

    public FormResponseApprovalStatus() {
        initializeEntityProperties();
    }

    public String getIterationRequired() {
        return IterationRequired;
    }

    public FormResponseApprovalStatus(ODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    public FormResponseApprovalStatus(ZODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    public void setIterationRequired(String iterationRequired) {
        IterationRequired = iterationRequired;
    }

    public FormResponseApprovalStatus(String FormID, String Version, String FormInstanceID, String FormSubmittedBy, String ApproverID) {
        this.FormID = FormID;
        this.Version = Version;
        this.FormInstanceID = FormInstanceID;
        this.FormSubmittedBy = FormSubmittedBy;
        this.ApproverID = ApproverID;
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

    public Time getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(Time createdTime) {
        CreatedTime = createdTime;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.FORM_RESPONSE_APPROVAL_STATUS_ENTITY_SET);
        this.setEntityType(ZCollections.FORM_RESPONSE_APPROVAL_ENTITY_TYPE);
        this.addKeyFieldNames("FormID");
        this.addKeyFieldNames("Version");
        this.addKeyFieldNames("FormInstanceID");
        this.addKeyFieldNames("ApproverID");
        this.addKeyFieldNames("FormSubmittedBy");
    }

    public String getApproverName() {
        ApproverMasterHelper helper = new ApproverMasterHelper();
        ApproverMasterData approver = helper.fetchApproverName(this.ApproverID);
        if (approver != null) {
            return approver.getFirstName() + approver.getLastName();
        }
        return "";
    }
}
