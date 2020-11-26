package com.ods.myjobcard_library.entities.Entity;

import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;

/**
 * Created by Anup Rathi on 07-04-2016.
 */
public class OprEntity {
    private ODataEntity entityOpr = null;
    private ArrayList<ODataEntity> entityPRTs = null;

    public OprEntity() {
    }

    public OprEntity(ODataEntity entityOpr) {
        this.entityOpr = entityOpr;
    }

    public OprEntity(ODataEntity entityOpr, ArrayList<ODataEntity> entityPRTs) {
        this.entityOpr = entityOpr;
        this.entityPRTs = entityPRTs;
    }

    public ODataEntity getEntityOpr() {
        return entityOpr;
    }

    public void setEntityOpr(ODataEntity entityOpr) {
        this.entityOpr = entityOpr;
    }

    public ArrayList<ODataEntity> getEntityPRTs() {
        return entityPRTs;
    }

    public void setEntityPRTs(ArrayList<ODataEntity> entityPRTs) {
        this.entityPRTs = entityPRTs;
    }
}
