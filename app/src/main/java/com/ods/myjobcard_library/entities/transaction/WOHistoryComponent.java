package com.ods.myjobcard_library.entities.transaction;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.forms.FormAssignmentSetModel;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * this model is reading the data from the WOHistoryComponent service.
 */
public class WOHistoryComponent extends ZBaseEntity {

    private String ItemCategory;
    private String ReferenceOrder;
    private String Reservation;
    private String Item;
    private String EnteredBy;
    private boolean Deleted;
    private boolean MvtAllowed;
    private boolean FinalIssue;
    private String Material;
    private String Plant;
    private String StorLocation;
    private String Batch;
    private BigDecimal ReqmtQy;
    private String BaseUnit;
    private BigDecimal WithdrawalQty;
    private BigDecimal QuantityinUne;
    private String UnitofEntry;
    private String WorkOrderNum;
    private String MovementType;
    private String ItemText;
    private String PlannofOpera;
    private String StorageBin;
    private String OperAct;
    private String Counter;
    private boolean BackFlush;
    private String MaterialGroup;
    private String Description;
    private String TempID;
    private String IssueFlag;
    private String OnlineSearch;

    public WOHistoryComponent(ODataEntity entity) {
        create(entity);
    }

    public WOHistoryComponent(ZODataEntity entity) {
        create(entity);
    }

    public String getItemCategory() {
        return ItemCategory;
    }

    public void setItemCategory(String itemCategory) {
        ItemCategory = itemCategory;
    }

    public String getReferenceOrder() {
        return ReferenceOrder;
    }

    public void setReferenceOrder(String referenceOrder) {
        ReferenceOrder = referenceOrder;
    }

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

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public boolean isDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }

    public boolean isMvtAllowed() {
        return MvtAllowed;
    }

    public void setMvtAllowed(boolean mvtAllowed) {
        MvtAllowed = mvtAllowed;
    }

    public boolean isFinalIssue() {
        return FinalIssue;
    }

    public void setFinalIssue(boolean finalIssue) {
        FinalIssue = finalIssue;
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

    public String getBatch() {
        return Batch;
    }

    public void setBatch(String batch) {
        Batch = batch;
    }

    public BigDecimal getReqmtQy() {
        return ReqmtQy;
    }

    public void setReqmtQy(BigDecimal reqmtQy) {
        ReqmtQy = reqmtQy;
    }

    public String getBaseUnit() {
        return BaseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        BaseUnit = baseUnit;
    }

    public BigDecimal getWithdrawalQty() {
        return WithdrawalQty;
    }

    public void setWithdrawalQty(BigDecimal withdrawalQty) {
        WithdrawalQty = withdrawalQty;
    }

    public BigDecimal getQuantityinUne() {
        return QuantityinUne;
    }

    public void setQuantityinUne(BigDecimal quantityinUne) {
        QuantityinUne = quantityinUne;
    }

    public String getUnitofEntry() {
        return UnitofEntry;
    }

    public void setUnitofEntry(String unitofEntry) {
        UnitofEntry = unitofEntry;
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

    public String getItemText() {
        return ItemText;
    }

    public void setItemText(String itemText) {
        ItemText = itemText;
    }

    public String getPlannofOpera() {
        return PlannofOpera;
    }

    public void setPlannofOpera(String plannofOpera) {
        PlannofOpera = plannofOpera;
    }

    public String getStorageBin() {
        return StorageBin;
    }

    public void setStorageBin(String storageBin) {
        StorageBin = storageBin;
    }

    public String getOperAct() {
        return OperAct;
    }

    public void setOperAct(String operAct) {
        OperAct = operAct;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public boolean isBackFlush() {
        return BackFlush;
    }

    public void setBackFlush(boolean backFlush) {
        BackFlush = backFlush;
    }

    public String getMaterialGroup() {
        return MaterialGroup;
    }

    public void setMaterialGroup(String materialGroup) {
        MaterialGroup = materialGroup;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
    }

    public String getIssueFlag() {
        return IssueFlag;
    }

    public void setIssueFlag(String issueFlag) {
        IssueFlag = issueFlag;
    }

    public String getOnlineSearch() {
        return OnlineSearch;
    }

    public void setOnlineSearch(String onlineSearch) {
        OnlineSearch = onlineSearch;
    }


    public static ArrayList<WOHistoryComponent> getWorkOrderHistoryComponents(String woNum) {
        ArrayList<WOHistoryComponent> orderHistoryComponents = new ArrayList<>();
        try {
            String strResPath = null;
            String entitySetName = ZCollections.WOHISTORY_COMPONENTS_COLLECTION;
            strResPath = entitySetName + "?$filter= ReferenceOrder eq '" + woNum + "'&$orderby=Item asc";
            ResponseObject result = getObjectsFromEntity(entitySetName, strResPath);
            if (!result.isError())
                return (ArrayList<WOHistoryComponent>) result.Content();
        } catch (Exception e) {
            DliteLogger.WriteLog(WOHistoryComponent.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return orderHistoryComponents;
    }

    public static ResponseObject getObjectsFromEntity(String entitySetName, String resPath) {
        ResponseObject result = null;
        try {
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (!result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                ArrayList<WOHistoryComponent> content = null;
                WOHistoryComponent woHistoryComponent;
                content = new ArrayList<WOHistoryComponent>();
                for (ODataEntity entity : entities) {
                    woHistoryComponent = new WOHistoryComponent(entity);
                    if (woHistoryComponent != null) {
                        content.add(woHistoryComponent);
                    } else {
                        //pending: log the error message
                    }
                }
                if (result == null) {
                    result = new ResponseObject(ZConfigManager.Status.Success);
                }
                result.setMessage("");
                result.setContent(content);
            }
        } catch (Exception e) {
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        if (result != null)
            return result;
        else
            return new ResponseObject(ZConfigManager.Status.Error);
    }
}