package com.ods.myjobcard_library.entities.appsettings;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by MY HOME on 12/21/2018.
 */
public class AppFeature extends BaseEntity {

    private static ArrayList<String> userRoleFeatures;
    private static HashMap<String, Boolean> AppFeatures;
    private String Viewtype;
    private String Feature;
    private String Activeflag;
    private String Remarks;

    public AppFeature() {
    }

    public AppFeature(ODataEntity entity) {
        create(entity);
    }

    private static boolean getAppFeatureState(String viewtype, String feature) {
        AppFeature appFeature = new AppFeature();
        try {
            if (userRoleFeatures == null) {
                setUserRoleFeatures();
            }
            return userRoleFeatures.contains(feature.toLowerCase());
        } catch (Exception e) {
            DliteLogger.WriteLog(AppFeature.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return false;
    }

    public static void setUserRoleFeatures() {
        try {
            userRoleFeatures = new ArrayList<>();
            String entitySetName = ZCollections.APPLICATION_FEATURE_COLLECTION;
            //String resPath = entitySetName + "(Viewtype='"+ viewtype +"',Feature='"+ feature +"')";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, entitySetName);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                for (ODataEntity entity : entities) {
                    String featureName = String.valueOf(Objects.requireNonNull(entity.getProperties().get("Feature")).getValue());
                    userRoleFeatures.add(featureName.toLowerCase());
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(AppFeature.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public static boolean isFeatureActive(ZAppSettings.AppFeature appFeature) {
        boolean isActive = false;
        try {
            isActive = !ZConfigManager.SHOW_ROLE_BASED_FEATURES || (appFeature == null || getAppFeatureState(appFeature.getViewType(), appFeature.getFeatureName()));
        } catch (Exception e) {
            DliteLogger.WriteLog(AppFeature.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return isActive;
    }

    public String getViewtype() {
        return Viewtype;
    }

    public void setViewtype(String viewtype) {
        Viewtype = viewtype;
    }

    public String getFeature() {
        return Feature;
    }

    public void setFeature(String feature) {
        Feature = feature;
    }

    public String getActiveflag() {
        return Activeflag;
    }

    public void setActiveflag(String activeflag) {
        Activeflag = activeflag;
    }

    //get methods

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    /*private static HashMap<String, Boolean> getAppFeatures(){
        try{
            if(AppFeatures == null) {
                AppFeatures = new HashMap<>();
                AppFeature appFeature;
                for (ZAppSettings.AppFeature feature : ZAppSettings.AppFeature.values()) {
                    appFeature = getAppFeatureState(feature.getViewType(), feature.getFeatureName());
                    AppFeatures.put(feature.getFeatureName(), appFeature.isActive());
                }
            }
        }
        catch (Exception e){
            DliteLogger.WriteLog(AppFeature.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return AppFeatures;
    }*/

    public boolean isActive() {
        try {
            return getActiveflag() == null || getActiveflag().isEmpty() || Boolean.parseBoolean(getActiveflag());
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return false;
    }
}
