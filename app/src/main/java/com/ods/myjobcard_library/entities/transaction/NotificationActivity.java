package com.ods.myjobcard_library.entities.transaction;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by lenovo on 27-12-2015.
 */
public class NotificationActivity extends BaseEntity {

    //header details
    private String Notification;
    private String Activity;
    private String Item;
    private String OnlineSearch;
    private String EnteredBy;
    private String Cause;
    private String CatalogType;
    private String CodeGroup;
    private String ActivityCode;
    private String ActivityText;
    private String ActivityCodeText;
    private String CreatedBy;
    private GregorianCalendar CreatedOn;
    private String TaskClass;
    private String Classificatn;
    private GregorianCalendar StartDate;
    private GregorianCalendar EndDate;
    private String Delete;
    private String SortNumber;
    private String WorkOrderNum;
    private String OperAct;
    private String TempID;
    private Time StartTime;
    private Time EndTime;

    //constructor
    public NotificationActivity(String notification, String activity, boolean isWONotif) {
        super();
        this.Notification = notification;
        this.Activity = activity;
        initializeEntityProperties(isWONotif);
    }

    public NotificationActivity(ODataEntity entity, boolean isWONotif) {
        initializeEntityProperties(isWONotif);
        create(entity);
    }

    //get methods
    public static ResponseObject getActivities(ZAppSettings.FetchLevel fetchLevel, String notifNum, String itemNum, String activityNum, boolean isWONotif) {

        ResponseObject result = null;
        String resPath = "";
        String strEntitySet = ZCollections.NOTIFICATION_ACTIVITY_COLLECTION;
        String strOrderByURI = "&$orderby=Activity";
        boolean isFirstParam = true;
        try {
            if (isWONotif)
                strEntitySet = ZCollections.WO_NOTIFICATION_ACTIVITY_COLLECTION;
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
                    resPath += isFirstParam ? "?" : "&" + "$select=Activity,ActivityText,CodeGroup";
                    break;
                case Single:
                    if (activityNum != null && notifNum != null) {
                        resPath = strEntitySet + "?$filter=(Notification%20eq%20%27" + notifNum + "%27%20and%20Activity%20eq%20%27" + activityNum + "%27)";
                    }
                    break;
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
                ArrayList<NotificationActivity> activities = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    activities.add(new NotificationActivity(entity, isWONotif));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", activities);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(NotificationActivity.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
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


//Setter and Getter methods

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    private void initializeEntityProperties(boolean isWONotif) {
        if (!isWONotif) {
            this.setEntitySetName(ZCollections.NOTIFICATION_ACTIVITY_COLLECTION);
            this.setEntityType(ZCollections.NOTIFICATION_ACTIVITY_ENTITY_TYPE);
            this.setParentEntitySetName(ZCollections.NOTIFICATION_COLLECTION);
        } else {
            this.setEntitySetName(ZCollections.WO_NOTIFICATION_ACTIVITY_COLLECTION);
            this.setEntityType(ZCollections.WO_NOTIFICATION_ACTIVITY_ENTITY_TYPE);
            this.setParentEntitySetName(ZCollections.WO_NOTIFICATION_COLLECTION);
        }
        this.addKeyFieldNames(ZConfigManager.NOTIFICATION_KEY_FIELD);
        this.addKeyFieldNames(ZConfigManager.NOTIFICATION_ITEM_KEY_FIELD);
        this.addKeyFieldNames(ZConfigManager.NOTIFICATION_ACTIVITY_KEY_FIELD);
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

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
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

    public String getActivityCode() {
        return ActivityCode;
    }

    public void setActivityCode(String activityCode) {
        ActivityCode = activityCode;
    }

    public String getActivityText() {
        return ActivityText;
    }

    public void setActivityText(String activityText) {
        ActivityText = activityText;
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

    public String getTaskClass() {
        return TaskClass;
    }

    public void setTaskClass(String taskClass) {
        TaskClass = taskClass;
    }

    public String getClassificatn() {
        return Classificatn;
    }

    public void setClassificatn(String classificatn) {
        Classificatn = classificatn;
    }

    public GregorianCalendar getStartDate() {
        return StartDate;
    }

    public void setStartDate(GregorianCalendar startDate) {
        StartDate = startDate;
    }

    public GregorianCalendar getEndDate() {
        return EndDate;
    }

    public void setEndDate(GregorianCalendar endDate) {
        EndDate = endDate;
    }

    public String getDelete() {
        return Delete;
    }

    public void setDelete(String delete) {
        Delete = delete;
    }

    public String getSortNumber() {
        return SortNumber;
    }

    public void setSortNumber(String sortNumber) {
        SortNumber = sortNumber;
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

    public Time getStartTime() {
        return StartTime;
    }

    public void setStartTime(Time startTime) {
        StartTime = startTime;
    }

    public Time getEndTime() {
        return EndTime;
    }

    public void setEndTime(Time endTime) {
        EndTime = endTime;
    }

    public String getDisplayableActivityNumber() {
        if (TempID != null && !TempID.isEmpty())
            return ZConfigManager.LOCAL_IDENTIFIER + Activity;
        return getTruncated(Activity);
    }

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
    }

//End of Setter and Getter methods

    public String getActivityCodeText() {
        return ActivityCodeText;
    }

    public void setActivityCodeText(String activityCodeText) {
        ActivityCodeText = activityCodeText;
    }

    public String getTruncated(String originalNumber) {
        int truncatedNumber;
        truncatedNumber = Integer.parseInt(originalNumber);
        return truncatedNumber + "";
    }

}
