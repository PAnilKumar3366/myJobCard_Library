package com.ods.myjobcard_library.entities.supervisor;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ctentities.SpinnerItem;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 22-12-2016.
 */
public class TeamMember extends BaseEntity {

    //constructors
    public TeamMember(ODataEntity entity){
        create(entity);
    }

    @Override
    public void create(ODataEntity entity){
        ResponseObject result = null;
        try {
            super.create(entity);
            workOrdersCount = Integer.valueOf((String) SupervisorWorkOrder.getSupervisorWorkOrdersCount(null, null, Technician).Content());
        }
        catch (Exception e){
            DliteLogger.WriteLog(TeamMember.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    private String Technician;
    private String Name;
    private String EnteredBy;
    private int workOrdersCount;

    private static TeamMember currentSelectedMember;

    public static TeamMember getCurrentSelectedMember() {
        return currentSelectedMember;
    }

    public static void setCurrentSelectedMember(TeamMember currentSelectedMember) {
        TeamMember.currentSelectedMember = currentSelectedMember;
    }

    public String getTechnician() {
        return Technician;
    }

    public void setTechnician(String technician) {
        Technician = technician;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    //get methods
    public static ResponseObject getTeamMembers(String personNum){
        ResponseObject result = null;
        String entitySetName = ZCollections.SUPERVISOR_TEAM_MEMBERS_COLLECTIONS;
        String resPath = entitySetName;
        try{
            if(personNum != null && !personNum.isEmpty()){
                resPath += "?$filter=Technician eq '"+ personNum +"'";
            }
            result = DataHelper.getInstance().getEntities(entitySetName,resPath);
            if(result != null && !result.isError()){
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities);
            }
        }
        catch (Exception e){
            DliteLogger.WriteLog(TeamMember.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(),null);
        }
        return result;
    }

    public static ArrayList<SpinnerItem> getTeamMembersForSpinner(){
        ArrayList<SpinnerItem> teamMembers = new ArrayList<>();
        String entitySetName = ZCollections.SUPERVISOR_TEAM_MEMBERS_COLLECTIONS;
        String resPath = entitySetName;
        try{
            resPath += "?$orderby=Technician";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if(result != null && !result.isError()){
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                for(ODataEntity entity : entities){
                    teamMembers.add(new SpinnerItem(String.valueOf((entity.getProperties().get("Technician")).getValue()), String.valueOf((entity.getProperties().get("Name")).getValue())));
                }
            }
        }
        catch (Exception e){
            DliteLogger.WriteLog(TeamMember.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return teamMembers;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities){
        ResponseObject result = null;
        try{
            if(entities != null){
                ArrayList<TeamMember> members = new ArrayList<TeamMember>();
                for (ODataEntity entity : entities){
                    members.add(new TeamMember(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", members);
            }
            else
                result = new ResponseObject(ZConfigManager.Status.Error);
        }
        catch (Exception e){
            DliteLogger.WriteLog(TeamMember.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(),null);
        }
        return result;
    }

    public static TeamMember getMemberByIdFromMembers(List<TeamMember> members, String id) {
        for(TeamMember member: members)
        {
            if(member.getTechnician().equals(id))
                return member;
        }
        return null;
    }

    public int getWorkOrdersCount() {
        return workOrdersCount;
    }

    public void setWorkOrdersCount(int workOrdersCount) {
        this.workOrdersCount = workOrdersCount;
    }
}
