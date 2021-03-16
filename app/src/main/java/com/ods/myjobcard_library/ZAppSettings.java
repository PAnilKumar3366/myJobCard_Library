package com.ods.myjobcard_library;

import android.content.Context;

import com.ods.ods_sdk.AppSettings;

import java.util.ArrayList;
import java.util.HashMap;

public class ZAppSettings extends AppSettings {
    // public static String App_Name = "com.ods.myJobCard"; //"com.sap.wmUpd";
    // public static String App_ID = "com.ods.myJobCard";//"com.sap.wmNupd"; //"com.sap.wmDlite"; //"com.sap.wmWin";// "com.sap.wmNupd"; //"com.sap.wmUpd";
    /*//added by Shubham
    public static String App_LowVol_CT_Service_Root = "ctRootService";
    public static String App_HighVol_CT_ServiceRoot = "HighVolumeCTService";
    //end by Shubham*/
    public static String App_Asset_Service_Root = "Equipment"; //"com.sap.wmUpd";
    public static String HTML_FOMRS_PATH = ""; //"com.sap.wmUpd";
    //public static String App_IP = "mobile-a53d86cd7.hana.ondemand.com";//"52.166.195.165";//"52.18.115.57"; // "192.168.0.52";
    // public static int App_Port = 443; //8080;
    public static int Log_Level = 0;
    public static Context context = null;
    public static String strPrimaryUser;
    // public static String strUser;
    public static String strPswd;
    public static String userFirstName;
    public static String userLastName;
    //public static boolean isLoggedIn = false;
    // public static boolean isHttps = true;
    public static int App_Name_MinLength = 3;

    // public static boolean IsDemoModeEnabled = false;

    public static String App_FCM_Token;
    public static String App_Connection_ID;

    //For Bluetooth related constants
    public static String DEVICE_NAME = "";
    public static String BT_Address;
    public static boolean Bt_Secure;
    public static int conn_State;
    public static String resopr;

    public enum FetchLevel {
        ListMap(0),
        List(1),
        ListWithStatusAllowed(2),
        Header(3),
        Single(4),
        SingleWithItemCauses(5),
        All(6),
        ListSpinner(7),
        Count(8),
        Last(9);

        private final int value;

        FetchLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum FormAssignmentType {
        WorkOrderLevel("1"),
        OperationLevel("2"),
        Equipment("3"),
        FuncLoc("4"),
        TaskListType("5"),
        None("");

        public final String Value;

        FormAssignmentType(String value) {
            this.Value = value;
        }

        public static String getFormAssignmentType(String type) {
            String typeValue = "";
            for (FormAssignmentType value : FormAssignmentType.values()) {
                if (value.Value.equalsIgnoreCase(type))
                    return value.Value;
            }
            return typeValue;
        }
    }

    public enum InspectionCharType {
        MIC("01"),
        Qualitative("02");

        public final String charType;

        InspectionCharType(String charType) {
            this.charType = charType;
        }

        public String getCharType() {
            return this.charType;
        }

    }

    public enum Hierarchy {
        HeaderOnly(0),
        HeaderAndChild(1),
        All(2),
        None(3);

        private final int value;

        Hierarchy(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum Screens {
        WorkOrder("01", "com.ods.myjobcard.UI.WorkorderListActivity"),
        Notification("02", "com.ods.myjobcard.UI.NotificationListActivity"),
        SupervisorView("03", "com.ods.myjobcard.UI.supervisor.SupervisorViewActivity"),
        Team("04", "com.ods.myjobcard.UI.team.TeamActivity"),
        Equipment("05", "com.ods.myjobcard.UI.equipment.EquipmentDetailActivity"),
        FunctionalLocation("06", "com.ods.myjobcard.UI.functionallocation.FLDetailActivity"),
        AssetHierarchy("07", "com.ods.myjobcard.UI.assetTree.TreeListActivity"),
        AssetMap("08", "com.ods.myjobcard.UI.GISMap.GISMapActivity"),
        Operation("09", "com.ods.myjobcard.UI.WorkordersWithOperationsListActivity"),
        OnlineNotification("10", "com.ods.myjobcard.UI.onlinesearch.notifications.OnlineNotificationList");

        private String ScreenCode, ClassName;

        Screens(String code, String className) {
            this.ScreenCode = code;
            this.ClassName = className;
        }

        public static String getClassNameByCode(String code) {
            String className = "";
            for (Screens screen : Screens.values()) {
                if (screen.ScreenCode.equalsIgnoreCase(code))
                    return screen.ClassName;
            }
            return className;
        }

        public String getScreenCode() {
            return ScreenCode;
        }

        public String getClassName() {
            if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED && this.equals(WorkOrder))
                return Operation.ClassName;
            return ClassName;
        }
    }

