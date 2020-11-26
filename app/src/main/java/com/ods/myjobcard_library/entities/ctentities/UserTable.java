package com.ods.myjobcard_library.entities.ctentities;


import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 16-05-2016.
 */
public class UserTable extends BaseEntity {

    public static boolean isInitialized = false;
    private static String UserFirstName;
    private static String UserLastName;
    private static String UserFullName;
    private static String UserPersonnelNumber;
    private static String UserInRole;
    private static String UserCntrlArea;
    private static String UserPlant;
    private static String UserWorkCenter;
    private static String UserOprWorkCenter;
    private static String UserBusArea;
    private static String UserCostCenter;
    private static String UserDashboard;
    private static String UserRoleId;
    private static String UserWorkAssignmentType;
    private static String UserNotificationAssignmentType;
    private static String UserAddAssignmentType;
    private int RecordId;
    private String SettingGrp;
    private String SettingName;
    private String SettingValue;
    private String EnteredBy;


    public UserTable() {
        initializeEntityProperties();
    }

    public UserTable(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public static ResponseObject getUserDetails() {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.USER_TABLE_COLLECTION, ZCollections.USER_TABLE_COLLECTION);
            result = FromEntity((List<ODataEntity>) result.Content());
            //Set the User Details
            if (!result.isError()) {
                UserInRole = ZConfigManager.USER_ROLE_TECHNICIAN;
                ArrayList<UserTable> details = (ArrayList<UserTable>) result.Content();
                for (UserTable ut : details) {
                    if (ut.SettingName.equalsIgnoreCase("LASTNAME")) {
                        UserLastName = ut.SettingValue;
                    }
                    if (ut.SettingName.equalsIgnoreCase("FIRSTNAME")) {
                        UserFirstName = ut.SettingValue;
                    }
                    if (ut.SettingName.equalsIgnoreCase("PERNO")) {
                        UserPersonnelNumber = ut.SettingValue;
                    }
                    if (ut.SettingName.equalsIgnoreCase("RFPNR")) {
                        if (ut.SettingValue.equalsIgnoreCase("x"))
                            UserInRole = ZConfigManager.USER_ROLE_SUPERVISOR;
                    }
                    if (ut.SettingName.equalsIgnoreCase("CO_AREA")) {
                        UserCntrlArea = ut.SettingValue;
                    }
                    if (ut.SettingName.equalsIgnoreCase("IWK")) {
                        UserPlant = ut.SettingValue;
                    }
                    if (ut.SettingName.equalsIgnoreCase("VAP")) {
                        UserWorkCenter = ut.SettingValue;
                    }
                    if (ut.SettingName.equalsIgnoreCase("AGR")) {
                        UserOprWorkCenter = ut.SettingValue;
                    }
                    if (ut.SettingName.equalsIgnoreCase("BUS_AREA")) {
                        UserBusArea = ut.SettingValue;
                    }
                    if (ut.SettingName.equalsIgnoreCase("KOS")) {
                        UserCostCenter = ut.SettingValue;
                    }
                    if (ut.SettingName.equalsIgnoreCase("ROLE_ID")) {
                        UserRoleId = ut.SettingValue;
                    }
                    if (ut.SettingName.equalsIgnoreCase("DASHBOARD_ID")) {
                        UserDashboard = ut.SettingValue;
                    }
                    if (ut.SettingName.equalsIgnoreCase("WORKORDER_ASSIGNMENT_TYPE")) {
                        UserWorkAssignmentType = ut.SettingValue;
                    }
                    if (ut.SettingName.equalsIgnoreCase("NOTIFICATION_ASSIGNMENT_TYPE")) {
                        UserNotificationAssignmentType = ut.SettingValue;
                    }
                    if (ut.SettingName.equalsIgnoreCase("ADD_ASSIGNMENT_TYPE")) {
                        UserAddAssignmentType = ut.SettingValue;
                    }
                    if (UserLastName != null && UserFirstName != null && UserPersonnelNumber != null) {
                        isInitialized = true;
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(UserTable.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<UserTable> userTables = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    userTables.add(new UserTable(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", userTables);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(UserTable.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ResponseObject resetUserDetails() {
        ResponseObject result = null;
        try {
            UserLastName = null;
            UserPersonnelNumber = null;
            UserFirstName = null;
            UserInRole = null;
            UserCntrlArea = null;
            UserPlant = null;
            UserWorkCenter = null;
            UserOprWorkCenter = null;
            UserDashboard = null;
            UserRoleId = null;
            UserWorkAssignmentType = null;
            UserNotificationAssignmentType = null;
            UserAddAssignmentType = null;
            UserBusArea = null;
            UserCostCenter = null;
            isInitialized = false;
            result = new ResponseObject(ZConfigManager.Status.Success, "", null);
        } catch (Exception e) {
            DliteLogger.WriteLog(UserTable.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static String getUserFirstName() {
        if (!isInitialized)
            getUserDetails();
        return UserFirstName;
    }

    public static String getUserLastName() {
        if (!isInitialized)
            getUserDetails();
        return UserLastName;
    }

    public static String getUserFullName() {
        if (!isInitialized)
            getUserDetails();
        return UserFirstName + " " + UserLastName;
    }

    public static String getUserPersonnelNumber() {
        if (!isInitialized)
            getUserDetails();
        return UserPersonnelNumber;
    }

    public static String getUserInRole() {
        if (!isInitialized)
            getUserDetails();
        if (getUserRoleId() != null && !getUserRoleId().isEmpty()
                && getUserAddAssignmentType() != null && !getUserAddAssignmentType().isEmpty()) {
            UserInRole = ZConfigManager.USER_ROLE_SUPERVISOR;
        }
        return UserInRole;
    }

    public static boolean isSupervisor() {
        return getUserInRole() != null && getUserInRole().equalsIgnoreCase(ZConfigManager.USER_ROLE_SUPERVISOR);
    }

    public static String getUserCntrlArea() {
        if (!isInitialized)
            getUserDetails();
        return UserCntrlArea;
    }

    public static String getUserPlant() {
        if (!isInitialized)
            getUserDetails();
        return UserPlant;
    }

    public static String getUserWorkCenter() {
        if (!isInitialized)
            getUserDetails();
        return UserWorkCenter;
    }

    public static String getUserOprWorkCenter() {
        if (!isInitialized)
            getUserDetails();
        return UserOprWorkCenter;
    }

    public static String getUserDashboard() {
        if (!isInitialized)
            getUserDetails();
        return UserDashboard;
    }

    public static String getUserRoleId() {
        if (!isInitialized)
            getUserDetails();
        return UserRoleId;
    }

    public static String getUserWorkAssignmentType() {
        if (!isInitialized)
            getUserDetails();
        return UserWorkAssignmentType;
    }

    public static String getUserNotificationAssignmentType() {
        if (!isInitialized)
            getUserDetails();
        return UserNotificationAssignmentType;
    }

    public static String getUserAddAssignmentType() {
        if (!isInitialized)
            getUserDetails();
        return UserAddAssignmentType;
    }

    public static String getUserBusArea() {
        if (!isInitialized)
            getUserDetails();
        return UserBusArea;
    }

    public static String getUserCostCenter() {
        if (!isInitialized)
            getUserDetails();
        return UserCostCenter;
    }

    public static ResponseObject setUserDetails(PersonResponsible user) {
        ResponseObject response = null;
        try {
            response = getUserDetails();
            if (response != null && !response.isError()) {
                ArrayList<UserTable> userDetails = (ArrayList<UserTable>) response.Content();
                if (userDetails != null && user.getEmplApplName() != null && !user.getEmplApplName().isEmpty()) {
                    boolean isUpdated;
                    for (UserTable ut : userDetails) {
                        isUpdated = true;
                        if (ut.SettingName.equalsIgnoreCase("LASTNAME")) {
                            ut.SettingValue = "";
                        } else if (ut.SettingName.equalsIgnoreCase("FIRSTNAME")) {
                            ut.SettingValue = user.getEmplApplName();
                        } else if (ut.SettingName.equalsIgnoreCase("PERNO")) {
                            ut.SettingValue = user.getPersonnelNo();
                        } else if ((ut.SettingName.equalsIgnoreCase(ZConfigManager.USER_ROLE_SUPERVISOR))) {
                            ut.SettingValue = "";
                        } else {
                            isUpdated = false;
                        }
                        if (isUpdated) {
                            ut.setMode(ZAppSettings.EntityMode.Update);
                            response = ut.SaveToStore(false);
                        }
                    }
                }
            }
            resetUserDetails();
        } catch (Exception e) {
            DliteLogger.WriteLog(UserTable.class, ZAppSettings.LogLevel.Error, e.getMessage());
            response = new ResponseObject(ZConfigManager.Status.Error);
        }
        return response;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.USER_TABLE_COLLECTION);
        this.setEntityType("/ODS/SAP_WM_DLITE_SRV.UserTable");
        this.addKeyFieldNames("RecordId");
    }

    public int getRecordId() {
        return RecordId;
    }

    public void setRecordId(int recordId) {
        RecordId = recordId;
    }

    public String getSettingGrp() {
        return SettingGrp;
    }

    public void setSettingGrp(String settingGrp) {
        SettingGrp = settingGrp;
    }

    public String getSettingName() {
        return SettingName;
    }

    public void setSettingName(String settingName) {
        SettingName = settingName;
    }

    public String getSettingValue() {
        return SettingValue;
    }

    public void setSettingValue(String settingValue) {
        SettingValue = settingValue;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }
}
