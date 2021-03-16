package com.ods.myjobcard_library.entities.glassentities;

import com.ods.myjobcard_library.entities.transaction.WorkOrder;

import java.io.Serializable;
import java.util.ArrayList;

public class WorkOrderGlassEntity implements Serializable {

    private String WorkOrderNum;
    private String OrderType;
    private String ShortText;
    private String Priority;
    private String EquipNum;
    private String FuncLocation;
    private String MobileObjStatus;
    private String AddressNumber;
    private String WOAddressNumber;
    private String StatusFlag;
    private String UserStatus;
    ArrayList<OperationGlassEntity> oprentities;
    ArrayList<ComponentGlassEntity> componentGlassEntities;

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getShortText() {
        return ShortText;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getEquipNum() {
        return EquipNum;
    }

    public void setEquipNum(String equipNum) {
        EquipNum = equipNum;
    }

    public String getFuncLocation() {
        return FuncLocation;
    }

    public void setFuncLocation(String funcLocation) {
        FuncLocation = funcLocation;
    }

    public String getMobileObjStatus() {
        return MobileObjStatus;
    }

    public void setMobileObjStatus(String mobileObjStatus) {
        MobileObjStatus = mobileObjStatus;
    }

    public String getAddressNumber() {
        return AddressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        AddressNumber = addressNumber;
    }

    public String getWOAddressNumber() {
        return WOAddressNumber;
    }

    public void setWOAddressNumber(String WOAddressNumber) {
        this.WOAddressNumber = WOAddressNumber;
    }

    public String getStatusFlag() {
        return StatusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        StatusFlag = statusFlag;
    }

    public String getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(String userStatus) {
        UserStatus = userStatus;
    }

    public static WorkOrderGlassEntity getWorkOrder() {
        WorkOrder workOrder = WorkOrder.getCurrWo();
        WorkOrderGlassEntity workOrderGlassEntity = new WorkOrderGlassEntity();
        workOrderGlassEntity.setEquipNum(workOrder.getEquipNum());
        workOrderGlassEntity.setFuncLocation(workOrder.getFuncLocation());
        workOrderGlassEntity.setMobileObjStatus(workOrder.getStatusDetail().getStatusCode());
        workOrderGlassEntity.setOrderType(workOrder.getOrderType());
        workOrderGlassEntity.setPriority(workOrder.getPriority());
        workOrderGlassEntity.setShortText(workOrder.getShortText());
        workOrderGlassEntity.setUserStatus(workOrder.getUserStatus());
        workOrderGlassEntity.setWorkOrderNum(workOrder.getWorkOrderNum());
        workOrderGlassEntity.setOprentities(OperationGlassEntity.getWorkOrderOperations(workOrder.getWorkOrderNum()));
        workOrderGlassEntity.setComponentGlassEntities(ComponentGlassEntity.getWorkOrderComponent(workOrder.getWorkOrderNum()));
        return workOrderGlassEntity;
    }

    public ArrayList<OperationGlassEntity> getOprentities() {
        return oprentities;
    }

    public void setOprentities(ArrayList<OperationGlassEntity> oprentities) {
        this.oprentities = oprentities;
    }

    public ArrayList<ComponentGlassEntity> getComponentGlassEntities() {
        return componentGlassEntities;
    }

    public void setComponentGlassEntities(ArrayList<ComponentGlassEntity> componentGlassEntities) {
        this.componentGlassEntities = componentGlassEntities;
    }
}
