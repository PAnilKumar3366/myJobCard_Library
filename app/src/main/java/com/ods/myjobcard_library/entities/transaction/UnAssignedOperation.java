package com.ods.myjobcard_library.entities.transaction;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.PartnerAddress;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.client.odata.v4.EntityValue;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;

public class UnAssignedOperation extends Operation {

    public UnAssignedOperation(EntityValue oprEntity) {
        create(oprEntity);
        initializeEntityProperties();
        isOnline = true;
        deriveOperationStatus();
    }

    public UnAssignedOperation(ZODataEntity zoDataEntity) {
        partnerAddresses = new ArrayList<PartnerAddress>();
        validStatuses = new ArrayList<>();
        initializeEntityProperties();
        create(zoDataEntity);
    }

    public UnAssignedOperation(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel, boolean fetchAddress) {
        try {
            partnerAddresses = new ArrayList<PartnerAddress>();
            validStatuses = new ArrayList<>();
            initializeEntityProperties();
            create(entity, fetchLevel);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());

        }

    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.UNASSIGNED_OPR_ENTITY_SET);
        this.setEntityType(ZCollections.UNASSIGNED_OPR_ENTITY_TYPE);
        this.addKeyFieldNames(ZConfigManager.WO_KEY_FIELD);
        this.addKeyFieldNames(ZConfigManager.OPR_KEY_FIELD1);
        this.addKeyFieldNames(ZConfigManager.OPR_KEY_FIELD2);
        //this.setParentEntitySetName(ZCollections.WO_COLLECTION);
       /* this.setParentForeignKeyFieldName(ZConfigManager.WO_KEY_FIELD);
        this.setParentKeyFieldName(ZConfigManager.WO_KEY_FIELD);*/
    }
}
