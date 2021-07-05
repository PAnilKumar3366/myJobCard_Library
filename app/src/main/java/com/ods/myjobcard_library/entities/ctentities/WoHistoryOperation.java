package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.GregorianCalendar;

public class WoHistoryOperation extends ZBaseEntity {

    private String OnlineSearch;
    private String TransferFlag;
    private String TransferReason;
    private String TransferPerson;
    private String WorkOrderNum;
    private String OperationNum;
    private String PlannofOpera;
    private String Counter;
    private String Sequence;
    private String ControlKey;
    private String Plant;
    private String StdTextkey;
    private String ShortText;
    private String SupOpNode;
    private String SortTerm;
    private String MaterialGroup;
    private String Priority;
    private String ActivityType;
    private String SystemCondition;
    private String BusinessArea;
    private String PlanningPlant;
    private String PersonnelNo;
    private String InspectionType;
    private String DeletionFlag;
    private String FuncLoc;
    private String Equipment;
    private String SubOperation;
    private String ToWorkCenter;
    private String WorkCenter;
    private String NotificationNum;
    private BigDecimal ActualWork;
    private GregorianCalendar EarlSchStartExecDate;
    private Time EarlSchStartExecTime;
    private GregorianCalendar EarlSchFinishExecDate;
    private Time EarlSchFinishExecTime;
    private GregorianCalendar ActStartExecDate;
    private Time ActStartExecTime;
    private GregorianCalendar ActFinishExecDate;
    private Time ActFinishExectime;
    private GregorianCalendar LatestSchStartDate;
    private Time LatestSchStartTime;
    private GregorianCalendar LatestSchFinishDate;
    private Time LatestSchFinishTime;
    private String SystemStatusCode;
    private String SystemStatus;
    private String UserStatusCode;
    private String UserStatus;
    private String MobileStatus;
    private String StatusFlag;
    private String PurchaseGroup;
    private String TrackingNu;
    private String Vendor;
    private String TempID;
    private String Notes;
    private String EnteredBy;
    private String ConfNo;

    public WoHistoryOperation(ODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    public WoHistoryOperation(ZODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.WO_HISTORY_OPERATION_SET);
        this.setEntityType(ZCollections.WO_HISTORY_OPERATION_ENTITY_TYPE);
        this.addKeyFieldNames("WorkOrderNum");
        this.addKeyFieldNames("OperationNum");
        this.addKeyFieldNames("SubOperation");
    }

    public String getOnlineSearch() {
        return OnlineSearch;
    }

    public void setOnlineSearch(String onlineSearch) {
        OnlineSearch = onlineSearch;
    }

    public String getTransferFlag() {
        return TransferFlag;
    }

    public void setTransferFlag(String transferFlag) {
        TransferFlag = transferFlag;
    }

    public String getTransferReason() {
        return TransferReason;
    }

    public void setTransferReason(String transferReason) {
        TransferReason = transferReason;
    }

    public String getTransferPerson() {
        return TransferPerson;
    }

