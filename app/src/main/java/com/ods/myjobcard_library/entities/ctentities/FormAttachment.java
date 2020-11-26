package com.ods.myjobcard_library.entities.ctentities;

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

public class FormAttachment extends BaseEntity {

    private String InstanceId;
    private String FormId;
    private String Version;
    private String AttachCounter;
    private String FileName;
    private String MimeType;
    private String Description;
    private String ObjectNum;
    private String OperationNum;
    private String Equipment;
    private String FunctionalLoc;
    private String ImageData;
    private String QuestionId;
    private GregorianCalendar CreatedOn;
    private String CreatedBy;
    private GregorianCalendar ModifiedOn;
    private String ModifiedBy;

    public FormAttachment(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public FormAttachment() {
        initializeEntityProperties();
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<FormAttachment> formAttachments = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    formAttachments.add(new FormAttachment(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", formAttachments);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
        }
        return result;
    }

    public static ArrayList<FormAttachment> getFormAttachmentData(String formId, String version, String instanceId) {
        ResponseObject result = null;
        String resourcePath = null;
        try {
            if (formId == null)
                formId = "";
            if (version == null)
                version = "";

            String entitySetName = ZCollections.FORM_ATTACHMENT_SET;
            resourcePath = entitySetName;
            //if(instanceId == null)
            resourcePath += "?$filter=(tolower(FormId) eq '" + formId.toLowerCase() + "' and Version eq '" + version + "' and InstanceId eq '" + instanceId + "')";
            //else
            // resourcePath += "?$filter=(InstanceId eq '"+ instanceId +"')";

            result = DataHelper.getInstance().getEntities(entitySetName, resourcePath);

            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(FormAttachment.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

        if (result != null && !result.isError())
            return (ArrayList<FormAttachment>) result.Content();
        else
            return new ArrayList<>();
    }

    public String getInstanceId() {
        return InstanceId;
    }

    public void setInstanceId(String instanceId) {
        InstanceId = instanceId;
    }

    public String getFormId() {
        return FormId;
    }

    public void setFormId(String formId) {
        FormId = formId;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getAttachCounter() {
        return AttachCounter;
    }

    public void setAttachCounter(String attachCounter) {
        AttachCounter = attachCounter;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getMimeType() {
        return MimeType;
    }

    public void setMimeType(String mimeType) {
        MimeType = mimeType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getObjectNum() {
        return ObjectNum;
    }

    public void setObjectNum(String objectNum) {
        ObjectNum = objectNum;
    }

    public String getOperationNum() {
        return OperationNum;
    }

    public void setOperationNum(String operationNum) {
        OperationNum = operationNum;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getFunctionalLoc() {
        return FunctionalLoc;
    }

    public void setFunctionalLoc(String functionalLoc) {
        FunctionalLoc = functionalLoc;
    }

    public String getImageData() {
        return ImageData;
    }

    public void setImageData(String imageData) {
        ImageData = imageData;
    }

    public String getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(String questionId) {
        QuestionId = questionId;
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

    private void initializeEntityProperties() {
        this.setEntityType(ZCollections.FORM_ATTACHMENT_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.FORM_ATTACHMENT_SET);
        this.addKeyFieldNames("InstanceId");
        this.addKeyFieldNames("FormId");
        this.addKeyFieldNames("Version");
        this.addKeyFieldNames("AttachCounter");
    }


}
