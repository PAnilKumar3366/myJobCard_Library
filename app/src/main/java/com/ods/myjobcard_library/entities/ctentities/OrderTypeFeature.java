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

    private String OrderType;
    private String Feature;

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

    public static ArrayList<String> getFeaturess() {
        ArrayList<String> aa = null;
        try {
            aa = new ArrayList<>();
        } catch (Exception e) {
            DliteLogger.WriteLog(OrderTypeFeature.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return aa;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }



    /*public static ArrayList<OrderTypeFeature> getOrderTypeFeatures(String orderType){

        ArrayList<OrderTypeFeature> orderTypeFeatures = new ArrayList<>();
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);

        try {

            String resourcePath = Collections.LT_ORDERTYPEFEATURESET+"?$filter=(OrderType eq '"+ orderType +"')";

            result = DataHelper.getInstance().getEntities(resourcePath, StoreSettings.Stores.MdLV);

            if(!result.isError()){
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                for (ODataEntity entity : entities){
                    orderTypeFeatures.add(new OrderTypeFeature(entity));
                }
            }

        }catch (Exception e){

            result.setMessage(e.getMessage());
            DliteLogger.WriteLog(OrderTypeFeature.class, ZAppSettings.LogLevel.Error, e.getMessage());

        }
            return orderTypeFeatures;
        }*/

    public String getFeature() {
        return Feature;
    }

    public void setFeature(String feature) {
        Feature = feature;
    }


}