package com.ods.myjobcard_library;

import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.ConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.lang.reflect.Field;
import java.util.GregorianCalendar;
import java.util.List;

public class ZConfigManager extends ConfigManager {

    public static final int MasterData_BG_Refresh_Interval_Value = 2;
    public static final int MasterData_BG_Refresh_Retry_Interval_In_Min = 5;
    //Background Flush Flags
    public static final String TRANSACTION_WORK = "TransactionWork";
    public static final String PERIODIC_REQUEST = "BackgroundJob";
    //public static String Push_Service_Name = "http://%s:%d/restnotification/registration/";
    public static String Attachment_Service_URL = "http://52.51.40.48:50000/sap/opu/odata/ODS/SAP_WM_DLITE_SRV/";

    //public static String TX_SERVICE_NAME = "";//"S4HTransactionService";//
    public static String SAP_Host = "http://52.30.23.148:8000/";
    public static String Tx_Service_Suffix = "sap/opu/odata/ODS/SAP_WM_DLITE_SRV/";
    public static String TX_SERVICE_NAME = "TransactionService";//"S4HTransactionService";//
    public static String LOW_VOLUME_MD_SERVICE_NAME = "LowVolumeMDService";//"ctRootService";//"S4HLowVolumeService";//
    public static String HIGH_VOLUME_MD_SERVICE_NAME = "HighVolumeMDService";//"ctHighVolumeDev";//"S4HHighVolumeService";//
    public static String APP_SETTINGS_SERVICE_NAME = ZAppSettings.App_ID;//"S4HAppSettingsService";//


    //New System QA or Dev
    /*public static String TX_SERVICE_NAME = "TransactionService";//"S4HTransactionService";//
    public static String LOW_VOLUME_MD_SERVICE_NAME = "LowVolumeMDService";//"ctRootService";//"S4HLowVolumeService";//
    public static String HIGH_VOLUME_MD_SERVICE_NAME = "HighVolumeMDService";//"ctHighVolumeDev";//"S4HHighVolumeService";//
    public static String APP_SETTINGS_SERVICE_NAME = AppSettings.App_ID;//"S4HAppSettingsService";//AppSettings.App_ID
    public static String FORMS_ENGINE_SERVICE_NAME = "FormEngineService";//"S4HFormEngineService";//
    public static String SUPERVISOR_SERVICE_NAME = "SupervisorService";//"ZSupervisorService";*/