    public enum ReasonCodeTypes {
        HOLD("HOLD", 0),
        REJECT("REJECT", 1),
        TRANSFER("TRANSFER", 2),
        NONE("NONE", 3),
        ;

        private String typeDesc;
        private int typeID;

        ReasonCodeTypes(String typeDesc, int typeID) {
            this.typeDesc = typeDesc;
            this.typeID = typeID;
        }

        public String getResonCodeTypeDesc() {
            return typeDesc;
        }

        public int getResonCodeTypeID() {
            return typeID;
        }
    }

    public enum AssignmentType {

        WorkOrderLevel("1", "WorkOrderLevel", false),
        OperationLevel("2", "OperationLevel", true),
        WorkCenterWorkOrderLevel("3", "WorkCenterSingleIdLevel", false),
        WorkCenterOperationLevel("4", "WorkCenterMultiIdLevel", true),
        CapacityLevel("5", "CapacityLevel", true);

        private String assignmentId;
        private String assignmentTypeText;
        private boolean isOperationLevel;

        AssignmentType(String assignmentID, String assignmentTypeText, boolean isOperationLevel) {
            this.assignmentTypeText = assignmentTypeText;
            this.isOperationLevel = isOperationLevel;
            this.assignmentId = assignmentID;
        }

        public static AssignmentType getAssignmentTypeById(String id) {
            AssignmentType assignmentType = null;
            for (AssignmentType type : AssignmentType.values()) {
                if (type.assignmentId.equalsIgnoreCase(id)) {
                    assignmentType = type;
                    break;
                }
            }
            return assignmentType;
        }

        public String getAssignmentTypeText() {
            return assignmentTypeText;
        }

        public boolean isOperationLevel() {
            return isOperationLevel;
        }

        public String getAssignmentId() {
            return assignmentId;
        }
    }

    public enum NotificationAssignmentType {

        PersonelNumber("1", "PersonelNumber", false),
        WorkCenter("2", "WorkCenter", false);

        private String assignmentId;
        private String assignmentTypeText;
        private boolean isNotificationLevel;

        NotificationAssignmentType(String assignmentID, String assignmentTypeText, boolean isNotificationLevel) {
            this.assignmentTypeText = assignmentTypeText;
            this.isNotificationLevel = isNotificationLevel;
            this.assignmentId = assignmentID;
        }

        public static NotificationAssignmentType getNotificationAssignmentTypeById(String id) {
            NotificationAssignmentType notificationAssignmentType = null;
            for (NotificationAssignmentType type : NotificationAssignmentType.values()) {
                if (type.assignmentId.equalsIgnoreCase(id)) {
                    notificationAssignmentType = type;
                    break;
                }
            }
            return notificationAssignmentType;
        }

        public String getAssignmentTypeText() {
            return assignmentTypeText;
        }

        public boolean isNotificationLevel() {
            return isNotificationLevel;
        }

        public String getAssignmentId() {
            return assignmentId;
        }
    }

    public enum ClearStoreOption {

        //        0-Not Clear
//1-Always Clear
//2-SameWorkcenter
//3-diffrentWorkcenter
        NotClear("0", "Not Clear"),
        AlwaysClear("1", "Always Clear"),
        SameWorkcenter("2", "Same WorkCenter"),
        DifferentWorkcenter("3", "Different WorkCenter"),
        ;
        private String id;
        private String description;

        ClearStoreOption(String id, String description) {
            this.id = id;
            this.description = description;
        }

    }

    public enum WorkFlowActionType {

        Screen("Screen"),
        Action("Action"),
        Form("Form"),
        Navigation("Navigation");
        String type;

