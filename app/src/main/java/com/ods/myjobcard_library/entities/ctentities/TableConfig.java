package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

/**
 * Created by lenovo on 16-05-2016.
 */
public class TableConfig extends ZBaseEntity {

    private String EntitySet;
    private String Active;
    private String ServiceURL;
    private String AppStore;


    public TableConfig(ODataEntity entity) {
        create(entity);
    }

    public static ResponseObject getTableConfig() {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.SERVICE_CONFIG_COLLECTION, ZCollections.SERVICE_CONFIG_COLLECTION);
        } catch (Exception e) {
            DliteLogger.WriteLog(TableConfig.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }


    public String getEntitySet() {
        return EntitySet;
    }

    public void setEntitySet(String entitySet) {
        EntitySet = entitySet;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getServiceURL() {
        return ServiceURL;
    }

    public void setServiceURL(String serviceURL) {
        ServiceURL = serviceURL;
    }

    public String getAppStore() {
        return AppStore;
    }

    public void setAppStore(String appStore) {
        AppStore = appStore;
    }
}
