package com.ods.myjobcard_library.entities;

import com.ods.ods_sdk.StoreHelpers.BaseEntity;

import java.util.GregorianCalendar;

/**
 * Created by MY HOME on 4/16/2019.
 */
public class StatusChangeEntry extends BaseEntity {

    private String WorkOrderNum;
    private String Operation;
    private String StatusCode;
    private GregorianCalendar StatusChangedTime;
    private boolean IsConsidered;

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
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

    public boolean isConsidered() {
        return IsConsidered;
    }

    public void setIsConsidered(boolean isConsidered) {
        IsConsidered = isConsidered;
    }
}
