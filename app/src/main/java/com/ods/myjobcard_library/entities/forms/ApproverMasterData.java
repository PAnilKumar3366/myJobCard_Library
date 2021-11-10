package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.viewmodels.DeptHelper;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.sap.smp.client.odata.ODataEntity;

public class ApproverMasterData extends ZBaseEntity {

    private String UserSystemID;
    private String PersonnelNum;
    private String EmailID;
    private boolean Active;
    private String FirstName;
    private String LastName;
    private String Contact;
    private String DepartmentID;
    private String DepartmentName;
    private String ApproverRole;
    private String ApproverLevel;
    private String Plant;

    private String WorkCenter;

    public String getWorkCenter() {
        return WorkCenter;
    }

    public void setWorkCenter(String workCenter) {
        WorkCenter = workCenter;
    }


    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getApproverLevel() {
        return ApproverLevel;
    }

    public ApproverMasterData(String UserId, String FirstName, String LastName, String Department) {
        this.UserSystemID = UserId;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.DepartmentID = Department;
    }

    public ApproverMasterData(ODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    public ApproverMasterData(ZODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    public void setApproverLevel(String approverLevel) {
        ApproverLevel = approverLevel;
    }

    public String getUserSystemID() {
        return UserSystemID;
    }

    public void setUserSystemID(String userSystemID) {
        UserSystemID = userSystemID;
    }

    public String getPersonnelNum() {
        return PersonnelNum;
    }

    public void setPersonnelNum(String personnelNum) {
        PersonnelNum = personnelNum;
    }

    public String isEmailID() {
        return EmailID;
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

    public String getApproverRole() {
        return ApproverRole;
    }

    public void setApproverRole(String approverRole) {
        ApproverRole = approverRole;
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
        this.addKeyFieldNames("UserSystemID");
        this.addKeyFieldNames("PersonnelNum");
        this.addKeyFieldNames("EmailID");
    }

    public String getUserName() {
        return this.FirstName + this.LastName;
    }

    public String getDeptName() {
        DeptHelper deptHelper = new DeptHelper();
        DeptMasterData deptMasterData = deptHelper.getDeptMasterData(this.DepartmentID);
        if (deptMasterData != null && deptMasterData.getDepartmentName() != null)
            return deptMasterData.getDepartmentName();
        return "";
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }
}
