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

public class EntitySetKeys extends ZBaseEntity {
    private String Id;
    private String EntitySetName;
    private String KeyProperty;
    private String KeyName;
    private String IsRefKey;
    private String IsVisible;
    private String Sequence;

    EntitySetKeys(ODataEntity entity) {
        initializingEntityProperties();
        create(entity);
    }

    public static ArrayList<EntitySetKeys> getEntitySetKey(String entitySetname) {
        ResponseObject result = null;
        ArrayList<EntitySetKeys> entitySetKeys = new ArrayList<>();
        String resPath = ZCollections.ENTITY_SET_KEYS_TYPE_SET;
        try {
            String strQuery = ZCollections.ENTITY_SET_KEYS_TYPE_SET + "?$filter=(EntitySetName eq '" + entitySetname + "' and IsVisible eq 'true')";
            result = DataHelper.getInstance().getEntities(resPath, strQuery);
            result = FromEntity((List<ODataEntity>) result.Content());
            if (result.isError()) {
                result = new ResponseObject(ZConfigManager.Status.Error);
            } else {
                entitySetKeys = (ArrayList<EntitySetKeys>) result.Content();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(EntitySetKeys.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return entitySetKeys;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<EntitySetKeys> entitySetKeysList = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    entitySetKeysList.add(new EntitySetKeys(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", entitySetKeysList);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(FLCharacteristics.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Override
    public String getEntitySetName() {
        return EntitySetName;
    }

    @Override
    public void setEntitySetName(String entitySetName) {
        EntitySetName = entitySetName;
    }

    public String getKeyProperty() {
        return KeyProperty;
    }

    public void setKeyProperty(String keyProperty) {
        KeyProperty = keyProperty;
    }

    public String getKeyName() {
        return KeyName;
    }

    public void setKeyName(String keyName) {
        KeyName = keyName;
    }

    public String getIsRefKey() {
        return IsRefKey;
    }

    public void setIsRefKey(String isRefKey) {
        IsRefKey = isRefKey;
    }

    public String getIsVisible() {
        return IsVisible;
    }

    public void setIsVisible(String isVisible) {
        IsVisible = isVisible;
    }

    public String getSequence() {
        return Sequence;
    }

    public void setSequence(String sequence) {
        Sequence = sequence;
    }

    private void initializingEntityProperties() {
        this.setEntityType(ZCollections.ENTITY_SET_KEYS_TYPE);
        this.setEntitySetName(ZCollections.ENTITY_SET_KEYS_TYPE_SET);
        // this.setEntityResourcePath(Collections.ENTITY_SET_KEYS_TYPE_SET);
        this.addKeyFieldNames("EntitySetName");
        this.addKeyFieldNames("KeyProperty");
    }
}
