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

public class CheckSheetApprovalStatusHelper {

    public ArrayList<ZODataEntity> fetchFormApprovalStatus(String FormId, String InstanceID, String Version, String submittedBy, String counter, String approverID) {

        return getFormApprovalStatus(FormId, InstanceID, Version, submittedBy, counter, approverID);
    }

    private ArrayList<ZODataEntity> getFormApprovalStatus(String formId, String instanceID, String version, String submittedBy, String counter, String approverID) {
        ResponseObject result = null;
        ArrayList<ZODataEntity> ZODataEntities = new ArrayList<>();
        String entitySetName = ZCollections.FORM_RESPONSE_APPROVAL_STATUS_ENTITY_SET;
        String filterQuery = "";
        String orderBy = "&$orderby=CreatedDate,CreatedTime";
        filterQuery = entitySetName + "?$filter=FormID eq '" + formId + "' and Version eq '" + version + "' and FormInstanceID eq '" + instanceID + "'";

        if (submittedBy != null && !submittedBy.isEmpty())
            filterQuery += " and tolower(FormSubmittedBy) eq '" + submittedBy.toLowerCase() + "'";
        if (counter != null && !counter.isEmpty())
            filterQuery += " and Counter eq '" + counter + "'";
        if (approverID != null && !approverID.isEmpty())
            filterQuery += " and tolower(ApproverID) eq '" + approverID.toLowerCase() + "'";


        result = DataHelper.getInstance().getEntities(entitySetName, filterQuery + orderBy);
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
