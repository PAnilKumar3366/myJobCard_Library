package com.ods.myjobcard_library.entities.transaction;

import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.sap.smp.client.odata.ODataEntity;

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
}
