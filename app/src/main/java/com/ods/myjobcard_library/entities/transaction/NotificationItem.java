package com.ods.myjobcard_library.entities.transaction;

import android.text.TextUtils;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.client.odata.v4.EntityValue;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class NotificationItem extends BaseEntity {

    private String Notification;
    private String OnlineSearch;
    private String Item;
    private String EnteredBy;
    private GregorianCalendar CreatedOn;
    private String ChangedBy;
    private GregorianCalendar ChangedOn;
    private String ShortText;
    private String DefectTypes;
    private String DamageCodeGroup;
    private String DamageCode;
    private String Version;
    private String CatalogType;
    private String ObjectPartCode;
    private String ObjectPartCodeText;
    private String Assembly;
    private String InstallPoint;
    private String WorkCenter;
    private String PltforWorkCtr;
    private String Delete;
    private String SortNumber;
    private String Material;
    private String Plant;
    private String CostCenter;
    private BigDecimal Quantity;
    private String Equipment;
    private String FunctionalLoc;
    private String WorkOrderNum;
    private String OperAct;
    private String CodeGroupParts;
    private String CodeGroupPartsText;
    private String TempID;

    public NotificationItem(String notification, String item, boolean isWONotif) {
        super();
        this.Notification = notification;
        this.Item = item;
        initializeEntityProperties(isWONotif);
    }

    public NotificationItem(ODataEntity entityItem, boolean isWONotif) {
        initializeEntityProperties(isWONotif);
        create(entityItem);
    }

    public NotificationItem(EntityValue entityValue) {
        create(entityValue);
    }

    //get methods
    public static ResponseObject getNotifItems(ZAppSettings.FetchLevel fetchLevel, String notifNum, String notifItemNum, boolean isWONotif) {

        ResponseObject result = null;
        String resPath = "";
        String strEntitySet = isWONotif ? ZCollections.WO_NOTIFICATION_ITEMS_COLLECTION : ZCollections.NOTIFICATION_ITEMS_COLLECTION;
        String strOrderByURI = "&$orderby=Item";
        boolean isFirstParam = true;
        try {
            resPath = strEntitySet;
            if (notifNum != null && notifNum.length() > 0) {

                if (notifNum.startsWith(ZCollections.TEMP_ID_PREFIX)) {
                    resPath = strEntitySet + "?$filter=(TempID%20eq%20%27" + notifNum + "%27)" + strOrderByURI;
                } else {
                    resPath = strEntitySet + "?$filter=(Notification%20eq%20%27" + notifNum + "%27)" + strOrderByURI;
                }
                isFirstParam = false;
            }
            switch (fetchLevel) {
                case ListSpinner:
                    resPath += isFirstParam ? "?" : "&" + "$select=Item,ShortText,Codegroup";
                    break;
                case Single:
                    if (notifItemNum != null && notifNum != null) {
                        resPath = strEntitySet + "?$filter=(Notification%20eq%20%27" + notifNum + "%27%20and%20Item%20eq%20%27" + notifItemNum + "%27)" + strOrderByURI;
                    }
                default:
                    break;
            }

            result = DataHelper.getInstance().getEntities(strEntitySet, resPath);
            result = FromEntity((List<ODataEntity>) result.Content(), isWONotif);

        } catch (Exception e) {

            DliteLogger.WriteLog(NotificationItem.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);

        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, boolean isWONotif) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<NotificationItem> items = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    items.add(new NotificationItem(entity, isWONotif));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", items);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(NotificationItem.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getOnlineSearch() {
        return OnlineSearch;
    }

    public void setOnlineSearch(String onlineSearch) {
        OnlineSearch = onlineSearch;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }

    public String getDisplayableNotificationNum() {
        if (getTempID() == null || getTempID().isEmpty())
            return getNotification();
        else
            return getTempID().replace(ZConfigManager.LOCAL_ID, ZConfigManager.LOCAL_IDENTIFIER);
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public GregorianCalendar getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(GregorianCalendar createdOn) {
        CreatedOn = createdOn;
    }

    public String getChangedBy() {
        return ChangedBy;
    }

    public void setChangedBy(String changedBy) {
        ChangedBy = changedBy;
    }

    public GregorianCalendar getChangedOn() {
        return ChangedOn;
    }

    public void setChangedOn(GregorianCalendar changedOn) {
        ChangedOn = changedOn;
    }

    public String getShortText() {
        return ShortText;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }

    public String getDefectTypes() {
        return DefectTypes;
    }

    public void setDefectTypes(String defectTypes) {
        DefectTypes = defectTypes;
    }

    public String getDamageCodeGroup() {
        return DamageCodeGroup;
    }

    public void setDamageCodeGroup(String damageCodeGroup) {
        DamageCodeGroup = damageCodeGroup;
    }

    public String getDamageCode() {
        return DamageCode;
    }

    public void setDamageCode(String damageCode) {
        DamageCode = damageCode;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getCatalogType() {
        return CatalogType;
    }

    public void setCatalogType(String catalogType) {
        CatalogType = catalogType;
    }

    public String getObjectPartCode() {
        return ObjectPartCode;
    }

    public void setObjectPartCode(String objectPartCode) {
        ObjectPartCode = objectPartCode;
    }

    public String getAssembly() {
        return Assembly;
    }

    public void setAssembly(String assembly) {
        Assembly = assembly;
    }

    public String getInstallPoint() {
        return InstallPoint;
    }

    public void setInstallPoint(String installPoint) {
        InstallPoint = installPoint;
    }

    public String getWorkCenter() {
        return WorkCenter;
    }

    public void setWorkCenter(String workCenter) {
        WorkCenter = workCenter;
    }

    public String getPltforWorkCtr() {
        return PltforWorkCtr;
    }

    public void setPltforWorkCtr(String pltforWorkCtr) {
        PltforWorkCtr = pltforWorkCtr;
    }

    public String getDelete() {
        return Delete;
    }

    public void setDelete(String delete) {
        Delete = delete;
    }

    public String getSortNumber() {
        return SortNumber;
    }

    public void setSortNumber(String sortNumber) {
        SortNumber = sortNumber;
    }

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String material) {
        Material = material;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getCostCenter() {
        return CostCenter;
    }

    public void setCostCenter(String costCenter) {
        CostCenter = costCenter;
    }

    public BigDecimal getQuantity() {
        return Quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        Quantity = quantity;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getFunctionalLoc() {
        return FunctionalLoc;
    }

    public void setFunctionalLoc(String functionalLoc) {
        FunctionalLoc = functionalLoc;
    }

    /*public void setItemsCauses(ArrayList<NotificationItemCauses> ItemsCauses) {
        this.itemCauses = ItemsCauses;
    }*/

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getOperAct() {
        return OperAct;
    }

    public void setOperAct(String operAct) {
        OperAct = operAct;
    }

    public String getCodeGroupParts() {
        return CodeGroupParts;
    }

    public void setCodeGroupParts(String codeGroupParts) {
        CodeGroupParts = codeGroupParts;
    }

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
    }

    public String getCodeGroupPartsText() {
        return CodeGroupPartsText;
    }

    //End of Setters and Getters Method

    public void setCodeGroupPartsText(String codeGroupPartsText) {
        CodeGroupPartsText = codeGroupPartsText;
    }

    public String getObjectPartCodeText() {
        return ObjectPartCodeText;
    }

    public void setObjectPartCodeText(String objectPartCodeText) {
        ObjectPartCodeText = objectPartCodeText;
    }

    /*public void AddItemCause(NotificationItemCauses ItemCauses) {
        itemCauses.add(ItemCauses);
    }
    public void AddTask(NotificationTask task) {
        tasks.add(task);
    }
    public void AddActivity(NotificationActivity activity) {
        activities.add(activity);
    }*/

    public String getDisplayableItemNumber() {
        if (TempID != null && !TempID.isEmpty())
            return ZConfigManager.LOCAL_IDENTIFIER + Item;
        return Item;
    }

    private void initializeEntityProperties(boolean isWONotif) {
        if (!isWONotif) {
            this.setEntitySetName(ZCollections.NOTIFICATION_ITEMS_COLLECTION);
            this.setEntityType(ZCollections.NOTIFICATION_ITEMS_ENTITY_TYPE);
            this.setParentEntitySetName(ZCollections.NOTIFICATION_COLLECTION);
        } else {
            this.setEntitySetName(ZCollections.WO_NOTIFICATION_ITEMS_COLLECTION);
            this.setEntityType(ZCollections.WO_NOTIFICATION_ITEMS_ENTITY_TYPE);
            this.setParentEntitySetName(ZCollections.WO_NOTIFICATION_COLLECTION);
        }
        this.addKeyFieldNames(ZConfigManager.NOTIFICATION_KEY_FIELD);
        this.addKeyFieldNames(ZConfigManager.NOTIFICATION_ITEM_KEY_FIELD);
        this.setParentForeignKeyFieldName(ZConfigManager.NOTIFICATION_KEY_FIELD);
        this.setParentKeyFieldName(ZConfigManager.NOTIFICATION_KEY_FIELD);
    }

    public boolean isInitialized() {
        return (!TextUtils.isEmpty(this.Notification));
    }

    public String getTruncated(String originalNumber) {
        String truncatedStr = originalNumber;
        try {
            int truncatedNumber;
            truncatedNumber = Integer.parseInt(originalNumber);

            truncatedStr = truncatedNumber + "";
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Warning, e.getMessage());
        }
        return truncatedStr;
    }


}