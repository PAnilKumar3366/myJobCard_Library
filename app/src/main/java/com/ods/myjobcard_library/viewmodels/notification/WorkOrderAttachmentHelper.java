package com.ods.myjobcard_library.viewmodels.notification;

import android.os.AsyncTask;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.attachment.WorkOrderAttachment;
import com.ods.myjobcard_library.entities.lowvolume.LTAttachmentType;
import com.ods.myjobcard_library.entities.transaction.NotificationActivity;
import com.ods.myjobcard_library.interfaces.BackgroundTaskInterface;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.ods.ods_sdk.utils.OfflineAsyncHelper;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class contains all helper methods for getting the downloadable/available attachments
 */
public class WorkOrderAttachmentHelper {

   // public BackgroundTaskInterface TaskInterface;
    private LTAttachmentType ltAttachmentType;
    private ArrayList<ZODataEntity> zoDatadownloadableTaskAttachEntities;

    protected WorkOrderAttachmentHelper(){

    }
/*    public void setTaskInterface(BackgroundTaskInterface taskInterface) {
        TaskInterface = taskInterface;
    }*/

    /**
     * getting array list of ZODataEntity for downloadable task attachments
     * @param notification
     * @param item
     * @param task
     * @param isWONotif
     * @param notificationType
     * @return
     */
    protected ArrayList<ZODataEntity> getDownloadableNotificationItemTaskAttachments(String notification,String item,String task,boolean isWONotif,String notificationType) {
        String resourcePath = null;
        String entitySetName = null;
        String className;
        String objKey = notification + item + task;
        try {
            getAttachmentType(ZAppSettings.ObjectCategory.NOTASK.getObjCategory(), notificationType);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        className = (ltAttachmentType!=null&&ltAttachmentType.getClassName() != null) ? ltAttachmentType.getClassName() : "";
        entitySetName = !isWONotif ? ZCollections.NO_ATTACHMENT_COLLECTION : ZCollections.WO_NO_ATTACHMENT_COLLECTION;
        resourcePath = entitySetName + "?$filter=(ObjectKey eq '" + objKey + "' and ClassName eq '" + className + "')";
        try {
        ResponseObject responseObject = DataHelper.getInstance().getEntities(entitySetName, resourcePath);
            zoDatadownloadableTaskAttachEntities=new ArrayList<>();
            if (responseObject != null && !responseObject.isError()) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(responseObject.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    zoDatadownloadableTaskAttachEntities.add(zoDataEntity);
                   /* *//*Converting the ZODataEntity  to WorkOrderAttachment object  *//*
                    WorkOrderAttachment workOrderAttachment = new WorkOrderAttachment(zoDataEntity);
                    downloadableTaskAttachList.add(workOrderAttachment);*/
                }
            }
            return zoDatadownloadableTaskAttachEntities;
        } catch (Exception e) {
            DliteLogger.WriteLog(NotificationActivity.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ArrayList<ZODataEntity>();
        }
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
        helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }*/

    /*Converting the ZODataEntity list to WorkOrderAttachment's list  */
   /* protected ArrayList<WorkOrderAttachment> onFetchDownloadableAttachLiveData (ArrayList<ZODataEntity> zODataEntities)
    {
        downloadableAttachList=new ArrayList<>();
        try {
            if(zODataEntities!=null) {
                for (ZODataEntity entity : zODataEntities) {
                    WorkOrderAttachment workOrderAttachment = new WorkOrderAttachment(entity);
                    downloadableAttachList.add(workOrderAttachment);
                }
            }
            return downloadableAttachList;
        }
        catch (Exception e){
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
            return downloadableAttachList;
        }
    }*/

    private void getAttachmentType(String objCategory, String objType) {
        try {
            ltAttachmentType = LTAttachmentType.getAttachmentTypeEntry(objCategory, objType);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }
}
