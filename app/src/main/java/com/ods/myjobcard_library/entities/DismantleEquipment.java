package com.ods.myjobcard_library.entities;

import com.ods.myjobcard_library.ZCollections;
import com.sap.smp.client.odata.ODataEntity;

/**
 * Created by MY HOME on 9/26/2017.
 */
public class DismantleEquipment extends ZBaseEntity {

    public DismantleEquipment() {
        initializingEntityProperties();
    }

    public DismantleEquipment(ODataEntity entity) {
        initializingEntityProperties();
        create(entity);
    }

    private String Equipment;
    private String Item;
    private String FunctionalLocation;
    private String SuperiorEquipment;

    private void initializingEntityProperties() {
        this.setEntityType(ZCollections.DISMANTLE_EQUIPMENT_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.DISMANTLE_EQUIPMENT_COLLECTION);
        this.setEntityResourcePath(ZCollections.DISMANTLE_EQUIPMENT_COLLECTION);
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

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }
}
