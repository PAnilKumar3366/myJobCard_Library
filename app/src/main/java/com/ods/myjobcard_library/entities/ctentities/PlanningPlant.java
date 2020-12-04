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
 * Created by lenovo on 28-04-2016.
 */
public class PlanningPlant extends ZBaseEntity {

    private String Name1;
    private String ValuationArea;
    private String PlanningPlant;
    private String Plant;
    private String Name2;
    private String CompanyName;

    public PlanningPlant(ODataEntity entity) {
        create(entity);
    }

    //get methods
    public static ResponseObject getPlanningPlants(boolean isPlanPlant) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.PLAN_PLANT_COLLECTION, ZCollections.PLAN_PLANT_COLLECTION);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                ArrayList<PlanningPlant> planningPlants = (ArrayList<PlanningPlant>) result.Content();
                ArrayList<SpinnerItem> items = new ArrayList<>();
                SpinnerItem item;
                for (PlanningPlant plant : planningPlants) {
                    item = new SpinnerItem();
                    if (isPlanPlant) {
                        if (plant.getPlanningPlant() == null || plant.getPlanningPlant().isEmpty())
                            continue;
                        item.setId(plant.getPlanningPlant());
                    } else {
                        if (plant.getPlant() == null || plant.getPlant().isEmpty())
                            continue;
                        item.setId(plant.getPlant());
                    }
                    item.setDescription(plant.getName1());
                    items.add(item);
                }
                result.setContent(items);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(PlanningPlant.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<PlanningPlant> planningPlants = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    planningPlants.add(new PlanningPlant(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", planningPlants);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(PlanningPlant.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getName1() {
        return Name1;
    }

    public void setName1(String name1) {
        Name1 = name1;
    }

    public String getValuationArea() {
        return ValuationArea;
    }

    public void setValuationArea(String valuationArea) {
        ValuationArea = valuationArea;
    }

    public String getPlanningPlant() {
        return PlanningPlant;
    }

    public void setPlanningPlant(String planningPlant) {
        PlanningPlant = planningPlant;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getName2() {
        return Name2;
    }

    public void setName2(String name2) {
        Name2 = name2;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
}
