package com.ods.myjobcard_library.entities.qmentities;

import android.os.Parcel;
import android.os.Parcelable;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 02-04-2020.
 */

public class InspectionOperation extends BaseEntity implements Parcelable {
    public static final Creator<InspectionOperation> CREATOR = new Creator<InspectionOperation>() {
        @Override
        public InspectionOperation createFromParcel(Parcel in) {
            return new InspectionOperation(in);
        }

        @Override
        public InspectionOperation[] newArray(int size) {
            return new InspectionOperation[size];
        }
    };
    private String EnteredBy;
    private String RoutingNumber;
    private String NodeNumber;
    private String InspectionLot;
    private String TaskListType;
    private String Group;
    private String GroupCounter;
    private String Sequence;
    private String Operation;
    private String Plant;
    private String SupOperNode;
    private String Controlkey;
    private String ShortText;
    private String Counter;
    private String Nodes;
    private String Confirmation;
    private String ObjectNumber;
    private BigDecimal Denominator;
    private BigDecimal Numerator;
    private String InspPtComplete;
    private BigDecimal BaseQuantity;
    private String UoM;
    private BigDecimal OperationQty;
    private String ControlKey;

    public InspectionOperation(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public InspectionOperation(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity);
    }

    protected InspectionOperation(Parcel parcelable) {
        EnteredBy = parcelable.readString();
        RoutingNumber = parcelable.readString();
        NodeNumber = parcelable.readString();
        InspectionLot = parcelable.readString();
        TaskListType = parcelable.readString();
        Group = parcelable.readString();
        GroupCounter = parcelable.readString();
        Sequence = parcelable.readString();
        Operation = parcelable.readString();
        Plant = parcelable.readString();
        SupOperNode = parcelable.readString();
        Controlkey = parcelable.readString();
        ShortText = parcelable.readString();
        Counter = parcelable.readString();
        Nodes = parcelable.readString();
        Confirmation = parcelable.readString();
        ObjectNumber = parcelable.readString();
        Denominator = BigDecimal.valueOf(parcelable.readDouble());
        Numerator = BigDecimal.valueOf(parcelable.readDouble());
        InspPtComplete = parcelable.readString();
        BaseQuantity = BigDecimal.valueOf(parcelable.readDouble());
        UoM = parcelable.readString();
        OperationQty = BigDecimal.valueOf(parcelable.readDouble());
    }

    public static ArrayList<InspectionOperation> getInspLotOperations(String inspectionLotNum, String orderNum, String oprNum) {
        ResponseObject result = null;
        ArrayList<InspectionOperation> inspectionLotOperations = null;
        try {
            String entitySetName = ZCollections.WO_INSPECTIONLOT_OPERATION_ENTITY_COLLECTION;
            String resPath = entitySetName;
            if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED || !ZConfigManager.WO_OP_OBJS_DISPLAY.equalsIgnoreCase("x"))
                resPath += "?$filter=InspectionLot eq '" + inspectionLotNum + "'&$orderby=Operation";
            else
                resPath += "?$filter=InspectionLot eq '" + inspectionLotNum + "' and Operation eq '" + oprNum + "'&$orderby=Operation";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities, ZAppSettings.FetchLevel.All);
                inspectionLotOperations = (ArrayList<InspectionOperation>) result.Content();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(InspectionLot.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return inspectionLotOperations;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<InspectionOperation> inspOperations = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    inspOperations.add(new InspectionOperation(entity, ZAppSettings.FetchLevel.All));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", inspOperations);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(InspectionLot.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void initializeEntityProperties() {
        this.setEntityType(ZCollections.WO_INSPECTIONLOT_OPERATION_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.WO_INSPECTIONLOT_OPERATION_ENTITY_COLLECTION);
        this.addKeyFieldNames("InspectionLot");
        this.addKeyFieldNames("Operation");
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getRoutingNumber() {
        return RoutingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        RoutingNumber = routingNumber;
    }

    public String getNodeNumber() {
        return NodeNumber;
    }

    public void setNodeNumber(String nodeNumber) {
        NodeNumber = nodeNumber;
    }

    public String getInspectionLot() {
        return InspectionLot;
    }

    public void setInspectionLot(String inspectionLot) {
        InspectionLot = inspectionLot;
    }

    public String getTaskListType() {
        return TaskListType;
    }

    public void setTaskListType(String taskListType) {
        TaskListType = taskListType;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }

    public String getGroupCounter() {
        return GroupCounter;
    }

    public void setGroupCounter(String groupCounter) {
        GroupCounter = groupCounter;
    }

    public String getSequence() {
        return Sequence;
    }

    public void setSequence(String sequence) {
        Sequence = sequence;
    }

    public String getOperation() {
        return Operation;
    }

    public void setOperation(String operation) {
        Operation = operation;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getSupOperNode() {
        return SupOperNode;
    }

    public void setSupOperNode(String supOperNode) {
        SupOperNode = supOperNode;
    }

    public String getControlkey() {
        return Controlkey;
    }

    public void setControlkey(String controlkey) {
        Controlkey = controlkey;
    }

    public String getShortText() {
        return ShortText;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public String getNodes() {
        return Nodes;
    }

    public void setNodes(String nodes) {
        Nodes = nodes;
    }

    public String getConfirmation() {
        return Confirmation;
    }

    public void setConfirmation(String confirmation) {
        Confirmation = confirmation;
    }

    public String getObjectNumber() {
        return ObjectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        ObjectNumber = objectNumber;
    }

    public BigDecimal getDenominator() {
        return Denominator;
    }

    public void setDenominator(BigDecimal denominator) {
        Denominator = denominator;
    }

    public BigDecimal getNumerator() {
        return Numerator;
    }

    public void setNumerator(BigDecimal numerator) {
        Numerator = numerator;
    }

    public String getInspPtComplete() {
        return InspPtComplete;
    }

    public void setInspPtComplete(String inspPtComplete) {
        InspPtComplete = inspPtComplete;
    }

    public BigDecimal getBaseQuantity() {
        return BaseQuantity;
    }

    public void setBaseQuantity(BigDecimal baseQuantity) {
        BaseQuantity = baseQuantity;
    }

    public String getUoM() {
        return UoM;
    }

    public void setUoM(String uoM) {
        UoM = uoM;
    }

    public BigDecimal getOperationQty() {
        return OperationQty;
    }

    public void setOperationQty(BigDecimal operationQty) {
        OperationQty = operationQty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(RoutingNumber);
        parcel.writeString(NodeNumber);
        parcel.writeString(InspectionLot);
        parcel.writeString(TaskListType);
        parcel.writeString(Group);
        parcel.writeString(GroupCounter);
        parcel.writeString(Sequence);
        parcel.writeString(Operation);
        parcel.writeString(Plant);
        parcel.writeString(SupOperNode);
        parcel.writeString(Controlkey);
        parcel.writeString(ShortText);
        parcel.writeString(Counter);
        parcel.writeString(Nodes);
        parcel.writeString(Confirmation);
        parcel.writeString(ObjectNumber);
        parcel.writeString(InspPtComplete);
        parcel.writeString(UoM);
    }

    public String getControlKey() {
        return ControlKey;
    }

    public void setControlKey(String controlKey) {
        ControlKey = controlKey;
    }
}
