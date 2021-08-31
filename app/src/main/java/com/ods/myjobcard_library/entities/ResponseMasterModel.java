package com.ods.myjobcard_library.entities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by kansal on 09/05/2016.
 * (Ruchi) This model is used to get data from the offline store and to post data as well. It is storing the values related to
 * the filled form.
 */
public class ResponseMasterModel extends ZBaseEntity {

    private String InstanceID;
    private String FormID;
    private String Version;
    private String ResponseData;
    private String WoNum;
    private String CreatedBy;
    private GregorianCalendar CreatedOn;
    private String ModifiedBy;
    private GregorianCalendar ModifiedOn;
    private String IsDraft;
    private String OperationNum;
    private String Equipment;
    private String FunctionLocation;
    private String NonObjType;

    private String TaskListType;
    private String Group;
    private String GroupCounter;
    private String InternalCounter;
    private String Counter;
    private String Remarks;

    public ResponseMasterModel(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public ResponseMasterModel(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity);
    }

    public ResponseMasterModel() {
        initializeEntityProperties();
        this.setInstanceID(generateID());
    }

    public ResponseMasterModel(String id) {
        initializeEntityProperties();
        this.setInstanceID(id);
    }

    public static int getSubmittedFormsCount(String formID, String version) {
        int count = 0;
        try {
            if (formID == null)
                formID = "";
            if (version == null)
                version = "";

            String entitySetName = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION;
            String resourcePath = entitySetName;
            if (ZConfigManager.OBJECT_TYPE_GENERAL_FORM)
                resourcePath += "/$count?$filter=(tolower(FormID) eq '" + formID.toLowerCase() + "' and Version eq '" + version + "')";
            else
                resourcePath += "/$count?$filter=(tolower(FormID) eq '" + formID.toLowerCase() + "' and NonObjType eq 'X' and Version eq '" + version + "')";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resourcePath);
            if (result != null && !result.isError()) {
                count = Integer.parseInt(String.valueOf(result.Content()));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(ResponseMasterModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return count;
    }

    public static ArrayList<ResponseMasterModel> getResponseCaptureData(String formID, String version, String woNum, String oprNum, boolean isResponseData, String instanceID) {
        ResponseObject result = null;
        String resourcePath = null;// "WoHeaderSet?$expand=NavOpera/OperaToPRT";
        try {
            if (formID == null)
                formID = "";
            if (version == null)
                version = "";
            if (woNum == null)
                woNum = "";

            String entitySetName = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION;
            resourcePath = entitySetName;
            if (instanceID == null)
                resourcePath += "?$filter=(tolower(FormID) eq '" + formID.toLowerCase() + "' and Version eq '" + version + "' and NonObjType ne 'X' and WoNum eq '" + woNum + "'" + (oprNum != null && !oprNum.isEmpty() ? " and OperationNum eq '" + oprNum + "'" : "") + ")";
            else
                resourcePath += "?$filter=(InstanceID eq '" + instanceID + "')";

            if (!isResponseData)
                resourcePath += "&$select=InstanceID,WoNum,OperationNum,FormID,Version,CreatedOn,IsDraft,Counter,ModifiedBy";

            result = DataHelper.getInstance().getEntities(entitySetName, resourcePath);

            if (!result.isError()) {
                //parse data for Equipment Characteristics
                result = FromEntity((List<ODataEntity>) result.Content());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(ResponseMasterModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

        if (result != null && !result.isError())
            return (ArrayList<ResponseMasterModel>) result.Content();
        else
            return new ArrayList<>();
    }

    //getting response for General FormItems List
    public static ArrayList<ResponseMasterModel> getGeneralResponseCaptureData(String formID, String version, boolean isResponseData, String instanceID) {
        ResponseObject result = null;
        String resourcePath = null;
        try {
            if (formID == null)
                formID = "";
            if (version == null)
                version = "";

            String entitySetName = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION;
            resourcePath = entitySetName;
            if (instanceID != null)
                resourcePath += "?$filter=(InstanceID eq '" + instanceID + "')";
            else if (ZConfigManager.OBJECT_TYPE_GENERAL_FORM)
                resourcePath += "?$filter=(tolower(FormID) eq '" + formID.toLowerCase() + "' and Version eq '" + version + "')";
            else
                resourcePath += "?$filter=(tolower(FormID) eq '" + formID.toLowerCase() + "' and Version eq '" + version + "' and NonObjType eq 'X')";

            if (!isResponseData)
                resourcePath += "&$select=InstanceID,FormID,Version,CreatedOn,IsDraft,Counter";

            result = DataHelper.getInstance().getEntities(entitySetName, resourcePath);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(ResponseMasterModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

        if (result != null && !result.isError())
            return (ArrayList<ResponseMasterModel>) result.Content();
        else
            return new ArrayList<>();
    }

    /**
     * Get the required data for filled forms based on input parameters.
     *
     * @param orderNum Work Order Number.
     * @param formID   Form's unique id.
     * @param version  Version of the form.
     *
     * @return ArrayList of ResponseMasterModel.
     */
    public static ArrayList<ResponseMasterModel> getFilledFormsForSupervisorView(String orderNum, String formID, String version) {
        ArrayList<ResponseMasterModel> arrayList = new ArrayList<>();
        try {
            String entitySetName = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION;
            String resPath = entitySetName + "?$filter=(WoNum eq '" + orderNum + "' and FormID eq '" + formID + "' and Version eq '" + version + "')";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                arrayList = (ArrayList<ResponseMasterModel>) result.Content();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(ResponseMasterModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return arrayList;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<ResponseMasterModel> responseMasterModels = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    responseMasterModels.add(new ResponseMasterModel(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", responseMasterModels);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(ResponseMasterModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION);
        this.setEntityType(ZCollections.FORMS_RESPONSE_CAPTURE_ENTITY_TYPE);
        this.addKeyFieldNames(ZConfigManager.FORMS_RESPONSE_CAPTURE_KEY_FIELD);
        this.setParentEntitySetName(ZCollections.WO_COLLECTION);
        this.setParentForeignKeyFieldName("WoNum");
        this.setParentKeyFieldName(ZConfigManager.WO_KEY_FIELD);
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        this.Version = version;
    }

    public String getWoNum() {
        return WoNum;
    }

    public void setWoNum(String woNum) {
        WoNum = woNum;
    }

    public String getOperationNum() {
        return OperationNum;
    }

    public void setOperationNum(String operationNum) {
        OperationNum = operationNum;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        this.CreatedBy = createdBy;
    }

    public GregorianCalendar getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(GregorianCalendar createdOn) {
        this.CreatedOn = createdOn;
    }

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.ModifiedBy = modifiedBy;
    }

    public GregorianCalendar getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(GregorianCalendar modifiedOn) {
        this.ModifiedOn = modifiedOn;
    }

    public String getInstanceID() {
        return InstanceID;
    }

    public void setInstanceID(String instanceID) {
        InstanceID = instanceID;
    }

    public String getFormID() {
        return FormID;
    }

    public void setFormID(String formID) {
        FormID = formID;
    }

    public String getResponseData() {
        return ResponseData;
    }

    public void setResponseData(String responseData) {
        ResponseData = responseData;
    }

    public String getIsDraft() {
        return IsDraft;
    }

    public void setIsDraft(String isDraft) {
        IsDraft = isDraft;
    }

    public String getEquipment() {
        return Equipment;
    }
    // this method will generate a unique instanceID

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    // this method will get the posted data from offline store

    public String getFunctionLocation() {
        return FunctionLocation;
    }

    public void setFunctionLocation(String functionLocation) {
        FunctionLocation = functionLocation;
    }

    public String getNonObjType() {
        return NonObjType;
    }

    public void setNonObjType(String nonObjType) {
        NonObjType = nonObjType;
    }

    public String generateID() {
        GregorianCalendar dt;
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        dt = (GregorianCalendar) GregorianCalendar.getInstance();

        String dateString = df.format(dt.getTime()).toString();
        return ("FE" + dateString);
    }

    public String getTaskListType() {
        return TaskListType;
    }

    public void setTaskListType(String taskListType) {
        TaskListType = taskListType;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }

    public String getGroupCounter() {
        return GroupCounter;
    }

    public void setGroupCounter(String groupCounter) {
        GroupCounter = groupCounter;
    }

    public String getInternalCounter() {
        return InternalCounter;
    }

    public void setInternalCounter(String internalCounter) {
        InternalCounter = internalCounter;
    }

    @Override
    public String toString() {
        return this.getFormID() + "\n" + this.getVersion() + "\n" + this.getInstanceID() + "\n";
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }
}
