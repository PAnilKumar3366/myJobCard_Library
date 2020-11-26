package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZAppSettings;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 27-06-2016.
 */
public class ActivityType extends BaseEntity {

    private String COArea;
    private String ActivityType;
    private String CostCenter;
    private String CostCenterDescription;
    private String FiscalYear;
    private String ChartofAccts;
    private String FiscalYearVariant;
    private String Name;
    private String Description;
    private String ShortText;

    public ActivityType(ODataEntity entity) {
        create(entity);
    }

    public static ResponseObject getActivityTypes() {
        ResponseObject result = null;
        result = DataHelper.getInstance().getEntities(ZCollections.ACTIVITY_TYPE_COLLECTION, ZCollections.ACTIVITY_TYPE_COLLECTION);
        if (!result.isError()) {
            result = FromEntity((List<ODataEntity>) result.Content());
            ArrayList<ActivityType> activityTypes = (ArrayList<ActivityType>) result.Content();
            ArrayList<SpinnerItem> items = new ArrayList<>();
            SpinnerItem item;
            for (ActivityType type : activityTypes) {
                item = new SpinnerItem();
                item.setId(type.getActivityType());
                item.setDescription(type.getShortText());
                items.add(item);
            }
            result.setContent(items);
        } else {
            DliteLogger.WriteLog(AttendanceType.class, ZAppSettings.LogLevel.Error, "error in getting Activity Types. Message: " + result.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<ActivityType> activityTypes = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    activityTypes.add(new ActivityType(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", activityTypes);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(ActivityType.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getCOArea() {
        return COArea;
    }

    public void setCOArea(String COArea) {
        this.COArea = COArea;
    }

    public String getActivityType() {
        return ActivityType;
    }

    public void setActivityType(String activityType) {
        ActivityType = activityType;
    }

    public String getCostCenter() {
        return CostCenter;
    }

    public void setCostCenter(String costCenter) {
        CostCenter = costCenter;
    }

    public String getCostCenterDescription() {
        return CostCenterDescription;
    }

    public void setCostCenterDescription(String costCenterDescription) {
        CostCenterDescription = costCenterDescription;
    }

    public String getFiscalYear() {
        return FiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        FiscalYear = fiscalYear;
    }

    public String getChartofAccts() {
        return ChartofAccts;
    }

    public void setChartofAccts(String chartofAccts) {
        ChartofAccts = chartofAccts;
    }

    public String getFiscalYearVariant() {
        return FiscalYearVariant;
    }

    public void setFiscalYearVariant(String fiscalYearVariant) {
        FiscalYearVariant = fiscalYearVariant;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

}
