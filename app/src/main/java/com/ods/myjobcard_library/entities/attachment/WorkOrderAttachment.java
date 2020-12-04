package com.ods.myjobcard_library.entities.attachment;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.appsettings.TableConfigSet;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MY HOME on 4/18/2019.
 */
public class WorkOrderAttachment extends ZBaseEntity {

    private String ClassName;
    private String OperationNum;
    private String ClassType;
    private String ObjectKey;
    private String DocId;
    private String EnteredBy;
    private String DocCount;
    private String DocVerNo;
    private String DocVarId;
    private String DocVarTg;
    private String CompCount;
    private String PropName;
    private String PropValue;
    private String Extension;
    private String AttachmentSource;
    private String CompID;
    private String MimeType;
    private String CompSize;
    private String OnlineSearch;

    public WorkOrderAttachment(ODataEntity entity) {
        try {
            create(entity);
            initializeEntityProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get methods
    public static ResponseObject getAttachments(String objectKey, String classTypeName, boolean isWONotif, String className) {

        ResponseObject result = null;
        String resourcePath = null;
        String strOrderByURI = null;
        String strEntitySet = null;

        try {
            if (objectKey != null && objectKey.length() > 0 && classTypeName != null) {
                strEntitySet = classTypeName.equalsIgnoreCase(ZAppSettings.Screens.WorkOrder.getClassName()) ? ZCollections.WO_ATTACHMENT_COLLECTION : (!isWONotif ? ZCollections.NO_ATTACHMENT_COLLECTION : ZCollections.WO_NO_ATTACHMENT_COLLECTION);
                resourcePath = strEntitySet + "?$filter=(endswith(ObjectKey, '" + objectKey + "') eq true and ClassName eq '" + className + "')";//and tolower(EnteredBy) ne 'nsingla'
                //result = DataHelper.getInstance().getEntities(strEntitySet, resourcePath);
                result = DataHelper.getInstance().getEntities(resourcePath, TableConfigSet.getStore(strEntitySet));
                if (!result.isError()) {
                    //parse data for Notification Activites from payload
                    result = FromEntity((List<ODataEntity>) result.Content(), isWONotif, classTypeName);
                }
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrderAttachment.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, boolean isWoNotif, String classType) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<WorkOrderAttachment> attachments = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    attachments.add(new WorkOrderAttachment(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", attachments);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrderAttachment.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.WO_ATTACHMENT_COLLECTION);
        this.addKeyFieldNames("ObjectKey");
        this.addKeyFieldNames("DocId");
        this.addKeyFieldNames("AttachmentSource");
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getOperationNum() {
        return OperationNum;
    }

    public void setOperationNum(String operationNum) {
        OperationNum = operationNum;
    }

    public String getClassType() {
        return ClassType;
    }

    public void setClassType(String classType) {
        ClassType = classType;
    }

    public String getObjectKey() {
        return ObjectKey;
    }

    public void setObjectKey(String objectKey) {
        ObjectKey = objectKey;
    }

    public String getDocId() {
        return DocId;
    }

    public void setDocId(String docId) {
        DocId = docId;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getDocCount() {
        return DocCount;
    }

    public void setDocCount(String docCount) {
        DocCount = docCount;
    }

    public String getDocVerNo() {
        return DocVerNo;
    }

    public void setDocVerNo(String docVerNo) {
        DocVerNo = docVerNo;
    }

    public String getDocVarId() {
        return DocVarId;
    }

    public void setDocVarId(String docVarId) {
        DocVarId = docVarId;
    }

    public String getDocVarTg() {
        return DocVarTg;
    }

    public void setDocVarTg(String docVarTg) {
        DocVarTg = docVarTg;
    }

    public String getCompCount() {
        return CompCount;
    }

    public void setCompCount(String compCount) {
        CompCount = compCount;
    }

    public String getPropName() {
        return PropName;
    }

    public void setPropName(String propName) {
        PropName = propName;
    }

    public String getPropValue() {
        return PropValue;
    }

    public void setPropValue(String propValue) {
        PropValue = propValue;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }

    public String getAttachmentSource() {
        return AttachmentSource;
    }

    public void setAttachmentSource(String attachmentSource) {
        AttachmentSource = attachmentSource;
    }

    public String getCompID() {
        return CompID;
    }

    public void setCompID(String compID) {
        CompID = compID;
    }

    public String getMimeType() {
        return MimeType;
    }

    public void setMimeType(String mimeType) {
        MimeType = mimeType;
    }

    public String getCompSize() {
        return CompSize;
    }

    public void setCompSize(String compSize) {
        CompSize = compSize;
    }

    public String getOnlineSearch() {
        return OnlineSearch;
    }

    public void setOnlineSearch(String onlineSearch) {
        OnlineSearch = onlineSearch;
    }
}
