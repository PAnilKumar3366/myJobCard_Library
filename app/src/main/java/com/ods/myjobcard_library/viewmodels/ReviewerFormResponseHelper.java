package com.ods.myjobcard_library.viewmodels;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

public class ReviewerFormResponseHelper {

    public ArrayList<ZODataEntity> getCheckSheetInstanceList() {
        return fetchCheckSheetInstanceList();
    }

    /**
     * This method is used to fetch the Reviewer's CheckSheet Instance List from Offline.
     *
     * @return ZOData Entities
     */
    private ArrayList<ZODataEntity> fetchCheckSheetInstanceList() {
        ArrayList<ZODataEntity> zoDataEntities = new ArrayList<>();
        String entitySetName = ZCollections.REVIEWER_FORM_RESPONSE_ENTITY_SET;
        try {
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, entitySetName);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    zoDataEntities.add(zoDataEntity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return zoDataEntities;
    }
}
