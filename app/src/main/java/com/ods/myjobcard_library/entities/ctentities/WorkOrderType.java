package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 11-05-2016.
 */
public class WorkOrderType extends BaseEntity {

    private String OrderType;
    private String PriorityType;
    private String NotificationType;
    private String OrderDescription;
    private String PlantDescription;
    private String ValuationArea;
    private String PlanningPlant;
    private String Plant;
    private String CompanyCode;
    private String CompanyName;
    private String ControlKey;
    private String BusinessProcess;
    private String WoStatusProfile;
    private String OpStatusProfile;
    public WorkOrderType(ODataEntity entity) {
        create(entity);
    }

    //get methods
    public static ResponseObject getWorkOrderTypes() {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.WOTYPE_COLLECTION, ZCollections.WOTYPE_COLLECTION);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                ArrayList<WorkOrderType> orderTypes = (ArrayList<WorkOrderType>) result.Content();
                ArrayList<SpinnerItem> items = new ArrayList<>();
                SpinnerItem item;
                for (WorkOrderType orderType : orderTypes) {
                    item = new SpinnerItem();
                    item.setId(orderType.getOrderType());
                    item.setDescription(orderType.getOrderDescription());
                    items.add(item);
                }
                result.setContent(items);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrderType.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public static ResponseObject getWorkOrderType(String workOrderType) {

        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            String resPath = ZCollections.WOTYPE_COLLECTION;
            if (workOrderType != null)
                resPath += "?$filter=(OrderType eq '" + workOrderType + "')";
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.WOTYPE_COLLECTION, resPath);
            if (result != null && !result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
            }
            if (result == null)
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrderType.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<WorkOrderType> workOrderTypes = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    workOrderTypes.add(new WorkOrderType(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", workOrderTypes);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrderType.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getPriorityType() {
        return PriorityType;
    }

    public void setPriorityType(String priorityType) {
        PriorityType = priorityType;
    }

    public String getNotificationType() {
        return NotificationType;
    }

    public void setNotificationType(String notificationType) {
        NotificationType = notificationType;
    }

    public String getOrderDescription() {
        return OrderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        OrderDescription = orderDescription;
    }

    public String getPlantDescription() {
        return PlantDescription;
    }

    public void setPlantDescription(String plantDescription) {
        PlantDescription = plantDescription;
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

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        CompanyCode = companyCode;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getControlKey() {
        return ControlKey;
    }

    public void setControlKey(String controlKey) {
        ControlKey = controlKey;
    }

    public String getBusinessProcess() {
        return BusinessProcess;
    }

    public void setBusinessProcess(String businessProcess) {
        BusinessProcess = businessProcess;
    }

    public String getWoStatusProfile() {
        return WoStatusProfile;
    }

    public void setWoStatusProfile(String woStatusProfile) {
        WoStatusProfile = woStatusProfile;
    }

    public String getOpStatusProfile() {
        return OpStatusProfile;
    }

    public void setOpStatusProfile(String opStatusProfile) {
        OpStatusProfile = opStatusProfile;
    }
}