        WorkFlowActionType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public enum WorkFlowActionKey {

        WOHoldScreen("key_WO_SC_HOLD", "Screen"),
        WOTransferScreen("key_WO_SC_TRNS", "Screen"),
        WOSuspendScreen("key_WO_SC_SUSP", "Screen"),
        WOCompleteScreen("key_WO_SC_COMP", "Screen"),
        WORejectScreen("key_WO_SC_REJC", "Screen"),
        //Error Correction

        INSPResultPosting("key_INSP_NV", WorkFlowActionType.Navigation.getType()),
        WOAttachContent("key_WO_ATTCH_NV", WorkFlowActionType.Navigation.getType()),
        NOAttachContent("key_NO_ATTCH_NV", WorkFlowActionType.Navigation.getType()),
        MPRUpdate("key_MPR_NV", WorkFlowActionType.Navigation.getType()),

        WOAddComponent("key_MTRL_SC_EDIT", WorkFlowActionType.Screen.getType()),
        WOAddEdit("key_WO_SC_EDIT", WorkFlowActionType.Screen.getType()),
        ComponentIssue("key_MTRL_SC_ISSUE", WorkFlowActionType.Screen.getType()),
        TimeSheetPosting("key_TIME_SC_EDIT", WorkFlowActionType.Screen.getType()),
        WONOCrtd("key_WO_NO_SC_CRTD", WorkFlowActionType.Screen.getType()),
        NOAddEdit("key_NO_SC_EDIT", WorkFlowActionType.Screen.getType()),
        OPRAddEdit("key_OP_SC_EDIT", WorkFlowActionType.Screen.getType()),
        WONotes("key_WO_SC_NOTES", WorkFlowActionType.Screen.getType()),
        INSTALLEQ("key_INSEQ_SC_VIEW", WorkFlowActionType.Screen.getType()),
        WONOActivityEdit("key_WO_NO_ACTIVITY_SC_EDIT", WorkFlowActionType.Screen.getType()),
        WONOItemEdit("key_WO_NO_ITEM_SC_EDIT", WorkFlowActionType.Screen.getType()),
        WONOItemCauseEdit("key_WO_NO_ITEM_CAUSE_SC_EDIT", WorkFlowActionType.Screen.getType()),
        WONOTaskEdit("key_WO_NO_TASK_SC_EDIT", WorkFlowActionType.Screen.getType()),
        NOActivityEdit("key_NOACTIVITY_SC_EDIT", WorkFlowActionType.Screen.getType()),
        NOItemEdit("key_NO_ITEM_SC_EDIT", WorkFlowActionType.Screen.getType()),
        NOTaskEdit("key_NO_TASK_SC_EDIT", WorkFlowActionType.Screen.getType()),
        WONONotes("key_WO_NO_NOTES_SC_VIEW", WorkFlowActionType.Screen.getType()),
        NONotes("key_NO_NOTES_SC_VIEW", WorkFlowActionType.Screen.getType()),
        FormResCapture("key_FORM_RES_SC_VIEW", WorkFlowActionType.Screen.getType());

        String actionKey;
        String viewType;

        WorkFlowActionKey(String actionKey, String viewType) {
            this.actionKey = actionKey;
            this.viewType = viewType;
        }

        public String getViewType() {
            return viewType;
        }

        public String getActionKey() {
            return actionKey;
        }
    }

    public enum ObjectCategory {

        WO("WORKORDER"),
        NO("NOTIFICATION"),
        //WONO("WONOTIFICATION"),
        EQUI("EQUIPMENT"),
        FUNCLOC("FUNCTIONALLOCATION"),
        ;

        String objCategory;

        ObjectCategory(String objCategory) {
            this.objCategory = objCategory;
        }

        public String getObjCategory() {
            return objCategory;
        }
    }

    public enum VoiceActions {
        STATUSCHANGE("update status", "STATUSCHG", 0),
        NAVIGATE("navigate", "NAVIG", 1),
        SEARCH("search work order", "SRCH", 2),
        YES("yes, please do, go", "YES", 3),
        NONE("", "NONE", 3),
        ;

        private String voiceactionDesc, voiceactionCode;
        private int voiceactionID;

