package com.ods.myjobcard_library.entities.transaction;

import android.text.TextUtils;

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

public class WOPendingHistory extends BaseEntity {


    //WO child elements

    //Fields

    private String WorkOrderNum;
    private String OrderType;
    private String ReferenceOrder;
    private String EnteredBy;
    private String ObjectNum;
    private String Priority;
    private String PlanningPlant;
    private String ShortText;
    private GregorianCalendar Startdate;
    private GregorianCalendar TechCompletion;
    private GregorianCalendar BasicStartDate;
    private GregorianCalendar BasicFinishDate;
    private String EmplApplName;
    private String Equipment;
    private String FunctionalLoc;

    //Setters and Getters Method

    public WOPendingHistory(String workOrderNum) {
        super();
        this.WorkOrderNum = workOrderNum;
    }

    public WOPendingHistory(ODataEntity entity) {
        create(entity);
    }

    //get methods
    public static ResponseObject getWOHistoryPendingItems(String workOrderNum, boolean isHistory) {

        ResponseObject result = null;
        String resourcePath = null;
        String strEntitySet = null;
        try {
            if (isHistory) {
                strEntitySet = ZCollections.WO_HISTORY_COLLECTION;
            } else {
                strEntitySet = ZCollections.WO_PENDING_COLLECTION;
            }
            resourcePath = strEntitySet + "?$filter=(ReferenceOrder eq '" + workOrderNum + "')";
            result = DataHelper.getInstance().getEntities(strEntitySet, resourcePath);
            if (!result.isError()) {
                //parse data for WO History / WO Pending from payload
                result = FromEntity((List<ODataEntity>) result.Content());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WOPendingHistory.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<WOPendingHistory> woHistoryPendingItems = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    woHistoryPendingItems.add(new WOPendingHistory(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", woHistoryPendingItems);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(WOPendingHistory.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getReferenceOrder() {
        return ReferenceOrder;
    }

    public void setReferenceOrder(String referenceOrder) {
        ReferenceOrder = referenceOrder;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getObjectNum() {
        return ObjectNum;
    }

    public void setObjectNum(String objectNum) {
        ObjectNum = objectNum;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getPlanningPlant() {
        return PlanningPlant;
    }

    public void setPlanningPlant(String planningPlant) {
        PlanningPlant = planningPlant;
    }

    public String getShortText() {
        return ShortText;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }

    public GregorianCalendar getStartdate() {
        return Startdate;
    }

    public void setStartdate(GregorianCalendar startdate) {
        Startdate = startdate;
    }

    public GregorianCalendar getTechCompletion() {
        return TechCompletion;
    }

    public void setTechCompletion(GregorianCalendar techCompletion) {
        TechCompletion = techCompletion;
    }

    public GregorianCalendar getBasicStartDate() {
        return BasicStartDate;
    }

    public void setBasicStartDate(GregorianCalendar basicStartDate) {
        BasicStartDate = basicStartDate;
    }

    public GregorianCalendar getBasicFinishDate() {
        return BasicFinishDate;
    }

    public void setBasicFinishDate(GregorianCalendar basicFinishDate) {
        BasicFinishDate = basicFinishDate;
    }

    public String getEmplApplName() {
        return EmplApplName;
    }

    public void setEmplApplName(String emplApplName) {
        EmplApplName = emplApplName;
    }


    //End of Setters and Getters Method

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getFunctionalLoc() {
        return FunctionalLoc;
    }

    public void setFunctionalLoc(String functionalLoc) {
        FunctionalLoc = functionalLoc;
    }

    public boolean isInitialized() {
        return (!TextUtils.isEmpty(this.WorkOrderNum));
    }

    public ResponseObject update(ODataEntity entity) {
        ResponseObject result = null;
        try {

        } catch (Exception e) {
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);

        }
        return result;
    }

}