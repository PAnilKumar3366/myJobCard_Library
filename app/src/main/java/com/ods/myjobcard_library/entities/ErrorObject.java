package com.ods.myjobcard_library.entities;

import com.google.gson.Gson;
import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.entities.appsettings.TableConfigSet;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.appsetting.AppStoreSet;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

/**
 * Created by lenovo on 13-09-2016.
 */
public class ErrorObject extends BaseEntity {

    public ErrorObject(ODataEntity entity){
        create(entity);
    }

    private String Domain;
    private int HTTPStatusCode;
    private String RequestURL;
    private String Message;
    private String RequestBody;
    private String RequestMethod;
    private Long RequestID;
    private String InnerError;
    private String ObjectName;
    private String ObjectId;

    private String CustomTag;
    private String Code;

    private String AppStoreId;

    public BaseEntity getAffectedEntity() {
        return affectedEntity;
    }

    public void setAffectedEntity(BaseEntity affectedEntity) {
        this.affectedEntity = affectedEntity;
    }

    private BaseEntity affectedEntity;

    public static BaseEntity getAffectedEntityObject(ODataEntity errorEntity, AppStoreSet store) {
        BaseEntity errorEntityObj = null;
        try {
            String affectedEntityPath = String.valueOf(errorEntity.getNavigationProperty("AffectedEntity").getNavigationContent());
            ResponseObject result = DataHelper.getInstance().getEntities(affectedEntityPath, store);
            if (!result.isError() && result.Content() != null) {
                ODataEntity affectedEntity = (ODataEntity) result.Content();
                //Class cls = Class.forName(TableConfigSet.getClassName(ZCommon.getEntitySetNameFromPath(affectedEntity.getEditResourcePath())).replace("myjobcard", "mjc"));
                Class cls = Class.forName(TableConfigSet.getClassName(ZCommon.getEntitySetNameFromPath(affectedEntity.getEditResourcePath())));
                //errorEntityObj = (BaseEntity) cls.getDeclaredConstructor(ODataEntity.class,ZAppSettings.FetchLevel.class).newInstance(affectedEntity, ZAppSettings.FetchLevel.Single);
                errorEntityObj = (BaseEntity) cls.getDeclaredConstructor(ODataEntity.class).newInstance(affectedEntity);
//                    errorEntityObj = new WorkOrder((ODataEntity)DataHelper.getInstance().getEntities(Collections.WO_COLLECTION, affectedEntity.getEditResourcePath()).Content(), ZAppSettings.FetchLevel.Header);
                    /*((WorkOrder)errorEntityObj).setShortText("updated automatically 32");
                    errorEntityObj.setMode(ZAppSettings.EntityMode.Update);
                    result = errorEntityObj.SaveToStore();*/
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(ErrorObject.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return errorEntityObj;
    }

    public String getAppStoreId() {
        return AppStoreId;
    }

    private ErrorMessage error;

    public String getDomain() {
        return Domain;
    }

    public void setDomain(String domain) {
        Domain = domain;
    }

    public int getHTTPStatusCode() {
        return HTTPStatusCode;
    }

    public void setHTTPStatusCode(int HTTPStatusCode) {
        this.HTTPStatusCode = HTTPStatusCode;
    }

    public String getRequestURL() {
        return RequestURL;
    }

    public void setRequestURL(String requestURL) {
        RequestURL = requestURL;
    }

    public ErrorMessage getMessage() {
        ErrorMessage error = null;
        try {
            ErrorObject errorObject = new Gson().fromJson(Message, ErrorObject.class);
            error = errorObject.error;
        }
        catch (Exception e){
            //DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            error = new ErrorMessage();
            MessageBody msg = new MessageBody();
            msg.setValue(Message);
            error.setMessage(msg);
        }
        return error;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getRequestBody() {
        return RequestBody;
    }

    public void setRequestBody(String requestBody) {
        RequestBody = requestBody;
    }

    public String getRequestMethod() {
        return RequestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        RequestMethod = requestMethod;
    }

    public Long getRequestID() {
        return RequestID;
    }

    public void setRequestID(Long requestID) {
        RequestID = requestID;
    }

    public String getInnerError() {
        return InnerError;
    }

    public void setInnerError(String innerError) {
        InnerError = innerError;
    }

    public ErrorMessage getError() {
        return error;
    }

    public void setError(ErrorMessage error) {
        this.error = error;
    }

    public String getObjectName() {
        return ObjectName;
    }

    public void setObjectName(String objectName) {
        ObjectName = objectName;
    }

    public String getObjectId() {
        return ObjectId;
    }

    public void setObjectId(String objectId) {
        ObjectId = objectId;
    }

    //inner classes
    public class ErrorMessage{

        private String code;
        private MessageBody message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public MessageBody getMessage() {
            return message;
        }

        public void setMessage(MessageBody message) {
            this.message = message;
        }
    }

    public class MessageBody{

        private String lang;
        private String value;

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public void setAppStoreId(String appStoreId) {
        AppStoreId = appStoreId;
    }

    public void setObjectDetails(BaseEntity object) {
        try {
            setAffectedEntity(object);
            String displayName = TableConfigSet.getDisplayName(object.getEntitySetName());
            this.setObjectName(displayName);
            //this.setObjectName(object.getLocalEntityResourcePath().replace(object.getEntitySetName(), displayName));
            //Class cls = object.getClass();
            //this.setObjectName(object.getKeyFieldNames().get(0));
            /*Method method = cls.getDeclaredMethod("get" + object.getKeyFieldNames().get(0));
            String keyValue = String.valueOf(method.invoke(object));
            this.setObjectId(keyValue);*/
        } catch (Exception e) {
            DliteLogger.WriteLog(ErrorObject.class, ZAppSettings.LogLevel.Error,e.getMessage());
        }
    }

}