        VoiceActions(String voiceactionDesc, String voiceactionCode, int voiceactionID) {
            this.voiceactionCode = voiceactionCode;
            this.voiceactionDesc = voiceactionDesc;
            this.voiceactionID = voiceactionID;

        }

        public String getVoiceActionCode() {
            return voiceactionCode;
        }

        public String getVoiceActionDesc() {
            return voiceactionDesc;
        }

        public int getVoiceActionID() {
            return voiceactionID;
        }
    }

    public enum WorkFlowActionKeyClass {
        ADDCOMPONENT("key_WO_SC_ADDCOMPONENT", "", "Screen");
        String actionKey;
        String objectClass;

        WorkFlowActionKeyClass(String actionKey, String className, String viewtype) {
            this.actionKey = actionKey;
            this.objectClass = className;

        }

        public static WorkFlowActionKeyClass getClassName(String actionKey, boolean isTab) {
            for (WorkFlowActionKeyClass keyClass : WorkFlowActionKeyClass.values()) {
                if (keyClass.actionKey.equalsIgnoreCase(actionKey))
                    return keyClass;
            }
            return null;
        }
    }

    public enum ObjectEntity {

        Notification("NotificationHeaderSet", "com.ods.myjobcard.types.Notification", "NotificationHeaderSet"),
        User("User", "com.ods.myjobcard.types.ctentities.UserTable", "AppUserTableSet"),
        WorkOrder("WorkOrder", "com.ods.myjobcard.types.WorkOrder", "WoHeaderSet"),
        Operation("Operation", "com.ods.myjobcard.types.Operation", "WOOperationCollection"),
        ;

        String key;
        String objectClass;
        String entitySet;

        ObjectEntity(String key, String objectClass, String entitySet) {
            this.key = key;
            this.objectClass = objectClass;
            this.entitySet = entitySet;
        }

        public static ObjectEntity getByKey(String key) {
            for (ObjectEntity entity : ObjectEntity.values()) {
                if (entity.key.equalsIgnoreCase(key)) {
                    return entity;
                }
            }
            return null;
        }

        public String getKey() {
            return key;
        }

        public String getObjectClass() {
            return objectClass;
        }

        public String getEntitySet() {
            return entitySet;
        }
    }

    public enum RecordPointMandatoryFlag {

        Single("S"),
        All("X");

        private String Id;

        RecordPointMandatoryFlag(String id) {
            Id = id;
        }

        public String getId() {
            return Id;
        }
    }

    public enum ColorMap {

        C0("#f68b1f"),
        C1("#0083ca"),
        C2("#72bf44"),
        C3("#FFCDD2"),
        C4("#ab218e"),
        C5("#b21212"),
        C6("#FFECB3"),
        C7("#004990"),
        C8("#008a3b"),
        C9("#f68b1f"),
        C10("#52247f"),
        C11("#cb4d2c"),
        C12("#f0ab00"),
        C13("#00a1e4"),
        C14("#808080"),
        C15("#b2b2b2"),
        ;

        private String colorCode;

        ColorMap(String colorCode) {
            this.colorCode = colorCode;
        }

        public String getColorCode() {
            return colorCode;
        }
    }

    public enum DateFilterOptions {

        PlannedTomorrow("1", "Planned For Tomorrow"),
        ;

        private String id, description;

        DateFilterOptions(String id, String description) {
            this.id = id;
            this.description = description;
        }
    }

    public enum Features {

        OPERATION("OPERATION", "OPERATION"),
        COMPONENT("COMPONENT", "COMPONENT"),
        ATTACHMENT("ATTACHMENT", "ATTACHMENT"),
        FORMS("FORMS","FORMS"),
        RECORDPOINTS("RECORDPOINTS", "RECORD_POINT"),
        SIGNATURESCREEN("SIGNATURESCREEN", "SIGNATURE"),
        INSPECTIONLOT("INSPECTIONLOT", "INSPECTIONLOT"),
        NOTIFICATION("NOTIFICATION", "NOTIFICATION"),
        ITEM("ITEM", "ITEM"),
        ITEMCAUSE("ITEMCAUSE", "ITEMCAUSE"),
        ;

        private String featureName, featureValue;

        Features(String featureName, String featureValue) {
            this.featureName = featureName;
            this.featureValue = featureValue;
        }

