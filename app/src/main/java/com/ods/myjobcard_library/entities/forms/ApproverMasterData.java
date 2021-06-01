package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.sap.smp.client.odata.ODataEntity;

public class ApproverMasterData extends ZBaseEntity {

    private String UserID;
    private String UserPersonnelNum;
    private boolean EmailID;
    private boolean Active;
    private String FirstName;
    private String LastName;
    private String Contact;
    private String DepartmentID;
    private String Role;
    private String Plant;

    public ApproverMasterData(ODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserPersonnelNum() {
        return UserPersonnelNum;
    }

    public void setUserPersonnelNum(String userPersonnelNum) {
        UserPersonnelNum = userPersonnelNum;
    }

    public boolean isEmailID() {
        return EmailID;
    }

    public void setEmailID(boolean emailID) {
        EmailID = emailID;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.APPROVER_MASTER_DATA_ENTITY_SET);
        this.setEntityType(ZCollections.APPROVER_MASTER_DATA_ENTITY_TYPE);
        this.addKeyFieldNames("UserID");
        this.addKeyFieldNames("UserPersonnelNum");
        this.addKeyFieldNames("EmailID");
    }


}
