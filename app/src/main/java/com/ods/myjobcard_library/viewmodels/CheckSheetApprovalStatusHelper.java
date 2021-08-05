package com.ods.myjobcard_library.viewmodels;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.forms.FormResponseApprovalStatus;
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

    /**
     * This Method is calling from Pojo  and get the Reviewed Status for particular FormInstance and Counter Value.
     *
     * @param formId
     * @param instanceID
     * @param version
     * @param submittedBy
     * @param counter
     * @param approverID
     * @return
     */
    public ArrayList<FormResponseApprovalStatus> getInstanceReviewStatus(String formId, String instanceID, String version, String submittedBy, String counter, String approverID) {
        ResponseObject result = null;
        ArrayList<ZODataEntity> ZODataEntities = new ArrayList<>();
        ArrayList<FormResponseApprovalStatus> responseApprovalStatusesList = new ArrayList<>();
        String entitySetName = ZCollections.FORM_RESPONSE_APPROVAL_STATUS_ENTITY_SET;
        String filterQuery = "";
        String orderBy = "&$orderby=CreatedDate,CreatedTime";
        filterQuery = entitySetName + "?$filter=FormID eq '" + formId + "' and Version eq '" + version + "' and FormInstanceID eq '" + instanceID + "'";

        if (submittedBy != null && !submittedBy.isEmpty())
            filterQuery += " and FormSubmittedBy eq '" + submittedBy + "'";
        //filterQuery += " and tolower(FormSubmittedBy) eq '" + submittedBy.toLowerCase() + "'";
        if (counter != null && !counter.isEmpty())
            filterQuery += " and Counter eq '" + counter + "'";
        if (approverID != null && !approverID.isEmpty())
            // filterQuery += " and tolower(ApproverID) eq '" + approverID.toLowerCase() + "'";
            filterQuery += " and ApproverID eq '" + approverID + "'";


        result = DataHelper.getInstance().getEntities(entitySetName, filterQuery + orderBy);
        try {
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    ZODataEntities.add(zoDataEntity);
                }
                responseApprovalStatusesList = onFetchApproverEntities(ZODataEntities);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return responseApprovalStatusesList;
    }

    private ArrayList<ZODataEntity> getFormApprovalStatus(String formId, String instanceID, String version, String submittedBy, String counter, String approverID) {
        ResponseObject result = null;
        ArrayList<ZODataEntity> ZODataEntities = new ArrayList<>();
        String entitySetName = ZCollections.FORM_RESPONSE_APPROVAL_STATUS_ENTITY_SET;
        String filterQuery = "";
        String orderBy = "&$orderby=CreatedDate,CreatedTime";
        filterQuery = entitySetName + "?$filter=FormID eq '" + formId + "' and Version eq '" + version + "' and FormInstanceID eq '" + instanceID + "'";


        if (submittedBy != null && !submittedBy.isEmpty())
            filterQuery += " and FormSubmittedBy eq '" + submittedBy + "'";
        //filterQuery += " and tolower(FormSubmittedBy) eq '" + submittedBy.toLowerCase() + "'";
        if (counter != null && !counter.isEmpty())
            filterQuery += " and Counter eq '" + counter + "'";
        if (approverID != null && !approverID.isEmpty())
            // filterQuery += " and tolower(ApproverID) eq '" + approverID.toLowerCase() + "'";
            filterQuery += " and ApproverID eq '" + approverID + "'";


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

    /**
     * This method helps to converts the ZODataEntities into FormResponseApprovalStatus Objects.
     *
     * @param zODataEntities ZODataEntities
     * @return FormResponseApprovalStatus List
     */
    protected ArrayList<FormResponseApprovalStatus> onFetchApproverEntities(ArrayList<ZODataEntity> zODataEntities) {
        ArrayList<FormResponseApprovalStatus> approverMasterList = new ArrayList<>();
        try {
            for (ZODataEntity entity : zODataEntities) {
                FormResponseApprovalStatus approverMasterData = new FormResponseApprovalStatus(entity);
                approverMasterList.add(approverMasterData);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return approverMasterList;
    }
}
