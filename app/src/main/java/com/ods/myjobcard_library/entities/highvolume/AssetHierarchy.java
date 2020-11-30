package com.ods.myjobcard_library.entities.highvolume;


import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ctentities.AssetMapping;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Asset Hierarchy Model class which contains data members and data helper member functions.
 */
public class AssetHierarchy extends BaseEntity {

    private String ObjectId;
    private String TableId;
    private String Type;
    private String Description;
    private String ParentId;
    private int HierLevel;
    private String PlanningPlant;
    private String WorkCenter;

    public AssetHierarchy() {
        initializingEntityProperties();
    }

    public AssetHierarchy(ODataEntity entity) {
        initializingEntityProperties();
        create(entity);
    }

    /**
     * Fetching the list of Asset Hierarchy.
     *
     */
    public static ArrayList<AssetHierarchy> getAllAssetHierarchies() {
        ArrayList<AssetHierarchy> hierarchies = new ArrayList<>();
        try {
            String entitySetName = ZCollections.ASSET_HIERARCHY_ENTITY_SET;
            String resPath = entitySetName + "";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                hierarchies = fromEntity((List<ODataEntity>) result.Content());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(AssetMapping.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return hierarchies;
    }

    /**
     * Fetching the Asset Hierarchy Object.
     *
     * @return AssetHierarchy
     */
    public static AssetHierarchy getAssetHierarchy(String objectId) {
        AssetHierarchy hierarchy = null;
        try {
            String entitySetName = ZCollections.ASSET_HIERARCHY_ENTITY_SET;
            String resPath = entitySetName + "('" + objectId + "')";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                hierarchy = new AssetHierarchy((ODataEntity) result.Content());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(AssetMapping.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return hierarchy;
    }

    private static ArrayList<AssetHierarchy> fromEntity(List<ODataEntity> entityList) {
        ArrayList<AssetHierarchy> hierarchies = new ArrayList<>();
        try {
            for (ODataEntity entity : entityList) {
                AssetHierarchy hierarchy = new AssetHierarchy(entity);
                hierarchies.add(hierarchy);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(AssetMapping.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return hierarchies;
    }

    private void initializingEntityProperties() {
        this.setEntityType(ZCollections.ASSET_HIERARCHY_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.ASSET_HIERARCHY_ENTITY_SET);
        this.setEntityResourcePath(ZCollections.ASSET_HIERARCHY_ENTITY_SET);
        this.addKeyFieldNames("ObjectId");
    }

    public String getObjectId() {
        return ObjectId;
    }

    public void setObjectId(String objectId) {
        ObjectId = objectId;
    }

    public String getTableId() {
        return TableId;
    }

    public void setTableId(String tableId) {
        TableId = tableId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        ParentId = parentId;
    }

    public int getHierLevel() {
        return HierLevel;
    }

    public void setHierLevel(int hierLevel) {
        HierLevel = hierLevel;
    }

    public String getPlanningPlant() {
        return PlanningPlant;
    }

    public void setPlanningPlant(String planningPlant) {
        PlanningPlant = planningPlant;
    }

    public String getWorkCenter() {
        return WorkCenter;
    }

    public void setWorkCenter(String workCenter) {
        WorkCenter = workCenter;
    }
}
