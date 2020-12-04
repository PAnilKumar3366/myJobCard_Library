package com.ods.myjobcard_library.entities.lowvolume;

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

public class LTAllowedFollowOnObjectType extends ZBaseEntity {
    private String ObjectType;
    private String AllowedObjectType;
    private String RoleId;

    public LTAllowedFollowOnObjectType(ODataEntity entity) {
        initializingEntityProperties();
        create(entity);
    }

    public static ArrayList<LTAllowedFollowOnObjectType> getFollowupObjectTypes(String orderType) {
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Success, "", null);
        ArrayList<LTAllowedFollowOnObjectType> mAllowedFollowOnObjectTypes = new ArrayList<>();
        String entitySetName = ZCollections.LT_AllowedFollowOnObjectTypeSet;
        String resPath = entitySetName + "?$filter=(ObjectType eq '" + orderType + "')";
        try {
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                mAllowedFollowOnObjectTypes = (ArrayList<LTAllowedFollowOnObjectType>) result.Content();
                result = new ResponseObject(ZConfigManager.Status.Success, "Success", mAllowedFollowOnObjectTypes);

            }

        } catch (Exception e) {
            DliteLogger.WriteLog(LTAllowedFollowOnObjectType.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return mAllowedFollowOnObjectTypes;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<LTAllowedFollowOnObjectType> mAllowedFollowOnObjectTypes = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    mAllowedFollowOnObjectTypes.add(new LTAllowedFollowOnObjectType(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", mAllowedFollowOnObjectTypes);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(LTAllowedFollowOnObjectType.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getObjecttype() {
        return ObjectType;
    }

    public void setObjecttype(String objecttype) {
        ObjectType = objecttype;
    }

    public String getAllowedObjectType() {
        return AllowedObjectType;
    }

    public void setAllowedObjectType(String allowedObjectType) {
        AllowedObjectType = allowedObjectType;
    }

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }

    private void initializingEntityProperties() {
        this.setEntityType(ZCollections.LT_AllowedFollowOnObjectType);
        this.setEntitySetName(ZCollections.LT_AllowedFollowOnObjectTypeSet);
        this.setEntityResourcePath(ZCollections.LT_AllowedFollowOnObjectTypeSet);
        this.addKeyFieldNames("ObjectType");
        this.addKeyFieldNames("AllowedObjectType");
        this.addKeyFieldNames("RoleId");
    }


}
