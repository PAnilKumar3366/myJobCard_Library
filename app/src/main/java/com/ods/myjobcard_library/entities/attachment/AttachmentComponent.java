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

public class AttachmentComponent extends ZBaseEntity {

    private String DocCount;
    private String CompCount;
    private String CompID;
    private String MimeType;
    private String CompSize;
    private String EnteredBy;
    private String ObjectKey;
    private String AttachmentSource;

    public AttachmentComponent(ODataEntity entity) {
        try {
            create(entity);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }
    //Setter & getter Methods

    public static ResponseObject getAttachmentComponent(String objectKey, String RefDocCount, String attachmentSource, String className, boolean isWONotif) {
        ResponseObject result = null;
        String resourcePath = null;
        String strEntitySet = null;
        try {
            strEntitySet = className.equalsIgnoreCase(ZAppSettings.Screens.WorkOrder.getClassName()) ? ZCollections.WO_ATTACHMENT_COMPONENT_COLLECTION : (!isWONotif ? ZCollections.NO_ATTACHMENT_COMPONENT_COLLECTION : ZCollections.WO_NO_ATTACHMENT_COMPONENT_COLLECTION);
            resourcePath = strEntitySet;
            resourcePath += "?$filter=(DocCount eq '" + RefDocCount + "' and endswith(ObjectKey, '" + objectKey + "') eq true" + (className.equalsIgnoreCase(ZAppSettings.Screens.WorkOrder.getClassName()) ? " and AttachmentSource eq '" + attachmentSource + "')" : ")");
            result = DataHelper.getInstance().getEntities(strEntitySet, resourcePath);
            if (!result.isError()) {
                //parse data for WO History / WO Pending from payload
                result = FromEntity((List<ODataEntity>) result.Content());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(AttachmentComponent.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<AttachmentComponent> attachmentComponents = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    attachmentComponents.add(new AttachmentComponent(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", attachmentComponents);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(AttachmentComponent.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getDocCount() {
        return DocCount;
    }

    public void setDocCount(String docCount) {
        DocCount = docCount;
    }

    public String getCompCount() {
        return CompCount;
    }

    public void setCompCount(String compCount) {
        CompCount = compCount;
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

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getObjectKey() {
        return ObjectKey;
    }

    public void setObjectKey(String objectKey) {
        ObjectKey = objectKey;
    }

    //End of Setter & getter Methods

    public String getAttachmentSource() {
        return AttachmentSource;
    }

    public void setAttachmentSource(String attachmentSource) {
        AttachmentSource = attachmentSource;
    }

}