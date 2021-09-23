package com.ods.myjobcard_library.viewmodels;

import android.util.Log;

import com.ods.myjobcard_library.ZAppSettings;
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

public class UnAssignedOperationsHelper {

    /**
     * This method is used to get the UnAssingedOperations from UnAssignedOperationSet
     *
     * @return zODaaEntitiesList.
     */
    public ArrayList<ZODataEntity> fetchUnAssignedOprlist() {
        return getUnAssignedOpr();
    }

    public ArrayList<ZODataEntity> fetchWOUnAssingedOprList(String orderNum) {
        return getWOUnAssignedOpr(orderNum);
    }

    /**
     * This method is used to get the UnAssingedOperations from UnAssignedOperationSet
     *
     * @return zODaaEntitiesList.
     */
    private ArrayList<ZODataEntity> getUnAssignedOpr() {
        ArrayList<ZODataEntity> zoDataEntities = new ArrayList<>();
        ResponseObject result = new ResponseObject(ConfigManager.Status.Success);
        String strOrderByURI = "&$orderby=OperationNum,SubOperation";
        String strEntitySet = ZCollections.UNASSIGNED_OPR_ENTITY_SET;
        String resPath = strEntitySet + "?$filter=(startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true)&$select=OperationNum,WorkOrderNum,PlannofOpera,Counter,ControlKey,ShortText,MobileStatus,EarlSchStartExecDate,EarlSchStartExecTime,EarlSchFinishExecDate,EarlSchFinishExecTime,SystemStatus,UserStatus,ActivityType,SubOperation,ConfNo,ActivityType,Plant,WorkCenter,PersonnelNo,OpObjectNum,Equipment,FuncLoc,OrderType,TaskListType,Group,GroupCounter,InternalCounter,ActualWork,Work,EnteredBy" + strOrderByURI;
        result = DataHelper.getInstance().getEntities(strEntitySet, resPath);
        try {
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

    private ArrayList<ZODataEntity> getWOUnAssignedOpr(String orderNum) {
        ArrayList<ZODataEntity> zoDataEntities = new ArrayList<>();
        ResponseObject result = new ResponseObject(ConfigManager.Status.Success);

        String resPath = ZCollections.UNASSIGNED_OPR_ENTITY_SET + "?$filter=WorkOrderNum eq '" + orderNum + "' and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true" +
                "&$select=OperationNum,WorkOrderNum,PlannofOpera,Counter,ControlKey,ShortText,MobileStatus,EarlSchStartExecDate,EarlSchStartExecTime,EarlSchFinishExecDate,EarlSchFinishExecTime,SystemStatus,UserStatus,SubOperation,ConfNo,ActivityType,Plant,WorkCenter,PersonnelNo" +
                "&$orderby=OperationNum,SubOperation";
        result = DataHelper.getInstance().getEntities(ZCollections.UNASSIGNED_OPR_ENTITY_SET, resPath);
        try {
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

    /**
     * This method is used to fetch the SingleUnAssigned Operation ZODataEntity from Offline based on the Order number and Operation Number
     *
     * @param orderNum WorkOrder number
     * @param oprNum   UnAssginedOperationNumber
     * @return Single UnAssignedOperation Number
     */
    public ZODataEntity fetchSingleUnAssignedOpr(String orderNum, String oprNum) {
        return getSingleUnAssignedOpr(orderNum, oprNum);
    }

    /**
     * @param orderNum WorkOrder number
     * @param oprNum   UnAssginedOperationNumber
     * @return Single UnAssignedOperation Number
     */
    private ZODataEntity getSingleUnAssignedOpr(String orderNum, String oprNum) {
        ZODataEntity zoDataEntities = null;
        ResponseObject result = new ResponseObject(ConfigManager.Status.Success);
        String resPath = ZCollections.UNASSIGNED_OPR_ENTITY_SET + "?$filter=WorkOrderNum eq '" + orderNum + "' and OperationNum eq '" + oprNum + "' and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true" +
                "&$select=OperationNum,WorkOrderNum,PlannofOpera,Counter,ControlKey,ShortText,MobileStatus,EarlSchStartExecDate,EarlSchStartExecTime,EarlSchFinishExecDate,EarlSchFinishExecTime,SystemStatus,UserStatus,SubOperation,ConfNo,ActivityType,Plant,WorkCenter,PersonnelNo";
        result = DataHelper.getInstance().getEntities(ZCollections.UNASSIGNED_OPR_ENTITY_SET, resPath);
        try {
            if (result != null) {
                if (!result.isError()) {
                    List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                    for (ODataEntity entity : entities) {
                        zoDataEntities = new ZODataEntity(entity);
                        break;
                    }
                } else {
                    DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, result.getMessage());
                    Log.e(getClass().getName(), "getSingleUnAssignedOpr: " + result.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
            Log.e(getClass().getName(), "getSingleUnAssignedOpr: " + e.getMessage());
        }
        return zoDataEntities;
    }
}
