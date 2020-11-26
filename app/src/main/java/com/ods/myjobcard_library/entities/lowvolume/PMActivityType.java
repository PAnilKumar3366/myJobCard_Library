package com.ods.myjobcard_library.entities.lowvolume;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ctentities.AttendanceType;
import com.ods.myjobcard_library.entities.ctentities.SpinnerItem;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 27-06-2016.
 */
public class PMActivityType extends BaseEntity {

    private String OrderType;
    private String MaintActivType;
    private String Description;

    public PMActivityType(ODataEntity entity) {
        create(entity);
    }

    public static ResponseObject getPMActivityTypes(String orderType) {
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);
        try {
            String entitySetName = ZCollections.PM_ACTIVITY_TYPE_COLLECTION;
            String resPath = entitySetName + (orderType != null && !orderType.isEmpty() ? "?$filter=OrderType eq '" + orderType + "'" : "");
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                ArrayList<PMActivityType> activityTypes = (ArrayList<PMActivityType>) result.Content();
                ArrayList<SpinnerItem> items = new ArrayList<>();
                SpinnerItem item;
                for (PMActivityType type : activityTypes) {
                    item = new SpinnerItem();
                    item.setId(type.getMaintActivType());
                    item.setDescription(type.getDescription());
                    items.add(item);
                }
                result.setContent(items);
            } else {
                DliteLogger.WriteLog(AttendanceType.class, ZAppSettings.LogLevel.Error, "error in getting Activity Types. Message: " + result.getMessage());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(PMActivityType.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<PMActivityType> activityTypes = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    activityTypes.add(new PMActivityType(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", activityTypes);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(PMActivityType.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String OrderType) {
        this.OrderType = OrderType;
    }

    public String getMaintActivType() {
        return MaintActivType;
    }

    public void setMaintActivType(String activType) {
        MaintActivType = activType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

}
