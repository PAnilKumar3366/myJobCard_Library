package com.ods.myjobcard_library.entities.transaction;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.entities.odata.ZODataEntity;

public class WOHistoryOpLongText extends ZBaseEntity {

    private String OnlineSearch;
    private String TextName;
    private String WorkOrderNum;
    private String OperationNum;
    private String ComponentItem;
    private String TextObject;
    private String TagColumn;
    private String TextLine;
    private String EnteredBy;
    private String TextID;
    private String TempID;
    private String PlannofOpera;
    private String Counter;
    private String SubOperation;

    public WOHistoryOpLongText(ZODataEntity entity) {
        create(entity);
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.WO_HISTORY_OPERATION_LONG_TEXT_ENTITY_SET);
        this.setEntityType(ZCollections.WO_HISTORY_OPERATION_LONG_TEXT_ENTITY_TYPE);
        this.addKeyFieldNames("TextName");
        this.addKeyFieldNames("Item");
        this.addKeyFieldNames("Counter");
    }

    public String getOnlineSearch() {
        return OnlineSearch;
    }

    public void setOnlineSearch(String onlineSearch) {
        OnlineSearch = onlineSearch;
    }

    public String getTextName() {
        return TextName;
    }

    public void setTextName(String textName) {
        TextName = textName;
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

    public String getComponentItem() {
        return ComponentItem;
    }

    public void setComponentItem(String componentItem) {
        ComponentItem = componentItem;
    }

    public String getTextObject() {
        return TextObject;
    }

    public void setTextObject(String textObject) {
        TextObject = textObject;
    }

    public String getTagColumn() {
        return TagColumn;
    }

    public void setTagColumn(String tagColumn) {
        TagColumn = tagColumn;
    }

    public String getTextLine() {
        return TextLine;
    }

    public void setTextLine(String textLine) {
        TextLine = textLine;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getTextID() {
        return TextID;
    }

    public void setTextID(String textID) {
        TextID = textID;
    }

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
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

    public String getSubOperation() {
        return SubOperation;
    }

    public void setSubOperation(String subOperation) {
        SubOperation = subOperation;
    }
}
