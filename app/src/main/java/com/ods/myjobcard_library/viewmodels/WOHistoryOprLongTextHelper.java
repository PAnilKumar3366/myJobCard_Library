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

public class WOHistoryOprLongTextHelper {

    public WOHistoryOprLongTextHelper() {
    }

    public ResponseObject fetchHistoryOprLongtext(String WorkOrderNum, String OperationNum) {
        return getHistoryOprLongtext(WorkOrderNum, OperationNum);
    }

    /**
     * This method is used to fetch the ZOData entites form Offline
     *
     * @param WorkOrderNum
     * @param OperationNum
     * @return responseObject.
     */
    private ResponseObject getHistoryOprLongtext(String WorkOrderNum, String OperationNum) {
        ResponseObject result = new ResponseObject(ConfigManager.Status.Error);
        ArrayList<ZODataEntity> zoDataEntities = new ArrayList<>();
        String entitySetName = ZCollections.WO_HISTORY_OPERATION_LONG_TEXT_ENTITY_SET;
        String respath = ZCollections.WO_HISTORY_OPERATION_LONG_TEXT_ENTITY_SET;
        if (!OperationNum.isEmpty())
            respath += "?$filter=(WorkOrderNum eq '" + WorkOrderNum + " ' and OperationNum eq '" + OperationNum + "') &$orderby=Item";
        else
            respath += "?$filter=(WorkOrderNum eq '" + WorkOrderNum + " ')&$orderby=Item";
        try {
            result = DataHelper.getInstance().getEntities(entitySetName, respath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        ZODataEntity zoDataEntity = new ZODataEntity(entity);
                        zoDataEntities.add(zoDataEntity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
