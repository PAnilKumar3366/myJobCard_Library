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
 * Created by MY HOME on 4/24/2019.
 */
public class OrderControlKey extends ZBaseEntity {

    private String OrderType;
    private String PlanningPlant;
    private String Plant;
    private String PriorityType;
    private String NotificationType;
    private String OrderDescription;
    private String PlantDescription;
    private String ValuationArea;
    private String ControlKey;
    private String CompanyCode;
    private String CompanyName;

    private String BusinessProcess;//Added By Anil.

    public String getBusinessProcess() {
        return BusinessProcess;
    }

    public void setBusinessProcess(String businessProcess) {
        BusinessProcess = businessProcess;
    }

    public OrderControlKey(ODataEntity entity) {
        create(entity);
    }

    //get helper methods
    public static ResponseObject getOrderTypeControlKey(String workOrderType) {

        DataHelper dataHelper = null;
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);
        try {
            String resPath = ZCollections.ORDER_TYPE_CONTROL_KEY_COLLECTION;
            if (workOrderType != null)
                resPath += "?$filter=(OrderType eq '" + workOrderType + "')";
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.ORDER_TYPE_CONTROL_KEY_COLLECTION, resPath);
            if (result != null && !result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrderType.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public static String getOrderTypeDescription(String orderType) {
        String description = "";
        try {
            ResponseObject result = getOrderTypeControlKey(orderType);
            if (result != null && !result.isError()) {
                description = ((ArrayList<OrderControlKey>) result.Content()).get(0).getOrderDescription();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrderType.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return description;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<OrderControlKey> workOrderTypes = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    workOrderTypes.add(new OrderControlKey(entity));
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

    public String getControlKey() {
        return ControlKey;
    }

    public void setControlKey(String controlKey) {
        ControlKey = controlKey;
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
}
