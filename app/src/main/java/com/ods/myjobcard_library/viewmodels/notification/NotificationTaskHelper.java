package com.ods.myjobcard_library.viewmodels.notification;

import android.location.Location;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.appsettings.StatusCategory;
import com.ods.myjobcard_library.entities.ctentities.WorkOrderStatus;
import com.ods.myjobcard_library.entities.transaction.NotificationActivity;
import com.ods.myjobcard_library.entities.transaction.NotificationTask;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class contains all helper methods for deriving the Notification task status management
 */
public class NotificationTaskHelper {
    private StatusCategory statusDetailsObj;
    private ArrayList<StatusCategory> validStatuses;
    private String objType,currentStatus;
    boolean isWONotif;
    private NotificationTask notificationTaskObj;
    private ArrayList<ZODataEntity> zoDatataskEntities;


    protected NotificationTaskHelper(){

    }
   /* protected NotificationTaskHelper(NotificationTask notificationTask,StatusCategory statusDetail,boolean isWONotif) {
        notificationTaskObj=notificationTask;
        statusDetailsObj=statusDetail;
        this.isWONotif=isWONotif;

    }*/
/*    protected NotificationTaskHelper(String currentStatus,String objType){
       this.currentStatus=currentStatus;
       this.objType=objType;
    }*/

    /**
     *fetching the status information by filtering its current status and notification object type
     * @return
     * @param currentStatus
     * @param objType
     */
    protected StatusCategory deriveNotificationTaskStatus(String currentStatus, String objType){
        try {
            StatusCategory statusDetail = StatusCategory.getStatusDetails(currentStatus, objType, ZConfigManager.Fetch_Object_Type.NotificationTasks);
            if (statusDetail != null) {
                this.statusDetailsObj = statusDetail;
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return statusDetailsObj;
    }

    /**
     * getting the allowed statuses by passing its current status
     * @return
     */
    protected ArrayList<StatusCategory> getAllowedStatus(){
        ResponseObject result = null;
        String strResPath;
        ArrayList<String> allowedStatusList;
        int intNumOfActiveWO = 0;
        validStatuses = new ArrayList<>();
        try {
            if (statusDetailsObj != null) {
                result = WorkOrderStatus.getWorkOrderAllowedStatus(statusDetailsObj,statusDetailsObj.getObjectType());
                if (result != null && !result.isError()) {
                    allowedStatusList = (ArrayList<String>) result.Content();
                    for (String allowedStatus : allowedStatusList) {
                        StatusCategory status = StatusCategory.getStatusDetails(allowedStatus, objType, ZConfigManager.Fetch_Object_Type.NotificationTasks);
                        if (status != null)
                            addValidStatus(status);
                    }
                } else {
                    DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, "error in getting allowed status for Notification Task: " + notificationTaskObj.getNotification() + ". Message: " + result.getMessage());
                }
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, "error in getting allowed status for Notification Task: " + notificationTaskObj.getNotification() + ". Message: " + e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return validStatuses;
    }

    /**
     * set the all valid statuses to attaylist of the StatusCategory
     * @param status
     */
    private void addValidStatus(StatusCategory status){
        try {
            if (validStatuses == null)
                validStatuses = new ArrayList<>();
            validStatuses.add(status);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**
     * saving the updated status
     * @param autoFlush
     * @return
     */
    protected boolean updateStatus(NotificationTask notificationTask,StatusCategory statusDetailsObj,boolean autoFlush){
        boolean result = false;
        try {
            notificationTaskObj=notificationTask;
            notificationTaskObj.setStatusFlag(ZConfigManager.STATUS_SET_FLAG);
            notificationTaskObj.setMobileStatus(statusDetailsObj.getStatusCode());
            notificationTaskObj.setUserStatus(statusDetailsObj.getStatusCode());
            notificationTaskObj.setMobileObjectType(statusDetailsObj.getObjectType());
            notificationTaskObj.setMode(ZAppSettings.EntityMode.Update);
            ResponseObject response = notificationTaskObj.SaveToStore(autoFlush);
            result = response != null && !response.isError();
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }
    /*public StatusCategory getStatusDetail() {
        if (statusDetailsObj == null)
            statusDetailsObj = new StatusCategory();
        return statusDetailsObj;
    }

    public boolean isStatusAllowed(StatusCategory allowedStatus) {
        boolean result = false;
        try {
            for (StatusCategory status : validStatuses) {
                if (status.getStatusCode().equalsIgnoreCase(allowedStatus.getStatusCode())) {
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }*/

    protected int getTotalNumAttachments(boolean isWONotif,String objKey) {
        int intAttachmentsCount = 0;
        ResponseObject responseObject = null;
        String entitySetName = isWONotif ? ZCollections.WO_NO_ATTACHMENT_COLLECTION : ZCollections.NO_ATTACHMENT_COLLECTION;
        String strResPath;
        Object rawData = null;
        //String objKey=notificationTaskObj.getNotification()+notificationTaskObj.getItem()+notificationTaskObj.getTask();
        try {
            intAttachmentsCount = getTotalNumUploadedAttachments(objKey);
            strResPath = entitySetName + "/$count?$filter=(ObjectKey eq '" + objKey+ "')";
            responseObject = DataHelper.getInstance().getEntities(entitySetName, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intAttachmentsCount += Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(com.ods.myjobcard_library.entities.transaction.Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intAttachmentsCount;
    }

    protected int getTotalNumUploadedAttachments(String objKey) {
        int intAttachmentsCount = 0;
        ResponseObject responseObject = null;
        String entitySetName = ZCollections.NO_ATTACHMENT_CONTENT_UPLOAD_COLLECTION;
        String strResPath;
        Object rawData = null;
        try {
            strResPath = entitySetName + "/$count?$filter= (Notification eq '" + objKey+ "' and BINARY_FLG ne 'N')";
            responseObject = DataHelper.getInstance().getEntities(entitySetName, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intAttachmentsCount = Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intAttachmentsCount;
    }

    /**
     * getting array list of ZODataEntity for notification task/notification item's task
     * @param fetchLevel
     * @param notifNum
     * @param itemNum
     * @param taskNum
     * @param isWONotif
     * @return
     */
    protected  ArrayList<ZODataEntity> getTasks(ZAppSettings.FetchLevel fetchLevel, String notifNum, String itemNum, String taskNum, boolean isWONotif) {

        ResponseObject result = null;
        String resPath = "";
        String strEntitySet = isWONotif ? ZCollections.WO_NOTIFICATION_TASKS_COLLECTION : ZCollections.NOTIFICATION_TASKS_COLLECTION;
        String strOrderByURI = "&$orderby=Task";
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
                    resPath += isFirstParam ? "?" : "&" + "$select=Task,TaskText,CodeGroup";
                    break;
                case Single:
                    if (taskNum != null && notifNum != null) {
                        resPath = strEntitySet + "?$filter=(Notification%20eq%20%27" + notifNum + "%27%20and%20Task%20eq%20%27" + taskNum + "%27)";
                    }
                default:
                    break;
            }
            result = DataHelper.getInstance().getEntities(strEntitySet, resPath);
            zoDatataskEntities=new ArrayList<>();
            if(result!=null&&!result.isError()){
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    zoDatataskEntities.add(zoDataEntity);
                }
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(NotificationActivity.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ArrayList<ZODataEntity>();
        }
        return zoDatataskEntities;
    }
}