        public String getFeatureName() {
            return featureName;
        }

        public String getFeatureValue() {
            return featureValue;
        }
    }
    public enum MobileStatus {
        AWTAPEST("AWT-AP-ESTM", "assigned", "AWAE", false, 0, R.string.noAlert, false, R.drawable.accept),
        AWTAPPLN("AWT-AP-PLND", "assigned", "AWAP", false, 0, R.string.noAlert, false, R.drawable.accept),
        AWTEXTSVC("AWT-E-SVC", "assigned", "AESE", false, 0, R.string.noAlert, false, R.drawable.user_avatar),
        AWTMAT("AWT-MAT", "assigned", "AWMT", false, 0, R.string.noAlert, false, R.drawable.components),
        SKIPED("SCH-NT-EXE", "assigned", "SKIP", false, 0, R.string.noAlert, false, R.drawable.reject),
        INIT("INITIAL", "assigned", "INIT", false, 0, R.string.noAlert, false, R.drawable.download),
        REPLAN("REPLAN", "assigned", "RPLN", false, 0, R.string.noAlert, false, R.drawable.ic_trans_horiz),
        MATRDY("MAT-RDY", "assigned", "MRDY", false, 0, R.string.noAlert, false, R.drawable.issue_component),
        WOCREATED("RDY-SCH", "assigned", "REFS", false, 0, R.string.noAlert, false, R.drawable.download),
        SERREQ("SVC-REQ", "assigned", "RQSE", false, 0, R.string.noAlert, false, R.drawable.user_avatar),
        SERRDY("SVC-RDY", "assigned", "SEDY", false, 0, R.string.noAlert, false, R.drawable.accept),
        AWTSDN("AWT-STDN", "assigned", "AWSD", false, 0, R.string.noAlert, false, R.drawable.ic_alert),
        AWTTURN("AWT-TURN", "assigned", "AWTA", false, 0, R.string.noAlert, false, R.drawable.ic_alert),
        WORECEIVED("RDY-EXE", "received", "REFE", false, 1, R.string.noAlert, true, R.drawable.download),
        InComplete("INCOMPLETE", "incomplete", "INCP", false, 11, R.string.noAlert, false, R.drawable.download),
        Released("Released", "release", "REL", false, 13, R.string.noAlert, false, R.drawable.download),
        CONFIRMED("Confirmed", "confirm", "CNF", false, 14, R.string.noAlert, false, R.drawable.download),
        PARTIALCONFIRMED("Partial Confirmed", "partial confirm", "PCNF", false, 14, R.string.noAlert, false, R.drawable.download),
        Deleted("Deleted", "delete", "DLT", false, 15, R.string.noAlert, false, R.drawable.download),

        INSTALLED("INSTALLED", "install", "INST", false, 16, R.string.noAlert, false, R.drawable.equipment_install),
        ASSOCIATEDTOEQUIPMENT("Associated to Equipment", "install", "ASEQ", false, 17, R.string.noAlert, false, R.drawable.equipment_install),
        EQUIPMENTAVAILABLE("AVAILABLE", "available", "AVLB", false, 17, R.string.noAlert, false, R.drawable.equipment_install),
        INACTIVE("INACTIVE", "inactive", "INAC", false, 18, R.string.noAlert, false, R.drawable.equipment_install),

        /*
         defined ImageResKeys as a constants for getting the relavent staus images from resoure drawable
         */
        ASGD("ASSIGNED", "assigned", "ASGD", false, 0, R.string.noAlert, false, R.drawable.download),
        MOBI("RECEIVED", "received", "MOBI", false, 1, R.string.noAlert, true, R.drawable.download),
        ACCP("ACCEPT", "accept", "ACCP", false, 4, R.string.acceptAlert, true, R.drawable.accept),
        ENRT("ENROUTE", "enroute", "ENRT", true, 2, R.string.enrouteAlert, true, R.drawable.enrote),
        ARRI("ONSITE", "arrived", "ARRI", true, 3, R.string.arrivedAlert, false, R.drawable.onsite),
        STRT("START", "start", "STRT", true, 5, R.string.startAlert, true, R.drawable.start),
        HOLD("HOLD", "hold", "HOLD", false, 6, R.string.noAlert, true, R.drawable.hold),
        SUSPENDED("SUSPEND", "suspend", "SUSP", false, 7, R.string.noAlert, false, R.drawable.suspend),
        COMP("COMPLETE", "complete", "CPLT", false, 8, R.string.noAlert, true, R.drawable.complete),
        TRNS("TRANSFER", "transfer", "TRNS", false, 10, R.string.noAlert, false, R.drawable.ic_trans_horiz),
        REJC("REJECT", "reject", "REJC", false, 9, R.string.noAlert, false, R.drawable.reject),
        CRTD("CREATED", "create", "CRTD", false, 12, R.string.noAlert, false, R.drawable.download),
        NotSet("", "", "NTST", false, 16, R.string.noAlert, false, R.drawable.download);

