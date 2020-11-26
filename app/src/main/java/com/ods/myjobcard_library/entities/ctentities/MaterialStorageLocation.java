package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 16-05-2016.
 */
public class MaterialStorageLocation extends BaseEntity {

    private String MaterialDescription;
    private String Material;
    private String Plant;
    private String MaterialStorageLocation;
    private String MaterialType;
    private String IndustrySector;
    private String MaterialGroup;
    private String BaseUnit;
    private BigDecimal Stock;

    //newly added fields
    private String StorageBin;
    private String EnteredBy;

    public MaterialStorageLocation(ODataEntity entity) {
        create(entity);
    }

    public static ResponseObject getPlantMaterialStorage(String plant, String storeId, String material, boolean top, boolean inStockAvail) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String entitySetName = ZCollections.MATERIAL_STORAGE_COLLECTION;
        String filterQuery = "";
        filterQuery = "?$filter=";
        String resPath = "";
        try {
            if (plant != null && !plant.isEmpty())
                resPath += "Plant eq '" + plant + "'";
            if (storeId != null && !storeId.isEmpty())
                resPath += (!resPath.isEmpty() ? " and " : "") + "MaterialStorageLocation eq '" + storeId + "'";
            if (material != null && !material.isEmpty())
                resPath += (!resPath.isEmpty() ? " and " : "") + "Material eq '" + material + "'";
            if (inStockAvail)
                resPath += (!resPath.isEmpty() ? " and " : "") + "Stock gt 0";
            if (top)
                resPath += "&$top=" + ZConfigManager.MATERIALSTOCK_TOP + "";
            resPath = entitySetName + filterQuery + resPath;
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.MATERIAL_STORAGE_COLLECTION, resPath);
            result = FromEntity((List<ODataEntity>) result.Content());
        } catch (Exception e) {
            DliteLogger.WriteLog(MaterialStorageLocation.class, AppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public static ResponseObject getPlantMaterialStorageNew(String plant, String storeId, String material, int skipValue, int numRecords, boolean inStockAvail) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String entitySetName = ZCollections.MATERIAL_STORAGE_COLLECTION + "?$skip=" + skipValue + " &$top=" + numRecords;
        String filterQuery = "";
        filterQuery = "&$filter=";
        String resPath = "";
        try {
            if (plant != null && !plant.isEmpty())
                resPath += "Plant eq '" + plant + "'";
            if (storeId != null && !storeId.isEmpty())
                resPath += (!resPath.isEmpty() ? " and " : "") + "MaterialStorageLocation eq '" + storeId + "'";
            if (material != null && !material.isEmpty())
                resPath += (!resPath.isEmpty() ? " and " : "") + "Material eq '" + material + "'";
            if (inStockAvail)
                resPath += (!resPath.isEmpty() ? " and " : "") + "Stock gt 0";

            resPath = entitySetName + filterQuery + resPath;
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.MATERIAL_STORAGE_COLLECTION, resPath);
            result = FromEntity((List<ODataEntity>) result.Content());
        } catch (Exception e) {
            DliteLogger.WriteLog(MaterialStorageLocation.class, AppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public static int getTotalMaterialCount(String plant, String storeId, String material, boolean isInStock) {
        int totalMaterialCount = 0;
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String entitySetName = ZCollections.MATERIAL_STORAGE_COLLECTION;
        String filterQuery = "/$count?$filter=";
        String resPath = "";
        Object rawData = null;
        try {
            if (plant != null && !plant.isEmpty())
                resPath += "Plant eq '" + plant + "'";
            if (storeId != null && !storeId.isEmpty())
                resPath += (!resPath.isEmpty() ? " and " : "") + "MaterialStorageLocation eq '" + storeId + "'";
            if (material != null && !material.isEmpty())
                resPath += (!resPath.isEmpty() ? " and " : "") + "Material eq '" + material + "'";
            if (isInStock)
                resPath += (!resPath.isEmpty() ? " and " : "") + "Stock gt 0";
            resPath = entitySetName + filterQuery + resPath;
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.MATERIAL_STORAGE_COLLECTION, resPath);
            if (!result.isError()) {
                rawData = result.Content();
                totalMaterialCount = Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(MaterialStorageLocation.class, AppSettings.LogLevel.Error, e.getMessage());
        }
        return totalMaterialCount;
    }

    public static ResponseObject getMaterialStorage(String plant, String material) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String resPath = ZCollections.MATERIAL_STORAGE_COLLECTION;
        try {
            if (plant != null && material != null) {
                resPath += "?$filter=(Plant eq '" + plant + "' and Material eq '" + material + "')";
            }
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.MATERIAL_STORAGE_COLLECTION, resPath);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                ArrayList<MaterialStorageLocation> storageLocations = (ArrayList<MaterialStorageLocation>) result.Content();
                ArrayList<SpinnerItem> items = new ArrayList<>();
                if (storageLocations != null && storageLocations.size() > 0) {
                    SpinnerItem item = null;
                    for (MaterialStorageLocation storage : storageLocations) {
                        item = new SpinnerItem();
                        item.setDescription(storage.getMaterialStorageLocation());
                        item.setId(storage.getMaterialStorageLocation());
                        items.add(item);
                    }
                }
                result.setContent(items);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(MaterialStorageLocation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<MaterialStorageLocation> storageLocations = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    storageLocations.add(new MaterialStorageLocation(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", storageLocations);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(MaterialStorageLocation.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    //utility methods
    public static ArrayList<MaterialStorageLocation> searchMaterialPlants(String searchText, String searchOption, ArrayList<MaterialStorageLocation> materials) {

        ArrayList<MaterialStorageLocation> searchedMaterials = new ArrayList<MaterialStorageLocation>();
        if (searchOption.equals(ZCollections.SEARCH_OPTION_ID)) {
            for (MaterialStorageLocation material : materials) {
                if (material.getMaterial().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchedMaterials.add(material);
                }
            }
        } else if (searchOption.equals(ZCollections.SEARCH_OPTION_DESCRIPTION)) {
            for (MaterialStorageLocation materialPlant : materials) {
                if (materialPlant.getMaterialDescription().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchedMaterials.add(materialPlant);
                }
            }
        } else if (searchOption.equals(ZCollections.SEARCH_ITEM_PLANT)) {
            for (MaterialStorageLocation materialPlant : materials) {
                if (materialPlant.getPlant().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchedMaterials.add(materialPlant);
                }
            }
        } else {
            for (MaterialStorageLocation material : materials) {
                if (material.getMaterial().trim().toLowerCase().contains(searchText.toLowerCase()) || material.getMaterialDescription().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchedMaterials.add(material);
                }
            }
        }
        return searchedMaterials;

    }

    public static MaterialStorageLocation searchMaterialById(String materialId, ArrayList<MaterialStorageLocation> items) {
        for (MaterialStorageLocation material : items) {
            if (material.getMaterial().equalsIgnoreCase(materialId))
                return material;
        }
        return null;
    }

    public String getMaterialDescription() {
        return MaterialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        MaterialDescription = materialDescription;
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

    public String getMaterialStorageLocation() {
        return MaterialStorageLocation;
    }

    public void setMaterialStorageLocation(String materialStorageLocation) {
        MaterialStorageLocation = materialStorageLocation;
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

    public BigDecimal getStock() {
        return Stock;
    }

    public void setStock(BigDecimal stock) {
        Stock = stock;
    }

    public String getStorageBin() {
        return StorageBin;
    }

    public void setStorageBin(String storageBin) {
        StorageBin = storageBin;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }
}
