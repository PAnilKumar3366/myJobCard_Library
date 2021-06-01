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
 * This Class contains all helper methods for functioning the all capacity level data
 */
public class CapacityLevelHelper
{
    private ArrayList<ZODataEntity> zoDatacapacitylevelEntities;

    public CapacityLevelHelper(){

    }

    /**getting the capacity level data by filtered with wonum and oprnum
     * @param woNum
     * @param oprNum
     * @return
     */
    protected ArrayList<ZODataEntity> getCapacityData(String woNum,String oprNum){
        ResponseObject result = null;
        try {
            String entitySetName = ZCollections.CAPACITY_LEVEL_ENTITY_COLLECTION;
            String resPath = entitySetName;
            resPath += "?$filter=WorkOrderNum eq '" + woNum + "' and OperationNumber eq '" + oprNum + "'&$orderby=GeneralCounterForOrder";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            zoDatacapacitylevelEntities=new ArrayList<>();
            if(result!=null&&!result.isError()){
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    zoDatacapacitylevelEntities.add(zoDataEntity);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(CapacityLevelHelper.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ArrayList<ZODataEntity>();
        }
    return zoDatacapacitylevelEntities;
    }


}
