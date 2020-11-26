package com.ods.myjobcard_library.entities.transaction;

import androidx.annotation.NonNull;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ctentities.UserTable;
import com.ods.myjobcard_library.entities.supervisor.TeamMember;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by lenovo on 16-06-2016.
 */
public class UserTimeSheet extends BaseEntity {

    private String Counter;
    private String PersonnelNo;
    private GregorianCalendar Date;
    private String SendCCtr;
    private String ActivityType;
    private String SendingOrder;
    private String SendPOItem;
    private String SendBusProc;
    private String ActivityNumber;
    private String RecCCtr;
    private String RecWBSelem;
    private String RecOrder;
    private String Network;
    private String PlannofOpera;
    private String OperaCounter;
    private String AttAbsType;
    private String WageType;
    private String COArea;
    private String UnitofMeasure;
    private String Plant;
    private String OrderCategory;
    private String CompanyCode;
    private GregorianCalendar CreatedOn;
    private Time CreatedonTime;
    private String EnteredBy;
    private GregorianCalendar LastChange;
    private Time LastChangeTime;
    private String ChangedBy;
    private String ApprovedBy;
    private GregorianCalendar ApprovalDate;
    private String WorkItemID;
    private String Status;
    private String RefCounter;
    private String Reason;
    private String DocumentNumber;
    private String TaskCounter;
    private BigDecimal CatsHours;
    private Time StartTime;
    private Time EndTime;
    private boolean PreviousDay;
    private boolean FullDay;
    private BigDecimal RemainingWork;
    private String FinalConfirmtn;
    private String ShortText;
    private boolean LongText;
    private BigDecimal Amount;
    private BigDecimal CATSQuantity;
    private String OperAct;
    private String SubOperation;
    private String WorkCenter;
    private String PremiumNo;
    private String PremiumID;
    private boolean ErrorEntity;
    private String ErrorMsg;
    public UserTimeSheet() {
        initializeEntityProperties();
        this.setEntityResourcePath(getEntitySetName());
    }

