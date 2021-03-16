package com.ods.myjobcard_library.entities.glassentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OperationGlassEntity implements Serializable {

    private String OperationNum;
    private String WorkOrderNum;
    private String ShortText;
    private String EarlSchStartExecDate;
    private String EarlSchStartExecTime;
    private String EarlSchFinishExecDate;
    private String EarlSchFinishExecTime;
    private String MobileStatus;

    public OperationGlassEntity() {
    }

    public String getOperationNum() {
        return OperationNum;
    }

    public void setOperationNum(String operationNum) {
        OperationNum = operationNum;
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getShortText() {
        return ShortText;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }

    public static ArrayList<OperationGlassEntity> getWorkOrderOperations(String workOrderNum) {
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);
        ArrayList<OperationGlassEntity> operations = new ArrayList<>();
        String entitySetName = ZCollections.OPR_COLLECTION;
        String resPath = entitySetName + "?$filter=WorkOrderNum eq '" + workOrderNum + "'&$select=OperationNum,WorkOrderNum,ShortText,EarlSchStartExecDate,EarlSchFinishExecDate,EarlSchStartExecTime,EarlSchFinishExecTime,SystemStatusCode,SystemStatus,UserStatusCode,UserStatus,MobileStatus&$orderby=OperationNum";
        try {
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (!result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                for (ODataEntity entity : entities) {
                    Operation operation = new Operation(entity, ZAppSettings.FetchLevel.Header);
                    OperationGlassEntity operationGlassEntity = new OperationGlassEntity();
                    operationGlassEntity.setOperationNum(operation.getOperationNum());
                    operationGlassEntity.setShortText(operation.getShortText());
                    operationGlassEntity.setWorkOrderNum(operation.getWorkOrderNum());
                    operationGlassEntity.setEarlSchStartExecDate(ZCommon.getFormattedDate(operation.getEarlSchStartExecDate().getTime()));
                    operationGlassEntity.setEarlSchFinishExecDate(ZCommon.getFormattedDate(operation.getEarlSchFinishExecDate().getTime()));
                    operationGlassEntity.setEarlSchStartExecTime(ZCommon.getFormattedTime(operation.getEarlSchStartExecTime()));
                    operationGlassEntity.setEarlSchFinishExecTime(ZCommon.getFormattedTime(operation.getEarlSchFinishExecTime()));
                    operationGlassEntity.setMobileStatus(operation.getStatusDetail().woOprStatus != null ? operation.getStatusDetail().getStatusDescKey() : operation.getStatusDetail().getStatusCode());
                    operations.add(operationGlassEntity);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(OperationGlassEntity.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return operations;
    }

    public String getEarlSchStartExecDate() {
        return EarlSchStartExecDate;
    }

    public void setEarlSchStartExecDate(String earlSchStartExecDate) {
        EarlSchStartExecDate = earlSchStartExecDate;
    }

    public String getEarlSchStartExecTime() {
        return EarlSchStartExecTime;
    }

    public void setEarlSchStartExecTime(String earlSchStartExecTime) {
        EarlSchStartExecTime = earlSchStartExecTime;
    }

    public String getEarlSchFinishExecDate() {
        return EarlSchFinishExecDate;
    }

    public void setEarlSchFinishExecDate(String earlSchFinishExecDate) {
        EarlSchFinishExecDate = earlSchFinishExecDate;
    }

    public String getEarlSchFinishExecTime() {
        return EarlSchFinishExecTime;
    }

    public void setEarlSchFinishExecTime(String earlSchFinishExecTime) {
        EarlSchFinishExecTime = earlSchFinishExecTime;
    }

    public String getMobileStatus() {
        return MobileStatus;
    }

    public void setMobileStatus(String mobileStatus) {
        MobileStatus = mobileStatus;
    }
}
