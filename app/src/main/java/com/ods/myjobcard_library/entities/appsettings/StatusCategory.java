package com.ods.myjobcard_library.entities.appsettings;

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

public class StatusCategory extends ZBaseEntity {
    public ZAppSettings.NotificationUserStatus notificationStatus;
    public ZAppSettings.MobileStatus woOprStatus = ZAppSettings.MobileStatus.NotSet;
    private String RecordNo;
    private String ObjectType;
    private Short Sequence;
    private String StatusCode;
    private String StatusType;
    private String StatusVisible;
    private String StatusDescKey;
    private String VoiceCommandResKey;
    private String CnfPopup;
    private String CnfTextResKey;
    private String ImageResKey;
    private boolean InProcess;
    private String StatuscCategory;
    private String Active;
    private String CaptureTime;
    private String PostTime;
    private String Ref_Cal_Status;
    private String ActivityType;
    private String ActivityTypeDesc;
    private String DispTimeSheetString;
    private int PreCheck;
    private String PostConfirmations;
    private boolean AllowWoCreate;
    //newly added fields
    private String RoleId;
    private String Msgkey;
    private String EnteredBy;

    public StatusCategory(ODataEntity entity) {
        initializingEntityProperties();
        create(entity);
    }

    public StatusCategory() {
        initializingEntityProperties();
    }

