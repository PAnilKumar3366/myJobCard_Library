package com.ods.myjobcard_library.viewmodels.notification;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.attachment.UploadNotificationAttachmentContent;
import com.ods.myjobcard_library.entities.attachment.WorkOrderAttachment;
import com.ods.myjobcard_library.entities.lowvolume.LTAttachmentType;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.entities.transaction.NotificationTask;
import com.ods.myjobcard_library.interfaces.BackgroundTaskInterface;
import com.ods.myjobcard_library.utils.DocsUtil;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.ods.ods_sdk.utils.OfflineAsyncHelper;
import com.sap.smp.client.odata.ODataEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This Class contains all helper methods for getting and uploading the notification task uploaded attachments
 */
public class UploadNotificationAttachmentContentHelper
{
    private LTAttachmentType ltAttachmentType;
    private ArrayList<UploadNotificationAttachmentContent> uploadedTaskAttachList;
    /*    public BackgroundTaskInterface TaskInterface;

    public void setTaskInterface(BackgroundTaskInterface taskInterface) {
        TaskInterface = taskInterface;
    }*/
   protected UploadNotificationAttachmentContentHelper(){

   }

    /**
     * gettting uploaded task attachments
     * @param notification
     * @param item
     * @param task
     * @return
     */
   protected ArrayList<UploadNotificationAttachmentContent> getUploadedTaskAttachments(String notification, String item, String task){
       String resourcePath = null;
       String entitySetName = null;
       entitySetName = ZCollections.NO_ATTACHMENT_CONTENT_UPLOAD_COLLECTION;
       if (item.isEmpty())
           item="0000";
       resourcePath = entitySetName + "?$filter=(Notification eq '" + notification + "' and Item eq '" + item + "' and Task eq '" + task + "' and BINARY_FLG ne 'N')&$select=DocID,FILE_SIZE,FILE_NAME,Description,Notification,Item,Task,MIMETYPE";
       ResponseObject responseObject=DataHelper.getInstance().getEntities(entitySetName,resourcePath);
       try {
           uploadedTaskAttachList = new ArrayList<>();
           if (responseObject != null && !responseObject.isError()) {
               List<ODataEntity> entities = ZBaseEntity.setODataEntityList(responseObject.Content());
               for (ODataEntity entity : entities) {
                   ZODataEntity zoDataEntity = new ZODataEntity(entity);
                   /*Converting the ZODataEntity  to UploadNotificationAttachmentContent object  */
                   UploadNotificationAttachmentContent uploadNotificationAttachmentContent=new UploadNotificationAttachmentContent(zoDataEntity);
                   uploadedTaskAttachList.add(uploadNotificationAttachmentContent);
               }
           }
           return uploadedTaskAttachList;
       } catch (Exception e) {
           DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
           return new ArrayList<UploadNotificationAttachmentContent>();
       }
     /* OfflineAsyncHelper helper = new OfflineAsyncHelper(resourcePath, entitySetName, new OfflineAsyncHelper.Callbacks() {
           @Override
           public void onResult(ResponseObject response) {
               try {
                   ArrayList<ZODataEntity> entityList = new ArrayList<>();
                   if (response != null && !response.isError()) {
                       List<ODataEntity> entities = ZBaseEntity.setODataEntityList(response.Content());
                       for (ODataEntity entity : entities) {
                           ZODataEntity item = new ZODataEntity(entity);
                           entityList.add(item);
                       }
                       TaskInterface.onTaskPostExecute(entityList, false, "");
                   } else {
                       TaskInterface.onTaskPostExecute(entityList, true, response.getMessage());
                   }
               } catch (Exception e) {
                   e.printStackTrace();
                   TaskInterface.onTaskPostExecute(new ArrayList<ZODataEntity>(), true, e.getMessage());
               }
           }
       });
       helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
   }

    protected boolean UploadAttachment(String filePath, String fileName, boolean isImage, String description, NotificationTask notificationTask, boolean autoFlush) {
        ResponseObject result = null;
        try {
            result = PrepareAttachmentObject(filePath, fileName, isImage, description, notificationTask);
            if (!result.isError()) {
                UploadNotificationAttachmentContent uploadAttachmentFile = (UploadNotificationAttachmentContent) result.Content();
                result = uploadAttachmentFile.SaveToStore(autoFlush);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(UploadNotificationAttachmentContent.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error);
        }
        return result.isError();
    }

    protected ResponseObject PrepareAttachmentObject(String filePath, String fileName, boolean isImage, String description, NotificationTask notificationTask) {
        ResponseObject result = null;
        try {
            File sourceFile = new File(filePath);
            ByteArrayOutputStream output = DocsUtil.convertToBase64(filePath, isImage);
            if (output != null) {
                UploadNotificationAttachmentContent uploadAttachmentFile;
                uploadAttachmentFile = new UploadNotificationAttachmentContent();
                uploadAttachmentFile.setFILE_NAME(fileName != null ? fileName : sourceFile.getName());
                uploadAttachmentFile.setMIMETYPE(DocsUtil.getMimeTypeFromFile(sourceFile));
                //uploadAttachmentFile.setMode(AppSettings.EntityMode.Create);
                uploadAttachmentFile.setNotification(notificationTask.getNotification() != null ? notificationTask.getNotification() : "");
                uploadAttachmentFile.setItem(notificationTask.getItem() != null ? notificationTask.getItem() : "");
                uploadAttachmentFile.setTask(notificationTask.getTask() != null ? notificationTask.getTask() : "");
                uploadAttachmentFile.setDocID(ZConfigManager.LOCAL_ID + ZCommon.getReqTimeStamp(16));
                uploadAttachmentFile.setFILE_SIZE(String.valueOf(sourceFile.length()));
                uploadAttachmentFile.setBINARY_FLG("1");
                uploadAttachmentFile.setLine(output.toString());
                uploadAttachmentFile.setDescription(description);

                uploadAttachmentFile.setMode(ZAppSettings.EntityMode.Create);
                uploadAttachmentFile.setTempID(notificationTask.getTempID() != null ? notificationTask.getTempID() : "");
                result = new ResponseObject(ZConfigManager.Status.Success, "", uploadAttachmentFile);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error);
        }
        return result;
    }

}
