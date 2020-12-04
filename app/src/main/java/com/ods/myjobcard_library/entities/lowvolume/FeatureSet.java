package com.ods.myjobcard_library.entities.lowvolume;

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

public class FeatureSet extends ZBaseEntity {

    private String Feature;
    private String Description;

    public FeatureSet(ODataEntity entity) {
        create(entity);
    }

    public FeatureSet() {
    }

    public static ArrayList<String> getFeature(String feature) {
        //FeatureSet featureObj = new FeatureSet();
        //ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);

        ArrayList<String> features = new ArrayList<>();
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);

        try {
            String resourcePath = ZCollections.LT_FEATURESET + "?$filter=Feature eq '" + feature + "'";
            result = DataHelper.getInstance().getEntities(ZCollections.LT_FEATURESET, resourcePath);
            if (!result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                for (ODataEntity entity : entities) {

                    features.add(new FeatureSet(entity).getFeature());
                    //return new FeatureSet(entity);
                }
            }
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            DliteLogger.WriteLog(FeatureSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return features;
    }

    public String getFeature() {
        return Feature;
    }

    public void setFeature(String feature) {
        Feature = feature;
    }

    public String getDescription() {
        return Description;
    }

    /*public static FeatureSet getFeature(String feature)
    {
        FeatureSet featureObj = new FeatureSet();
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);
        try{
            String resourcePath = Collections.LT_FEATURESET+"?$filter=Feature eq '"+ feature +"'";
            result = DataHelper.getInstance().getEntities(resourcePath, StoreSettings.Stores.MdLV);
            if(!result.isError()){
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                for (ODataEntity entity : entities){
                    return new FeatureSet(entity);
                }
            }
        }
        catch (Exception e){
            result.setMessage(e.getMessage());
            DliteLogger.WriteLog(FeatureSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return featureObj;
    }*/

    public void setDescription(String description) {
        Description = description;
    }


}