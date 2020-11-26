package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.List;

public class WorkFlow extends BaseEntity {

    private String Id;
    private String Event;
    private String ObjectCategory;
    private String ObjectType;
    private String ActionType;
    private String ActionKey;
    private Short Sequance;
    private String Active;
    private String Remarks;

    public WorkFlow(ODataEntity entity) {
        super.create(entity);
    }

    public WorkFlow() {
    }

    //helper methods
    public static WorkFlow getWorkFlowEntry(String event, String objCategory) {
        WorkFlow workFlow = null;
        try {
            String entitySetName = ZCollections.WORKFLOW_COLLECTION;
            String resPath = entitySetName + "?$filter=Event eq '" + event + "' and ObjectCategory eq '" + objCategory + "'";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                if (entities != null && entities.size() > 0)
                    workFlow = new WorkFlow(entities.get(0));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkFlow.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return workFlow;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
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

    public String getActionType() {
        return ActionType;
    }

    public void setActionType(String actionType) {
        ActionType = actionType;
    }

    public String getActionKey() {
        return ActionKey;
    }

    public void setActionKey(String actionKey) {
        ActionKey = actionKey;
    }

    public Short getSequance() {
        return Sequance;
    }

    public void setSequance(Short sequance) {
        Sequance = sequance;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

}
