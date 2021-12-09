package com.ods.myjobcard_library.entities.attachment;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.utils.DocsUtil;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UploadNotificationAttachmentContent extends ZBaseEntity {

    private static HashMap<String, UploadNotificationAttachmentContent> FormAttachments;
    private String Notification;
    private String FILE_NAME;
    private String MIMETYPE;
    private String Line;
    private String DocID;
    private String FILE_SIZE;
    private String BINARY_FLG;
    private String FIRST_LINE;
    private String LAST_LINE;
    private String PROPERTY;
    private String Description;
    private String TempID;
    private String URL;
    private String FuncLocation;
    private String Equipment;

    //Newly added fields for uploading the notification task and Item's task attachment
    private String Item;
    private String Task;

    public UploadNotificationAttachmentContent(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        try {
            initializeEntityProperties();
            create(entity);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**
     newly added constructer to create or map the new instance with the given ZODataEntity Object.
     *
     * @param entity ZODataEntity Contains the oDataEntity or EntityValue
     */
    public UploadNotificationAttachmentContent(ZODataEntity entity) {
        try {
            initializeEntityProperties();
            create(entity);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public UploadNotificationAttachmentContent() {
        initializeEntityProperties();
    }

    public static void addFormAttachment(UploadNotificationAttachmentContent attachment) {
        if (FormAttachments == null)
            FormAttachments = new HashMap<>();
        FormAttachments.put(attachment.getDescription(), attachment);
    }

    public static HashMap<String, UploadNotificationAttachmentContent> getFormAttachments() {
        return FormAttachments;
    }

    public static void clearFormAttachments() {
        FormAttachments = null;
    }

    public static ResponseObject UploadAttachment(String filePath, String fileName, boolean isImage, String description, Notification notification, boolean autoFlush) {
        ResponseObject result = null;
        String docId="";
        try {
            result = PrepareAttachmentObject(filePath, fileName, isImage, description, notification);
            if (!result.isError()) {
                UploadNotificationAttachmentContent uploadAttachmentFile = (UploadNotificationAttachmentContent) result.Content();
                docId=uploadAttachmentFile.getDocID();
                result = uploadAttachmentFile.SaveToStore(autoFlush);
                result.setContent(docId);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(UploadNotificationAttachmentContent.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error);
        }
        return result;
    }

    public static ResponseObject PrepareAttachmentObject(String filePath, String fileName, boolean isImage, String description, Notification notification) {
        ResponseObject result = null;
        UploadNotificationAttachmentContent uploadAttachmentFile;
        String docID = "";
        try {
            File sourceFile = new File(filePath);
            String base64Content = "";
            if (isImage)
                base64Content = DocsUtil.resizeFileContent(filePath);
            else {
                ByteArrayOutputStream output = DocsUtil.convertToBase64(filePath, isImage);
                base64Content = output.toString();
            }
            if (!base64Content.isEmpty()) {
                uploadAttachmentFile = new UploadNotificationAttachmentContent();
                uploadAttachmentFile.setFILE_NAME(fileName != null ? fileName : sourceFile.getName());
                uploadAttachmentFile.setMIMETYPE(DocsUtil.getMimeTypeFromFile(sourceFile));
                //uploadAttachmentFile.setMode(AppSettings.EntityMode.Create);
                uploadAttachmentFile.setNotification(notification != null ? notification.getNotification() : "");
                docID = ZConfigManager.LOCAL_ID + ZCommon.getReqTimeStamp(16);
                uploadAttachmentFile.setDocID(docID);
                uploadAttachmentFile.setFILE_SIZE(String.valueOf(sourceFile.length()));
                uploadAttachmentFile.setBINARY_FLG("1");
                uploadAttachmentFile.setLine(base64Content);
                uploadAttachmentFile.setDescription(description);

                uploadAttachmentFile.setMode(ZAppSettings.EntityMode.Create);
                uploadAttachmentFile.setTempID(notification != null ? notification.getTempID() : "");
                result = new ResponseObject(ZConfigManager.Status.Success, "", uploadAttachmentFile);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(UploadNotificationAttachmentContent.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error);
        }
        return result;
    }
    //Setter & getter Methods

    public static UploadNotificationAttachmentContent PrepareAttachmentObject(String fileContent, String fileName, String description, Notification notification, String fileSize, String fileType) {
        UploadNotificationAttachmentContent uploadAttachmentFile = null;
        try {
            if (fileContent != null && !fileContent.isEmpty()) {
                fileContent = DocsUtil.resizeBase64Content(fileContent);
                uploadAttachmentFile = new UploadNotificationAttachmentContent();
                uploadAttachmentFile.setFILE_NAME(fileName);
                uploadAttachmentFile.setMIMETYPE(fileType);//DocsUtil.extractMimeType(fileContent)
                //uploadAttachmentFile.setMode(AppSettings.EntityMode.Create);
                uploadAttachmentFile.setNotification(notification != null ? notification.getNotification() : "");
                uploadAttachmentFile.setDocID(ZConfigManager.LOCAL_ID + ZCommon.getReqTimeStamp(16));
                uploadAttachmentFile.setFILE_SIZE(String.valueOf(fileContent.length()));//(fileContent.length() * 3) / 4)
                uploadAttachmentFile.setBINARY_FLG("1");
                uploadAttachmentFile.setLine(fileContent);
                uploadAttachmentFile.setDescription(description);

                uploadAttachmentFile.setMode(ZAppSettings.EntityMode.Create);
                uploadAttachmentFile.setTempID(notification != null ? notification.getTempID() : "");
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(UploadNotificationAttachmentContent.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return uploadAttachmentFile;
    }

    public static ResponseObject getUploadedAttachments(String notification) {
        ResponseObject result = null;
        try {
            String resPath = ZCollections.NO_ATTACHMENT_CONTENT_UPLOAD_COLLECTION;
            if (notification != null && !notification.isEmpty())
                resPath += "?$filter=(Notification eq '" + notification + "' and BINARY_FLG ne 'N')&$select=DocID,FILE_SIZE,FILE_NAME,Description,Notification,MIMETYPE,URL";
            result = getObjListFromStore(ZCollections.NO_ATTACHMENT_CONTENT_UPLOAD_COLLECTION, resPath);
            if (!result.isError()) {
                ArrayList<UploadNotificationAttachmentContent> content = (ArrayList<UploadNotificationAttachmentContent>) result.Content();
                if (content != null) {
                    result.setContent(content);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(UploadNotificationAttachmentContent.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        if (result != null)
            return result;
        else
            return new ResponseObject(ZConfigManager.Status.Error);
    }

    public static ResponseObject getObjListFromStore(String entitySetName, String resPath) {
        ResponseObject result = null;
        try {
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (!result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                ArrayList<UploadNotificationAttachmentContent> content = null;
                UploadNotificationAttachmentContent attachment;
                content = new ArrayList<UploadNotificationAttachmentContent>();
                for (ODataEntity entity : entities) {
                    attachment = new UploadNotificationAttachmentContent(entity, ZAppSettings.FetchLevel.List);
                    //result = wo.fromEntity(entity);
                    if (attachment != null) {
                        content.add(attachment);
                    } else {
                        //pending: log the error message
                    }
                }
                if (result == null) {
                    result = new ResponseObject(ZConfigManager.Status.Success);
                }
                result.setMessage("");
                result.setContent(content);
            }
        } catch (Exception e) {
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        if (result != null)
            return result;
        else
            return new ResponseObject(ZConfigManager.Status.Error);
    }

    public String getFuncLocation() {
        return FuncLocation;
    }

    public void setFuncLocation(String funcLocation) {
        FuncLocation = funcLocation;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.NO_ATTACHMENT_CONTENT_UPLOAD_COLLECTION);
        this.setEntityType(ZCollections.NO_ATTACHMENT_CONTENT_UPLOAD_ENTITY_TYPE);
        this.addKeyFieldNames("DocID");
        this.addKeyFieldNames("Notification");
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }

    public String getFILE_NAME() {
        return FILE_NAME;
    }

    public void setFILE_NAME(String FILE_NAME) {
        this.FILE_NAME = FILE_NAME;
    }

    public String getMIMETYPE() {
        return MIMETYPE;
    }

    public void setMIMETYPE(String MIMETYPE) {
        this.MIMETYPE = MIMETYPE;
    }

    public String getLine() {
        return Line;
    }

    public void setLine(String line) {
        Line = line;
    }

    public void appendLine(String line) {
        Line = Line + line;
    }

    public String getDocID() {
        return DocID;
    }

    public void setDocID(String docID) {
        DocID = docID;
    }

    public String getFILE_SIZE() {
        return FILE_SIZE;
    }

    public void setFILE_SIZE(String FILE_SIZE) {
        this.FILE_SIZE = FILE_SIZE;
    }

    public String getBINARY_FLG() {
        return BINARY_FLG;
    }

    public void setBINARY_FLG(String BINARY_FLG) {
        this.BINARY_FLG = BINARY_FLG;
    }

    public String getFIRST_LINE() {
        return FIRST_LINE;
    }

    //End of Setter & getter Methods

    public void setFIRST_LINE(String FIRST_LINE) {
        this.FIRST_LINE = FIRST_LINE;
    }

    public String getLAST_LINE() {
        return LAST_LINE;
    }

    public void setLAST_LINE(String LAST_LINE) {
        this.LAST_LINE = LAST_LINE;
    }

    public String getPROPERTY() {
        return PROPERTY;
    }

    public void setPROPERTY(String PROPERTY) {
        this.PROPERTY = PROPERTY;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String url) {
        this.URL = url;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getTask() {
        return Task;
    }

    public void setTask(String task) {
        Task = task;
    }
}