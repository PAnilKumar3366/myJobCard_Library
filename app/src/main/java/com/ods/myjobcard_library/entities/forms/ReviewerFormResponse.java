package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.entities.odata.ZODataEntity;

import java.util.GregorianCalendar;

public class ReviewerFormResponse extends ZBaseEntity {

    private String InstanceID;
    private String FormID;
    private String Version;
    private String WoNum;
    private String OperationNum;
    private String TaskListType;
    private String Group;
    private String GroupCounter;
    private String InternalCounter;
    private String Equipment;
    private String FunctionLocation;
    private String ResponseData;
    private GregorianCalendar CreatedOn;
    private String CreatedBy;
    private GregorianCalendar ModifiedOn;
    private String ModifiedBy;
    private String IsDraft;
    private String NonObjType;
    private String OrderType;
    private String Counter;

    public ReviewerFormResponse() {
        initializingEntityProperties();
    }

    public ReviewerFormResponse(ZODataEntity entity) {
        initializingEntityProperties();
        create(entity);
    }


    private void initializingEntityProperties() {
        this.setEntityType(ZCollections.REVIEWER_FORM_RESPONSE_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.REVIEWER_FORM_RESPONSE_ENTITY_SET);
        this.addKeyFieldNames("InstanceID");
    }

    public String getInstanceID() {
        return InstanceID;
    }

    public void setInstanceID(String instanceID) {
        InstanceID = instanceID;
    }

    public String getFormID() {
        return FormID;
    }

    public void setFormID(String formID) {
        FormID = formID;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getWoNum() {
        return WoNum;
    }

    public void setWoNum(String woNum) {
        WoNum = woNum;
    }

    public String getOperationNum() {
        return OperationNum;
    }

    public void setOperationNum(String operationNum) {
        OperationNum = operationNum;
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

    public String getInternalCounter() {
        return InternalCounter;
    }

    public void setInternalCounter(String internalCounter) {
        InternalCounter = internalCounter;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getFunctionLocation() {
        return FunctionLocation;
    }

    public void setFunctionLocation(String functionLocation) {
        FunctionLocation = functionLocation;
    }

    public String getResponseData() {
        return ResponseData;
    }

    public void setResponseData(String responseData) {
        ResponseData = responseData;
    }

    public GregorianCalendar getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(GregorianCalendar createdOn) {
        CreatedOn = createdOn;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public GregorianCalendar getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(GregorianCalendar modifiedOn) {
        ModifiedOn = modifiedOn;
    }

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public String getIsDraft() {
        return IsDraft;
    }

    public void setIsDraft(String isDraft) {
        IsDraft = isDraft;
    }

    public String getNonObjType() {
        return NonObjType;
    }

    public void setNonObjType(String nonObjType) {
        NonObjType = nonObjType;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }
}
