package com.ods.myjobcard_library.entities.ctentities;

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

public class OrderTypeFeature extends ZBaseEntity {

    public static final String LEVEL_PARTIAL = "1";
    public static final String LEVEL_ALL = "2";
    private String OrderType;
    private String Feature;
    private String RoleID;
    private String Object;
    private String MandatoryLevel;

    public OrderTypeFeature() {
    }

    public OrderTypeFeature(ODataEntity entity) {
        create(entity);
    }

    public static ArrayList<String> getOrderTypeFeatures(String orderType) {
        ArrayList<String> orderTypeFeatures = new ArrayList<>();
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);
        try {
            String resourcePath = ZCollections.LT_ORDERTYPEFEATURESET + "?$filter=(OrderType eq '" + orderType + "')";
            result = DataHelper.getInstance().getEntities(ZCollections.LT_ORDERTYPEFEATURESET, resourcePath);
            if (!result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                for (ODataEntity entity : entities) {
                    //orderTypeFeatures.add(new OrderTypeFeature(entity));
                    orderTypeFeatures.add(new OrderTypeFeature(entity).getFeature());
                }
            }
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            DliteLogger.WriteLog(OrderTypeFeature.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return orderTypeFeatures;
    }

    public static ArrayList<OrderTypeFeature> getMandatoryFeaturesByObjectType(String orderType) {
        ArrayList<OrderTypeFeature> arrayList = new ArrayList<>();
        ResponseObject result = null;
        try {
            String entitySetName = ZCollections.LT_ORDERTYPEFEATURESET;
            String resPath = entitySetName + "?$filter=(OrderType eq '" + orderType + "')";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                for (ODataEntity entity : entities) {
                    OrderTypeFeature orderTypeFeature = new OrderTypeFeature(entity);
                    arrayList.add(orderTypeFeature);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(OrderTypeFeature.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return arrayList;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getRoleID() {
        return RoleID;
    }

    public void setRoleID(String roleID) {
        RoleID = roleID;
    }

    public String getObject() {
        return Object;
    }

    public void setObject(String object) {
        Object = object;
    }

    public String getMandatoryLevel() {
        return MandatoryLevel;
    }

    public void setMandatoryLevel(String mandatoryLevel) {
        MandatoryLevel = mandatoryLevel;
    }

    public String getFeature() {
        return Feature;
    }

    public void setFeature(String feature) {
        Feature = feature;
    }


}