package com.ods.myjobcard_library.entities.Entity;

import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataProperty;

import java.util.ArrayList;

/**
 * Created by Anup Rathi on 07-04-2016.
 */
public class WOEntity {
    private ODataEntity entityWO = null;
    private ArrayList<OprEntity> entityOprs = null;

    public WOEntity() {
    }

    public WOEntity(ODataEntity entityWO) {
        this.entityWO = entityWO;
    }

    public WOEntity(ODataEntity entityWO, ArrayList<OprEntity> entityOprs) {
        this.entityWO = entityWO;
        this.entityOprs = entityOprs;
    }

    public ODataEntity getEntityWO() {
        return entityWO;
    }

    public ODataEntity convertEntity(ODataEntity entity){
        for (ODataProperty property : entityWO.getProperties().values()){
            entity.getProperties().put(property.getName(), property);
        }
        return entity;
    }

    public void setEntityWO(ODataEntity entityWO) {
        this.entityWO = entityWO;
    }

    public ArrayList<OprEntity> getEntityOprs() {
        return entityOprs;
    }

    public void setEntityOprs(ArrayList<OprEntity> entityOprs) {
        this.entityOprs = entityOprs;
    }
}
