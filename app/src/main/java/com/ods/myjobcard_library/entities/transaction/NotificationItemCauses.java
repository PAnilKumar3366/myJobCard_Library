package com.ods.myjobcard_library.entities.transaction;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by lenovo on 27-12-2015.
 */
public class NotificationItemCauses extends ZBaseEntity {

    //header details
    private String Notification;
    private String Item;
    private String Cause;
    private String EnteredBy;
    private GregorianCalendar CreatedOn;
    private String ChangedBy;
    private GregorianCalendar ChangedOn;
    private String CauseText;
    private String CatalogType;
    private String CodeGroup;
    private String CauseCode;
    private String CauseAssembly;
    private BigDecimal Quantity;
    private String UnitofMeasure;
    private String Delete;
    private String WorkOrderNum;
    private String OperAct;
    private String TempID;
    private String SortNumber;
    //constructor
    public NotificationItemCauses(String notification, String item, boolean isWONotif) {
        super();
        initializeEntityProperties(isWONotif);
        this.Notification = notification;
        this.Item = item;
    }
    public NotificationItemCauses(ODataEntity entity, boolean isWONotif) {
        initializeEntityProperties(isWONotif);
        create(entity);
    }

    //get methods
    public static ResponseObject getItemCauses(ZAppSettings.FetchLevel fetchLevel, String notifNum, String itemNum, boolean isWONotif) {

        ResponseObject result = null;
        String resPath = "";
        String strEntitySet = isWONotif ? ZCollections.WO_NOTIFICATION_ITEM_CAUSES_COLLECTION : ZCollections.NOTIFICATION_ITEM_CAUSES_COLLECTION;
        String strOrderByURI = "&$orderby=Cause";
        boolean isFirstParam = true;
        try {
            resPath = strEntitySet;
            if (notifNum != null && itemNum != null && (notifNum.length() > 0 || itemNum.length() > 0)) {

                if (notifNum.startsWith(ZCollections.TEMP_ID_PREFIX)) {
                    resPath = strEntitySet + "?$filter=(TempID%20eq%20%27" + notifNum + "%27%20and%20Item%20eq%20%27" + itemNum + "%27)" + strOrderByURI;
                } else {
                    resPath = strEntitySet + "?$filter=(Notification%20eq%20%27" + notifNum + "%27%20and%20Item%20eq%20%27" + itemNum + "%27)" + strOrderByURI;
                }
                isFirstParam = false;

            }
            switch (fetchLevel) {
                case ListSpinner:
                    resPath += isFirstParam ? "?" : "&" + "$select=Cause,CauseText,CodeGroup";
                    break;
                default:
                    break;
            }

            result = DataHelper.getInstance().getEntities(strEntitySet, resPath);
            result = FromEntity((List<ODataEntity>) result.Content(), isWONotif);

        } catch (Exception e) {
            DliteLogger.WriteLog(NotificationItemCauses.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }


//Setter and Getter methods

    private static ResponseObject FromEntity(List<ODataEntity> entities, boolean isWONotif) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<NotificationItemCauses> itemCauses = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    itemCauses.add(new NotificationItemCauses(entity, isWONotif));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", itemCauses);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(NotificationItemCauses.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void initializeEntityProperties(boolean isWONotif) {
        if (!isWONotif) {
            this.setEntitySetName(ZCollections.NOTIFICATION_ITEM_CAUSES_COLLECTION);
            this.setEntityType(ZCollections.NOTIFICATION_ITEM_CAUSES_ENTITY_TYPE);
            this.setParentEntitySetName(ZCollections.NOTIFICATION_COLLECTION);
        } else {
            this.setEntitySetName(ZCollections.WO_NOTIFICATION_ITEM_CAUSES_COLLECTION);
            this.setEntityType(ZCollections.WO_NOTIFICATION_ITEM_CAUSES_ENTITY_TYPE);
            this.setParentEntitySetName(ZCollections.WO_NOTIFICATION_COLLECTION);
        }
        this.addKeyFieldNames(ZConfigManager.NOTIFICATION_KEY_FIELD);
        this.addKeyFieldNames(ZConfigManager.NOTIFICATION_ITEM_KEY_FIELD);
        this.addKeyFieldNames("Cause");
        this.setParentForeignKeyFieldName(ZConfigManager.NOTIFICATION_KEY_FIELD);
        this.setParentKeyFieldName(ZConfigManager.NOTIFICATION_KEY_FIELD);
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

    public String getCauseText() {
        return CauseText;
    }

    public void setCauseText(String causeText) {
        CauseText = causeText;
    }

    public String getCodeGroup() {
        return CodeGroup;
    }

    public void setCodeGroup(String codeGroup) {
        CodeGroup = codeGroup;
    }

    public String getCauseCode() {
        return CauseCode;
    }

    public void setCauseCode(String causeCode) {
        CauseCode = causeCode;
    }

    public String getCatalogType() {
        return CatalogType;
    }

    public void setCatalogType(String catalogType) {
        CatalogType = catalogType;
    }

    public String getCauseAssembly() {
        return CauseAssembly;
    }

    public void setCauseAssembly(String assembly) {
        CauseAssembly = assembly;
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

    public BigDecimal getQuantity() {
        return Quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        Quantity = quantity;
    }

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

    public String getCause() {
        return Cause;
    }

    public void setCause(String cause) {
        Cause = cause;
    }

    public String getUnitofMeasure() {
        return UnitofMeasure;
    }

    public void setUnitofMeasure(String unitofMeasure) {
        UnitofMeasure = unitofMeasure;
    }

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
    }

//End of Setter and Getter methods

    public String getDisplayableCauseNumber() {
        if (TempID != null && !TempID.isEmpty())
            return ZConfigManager.LOCAL_IDENTIFIER + Cause;
        return getTruncated(Cause);
    }

    public String getTruncated(String originalNumber) {

        int truncatedNumber;
        truncatedNumber = Integer.parseInt(originalNumber);

        return truncatedNumber + "";
    }

}
