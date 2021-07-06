package com.ods.myjobcard_library.viewmodels;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.ConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

public class WOHistoryOprHelper {

    private ArrayList<ZODataEntity> woHistoryOprEnitiesList = new ArrayList<>();

    public WOHistoryOprHelper() {
    }

    public ArrayList<ZODataEntity> fetchWoHistoryOprList(String WorkOrderNum) {
        return getWoHistoryOprList(WorkOrderNum);
    }

    /**
     * this method is used to fetch the WorkOrder history operation list from offline.
     *
     * @param WorkOrderNum
     * @return
     */
    private ArrayList<ZODataEntity> getWoHistoryOprList(String WorkOrderNum) {
        String entitySet = ZCollections.WO_HISTORY_OPERATION_SET;
        String entityType = ZCollections.WO_HISTORY_OPERATION_ENTITY_TYPE;
        String resPath = entitySet;
        ResponseObject result = new ResponseObject(ConfigManager.Status.Error);
       /* if(!OprNum.isEmpty())
            resPath+="?$filter=(WorkOrderNum eq '"+WorkOrderNum+"' and OperationNum eq '"+OprNum+"')";*/
        //else
        resPath += "?$filter=(WorkOrderNum eq '" + WorkOrderNum + "')";
        try {
            result = DataHelper.getInstance().getEntities(entitySet, resPath);
            if (result != null && result.isError()) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        ZODataEntity zoDataEntity = new ZODataEntity(entity);
                        woHistoryOprEnitiesList.add(zoDataEntity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return woHistoryOprEnitiesList;
    }
}
