package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by lenovo on 24-05-2016.
 * (Ruchi) this model is reading the data from FormMaster service and contains the actual xml string of the form in FormData field
 */
public class FormSetModel extends ZBaseEntity {

    private String FormID;
    private String FormName;
    private String Version;
    private String Description;
    private String FormData;
    private boolean Active;
    private GregorianCalendar CreatedOn;
    private String CreatedBy;
    private GregorianCalendar ModifiedOn;
    private String ModifiedBy;

    public FormSetModel(ODataEntity entity) {
        create(entity);
    }

    public static ArrayList<FormSetModel> getFormsData(String formID, String version, boolean isFormData) {
        ResponseObject result = null;
        String resourcePath = null;// "WoHeaderSet?$expand=NavOpera/OperaToPRT";
        ArrayList<FormSetModel> formSetModels = new ArrayList<>();
        try {
            if (formID == null)
                formID = "";
            if (version == null)
                version = "";

            String entitySetName = ZCollections.FORMS_COLLECTION;
            resourcePath = entitySetName;
            resourcePath += "?$filter=(FormID eq '" + formID + "' and Version eq '" + version + "')";
            if (!isFormData)
                resourcePath += "&$select=FormName,Description,Active";

            result = DataHelper.getInstance().getEntities(entitySetName, resourcePath);
            if (!result.isError()) {
                //parse data for Equipment Characteristics
                result = FromEntity((List<ODataEntity>) result.Content());
                formSetModels = (ArrayList<FormSetModel>) result.Content();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(FormSetModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return formSetModels;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<FormSetModel> formSetModels = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    formSetModels.add(new FormSetModel(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", formSetModels);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(FormSetModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getFormID() {
        return FormID;
    }

    public void setFormID(String formID) {
        this.FormID = formID;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        this.Version = version;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        this.CreatedBy = createdBy;
    }

    public GregorianCalendar getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(GregorianCalendar createdOn) {
        this.CreatedOn = createdOn;
    }

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.ModifiedBy = modifiedBy;
    }

    public GregorianCalendar getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(GregorianCalendar modifiedOn) {
        this.ModifiedOn = modifiedOn;
    }

    public String getFormName() {
        return FormName;
    }

    public void setFormName(String formName) {
        this.FormName = formName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getFormData() {
        return FormData;
    }

    public void setFormData(String formData) {
        FormData = formData;
    }

    // this method is reading the data from offline store

    public boolean getActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    @Override
    public String toString() {
        return this.getFormName() + "\n" + this.getVersion() + "\n" + this.getFormID() + "\n";
    }
}