    public UserTimeSheet(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity);
    }

    //get methods
    public static ResponseObject getUserTimeSheets(GregorianCalendar date, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        String resourcePath = null;
        String strEntitySet = null;
        String formattedDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(ZConfigManager.QUERIABLE_DATE_FORMAT);
        if (date != null) {
            formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
        }
        try {
            strEntitySet = ZCollections.USER_TIMESHEET_COLLECTION;
            switch (fetchLevel) {
                case List:
                    resourcePath = strEntitySet + "?$filter=(Date eq datetime'" + formattedDate + "')&$orderby=RecOrder";
                    break;
            }
            result = DataHelper.getInstance().getEntities(strEntitySet, resourcePath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities);
            }
        } catch (Exception e) {
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }


    //Setter and Getter methods

    public static ResponseObject getTechnicianTimeSheets(@NonNull String technicianId, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        String resourcePath = null;
        String strEntitySet = null;
        try {
            strEntitySet = ZCollections.SUPERVISOR_TIMESHEET_COLLECTIONS;
            switch (fetchLevel) {
                case List:
                    resourcePath = strEntitySet + "?$filter=(PersonnelNo eq '" + technicianId + "')";
                    break;
            }
            result = DataHelper.getInstance().getEntities(strEntitySet, resourcePath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities);
            }
        } catch (Exception e) {
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<UserTimeSheet> timeEntries = new ArrayList<UserTimeSheet>();
                for (ODataEntity entity : entities) {
                    timeEntries.add(new UserTimeSheet(entity, ZAppSettings.FetchLevel.List));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", timeEntries);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(TeamMember.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ResponseObject postAutoTimeEntry(String activityType, BigDecimal duration, WorkOrder workOrder, Long startTime, Long endTime, boolean autoFlush) {
        ResponseObject result = null;
        try {
            result = getTimeSheetObjectForAutoPost(activityType, duration, startTime, endTime, workOrder);
            if (!result.isError()) {
                UserTimeSheet timeEntry = (UserTimeSheet) result.Content();
                result = timeEntry.postUserTime(autoFlush);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(UserTimeSheet.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ResponseObject getTimeSheetObjectForAutoPost(String activityType, BigDecimal duration, Long startTime, Long endTime, WorkOrder workOrder) {
        ResponseObject result = null;
        try {
            UserTimeSheet timeEntry = new UserTimeSheet();

            /*String oprNum;
            if(wo.getCurrentOperation() == null) {
                oprNum = (String) (Operation.getFirstOperationNum(wo.getWorkOrderNum()).Content());
                if(oprNum != null && !oprNum.isEmpty()) {
                    timeEntry.setOperAct(oprNum);
                }
            }
            else */
            if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED && workOrder.getCurrentOperation() != null) {
                timeEntry.setOperAct(workOrder.getCurrentOperation().getOperationNum());
            }
            timeEntry.setAttAbsType(ZConfigManager.ATT_TYPE_HOURS_OF_COSTING);
            timeEntry.setDate(ZCommon.getDeviceDateTime());
            timeEntry.getDate().set(Calendar.HOUR_OF_DAY, 0);
            timeEntry.getDate().set(Calendar.MINUTE, 0);
            timeEntry.getDate().set(Calendar.SECOND, 0);
            timeEntry.getDate().set(Calendar.MILLISECOND, 0);
            timeEntry.setActivityType(activityType);
            timeEntry.setMode(ZAppSettings.EntityMode.Create);
            timeEntry.setRecOrder(workOrder.getWorkOrderNum());
            if (!ZConfigManager.ENABLE_CAPTURE_DURATION && startTime != 0 && endTime != 0) {
                timeEntry.setStartTime(new Time(startTime));
                timeEntry.setEndTime(new Time(endTime));
            }
            timeEntry.setCatsHours(duration.setScale(2, RoundingMode.HALF_DOWN));
            timeEntry.setCOArea(UserTable.getUserCntrlArea());
            timeEntry.setCounter(ZCommon.getReqTimeStamp(12));
            timeEntry.setPersonnelNo(UserTable.getUserPersonnelNumber());
            timeEntry.setPlant(workOrder.getPlant());
            timeEntry.setWorkCenter(workOrder.getMainWorkCtr());
            timeEntry.setPremiumID(ZConfigManager.DEFAULT_PREMIUM_ID);
            timeEntry.setPremiumNo(ZConfigManager.DEFAULT_PREMIUM_NO);
            result = new ResponseObject(ZConfigManager.Status.Success, "", timeEntry);
        } catch (Exception e) {
            DliteLogger.WriteLog(UserTimeSheet.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.USER_TIMESHEET_COLLECTION);
        this.setEntityType(ZCollections.USER_TIMESHEET_ENTITY_TYPE);
        this.addKeyFieldNames("Counter");
        this.setParentEntitySetName(ZCollections.USER_TIMESHEET_COLLECTION);
        this.setParentForeignKeyFieldName("Counter");
        this.setParentKeyFieldName("Counter");
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public String getPersonnelNo() {
        return PersonnelNo;
    }

    public void setPersonnelNo(String personnelNo) {
        PersonnelNo = personnelNo;
    }

    public GregorianCalendar getDate() {
        return Date;
    }

    public void setDate(GregorianCalendar date) {
        Date = date;
    }

    public String getSendCCtr() {
        return SendCCtr;
    }

    public void setSendCCtr(String sendCCtr) {
        SendCCtr = sendCCtr;
    }

    public String getActivityType() {
        return ActivityType;
    }

    public void setActivityType(String activityType) {
        ActivityType = activityType;
    }

    public String getSendingOrder() {
        return SendingOrder;
    }

    public void setSendingOrder(String sendingOrder) {
        SendingOrder = sendingOrder;
    }

    public String getSendPOItem() {
        return SendPOItem;
    }

    public void setSendPOItem(String sendPOItem) {
        SendPOItem = sendPOItem;
    }

    public String getSendBusProc() {
        return SendBusProc;
    }

    public void setSendBusProc(String sendBusProc) {
        SendBusProc = sendBusProc;
    }

    public String getActivityNumber() {
        return ActivityNumber;
    }

    public void setActivityNumber(String activityNumber) {
        ActivityNumber = activityNumber;
    }

    public String getRecCCtr() {
        return RecCCtr;
    }

    public void setRecCCtr(String recCCtr) {
        RecCCtr = recCCtr;
    }

    public String getRecWBSelem() {
        return RecWBSelem;
    }

    public void setRecWBSelem(String recWBSelem) {
        RecWBSelem = recWBSelem;
    }

    public String getRecOrder() {
        return RecOrder;
    }

    public void setRecOrder(String recOrder) {
        RecOrder = recOrder;
    }

    public String getNetwork() {
        return Network;
    }

    public void setNetwork(String network) {
        Network = network;
    }

    public String getPlannofOpera() {
        return PlannofOpera;
    }

    public void setPlannofOpera(String plannofOpera) {
        PlannofOpera = plannofOpera;
    }

    public String getOperaCounter() {
        return OperaCounter;
    }

    public void setOperaCounter(String operaCounter) {
        OperaCounter = operaCounter;
    }

    public String getAttAbsType() {
        return AttAbsType;
    }

    public void setAttAbsType(String attAbsType) {
        AttAbsType = attAbsType;
    }

    public String getWageType() {
        return WageType;
    }

    public void setWageType(String wageType) {
        WageType = wageType;
    }

    public String getCOArea() {
        return COArea;
    }

    public void setCOArea(String COArea) {
        this.COArea = COArea;
    }

    public String getUnitofMeasure() {
        return UnitofMeasure;
    }

    public void setUnitofMeasure(String unitofMeasure) {
        UnitofMeasure = unitofMeasure;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getOrderCategory() {
        return OrderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        OrderCategory = orderCategory;
    }

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        CompanyCode = companyCode;
    }

    public GregorianCalendar getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(GregorianCalendar createdOn) {
        CreatedOn = createdOn;
    }

    public Time getCreatedonTime() {
        return CreatedonTime;
    }

    public void setCreatedonTime(Time createdonTime) {
        CreatedonTime = createdonTime;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public GregorianCalendar getLastChange() {
        return LastChange;
    }

    public void setLastChange(GregorianCalendar lastChange) {
        LastChange = lastChange;
    }

    public Time getLastChangeTime() {
        return LastChangeTime;
    }

    public void setLastChangeTime(Time lastChangeTime) {
        LastChangeTime = lastChangeTime;
    }

    public String getChangedBy() {
        return ChangedBy;
    }

    public void setChangedBy(String changedBy) {
        ChangedBy = changedBy;
    }

    public String getApprovedBy() {
        return ApprovedBy;
    }

    public void setApprovedBy(String approvedBy) {
        ApprovedBy = approvedBy;
    }

    public GregorianCalendar getApprovalDate() {
        return ApprovalDate;
    }

    public void setApprovalDate(GregorianCalendar approvalDate) {
        ApprovalDate = approvalDate;
    }

    public String getWorkItemID() {
        return WorkItemID;
    }

    public void setWorkItemID(String workItemID) {
        WorkItemID = workItemID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRefCounter() {
        return RefCounter;
    }

    public void setRefCounter(String refCounter) {
        RefCounter = refCounter;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        DocumentNumber = documentNumber;
    }

    public String getTaskCounter() {
        return TaskCounter;
    }

    public void setTaskCounter(String taskCounter) {
        TaskCounter = taskCounter;
    }

    public BigDecimal getCatsHours() {
        return CatsHours;
    }

    public void setCatsHours(BigDecimal catsHours) {
        CatsHours = catsHours;
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

    public boolean getPreviousDay() {
        return PreviousDay;
    }

    public void setPreviousDay(boolean previousDay) {
        PreviousDay = previousDay;
    }

    public boolean getFullDay() {
        return FullDay;
    }

    public void setFullDay(boolean fullDay) {
        FullDay = fullDay;
    }

    public BigDecimal getRemainingWork() {
        return RemainingWork;
    }

    public void setRemainingWork(BigDecimal remainingWork) {
        RemainingWork = remainingWork;
    }

    public String getFinalConfirmtn() {
        return FinalConfirmtn;
    }

    public void setFinalConfirmtn(String finalConfirmtn) {
        FinalConfirmtn = finalConfirmtn;
    }

    public String getShortText() {
        return ShortText;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }

    public boolean getLongText() {
        return LongText;
    }

    public void setLongText(boolean longText) {
        LongText = longText;
    }

    public BigDecimal getAmount() {
        return Amount;
    }

    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    public BigDecimal getCATSQuantity() {
        return CATSQuantity;
    }

    public void setCATSQuantity(BigDecimal CATSQuantity) {
        this.CATSQuantity = CATSQuantity;
    }

    public String getOperAct() {
        return OperAct;
    }

    public void setOperAct(String operAct) {
        OperAct = operAct;
    }

    public String getSubOperation() {
        return SubOperation;
    }

    public void setSubOperation(String subOperation) {
        SubOperation = subOperation;
    }

    public String getWorkCenter() {
        return WorkCenter;
    }

    //End of Setter and geter Methods

    public void setWorkCenter(String workCenter) {
        WorkCenter = workCenter;
    }

    public boolean getErrorEntity() {
        return ErrorEntity;
    }

    public void setErrorEntity(boolean errorEntity) {
        ErrorEntity = errorEntity;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    //post methods
    public ResponseObject postUserTime(boolean autoFlush) {
        ResponseObject result = null;
//        result = DataHelper.getInstance().CreateUpdateUserTime(this, this.getMode());
        result = SaveToStore(autoFlush);
        if (result.isError())
            DliteLogger.WriteLog(UserTimeSheet.class, ZAppSettings.LogLevel.Error, result.getMessage());
        return result;
    }

    public String getPremiumNo() {
        return PremiumNo;
    }

    public void setPremiumNo(String premiumNo) {
        PremiumNo = premiumNo;
    }

    public String getPremiumID() {
        return PremiumID;
    }

    public void setPremiumID(String premiumID) {
        PremiumID = premiumID;
    }


    //----------------------------------------------------------------------------------------------

   /* public double convertTmeintoHrs(double hr, double mn){

        return hr + mn/60;
    }*/

    //----------------------------------------------------------------------------------------------


}
