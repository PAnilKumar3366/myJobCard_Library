package com.ods.myjobcard_library.viewmodels;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

public class ResponseApprovalStatusHelper {

    public ArrayList<ZODataEntity> fetchFormApprovalStatus(String FormId, String InstanceID, String Version) {

        return getFormApprovalStatus(FormId, InstanceID, Version);
    }

    private ArrayList<ZODataEntity> getFormApprovalStatus(String formId, String instanceID, String version) {
        ResponseObject result = null;
        ArrayList<ZODataEntity> ZODataEntities = new ArrayList<>();
        String respath = "";
        String entitySetName = ZCollections.FORM_INSTANCE_STATUS_ENTITY_SET;
        String filterQuery = "$filter=FormID eq '" + formId + "' FormVersion eq '" + version + "' FormInstanceID eq '" + instanceID + "'";
        result = DataHelper.getInstance().getEntities(entitySetName, entitySetName + filterQuery);
        try {
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    ZODataEntities.add(zoDataEntity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return ZODataEntities;
    }
}
