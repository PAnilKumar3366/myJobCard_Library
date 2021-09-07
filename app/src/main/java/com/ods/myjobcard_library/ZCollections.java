package com.ods.myjobcard_library;

import com.ods.ods_sdk.Collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ZCollections extends Collections {

	public static final String Service_Name = "ODS_SAP_WM_DLITE_SRV.";//"ZODS_WORKMANAGER_SERVICE_SRV.";//"SAP_WM_DLITE_SRV";// "ZODS_WORKMANAGER_SERVICE_SRV.";
	public static final String FE_Service_Name = "ODS_FORM_ENGINE_SRV.";//"ZODS_WORKMANAGER_SERVICE_SRV.";//"SAP_WM_DLITE_SRV";// "ZODS_WORKMANAGER_SERVICE_SRV.";
	static String getFormsEngineServiceName(){
		if(ZConfigManager.ENABLE_NEW_FE)
			return "ODSMFE_PR_FORMUI_SRV.";
		return FE_Service_Name;
	}
	public static final String APPLICATION_STORE_COLLECTION = "APLLICATIONSTORE";
	public static final String APP_STORE_COLLECTION = "AppStoreSet";
	public static final String SERVICE_CONFIG_COLLECTION = "ServiceConfigSet";
	public static final String APPLICATION_CONFIG_COLLECTION = "ApplicationConfigSet";
	public static final String APPLICATION_FEATURE_COLLECTION = "ApplicationFeatureSet";
	public static final String CHANGE_PASSWORD_COLLECTION = "ChangePasswordSet";
	public static final String CHANGE_PASSWORD_ENTITY_TYPE = "ODS_SAP_WM_AP_DLITE_SRV.ChangePassword";
	public static final String VK_APPUID = "appuid";
	public static final String VK_APPPWD = "apppwd";

	//Replacing Activities
	public static final String NOTIFICATION_ACTIVITY = "notificationlistactivity";
	public static final String NOTIFICATION_ITEM_DETAIL_ACTIVITY = "notificationitemlistactivity";
	public static final String WORKORDER_LIST_ACTIVITY = "workorderlistactivity";
	public static final String WORKORDER_WITHOPERATION_LIST_ACTIVITY = "workorderswithoperationslistactivity";
	public static final String TEAM_ACTIVITY = "teamactivity";
	public static final String NOTIFICATION_DETAIL_ACTIVITY = "notificationdetailactivity";
	public static final String NOTIFICATION_DOCS_FRAGMENT = "notificationdocsfragment";
	public static final String COMPONENTS_DETAILS_FRAGMENT = "componentsdetailsfragment";
	public static final String JOB_ADD_EDIT_DIALOG = "jobaddeditdialog";

	public static final String EQUIPMENT_OVERVIEW_FRAGMENT = "equipmentoverviewfragment";
	public static final String SUPERVISOR_VIEW_ACTIVITY = "supervisorviewactivity";
	public static final String NOTIFICATION_LIST_ACTIVITY = "notificationlistactivity";

	public static final String PLAN_PLANT_COLLECTION = "LTPlanningPlantSet";
	public static final String WORK_CENTER_COLLECTION = "LTWorkCenterSet";
	public static final String PRIORITY_COLLECTION = "LTPrioritySet";
	public static final String WOTYPE_COLLECTION = "LTWorkOrderTypeSet";
	public static final String CTRL_KEY_COLLECTION = "LTControlKeySet";
	public static final String ATTENDANCE_TYPE_COLLECTION = "LTAttendanceTypeSet";
	public static final String ACTIVITY_TYPE_COLLECTION = "LTActivityTableSet";
	public static final String PM_ACTIVITY_TYPE_COLLECTION = "LTPMActivitySet";
	public static final String WORKORDER_STATUS_COLLECTION = "WorkOrderStatusSet";
	public static final String REASON_CODE_COLLECTION = "LTReasonCodeSet";
	public static final String LABOUR_CODE_COLLECTION = "LTPersonRespSet";
	public static final String ORDER_TYPE_CONTROL_KEY_COLLECTION = "LTOrderControlKeySet";
	public static final String WORKFLOW_COLLECTION = "LTWorkflowSet";

	public static final String FORMS_COLLECTION = "FormMasterSet";
	public static final String FORMS_RESPONSE_CAPTURE_COLLECTION = "ResponseCaptureSet";
	public static final String FORM_ASSIGNMENT_COLLECTION = "FormAssingmentSet";

	public static final String BUSINESS_AREA_COLLECTION = "LTBusinessAreaSet";
	public static final String BUSINESS_PARTNER_COLLECTION = "  ";
	public static final String NOTIFICATION_TYPE_COLLECTION = "LTNotificationTypeSet";
	public static final String PLANNER_GROUP_COLLECTION = "      ";
	public static final String PLANT_SECTION_COLLECTION = "        ";
	public static final String STORAGE_LOCATION_COLLECTION = "LTStorageLocationSet";
	public static final String USER_TABLE_COLLECTION = "AppUserTableSet";
	public static final String CATALOG_PROFILE_COLLECTION = "LTCatalogProfileSet";
	public static final String CATALOG_CODE_COLLECTION = "LTCatalogCodeSet";

	public static final String MATERIAL_STORAGE_COLLECTION = "HTMaterialStorageLocationSet";

	public static final String FL_COLLECTION = "FunctionalLocationHeaderSet";
	public static final String FL_CHARACTERISTICS_COLLECTION = "FunctionalLocationCharateristicsSet";
	public static final String FL_CHARACTERISTICS_ENTITY_TYPE = "FunctionalLocationCharateristics";
	public static final String FL_CLASSIFICATION_SET_COLLECTION = "FunctionallocationClassificationSet";
	public static final String FL_CLASSIFICATION_ENTITY_TYPE = "FunctionallocationClassification";
	public static final String FL_MEASUREMENT_POINT_COLLECTION = "FunctionalLocationMeasurementPointSet";

	public static final String EQUIPMENT_COLLECTION = "EquipmentHeaderSet";
	public static final String EQUIPMENT_ENTITY_TYPE = "ODS_SAP_WM_HT_DLITE_SRV.EquipmentHeader";
	public static final String EQUIPMENT_CHARACTERISTICS_ENTITY_TYPE = "EquipmentCharateristics";
	public static final String EQUIPMENT_CHARACTERISTICS_COLLECTION = "EquipmentCharateristicsSet";
	public static final String EQUIPMENT_CLASSIFICATION_SET_COLLECTION = "EquipmentClassificationSet";
	public static final String EQUIPMENT_CLASSIFICATION_ENTITY_TYPE = "EquipmentClassification";
	public static final String QM_RESULT_TABLE_COLLECTION = "LTQmResultTableSet";
	public static final String QM_RESULT_TABLE_COLLECTION_ENTITY_TYPE = "LTQmResultTable";
	public static final String EQUIPMENT_MEASUREMENT_POINT_COLLECTION = "EquipmentMeasurementPointSet";
	public static final String EQUIPMENT_CATEGORY_COLLECTION = "EquipmentCategorySet";
	public static final String MEASUREMENT_DOCUMENT_HISTORY_COLLECTION = "MeasurementDocumentHistorySet";

	public static final String CLASS_CHARACTERISTICVALUE_ENTITY_TYPE = "ClassCharacteristicValue";
	public static final String CLASS_CHARACTERISTICVALUE_SET_COLLECTION = "ClassCharacteristicValueSet";

	public static final String MATERIAL_PLANT_COLLECTION = "HTMaterialPlantSet";
	public static final String ASSET_MAPPING_COLLECTION = "HTGISAssetMappingSet";
	public static final String ASSET_HIERARCHY_ENTITY_SET = "HTAssetHierarchySet";
	public static final String ASSET_HIERARCHY_ENTITY_TYPE = "ODS_SAP_WM_HT_DLITE_SRV.HTAssetHierarchy";

	public static final String WO_COLLECTION = "WoHeaderSet";// "WoHeaderServiceSet";//"WoHeaderSet";// "WOHeaderCollection";
	public static final String OPR_COLLECTION = "WOOperationCollection";// "WOOperationCollection";
	public static final String PRT_COLLECTION = "WOPRTCollection";
	public static final String PARTNER_ADDR_COLLECTION = "WoPartnerAddressSet";
	public static final String ADDR_COLLECTION = "AddressSet";
	public static final String COMPONENT_COLLECTION = "WoComponentSet";
	public static final String WO_HISTORY_COLLECTION = "WoHistorySet";
	public static final String WO_PENDING_COLLECTION = "WoPendingSet";
	public static final String WO_OBJECTS_COLLECTION = "WoObjectSet";
	public static final String WO_LONG_TEXT_COLLECTION = "WoLongTextSet";
	public static final String WO_HISTORY_PENDING_LONG_TEXT_COLLECTION = "WOHistoryPendingLongTextSet";
	public static final String WO_CONFIRMATION__COLLECTION = "WOConfirmationSet";
	public static final String WO_LONG_TEXT_ENTITY_TYPE = Service_Name + "WOLongText";
	public static final String MEASPOINT_READING_COLLECTION = "MeasurementPointReadingSet";
	public static final String WO_COMPONENT_ISSUE_COLLECTION = "WOComponentIssueSet";
	public static final String WO_COMPONENT_ISSUE_ENTITY_TYPE = Service_Name + "WOComponentIssue";
	public static final String INSTALL_EQUIPMENT_COLLECTION = "InstallEquipmentSet";
	public static final String DISMANTLE_EQUIPMENT_COLLECTION = "EquipmentDismantleSet";
	public static final String INSTALL_EQUIPMENT_ENTITY_TYPE = Service_Name + "InstallEquipment";
	public static final String DISMANTLE_EQUIPMENT_ENTITY_TYPE = Service_Name + "EquipmentDismantle";
	public static final String VALID_WO_COLLECTION = "ValidWoListSet";
	public static final String VALID_OPERATION_COLLECTION = "ValidWoOpListSet";
	public static final String VALID_SUP_WORKORDER_COLLECTION = "ValidSupWoListSet";
	public static final String VALID_SUP_OPERATION_COLLECTION = "ValidSupOpListSet";
    public static final String STATUS_CHANGE_LOG_COLLECTION = "StatusChangeLogSet";
    public static final String STATUS_CHANGE_LOG_ENTITY_TYPE = Service_Name + "StatusChangeLog";
	public static final String STATUS_CATEGORY_ENTITY_TYPE = "StatusCategory";
	public static final String STATUS_CATEGORY_SET_COLLECTION = "StatusCategorySet";
	public static final String LT_PREMIUM_ID_SET = "LTPremiumIDSet";
	public static final String LT_PREMIUM_ID_ENTITY_TYPE = "LTPremiumID";
	public static final String LT_AllowedFollowOnObjectType="LTAllowedFollowOnObjectType";
	public static final String LT_AllowedFollowOnObjectTypeSet="LTAllowedFollowOnObjectTypeSet";
	public static final String LT_ATTACHMENT_TYPE_SET = "LTAttachmentTypeSet";
	public static final String LT_ATTACHMENT_TYPE_ENTITY = "LTAttachmentType";


	public static final String USER_COLLECTION = "UserSet";

	public static final String STANDARDTEXT_SET = "StandardTextSet";
	public static final String STASTUS_PROFILE_SET = "LTStatusProfileSet";
	public static final String STATUS_PROFILE_ENTITY_TYPE = "LTStatusProfile";

	public static final String FORM_ATTACHMENT_SET = "FormAttachmentSet";
	public static final String FORM_ATTACHMENT_ENTITY_TYPE = getFormsEngineServiceName() + "FormAttachment";

	public static final String LONG_TEXT_TYPE_WO = "AUFK";
	public static final String LONG_TEXT_TYPE_OPERATION = "AVOT";
	public static final String LONG_TEXT_TYPE_COMPONENT = "MATK";
	public static final String LONG_TEXT_TYPE_NOTIFICATION = "QMEL";
	public static final String LONG_TEXT_TYPE_NOTIFICATION_ACTIVITY = "QMMA";
	public static final String LONG_TEXT_TYPE_NOTIFICATION_ITEM = "QMFE";
	public static final String LONG_TEXT_TYPE_NOTIFICATION_TASK = "QMSM";
	public static final String LONG_TEXT_TYPE_NOTIFICATION_ITEM_CAUSE = "QMUR";

	public static final String USER_TIMESHEET_COLLECTION = "GETCATSRecordSet";
	public static final String USER_TIMESHEET_ENTITY_TYPE = Service_Name + "GETCATSRecods";

	public static final String PARTNER_ADDR_ENTITY_TYPE = Service_Name + "WOPartnerAddress";


	public static final String WO_ENTITY_TYPE =  Service_Name + "WoHeader";//"ZODS_WORKMANAGER_TEST_SRV_01.WOHeader";// "ZWOGETBAPI_SRV.WOHeader";
	public static final String OPR_ENTITY_TYPE1 = Service_Name + "WOOperation";//"ZODS_WORKMANAGER_TEST_SRV_01.WOOperation"; //"ZWOGETBAPI_SRV.WOOperation";
	public static final String PRT_ENTITY_TYPE = Service_Name + "WOPRT";
	public static final String COMPONENT_ENTITY_TYPE = Service_Name + "WOComponent";
	public static final String WO_HISTORY_ENTITY_TYPE = Service_Name + "WOHistory";
	public static final String WO_PENDING__ENTITY_TYPE = Service_Name + "WOPending";
	public static final String WO_OBJECTS__ENTITY_TYPE = Service_Name + "WOObjectList";
	public static final String WO_NOTIFICATION_COLLECTION = "WoNotificationHeaderSet";
	public static final String WO_NOTIFICATION_ENTITY_TYPE = Service_Name + "WONotificationHeader";
	public static final String WO_NOTIFICATION_ACTIVITY_COLLECTION = "WONotificationActivityCollection";
	public static final String WO_NOTIFICATION_ACTIVITY_ENTITY_TYPE = Service_Name + "WONotificationActivity";
	public static final String WO_NOTIFICATION_ITEMS_COLLECTION = "WONotificationItemCollection";
	public static final String WO_NOTIFICATION_ITEMS_ENTITY_TYPE = Service_Name + "WONotificationItem";
	public static final String WO_NOTIFICATION_ITEM_CAUSES_COLLECTION = "WONotificationItemCausesCollection";
	public static final String WO_NOTIFICATION_ITEM_CAUSES_ENTITY_TYPE = Service_Name + "WONotificationItemCauses";
	public static final String WO_NOTIFICATION_TASKS_COLLECTION = "WONotificationTaskCollection";
	public static final String WO_NOTIFICATION_TASKS_ENTITY_TYPE = Service_Name + "WONotificationTask";
	public static final String FORMS_RESPONSE_CAPTURE_ENTITY_TYPE = getFormsEngineServiceName() + "ResponseCapture";
	public static final String REVIEWER_FORM_RESPONSE_ENTITY_TYPE = getFormsEngineServiceName() + "ReviewerFormResponse";
	public static final String REVIEWER_FORM_RESPONSE_ENTITY_SET = "ReviewerFormResponseSet";

	public static final String WO_CONFIRMATION_ENTITY_TYPE = Service_Name + "WOConfirmation";
	public static final String MEASPOINT_READING_ENTITY_TYPE = Service_Name + "MeasurementPointReading";
	public static final String ACTIVITIES_COUNT = "ACTIVITY_COUNT";
	public static final String TASKS_COUNT = "TASKS_COUNT";
	public static final String ITEMS_COUNT = "ITEMS_COUNT";
	public static final String ITEMS_CAUSE_COUNT = "ITEMS_CAUSE_COUNT";

	public static final String SUPERVISOR_TEAM_MEMBERS_COLLECTIONS = "SupervisorTechnicianSet";
	public static final String SUPERVISOR_WO_COLLECTIONS = "SupervisorWoHeaderSet";
	public static final String SUPERVISOR_OPERATION_COLLECTIONS = "SupervisorWOOperationSet";
	public static final String SUPERVISOR_COMPONENT_COLLECTIONS = "SupervisorComponentSet";
	public static final String SUPERVISOR_ADDRESS_COLLECTIONS = "SupervisorAddressSet";
	public static final String SUPERVISOR_TIMESHEET_COLLECTIONS = "SupervisorGETCATSRecordSet";
	public static final String SUPERVISOR_MEASPOINT_READING_COLLECTION = "SupMeasurementPointReadingSet";
	public static final String SUPERVISOR_MEASPOINT_READING_ENTITY_TYPE = "SupMeasurementPointReading";


	public static final String NOTIFICATION_COLLECTION = "NotificationHeaderSet";
	public static final String NOTIFICATION_ENTITY_TYPE = Service_Name + "NotificationHeader";
	public static final String NOTIFICATION_ACTIVITY_COLLECTION = "NotificationActivitySet";
	public static final String NOTIFICATION_ACTIVITY_ENTITY_TYPE = Service_Name + "NotificationActivity";
	public static final String NOTIFICATION_ITEMS_COLLECTION = "NotificationItemSet";
	public static final String NOTIFICATION_ITEMS_ENTITY_TYPE = Service_Name + "NotificationItem";
	public static final String NOTIFICATION_ITEM_CAUSES_COLLECTION = "NotificationItemCauseSet";
	public static final String NOTIFICATION_ITEM_CAUSES_ENTITY_TYPE = Service_Name + "NotificationItemCauses";
	public static final String NOTIFICATION_TASKS_COLLECTION = "NotificationTaskSet";
	public static final String NOTIFICATION_TASKS_ENTITY_TYPE = Service_Name + "NotificationTask";
	public static final String NOTIFICATION_HISTORY_COLLECTION = "NotificationHistorySet";
	public static final String NOTIFICATION_HISTORY_ENTITY_TYPE = Service_Name + "NotificationHistoryPending";
	public static final String NOTIFICATION_PENDING_COLLECTION = "NotificationPendingSet";
	public static final String NOTIFICATION_PENDING_ENTITY_TYPE = Service_Name + "NotificationPending";
	public static final String WONOTIFICATION_LONG_TEXT_COLLECTION = "WONotificationLongTextSet";
	public static final String WONOTIFICATION_LONG_TEXT_ENTITY_TYPE = Service_Name + "WONotificationLongText";
	public static final String NOTIFICATION_LONG_TEXT_COLLECTION = "NotificationLongTextSet";
	public static final String NOTIFICATION_HISTORY_PENDING_LONG_TEXT_COLLECTION = "NOHistoryPendingLongTextSet";
	public static final String NOTIFICATION_LONG_TEXT_ENTITY_TYPE = Service_Name + "NotificationLongText";
	public static final String NOTIFICATION_ADDR_COLLECTION = "NotificationAddressSet";
	public static final String VALID_NOTIFICATION_COLLECTION = "ValidNotifListSet";


	public static final String NO_ATTACHMENT_COLLECTION = "NOAttachmentSet";
	public static final String WO_NO_ATTACHMENT_COLLECTION = "WONOAttachmentSet";
	public static final String WO_ATTACHMENT_COLLECTION = "WoAttachmentSet";
	public static final String ATTACHMENT_CONNECTION_ENTITY_TYPE = Service_Name + "AttachmentConnectionSignature";
	public static final String WO_ATTACHMENT_CONNECTION_SIGNATURE_COLLECTION = "WOAttachConnSignSet";
	public static final String ATTACHMENT_COMPONENT_ENTITY_TYPE = Service_Name + "AttachmentComponent";
	public static final String WO_ATTACHMENT_COMPONENT_COLLECTION = "WOAttachmentComponentSet";
	public static final String ATTACHMENT_CONTENT_ENTITY_TYPE = Service_Name + "AttachmentContent";
	public static final String WO_ATTACHMENT_CONTENT_DOWNLOAD_COLLECTION = "WOAttachContentDownloadSet";
	public static final String WO_ATTACHMENT_CONTENT_COLLECTION = "WOAttachContentSet";
	public static final String WO_ATTACHMENT_CONTENT_UPLOAD_ENTITY_TYPE =  Service_Name + "UploadWOAttachmentContent";//"ZODS_WORKMANAGER_TEST_SRV_01.WOHeader";// "ZWOGETBAPI_SRV.WOHeader";
	public static final String WO_ATTACHMENT_CONTENT_UPLOAD_COLLECTION = "UploadWOAttachmentContentSet";

	public static final String NO_ATTACHMENT_CONNECTION_ENTITY_TYPE = Service_Name + "NOAttachConnSign";
	public static final String NO_ATTACHMENT_CONNECTION_SIGNATURE_COLLECTION = "NOAttachConnSignSet";
	public static final String NO_ATTACHMENT_COMPONENT_ENTITY_TYPE = Service_Name + "NOAttachmentComponent";
	public static final String NO_ATTACHMENT_COMPONENT_COLLECTION = "NOAttachmentComponentSet";
	public static final String NO_ATTACHMENT_CONTENT_ENTITY_TYPE = Service_Name + "NOAttachContentDownload";
	public static final String NO_ATTACHMENT_CONTENT_COLLECTION = "NOAttachContentDownloadSet";
	public static final String NO_ATTACHMENT_CONTENT_UPLOAD_ENTITY_TYPE =  Service_Name + "UploadNOAttachmentContent";//"ZODS_WORKMANAGER_TEST_SRV_01.WOHeader";// "ZWOGETBAPI_SRV.WOHeader";
	public static final String NO_ATTACHMENT_CONTENT_UPLOAD_COLLECTION = "UploadNOAttachmentContentSet";

	public static final String WO_NO_ATTACHMENT_CONNECTION_ENTITY_TYPE = Service_Name + "WONOAttachConnSign";
	public static final String WO_NO_ATTACHMENT_CONNECTION_SIGNATURE_COLLECTION = "WONOAttachConnSignSet";
	public static final String WO_NO_ATTACHMENT_COMPONENT_ENTITY_TYPE = Service_Name + "WONOAttachmentComponent";
	public static final String WO_NO_ATTACHMENT_COMPONENT_COLLECTION = "WONOAttachmentComponentSet";
	public static final String WO_NO_ATTACHMENT_CONTENT_ENTITY_TYPE = Service_Name + "WONOAttachContentDownload";
	public static final String WO_NO_ATTACHMENT_CONTENT_COLLECTION = "WONOAttachContentDownloadSet";

	public static final String WO_INSPECTIONLOT_ENTITY_TYPE="InspectionLot";
	public static final String WO_INSPECTIONLOT_ENTITY_COLLECTION = "InspectionLotSet";
	public static final String WO_INSPECTIONLOT_OPERATION_ENTITY_TYPE = "InspectionOper";
	public static final String WO_INSPECTIONLOT_OPERATION_ENTITY_COLLECTION = "InspectionOperSet";
	public static final String WO_INSPECTIONLOT_POINT_ENTITY_TYPE = "InspectionPoint";
	public static final String WO_INSPECTIONLOT_POINT_ENTITY_COLLECTION = "InspectionPointSet";
	public static final String WO_INSPECTIONLOT_CHARACTERISTIC_ENTITY_TYPE = "InspectionChar";
	public static final String WO_INSPECTIONLOT_CHARACTERISTIC_ENTITY_COLLECTION = "InspectionCharSet";
	public static final String WO_INSPECTIONLOT_RESULTGET_ENTITY_TYPE = "ODS_PR_QM_CALIBRATION_SRV.InspectionResultsGet";
	public static final String WO_INSPECTIONLOT_RESULTGET_COLLECTION = "InspectionResultsGetSet";

	public static final String WO_HISTORY_OPERATION_SET = "WoHistoryOperationSet";
	public static final String WO_HISTORY_OPERATION_ENTITY_TYPE = "WoHistoryOperation";

	public static final String WO_HISTORY_OPERATION_LONG_TEXT_ENTITY_TYPE = "WOHistoryOpLongText";
	public static final String WO_HISTORY_OPERATION_LONG_TEXT_ENTITY_SET = "WOHistoryOpLongTextSet";

	public static final String BREAKDOWN_REPORT_ENTITY_TYPE = "BreakdownReport";
	public static final String BREAKDOWN_REPORT_ENTITY_COLLECTION = "BreakdownReportSet";

	public static final String CAPACITY_LEVEL_ENTITY_TYPE = Service_Name + "WoCapacity";
	public static final String CAPACITY_LEVEL_ENTITY_COLLECTION = "WoCapacitySet";

	public static final String WOHISTORY_COMPONENTS_ENTITY_TYPE = "WOHistoryComponent";
	public static final String WOHISTORY_COMPONENTS_COLLECTION = "WOHistoryComponentSet";

	public static final String ADDR_ENTITY_TYPE = Service_Name + "Address";
	public static final String WO_NO_TASK_ENTITY_TYPE = "NavTask";
	public static final String WO_NO_ACTIVITY_ENTITY_TYPE = "NavAct";
	public static final String WO_NO_ITEM_ENTITY_TYPE = "NavItem";
	public static final String WO_NO_ITEM_CAUSE_ENTITY_TYPE = "NavItemCause";
	public static final String NO_TASK_ENTITY_TYPE = "NavNOTask";
	public static final String NO_ACTIVITY_ENTITY_TYPE = "NavNOAct";
	public static final String NO_ITEM_ENTITY_TYPE = "NavNOItem";
	public static final String NO_ITEM_CAUSE_ENTITY_TYPE = "NavNOItemCause";
	public static final String WO_OPR_NAV_PROPERTY = "NAVOPERA";

	public static final String PUSH_ENTITY_COLLECTION = "SubscriptionCollection";
	public static final String PUSH_ENTITY_TYPE = "ODS_SAP_WM_AP_DLITE_SRV.Subscription";

	public static final String WO_BOMITEM_ENTITY_TYPE = "WoBomItem";
	public static final String WO_BOMITEM_ENTITY_SET = "WoBomItemSet";
	public static final String WO_BOMHEADER_ENTITY_TYPE = "WoBomHeader";

	public static final String ENTITY_SET_KEYS_TYPE = "EntitySetKeys";
	public static final String ENTITY_SET_KEYS_TYPE_SET = "EntitySetKeysSet";


	public static final String OPERATION_COMPLETE_TEXT = "Complete Confirmation";
	public static final String OPERATION_INCOMPLETE_TEXT = "Cancel Confirmation";
	public static final String PRT_ENTITY_TYPE1 = "NavOpera";
	public static final String WO_ENTITY_Number = "Number";
	public static final String WO_ENTITY_Orderid = "Orderid";
	public static final String WO_ENTITY_OrderType = "OrderType";
	public static final String WO_ENTITY_Planplant = "Planplant";
	public static final String WO_ENTITY_BusArea = "BusArea";
	public static final String WO_ENTITY_MnWkCtr = "MnWkCtr";
	public static final String WO_ENTITY_Plant = "Plant";
	public static final String WO_ENTITY_MnWkctrId = "MnWkctrId";
	public static final String WO_ENTITY_Pmacttype = "Pmacttype";
	public static final String WO_ENTITY_FunctLoc = "FunctLoc";
	public static final String WO_ENTITY_Equipment = "Equipment";
	public static final String WO_ENTITY_StartDate = "StartDate";
	public static final String WO_ENTITY_ShortText = "ShortText";
	public static final  String OPR_ENTITY_Activity =  "Activity";
	public static final  String OPR_ENTITY_CtrlKey =  "ControlKey";
	public static final  String OPR_ENTITY_WrkCntr =  "WorkCntr";
	public static final  String OPR_ENTITY_Plant =  "Plant";
	public static final  String OPR_ENTITY_Desc =  "Description";
	public static final String TEMP_ID_PREFIX = "L";
	public static final float Convert_KB_MB = 1024f;
	public static final String WO_LOCATION_SP_NAME = "WO_LOCATION_SP";
	public static final String SERVER_DETAILS_SP_NAME = "ServerDetails_SP";
	public static final String AUTO_TIME_ENTRIES_SP_NAME = "AutoTimeEntries_SP";
	public static final String CURR_WO_SP_NAME = "CurrentWorkOrder_SP";
	public static final String CURR_NOTIF_SP_NAME = "CurrentNotification_SP";
	public static final String Last_Refresh_Time="LastSync";
	public static final String ARG_HOST = "HOST";
	public static final String ARG_PORT = "PORT";
	public static final String ARG_APP_NAME = "APP_NAME";
	public static final String ARG_FCM_TOKEN = "APP_FCM_TOKEN";
	public static final String ARG_IS_HTTPS = "IS_HTTPS";
	public static final String ARG_IS_DEMO_MODE = "IS_DEMO_MODE";
	public static final String ARG_USER_ID = "USER_ID";
	public static final String ARG_IS_HISTORY = "IsHistory";
	public static final String ARG_USER_NEW_PWD = "NEW_PWD";
	public static final String ARG_USER_WORKCENTER = "USER_WORKCENTER";
	public static final String ARG_USER_PLANT = "USER_PLANT";
	public static final String ARG_APP_PUSH_ID = "APPPID";
	public static final String ARG_SECONDARY_USER_FULLNAME = "SECONDARY_USER_FULLNAME";
	public static final String ARG_SECONDARY_USER_ID = "SECONDARY_USER_ID";
	public static final String ARG_OPR_DETAIL_ID = "Operation";
	public static final String ARG_OPR_COUNT = "OPERATIONS_COUNT";
	public static final String ARG_SUB_OPR = "SUB_OPR";
	public static final String ARG_DETAIL_ID = "WoDetail";
	public static final String ARG_COMPONENT_ID = "CURRENT_SELECTED_COMPONENT";
	public static final String ARG_TASK_ID = "CURRENT_SELECTED_TASK";
	public static final String ARG_COMPONENT_COUNT = "COMPONENT_COUNT";
	public static final String ARG_TASK_COUNT = "TASK_COUNT";
	public static final String ARG_NOTIFICATION_DETAIL_ID = "NotificationId";
	public static final String ARG_ACTIVITY_DETAIL_ID = "ActivityId";
	public static final String ARG_TASK_DETAIL_ID = "TaskId";
	public static final String ARG_ITEM_DETAIL_ID = "ItemId";
	public static final String ARG_ITEM_CAUSE_DETAIL_ID = "CauseId";
	public static final String ARG_STATUS = "StatusDesc";
	public static final String ARG_IS_SUSP_STATUS = "IsSuspStatus";
	public static final String ARG_WOs_COUNT_ID = "WOCount";
	public static final String ARG_IS_FROM_NOTIFICATION_ITEM = "isFromNotificationItem";
	public static final String ARG_IS_WO_NOTIFICATION = "isFromWONotification";
	public static final String ARG_WO_OPRs_COUNT_ID = "WOOprCount";
	public static final String ARG_LAST_SYNC_TIME = "LastSync";
	public static final String ARG_LAST_MASTER_DATA_SYNC_TIME = "LastMasterDataSync";
	public static final String ARG_SUPERIOR_EQUIPMENT_ID = "SuperiorEquipmentId";
	public static final String ARG_FUNCTION_LOCATION_ID = "FunctionalLocationId";
	public static final String ARG_ENROUTE_TIME = "EnrouteTime";
	public static final String ARG_ONSITE_TIME = "OnSiteTime";
	public static final String ARG_START_TIME = "StartTime";
	public static final String ARG_NOTIFICATION = "notification";
	public static final String ARG_OPEN_CREATE_JOB_SCREEN = "openNotificationScreen";
	public static final String ARG_WORK_START_TIME = "WST";
	public static final String ARG_WORK_END_TIME = "WFT";
	public static final String ARG_WORK_DURATION = "duration";
	public static final String ARG_ACTIVITY_TYPE = "activity_type";
	public static final String ARG_CATS_FINAL_CONFIRMATION = "CATS_FINAL_CONFIRMATION";
	public static final String BROADCAST_ACTION = "com.ods.myjobcard.uploadAttachment.BROADCAST";
	//public static final String ARG_ITEM_ID = "item_id";
	public static final String EXTENDED_DATA_STATUS = "com.ods.myjobcard.uploadAttachment.STATUS";
	public static final String CARRIER_COLLECTION = "CarrierCollection";
	public static final String CARRIER_ENTRY_ID = "carrid";
	public static final String CARRIER_ENTRY_NAME = "CARRNAME";
	public static final String CARRIER_ENTRY_URL = "URL";
	public static final String LT_FEATURESET = "LTFeatureSet";
	public static final String LT_ORDERTYPEFEATURESET = "LTOrderTypeFeatureSet";
	//	Mobile Object Statuses
	public static final String STATUS_RECEIVED = "RECEIVED";
	public static final String STATUS_ACCEPT = "ACCEPT";
	public static final String STATUS_ENROUTE = "ENROUTE";
	public static final String STATUS_ONSITE = "ONSITE";
	public static final String STATUS_START = "STARTED";
	public static final String STATUS_HOLD = "HOLD";
	public static final String STATUS_COMPLETE = "COMPLETE";
	//CT Search List Options
	public static final String SEARCH_OPTION_ID = "Id";
	public static final String SEARCH_OPTION_DESCRIPTION = "Description";
	public static final String SEARCH_OPTION_NAME = "Name";
	public static final String SEARCH_OPTION_TECH_ID = "TechIdentNo";
	public static final String SEARCH_ITEM_PLANT = "Plant";
	public static final String SEARCH_ITEM_STORAGE = "Storage Location";
	public static final String INVALID_SERVICE_URL = "Invalid";
	public static final String SEARCH_VERSION_ID = "Version";
	public static final String SEARCH_FORM_NAME = "Form Name";
	public static final String SEARCH_FORM_CATEGORY = "Category";
	public static final String SEARCH_APPROVER_DEPT = "Department";
	//public static final String INVALID_STORE_ID = "Invalid";
	public static final boolean ENABLE_BG_SYNC = true;
	public static final String SUPERVISOR_USER_COLLECTIONS = "UserSet";

	public static final String multiDataIdentifier = "mjc_$vm";
	public static final String singleDataIdentifier = "mjc_$vsco";
	public static final String readValueIdentifier = "mjc_vr";
	public static final String formResponseIdentifier = "mjc_vr_form";
	public static final String createNotificationIdentifier = "mjc_create_notification";
	public static final String entityIdentifier = "$e";
	public static final String queryParameterIdentifier = "$qp";
	public static final String objectIdentifier = "_p";
	public static final String readFieldIdentifier = "_f";
	public static final String fieldIdentifier = "$f";
	public static final String objectTypeIdentifier = "$t";
	public static final String fieldSeparator = ",";
	public static final String instanceTag = "instance";
	public static final String descriptionIdentifier = "$mjc$";
	public static final String valueIdentifier = "-999";
	public static final String multiDataItemFormat = "<item><label>$mjc$</label><value>-999</value></item>"; // for forms designed in kobo
	public static final String multiDataItemFormat2 = "<option value=\\\"-999\\\">$mjc$</option>"; // for forms designed in vellam
	public static final String singleDataItemFormat = "<item><label>$mjc$</label><value>-999</value></item>";

	public final static int STATUS_CHANGE_FORM_TRIGGERED = 998;
	public final static int CREATE_NEW_JOB_FROM_FORM = 999;

	//OnlineSearch
	public static boolean isUnAssigned = false;
	public static HashMap<String, String> REFRESH_SERVICES = new HashMap<>();
	public static UUID OneTimeRequestId;
	public static final String GenericNetworkError = "Unknown Network Error";

	public static final String FORM_MASTER_METADATA_ENTITY_SET = "FormMasterMetadataSet";
	public static final String FORM_MASTER_METADATA_ENTITY_TYPE = "FormMasterMetadata";
	public static final String FROM_APPROVER_ENTITY_SET = "FormApproverSet";
	public static final String FORM_APPROVER_ENTITY_TYPE = getFormsEngineServiceName() + "FormApprover";
	public static final String APPROVER_MASTER_DATA_ENTITY_SET = "ApproverMasterDataSet";
	public static final String APPROVER_MASTER_DATA_ENTITY_TYPE = "ApproverMasterData";
	public static final String DEPT_MASTER_DATA_ENTITY_SET = "DeptMasterDataSet";
	public static final String DEPT_MASTER_DATA_ENTITY_TYPE = "ApproverMasterData";
	public static final String FORM_RESPONSE_APPROVAL_STATUS_ENTITY_SET = "FormResponseApprovalStatusSet";
	public static final String FORM_RESPONSE_APPROVAL_ENTITY_TYPE = getFormsEngineServiceName() + "FormResponseApprovalStatus";
	public static final String FORM_MANUAL_ASSIGNMENT_ENTITY_SET = "FormManualAssignmentSet";
	public static final String FORM_MANUAL_ASSIGNMENT_ENTITY_TYPE = getFormsEngineServiceName() + "FormManualAssignment";
	public static final String FORM_IMAGE_ENTITY_SET = "FormImageSet";
	public static final String FORM_IMAGE_ENTITY_TYPE = getFormsEngineServiceName() + "FormImage";

	public static String getEditResourcePath(String collection, String key) {
		return String.format("%s('%s')", collection, key);

	}

	public static ArrayList<String> getCTSearchOptions() {
		ArrayList<String> options = new ArrayList<>();
		options.add(SEARCH_OPTION_ID);
		options.add(SEARCH_OPTION_DESCRIPTION);
		return options;
	}

	public static ArrayList<String> getResourceSearchOptions() {
		ArrayList<String> options = new ArrayList<>();
		options.add(SEARCH_OPTION_ID);
		options.add(SEARCH_OPTION_NAME);
		return options;
	}

	public static ArrayList<String> getApproversSearchOptions() {
		ArrayList<String> options = new ArrayList<>();
		options.add(SEARCH_OPTION_ID);
		options.add(SEARCH_OPTION_NAME);
		options.add(SEARCH_APPROVER_DEPT);
		return options;
	}

	public static ArrayList<String> getCheckListSearchOptions() {
		ArrayList<String> options = new ArrayList<>();
		options.add(SEARCH_FORM_NAME);
		options.add(SEARCH_FORM_CATEGORY);
		return options;
	}
}