    /* public static String TX_SERVICE_NAME = "";//"S4HTransactionService";
     public static String LOW_VOLUME_MD_SERVICE_NAME = "";//"ctRootService";//"S4HLowVolumeService";//
     public static String HIGH_VOLUME_MD_SERVICE_NAME = "";//"ctHighVolumeDev";//"S4HHighVolumeService";//
     public static String APP_SETTINGS_SERVICE_NAME = AppSettings.App_ID;//"S4HAppSettingsService";//
     public static String FORMS_ENGINE_SERVICE_NAME = "";//"S4HFormEngineService";//
     public static String SUPERVISOR_SERVICE_NAME = "";//"ZSupervisorService";*/
    public static String FORMS_ENGINE_SERVICE_NAME = "FormEngineService";//"S4HFormEngineService";//
    public static String SUPERVISOR_SERVICE_NAME = "SupervisorService";//"ZSupervisorService";
    public static String zHighVolumeService = "ZHighVolumeService";
    public static String WO_KEY_FIELD = "WorkOrderNum"; //"com.sap.wmUpd";
    public static String WO_KEY_FIELD1 = "TempID"; //"com.sap.wmUpd";
    public static String OPR_KEY_FIELD1 = "OperationNum"; //"com.sap.wmUpd";
    public static String OPR_KEY_FIELD2 = "SubOperation"; //"com.sap.wmUpd";
    //    public static String OPR_KEY_FIELD2 = "Counter"; //"com.sap.wmUpd";
    public static String PRT_KEY_FIELD1 = "RoutingNum"; //"com.sap.wmUpd";
    public static String PRT_KEY_FIELD2 = "Item"; //"com.sap.wmUpd";
    public static String PRT_KEY_FIELD3 = "Order"; //"com.sap.wmUpd";
    public static String COMPONENT_KEY_FIELD2 = "Item"; //"com.sap.wmUpd";
    public static String COMPONENT_KEY_FIELD1 = "OperAct"; //"com.sap.wmUpd";
    public static String NOTIFICATION_KEY_FIELD = "Notification"; //"com.sap.wmUpd";
    public static String NOTIFICATION_TASK_KEY_FIELD = "Task";
    public static String NOTIFICATION_ACTIVITY_KEY_FIELD = "Activity";
    public static String NOTIFICATION_ITEM_KEY_FIELD = "Item";
    public static String NOTIFICATION_ITEM_CAUSE_KEY_FIELD = "Cause";
    public static String FORMS_RESPONSE_CAPTURE_KEY_FIELD = "InstanceID";
    public static String FORM_ASSIGNMENT_TYPE = "3";
    public static String WONUM_VALUE_PREFIX = "L00";
    public static String LOCAL_IDENTIFIER = "Temp";
    public static String LOCAL_ID = "L"; //"com.sap.wmUpd";
    public static int MAX_LONGTEXT_LINE_LENGTH = 72;
    public static String DEFAULT_ADDRESS_TYPE = "VN";
    public static String ADDRESS_SEPARATOR = ", ";
    public static boolean GOOGLE_MAP_API_CALL_ENABLED = true; // added by Shubham
    public static boolean POST_AUTO_TIME_ENTRY = true;
    public static boolean POST_CONFIRMATIONS = true;
    public static boolean OBJECT_TYPE_GENERAL_FORM = true;
    public static boolean POST_CATS_TIMESHEET = false;
    public static boolean CAPTURE_TIME_ON_WORK_HALT = true;
    public static boolean OPERATION_LEVEL_ASSIGNMENT_ENABLED = false;
    public static String WO_OP_OBJS_DISPLAY = "X";
    public static ZAppSettings.MobileStatus WO_ACTIVE_STATUS_DISABLED = ZAppSettings.MobileStatus.ENROUTE;
    public static String DEFAULT_ASSIGNMENT_TYPE = ZAppSettings.AssignmentType.WorkOrderLevel.getAssignmentTypeText();
    public static ZAppSettings.AssignmentType ASSIGNMENT_TYPE = ZAppSettings.AssignmentType.WorkOrderLevel;
    public static ZAppSettings.NotificationAssignmentType NOTIFICATION_ASSIGNMENT_TYPE = ZAppSettings.NotificationAssignmentType.PersonelNumber;
    public static boolean CREATE_WORKORDER_WITH_OPERATION = false;
    public static boolean SHOW_OPERATION_NOTES_IN_WO = true;
    public static boolean SHOW_STATUS_CHANGE_ALERT = true;
    public static boolean SHOW_LOGOUT_ALERT = true;
    public static boolean ATTACHMENT_REQUIRED = true;
    public static boolean OPERATION_COMPLETION_REQUIRED = true;
    public static boolean COMPONENT_ISSUE_REQUIRED = true;
    public static boolean MANDATORY_FORMS_REQUIRED = true;
    public static boolean MPOINT_READING_REQUIRED = true;
    public static boolean ENABLE_SUPERVISOR_READING_ALERT = true;
    public static boolean ENABLE_OPERATION_MEASUREMENTPOINT_READINGS = true;
    public static boolean ENABLE_SIGNATURE_CAPTURE_ON_COMPLETION = true;
    public static boolean ENABLE_SIMPLIFIED_DASHBOARD = true;
    public static long AUTO_FLUSH_TIME_SPAN = 180000;//in millis
    public static int AUTO_FLUSH_REPEATS = 20;
    public static int TEXT_MAX_LENGTH_SMALL = 30;
    public static int TEXT_MAX_LENGTH_MEDIUM = 40;
    public static int TEXT_MAX_LENGTH_LARGE = 255;
    public static int MAX_MEASUREMENT_DOC_COUNT = 5;
    public static int MAX_RECORDS_LOAD_COUNT = 100;
    public static String USER_ROLE_SUPERVISOR = "SUPERVISOR";
    public static String USER_ROLE_TECHNICIAN = "TECHNICIAN";
    public static String USER_LANDING_SCREEN_CLASSNAME = "dashboardscreen";//"com.ods.myjobcard.UI.assetTree.TreeListActivity";//
    public static String ESRI_ASSET_MAP = "ESRI";
    public static String LATLONGO_ASSET_MAP = "LatLonGo";
    public static String DEFAULT_ASSET_MAP = ESRI_ASSET_MAP;
    public static String ATT_TYPE_HOURS_OF_COSTING = "";
    public static float ALPHA_VAL_UNSELECTED_TAB = .75f;
    public static String DATE_FORMAT = "dd-MM-yyyy";
    public static String TIME_FORMAT = "HH:mm";
    public static String QUERIABLE_DATE_FORMAT = "yyyy-MM-dd'T'00:00:00";
    public static String INVALID_SERVICE_URL = "Invalid";
    public static String INVALID_STORE_ID = "Invalid";
    public static String INVALID_CLASS_NAME = "Invalid";
    public static boolean ENABLE_BG_SYNC = true;
    public static int StoreReopenMaxAttempts = 3;
    public static String AttachmentsFolder = "/ODS/";
    public static String STANDARDTEXT_LINE_SEPARATOR = "L ";
    public static int User_ID_MinLength = 4;
    public static int User_PSWD_MinLength = 5;
    public static int Bitmap_Compression_Quality = 80;
    public static String ACTIVITY_TYPE_TRAVEL_TIME = "PS-06";
    public static String ACTIVITY_TYPE_ACCESS_TIME = "PS-07";
    public static String ACTIVITY_TYPE_WORK_TIME = "PS-08";
    public static String ATTACHMENT_CLASS_TYPE_WORKORDER = "BUS2007";
    public static String ATTACHMENT_CLASS_TYPE_NOTIFICATION = "BUS2038";
    public static String ATTACHMENT_CLASS_TYPE_EQUIPMENT = "";
    public static String ATTACHMENT_CLASS_TYPE_FUNCTIONAL_LOCATION = "";
    public static String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public static float MAX_UPLOAD_FILE_SIZE = 16f;
    public static float OFFLINE_UPLOAD_MAX_FILE_SIZE = 3f;
    public static GregorianCalendar DEFAULT_CALENDAR_VAL;
    public static String STATUS_SET_FLAG = "X";
    public static String STATUS_UNSET_FLAG = "null";
    public static boolean AUTO_NOTES_ON_STATUS = false;
    public static String AUTO_NOTES_TEXT_LINE1 = "Status Updated to: ";
    public static String AUTO_NOTES_TEXT_LINE2 = " by: ";
    public static String AUTO_NOTES_TEXT_LINE3 = " At: ";
    public static String AUTO_NOTES_TEXT_LINE4 = " Reason: ";
    public static String AUTO_NOTES_TEXT_LINE5 = "Transferred To:";
    public static boolean ENABLE_POST_DEVICE_LOCATION_NOTES = true;
    public static int MAX_PUSH_TOKEN_GENERATION_ATTEMPTS = 3;
    public static boolean ENABLE_HYBRID_FORM = true;
    public static boolean PARTIAL_COMPONENT_ISSUE_ALLOWED = true;
    public static String RECORDS_POINT_MANDATORY_FLAG = "S";
    public static String DEFAULT_STATUS_TO_CHANGE = "CRTD,ASGD";
    public static String OPR_INSP_ENABLE_STATUS = "ICHA";
    public static String OPR_INSP_RESULT_RECORDED_STATUS = "QMDA";
    public static String DEFAULT_STATUS_TO_SEND = "MOBI";
    public static int Device_Log_Auto_Deletion_Days = 30;
    public static int TIMESHEET_FETCH_INTERVAL = 15;
    public static boolean ENABLE_WORKCENTER_GROUP_ID_LOGIN = false;
    public static boolean SHOW_FORM_NOTIFICATION = false;
    public static boolean SHOW_ROLE_BASED_FEATURES = false;
    public static boolean ENABLE_NEW_FE = true;
    public static String OPERATION_STATUS_TO_MARK_INCOMPLETE = "CRTD";
    public static String DEFAULT_PREMIUM_ID = "";
    public static String DEFAULT_PREMIUM_NO = "";
    public static boolean POST_FORM_ATTACHMENT_IN_WO = false;
    public static int MATERIALSTOCK_TOP = 5000;
    public static boolean ENABLE_AUTO_BG_SYNC = true;
    public static int ONLINE_CHECK_NOTIFICATION_DATE_SPAN = 7;
    public static int ONLINE_CHECK_WORKORDER_DATE_SPAN = 7;
    public static boolean ENABLE_CAPTURE_TEAM_TIMESHEET = true;
    public static boolean ENABLE_CAPTURE_DURATION = true;
    public static String USER_STORE_NAME = "USERSTORE";
    public static boolean isLogLevelSelect = false;
    public static int SELECTED_LOG_LEVEL = 0;
    public static String MasterDataRefresh_TargetDate = "TargetDate";
    public static String MASTER_DATA_REFRESH_TAG = "MasterDataRefresh";
    public static boolean MasterData_BG_Refresh_Enable = true;
    public static int MasterData_BG_Refresh_Retry_Attempts = 1;
    public static boolean MasterData_BG_Refresh_Unit_In_Hours = true;
    public static boolean ENABLE_LOCAL_STATUS_CHANGE = true;
    public static boolean ENABLE_LOCAL_NO_TO_WO = true;

