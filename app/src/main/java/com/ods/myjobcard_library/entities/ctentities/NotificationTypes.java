package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 16-05-2016.
 */
public class NotificationTypes extends ZBaseEntity {

    private String NotifictnType;
    private String NotifCat;
    private String CatalogProfile;
    private String OrderType;
    private String PriorityType;
    private String NotificationTypeText;
    private String BusinessProcess;

    private String StatusProfile;

    public NotificationTypes(ODataEntity entity) {
        create(entity);
    }

    //get methods
    public static ResponseObject getNotificationTypes(String orderType, ZAppSettings.FetchLevel fetchLevel) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String resPath = ZCollections.NOTIFICATION_TYPE_COLLECTION;
        String strOrderByURI = "$orderby=";
        String strOrderByCriteria = "NotifictnType";
        try {
            strOrderByURI += strOrderByCriteria;
            if (orderType != null && !orderType.isEmpty())
                resPath += "?$filter=(OrderType eq '" + orderType + "')&" + strOrderByURI;
            else
                resPath += "?" + strOrderByURI;

            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.NOTIFICATION_TYPE_COLLECTION, resPath);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                switch (fetchLevel) {
                    case ListSpinner:
                        ArrayList<NotificationTypes> types = (ArrayList<NotificationTypes>) result.Content();
                        if (types != null && !types.isEmpty()) {
                            ArrayList<SpinnerItem> spinnerItems = new ArrayList<>();
                            SpinnerItem item;
                            for (NotificationTypes type : types) {
                                item = new SpinnerItem();
                                item.setId(type.getNotifictnType());
                                item.setDescription(type.getNotificationTypeText());
                                spinnerItems.add(item);
                            }
                            result.setContent(spinnerItems);
                        }
                        break;
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(NotificationTypes.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public static ResponseObject getNotificationType(String notificationType) {

        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            String resPath = ZCollections.NOTIFICATION_TYPE_COLLECTION;
            if (notificationType != null)
                resPath += "?$filter=(NotifictnType eq '" + notificationType + "')";
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.NOTIFICATION_TYPE_COLLECTION, resPath);
            if (result != null && !result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrderType.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<NotificationTypes> notificationTypes = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    notificationTypes.add(new NotificationTypes(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", notificationTypes);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(NotificationTypes.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getNotifictnType() {
        return NotifictnType;
    }

    public void setNotifictnType(String notifictnType) {
        NotifictnType = notifictnType;
    }

    public String getNotifCat() {
        return NotifCat;
    }

    public void setNotifCat(String notifCat) {
        NotifCat = notifCat;
    }

    public String getCatalogProfile() {
        return CatalogProfile;
    }

    public void setCatalogProfile(String catalogProfile) {
        CatalogProfile = catalogProfile;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getPriorityType() {
        return PriorityType;
    }

    public void setPriorityType(String priorityType) {
        PriorityType = priorityType;
    }

    public String getNotificationTypeText() {
        return NotificationTypeText;
    }

    public void setNotificationTypeText(String notificationTypeText) {
        NotificationTypeText = notificationTypeText;
    }

    public String getBusinessProcess() {
        return BusinessProcess;
    }

    public void setBusinessProcess(String businessProcess) {
        BusinessProcess = businessProcess;
    }

    public String getStatusProfile() {
        return StatusProfile;
    }

    public void setStatusProfile(String statusProfile) {
        StatusProfile = statusProfile;
    }
}
