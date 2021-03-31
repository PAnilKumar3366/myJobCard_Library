package com.ods.myjobcard_library.viewmodels.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.attachment.UploadNotificationAttachmentContent;
import com.ods.myjobcard_library.entities.attachment.WorkOrderAttachment;
import com.ods.myjobcard_library.entities.transaction.NotificationTask;
import com.ods.myjobcard_library.interfaces.BackgroundTaskInterface;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

/**
 * This class contains all live data setter and getter methods by getting through its helper methods
 */
public class NotificationTaskDocsViewModel extends NotificationBaseViewModel
{
    ArrayList<WorkOrderAttachment> downloadableAttachList;
    ArrayList<UploadNotificationAttachmentContent> uploadedAttachList;
    private MutableLiveData<ArrayList<WorkOrderAttachment>> downloadableAttachLiveData = new MutableLiveData<ArrayList<WorkOrderAttachment>>();
    private MutableLiveData<ArrayList<UploadNotificationAttachmentContent>> uploadedAttachLiveData = new MutableLiveData<ArrayList<UploadNotificationAttachmentContent>>();
    private MutableLiveData<Boolean> uploadTaskAttachment = new MutableLiveData<Boolean>();
    private UploadNotificationAttachmentContentHelper uploadedattachmentContentHelper;
    private WorkOrderAttachmentHelper downloadableAttachmentContentHelper;

    private int attchCount;

    /**
     * creating the helper instances in this constructer for calling its helper methods
     * @param application
     */
    public NotificationTaskDocsViewModel(@NonNull Application application) {
        super(application);
        uploadedattachmentContentHelper =new UploadNotificationAttachmentContentHelper();
        downloadableAttachmentContentHelper=new WorkOrderAttachmentHelper();
    }

    public MutableLiveData<ArrayList<UploadNotificationAttachmentContent>> getUploadedAttachLiveData() {
        return uploadedAttachLiveData;
    }

    /**
     * fetching the notification task or item's task uploaded attachmnets by calling through helper instances
     * @param notification
     * @param item
     * @param task
     */
    public void fetchUploadedAttachLiveData(String notification,String item,String task){
        try {
            uploadedAttachLiveData.setValue(uploadedattachmentContentHelper.getUploadedTaskAttachments(notification,item,task));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
      /*  ArrayList<ZODataEntity> uploadedAttachmentArrayList=attachmentContentHelper.getUploadedNotificationItemTaskAttachments(notification,item,task);
        if(uploadedAttachmentArrayList!=null)
            onFetchUploadedAttachments(uploadedAttachmentArrayList);*/
        /*attachmentContentHelper.setTaskInterface(new BackgroundTaskInterface() {
            @Override
            public void onTaskPostExecute(ArrayList<ZODataEntity> zoDataEntities, boolean isError, String errorMsg) {
                if(!isError){
                    onFetchUploadedAttachments(zoDataEntities);
                }
                else
                    setError(errorMsg);
            }

            @Override
            public void onTaskPreExecute() {

            }

            @Override
            public void onTaskProgressUpdate() {

            }
        });
        attachmentContentHelper.getUploadedNotificationItemTaskAttachments(notification,item,task);*/

    }
    /*Converting the ZODataEntity list to WorkOrderAttachment's list  */
    /*protected void onFetchDownloadableAttachLiveData (ArrayList<ZODataEntity> zODataEntities)
    {
        downloadableAttachList=new ArrayList<>();
        try {
            for(ZODataEntity entity:zODataEntities){
                WorkOrderAttachment workOrderAttachment=new WorkOrderAttachment(entity);
                downloadableAttachList.add(workOrderAttachment);
            }
            downloadableAttachLiveData.setValue(downloadableAttachList);
        }
        catch (Exception e){
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
    }*/
    public MutableLiveData<ArrayList<WorkOrderAttachment>> getDownloadableAttachLiveData() {
        return downloadableAttachLiveData;
    }

    /**
     * fetching the downloadable/available notification task or item's task attachments
     * @param notification
     * @param item
     * @param task
     * @param isWONotif
     * @param notificationType
     */
    public void fetchDownloadableAttachLiveData(String notification,String item,String task,boolean isWONotif,String notificationType){
        try {
            downloadableAttachLiveData.setValue(downloadableAttachmentContentHelper.getDownloadableNotificationItemTaskAttachments(notification,item,task,isWONotif,notificationType));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }

/*        WorkOrderAttachmentHelper attachmentContentHelper=new WorkOrderAttachmentHelper();
        attachmentContentHelper.setTaskInterface(new BackgroundTaskInterface() {
            @Override
            public void onTaskPostExecute(ArrayList<ZODataEntity> zoDataEntities, boolean isError, String errorMsg) {
                if(!isError){
                    onFetchDownloadableAttachLiveData(zoDataEntities);
                }
                else
                    setError(errorMsg);
            }

            @Override
            public void onTaskPreExecute() {

            }

            @Override
            public void onTaskProgressUpdate() {

            }
        });
        attachmentContentHelper.getDownloadableNotificationItemTaskAttachments(notification,item,task,isWONotif,notificationType);*/
    }
    /*Converting the ZODataEntity list to Uploaded attachment's list  */
   /* protected void onFetchUploadedAttachments(ArrayList<ZODataEntity> zODataEntities)
    {
        uploadedAttachList=new ArrayList<>();
        for(ZODataEntity entity:zODataEntities){
            UploadNotificationAttachmentContent uploadNotificationAttachmentContent=new UploadNotificationAttachmentContent(entity);
            uploadedAttachList.add(uploadNotificationAttachmentContent);
        }
        uploadedAttachLiveData.setValue(uploadedAttachList);
    }*/

    public MutableLiveData<Boolean> getUploadTaskAttachmentResult() {
        return uploadTaskAttachment;
    }

    /**
     * posting uploaded attachmnets by calling its helper method
     * @param filePath
     * @param fileName
     * @param isImage
     * @param description
     * @param notificationTask
     * @param autoFlush
     */
    public void postingTaskAttachment(String filePath, String fileName, boolean isImage, String description, NotificationTask notificationTask, boolean autoFlush){
        try {
            uploadTaskAttachment.setValue(uploadedattachmentContentHelper.UploadAttachment(filePath,fileName,isImage,description,notificationTask,autoFlush));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

}
