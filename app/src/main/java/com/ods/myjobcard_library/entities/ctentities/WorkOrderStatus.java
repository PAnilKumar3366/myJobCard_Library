package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.appsettings.StatusCategory;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 31-03-2017.
 */
public class WorkOrderStatus extends BaseEntity {

    private String ObjectType;
    private String Status;
    private String StatusAllowed;
    private String CurrentStatusCode;
    private String AllowedStatusCode;
    private String StatusCategory;
    private String RoleID;
    private String StatusCategoryId;

    public static ResponseObject getWorkOrderAllowedStatus(StatusCategory status, String type) {
        ResponseObject result = null;
        String strResPath;
        String entitySet = ZCollections.WORKORDER_STATUS_COLLECTION;
        try {
            if (status != null) {
                strResPath = entitySet + "?$filter=(CurrentStatusCode eq '" + status.getStatusCode() + "'" +
                        " and StatusCategory eq '" + status.getStatuscCategory() + "'" +
                        " and tolower(ObjectType) eq '" + type.toLowerCase() + "')";

                //Query for Temparorily set to the Allowed statuses
                //strResPath = entitySet + "?$filter=(CurrentStatusCode eq '" + status.getStatusCode() + "')";
                result = DataHelper.getInstance().getEntities(entitySet, strResPath);
                if (result != null && !result.isError()) {
                    List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                    if (entities == null || entities.size() == 0) {
                        strResPath = entitySet + "?$filter=(CurrentStatusCode eq '" + status.getStatusCode() + "'" +
                                " and StatusCategory eq '" + status.getStatuscCategory() + "'" +
                                " and tolower(ObjectType) eq 'x')";
                        result = DataHelper.getInstance().getEntities(entitySet, strResPath);
                        if (result != null && !result.isError())
                            entities = (List<ODataEntity>) result.Content();
                    }
                    result = FromEntity(entities);
                } else {
                    DliteLogger.WriteLog(WorkOrderStatus.class, ZAppSettings.LogLevel.Error, result.getMessage());
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrderStatus.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<String> allowedStatusCodes = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    allowedStatusCodes.add(String.valueOf(entity.getProperties().get("AllowedStatusCode").getValue()));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", allowedStatusCodes);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrderStatus.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatusAllowed() {
        return StatusAllowed;
    }

    public void setStatusAllowed(String statusAllowed) {
        StatusAllowed = statusAllowed;
    }

    public String getCurrentStatusCode() {
        return CurrentStatusCode;
    }

    public void setCurrentStatusCode(String currentStatusCode) {
        CurrentStatusCode = currentStatusCode;
    }

    public String getAllowedStatusCode() {
        return AllowedStatusCode;
    }

    public void setAllowedStatusCode(String allowedStatusCode) {
        AllowedStatusCode = allowedStatusCode;
    }

    public String getStatusCategory() {
        return StatusCategory;
    }

    public void setStatusCategory(String statusCategory) {
        StatusCategory = statusCategory;
    }

    public String getRoleID() {
        return RoleID;
    }

    public void setRoleID(String roleID) {
        RoleID = roleID;
    }

    public String getStatusCategoryId() {
        return StatusCategoryId;
    }

    public void setStatusCategoryId(String statusCategoryId) {
        StatusCategoryId = statusCategoryId;
    }
}
