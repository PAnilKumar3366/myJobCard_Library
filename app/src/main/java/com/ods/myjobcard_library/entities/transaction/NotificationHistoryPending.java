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

public class NotificationHistoryPending extends BaseEntity {

    private String Notification;
    private String NotificationType;

    //WO child elements
    //Fields
    private String Priority;
    private String Description;
    private GregorianCalendar CompletionDate;
    private String EnteredBy;
    private Time CompletionTime;
    private boolean Breakdown;
    private GregorianCalendar MalFunctionStart;
    private Time MalFunctionEnd;
    private GregorianCalendar ReqStart;
    private Time ReqStartTime;
    private String Equipment;
    private String FunctionalLoc;
    private GregorianCalendar RequiredEnd;
    private Time ReqEndtTime;
    private String ContactPerson;
    private String LastFirstName;
    private String EmplApplName;
    private String ObjectNumber;
    private String OnlineSearch;

    public NotificationHistoryPending(String notification) {
        super();
        this.Notification = notification;
    }

    public NotificationHistoryPending(ODataEntity entity) {
        create(entity);
    }


    //Setters and Getters Method

    //get methods
    public static ResponseObject getNotifHistoryPendingItems(String equipment, String functionalLoc, boolean isHistory) {

        ResponseObject result = null;
        String resourcePath = null;
        String strEntitySet = null;
        try {
            if (isHistory) {
                strEntitySet = ZCollections.NOTIFICATION_HISTORY_COLLECTION;
            } else {
                strEntitySet = ZCollections.NOTIFICATION_PENDING_COLLECTION;
            }
            resourcePath = strEntitySet;
            if (equipment == null)
                equipment = "";
            if (functionalLoc == null)
                functionalLoc = "";

            resourcePath += "?$filter=(Equipment eq '" + equipment + "' or FunctionalLoc eq '" + functionalLoc + "')";
            result = DataHelper.getInstance().getEntities(strEntitySet, resourcePath);
            if (!result.isError()) {
                //parse data for WO History / WO Pending from payload
                result = FromEntity((List<ODataEntity>) result.Content());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(NotificationHistoryPending.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;

    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<NotificationHistoryPending> historyPendings = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    historyPendings.add(new NotificationHistoryPending(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", historyPendings);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(NotificationHistoryPending.class, ZAppSettings.LogLevel.Error, e.getMessage());
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

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }

    public String getNotificationType() {
        return NotificationType;
    }

    public void setNotificationType(String notificationType) {
        NotificationType = notificationType;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public GregorianCalendar getCompletionDate() {
        return CompletionDate;
    }

    public void setCompletionDate(GregorianCalendar completionDate) {
        CompletionDate = completionDate;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public Time getCompletionTime() {
        return CompletionTime;
    }

    public void setCompletionTime(Time completionTime) {
        CompletionTime = completionTime;
    }

    public boolean isBreakdown() {
        return Breakdown;
    }

    public void setBreakdown(boolean breakdown) {
        Breakdown = breakdown;
    }

    public GregorianCalendar getMalFunctionStart() {
        return MalFunctionStart;
    }

    public void setMalFunctionStart(GregorianCalendar malFunctionStart) {
        MalFunctionStart = malFunctionStart;
    }

    public Time getMalFunctionEnd() {
        return MalFunctionEnd;
    }

    public void setMalFunctionEnd(Time malFunctionEnd) {
        MalFunctionEnd = malFunctionEnd;
    }

    public GregorianCalendar getReqStart() {
        return ReqStart;
    }

    public void setReqStart(GregorianCalendar reqStart) {
        ReqStart = reqStart;
    }

    public Time getReqStartTime() {
        return ReqStartTime;
    }

    public void setReqStartTime(Time reqStartTime) {
        ReqStartTime = reqStartTime;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getFunctionalLoc() {
        return FunctionalLoc;
    }

    public void setFunctionalLoc(String functionalLoc) {
        FunctionalLoc = functionalLoc;
    }

    public GregorianCalendar getRequiredEnd() {
        return RequiredEnd;
    }

    public void setRequiredEnd(GregorianCalendar requiredEnd) {
        RequiredEnd = requiredEnd;
    }

    public Time getReqEndtTime() {
        return ReqEndtTime;
    }

    public void setReqEndtTime(Time reqEndtTime) {
        ReqEndtTime = reqEndtTime;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }

    public String getLastFirstName() {
        return LastFirstName;
    }

    public void setLastFirstName(String lastFirstName) {
        LastFirstName = lastFirstName;
    }

    public String getEmplApplName() {
        return EmplApplName;
    }

    public void setEmplApplName(String emplApplName) {
        EmplApplName = emplApplName;
    }

    //End of Setters and Getters Method

    public String getObjectNumber() {
        return ObjectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        ObjectNumber = objectNumber;
    }

}