    public static StatusCategory getStatusDetails(String statusCode, String objectType, ZConfigManager.Fetch_Object_Type object) {
        StatusCategory statusCategory = null;
        try {
            String objectStr = object.equals(ZConfigManager.Fetch_Object_Type.WorkOrder) ? "WORKORDERLEVEL"
                    : object.equals(ZConfigManager.Fetch_Object_Type.Operation) ? "OPERATIONLEVEL" : "NOTIFICATIONLEVEL";
            String entitySetName = ZCollections.STATUS_CATEGORY_SET_COLLECTION;
            String resPath = entitySetName + "?$filter=StatusCode eq '" + statusCode + "' and ObjectType eq '" + objectType + "' and tolower(StatuscCategory) eq '" + objectStr.toLowerCase() + "'";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                if (entities != null && entities.size() == 0) {
                    resPath = entitySetName + "?$filter=tolower(StatusCode) eq '" + statusCode.toLowerCase() + "' and tolower(ObjectType) eq 'x' and tolower(StatuscCategory) eq '" + objectStr.toLowerCase() + "'";
                    result = DataHelper.getInstance().getEntities(entitySetName, resPath);
                    if (result != null && !result.isError()) {
                        entities = (List<ODataEntity>) result.Content();
                    }
                }
                if (entities != null && entities.size() > 0) {
                    statusCategory = new StatusCategory(entities.get(0));
                    if (object == ZConfigManager.Fetch_Object_Type.Notification)
                        statusCategory.notificationStatus = ZAppSettings.NotificationUserStatus.valueOf(statusCategory.getStatusDescKey());
                    else
                        statusCategory.woOprStatus = ZAppSettings.MobileStatus.valueOf(statusCategory.getStatusDescKey());
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(StatusCategory.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return statusCategory;
    }

    public static ArrayList<StatusCategory> getStatuses(String objectType, ZConfigManager.Fetch_Object_Type object) {
        ArrayList<StatusCategory> statusCategories = new ArrayList<>();
        try {
            String objectStr = object.equals(ZConfigManager.Fetch_Object_Type.WorkOrder) ? "WORKORDERLEVEL"
                    : object.equals(ZConfigManager.Fetch_Object_Type.Operation) ? "OPERATIONLEVEL" : "NOTIFICATIONLEVEL";
            String entitySetName = ZCollections.STATUS_CATEGORY_SET_COLLECTION;
            String resPath = entitySetName + "?$filter=tolower(ObjectType) eq '" + objectType.toLowerCase() + "' and tolower(StatuscCategory) eq '" + objectStr.toLowerCase() + "'";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                if (entities != null && entities.size() == 0) {
                    resPath = entitySetName + "?$filter=tolower(ObjectType) eq 'x' and tolower(StatuscCategory) eq '" + objectStr.toLowerCase() + "'";
                    result = DataHelper.getInstance().getEntities(entitySetName, resPath);
                    if (result != null && !result.isError()) {
                        entities = (List<ODataEntity>) result.Content();
                    }
                }
                if (entities != null && entities.size() > 0)
                    for (ODataEntity entity : entities) {
                        StatusCategory statusCategory = new StatusCategory(entity);
                        statusCategories.add(statusCategory);
                    }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(StatusCategory.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return statusCategories;
    }

    public String getMsgkey() {
        return Msgkey;
    }

    public void setMsgkey(String msgkey) {
        Msgkey = msgkey;
    }

    private void initializingEntityProperties() {
        this.setEntityType(ZCollections.STATUS_CATEGORY_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.STATUS_CATEGORY_SET_COLLECTION);
        this.addKeyFieldNames("RecordNo");
        this.addKeyFieldNames("ObjectType");
    }

    public String getRecordNo() {
        return RecordNo;
    }

    public void setRecordNo(String recordNo) {
        RecordNo = recordNo;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public Short getSequence() {
        return Sequence;
    }

    public void setSequence(Short sequence) {
        Sequence = sequence;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public String getStatusType() {
        return StatusType;
    }

    public void setStatusType(String statusType) {
        StatusType = statusType;
    }

    public String getStatusVisible() {
        return StatusVisible;
    }

    public String getStatusDescKey() {
        return StatusDescKey;
    }

    public void setStatusDescKey(String statusDescKey) {
        StatusDescKey = statusDescKey;
    }

    public String getVoiceCommandResKey() {
        return VoiceCommandResKey;
    }

    public void setVoiceCommandResKey(String voiceCommandResKey) {
        VoiceCommandResKey = voiceCommandResKey;
    }

    public String getCnfPopup() {
        return CnfPopup;
    }

    public void setCnfPopup(String cnfPopup) {
        CnfPopup = cnfPopup;
    }

    public String getCnfTextResKey() {
        return CnfTextResKey;
    }

    public void setCnfTextResKey(String cnfTextResKey) {
        CnfTextResKey = cnfTextResKey;
    }

    public String getImageResKey() {
        return ImageResKey;
    }

    public void setImageResKey(String imageResKey) {
        ImageResKey = imageResKey;
    }

    public boolean getInProcess() {
        return InProcess;
    }

    public String getStatuscCategory() {
        return StatuscCategory;
    }

    public void setStatuscCategory(String statuscCategory) {
        StatuscCategory = statuscCategory;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getCaptureTime() {
        return CaptureTime;
    }

    public void setCaptureTime(String captureTime) {
        CaptureTime = captureTime;
    }

    public String getPostTime() {
        return PostTime;
    }

    public void setPostTime(String postTime) {
        PostTime = postTime;
    }

    public String getRef_Cal_Status() {
        return Ref_Cal_Status;
    }

    public void setRef_Cal_Status(String ref_Cal_Status) {
        Ref_Cal_Status = ref_Cal_Status;
    }

    public String getActivityType() {
        return ActivityType;
    }

    public void setActivityType(String activityType) {
        ActivityType = activityType;
    }

    public String getActivityTypeDesc() {
        return ActivityTypeDesc;
    }

    public void setActivityTypeDesc(String activityTypeDesc) {
        ActivityTypeDesc = activityTypeDesc;
    }

    public String getDispTimeSheetString() {
        return DispTimeSheetString;
    }

    public void setDispTimeSheetString(String dispTimeSheetString) {
        DispTimeSheetString = dispTimeSheetString;
    }

    public int getPreCheck() {
        return PreCheck;
    }

    public void setPreCheck(int preCheck) {
        PreCheck = preCheck;
    }

    public String getPostConfirmations() {
        return PostConfirmations;
    }

    public void setPostConfirmations(String postConfirmations) {
        PostConfirmations = postConfirmations;
    }

    //helper methods

    public boolean postConfirmationEnabled() {
        return PostConfirmations != null && PostConfirmations.equalsIgnoreCase("x");
    }

    public boolean isStatusVisible() {
        return StatusVisible != null && StatusVisible.equalsIgnoreCase("x");
    }

    public void setStatusVisible(String statusVisible) {
        StatusVisible = statusVisible;
    }

    public boolean showStatusChangeCnfPopup() {
        return CnfPopup != null && CnfPopup.equalsIgnoreCase("x");
    }

    public boolean captureTimeEnabled() {
        return CaptureTime != null && CaptureTime.equalsIgnoreCase("x");
    }

    public boolean postTimeEnabled() {
        return PostTime != null && PostTime.equalsIgnoreCase("x");
    }

    public boolean showTimeSheetScreen() {
        return DispTimeSheetString != null && DispTimeSheetString.equalsIgnoreCase("x");
    }

    public boolean preCompletionCheckEnabled() {
        return PreCheck > 0;
    }

    public boolean isInProcess() {
        return InProcess;
    }

    public void setInProcess(boolean inProcess) {
        InProcess = inProcess;
    }

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public boolean getAllowWoCreate() {
        return AllowWoCreate;
    }

    public void setAllowWoCreate(boolean allowWoCreate) {
        AllowWoCreate = allowWoCreate;
    }
    /*public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }*/
}
