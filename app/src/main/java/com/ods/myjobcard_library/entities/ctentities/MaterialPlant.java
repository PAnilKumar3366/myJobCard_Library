package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZAppSettings;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 16-05-2016.
 */
public class MaterialPlant extends BaseEntity {

    private String Material;
    private String Plant;
    private String MaterialType;
    private String IndustrySector;
    private String MaterialGroup;
    private String BaseUnit;
    private String OrderUnit;
    private String MaterialDescription;

    public MaterialPlant(ODataEntity entity) {
        create(entity);
    }

    //utility methods
    public static ArrayList<MaterialPlant> searchMaterialPlants(String searchText, String searchOption, ArrayList<MaterialPlant> materials) {

        ArrayList<MaterialPlant> searchedMaterials = new ArrayList<MaterialPlant>();
        if (searchOption.equals(ZCollections.SEARCH_OPTION_ID)) {
            for (MaterialPlant material : materials) {
                if (material.getMaterial().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchedMaterials.add(material);
                }
            }
        } else if (searchOption.equals(ZCollections.SEARCH_OPTION_DESCRIPTION)) {
            for (MaterialPlant materialPlant : materials) {
                if (materialPlant.getMaterialDescription().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchedMaterials.add(materialPlant);
                }
            }
        } else if (searchOption.equals(ZCollections.SEARCH_ITEM_PLANT)) {
            for (MaterialPlant materialPlant : materials) {
                if (materialPlant.getPlant().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchedMaterials.add(materialPlant);
                }
            }
        } else {
            for (MaterialPlant material : materials) {
                if (material.getMaterial().trim().toLowerCase().contains(searchText.toLowerCase()) || material.getMaterialDescription().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchedMaterials.add(material);
                }
            }
        }
        return searchedMaterials;

    }

    //get methods
    public static ResponseObject getMaterialPlant(String plantId) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String resPath = ZCollections.MATERIAL_PLANT_COLLECTION;
        if (plantId != null) {
            resPath += "?$filter=(Plant eq '" + plantId + "')";
        }
        try {
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.MATERIAL_PLANT_COLLECTION, resPath);
            result = FromEntity((List<ODataEntity>) result.Content());
        } catch (Exception e) {
            DliteLogger.WriteLog(MaterialPlant.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<MaterialPlant> materialPlants = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    materialPlants.add(new MaterialPlant(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", materialPlants);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(MaterialPlant.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String material) {
        Material = material;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getMaterialType() {
        return MaterialType;
    }

    public void setMaterialType(String materialType) {
        MaterialType = materialType;
    }

    public String getIndustrySector() {
        return IndustrySector;
    }

    public void setIndustrySector(String industrySector) {
        IndustrySector = industrySector;
    }

    public String getMaterialGroup() {
        return MaterialGroup;
    }

    public void setMaterialGroup(String materialGroup) {
        MaterialGroup = materialGroup;
    }

    public String getBaseUnit() {
        return BaseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        BaseUnit = baseUnit;
    }

    public String getOrderUnit() {
        return OrderUnit;
    }

    public void setOrderUnit(String orderUnit) {
        OrderUnit = orderUnit;
    }

    public String getMaterialDescription() {
        return MaterialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        MaterialDescription = materialDescription;
    }
}
