package com.ods.myjobcard_library.entities.ctentities;


import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 16-05-2016.
 */
public class BusinessArea extends BaseEntity {

    private String BusinessArea;
    private String BusinessAreaText;


    public BusinessArea(ODataEntity entity) {
        create(entity);
    }

    public static ResponseObject getBusinessAreas() {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.BUSINESS_AREA_COLLECTION, ZCollections.BUSINESS_AREA_COLLECTION);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                ArrayList<BusinessArea> businessAreas = (ArrayList<BusinessArea>) result.Content();
                ArrayList<SpinnerItem> items = new ArrayList<>();
                SpinnerItem item = null;
                for (BusinessArea area : businessAreas) {
                    item = new SpinnerItem();
                    item.setId(area.getBusinessArea());
                    item.setDescription(area.getBusinessAreaText());
                    items.add(item);
                }
                result.setContent(items);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(BusinessArea.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<BusinessArea> businessAreas = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    businessAreas.add(new BusinessArea(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", businessAreas);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(BusinessArea.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getBusinessArea() {
        return BusinessArea;
    }

    public void setBusinessArea(String businessArea) {
        BusinessArea = businessArea;
    }

    public String getBusinessAreaText() {
        return BusinessAreaText;
    }

    public void setBusinessAreaText(String businessAreaText) {
        BusinessAreaText = businessAreaText;
    }


//get methods

}
