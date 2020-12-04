package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.util.Date;

public class WoBomHeader extends ZBaseEntity {
    private String BOMCategory;
    private String BOM;
    private String EnteredBy;
    private String Alternative;
    private String Counter;
    private Date ValidFrom;
    private String BaseUnit;
    private BigDecimal BaseQuantity;
    private String BOMStatus;
    private String BOMGroup;
    private String BOMText;

    public WoBomHeader(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity);
    }

    public WoBomHeader(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    private void initializeEntityProperties() {
        this.setEntityType(ZCollections.WO_BOMITEM_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.WO_BOMITEM_ENTITY_SET);
        this.addKeyFieldNames("BOMCategory");
        this.addKeyFieldNames("BOM");
    }

}
