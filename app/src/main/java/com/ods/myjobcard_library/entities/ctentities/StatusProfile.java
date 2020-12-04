package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

public class StatusProfile extends ZBaseEntity {
    private String RoleID;
    private String StatusProfile;
    private String UserStatusCode;
    private String StatusCategory;
    private String StatusCode;
    private String StatusDescription;
    private boolean WithoutStatNo;
    private String EnteredBy;

    private boolean isSelected;

    private StatusProfile(ODataEntity entity) {
        initializingEntityProperties();
        create(entity);
    }

    public static ArrayList<StatusProfile> getStatsProfileList(String orderType) {
        DataHelper dataHelper = null;
        String statusProfile = "";
        ResponseObject result = null;
        ArrayList<StatusProfile> statusProfiles = new ArrayList<>();
        String resourcePath = ZCollections.STASTUS_PROFILE_SET;
        try {
            if (orderType != null && !orderType.isEmpty()) {
                result = WorkOrderType.getWorkOrderType(orderType);
                if (!result.isError()) {
                    ArrayList<WorkOrderType> workOrderTypes = (ArrayList<WorkOrderType>) result.Content();
                    if (workOrderTypes.size() > 0)
                        statusProfile = workOrderTypes.get(0).getWoStatusProfile();
                    else
                        return statusProfiles;
                    resourcePath += "?$filter=(StatusProfile eq '" + statusProfile + "' and WithoutStatNo eq true)";
                    dataHelper = DataHelper.getInstance();
                    result = dataHelper.getEntities(ZCollections.STASTUS_PROFILE_SET, resourcePath);
                    result = FromEntity((List<ODataEntity>) result.Content());
                    if (!result.isError())
                        statusProfiles = (ArrayList<StatusProfile>) result.Content();
                }

            }
        } catch (Exception e) {
            DliteLogger.WriteLog(StandardTextSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return statusProfiles;
    }

    //method for searching with Status Id or Description
    public static ArrayList<StatusProfile> searchStatusProfile(String searchText, String searchOption, ArrayList<StatusProfile> statusProfileList) {

        ArrayList<StatusProfile> searchedResources = new ArrayList<StatusProfile>();
        try {
            if (searchOption.equals(ZCollections.SEARCH_OPTION_ID)) {
                for (StatusProfile statusProfile : statusProfileList) {
                    if (statusProfile.getStatusCode().trim().toLowerCase().contains(searchText.toLowerCase())) {
                        searchedResources.add(statusProfile);
                    }
                }
            } else if (searchOption.equals(ZCollections.SEARCH_OPTION_DESCRIPTION)) {
                for (StatusProfile statusProfile : statusProfileList) {
                    if (statusProfile.getStatusDescription().trim().toLowerCase().contains(searchText.toLowerCase())) {
                        searchedResources.add(statusProfile);
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(PersonResponsible.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return searchedResources;

    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<StatusProfile> statusProfiles = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    statusProfiles.add(new StatusProfile(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", statusProfiles);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(StatusProfile.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getRoleID() {
        return RoleID;
    }

    public void setRoleID(String roleID) {
        RoleID = roleID;
    }

    public String getStatusProfile() {
        return StatusProfile;
    }

    public void setStatusProfile(String statusProfile) {
        StatusProfile = statusProfile;
    }

    public String getUserStatusCode() {
        return UserStatusCode;
    }

    public void setUserStatusCode(String userStatusCode) {
        UserStatusCode = userStatusCode;
    }

    public String getStatusCategory() {
        return StatusCategory;
    }

    public void setStatusCategory(String statusCategory) {
        StatusCategory = statusCategory;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public String getStatusDescription() {
        return StatusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        StatusDescription = statusDescription;
    }

    public boolean isWithoutStatNo() {
        return WithoutStatNo;
    }

    public void setWithoutStatNo(boolean withoutStatNo) {
        WithoutStatNo = withoutStatNo;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private void initializingEntityProperties() {
        this.setEntitySetName(ZCollections.STASTUS_PROFILE_SET);
        this.setEntityType(ZCollections.STATUS_PROFILE_ENTITY_TYPE);
        this.setEntityResourcePath(ZCollections.STATUS_PROFILE_ENTITY_TYPE);
        this.addKeyFieldNames("RoleID");
        this.addKeyFieldNames("StatusProfile");
        this.addKeyFieldNames("UserStatusCode");
        this.addKeyFieldNames("StatusCategory");
    }
}
