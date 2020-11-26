package com.ods.myjobcard_library.entities;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.supervisor.SupervisorWorkOrder;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Components extends BaseEntity {

    //Compoent child elements
    private DataHelper dataHelper = null;
    private ArrayList<String> longTexts = null;

    //Fields
    private String ItemCategory;
    private String BusinessAreaText;
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
    private BigDecimal ReqmtQty;
    private String BaseUnit;
    private BigDecimal WithdrawalQty;
    private BigDecimal QuantityinUnE;
    private String UnitofEntry;
    private String WorkOrderNum;
    private String MovementType;
    private String BusinessArea;
    private String Text;
    private String PlannofOpera;
    private String OperAct;
    private String Counter;
    private String BackFlush;
    private String StorageType;
    private String StorageBin;
    private String MaterialGroup;
    private String MaterialDescription;
    private String TempID;
    private String IssueFlag;
    private String OnlineSearch;
    private String ItemText;

    public Components(String workOrderNum, String plannofopera) {
        this.WorkOrderNum = workOrderNum;
        this.PlannofOpera = plannofopera;
        initializeEntityProperties();
    }

    public Components(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity);
    }

    public static ResponseObject getComponents(ZAppSettings.FetchLevel fetchLevel, String orderNum, String operationNum, String componentNum, String OrderByCriteria) {

        ResponseObject resultComponents = null;
        String resourcePath = null;
        String strOrderBy = "&$orderby=";
        String strOrderByURI = null;
        String strEntitySet = null;
        try {
            if (OrderByCriteria == null)
                OrderByCriteria = "Item desc";
            strOrderByURI = strOrderBy + OrderByCriteria;
            strEntitySet = ZCollections.COMPONENT_COLLECTION;
            switch (fetchLevel) {
                case List:
                    resourcePath = strEntitySet + "?$select=WorkOrderNum,OperAct,Item,FinalIssue,ReqmtQty,WithdrawalQty,MovementType,StorageType,StorageBin,BaseUnit&$filter=(WorkOrderNum eq '" + orderNum + "' and Deleted ne true)" + strOrderByURI;
                    break;
                case Header:
                    if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED || !ZConfigManager.WO_OP_OBJS_DISPLAY.equalsIgnoreCase("x"))
                        resourcePath = strEntitySet + "?$filter=(WorkOrderNum eq '" + orderNum + "' and Deleted ne true)" + strOrderByURI;//" + strOrderByURI +" )";
                    else
                        resourcePath = strEntitySet + "?$filter=(WorkOrderNum eq '" + orderNum + "' and OperAct eq '" + operationNum + "' and Deleted ne true)" + strOrderByURI;//" + strOrderByURI +" )";

                    break;
                case Single:
                    if (orderNum != null && orderNum.length() > 0 && componentNum != null && componentNum.length() > 0) {
                        resourcePath = strEntitySet + "?$filter=(WorkOrderNum eq '" + orderNum + "' and Item eq '" + componentNum + "')";//" + strOrderByURI +" )";
                    }
                    break;
                case All:
                    resourcePath = strEntitySet + "?$filter=(WorkOrderNum eq '" + orderNum + "' and Deleted ne true)" + strOrderByURI;//" + strOrderByURI +" )";
                    break;
                default:
                    resourcePath = strEntitySet + "?$filter=(WorkOrderNum eq '" + orderNum + "' and Deleted ne true)" + strOrderByURI;//" + strOrderByURI +" )";
                    break;
            }
            resultComponents = DataHelper.getInstance().getEntities(strEntitySet, resourcePath);
            if (resultComponents != null && !resultComponents.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) resultComponents.Content();
                resultComponents = FromEntity(entities, fetchLevel);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Components.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return resultComponents;
    }

    public static ResponseObject getSupervisorComponents(ZAppSettings.FetchLevel fetchLevel, String orderNum, String operationNum, String componentNum, String OrderByCriteria) {

        ResponseObject resultComponents = null;
        String resourcePath = null;
        String strOrderBy = "&$orderby=";
        String strOrderByURI = null;
        String strEntitySet = null;
        try {
            if (OrderByCriteria == null)
                OrderByCriteria = "Item";
            strOrderByURI = strOrderBy + OrderByCriteria;
            strEntitySet = ZCollections.SUPERVISOR_COMPONENT_COLLECTIONS;
            switch (fetchLevel) {
                case List:
                    resourcePath = strEntitySet + "?$select=WorkOrderNum,OperAct,Item,ReqmtQty,WithdrawalQty,MovementType,StorageType,StorageBin&$filter=(WorkOrderNum eq '" + orderNum + "')" + strOrderByURI;
                    break;
                case Header:
                    resourcePath = strEntitySet + "?$filter=(WorkOrderNum eq '" + orderNum + "')" + strOrderByURI;//" + strOrderByURI +" )";
                    break;
                case Single:
                    if (orderNum != null && orderNum.length() > 0 && componentNum != null && componentNum.length() > 0) {
                        resourcePath = strEntitySet + "?$filter=(WorkOrderNum eq '" + orderNum + "' and OperAct eq '" + operationNum + "' and Item eq '" + componentNum + "')";//" + strOrderByURI +" )";
                    }
                    break;
                case All:
                    resourcePath = strEntitySet + "?$filter=(WorkOrderNum eq '" + orderNum + "')" + strOrderByURI;//" + strOrderByURI +" )";
                    break;
                default:
                    resourcePath = strEntitySet + "?$filter=(WorkOrderNum eq '" + orderNum + "')" + strOrderByURI;//" + strOrderByURI +" )";
                    break;
            }
            resultComponents = DataHelper.getInstance().getEntities(strEntitySet, resourcePath);
            if (resultComponents != null && !resultComponents.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) resultComponents.Content();
                resultComponents = FromEntity(entities, fetchLevel);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Components.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return resultComponents;
    }


    //Setters and Getters Method

    public static Components getSupervisorComponent(String workOrderNum, String componentNum) {
        //Operation operation = null;
        Components component = null;
        try {
            //String entitySetName = ZCollections.OPR_COLLECTION;
            String entitySetName = ZCollections.SUPERVISOR_COMPONENT_COLLECTIONS;
            //String resPath = entitySetName + "(WorkOrderNum='"+ workOrderNum +"',OperationNum='"+ operationNum +"',SubOperation='"+ subOperation +"')";
            String resPath = entitySetName + "(WorkOrderNum='" + workOrderNum + "',Item='" + componentNum + "')";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                //component = new Operation((ODataEntity)result.Content(), ZZAppSettings.FetchLevel.Single);
                component = new Components((ODataEntity) result.Content(), ZAppSettings.FetchLevel.Single);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return component;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<Components> components = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    components.add(new Components(entity, fetchLevel));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", components);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(SupervisorWorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static int getHighestComponentNum(@NonNull String workOrderNum) {
        ResponseObject response = null;
        String resPath = ZCollections.COMPONENT_COLLECTION;
        resPath += "?$filter=(WorkOrderNum eq '" + workOrderNum + "')&$orderby=Item desc&$select=Item&$top=1";
        try {
            response = DataHelper.getInstance().getEntities(ZCollections.COMPONENT_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities.size() > 0) {
                    String oprNumStr = String.valueOf(entities.get(0).getProperties().get("Item").getValue());
                    if (oprNumStr != null && !oprNumStr.isEmpty()) {
                        return Integer.valueOf(oprNumStr);
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return 0;
    }

    public static ArrayList<Components> getOprComponents(ArrayList<Components> woComponents, String oprNum) {

        ArrayList<Components> oprComps = new ArrayList<>();
        for (Components comp : woComponents) {
            if (comp.getOperAct().equalsIgnoreCase(oprNum))
                oprComps.add(comp);
        }
        return oprComps;
    }

    public String getOnlineSearch() {
        return OnlineSearch;
    }

    public void setOnlineSearch(String onlineSearch) {
        OnlineSearch = onlineSearch;
    }

    public String getItemText() {
        return ItemText;
    }

    public void setItemText(String itemText) {
        ItemText = itemText;
    }

    public String getBusinessAreaText() {
        return BusinessAreaText;
    }

    public void setBusinessAreaText(String businessAreaText) {
        BusinessAreaText = businessAreaText;
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

    public BigDecimal getReqmtQty() {
        return ReqmtQty;
    }

    public void setReqmtQty(BigDecimal reqmtQty) {
        ReqmtQty = reqmtQty;
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

    public BigDecimal getQuantityinUnE() {
        return QuantityinUnE;
    }

    public void setQuantityinUnE(BigDecimal quantityinUnE) {
        QuantityinUnE = quantityinUnE;
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

    public String getBusinessArea() {
        return BusinessArea;
    }

    public void setBusinessArea(String businessArea) {
        BusinessArea = businessArea;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getPlannofOpera() {
        return PlannofOpera;
    }

    public void setPlannofOpera(String plannofOpera) {
        PlannofOpera = plannofOpera;
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

    public String getBackFlush() {
        return BackFlush;
    }

    public void setBackFlush(String backFlush) {
        BackFlush = backFlush;
    }

    public String getStorageType() {
        return StorageType;
    }

    public void setStorageType(String storageType) {
        StorageType = storageType;
    }

    public String getStorageBin() {
        return StorageBin;
    }

    public void setStorageBin(String storageBin) {
        StorageBin = storageBin;
    }

    public String getMaterialGroup() {
        return MaterialGroup;
    }

    public void setMaterialGroup(String materialGroup) {
        MaterialGroup = materialGroup;
    }

    public String getMaterialDescription() {
        return MaterialDescription;
    }

    public void setMaterialDescription(String materialDescription) {

        MaterialDescription = materialDescription;
    }

    //End of Setters and Getters Method

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
    }

    public String getItemCategory() {
        return ItemCategory;
    }

    public void setItemCategory(String itemCategory) {
        ItemCategory = itemCategory;
    }

    public ArrayList<String> getLongTexts() {
        return longTexts;
    }

    public void setLongTexts(ArrayList<String> longTexts) {
        this.longTexts = longTexts;
    }

    public String getIssueFlag() {
        return IssueFlag;
    }

    public void setIssueFlag(String issueFlag) {
        IssueFlag = issueFlag;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.COMPONENT_COLLECTION);
        this.setEntityType(ZCollections.COMPONENT_ENTITY_TYPE);
        this.addKeyFieldNames(ZConfigManager.WO_KEY_FIELD);
        this.addKeyFieldNames(ZConfigManager.COMPONENT_KEY_FIELD1);
        this.addKeyFieldNames(ZConfigManager.COMPONENT_KEY_FIELD2);
        this.setParentEntitySetName(ZCollections.WO_COLLECTION);
        this.setParentForeignKeyFieldName(ZConfigManager.WO_KEY_FIELD);
        this.setParentKeyFieldName(ZConfigManager.WO_KEY_FIELD);
    }

    public boolean isInitialized() {
        return (!TextUtils.isEmpty(this.WorkOrderNum) && !TextUtils.isEmpty(this.PlannofOpera));
    }

    public ResponseObject update(ODataEntity entity) {
        ResponseObject result = null;
        PRT prt;
        try {

        } catch (Exception e) {
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);

        }
        return result;
    }

    public String getTruncated(String originalNumber) {
        String truncatedNumberStr = originalNumber;
        try {
            int truncatedNumber = Integer.parseInt(originalNumber);
            return truncatedNumber + "";
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return truncatedNumberStr;
    }
}