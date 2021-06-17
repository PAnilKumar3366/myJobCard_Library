package com.ods.myjobcard_library.viewmodels.workorder;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class contains all helper methods for functioning the all wohistorycomponent data
 */
public class WOHistoryComponentHelper {

    private ArrayList<ZODataEntity> zoDataEntities;

    public WOHistoryComponentHelper(){

    }

    /**feting the wo history component records by filtered with wonum
     * @param woNum
     * @return
     */
    protected ArrayList<ZODataEntity> getWOHistoryComponents(String woNum){
        ResponseObject result = null;
        try {
            String entitySetName = ZCollections.WOHISTORY_COMPONENTS_COLLECTION;
            String resPath = entitySetName;
            resPath += "?$filter=ReferenceOrder eq '" + woNum + "'&$orderby=Item";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            zoDataEntities=new ArrayList<>();
            if(result!=null&&!result.isError()){
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    zoDataEntities.add(zoDataEntity);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(CapacityLevelHelper.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ArrayList<ZODataEntity>();
        }
        return zoDataEntities;
    }
}
