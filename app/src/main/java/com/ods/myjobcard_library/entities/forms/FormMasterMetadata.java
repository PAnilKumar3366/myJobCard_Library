package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.sap.smp.client.odata.ODataEntity;

/**
 * This Model class reading the data from Service and contains the Actual data
 */
public class FormMasterMetadata extends ZBaseEntity {

    private String FormID;
    private String Version;
    private String FormName;
    private String Description;
    private boolean Active;
    private String CreatedBy;
    private String FormCategory;
    private String FunctionalArea;
    private String SubArea;


    public FormMasterMetadata(ODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    public FormMasterMetadata(ZODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.FORM_MASTER_METADATA_ENTITY_SET);
        this.setEntityType(ZCollections.FORM_MASTER_METADATA_ENTITY_TYPE);
        this.addKeyFieldNames("FormID");
        this.addKeyFieldNames("Version");
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

    public String getFormName() {
        return FormName;
    }

    public void setFormName(String formName) {
        FormName = formName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getFormCategory() {
        return FormCategory;
    }

    public void setFormCategory(String formCategory) {
        FormCategory = formCategory;
    }

    public String getFunctionalArea() {
        return FunctionalArea;
    }

    public void setFunctionalArea(String functionalArea) {
        FunctionalArea = functionalArea;
    }

    public String getSubArea() {
        return SubArea;
    }

    public void setSubArea(String subArea) {
        SubArea = subArea;
    }

}
