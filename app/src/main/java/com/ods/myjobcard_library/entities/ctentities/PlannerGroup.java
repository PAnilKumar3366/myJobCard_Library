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

/**
 * Created by lenovo on 16-05-2016.
 */
public class PlannerGroup extends ZBaseEntity {

    private String PlanningPlant;
    private String PlannerGroup;
    private String PMPlGrpName;
    private String PhoneNumber;
    private String OrderType;


    public PlannerGroup(ODataEntity entity) {
        create(entity);
    }

    public static ResponseObject getPlannerGroups() {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.PLANNER_GROUP_COLLECTION, ZCollections.PLANNER_GROUP_COLLECTION);
            result = FromEntity((List<ODataEntity>) result.Content());
        } catch (Exception e) {
            DliteLogger.WriteLog(PlannerGroup.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<PlannerGroup> plannerGroups = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    plannerGroups.add(new PlannerGroup(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", plannerGroups);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(PlannerGroup.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getPlanningPlant() {
        return PlanningPlant;
    }

    public void setPlanningPlant(String planningPlant) {
        PlanningPlant = planningPlant;
    }

    public String getPlannerGroup() {
        return PlannerGroup;
    }

    public void setPlannerGroup(String plannerGroup) {
        PlannerGroup = plannerGroup;
    }

    public String getPMPlGrpName() {
        return PMPlGrpName;
    }

    public void setPMPlGrpName(String PMPlGrpName) {
        this.PMPlGrpName = PMPlGrpName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }
}