    public void setTransferPerson(String transferPerson) {
        TransferPerson = transferPerson;
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getOperationNum() {
        return OperationNum;
    }

    public void setOperationNum(String operationNum) {
        OperationNum = operationNum;
    }

    public String getPlannofOpera() {
        return PlannofOpera;
    }

    public void setPlannofOpera(String plannofOpera) {
        PlannofOpera = plannofOpera;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public String getSequence() {
        return Sequence;
    }

    public void setSequence(String sequence) {
        Sequence = sequence;
    }

    public String getControlKey() {
        return ControlKey;
    }

    public void setControlKey(String controlKey) {
        ControlKey = controlKey;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getStdTextkey() {
        return StdTextkey;
    }

    public void setStdTextkey(String stdTextkey) {
        StdTextkey = stdTextkey;
    }

    public String getShortText() {
        return ShortText;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }

    public String getSupOpNode() {
        return SupOpNode;
    }

    public void setSupOpNode(String supOpNode) {
        SupOpNode = supOpNode;
    }

    public String getSortTerm() {
        return SortTerm;
    }

    public void setSortTerm(String sortTerm) {
        SortTerm = sortTerm;
    }

    public String getMaterialGroup() {
        return MaterialGroup;
    }

    public void setMaterialGroup(String materialGroup) {
        MaterialGroup = materialGroup;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getActivityType() {
        return ActivityType;
    }

    public void setActivityType(String activityType) {
        ActivityType = activityType;
    }

    public String getSystemCondition() {
        return SystemCondition;
    }

    public void setSystemCondition(String systemCondition) {
        SystemCondition = systemCondition;
    }

    public String getBusinessArea() {
        return BusinessArea;
    }

    public void setBusinessArea(String businessArea) {
        BusinessArea = businessArea;
    }

    public String getPlanningPlant() {
        return PlanningPlant;
    }

    public void setPlanningPlant(String planningPlant) {
        PlanningPlant = planningPlant;
    }

    public String getPersonnelNo() {
        return PersonnelNo;
    }

    public void setPersonnelNo(String personnelNo) {
        PersonnelNo = personnelNo;
    }

    public String getInspectionType() {
        return InspectionType;
    }

    public void setInspectionType(String inspectionType) {
        InspectionType = inspectionType;
    }

    public String getDeletionFlag() {
        return DeletionFlag;
    }

    public void setDeletionFlag(String deletionFlag) {
        DeletionFlag = deletionFlag;
    }

    public String getFuncLoc() {
        return FuncLoc;
    }

    public void setFuncLoc(String funcLoc) {
        FuncLoc = funcLoc;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getSubOperation() {
        return SubOperation;
    }

    public void setSubOperation(String subOperation) {
        SubOperation = subOperation;
    }

    public String getToWorkCenter() {
        return ToWorkCenter;
    }

    public void setToWorkCenter(String toWorkCenter) {
        ToWorkCenter = toWorkCenter;
    }

    public String getWorkCenter() {
        return WorkCenter;
    }

    public void setWorkCenter(String workCenter) {
        WorkCenter = workCenter;
    }

    public String getNotificationNum() {
        return NotificationNum;
    }

    public void setNotificationNum(String notificationNum) {
        NotificationNum = notificationNum;
    }

    public BigDecimal getActualWork() {
        return ActualWork;
    }

    public void setActualWork(BigDecimal actualWork) {
        ActualWork = actualWork;
    }

    public GregorianCalendar getEarlSchStartExecDate() {
        return EarlSchStartExecDate;
    }

    public void setEarlSchStartExecDate(GregorianCalendar earlSchStartExecDate) {
        EarlSchStartExecDate = earlSchStartExecDate;
    }

    public Time getEarlSchStartExecTime() {
        return EarlSchStartExecTime;
    }

    public void setEarlSchStartExecTime(Time earlSchStartExecTime) {
        EarlSchStartExecTime = earlSchStartExecTime;
    }

    public GregorianCalendar getEarlSchFinishExecDate() {
        return EarlSchFinishExecDate;
    }

    public void setEarlSchFinishExecDate(GregorianCalendar earlSchFinishExecDate) {
        EarlSchFinishExecDate = earlSchFinishExecDate;
    }

    public Time getEarlSchFinishExecTime() {
        return EarlSchFinishExecTime;
    }

    public void setEarlSchFinishExecTime(Time earlSchFinishExecTime) {
        EarlSchFinishExecTime = earlSchFinishExecTime;
    }

    public GregorianCalendar getActStartExecDate() {
        return ActStartExecDate;
    }

    public void setActStartExecDate(GregorianCalendar actStartExecDate) {
        ActStartExecDate = actStartExecDate;
    }

    public Time getActStartExecTime() {
        return ActStartExecTime;
    }

    public void setActStartExecTime(Time actStartExecTime) {
        ActStartExecTime = actStartExecTime;
    }

    public GregorianCalendar getActFinishExecDate() {
        return ActFinishExecDate;
    }

    public void setActFinishExecDate(GregorianCalendar actFinishExecDate) {
        ActFinishExecDate = actFinishExecDate;
    }

    public Time getActFinishExectime() {
        return ActFinishExectime;
    }

    public void setActFinishExectime(Time actFinishExectime) {
        ActFinishExectime = actFinishExectime;
    }

    public GregorianCalendar getLatestSchStartDate() {
        return LatestSchStartDate;
    }

    public void setLatestSchStartDate(GregorianCalendar latestSchStartDate) {
        LatestSchStartDate = latestSchStartDate;
    }

    public Time getLatestSchStartTime() {
        return LatestSchStartTime;
    }

    public void setLatestSchStartTime(Time latestSchStartTime) {
        LatestSchStartTime = latestSchStartTime;
    }

    public GregorianCalendar getLatestSchFinishDate() {
        return LatestSchFinishDate;
    }

    public void setLatestSchFinishDate(GregorianCalendar latestSchFinishDate) {
        LatestSchFinishDate = latestSchFinishDate;
    }

    public Time getLatestSchFinishTime() {
        return LatestSchFinishTime;
    }

    public void setLatestSchFinishTime(Time latestSchFinishTime) {
        LatestSchFinishTime = latestSchFinishTime;
    }

    public String getSystemStatusCode() {
        return SystemStatusCode;
    }

    public void setSystemStatusCode(String systemStatusCode) {
        SystemStatusCode = systemStatusCode;
    }

    public String getSystemStatus() {
        return SystemStatus;
    }

    public void setSystemStatus(String systemStatus) {
        SystemStatus = systemStatus;
    }

    public String getUserStatusCode() {
        return UserStatusCode;
    }

    public void setUserStatusCode(String userStatusCode) {
        UserStatusCode = userStatusCode;
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

    public String getStatusFlag() {
        return StatusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        StatusFlag = statusFlag;
    }

    public String getPurchaseGroup() {
        return PurchaseGroup;
    }

    public void setPurchaseGroup(String purchaseGroup) {
        PurchaseGroup = purchaseGroup;
    }

    public String getTrackingNu() {
        return TrackingNu;
    }

    public void setTrackingNu(String trackingNu) {
        TrackingNu = trackingNu;
    }

    public String getVendor() {
        return Vendor;
    }

    public void setVendor(String vendor) {
        Vendor = vendor;
    }

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getConfNo() {
        return ConfNo;
    }

    public void setConfNo(String confNo) {
        ConfNo = confNo;
    }
}
