package com.ods.myjobcard_library.viewmodels;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.forms.FormMasterMetadata;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

public class FormMasterMetaDataHelper {
    private ArrayList<ZODataEntity> zoDataManualFormMasterEntities;
    private ArrayList<FormMasterMetadata> formMasterMetaDataList = new ArrayList<>();

    /**Getting the Form Master Metadata list
     * @return
     */
    public ArrayList<ZODataEntity> getZoDataManualFormMasterEntities(int skipValue, int numRecords) {
        ResponseObject result = null;
        try {
            String entitySetName = ZCollections.FORM_MASTER_METADATA_ENTITY_SET;
            String resPath = entitySetName+"?$skip=" + skipValue + " &$top=" + numRecords;
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            zoDataManualFormMasterEntities=new ArrayList<>();
            if(result!=null&&!result.isError()){
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    zoDataManualFormMasterEntities.add(zoDataEntity);
                }
            }
        }
        catch (Exception e) {
            DliteLogger.WriteLog(FormMasterMetaDataHelper.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ArrayList<ZODataEntity>();
        }
        return zoDataManualFormMasterEntities;
    }
}
