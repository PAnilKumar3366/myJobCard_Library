package com.ods.myjobcard_library.viewmodels;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.List;

public class OperationHelper {
    private ZODataEntity zoDataEntity;

    public OperationHelper(){

    }

    public ZODataEntity getSingleOperationZODataEntity(String workOrderNum, String operationNum, String subOperation){
        ResponseObject result = null;
        try {
            String strEntitySet = ZCollections.OPR_COLLECTION;
            String resPath = strEntitySet + "?$filter=(WorkOrderNum eq '" + workOrderNum + "' and OperationNum eq '" + operationNum + "' and SubOperation eq '" + subOperation + "')";
            result = DataHelper.getInstance().getEntities(strEntitySet, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    zoDataEntity = new ZODataEntity(entity);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return zoDataEntity;
    }
}
