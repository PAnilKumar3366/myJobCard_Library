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
import java.util.List;

/**
 * Created by lenovo on 16-05-2016.
 */
public class PlantSection extends BaseEntity {

    private String Plant;
    private String PlantSection;
    private String PersResp;
    private String PhonePersResp;


    public PlantSection(ODataEntity entity) {
        create(entity);
    }

    public static ResponseObject getPlantSections() {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.PLANT_SECTION_COLLECTION, ZCollections.PLANT_SECTION_COLLECTION);
            result = FromEntity((List<ODataEntity>) result.Content());
        } catch (Exception e) {
            DliteLogger.WriteLog(PlantSection.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<PlantSection> plantSections = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    plantSections.add(new PlantSection(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", plantSections);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(PlantSection.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }


    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getPlantSection() {
        return PlantSection;
    }

    public void setPlantSection(String plantSection) {
        PlantSection = plantSection;
    }

    public String getPersResp() {
        return PersResp;
    }

    public void setPersResp(String persResp) {
        PersResp = persResp;
    }

    public String getPhonePersResp() {
        return PhonePersResp;
    }

    public void setPhonePersResp(String phonePersResp) {
        PhonePersResp = phonePersResp;
    }
}