        boolean consideredAsActive;
        boolean showOnChart;
        int drawableResId;
        private String mobileStatusCode, mobileStatusDesc, voiceActionDesc, viewID;
        private int mobileStatusIcon, mobileStatusChangeAlertText;

        MobileStatus(String mobileStatusDesc, String voiceActionDesc, String MobileStatusCode, boolean ConsideredAsActive, int MobileStatusIcon,
                     int MobileStatusChangeAlertText, boolean showOnChart, int drawableResId) {
            this.mobileStatusCode = MobileStatusCode;
            this.voiceActionDesc = voiceActionDesc;
            this.mobileStatusDesc = mobileStatusDesc;
            this.mobileStatusIcon = MobileStatusIcon;
            this.consideredAsActive = ConsideredAsActive;
            this.mobileStatusChangeAlertText = MobileStatusChangeAlertText;
            this.showOnChart = showOnChart;
            this.drawableResId = drawableResId;
        }

        public static ArrayList<MobileStatus> getAllActiveStatus() {
            ArrayList<MobileStatus> arrayList = new ArrayList<>();
            for (MobileStatus status : MobileStatus.values()) {
                if (status.getMobileStatusIsConsideredActive())
                    arrayList.add(status);
            }
            return arrayList;
        }

        public String getMobileStatusCode() {
            return mobileStatusCode;
        }

        public String getMobileStatusDesc() {
            return mobileStatusDesc;
        }

        public String getVoiceActionDesc() {
            return voiceActionDesc;
        }

        public int getDrawableResId() {
            return drawableResId;
        }

        public int getMobileStatusIcon() {
            return mobileStatusIcon;
        }

        public boolean getMobileStatusIsConsideredActive() {
            return consideredAsActive;
        }

        public int getMobileStatusChangeAlertText() {
            return mobileStatusChangeAlertText;
        }

        public boolean isShowOnChart() {
            return showOnChart;
        }
    }

    //Added by Anil
    public enum NotificationUserStatus {

/*
        CREATED("RECEIVED", "created", "PEND", false, 0, R.string.noAlert, true, R.drawable.download),
        ACCEPTED("ACCEPT", "accept", "ACCP", false, 4, R.string.notificationAcceptAlert, true, R.drawable.accept),
        REJECTED("REJECT", "reject", "MRJC", false, 9, R.string.notificationRejectAlert, true, R.drawable.reject),
        INPROGRESS("START", "in progress", "SCRN", true, 5, R.string.notificationStartAlert, true, R.drawable.start),
        COMPLETED("COMPLETE", "complete", "NOCO", false, 8, R.string.notificationCompleteAlert, true, R.drawable.complete),
        NotSet("", "", "NTST", false, 16, R.string.noAlert, false, R.drawable.download);
*/
        CRTD("RECEIVED", "created", "PEND", false, 0, R.string.noAlert, true, R.drawable.download),
        ACCP("ACCEPT", "accept", "ACCP", false, 4, R.string.notificationAcceptAlert, true, R.drawable.accept),
        REJC("REJECT", "reject", "MRJC", false, 9, R.string.notificationRejectAlert, true, R.drawable.reject),
        STRT("START", "in progress", "SCRN", true, 5, R.string.notificationStartAlert, true, R.drawable.start),
        COMP("COMPLETE", "complete", "NOCO", false, 8, R.string.notificationCompleteAlert, true, R.drawable.complete),
        NotSet("", "", "NTST", false, 16, R.string.noAlert, false, R.drawable.download);