    public static long BG_SYNC_TIME_INTERVAL = 8;
    public static int BG_SYNC_RETRY_INTERVAL = 2;
    public static int BG_SYNC_RETRY_COUNT = 2;
    public static String EventBased_Sync = "x";
    public static String TimeBased_Sync = "x";
    public static int EventBased_Sync_Type = 1;
    public static int TimeBased_Sync_Type = 2;
    public static String DOWNLOAD_CREATEDBY_NOTIF = "X";
    public static String DOWNLOAD_CREATEDBY_WO = "X";
    public static boolean isBGFlushInProgress = false;

    public static GregorianCalendar getDefaultCalendarVal() {
        try {
            DEFAULT_CALENDAR_VAL = (GregorianCalendar) GregorianCalendar.getInstance();
            DEFAULT_CALENDAR_VAL.setTimeInMillis(31516200000l);
        } catch (Exception e) {
            DliteLogger.WriteLog(ConfigManager.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return DEFAULT_CALENDAR_VAL;
    }

    public enum Fetch_Object_Type {
        WorkOrder,
        Operation,
        PRT,
        Components,
        WONotification,
        WONotificationItems,
        WONotificationActivity,
        WONotificationTasks,
        WONotificationItemCauses,
        WOConfirmation,
        Notification,
        NotificationItems,
        NotificationActivity,
        NotificationTasks,
        NotificationItemCauses,
        UserTimeSheet,
        PartnerAddress,
        Address,
        WOHistoryPending,
        NotificationHistoryPending,
        ObjectList,
        WOLongText,
        FLCharacteristics,
        FLClassificationSet,
        FLMeasurementPoint,
        EquipmentCharacteristics,
        EquipmentClassificationSet,
        EquipmentMeasurementPoint,
        AttachmentConnection,
        AttachmentComponent,
        AttachmentContent,
        PlanningPlant, //added by Shubham
        WorkCenter,     //added by Shubham
        Priority,        //added by Shubham
        Equipment,      //added by Shubham
        FunctionalLocation,  //added by Shubham
        ControlKey,  //added by Shubham
        WorkOrderType,  //added by Shubham
        FormSetModel, //added by Shubham
        ResponseMasterModel,
        FormAssignmentSetModel
    }


    public enum Tab_Name {
        TAB_WO_OVERVIEW,
        TAB_WO_OPERATIONS,
        TAB_WO_PARTS,
        TAB_WO_ATTACHMENTS,
        TAB_WO_FORMS,
        TAB_WO_HISTORY,

        TAB_NOTF_OVERVIEW,
        TAB_NOTF_ACTIVITIES,
        TAB_NOTF_ITEMS,
        TAB_NOTF_TASKS,
        TAB_NOTF_ATTACHMENTS,

        TAB_EQP_OVERVIEW,
        TAB_EQP_CLASSIFICATIONS,
        TAB_EQP_MEASURING_POINTS,

        TAB_FL_OVERVIEW,
        TAB_FL_CLASSIFICATIONS,
        TAB_FL_MEASURING_POINTS
    }
    public static void setAppConfigurations(){
        try{
            ResponseObject result = DataHelper.getInstance().getEntities(ZCollections.APPLICATION_CONFIG_COLLECTION, ZCollections.APPLICATION_CONFIG_COLLECTION);
            if(result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                for (ODataEntity entity : entities) {
                    try {
                        String keyName = String.valueOf(entity.getProperties().get("Key").getValue());
                        Class cls = ZConfigManager.class;
                        Field field = cls.getField(keyName);
                        String value = String.valueOf(entity.getProperties().get("Value").getValue());
                        if (field != null && value != null) {
                            if(field.getType() == boolean.class)
                                field.set(null, Boolean.parseBoolean(value));
                            else if(field.getType() == int.class)
                                field.set(null, Integer.valueOf(value));
                            else if(field.getType() == long.class)
                                field.set(null, Long.valueOf(value));
                            else if(field.getType() == float.class)
                                field.set(null, Float.valueOf(value));
                            else if(field.getType() == ZAppSettings.MobileStatus.class)
                                field.set(null, ZAppSettings.MobileStatus.valueOf(value.substring((value.lastIndexOf(".")+1))));
                            else if(field.getType() == String.class) {
                                if(field.getName().equalsIgnoreCase("Push_Service_Name"))
                                    //todo replace Push_Service_Name with 'value' after the updated add-on installed on HANA
                                    field.set(null, String.format(value, ZAppSettings.App_IP, ZAppSettings.App_Port));
                                else
                                    field.set(null, value);
                            }
                        }
                    }
                    catch (Exception e){
                        DliteLogger.WriteLog(ZConfigManager.class, ZAppSettings.LogLevel.Warning, e.getMessage());
                    }
                }
            }
        }
        catch (Exception e){
            DliteLogger.WriteLog(ZConfigManager.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

}
