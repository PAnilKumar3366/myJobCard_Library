package com.ods.myjobcard_library.entities.transaction;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.GregorianCalendar;

public class CapacityLevelData extends ZBaseEntity
{
    private String CapRecordId;
    private String IntCounter;
    private String CapReqCnt;
    private String WoNum;
    private String OprRoutNo;
    private String OprCounter;
    private String Operation;
    private String RemSplitInd;
    private String CapacityId;
    private BigDecimal NoOfSplits;
    private String PersNo;
    private Short SplitNo;
    private BigDecimal Work;
    private String WorkUOM;
    private BigDecimal NormalDuration;
    private String NormalDurationUnit;
    private GregorianCalendar StartDate;
    private Time StartTime;
    private GregorianCalendar EndDate;
    private Time EndTime;
    private String EnteredBy;
    private String Action;


    /**
     * Constructor  to create or map the new instance with the given ZODataEntity Object.
     *
     * @param entity ZODataEntity Contains the oDataEntity or EntityValue instance map to
     */
    public CapacityLevelData(ZODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    public CapacityLevelData(ODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }
    public CapacityLevelData(){
        initializeEntityProperties();
    }
    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.CAPACITY_LEVEL_ENTITY_COLLECTION);
        this.setEntityType(ZCollections.CAPACITY_LEVEL_ENTITY_TYPE);
        this.addKeyFieldNames("CapRecordId");
        this.addKeyFieldNames("IntCounter");
        this.addKeyFieldNames("CapReqCnt");
        this.addKeyFieldNames("WoNum");
        this.addKeyFieldNames("Operation");
    }

    //Added service metadata

    public String getCapRecordId() {
        return CapRecordId;
    }

    public void setCapRecordId(String capRecordId) {
        CapRecordId = capRecordId;
    }

    public String getCapReqCnt() {
        return CapReqCnt;
    }

    public void setCapReqCnt(String capReqCnt) {
        CapReqCnt = capReqCnt;
    }

    public String getCapacityId() {
        return CapacityId;
    }

    public void setCapacityId(String capacityId) {
        CapacityId = capacityId;
    }

    public String getIntCounter() {
        return IntCounter;
    }

    public void setIntCounter(String intCounter) {
        IntCounter = intCounter;
    }

    public String getWoNum() {
        return WoNum;
    }

    public void setWoNum(String woNum) {
        WoNum = woNum;
    }

    public String getOperation() {
        return Operation;
    }

    public void setOperation(String operation) {
        Operation = operation;
    }

    public BigDecimal getNoOfSplits() {
        return NoOfSplits;
    }

    public void setNoOfSplits(BigDecimal noOfSplits) {
        NoOfSplits = noOfSplits;
    }

    public BigDecimal getNormalDuration() {
        return NormalDuration;
    }

    public void setNormalDuration(BigDecimal normalDuration) {
        NormalDuration = normalDuration;
    }

    public String getNormalDurationUnit() {
        return NormalDurationUnit;
    }

    public void setNormalDurationUnit(String normalDurationUnit) {
        NormalDurationUnit = normalDurationUnit;
    }

    public BigDecimal getWork() {
        return Work;
    }

    public void setWork(BigDecimal work) {
        Work = work;
    }

    public Short getSplitNo() {
        return SplitNo;
    }

    public void setSplitNo(Short splitNo) {
        SplitNo = splitNo;
    }

    public String getOprCounter() {
        return OprCounter;
    }

    public void setOprCounter(String oprCounter) {
        OprCounter = oprCounter;
    }

    public String getOprRoutNo() {
        return OprRoutNo;
    }

    public void setOprRoutNo(String oprRoutNo) {
        OprRoutNo = oprRoutNo;
    }

    public String getPersNo() {
        return PersNo;
    }

    public void setPersNo(String persNo) {
        PersNo = persNo;
    }

    public String getRemSplitInd() {
        return RemSplitInd;
    }

    public void setRemSplitInd(String remSplitInd) {
        RemSplitInd = remSplitInd;
    }

    public String getWorkUOM() {
        return WorkUOM;
    }

    public void setWorkUOM(String workUOM) {
        WorkUOM = workUOM;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public GregorianCalendar getStartDate() {
        return StartDate;
    }

    public void setStartDate(GregorianCalendar startDate) {
        StartDate = startDate;
    }

    public Time getStartTime() {
        return StartTime;
    }

    public void setStartTime(Time startTime) {
        StartTime = startTime;
    }

    public GregorianCalendar getEndDate() {
        return EndDate;
    }

    public void setEndDate(GregorianCalendar endDate) {
        EndDate = endDate;
    }

    public Time getEndTime() {
        return EndTime;
    }

    public void setEndTime(Time endTime) {
        EndTime = endTime;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }
}
