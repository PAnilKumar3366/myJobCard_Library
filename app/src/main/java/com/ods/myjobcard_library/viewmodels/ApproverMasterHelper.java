package com.ods.myjobcard_library.viewmodels;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.forms.ApproverMasterData;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

public class ApproverMasterHelper {


    public ArrayList<ZODataEntity> fetchApproverList() {
        return getFetchApproverList();
    }

    public ArrayList<ZODataEntity> searchApproverList(String searchText, String searchKey) {
        return fetchSearchApproverList(searchText, "");
    }

    private ArrayList<ZODataEntity> fetchSearchApproverList(String searchText, String searchKey) {
        ResponseObject result = null;
        ArrayList<ZODataEntity> approverSearchList = new ArrayList<>();
        try {

            String entitySetName = ZCollections.APPROVER_MASTER_DATA_ENTITY_SET;
            String resPath = entitySetName;
            if (searchKey.equalsIgnoreCase(ZCollections.SEARCH_OPTION_ID))
                resPath += "?$filter=(indexof(UserSystemID, '" + searchText + "') ne -1)";
            else if (searchText.equalsIgnoreCase(ZCollections.SEARCH_OPTION_NAME))
                resPath += "?$filter=(indexof(FirstName, '" + searchText + "') ne -1 or indexof(LastName, '" + searchText + "'))";
            else if (searchText.equalsIgnoreCase(ZCollections.SEARCH_APPROVER_DEPT))
                resPath += "?$filter=(indexof(DepartmentID, '" + searchText + "') ne -1)";

            /*if (searchbyID)
                resPath += "?$filter=(indexof(UserSystemID, '" + searchText + "') ne -1)";
            else
                resPath += "?$filter=(indexof(FirstName, '" + searchText + "') ne -1)";*/

            //resPath += "$filter=UserSystemID eq '" + searchText"' and startswith(SystemStatus,'" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true and (endswith(Equipment, '" + searchedText + "') eq true or FuncLoc eq '" + searchedText + "') and (SubOperation eq '' or SubOperation eq null)";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);


            if (result != null && !result.isError()) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    approverSearchList.add(zoDataEntity);
                }
            } else
                DliteLogger.WriteLog(ApproverMasterHelper.class, ZAppSettings.LogLevel.Error, result.getMessage());
        } catch (Exception e) {
            DliteLogger.WriteLog(ApproverMasterHelper.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ArrayList<ZODataEntity>();
        }
        return approverSearchList;
    }

    private ArrayList<ZODataEntity> getFetchApproverList() {
        ResponseObject result = null;
        ArrayList<ZODataEntity> approverList = new ArrayList<>();
        try {
            String entitySetName = ZCollections.APPROVER_MASTER_DATA_ENTITY_SET;
            String respath = ZCollections.APPROVER_MASTER_DATA_ENTITY_SET;
            result = DataHelper.getInstance().getEntities(entitySetName, respath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    approverList.add(zoDataEntity);
                }
            } else
                DliteLogger.WriteLog(ApproverMasterHelper.class, ZAppSettings.LogLevel.Error, result.getMessage());
        } catch (Exception e) {
            DliteLogger.WriteLog(ApproverMasterHelper.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ArrayList<ZODataEntity>();
        }
        return approverList;
    }

    public ApproverMasterData fetchApproverName(String approverID) {
        return getApproverName(approverID);
    }

    private ApproverMasterData getApproverName(String approverID) {
        ApproverMasterData approverName = null;
        String entitySetName = ZCollections.APPROVER_MASTER_DATA_ENTITY_SET;
        String respath = ZCollections.APPROVER_MASTER_DATA_ENTITY_SET;
        respath += "$filter=UserSystemID eq '" + approverID;
        ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, respath);
        try {
            if (result != null && result.isError()) {
                approverName = (ApproverMasterData) result.Content();
                /*if(approver!=null)
                    return approver.getFirstName()+approver.getLastName();*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return approverName;
    }

}
