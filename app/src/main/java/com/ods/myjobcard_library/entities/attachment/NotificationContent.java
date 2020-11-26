package com.ods.myjobcard_library.entities.attachment;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

public class NotificationContent extends BaseEntity {

    private String Line;
    private String DocID;
    private String Notification;
    private String FILE_SIZE;
    private String BINARY_FLG;
    private String FIRST_LINE;
    private String LAST_LINE;
    private String FILE_NAME;
    private String MIMETYPE;
    private String PROPERTY;

    public NotificationContent(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        try {
            create(entity);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }
    //Setter & getter Methods

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

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
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

    public void setPROPERTY(String PROPERTY) {
        this.PROPERTY = PROPERTY;
    }


    //End of Setter & getter Methods

}