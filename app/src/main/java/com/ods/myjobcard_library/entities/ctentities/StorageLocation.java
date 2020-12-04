package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 16-05-2016.
 */
public class StorageLocation extends ZBaseEntity {

    private String Plant;
    private String StorLocation;
    private String StorageLocationDescription;

    public StorageLocation(ODataEntity entity) {
        create(entity);
    }

    public static ResponseObject getPlantStorageLocations(String plant) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            dataHelper = DataHelper.getInstance();
            String resPath = ZCollections.STORAGE_LOCATION_COLLECTION + "?$filter=Plant eq '" + plant + "'";
            result = dataHelper.getEntities(ZCollections.STORAGE_LOCATION_COLLECTION, resPath);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                ArrayList<StorageLocation> locations = (ArrayList<StorageLocation>) result.Content();
                ArrayList<SpinnerItem> items = new ArrayList<>();
                SpinnerItem item = null;
                for (StorageLocation location : locations) {
                    item = new SpinnerItem();
                    item.setId(location.getStorLocation());
                    item.setDescription(location.getStorageLocationDescription());
                    items.add(item);
                }
                result.setContent(items);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(StorageLocation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<StorageLocation> storageLocations = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    storageLocations.add(new StorageLocation(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", storageLocations);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(StorageLocation.class, ZAppSettings.LogLevel.Error, e.getMessage());
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

    public String getStorLocation() {
        return StorLocation;
    }

    public void setStorLocation(String storLocation) {
        StorLocation = storLocation;
    }

    public String getStorageLocationDescription() {
        return StorageLocationDescription;
    }

    public void setStorageLocationDescription(String storageLocationDescription) {
        StorageLocationDescription = storageLocationDescription;
    }
}
