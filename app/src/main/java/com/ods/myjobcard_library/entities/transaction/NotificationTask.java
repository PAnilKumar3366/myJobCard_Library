package com.ods.myjobcard_library.entities.transaction;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by lenovo on 27-12-2015.
 */
public class NotificationTask extends ZBaseEntity {

    //header details
    private String Notification;
    private String Task;
    private String CatalogType;
    private String CodeGroup;
    private String TaskCode;
    private String TaskText;
    private String CreatedBy;
    private GregorianCalendar CreatedOn;
    private String ChangedBy;
    private GregorianCalendar ChangedOn;
    private GregorianCalendar PlannedStart;
    private GregorianCalendar PlannedFinish;
    private String ObjectNumber;
    private String Item;
    private String Cause;
    private String TaskProcessor;
    private String Responsible;
    private BigDecimal Quantity;
    private String UnitofMeasure;
    private String Delete;
    private String WorkOrderNum;
    private String OperAct;
    private String SystemStatus;
    private String UserStatus;
    private String MobileStatus;
    private String SortNumber;
    private String TempID;
    private Time CarriedOutTime;
    private GregorianCalendar CarriedOutDate;
    private String CarriedOutBy;
    private String OnlineSearch;
    private String EnteredBy;
    private String StatusFlag;


    /**
     * newly added fields for task status management feature
     */
    private Time PlannedStartTime;
    private Time PlannedFinishTime;
    private String CompletedBy;
    private GregorianCalendar CompletedOn;
    private Time CompletionTime;
    private String UserStatusCode;
    private String MobileObjectType;


    //constructor
    public NotificationTask(String notification, String task, boolean isWONotif) {
        super();
        this.Notification = notification;
        this.Task = task;
        initializeEntityProperties(isWONotif);
    }

    public NotificationTask(ODataEntity entity, boolean isWONotif) {
        initializeEntityProperties(isWONotif);
        create(entity);
    }

