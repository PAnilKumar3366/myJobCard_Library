package com.ods.myjobcard_library.entities.appsettings;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.List;

public class PushSubscription extends BaseEntity {

    private String ID;
    private String user;
    private String title;
    private String deliveryAddress;
    private boolean persistNotifications;
    private String collection;
    private String filter;
    private String select;
    private String changeType;


    //Setters and Getters Method

    public PushSubscription(String name, String regID, String pushCollection) {
        setEntitySetName(ZCollections.PUSH_ENTITY_COLLECTION);
        setEntityType(ZCollections.PUSH_ENTITY_TYPE);
        setuser(name);
        settitle(name);
        setdeliveryAddress(regID);
        setcollection(pushCollection);
        setMode(ZAppSettings.EntityMode.Create);
        setEntityResourcePath(ZCollections.PUSH_ENTITY_COLLECTION);
        setpersistNotifications(false);
        setID(ZCommon.getReqTimeStamp(16));
    }

    public PushSubscription(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        create(entity);
    }

    public static PushSubscription getSubscription(String userName) {
        PushSubscription subscription = null;
        try {
            String entitySetName = ZCollections.PUSH_ENTITY_COLLECTION;
            String resPath = entitySetName + "?$filter=user eq '" + userName + "'";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                subscription = (new PushSubscription(((List<ODataEntity>) result.Content()).get(0), ZAppSettings.FetchLevel.Header));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(PushSubscription.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return subscription;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getuser() {
        return user;
    }

    public void setuser(String user) {
        this.user = user;
    }

    public String getdeliveryAddress() {
        return deliveryAddress;
    }

    public void setdeliveryAddress(String regID) {
        this.deliveryAddress = ZConfigManager.Push_Service_Name + regID;
    }

    public boolean getpersistNotifications() {
        return persistNotifications;
    }

    public void setpersistNotifications(boolean persistNotifications) {
        this.persistNotifications = persistNotifications;
    }

    public String getcollection() {
        return collection;
    }

    public void setcollection(String collection) {
        this.collection = collection;
    }

    public String getfilter() {
        return filter;
    }

    public void setfilter(String filter) {
        this.filter = filter;
    }

    public String getselect() {
        return select;
    }

    public void setselect(String select) {
        this.select = select;
    }

    public String getchangeType() {
        return changeType;
    }


//End of Setters and Getters Method

    public void setchangeType(String changeType) {
        this.changeType = changeType;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title.toUpperCase();
    }
}