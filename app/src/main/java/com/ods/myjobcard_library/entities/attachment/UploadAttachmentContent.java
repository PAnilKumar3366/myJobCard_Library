package com.ods.myjobcard_library.entities.attachment;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.myjobcard_library.utils.DocsUtil;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.Common;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadAttachmentContent extends ZBaseEntity {

    private String WorkOrderNum;
    private String OperationNum;
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

    public UploadAttachmentContent(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        try {
            initializeEntityProperties();
            create(entity);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public UploadAttachmentContent() {
        initializeEntityProperties();
    }

    public static ResponseObject UploadAttachment(String filePath, String fileName, boolean isImage, String description, WorkOrder workOrder, boolean autoFlush) {
        ResponseObject result = null;
        UploadAttachmentContent uploadAttachmentFile;
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

                uploadAttachmentFile = new UploadAttachmentContent();
                uploadAttachmentFile.setFILE_NAME(fileName != null ? fileName : sourceFile.getName());
                uploadAttachmentFile.setMIMETYPE(DocsUtil.getMimeTypeFromFile(sourceFile));
                uploadAttachmentFile.setWorkOrderNum(workOrder.getWorkOrderNum());
                docID = ZConfigManager.LOCAL_ID + Common.getReqTimeStamp(16);
                uploadAttachmentFile.setDocID(docID);
                uploadAttachmentFile.setFILE_SIZE(String.valueOf(sourceFile.length()));
                uploadAttachmentFile.setBINARY_FLG("1");
                uploadAttachmentFile.setLine(base64Content);
                uploadAttachmentFile.setDescription(description);
                if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)
                    uploadAttachmentFile.setOperationNum(workOrder.getCurrentOperation().getOperationNum());
                else
                    uploadAttachmentFile.setOperationNum("");

                uploadAttachmentFile.setMode(ZAppSettings.EntityMode.Create);
                uploadAttachmentFile.setTempID(workOrder.getTempID());
                result = uploadAttachmentFile.SaveToStore(autoFlush);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(UploadNotificationAttachmentContent.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error);
        }
        result.setContent(docID);
        return result;
    }
    //Setter & getter Methods

    public static ResponseObject UploadAttachmentBase64Content(String fileContent, String fileName, String description, WorkOrder workOrder, String fileSize, String fileType, boolean autoFlush) {
        ResponseObject result = null;
        try {
            if (fileContent != null && !fileContent.isEmpty()) {
                fileContent = DocsUtil.resizeBase64Content(fileContent);
                UploadAttachmentContent uploadAttachmentFile;
                uploadAttachmentFile = new UploadAttachmentContent();
                uploadAttachmentFile.setFILE_NAME(fileName);
                uploadAttachmentFile.setMIMETYPE(fileType);
                uploadAttachmentFile.setWorkOrderNum(workOrder.getWorkOrderNum());
                uploadAttachmentFile.setDocID(ZConfigManager.LOCAL_ID + Common.getReqTimeStamp(16));
                uploadAttachmentFile.setFILE_SIZE(String.valueOf(fileContent.length()));
                uploadAttachmentFile.setBINARY_FLG("1");
                uploadAttachmentFile.setLine(fileContent);
                uploadAttachmentFile.setDescription(description);
                if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)
                    uploadAttachmentFile.setOperationNum(workOrder.getCurrentOperation().getOperationNum());
                else
                    uploadAttachmentFile.setOperationNum("");

                uploadAttachmentFile.setMode(ZAppSettings.EntityMode.Create);
                uploadAttachmentFile.setTempID(workOrder.getTempID());
                result = uploadAttachmentFile.SaveToStore(autoFlush);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(UploadNotificationAttachmentContent.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error);
        }
        return result;
    }

    public static ResponseObject getUploadedAttachments(String workOrderNum) {
        ResponseObject result = null;
        try {
            String resPath = ZCollections.WO_ATTACHMENT_CONTENT_UPLOAD_COLLECTION;
            if (workOrderNum != null && !workOrderNum.isEmpty())
                resPath += "?$filter=(WorkOrderNum eq '" + workOrderNum + "' and BINARY_FLG ne 'N')&$select=DocID,FILE_SIZE,FILE_NAME,Description,WorkOrderNum,MIMETYPE,URL";
            result = getObjListFromStore(ZCollections.WO_ATTACHMENT_CONTENT_UPLOAD_COLLECTION, resPath);
            if (!result.isError()) {
                ArrayList<UploadAttachmentContent> codes = (ArrayList<UploadAttachmentContent>) result.Content();
                if (codes != null) {
                    result.setContent(codes);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(UploadAttachmentContent.class, ZAppSettings.LogLevel.Error, e.getMessage());
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
                ArrayList<UploadAttachmentContent> content = null;
                UploadAttachmentContent attachment;
                content = new ArrayList<UploadAttachmentContent>();
                for (ODataEntity entity : entities) {
                    attachment = new UploadAttachmentContent(entity, ZAppSettings.FetchLevel.List);
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

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.WO_ATTACHMENT_CONTENT_UPLOAD_COLLECTION);
        this.setEntityType(ZCollections.WO_ATTACHMENT_CONTENT_UPLOAD_ENTITY_TYPE);
        this.addKeyFieldNames("DocID");
        this.addKeyFieldNames("WorkOrderNum");
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getOperationNum() {
        return OperationNum;
    }

    public void setOperationNum(String operationNum) {
        OperationNum = operationNum;
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

    //End of Setter & getter Methods

    public void setTempID(String tempID) {
        TempID = tempID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

}