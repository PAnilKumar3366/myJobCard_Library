package com.ods.myjobcard_library.entities.ctentities;

import androidx.annotation.NonNull;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by lenovo on 05-05-2016.
 */
public class WorkCenter extends BaseEntity {

    private String ObjectType;
    private String ObjectID;
    private GregorianCalendar StartDate;
    private GregorianCalendar EndDate;
    private String WorkCenter;
    private String Plant;
    private String PlanningPlant;
    private String Description;
    private String ShortText;
    private boolean Deletion;
    private String EnteredBy;

    public WorkCenter(ODataEntity entity) {
        create(entity);
    }

    //get methods
    public static ResponseObject getWorkCenters() {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            dataHelper = DataHelper.getInstance();
            String resPath = ZCollections.WORK_CENTER_COLLECTION;
            resPath += "?$orderby=WorkCenter";
            result = dataHelper.getEntities(ZCollections.WORK_CENTER_COLLECTION, resPath);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                ArrayList<WorkCenter> workCenters = (ArrayList<WorkCenter>) result.Content();
                ArrayList<SpinnerItem> items = new ArrayList<>();
                SpinnerItem item;
                for (WorkCenter workCenter : workCenters) {
                    item = new SpinnerItem();
                    item.setId(workCenter.getWorkCenter());
                    item.setDescription(workCenter.getDescription());
                    item.setPlantId(workCenter.getPlant());
                    item.setObjectID(workCenter.getObjectID());
                    items.add(item);
                }
                result.setContent(items);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkCenter.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public static ArrayList<SpinnerItem> getPlantWorkCenters(String plant) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        ArrayList<SpinnerItem> plantWorkCenters = new ArrayList<>();
        try {
            if (plant != null && !plant.isEmpty()) {
                dataHelper = DataHelper.getInstance();
                String resPath = ZCollections.WORK_CENTER_COLLECTION;
                resPath += "?$filter=Plant eq '" + plant + "'&$orderby=WorkCenter";
                result = dataHelper.getEntities(ZCollections.WORK_CENTER_COLLECTION, resPath);
                if (!result.isError()) {
                    result = FromEntity((List<ODataEntity>) result.Content());
                    ArrayList<WorkCenter> workCenters = (ArrayList<WorkCenter>) result.Content();
                    SpinnerItem item;
                    for (WorkCenter workCenter : workCenters) {
                        item = new SpinnerItem();
                        item.setId(workCenter.getWorkCenter());
                        item.setDescription(workCenter.getDescription());
                        item.setObjectID(workCenter.getObjectID());
                        plantWorkCenters.add(item);
                    }
                }
            } else {
                result = getWorkCenters();
                if (!result.isError())
                    plantWorkCenters = (ArrayList<SpinnerItem>) result.Content();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkCenter.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return plantWorkCenters;
    }

    public static ResponseObject getWorkCenter(@NonNull String objectID) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String resPath = ZCollections.WORK_CENTER_COLLECTION;
        String workCenter = "";
        try {
            if (!objectID.isEmpty()) {
                resPath += "?$filter=ObjectID eq '" + objectID + "'";
                dataHelper = DataHelper.getInstance();
                result = dataHelper.getEntities(ZCollections.WORK_CENTER_COLLECTION, resPath);
                if (!result.isError()) {
                    result = FromEntity((List<ODataEntity>) result.Content());
                    ArrayList<WorkCenter> workCenters = (ArrayList<WorkCenter>) result.Content();
                    if (workCenters.size() > 0) {

                        result.setContent(workCenters.get(0));
                        return result;
                    }
                }
            }
            result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkCenter.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    public static String getWorkCenterObjId(String workCenter) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String workcenterId = "";
        String resPath = ZCollections.WORK_CENTER_COLLECTION;
        try {
            if (!workCenter.isEmpty()) {
                resPath += "?$filter=WorkCenter eq '" + workCenter + "'";
                dataHelper = DataHelper.getInstance();
                result = dataHelper.getEntities(ZCollections.WORK_CENTER_COLLECTION, resPath);
                if (!result.isError()) {
                    result = FromEntity((List<ODataEntity>) result.Content());
                    ArrayList<WorkCenter> workCenters = (ArrayList<WorkCenter>) result.Content();
                    if (workCenters.size() > 0) {
                        workcenterId = workCenters.get(0).ObjectID;
                        //result.setContent(workCenters.get(0));
                        return workcenterId;
                    }
                }
            }
            result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkCenter.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error);
            result.setMessage(e.getMessage());
        }
        return workcenterId;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<WorkCenter> workCenters = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    workCenters.add(new WorkCenter(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", workCenters);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkCenter.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public String getObjectID() {
        return ObjectID;
    }

    public void setObjectID(String objectID) {
        ObjectID = objectID;
    }

    public String getWorkCenter() {
        return WorkCenter;
    }

    public void setWorkCenter(String workCenter) {
        WorkCenter = workCenter;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public GregorianCalendar getStartDate() {
        return StartDate;
    }

    public void setStartDate(GregorianCalendar startDate) {
        StartDate = startDate;
    }

    public GregorianCalendar getEndDate() {
        return EndDate;
    }

    public void setEndDate(GregorianCalendar endDate) {
        EndDate = endDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getShortText() {
        return ShortText;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }

    public String getPlanningPlant() {
        return PlanningPlant;
    }

    public void setPlanningPlant(String planningPlant) {
        PlanningPlant = planningPlant;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public Boolean getDeletion() {
        return Deletion;
    }

    public void setDeletion(Boolean deletion) {
        Deletion = deletion;
    }
}
