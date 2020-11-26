package com.ods.myjobcard_library.entities.transaction;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.Components;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;

/**
 * Created by lenovo on 12-09-2016.
 */
public class WOComponentIssue extends BaseEntity {

    public WOComponentIssue(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public WOComponentIssue() {
        initializeEntityProperties();
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.WO_COMPONENT_ISSUE_COLLECTION);
        this.setEntityType(ZCollections.WO_COMPONENT_ISSUE_ENTITY_TYPE);
        this.setParentEntitySetName(ZCollections.WO_COLLECTION);
        this.setParentForeignKeyFieldName(ZConfigManager.WO_KEY_FIELD);
        this.setParentKeyFieldName(ZConfigManager.WO_KEY_FIELD);
        this.addKeyFieldNames("Counter");
        this.addKeyFieldNames("Item");
        this.addKeyFieldNames("WorkOrderNum");
        this.addKeyFieldNames("OperAct");
    }

    private String Reservation;
    private String Item;
    private String Material;
    private String Plant;
    private String StorLocation;
    private BigDecimal IssueQty;
    private String UOM;
    private String WorkOrderNum;
    private String MovementType;
    private String OperAct;
    private String HeaderTxt;
    private String Counter;
    private String ValType;

    public String getReservation() {
        return Reservation;
    }

    public void setReservation(String reservation) {
        Reservation = reservation;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String material) {
        Material = material;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getStorLocation() {
        return StorLocation;
    }

    public void setStorLocation(String storLocation) {
        StorLocation = storLocation;
    }

    public BigDecimal getIssueQty() {
        return IssueQty;
    }

    public void setIssueQty(BigDecimal issueQty) {
        IssueQty = issueQty;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getMovementType() {
        return MovementType;
    }

    public void setMovementType(String movementType) {
        MovementType = movementType;
    }

    public String getOperAct() {
        return OperAct;
    }

    public void setOperAct(String operAct) {
        OperAct = operAct;
    }

    public String getHeaderTxt() {
        return HeaderTxt;
    }

    public void setHeaderTxt(String headerTxt) {
        HeaderTxt = headerTxt;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public static ResponseObject issueComponent(Components component, double issuedQty, String headerTxt, boolean autoFlush) {
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);
        try {
            WOComponentIssue componentIssue = new WOComponentIssue();
            componentIssue.setMode(ZAppSettings.EntityMode.Create);
            componentIssue.setIssueQty(new BigDecimal(issuedQty));
            componentIssue.setItem(component.getTruncated(component.getItem()));
            componentIssue.setMaterial(component.getMaterial());
            componentIssue.setOperAct(component.getOperAct());
            componentIssue.setPlant(component.getPlant());
            componentIssue.setReservation(component.getReservation());
            componentIssue.setStorLocation(component.getStorLocation());
            componentIssue.setUOM(component.getBaseUnit());
            componentIssue.setWorkOrderNum(component.getWorkOrderNum());
            componentIssue.setHeaderTxt(headerTxt);
            componentIssue.setCounter(ZCommon.getReqTimeStamp(8));
            componentIssue.setValType(component.getBatch());
            result = componentIssue.SaveToStore(false);

            if (!result.isError()) {
                component.setMode(ZAppSettings.EntityMode.Update);
                component.setWithdrawalQty(new BigDecimal(issuedQty).add(component.getWithdrawalQty()));
                result = component.SaveToStore(autoFlush);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WOComponentIssue.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result.setMessage(e.getMessage());
        }
        return result;
    }

    public String getValType() {
        return ValType;
    }

    public void setValType(String valType) {
        ValType = valType;
    }
}
