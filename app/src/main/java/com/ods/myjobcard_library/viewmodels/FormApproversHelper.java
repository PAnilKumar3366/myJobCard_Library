package com.ods.myjobcard_library.viewmodels;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.forms.FormApproverSetModel;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FormApproversHelper {

    public ArrayList<ZODataEntity> getWOFormApproversList(String FormID, String ApproverID, String WoNumber, String OprNum, String version) {
        return fetchWOFormApproversList(FormID, ApproverID, WoNumber, OprNum, version);
    }

    /**
     * This method is used to delete the FormApprover entity in both offline and online based on the below Parameters.
     *
     * @param FormID     CheckSheet FormID
     * @param ApproverID Selected ApproverID
     * @param WoNum      WorkOrder Number
     * @param OprNum     Operation Number
     * @return Result Object
     */
    private ResponseObject deleteFormApproverEntity(String FormID, String ApproverID, String WoNum, String OprNum) {
        ResponseObject result = null;
        ArrayList<ZODataEntity> approverList = new ArrayList<>();
        DataHelper dataHelper = DataHelper.getInstance();
        String entitySetName = ZCollections.FROM_APPROVER_ENTITY_SET;
        String resPath = entitySetName;
        if (OprNum != null && !OprNum.isEmpty())
            resPath += "?$filter=(FormID eq'" + FormID + "' and WorkOrderNum eq '" + WoNum + "' and OprNum eq '" + OprNum + "' and ApproverID eq '" + ApproverID + "')";
        else
            resPath += "?$filter=(FormID eq'" + FormID + "' and WorkOrderNum eq '" + WoNum + "' and ApproverID eq '" + ApproverID + "')";
        result = dataHelper.getEntities(entitySetName, resPath);
        try {
            if (result != null && !result.isError()) {
                FormApproverSetModel formApproverSetModel;
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                if (entities.size() > 0) {
                    formApproverSetModel = new FormApproverSetModel(entities.get(0));
                    formApproverSetModel.setMode(AppSettings.EntityMode.Delete);
                    result = formApproverSetModel.SaveToStore(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public ResponseObject deleteFormApprover(String FormID, String ApproverID, String WoNum, String OprNum) {
        return deleteFormApproverEntity(FormID, ApproverID, WoNum, OprNum);
    }

    /**
     * This method is used to fetch the FormApprover entities from Offline based on the below Parameters.
     *
     * @param FormID     CheckSheet FormID
     * @param ApproverID Selected ApproverID
     * @param WoNumber   WorkOrder Number
     * @param OprNum     Operation Number
     * @return Returns the ZODataEntities
     */
    private @NotNull ArrayList<ZODataEntity> fetchWOFormApproversList(String FormID, String ApproverID, String WoNumber, String OprNum, String version) {
        ResponseObject result = null;
        ArrayList<ZODataEntity> approverList = new ArrayList<>();
        String entitySetName = ZCollections.FROM_APPROVER_ENTITY_SET;
        String resPath = entitySetName;
        //"?$resPath=(StatusProfile eq '" + statusProfile + "' and WithoutStatNo eq true)"
        if (OprNum != null && !OprNum.isEmpty())
            resPath += "?$filter=(FormID eq'" + FormID + "' and Version eq '" + version + "' and WorkOrderNum eq '" + WoNumber + "' and OprNum eq '" + OprNum + "')";
        else
            resPath += "?$filter=(FormID eq'" + FormID + "' and Version eq '" + version + "' and WorkOrderNum eq '" + WoNumber + "')";


        result = DataHelper.getInstance().getEntities(entitySetName, resPath);
        try {
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    approverList.add(zoDataEntity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return approverList;
    }

}
