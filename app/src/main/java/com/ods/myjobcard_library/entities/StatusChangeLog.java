package com.ods.myjobcard_library.entities;

import android.location.Location;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.GregorianCalendar;
import java.util.List;

public class StatusChangeLog extends ZBaseEntity {

    private String Counter;
    private String ObjectNum;
    private String Operation;
    private String StatusCode;
    private String PostedBy;
    private GregorianCalendar StatusChangedTime;
    private boolean IsConsidered;
    private String StatusCategory;
    private BigDecimal Longitude;
    private BigDecimal Latitude;
    private Time StatusTime;

    public StatusChangeLog(ODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    public StatusChangeLog() {
        initializeEntityProperties();
    }

    //helper methods
    public static StatusChangeLog getObjectStatusTimeLog(String objectNumber, String operation, com.ods.myjobcard_library.entities.appsettings.StatusCategory newStatus) {
        StatusChangeLog statusChangeLog = null;
        ResponseObject result;
        try {
            String entitySetName = ZCollections.STATUS_CHANGE_LOG_COLLECTION;
            String resPath = entitySetName + "?$filter=ObjectNum eq '" + objectNumber + "'"
                    + (operation != null && !operation.isEmpty() ? " and Operation eq '" + operation + "'" : "")
                    + " and StatusCode eq '" + newStatus.getRef_Cal_Status() + "' and IsConsidered eq false";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> logsList = (List<ODataEntity>) result.Content();
                if (logsList != null && logsList.size() > 0)
                    statusChangeLog = new StatusChangeLog(logsList.get(0));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(StatusChangeLog.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return statusChangeLog;
    }

    public static void saveStatusChangeLog(String objectNum, String operation, com.ods.myjobcard_library.entities.appsettings.StatusCategory status, GregorianCalendar changedTime, Location location) {
        try {
            StatusChangeLog statusChangeLog = new StatusChangeLog();
            statusChangeLog.setCounter(ZCommon.getReqTimeStamp(16));
            statusChangeLog.setObjectNum(objectNum);
            statusChangeLog.setOperation(operation);
            statusChangeLog.setIsConsidered(false);
            statusChangeLog.setStatusCategory(status.getStatuscCategory());
            statusChangeLog.setStatusChangedTime(changedTime);
            statusChangeLog.setStatusTime(new Time(changedTime.getTimeInMillis()));
            statusChangeLog.setStatusCode(status.getStatusCode());
            statusChangeLog.setPostedBy(ZAppSettings.strUser);
            if (location != null) {
                statusChangeLog.setLatitude(BigDecimal.valueOf(location.getLatitude()));
                statusChangeLog.setLongitude(BigDecimal.valueOf(location.getLongitude()));
            }
            statusChangeLog.setMode(ZAppSettings.EntityMode.Create);
            statusChangeLog.SaveToStore(false);
        } catch (Exception e) {
            DliteLogger.WriteLog(StatusChangeLog.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public String getPostedBy() {
        return PostedBy;
    }

    public void setPostedBy(String postedBy) {
        PostedBy = postedBy;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.STATUS_CHANGE_LOG_COLLECTION);
        this.setEntityType(ZCollections.STATUS_CHANGE_LOG_ENTITY_TYPE);
        this.addKeyFieldNames("ObjectNum");
        this.addKeyFieldNames("Operation");
        this.addKeyFieldNames("StatusCode");
        this.addKeyFieldNames("Counter");
    }

    public String getObjectNum() {
        return ObjectNum;
    }

    public void setObjectNum(String objectNum) {
        ObjectNum = objectNum;
    }

    public String getOperation() {
        return Operation;
    }

    public void setOperation(String operation) {
        Operation = operation;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public GregorianCalendar getStatusChangedTime() {
        return StatusChangedTime;
    }

    public void setStatusChangedTime(GregorianCalendar statusChangedTime) {
        StatusChangedTime = statusChangedTime;
    }

    public boolean getIsConsidered() {
        return IsConsidered;
    }

    public void setIsConsidered(boolean considered) {
        IsConsidered = considered;
    }

    public String getStatusCategory() {
        return StatusCategory;
    }

    public void setStatusCategory(String statusCategory) {
        StatusCategory = statusCategory;
    }

    public Time getStatusTime() {
        return StatusTime;
    }

    public void setStatusTime(Time statusTime) {
        StatusTime = statusTime;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public BigDecimal getLongitude() {
        return Longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        Longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return Latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        Latitude = latitude;
    }
}
