package com.ods.myjobcard_library.entities.lowvolume;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ctentities.WorkFlow;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.List;

public class LTAttachmentType extends BaseEntity {
    private String ObjectCategory;
    private String ObjectType;
    private String DMS;
    private String BDS;
    private String ClassName;

    public LTAttachmentType(ODataEntity entity) {
        initializingEntityProperties();
        create(entity);
    }

    public static LTAttachmentType getAttachmentTypeEntry(String objCategory, String objType) {
        LTAttachmentType attachmentType = null;
        try {
            String entitySetName = ZCollections.LT_ATTACHMENT_TYPE_SET;
            String resPath = "";
            /*if(objCategory.equalsIgnoreCase("EQUIPMENT")||objCategory.equalsIgnoreCase("FUNCLOC"))
                resPath = entitySetName + "?$filter=ObjectCategory eq '"+ objCategory +"' and tolower(ObjectType) eq 'x'";
            else*/
            resPath = entitySetName + "?$filter=ObjectCategory eq '" + objCategory + "' and ObjectType eq '" + objType + "'";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                if (entities == null || entities.size() == 0) {
                    resPath = entitySetName + "?$filter=ObjectCategory eq '" + objCategory + "' and tolower(ObjectType) eq 'x'";
                    result = DataHelper.getInstance().getEntities(entitySetName, resPath);
                }
                if (result != null && !result.isError())
                    entities = (List<ODataEntity>) result.Content();
                if (entities != null && entities.size() > 0)
                    attachmentType = new LTAttachmentType(entities.get(0));

            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkFlow.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return attachmentType;
    }

    private void initializingEntityProperties() {
        this.setEntityType(ZCollections.LT_ATTACHMENT_TYPE_ENTITY);
        this.setEntitySetName(ZCollections.LT_ATTACHMENT_TYPE_SET);
        this.setEntityResourcePath(ZCollections.LT_ATTACHMENT_TYPE_SET);
        this.addKeyFieldNames("ObjectCategory");
        this.addKeyFieldNames("ObjectType");
    }

    public String getObjectCategory() {
        return ObjectCategory;
    }

    public void setObjectCategory(String objectCategory) {
        ObjectCategory = objectCategory;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public String getDMS() {
        return DMS;
    }

    public void setDMS(String DMS) {
        this.DMS = DMS;
    }

    public String getBDS() {
        return BDS;
    }

    public void setBDS(String BDS) {
        this.BDS = BDS;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }
}
