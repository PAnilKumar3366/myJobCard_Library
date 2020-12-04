package com.ods.myjobcard_library.entities.attachment;

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

public class Connection extends ZBaseEntity {

    //private ArrayList<Signature> signatures;
    private AttachmentComponent attachmentComponent = null;
    private DataHelper dataHelper = null;

    private String ClassName;
    private String ClassType;
    private String ObjectKey;
    private String DocId;
    private String EnteredBy;
    private String DocCount;
    private String DocVerNo;
    private String DocVarID;
    private String DocVarTg;
    private String CompCount;
    private String PropName;
    private String PropValue;
    private String Extension;
    private String AttachmentSource;


    public Connection(String DocID) {
        super();
        try {
            this.DocId = DocID;
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public Connection(ODataEntity entity, boolean isWONotif) {
        try {
            create(entity, isWONotif, ZAppSettings.Screens.WorkOrder.getClassName());
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public Connection(ODataEntity entity, boolean isWONotif, String classType) {
        try {
            create(entity, isWONotif, classType);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    //Setter & getter Methods

    //get methods
    public static ResponseObject getAttachments(String objectKey, String classTypeName, boolean isWONotif) {

        ResponseObject result = null;
        String resourcePath = null;
        String strOrderByURI = null;
        String strEntitySet = null;

        try {
            if (objectKey != null && objectKey.length() > 0 && classTypeName != null) {
                strEntitySet = classTypeName.equalsIgnoreCase(ZAppSettings.Screens.WorkOrder.getClassName()) ? ZCollections.WO_ATTACHMENT_CONNECTION_SIGNATURE_COLLECTION : (!isWONotif ? ZCollections.NO_ATTACHMENT_CONNECTION_SIGNATURE_COLLECTION : ZCollections.WO_NO_ATTACHMENT_CONNECTION_SIGNATURE_COLLECTION);
                resourcePath = strEntitySet + "?$filter=(endswith(ObjectKey, '" + objectKey + "') eq true)";//and tolower(EnteredBy) ne 'nsingla'
                result = DataHelper.getInstance().getEntities(strEntitySet, resourcePath);
                if (!result.isError()) {
                    //parse data for Notification Activites from payload
                    result = FromEntity((List<ODataEntity>) result.Content(), isWONotif, classTypeName);
                }
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(Connection.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, boolean isWoNotif, String classType) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<Connection> connections = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    connections.add(new Connection(entity, isWoNotif, classType));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", connections);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(Connection.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static boolean uploadedFileExistsInDownloads(String fileName, String description, ArrayList<WorkOrderAttachment> downloads) {

        for (WorkOrderAttachment download : downloads) {
            if (download.getCompID() != null && download.getCompID().equalsIgnoreCase(fileName) && download.getPropValue() != null && download.getPropValue().equalsIgnoreCase(description))
                return true;
        }
        return false;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
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

    public void setDocId(String docID) {
        DocId = docID;
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

    public String getDocVarID() {
        return DocVarID;
    }

    public void setDocVarID(String docVarID) {
        DocVarID = docVarID;
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

    public AttachmentComponent getAttachmentComponent() {
        return attachmentComponent;
    }

    public String getExtension() {
        return Extension;
    }

//End of Setter & getter Methods

    public void setExtension(String extension) {
        Extension = extension;
    }

    public String getAttachmentSource() {
        return AttachmentSource;
    }

    public void setAttachmentSource(String attachmentSource) {
        AttachmentSource = attachmentSource;
    }

    public ResponseObject create(ODataEntity entity, boolean isWONotif, String classType) {
        ResponseObject result = null;
        ArrayList<AttachmentComponent> attachmentComponents = null;
        try {
            super.create(entity);
            ResponseObject resultComponent = null;
            try {
                resultComponent = AttachmentComponent.getAttachmentComponent(ObjectKey, DocCount, AttachmentSource, classType, isWONotif);
                if (resultComponent != null) {
                    if (!resultComponent.isError()) {
                        attachmentComponents = (ArrayList<AttachmentComponent>) resultComponent.Content();
                        if (attachmentComponents != null && attachmentComponents.size() > 0)
                            attachmentComponent = attachmentComponents.get(0);
                    }
                } else {
                    DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, "Could not add Attachment attachmentComponent details to Work order: " + resultComponent.getMessage());
                }
            } catch (Exception e) {
                DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            }
            result = new ResponseObject(ZConfigManager.Status.Success, "", this);
        } catch (Exception e) {
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }
}