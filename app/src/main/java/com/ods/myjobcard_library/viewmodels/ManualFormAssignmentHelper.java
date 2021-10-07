package com.ods.myjobcard_library.viewmodels;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ResponseMasterModel;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.forms.FormListObject;
import com.ods.myjobcard_library.entities.forms.FormSetModel;
import com.ods.myjobcard_library.entities.forms.ManualFormAssignmentSetModel;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This Class contains all helper methods for functioning the all manual form assignment data
 */
public class ManualFormAssignmentHelper
{
    private ArrayList<ZODataEntity> zoDataManualFormAssignmentEntities;

    private ArrayList<FormListObject> formItemsList = new ArrayList<>();
    private ArrayList<FormSetModel> formMasterList = new ArrayList<>();
    private ManualFormAssignmentSetModel manualFormAssignmentSetModel;

    /** getting the manual form assignment data by filtered with wo number and Operation number
     * @param woNum
     * @param oprNum
     * @return
     */
    protected ArrayList<ZODataEntity> getManualFormAssignmentData(String woNum,String oprNum){
        ResponseObject result = null;
        try {
            String entitySetName = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET;
            String resPath = entitySetName;
            if (oprNum != null && !oprNum.isEmpty())
                resPath += "?$filter= (WorkOrderNum eq '" + woNum + "' and OprNum eq '" + oprNum + "')&$orderby=FlowSequence asc,Mandatory desc";
            else
                resPath += "?$filter= (WorkOrderNum eq '" + woNum + "')&$orderby=FlowSequence asc,Mandatory desc";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            zoDataManualFormAssignmentEntities = new ArrayList<>();
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    zoDataManualFormAssignmentEntities.add(zoDataEntity);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(ManualFormAssignmentHelper.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ArrayList<ZODataEntity>();
        }
        return zoDataManualFormAssignmentEntities;
    }

    /** getting each item of the form details
     * @param list
     * @return
     */
    protected ArrayList<FormListObject> getManualFormItemsList(ArrayList<ManualFormAssignmentSetModel> list, String woNum) {
        //list = FormAssignmentSetModel.getFormAssignmentData(orderType, controlKey, equipmentCat, funcLocCat, taskListType, group, groupCounter, internalCounter);

        Iterator<ManualFormAssignmentSetModel> it1 = list.iterator();

        if (formItemsList.size() > 0)
            formItemsList.clear();

        while (it1.hasNext()) {
            int filledForms = 0;
//            String responseData = "";
            String instanceId = null;
            String isDraft = "";
            ManualFormAssignmentSetModel f1 = it1.next();
            ArrayList<ResponseMasterModel> response = ResponseMasterModel.getResponseCaptureData(f1.getFormID(), f1.getVersion(), woNum, f1.getOprNum(), false, null);
            if (response != null) {
                Iterator<ResponseMasterModel> it = response.iterator();
                while (it.hasNext()) {
                    ResponseMasterModel res = it.next();
                    if (!(f1.getMultipleSub().equals("X"))) {
//                            String str = res.getResponseData();
//                        responseData = DisplayInsideFormList.convertHexToString(str);
                        instanceId = res.getInstanceID();
                        isDraft = res.getIsDraft();
                    }
                    /*if ((res.getFormID().equals(f1.getFormID()) && res.getVersion().equals(f1.getVersion())) && res.getWoNum().equals(workOrderNum)) {
                        filledForms++;
                    }*/
                }
                filledForms = response.size();
            }

            formMasterList = FormSetModel.getFormsData(f1.getFormID(), f1.getVersion(), false);
            Iterator<FormSetModel> it2 = formMasterList.iterator();
            while (it2.hasNext()) {
                FormSetModel f2 = it2.next();
                FormListObject ob = new FormListObject(f2.getFormName(), f1.getFormID(),
                        f1.getVersion(), f1.getMandatory(), f1.getOccurInt(),
                        f1.getMultipleSub(), filledForms, instanceId, f1.isGridTheme(),f1.getOprNum());
                ob.setIsDraft(isDraft);
                ob.setDescription(f2.getDescription());
                ob.setFunctionalArea(f2.getFunctionalArea());
                ob.setFormCategory(f2.getCategory());
                ob.setSubArea(f2.getSubArea());
                formItemsList.add(ob);
                break;
            }
        }
        return formItemsList;
    }
    protected boolean postManualFormAssignment(ArrayList<ManualFormAssignmentSetModel> manualFormAssignmentList,boolean autoFlush)
    {
        boolean result = false;
        try {
            for (ManualFormAssignmentSetModel formAssignmentSetModel:manualFormAssignmentList) {
                ResponseObject response = formAssignmentSetModel.SaveToStore(autoFlush);
                result = response != null && !response.isError();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;

    }

    protected ArrayList<ZODataEntity> getManualUnSubmittedMandatoryForm(String resPath){
        ResponseObject result = null;
        try {
            String entitySetName = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET;
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            zoDataManualFormAssignmentEntities=new ArrayList<>();
            if(result!=null&&!result.isError()){
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                for (ODataEntity entity : entities) {
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    zoDataManualFormAssignmentEntities.add(zoDataEntity);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(ManualFormAssignmentHelper.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ArrayList<ZODataEntity>();
        }
        return zoDataManualFormAssignmentEntities;
    }

    public int fetChApproversCount(String FormID, String ApproverID, String wo_Number, String OprNum, String version) {
        //"/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + getWorkOrderNum() + "')"
        return getCheckSheetApproversCount(FormID, ApproverID, wo_Number, OprNum, version);
    }

    /**
     * This method is used to fetch the FormApprover entities from Offline based on the below Parameters.
     *
     * @param FormID     CheckSheet FormID
     * @param ApproverID Selected ApproverID
     * @param WoNumber   WorkOrder Number
     * @param OprNum     Operation Number
     * @return Returns the number of approvers count
     */
    private int getCheckSheetApproversCount(String FormID, String ApproverID, String WoNumber, String OprNum, String version) {
        ResponseObject result = null;
        int count = 0;
        Object rawData = null;
        ArrayList<ZODataEntity> approverList = new ArrayList<>();
        String entitySetName = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET;
        String resPath = entitySetName;
        //"?$resPath=(StatusProfile eq '" + statusProfile + "' and WithoutStatNo eq true)"
        if (OprNum != null && !OprNum.isEmpty())
            resPath += "$count??$filter=(FormID eq'" + FormID + "' and Version eq '" + version + "' and WorkOrderNum eq '" + WoNumber + "' and OprNum eq '" + OprNum + "')";
        else
            resPath += "$count??$filter=(FormID eq'" + FormID + "' and Version eq '" + version + "' and WorkOrderNum eq '" + WoNumber + "')";
        result = DataHelper.getInstance().getEntities(entitySetName, resPath);
        try {
            if (!result.isError()) {
                rawData = result.Content();
                count = Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return count;
    }
}
