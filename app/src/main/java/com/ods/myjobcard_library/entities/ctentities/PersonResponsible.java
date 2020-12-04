package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 16-05-2016.
 */
public class PersonResponsible extends ZBaseEntity {

    private String PersonnelNo;
    private String PersonnelArea;
    private String OrgKey;
    private String BusinessArea;
    private String CostCenter;
    private String EmplApplName;
    private String COArea;
    private String SystemID;
    private String Supervisor;
    private String WorkCenter;
    private String Plant;
    private String OperationWorkCenter;
    private String EnteredBy;
    private String Technician;


    public PersonResponsible() {
    }

    public PersonResponsible(ODataEntity entity) {
        create(entity);
    }

    public static ResponseObject getPersonResponsible(String workCenter, String plant) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String entitySetName = ZCollections.LABOUR_CODE_COLLECTION;
        String resPath = entitySetName;
        try {
            if (workCenter != null && !workCenter.isEmpty())
                resPath += "?$filter=WorkCenter eq '" + workCenter + "'";
            if (plant != null && !plant.isEmpty())
                resPath += (resPath.equals(entitySetName) ? "?$filter=" : " and ") + "Plant eq '" + plant + "'";

            resPath += (resPath.equals(entitySetName) ? "?$orderby=PersonnelNo" : "&$orderby=PersonnelNo");
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(entitySetName, resPath);
            result = FromEntity((List<ODataEntity>) result.Content());
        } catch (Exception e) {
            DliteLogger.WriteLog(PersonResponsible.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public static ArrayList<PersonResponsible> getPersonResponsible(String operationWorkCenter) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String entitySetName = ZCollections.LABOUR_CODE_COLLECTION;
        String resPath = entitySetName;
        ArrayList<PersonResponsible> persons = new ArrayList<>();
        try {
            resPath += "?$filter=tolower(OperationWorkCenter) eq '" + operationWorkCenter.toLowerCase() + "'";
            resPath += "&$orderby=PersonnelNo";
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(entitySetName, resPath);
            result = FromEntity((List<ODataEntity>) result.Content());
            if (result != null && !result.isError()) {
                persons = (ArrayList<PersonResponsible>) result.Content();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(PersonResponsible.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return persons;
    }

    //get methods
    public static ArrayList<SpinnerItem> getLabourCodes(boolean supervisorsOnly, boolean techniciansOnly) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String entitySetName = ZCollections.LABOUR_CODE_COLLECTION;
        String resPath = entitySetName;
        ArrayList<PersonResponsible> codes;
        ArrayList<SpinnerItem> items = new ArrayList<>();
        try {
            if (supervisorsOnly && !techniciansOnly)
                resPath += "?$filter=Supervisor eq 'X'";
            else if (techniciansOnly && !supervisorsOnly)
                resPath += "?$filter=Supervisor ne 'X'";

            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(entitySetName, resPath);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                codes = (ArrayList<PersonResponsible>) result.Content();
                if (codes != null && codes.size() > 0) {
                    SpinnerItem item;
                    for (PersonResponsible code : codes) {
                        item = new SpinnerItem();
                        item.setId(code.getPersonnelNo());
                        item.setDescription(code.getEmplApplName() + (code.getSystemID() != null && !code.getSystemID().isEmpty() ? (" - " + code.getSystemID()) : ""));
                        items.add(item);
                    }
                    result.setContent(items);
                } else {
                    result.setError(true);
                    result.setMessage("No Records found");
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(PersonResponsible.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return items;
    }

    public static ResponseObject getLabour(String personnelNo, String username) {
        DataHelper dataHelper = null;
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);
        String entitySetName = ZCollections.LABOUR_CODE_COLLECTION;
        String resPath = entitySetName;
        try {
            if (personnelNo != null && !personnelNo.isEmpty()) {
                personnelNo = ZCommon.getFormattedInt(8, Integer.valueOf(personnelNo));
                resPath += "?$filter=PersonnelNo eq '" + personnelNo + "'";
            } else if (username != null && !username.isEmpty())
                resPath += "?$filter=tolower(SystemID) eq '" + username.toLowerCase() + "'";

            if (!resPath.equals(entitySetName)) {
                dataHelper = DataHelper.getInstance();
                result = dataHelper.getEntities(entitySetName, resPath);
                if (!result.isError()) {
                    result = FromEntity((List<ODataEntity>) result.Content());
                    ArrayList<PersonResponsible> codes = (ArrayList<PersonResponsible>) result.Content();
                    if (codes != null && codes.size() > 0) {
                        result.setContent(codes.get(0));
                        result.setStatus(ZConfigManager.Status.Success);
                    } else {
                        result.setError(true);
                        result.setMessage("No Records found");
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(PersonResponsible.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public static ArrayList<SpinnerItem> getPlantTechniciansForSpinner(String plant, String workCenter) {
        ArrayList<SpinnerItem> plantTechnicians = new ArrayList<>();
        try {
            ResponseObject result = getPersonResponsible(workCenter, plant);
            if (result != null && !result.isError()) {
                ArrayList<PersonResponsible> technicians = (ArrayList<PersonResponsible>) result.Content();
                for (PersonResponsible technician : technicians) {
                    plantTechnicians.add(new SpinnerItem(technician.getPersonnelNo(), technician.getEmplApplName()));
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(PersonResponsible.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return plantTechnicians;
    }

    public static ArrayList<SpinnerItem> getOprWorkCenterSpinnerItems(String operationWorkCenter) {
        ArrayList<SpinnerItem> oprWorkCenterPersons = new ArrayList<>();
        try {
            ArrayList<PersonResponsible> persons = getPersonResponsible(operationWorkCenter);
            for (PersonResponsible person : persons) {
                oprWorkCenterPersons.add(new SpinnerItem(person.getPersonnelNo(), person.getEmplApplName()));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(PersonResponsible.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return oprWorkCenterPersons;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<PersonResponsible> personResponsibles = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    personResponsibles.add(new PersonResponsible(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", personResponsibles);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(PersonResponsible.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    //utility methods
    public static ArrayList<SpinnerItem> searchResources(String searchText, String searchOption, ArrayList<SpinnerItem> resources) {

        ArrayList<SpinnerItem> searchedResources = new ArrayList<SpinnerItem>();
        try {
            if (searchOption.equals(ZCollections.SEARCH_OPTION_ID)) {
                for (SpinnerItem resource : resources) {
                    if (resource.getTruncatedId().trim().toLowerCase().contains(searchText.toLowerCase())) {
                        searchedResources.add(resource);
                    }
                }
            } else if (searchOption.equals(ZCollections.SEARCH_OPTION_NAME)) {
                for (SpinnerItem resource : resources) {
                    if (resource.getDescription().trim().toLowerCase().contains(searchText.toLowerCase())) {
                        searchedResources.add(resource);
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(PersonResponsible.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return searchedResources;

    }

    public static String getDisplayableEmpName(String personalNum) {
        String personName = "";
        try {
            String entitySetName = ZCollections.LABOUR_CODE_COLLECTION;
            String resPath = entitySetName + "('" + personalNum + "')";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                ODataEntity entity = (ODataEntity) result.Content();
                PersonResponsible personResponsible = new PersonResponsible(entity);
                personName = personResponsible.getEmplApplName();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(PersonResponsible.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return personName;
    }

    public String getPersonnelNo() {
        return PersonnelNo;
    }

    public void setPersonnelNo(String personnelNo) {
        PersonnelNo = personnelNo;
    }

    public String getPersonnelArea() {
        return PersonnelArea;
    }

    public void setPersonnelArea(String personnelArea) {
        PersonnelArea = personnelArea;
    }

    public String getOrgKey() {
        return OrgKey;
    }

    public void setOrgKey(String orgKey) {
        OrgKey = orgKey;
    }

    public String getBusinessArea() {
        return BusinessArea;
    }

    public void setBusinessArea(String businessArea) {
        BusinessArea = businessArea;
    }

    public String getCostCenter() {
        return CostCenter;
    }

    public void setCostCenter(String costCenter) {
        CostCenter = costCenter;
    }


//get methods

    public String getEmplApplName() {
        return EmplApplName;
    }

    public void setEmplApplName(String emplApplName) {
        EmplApplName = emplApplName;
    }

    public String getCOArea() {
        return COArea;
    }

    public void setCOArea(String COArea) {
        this.COArea = COArea;
    }

    public String getSystemID() {
        return SystemID;
    }

    public void setSystemID(String systemID) {
        SystemID = systemID;
    }

    public String getSupervisor() {
        return Supervisor;
    }

    public void setSupervisor(String supervisor) {
        Supervisor = supervisor;
    }

    public String getWorkCenter() {
        return WorkCenter;
    }

    public void setWorkCenter(String workCenter) {
        WorkCenter = workCenter;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getOperationWorkCenter() {
        return OperationWorkCenter;
    }

    public void setOperationWorkCenter(String operationWorkCenter) {
        OperationWorkCenter = operationWorkCenter;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getTechnician() {
        return Technician;
    }

    public void setTechnician(String technician) {
        Technician = technician;
    }

}
