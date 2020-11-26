package com.ods.myjobcard_library.entities;

import com.ods.myjobcard_library.ZCollections;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.sap.smp.client.odata.ODataEntity;

public class User extends BaseEntity {

    private int RecordID;
    private String SystemID;
    private String Name;
    private String CurrentPinCode;
    private String PinCode;

    public User() {
        initializeEntityProperties();
    }

    public User(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.USER_COLLECTION);
        this.setEntityType("WebApplication5.Model.User");
        this.addKeyFieldNames("SystemID");
    }

    public String getSystemID() {
        return SystemID;
    }

    public void setSystemID(String systemID) {
        SystemID = systemID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCurrentPinCode() {
        return CurrentPinCode;
    }

    public void setCurrentPinCode(String currentPinCode) {
        CurrentPinCode = currentPinCode;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public int getRecordID() {
        return RecordID;
    }

    public void setRecordID(int recordID) {
        RecordID = recordID;
    }

}