    //get methods
    public static ResponseObject getTasks(ZAppSettings.FetchLevel fetchLevel, String notifNum, String itemNum, String taskNum, boolean isWONotif) {

        ResponseObject result = null;
        String resPath = "";
        String strEntitySet = isWONotif ? ZCollections.WO_NOTIFICATION_TASKS_COLLECTION : ZCollections.NOTIFICATION_TASKS_COLLECTION;
        String strOrderByURI = "&$orderby=Task";
        boolean isFirstParam = true;
        try {
            resPath = strEntitySet;
            if (notifNum != null && itemNum != null && (notifNum.length() > 0 || itemNum.length() > 0)) {

                if (notifNum.startsWith(ZCollections.TEMP_ID_PREFIX)) {
                    resPath = strEntitySet + "?$filter=(TempID%20eq%20%27" + notifNum + "%27%20and%20Item%20eq%20%27" + itemNum + "%27)" + strOrderByURI;
                } else {
                    resPath = strEntitySet + "?$filter=(Notification%20eq%20%27" + notifNum + "%27%20and%20Item%20eq%20%27" + itemNum + "%27)" + strOrderByURI;
                }
                isFirstParam = false;
            }
            switch (fetchLevel) {
                case ListSpinner:
                    resPath += isFirstParam ? "?" : "&" + "$select=Task,TaskText,CodeGroup";
                    break;
                case Single:
                    if (taskNum != null && notifNum != null) {
                        resPath = strEntitySet + "?$filter=(Notification%20eq%20%27" + notifNum + "%27%20and%20Task%20eq%20%27" + taskNum + "%27)";
                    }
                default:
                    break;
            }
            result = DataHelper.getInstance().getEntities(strEntitySet, resPath);
            result = FromEntity((List<ODataEntity>) result.Content(), isWONotif);

        } catch (Exception e) {
            DliteLogger.WriteLog(NotificationActivity.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, boolean isWONotif) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<NotificationTask> tasks = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    tasks.add(new NotificationTask(entity, isWONotif));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", tasks);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(NotificationTask.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public Time getCarriedOutTime() {
        return CarriedOutTime;
    }

    public void setCarriedOutTime(Time carriedOutTime) {
        CarriedOutTime = carriedOutTime;
    }

    public GregorianCalendar getCarriedOutDate() {
        return CarriedOutDate;
    }

    public void setCarriedOutDate(GregorianCalendar carriedOutDate) {
        CarriedOutDate = carriedOutDate;
    }

    public String getCarriedOutBy() {
        return CarriedOutBy;
    }

    public void setCarriedOutBy(String carriedOutBy) {
        CarriedOutBy = carriedOutBy;
    }

    public String getOnlineSearch() {
        return OnlineSearch;
    }

    public void setOnlineSearch(String onlineSearch) {
        OnlineSearch = onlineSearch;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getStatusFlag() {
        return StatusFlag;
    }

//Setter and Getter methods

    public void setStatusFlag(String statusFlag) {
        StatusFlag = statusFlag;
    }

    private void initializeEntityProperties(boolean isWONotif) {
        if (!isWONotif) {
            this.setEntitySetName(ZCollections.NOTIFICATION_TASKS_COLLECTION);
            this.setEntityType(ZCollections.NOTIFICATION_TASKS_ENTITY_TYPE);
            this.setParentEntitySetName(ZCollections.NOTIFICATION_COLLECTION);
        } else {
            this.setEntitySetName(ZCollections.WO_NOTIFICATION_TASKS_COLLECTION);
            this.setEntityType(ZCollections.WO_NOTIFICATION_TASKS_ENTITY_TYPE);
            this.setParentEntitySetName(ZCollections.WO_NOTIFICATION_COLLECTION);
        }
        this.addKeyFieldNames(ZConfigManager.NOTIFICATION_KEY_FIELD);
        this.addKeyFieldNames(ZConfigManager.NOTIFICATION_ITEM_KEY_FIELD);
        this.addKeyFieldNames(ZConfigManager.NOTIFICATION_TASK_KEY_FIELD);
        this.setParentForeignKeyFieldName(ZConfigManager.NOTIFICATION_KEY_FIELD);
        this.setParentKeyFieldName(ZConfigManager.NOTIFICATION_KEY_FIELD);
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }

    public String getDisplayableNotificationNum() {
        if (getTempID() == null || getTempID().isEmpty())
            return getNotification();
        else
            return getTempID().replace(ZConfigManager.LOCAL_ID, ZConfigManager.LOCAL_IDENTIFIER);
    }

    public String getTask() {
        return Task;
    }

    public void setTask(String task) {
        Task = task;
    }

    public String getCatalogType() {
        return CatalogType;
    }

    public void setCatalogType(String catalogType) {
        CatalogType = catalogType;
    }

    public String getCodeGroup() {
        return CodeGroup;
    }

    public void setCodeGroup(String codeGroup) {
        CodeGroup = codeGroup;
    }

    public String getTaskCode() {
        return TaskCode;
    }

    public void setTaskCode(String taskCode) {
        TaskCode = taskCode;
    }

    public String getTaskText() {
        return TaskText;
    }

    public void setTaskText(String taskText) {
        TaskText = taskText;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public GregorianCalendar getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(GregorianCalendar createdOn) {
        CreatedOn = createdOn;
    }

    public String getChangedBy() {
        return ChangedBy;
    }

    public void setChangedBy(String changedBy) {
        ChangedBy = changedBy;
    }

    public GregorianCalendar getChangedOn() {
        return ChangedOn;
    }

    public void setChangedOn(GregorianCalendar changedOn) {
        ChangedOn = changedOn;
    }

    public GregorianCalendar getPlannedStart() {
        return PlannedStart;
    }

    public void setPlannedStart(GregorianCalendar plannedStart) {
        PlannedStart = plannedStart;
    }

    public GregorianCalendar getPlannedFinish() {
        return PlannedFinish;
    }

    public void setPlannedFinish(GregorianCalendar plannedFinish) {
        PlannedFinish = plannedFinish;
    }

    public String getObjectNumber() {
        return ObjectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        ObjectNumber = objectNumber;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getCause() {
        return Cause;
    }

    public void setCause(String cause) {
        Cause = cause;
    }

    public String getTaskProcessor() {
        return TaskProcessor;
    }

    public void setTaskProcessor(String taskProcessor) {
        TaskProcessor = taskProcessor;
    }

    public String getResponsible() {
        return Responsible;
    }

    public void setResponsible(String responsible) {
        Responsible = responsible;
    }

    public BigDecimal getQuantity() {
        return Quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        Quantity = quantity;
    }

    public String getUnitofMeasure() {
        return UnitofMeasure;
    }

    public void setUnitofMeasure(String unitofMeasure) {
        UnitofMeasure = unitofMeasure;
    }

    public String getDelete() {
        return Delete;
    }

    public void setDelete(String delete) {
        Delete = delete;
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getOperAct() {
        return OperAct;
    }

    public void setOperAct(String operAct) {
        OperAct = operAct;
    }

    public String getSystemStatus() {
        return SystemStatus;
    }

    public void setSystemStatus(String systemStatus) {
        SystemStatus = systemStatus;
    }

    public String getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(String userStatus) {
        UserStatus = userStatus;
    }

    public String getMobileStatus() {
        return MobileStatus;
    }

    public void setMobileStatus(String mobileStatus) {
        MobileStatus = mobileStatus;
    }

    public String getSortNumber() {
        return SortNumber;
    }

    public void setSortNumber(String sortNumber) {
        SortNumber = sortNumber;
    }

    public String getDisplayableTaskNumber() {
        if (TempID != null && !TempID.isEmpty())
            return ZConfigManager.LOCAL_IDENTIFIER + Task;
        return getTruncated(Task);
    }

//End of Setter and Getter methods

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
    }

    public String getTruncated(String originalNumber) {

        int truncatedNumber;
        truncatedNumber = Integer.parseInt(originalNumber);

        return truncatedNumber + "";
    }

    public Time getPlannedStartTime() {
        return PlannedStartTime;
    }

    public void setPlannedStartTime(Time plannedStartTime) {
        PlannedStartTime = plannedStartTime;
    }

    public Time getPlannedFinishTime() {
        return PlannedFinishTime;
    }

    public void setPlannedFinishTime(Time plannedFinishTime) {
        PlannedFinishTime = plannedFinishTime;
    }

    public String getCompletedBy() {
        return CompletedBy;
    }

    public void setCompletedBy(String completedBy) {
        CompletedBy = completedBy;
    }

    public GregorianCalendar getCompletedOn() {
        return CompletedOn;
    }

    public void setCompletedOn(GregorianCalendar completedOn) {
        CompletedOn = completedOn;
    }

    public Time getCompletionTime() {
        return CompletionTime;
    }

    public void setCompletionTime(Time completionTime) {
        CompletionTime = completionTime;
    }

    public String getUserStatusCode() {
        return UserStatusCode;
    }

    public void setUserStatusCode(String userStatusCode) {
        UserStatusCode = userStatusCode;
    }

    public String getMobileObjectType() {
        return MobileObjectType;
    }

    public void setMobileObjectType(String mobileObjectType) {
        MobileObjectType = mobileObjectType;
    }
}
