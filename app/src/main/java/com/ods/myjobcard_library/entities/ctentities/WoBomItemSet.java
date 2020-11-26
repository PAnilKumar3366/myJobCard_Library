package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WoBomItemSet extends BaseEntity {
    private String BOMCategory;
    private String BOM;
    private String ItemNode;
    private String ComponentUnit;
    private BigDecimal Quantity;
    private String MaterialDescription;
    private String EnteredBy;
    private String Counter;
    private String Component;
    private String InheritedItemNode;
    private String ItemCategory;
    private String ItemGroup;
    private String ItemId;


    public WoBomItemSet(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public WoBomItemSet(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity);
    }

    public static ResponseObject getBomItemList(String BOMCategory, String BOM) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String entitySetName = ZCollections.WO_BOMITEM_ENTITY_SET;
        String filterQuery = "?$filter=";
        String resPath = "";
        try {
            if (BOMCategory != null && !BOMCategory.isEmpty())
                resPath += "BOMCategory eq '" + BOMCategory + "'";
            if (BOM != null && !BOM.isEmpty())
                resPath += (!resPath.isEmpty() ? " and " : "") + "BOM eq '" + BOM + "'";
            resPath = entitySetName + filterQuery + resPath;
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.WO_BOMITEM_ENTITY_SET, resPath);
            result = FromEntity((List<ODataEntity>) result.Content());
        } catch (Exception e) {
            DliteLogger.WriteLog(MaterialStorageLocation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<WoBomItemSet> storageLocations = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    storageLocations.add(new WoBomItemSet(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", storageLocations);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(MaterialStorageLocation.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ArrayList<WoBomItemSet> searchBomItems(String searchText, String searchOption, ArrayList<WoBomItemSet> bomitems) {
        ArrayList<WoBomItemSet> searchItemList = new ArrayList<>();
        if (searchOption.equals(ZCollections.SEARCH_OPTION_ID)) {
            for (WoBomItemSet bomItem : bomitems) {
                if (bomItem.getComponent().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchItemList.add(bomItem);
                }
            }
        } else if (searchOption.equals(ZCollections.SEARCH_OPTION_DESCRIPTION)) {
            for (WoBomItemSet bomItem : bomitems) {
                if (bomItem.getMaterialDescription().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchItemList.add(bomItem);
                }
            }
        } else {
            for (WoBomItemSet bomItem : bomitems) {
                if (bomItem.getComponent().trim().toLowerCase().contains(searchText.toLowerCase()) || bomItem.getMaterialDescription().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchItemList.add(bomItem);
                }
            }
        }
        return searchItemList;
    }

    public String getBOMCategory() {
        return BOMCategory;
    }

    public void setBOMCategory(String BOMCategory) {
        this.BOMCategory = BOMCategory;
    }

    public String getBOM() {
        return BOM;
    }

    public void setBOM(String BOM) {
        this.BOM = BOM;
    }

    public String getItemNode() {
        return ItemNode;
    }

    public void setItemNode(String itemNode) {
        ItemNode = itemNode;
    }

    public String getComponentUnit() {
        return ComponentUnit;
    }

    public void setComponentUnit(String componentUnit) {
        ComponentUnit = componentUnit;
    }

    public BigDecimal getQuantity() {
        return Quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        Quantity = quantity;
    }

    public String getMaterialDescription() {
        return MaterialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        MaterialDescription = materialDescription;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public String getComponent() {
        return Component;
    }

    public void setComponent(String component) {
        Component = component;
    }

    public String getInheritedItemNode() {
        return InheritedItemNode;
    }

    public void setInheritedItemNode(String inheritedItemNode) {
        InheritedItemNode = inheritedItemNode;
    }

    public String getItemCategory() {
        return ItemCategory;
    }

    public void setItemCategory(String itemCategory) {
        ItemCategory = itemCategory;
    }

    public String getItemGroup() {
        return ItemGroup;
    }

    public void setItemGroup(String itemGroup) {
        ItemGroup = itemGroup;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    private void initializeEntityProperties() {
        this.setEntityType(ZCollections.WO_BOMITEM_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.WO_BOMITEM_ENTITY_SET);
        this.addKeyFieldNames("BOMCategory");
        this.addKeyFieldNames("BOM");
        this.addKeyFieldNames("ItemNode");
    }
}
