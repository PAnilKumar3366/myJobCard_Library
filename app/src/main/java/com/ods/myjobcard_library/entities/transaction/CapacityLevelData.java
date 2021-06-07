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
    private String WorkOrderNum;
    private String OperationNumber;
    private String GeneralCounterForOrder;
    private String PersonnelNumber;
    private Double WorkInvolvedInTheActivity;
    private String UnitForWork;
    private Double NormalDurationOfTheActivity;
    private String NormalDurationOrUnit;

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
    private GregorianCalendar Date;
    private Time Time;
    private String EnteredBy;



    public CapacityLevelData(String personnelNumber,String workDuration,String normalDuration)
    {
        PersonnelNumber=personnelNumber;
        WorkInvolvedInTheActivity=Double.parseDouble(workDuration);
        NormalDurationOfTheActivity=Double.parseDouble(normalDuration);
    }

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
    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.CAPACITY_LEVEL_ENTITY_COLLECTION);
        this.setEntityType(ZCollections.CAPACITY_LEVEL_ENTITY_TYPE);
        this.addKeyFieldNames("CapRecordId");
        this.addKeyFieldNames("IntCounter");
        this.addKeyFieldNames("CapReqCnt");
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getOperationNumber() {
        return OperationNumber;
    }

    public void setOperationNumber(String operationNumber) {
        OperationNumber = operationNumber;
    }

    public String getGeneralCounterForOrder() {
        return GeneralCounterForOrder;
    }

    public void setGeneralCounterForOrder(String generalCounterForOrder) {
        GeneralCounterForOrder = generalCounterForOrder;
    }

    public String getPersonnelNumber() {
        return PersonnelNumber;
    }

    public void setPersonnelNumber(String personnelNumber) {
        PersonnelNumber = personnelNumber;
    }

    public Double getWorkInvolvedInTheActivity() {
        return WorkInvolvedInTheActivity;
    }

    public void setWorkInvolvedInTheActivity(Double workInvolvedInTheActivity) {
        WorkInvolvedInTheActivity = workInvolvedInTheActivity;
    }

    public String getUnitForWork() {
        return UnitForWork;
    }

    public void setUnitForWork(String unitForWork) {
        UnitForWork = unitForWork;
    }

    public Double getNormalDurationOfTheActivity() {
        return NormalDurationOfTheActivity;
    }

    public void setNormalDurationOfTheActivity(Double normalDurationOfTheActivity) {
        NormalDurationOfTheActivity = normalDurationOfTheActivity;
    }

    public String getNormalDurationOrUnit() {
        return NormalDurationOrUnit;
    }

    public void setNormalDurationOrUnit(String normalDurationOrUnit) {
        NormalDurationOrUnit = normalDurationOrUnit;
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

    public GregorianCalendar getDate() {
        return Date;
    }

    public void setDate(GregorianCalendar date) {
        Date = date;
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

    public java.sql.Time getTime() {
        return Time;
    }

    public void setTime(java.sql.Time time) {
        Time = time;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }
}
