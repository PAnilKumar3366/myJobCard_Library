package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MY HOME on 9/22/2017.
 */
public class EquipmentCategory extends ZBaseEntity {

    private String EquipCategory;
    private String Language;
    private String CategoryDesc;

    public EquipmentCategory() {
    }

    public EquipmentCategory(ODataEntity entity) {
        create(entity);
    }

    public static ArrayList<SpinnerItem> getCategoriesForSpinner() {
        ArrayList<SpinnerItem> categories = new ArrayList<>();
        try {
            String entitySetName = ZCollections.EQUIPMENT_CATEGORY_COLLECTION;
            String resPath = entitySetName;
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                String categoryId;
                String description;
                for (ODataEntity entity : entities) {
                    categoryId = String.valueOf(entity.getProperties().get("EquipCategory").getValue());
                    description = String.valueOf(entity.getProperties().get("CategoryDesc").getValue());
                    categories.add(new SpinnerItem(categoryId, description));
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(EquipmentCategory.class, ZAppSettings.LogLevel.Error, e.getMessage());
            //ClientLogManager.writeLogError("Error occurred while reading Equipment Categories from offline store.", e);
        }
        return categories;
    }

    public String getEquipCategory() {
        return EquipCategory;
    }

    public void setEquipCategory(String equipCategory) {
        EquipCategory = equipCategory;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getCategoryDesc() {
        return CategoryDesc;
    }

    public void setCategoryDesc(String description) {
        CategoryDesc = description;
    }
}
