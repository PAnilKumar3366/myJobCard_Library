package com.ods.myjobcard_library.viewmodels;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.forms.DeptMasterData;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

public class DeptHelper {

    public DeptHelper() {
    }

    /**
     * This Methods helps to get the Specific Department Master data as ZOData entity.
     *
     * @param DeptId DepartmentID
     * @return
     */
    public DeptMasterData getDeptMasterData(String DeptId) {
        ResponseObject result = null;
        ArrayList<ZODataEntity> deptZODataEntities = new ArrayList<>();
        DeptMasterData deptMasterData = null;
        String entitySetName = ZCollections.DEPT_MASTER_DATA_ENTITY_SET;
        String resPath = entitySetName;
        resPath += "?$filter=DepartmentID eq '" + DeptId;
        result = DataHelper.getInstance().getEntities(entitySetName, resPath);
        try {
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    deptZODataEntities.add(zoDataEntity);
                }
                deptMasterData = new DeptMasterData(deptZODataEntities.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return deptMasterData;
    }
}
