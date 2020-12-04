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
 * Created by MY HOME on 12/12/2017.
 */
public class AssetMapping extends ZBaseEntity {

    private String GISID;
    private String ObjectID;
    private String ObjectType;
    private String LayerRef;
    private String EnteredBy;

    public AssetMapping() {
    }

    public AssetMapping(ODataEntity entity) {
        create(entity);
    }

    public static ArrayList<AssetMapping> getAssetMappings(String objectType) {
        ArrayList<AssetMapping> mappings = new ArrayList<>();
        try {
            String entityName = ZCollections.ASSET_MAPPING_COLLECTION;
            String resPath = entityName;
            if (objectType != null && !objectType.isEmpty()) {
                resPath += "?$filter=ObjectType eq '" + objectType + "'";
            }
            ResponseObject result = DataHelper.getInstance().getEntities(entityName, resPath);
            if (result != null && !result.isError()) {
                mappings = fromEntity((List<ODataEntity>) result.Content());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(AssetMapping.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return mappings;
    }

    public static String getObjectId(long gisID, String layerRef, ArrayList<AssetMapping> mappings) {
        String objID = "";
        try {
            for (AssetMapping mapping : mappings) {
                if (mapping.getGISIDInt() == gisID && mapping.getLayerRef().equalsIgnoreCase(layerRef)) {

                    return mapping.getObjectID();
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(AssetMapping.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return objID;
    }

    private static ArrayList<AssetMapping> fromEntity(List<ODataEntity> entityList) {
        ArrayList<AssetMapping> mappings = new ArrayList<>();
        try {
            for (ODataEntity entity : entityList) {
                AssetMapping mapping = new AssetMapping(entity);
                mappings.add(mapping);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(AssetMapping.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return mappings;
    }

    public String getGISID() {
        return GISID;
    }

    public void setGISID(String GISID) {
        this.GISID = GISID;
    }

    public int getGISIDInt() {
        int gisId = -1;
        try {
            gisId = Integer.parseInt(GISID);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return gisId;
    }

    public String getObjectID() {
        return ObjectID;
    }

    public void setObjectID(String objectID) {
        ObjectID = objectID;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public String getLayerRef() {
        return LayerRef;
    }

    public void setLayerRef(String layerRef) {
        LayerRef = layerRef;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }
}
