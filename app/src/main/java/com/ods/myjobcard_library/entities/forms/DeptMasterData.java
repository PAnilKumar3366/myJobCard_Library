package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.sap.smp.client.odata.ODataEntity;

public class DeptMasterData extends ZBaseEntity {

    private String DepartmentID;
    private String DepartmentName;
    private boolean Active;

    public DeptMasterData(ODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.DEPT_MASTER_DATA_ENTITY_SET);
        this.setEntityType(ZCollections.DEPT_MASTER_DATA_ENTITY_TYPE);
        this.addKeyFieldNames("DepartmentID");
        this.addKeyFieldNames("DepartmentName");
    }

}
