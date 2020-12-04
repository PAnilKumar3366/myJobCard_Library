package com.ods.myjobcard_library.entities.appsettings;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.appsetting.AppStoreSet;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.client.odata.v4.EntityValue;
import com.sap.client.odata.v4.EntityValueList;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.store.ODataResponseSingle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableConfigSet extends ZBaseEntity {

    private static HashMap<String, TableConfigSet> entityList = new HashMap<String, TableConfigSet>();
    private String EntitySet;
    private String EntityType;
    private String Active;
    private String AppStoreId;
    private String ClassName;
    private String Object;
    private String ServiceURL;
    private String AppStoreCode;
    private String DisplayName;
    private String ReqID;
    private String AppStoreName;
    private String Keys;
    private String RoleID;
    private String EnteredBy;

    //Setters and Getters Method

    public TableConfigSet(ODataEntity entity) {
        create(entity);
    }

    public TableConfigSet(EntityValue entityValue) {
        create(entityValue);
    }

    //get methods
    public static ResponseObject getTableDetails() {

        ResponseObject resultObjectList = null;
        ArrayList<TableConfigSet> records;
        ODataResponseSingle responseSingle;
        List<ODataEntity> entities;
        TableConfigSet tableConfig;
        try {
            resultObjectList = DataHelper.getInstance().getEntities(ZCollections.SERVICE_CONFIG_COLLECTION, new AppStoreSet());
            if (!resultObjectList.isError()) {
                //handle the service response here
                entities = (List<ODataEntity>) resultObjectList.Content();
                for (ODataEntity entity : entities) {
                    if (entity != null) {
                        tableConfig = new TableConfigSet(entity);
                        if (tableConfig.isActive()) {
                            entityList.put(tableConfig.getEntitySet(), tableConfig);
                        }
                    }
                }

            }
            /*resultObjectList = DataHelper.getInstance().getEntities(Collections.TRANSACTION_CONFIG_COLLECTION, Collections.TRANSACTION_CONFIG_COLLECTION);
            if(!resultObjectList.isError())
            {
                entities = (List<ODataEntity>)resultObjectList.Content();
                        for (ODataEntity entity : entities) {
                            if(entity != null)
                            {
                                tableConfig = new TableConfigSet(entity, AppSettings.FetchLevel.List);
                                if(tableConfig != null)
                                {
                                    if(tableConfig.isActive())
                                    {
                                        entityList.put(tableConfig.getEntitySet(),tableConfig);
                                    }
                                }

                            }
                        }
            }*/
        } catch (Exception e) {
            DliteLogger.WriteLog(AppStoreSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return resultObjectList;
    }

    public static ArrayList<String> getDefiningRequests(String StoreID, String user) {

        ResponseObject resultObjectList = null;
        ArrayList<TableConfigSet> records = null, records1 = null;
        ODataResponseSingle responseSingle = null;
        //HashMap<String, String> definingRequests = null;
        ArrayList<String> storeDefiningRequests = new ArrayList<>();
        String strQuery, strESet, strDefReq;
        try {
            strQuery = "?$filter=(AppStoreId eq '" + StoreID + "' and Active eq 'X' and ServiceURL ne '')&$select=EntitySet,ServiceURL";
            resultObjectList = DataHelper.getInstance().getEntities(ZCollections.SERVICE_CONFIG_COLLECTION + strQuery, new AppStoreSet());
            //definingRequests = new HashMap<>();
            if (!resultObjectList.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) resultObjectList.Content();

                for (ODataEntity entity : entities) {
                    if (entity != null) {
                        if (entity.getProperties() != null && entity.getProperties().size() > 0) {
                            if (entity.getProperties().containsKey("EntitySet")) {
                                strDefReq = entity.getProperties().get("ServiceURL").getValue().toString();
                                if (strDefReq.contains("strUser"))
                                    strDefReq = strDefReq.replace("\" + strUser + \"", user.toUpperCase());
                                //definingRequests.put(strESet,strDefReq);
                                storeDefiningRequests.add(strDefReq);
                            }
                        }
                    }
                }
                //}
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(AppStoreSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return storeDefiningRequests;
    }

    public static ArrayList<String> getOnlineAppSettingsDefiningReq(String StoreID, String user) {

        ResponseObject resultObjectList = null;
        ArrayList<TableConfigSet> records = null, records1 = null;
        ODataResponseSingle responseSingle = null;
        //HashMap<String, String> definingRequests = null;
        ArrayList<String> storeDefiningRequests = new ArrayList<>();
        String strQuery, strESet, strDefReq;
        try {
            strQuery = "?$filter=(AppStoreId eq '" + StoreID + "' and Active eq 'X' and ServiceURL ne '')";//&$select=EntitySet,ServiceURL
            resultObjectList = DataHelper.getInstance().getEntitiesOnline(ZCollections.SERVICE_CONFIG_COLLECTION + strQuery, ZCollections.SERVICE_CONFIG_COLLECTION, new AppStoreSet());
            //definingRequests = new HashMap<>();
            if (!resultObjectList.isError()) {
                //List<ODataEntity> entities = (List<ODataEntity>) resultObjectList.Content();
                EntityValueList entityList = (EntityValueList) resultObjectList.Content();

                for (EntityValue entityValue : entityList) {
                    if (entityValue != null) {
                        TableConfigSet tableConfigSet = new TableConfigSet(entityValue);
                        if (tableConfigSet.getAppStoreId().equals("000") && !tableConfigSet.ServiceURL.isEmpty() && !tableConfigSet.ServiceURL.equals("ChangePasswordSet")) {
                            strDefReq = tableConfigSet.getServiceURL();
                            if (strDefReq.contains("strUser"))
                                strDefReq = strDefReq.replace("\" + strUser + \"", user.toUpperCase());
                            storeDefiningRequests.add(strDefReq);
                        }
                    }
                }
                //}
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(AppStoreSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return storeDefiningRequests;
    }

    public static String getStoreID(String EntitySetName) {
        String strStoredID = null;
        TableConfigSet tc = null;
        try {
            if (entityList.isEmpty()) {
                getTableDetails();
            }
            if (entityList.containsKey(EntitySetName)) {
                tc = entityList.get(EntitySetName);
                strStoredID = tc.getAppStoreCode();
            } else {
                strStoredID = ZConfigManager.INVALID_STORE_ID;
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(AppStoreSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return strStoredID;
    }

    public static AppStoreSet getStore(String EntitySetName) {
        String appStoredID = "000";
        TableConfigSet tc = null;
        try {
//            if(!EntitySetName.equalsIgnoreCase("TableConfigSet") && !EntitySetName.equalsIgnoreCase("TransactionDataConfigSet") && !EntitySetName.equalsIgnoreCase("WorkOrderStatusSet") && !EntitySetName.equalsIgnoreCase("AppStoreSet")) {
            if (!EntitySetName.equalsIgnoreCase(ZCollections.SERVICE_CONFIG_COLLECTION) && !EntitySetName.equalsIgnoreCase(ZCollections.APP_STORE_COLLECTION)) {
                if (entityList.isEmpty()) {
                    getTableDetails();
                }
                if (entityList.containsKey(EntitySetName)) {
                    tc = entityList.get(EntitySetName);
                    appStoredID = tc.getAppStoreId();

                    /*for (StoreSettings.Stores st : StoreSettings.Stores.values()) {
                        if (st.ID() == intStoredID) {
                            stores = st;
                        }
                    }*/
                    //stores = null;
                }
                /*else { if(EntitySetName.equalsIgnoreCase(Collections.PUSH_ENTITY_COLLECTION)){stores = StoreSettings.Stores.Tx;}else{stores = null;}
                    //stores = null;
                }*/
            }
            /*else
            {
                return StoreSettings.Stores.AppSettings;
            }*/

        } catch (Exception e) {
            DliteLogger.WriteLog(AppStoreSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return AppStoreSet.getAppStoreByID(appStoredID);
    }

    public static String getServiceURL(String EntitySetName) {
        String strServiceURL = null;
        TableConfigSet tc = null;
        try {
            if (entityList.isEmpty()) {
                getTableDetails();
            }
            if (entityList.containsKey(EntitySetName)) {
                tc = entityList.get(EntitySetName);
                strServiceURL = tc.getServiceURL();
            } else {
                strServiceURL = ZConfigManager.INVALID_SERVICE_URL;
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(AppStoreSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return strServiceURL;
    }

    public static String getClassName(String entitySetName) {
        String className = null;
        TableConfigSet tc = null;
        try {
            if (entityList.isEmpty()) {
                getTableDetails();
            }
            if (entityList.containsKey(entitySetName)) {
                tc = entityList.get(entitySetName);
                className = tc.getClassName();
            } else {
                className = ZConfigManager.INVALID_CLASS_NAME;
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(AppStoreSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return className;
    }

    public static String getDisplayName(String entitySetName) {
        String displayName = null;
        TableConfigSet tc = null;
        try {
            if (entityList.isEmpty()) {
                getTableDetails();
            }
            if (entityList.containsKey(entitySetName)) {
                tc = entityList.get(entitySetName);
                displayName = tc.getDisplayName();
            } else {
                displayName = ZConfigManager.INVALID_CLASS_NAME;
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(AppStoreSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return displayName;
    }

    public String getEntitySet() {
        return EntitySet;
    }

    public void setEntitySet(String entitySet) {
        EntitySet = entitySet;
    }

    @Override
    public String getEntityType() {
        return EntityType;
    }

    @Override
    public void setEntityType(String entityType) {
        EntityType = entityType;
    }

    public String getObject() {
        return Object;
    }

    public void setObject(String object) {
        Object = object;
    }

    public String getAppStoreCode() {
        return AppStoreCode;
    }

    public void setAppStoreCode(String appStoreCode) {
        AppStoreCode = appStoreCode;
    }

    public String getActive() {
        return Active;
    }

    public boolean isActive() {
        if (Active != null && !Active.equalsIgnoreCase("X")) {
            return false;
        }
        return true;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getRoleID() {
        return RoleID;
    }

    public void setRoleID(String roleID) {
        RoleID = roleID;
    }


    //End of Setters and Getters Method

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getAppStoreId() {
        return AppStoreId;
    }

    public void setAppStoreId(String appStoreId) {
        AppStoreId = appStoreId;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getServiceURL() {
        return ServiceURL;
    }

    public void setServiceURL(String serviceURL) {
        ServiceURL = serviceURL;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getReqID() {
        return ReqID;
    }

    public void setReqID(String reqID) {
        ReqID = reqID;
    }

    public String getAppStoreName() {
        return AppStoreName;
    }

    public void setAppStoreName(String appStoreName) {
        AppStoreName = appStoreName;
    }

    public String getKeys() {
        return Keys;
    }

    public void setKeys(String keys) {
        Keys = keys;
    }
}