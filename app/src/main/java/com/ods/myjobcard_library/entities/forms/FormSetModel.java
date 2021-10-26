package com.ods.myjobcard_library.entities.forms;

import android.util.Log;

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
    private String CodeGroup;
    private String Stylesheet;
    private String FormModel;
    private String Theme;
    private String FormHTML;

    private String FunctionalArea;
    private String SubArea;
    private String Category;


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
           /* if (!isFormData)
                resourcePath += "&$select=FormName,Description,Active,FunctionalArea,SubArea,Category";*/ //Tempororily completed this line of code due to newly fileds are not available in petronas Dev system.Once enable then this line should be uncommented

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

    public static ArrayList<FormSetModel> getMasterChecklists(int skipValue, int numRecords, boolean inStockAvail) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String entitySetName = "FormMasterSet?$skip=" + skipValue + " &$top=" + numRecords;
        String filterQuery = "";
        filterQuery = "&$filter=";
        String resPath = "";
        ArrayList<FormSetModel> allCheckList = new ArrayList<>();
        try {
            /*if (plant != null && !plant.isEmpty()) {
                resPath = resPath + "Plant eq '" + plant + "'";
            }

            if (storeId != null && !storeId.isEmpty()) {
                resPath = resPath + (!resPath.isEmpty() ? " and " : "") + "MaterialStorageLocation eq '" + storeId + "'";
            }

            if (material != null && !material.isEmpty()) {
                resPath = resPath + (!resPath.isEmpty() ? " and " : "") + "Material eq '" + material + "'";
            }

            if (inStockAvail) {
                resPath = resPath + (!resPath.isEmpty() ? " and " : "") + "Stock gt 0";
            }*/

            resPath = entitySetName + resPath;
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities("FormMasterSet", resPath);
            result = FromEntity((List) result.Content());
            allCheckList = (ArrayList<FormSetModel>) result.Content();
        } catch (Exception var12) {
            Log.e(FormSetModel.class.getName(), "getMasterChecklists: " + var12.getMessage());
            DliteLogger.WriteLog(FormSetModel.class, ZAppSettings.LogLevel.Error, var12.getMessage());
            return allCheckList;
        }

        return allCheckList;
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

    public boolean isActive() {
        return Active;
    }

    public String getCodeGroup() {
        return CodeGroup;
    }

    public void setCodeGroup(String codeGroup) {
        CodeGroup = codeGroup;
    }

    public String getStylesheet() {
        return Stylesheet;
    }

    public void setStylesheet(String stylesheet) {
        Stylesheet = stylesheet;
    }

    public String getFormModel() {
        return FormModel;
    }

    public void setFormModel(String formModel) {
        FormModel = formModel;
    }

    public String getTheme() {
        return Theme;
    }

    public void setTheme(String theme) {
        Theme = theme;
    }

    public String getFormHTML() {
        return FormHTML;
    }

    public void setFormHTML(String formHTML) {
        FormHTML = formHTML;
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

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }


    @Override
    public String toString() {
        return this.getFormName() + "\n" + this.getVersion() + "\n" + this.getFormID() + "\n";
    }
}
