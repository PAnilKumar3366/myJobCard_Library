package com.ods.myjobcard_library.entities.attachment;

import android.content.Context;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

public class Content extends BaseEntity {

    private String Line;
    private String DocID;
    private String WorkOrderNum;
    private String FILE_SIZE;
    private String BINARY_FLG;
    private String FIRST_LINE;
    private String LAST_LINE;
    private String FILE_NAME;
    private String MIMETYPE;
    private String PROPERTY;

    public Content(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        try {
            create(entity);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
    }
    //Setter & getter Methods

    public static ResponseObject getAttchmentContent(String DocID, String WorkOrderNum, String FileName, String Mime, boolean isURL, boolean isNotification, Context context) {
        ResponseObject responseObject = null;
        try {
            //WorkOrderNum = Common.getFormattedInt(12, Integer.valueOf(WorkOrderNum));
            DocID = DocID.replace(" ", "%20");
            //For Dev
            //String path = ConfigManager.Attachment_Service_URL + (isURL ? Collections.WO_ATTACHMENT_CONTENT_COLLECTION : Collections.WO_ATTACHMENT_CONTENT_DOWNLOAD_COLLECTION) + "(DocID='"+ DocID +"',WorkOrderNum='"+WorkOrderNum +"')"+ (isURL ? "?$format=json" : "/$value");

            //String path = ConfigManager.SAP_Host + ConfigManager.Tx_Service_Suffix + (isURL ? Collections.WO_ATTACHMENT_CONTENT_COLLECTION : Collections.WO_ATTACHMENT_CONTENT_DOWNLOAD_COLLECTION) + "(DocID='"+ DocID +"',WorkOrderNum='"+WorkOrderNum +"')"+ (isURL ? "?$format=json" : "/$value");
            //String path = (AppSettings.isHttps ? "https://" : "http://") + AppSettings.App_IP + ":" + AppSettings.App_Port + "/" + ConfigManager.TX_SERVICE_NAME + "/" + (isURL ? Collections.WO_ATTACHMENT_CONTENT_COLLECTION : Collections.WO_ATTACHMENT_CONTENT_DOWNLOAD_COLLECTION) + "(DocID='"+ DocID +"',WorkOrderNum='"+WorkOrderNum +"')"+ (isURL ? "?$format=json" : "/$value");
            String path = (isNotification ? "NOAttachContentSet" : ZCollections.WO_ATTACHMENT_CONTENT_COLLECTION) + "(DocID='" + DocID + "'," + (isNotification ? "Notification='" : "WorkOrderNum='") + WorkOrderNum + "')" + (isURL ? "?$format=json" : "");

            //For QA
            //String path = ConfigManager.Attachment_Service_URL + "SAP_WM_DLITE_SRV/" + (isURL ? Collections.WO_ATTACHMENT_CONTENT_COLLECTION : Collections.WO_ATTACHMENT_CONTENT_DOWNLOAD_COLLECTION) + "(DocID='"+ DocID +"',WorkOrderNum='"+WorkOrderNum +"')"+ (isURL ? "?$format=json" : "/$value");
            DownloadAttachment.downloadInBackground(path, FileName, Mime, isURL, context, (isNotification ? "NOAttachContentSet" : ZCollections.WO_ATTACHMENT_CONTENT_COLLECTION));
            responseObject = new ResponseObject(ZConfigManager.Status.Success, "Downloading in Background", null);

        } catch (Exception e) {
            DliteLogger.WriteLog(null, ZAppSettings.LogLevel.Error, e.getMessage());
            responseObject = new ResponseObject(ZConfigManager.Status.Error, "Downloading failed " + e.getMessage(), e);
        }
        return responseObject;
    }

    public String getLine() {
        return Line;
    }

    public void setLine(String line) {
        Line = line;
    }

    public String getDocID() {
        return DocID;
    }

    public void setDocID(String docID) {
        DocID = docID;
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
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

    public String getPROPERTY() {
        return PROPERTY;
    }


    //End of Setter & getter Methods

    public void setPROPERTY(String PROPERTY) {
        this.PROPERTY = PROPERTY;
    }


}