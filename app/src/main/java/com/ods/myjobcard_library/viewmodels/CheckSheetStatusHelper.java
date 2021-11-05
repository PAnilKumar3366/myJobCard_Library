package com.ods.myjobcard_library.viewmodels;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.appsettings.StatusCategory;
import com.ods.myjobcard_library.entities.ctentities.WorkOrderStatus;
import com.ods.myjobcard_library.entities.forms.FormResponseApprovalStatus;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.ConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

public class CheckSheetStatusHelper {

    private StatusCategory statusDetailsObj;
    private ArrayList<StatusCategory> validStatuses;
    private String objType, currentStatus;

    public ZODataEntity getCheckSheetInstanceStatus(String formID, String version, String instanceID, String submittedBy, String counter, String approverID) {
        return fetchCheckSheetInstanceStatus(formID, version, instanceID, submittedBy, counter, approverID);
    }

    public ResponseObject saveInstanceStatus(FormResponseApprovalStatus status) {
        return onSaveInstanceStatus(status);
    }

    private ResponseObject onSaveInstanceStatus(FormResponseApprovalStatus status) {
        ResponseObject result = status.SaveToStore(true);
        return result;
    }

    private ZODataEntity fetchCheckSheetInstanceStatus(String formID, String version, String instanceID, String submittedBy, String counter, String approverID) {
        ZODataEntity zoDataEntity = null;
        try {
            ResponseObject result = new ResponseObject(ConfigManager.Status.Error);
            String resPath = "";
            String entitySet = ZCollections.FORM_RESPONSE_APPROVAL_STATUS_ENTITY_SET;
            String strOrderByURI = "&$orderby=CreatedDate";
            approverID = approverID != null ? approverID : "";
            if (submittedBy != null && !submittedBy.isEmpty())
                resPath = entitySet + "?$filter=FormID eq '" + formID + "' and Version eq '" + version + "' and FormInstanceID eq '" + instanceID + "'and tolower(FormSubmittedBy) eq'" + submittedBy.toLowerCase() + "' and  Counter eq '" + counter + "' and tolower(ApproverID) eq '"+ approverID.toLowerCase() +"'";
            else
                resPath = entitySet + "?$filter=FormID eq '" + formID + "' and Version eq '" + version + "' and FormInstanceID eq '" + instanceID + "' and  Counter eq '" + counter + "' and ApproverID eq '"+ approverID.toLowerCase() +"'";
            result = DataHelper.getInstance().getEntities(entitySet, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    zoDataEntity = new ZODataEntity(entity);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return zoDataEntity;
    }

    private ArrayList<ZODataEntity> getFormApprovalStatus(String formId, String instanceID, String version, String submittedBy, String counter) {
        ResponseObject result = null;
        ArrayList<ZODataEntity> ZODataEntities = new ArrayList<>();
        String entitySetName = ZCollections.FORM_RESPONSE_APPROVAL_STATUS_ENTITY_SET;
        String filterQuery = "";
        filterQuery = "$filter=FormID eq '" + formId + "' Version eq '" + version + "' FormInstanceID eq '" + instanceID + "'";

        if (submittedBy != null && !submittedBy.isEmpty())
            filterQuery += " and FormSubmittedBy eq '" + submittedBy + "'";
        if (counter != null && !counter.isEmpty())
            filterQuery += " and Counter eq '" + counter + "'";

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

    /**
     * Deriving the Status fields from StatusCategory and
     * fetching the status information by filtering its current status and FormInstance object type
     *
     * @param currentStatus
     * @param objType
     * @return
     */
    protected ArrayList<StatusCategory> deriveChecksheetStatus(String currentStatus, String objType) {
        ArrayList<StatusCategory> statusDetail = new ArrayList<>();
        try {

            statusDetail = StatusCategory.getCheckSheetStatuDetails(currentStatus, objType);

        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return statusDetail;
    }

    /**
     * getting the allowed statuses by passing its current status
     *
     * @return
     */
    protected ArrayList<StatusCategory> getAllowedStatus() {
        ResponseObject result = null;
        String strResPath;
        ArrayList<String> allowedStatusList;
        int intNumOfActiveWO = 0;
        validStatuses = new ArrayList<>();
        try {
            if (statusDetailsObj != null) {
                result = WorkOrderStatus.getWorkOrderAllowedStatus(statusDetailsObj, statusDetailsObj.getObjectType());
                if (result != null && !result.isError()) {
                    allowedStatusList = (ArrayList<String>) result.Content();
                    for (String allowedStatus : allowedStatusList) {
                        StatusCategory status = StatusCategory.getStatusDetails(allowedStatus, objType, ZConfigManager.Fetch_Object_Type.NotificationTasks);
                        if (status != null)
                            addValidStatus(status);
                    }
                } else {
                    DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, result.getMessage());
                }
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return validStatuses;
    }

    /**
     * set the all valid statuses to attaylist of the StatusCategory
     *
     * @param status
     */
    private void addValidStatus(StatusCategory status) {
        try {
            if (validStatuses == null)
                validStatuses = new ArrayList<>();
            validStatuses.add(status);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }
}