        boolean consideredAsActive;
        boolean showOnChart;
        int drawableResId;
        private String mobileStatusCode;
        private String mobileStatusDesc;
        private String voiceActionDesc;
        private String viewID;
        private int mobileStatusIcon, mobileStatusChangeAlertText;

        NotificationUserStatus(String mobileStatusDesc, String voiceActionDesc, String MobileStatusCode, boolean ConsideredAsActive, int MobileStatusIcon,
                               int MobileStatusChangeAlertText, boolean showOnChart, int drawableResId) {
            this.mobileStatusCode = MobileStatusCode;
            this.voiceActionDesc = voiceActionDesc;
            this.mobileStatusDesc = mobileStatusDesc;
            this.mobileStatusIcon = MobileStatusIcon;
            this.consideredAsActive = ConsideredAsActive;
            this.mobileStatusChangeAlertText = MobileStatusChangeAlertText;
            this.showOnChart = showOnChart;
            this.drawableResId = drawableResId;
        }

        public String getMobileStatusCode() {
            return mobileStatusCode;
        }

        public void setMobileStatusCode(String mobileStatusCode) {
            this.mobileStatusCode = mobileStatusCode;
        }

        public String getMobileStatusDesc() {
            return mobileStatusDesc;
        }

        public void setMobileStatusDesc(String mobileStatusDesc) {
            this.mobileStatusDesc = mobileStatusDesc;
        }

        public String getVoiceActionDesc() {
            return voiceActionDesc;
        }

        public void setVoiceActionDesc(String voiceActionDesc) {
            this.voiceActionDesc = voiceActionDesc;
        }

        public String getViewID() {
            return viewID;
        }

        public void setViewID(String viewID) {
            this.viewID = viewID;
        }

        public int getMobileStatusIcon() {
            return mobileStatusIcon;
        }

        public void setMobileStatusIcon(int mobileStatusIcon) {
            this.mobileStatusIcon = mobileStatusIcon;
        }

        public int getMobileStatusChangeAlertText() {
            return mobileStatusChangeAlertText;
        }

        public void setMobileStatusChangeAlertText(int mobileStatusChangeAlertText) {
            this.mobileStatusChangeAlertText = mobileStatusChangeAlertText;
        }

        public boolean isConsideredAsActive() {
            return consideredAsActive;
        }

        public void setConsideredAsActive(boolean consideredAsActive) {
            this.consideredAsActive = consideredAsActive;
        }

        public boolean isShowOnChart() {
            return showOnChart;
        }

        public void setShowOnChart(boolean showOnChart) {
            this.showOnChart = showOnChart;
        }

        public int getDrawableResId() {
            return drawableResId;
        }

        public void setDrawableResId(int drawableResId) {
            this.drawableResId = drawableResId;
        }

    }

    public enum Priorities {

        VeryHigh("1", "Very High", "#f23b3b", R.drawable.emergency_veryhigh),
        High("2", "High", "#FFBB33", R.drawable.emergency_high),
        Medium("3", "Medium", "#99CC00", R.drawable.emergency_low),
        Low("4", "Low", "#33B5E5", R.drawable.emergency_medium);

        private String value;
        private String description;
        private String colorCode;
        private int drawableResId;

        Priorities(String value, String description, String colorCode, int drawableResId) {
            this.value = value;
            this.description = description;
            this.colorCode = colorCode;
            this.drawableResId = drawableResId;
        }

        public static int getDrawableByValue(String value) {
            for (Priorities priority : Priorities.values()) {
                if (priority.getValue().equalsIgnoreCase(value))
                    return priority.getDrawableResId();
            }
            return 0;
        }

        public static String getDescriptionByValue(String value) {
            for (Priorities priority : Priorities.values()) {
                if (priority.getValue().equalsIgnoreCase(value))
                    return priority.getDescription();
            }
            return "";
        }

        public String getValue() {
            return this.value;
        }

        public String getDescription() {
            return this.description;
        }

        public String getColorCode() {
            return this.colorCode;
        }

        public int getDrawableResId() {
            return drawableResId;
        }
    }
}
