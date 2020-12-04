package com.ods.myjobcard_library.entities;

import com.ods.myjobcard_library.ZCollections;
import com.sap.smp.client.odata.ODataEntity;

import java.sql.Time;
import java.util.GregorianCalendar;

/**
 * Created by MY HOME on 9/26/2017.
 */
public class InstallEquipment extends ZBaseEntity {

    private String Equipment;
    private String Item;
    private String FunctionalLocation;
    private String SuperiorEquipment;
    private String Position;
    private GregorianCalendar InstallDate;
    private Time InstallTime;

    public InstallEquipment() {
        initializingEntityProperties();
    }
    public InstallEquipment(ODataEntity entity) {
        initializingEntityProperties();
        create(entity);
    }

    private void initializingEntityProperties() {
        this.setEntityType(ZCollections.INSTALL_EQUIPMENT_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.INSTALL_EQUIPMENT_COLLECTION);
        this.setEntityResourcePath(ZCollections.INSTALL_EQUIPMENT_COLLECTION);
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getFunctionalLocation() {
        return FunctionalLocation;
    }

    public void setFunctionalLocation(String functionalLocation) {
        FunctionalLocation = functionalLocation;
    }

    public String getSuperiorEquipment() {
        return SuperiorEquipment;
    }

    public void setSuperiorEquipment(String superiorEquipment) {
        SuperiorEquipment = superiorEquipment;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public GregorianCalendar getInstallDate() {
        return InstallDate;
    }

    public void setInstallDate(GregorianCalendar installDate) {
        InstallDate = installDate;
    }

    public Time getInstallTime() {
        return InstallTime;
    }

    public void setInstallTime(Time installTime) {
        InstallTime = installTime;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }
}
