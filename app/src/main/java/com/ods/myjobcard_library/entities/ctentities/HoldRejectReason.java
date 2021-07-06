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

/**
 * Created by lenovo on 05-05-2016.
 */
public class HoldRejectReason extends ZBaseEntity {

    private String Code;
    private String Type;
    private String Reason;
    private String Active;
    private String Language;
    private String ObjectType;
    private String ObjectCategory;
    private String RoleID;

    public HoldRejectReason(ODataEntity entity) {
        create(entity);
    }

    //get methods
    public static ResponseObject getReasons(ZAppSettings.ReasonCodeTypes ReasonType,ZConfigManager.Fetch_Object_Type objectCategory) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String strResPath = null;
        String strOrderBy = "&$orderby=Code";
        ArrayList<HoldRejectReason> reasons;
        ArrayList<SpinnerItem> items;
        try {
            String objectStr = objectCategory.equals(ZConfigManager.Fetch_Object_Type.WorkOrder) ? ZAppSettings.StatusCategoryType.WorkOrderLevel.getStatusCategoryType()
                    : objectCategory.equals(ZConfigManager.Fetch_Object_Type.Operation) ? ZAppSettings.StatusCategoryType.OperationLevel.getStatusCategoryType() :objectCategory.equals(ZConfigManager.Fetch_Object_Type.NotificationTasks)?ZAppSettings.StatusCategoryType.NotificationTaskLevel.getStatusCategoryType(): ZAppSettings.StatusCategoryType.NoticationLevel.getStatusCategoryType();

          //  strResPath = ZCollections.REASON_CODE_COLLECTION + "?$filter=(Type eq '" + ReasonType.getResonCodeTypeDesc() + "' and ObjectType eq '" + objectType + "' and tolower(ObjectCategory) eq '" + objectStr.toLowerCase() + "')" + strOrderBy;
            strResPath = ZCollections.REASON_CODE_COLLECTION + "?$filter=(Type eq '" + ReasonType.getResonCodeTypeDesc() + "' and tolower(ObjectCategory) eq '" + objectStr.toLowerCase() + "')" + strOrderBy;
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.REASON_CODE_COLLECTION, strResPath);
            if (result != null && !result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
               /* if (result != null && ((List<ODataEntity>) result.Content()).size() == 0) {
                    strResPath = ZCollections.REASON_CODE_COLLECTION + "?$filter=(Type eq '" + ReasonType.getResonCodeTypeDesc() + "' and tolower(ObjectType) eq 'x' and tolower(ObjectCategory) eq '" + objectStr.toLowerCase() + "')";
                    result = DataHelper.getInstance().getEntities(ZCollections.REASON_CODE_COLLECTION, strResPath);
                    if (result != null && !result.isError()) {
                        result = FromEntity((List<ODataEntity>) result.Content());
                    }
                }*/
                reasons = (ArrayList<HoldRejectReason>) result.Content();
                if (reasons != null && reasons.size() > 0) {
                    items = new ArrayList<>();
                    SpinnerItem item;
                    for (HoldRejectReason reason : reasons) {
                        item = new SpinnerItem();
                        item.setId(reason.getCode());
                        item.setDescription(reason.getReason());
                        items.add(item);
                    }
                    result.setContent(items);
                } else {
                    result.setError(true);
                    result.setMessage("No Records found");
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(HoldRejectReason.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<HoldRejectReason> holdRejectReasons = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    holdRejectReasons.add(new HoldRejectReason(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", holdRejectReasons);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(HoldRejectReason.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public boolean isActive() {
        if (Active != null && Active.length() > 0)
            return true;
        return false;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public String getObjectCategory() {
        return ObjectCategory;
    }

    public void setObjectCategory(String objectCategory) {
        ObjectCategory = objectCategory;
    }

    public String getRoleID() {
        return RoleID;
    }

    public void setRoleID(String roleID) {
        RoleID = roleID;
    }
}