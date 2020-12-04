package com.ods.myjobcard_library.entities;

import android.text.TextUtils;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

public class ObjectList extends ZBaseEntity {

    private String WorkOrderNum;
    private String ObjectNumber;
    private int ObjectList;
    private int ObjectCounter;
    private String Equipment;
    private String EnteredBy;
    private String Notification;
    private String Assembly;
    private String LocAccAssmt;
    private String SortField;
    private String ProcessIndic;
    private String ObjListUsage;
    private String SerialNumber;
    private String Material;
    private String Number;
    private String MaterialDescription;
    private String EquipmentDescription;
    private String FunctionalLoc;
    private String FuncDescription;
    private String Description;
    private String PlanningPlant;
    private String NODescription;
    private String NotificationType;
    private String OnlineSearch;


    //Setters and Getters Method

    public ObjectList(String workOrderNum) {
        super();
        this.WorkOrderNum = workOrderNum;
    }

    public ObjectList(ODataEntity entity) {
        create(entity);
    }

    public static ResponseObject getObjects(String workOrderNum) {

        ResponseObject result = null;
        String resourcePath = null;
        String strEntitySet = null;
        try {
            strEntitySet = ZCollections.WO_OBJECTS_COLLECTION;
            resourcePath = strEntitySet + "?$filter=(WorkOrderNum eq '" + workOrderNum + "')";
            result = DataHelper.getInstance().getEntities(strEntitySet, resourcePath);
            if (!result.isError()) {
                //parse data for WO History / WO Pending from payload
                result = FromEntity((List<ODataEntity>) result.Content());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(ObjectList.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<ObjectList> woObjects = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    woObjects.add(new ObjectList(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", woObjects);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(ObjectList.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getOnlineSearch() {
        return OnlineSearch;
    }

    public void setOnlineSearch(String onlineSearch) {
        OnlineSearch = onlineSearch;
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getObjectNumber() {
        return ObjectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        ObjectNumber = objectNumber;
    }

    public int getObjectList() {
        return ObjectList;
    }

    public void setObjectList(int objectList) {
        ObjectList = objectList;
    }

    public int getObjectCounter() {
        return ObjectCounter;
    }

    public void setObjectCounter(int objectCounter) {
        ObjectCounter = objectCounter;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }

    public String getAssembly() {
        return Assembly;
    }

    public void setAssembly(String assembly) {
        Assembly = assembly;
    }

    public String getLocAccAssmt() {
        return LocAccAssmt;
    }

    public void setLocAccAssmt(String locAccAssmt) {
        LocAccAssmt = locAccAssmt;
    }

    public String getSortField() {
        return SortField;
    }

    public void setSortField(String sortField) {
        SortField = sortField;
    }

    public String getProcessIndic() {
        return ProcessIndic;
    }

    public void setProcessIndic(String processIndic) {
        ProcessIndic = processIndic;
    }

    public String getObjListUsage() {
        return ObjListUsage;
    }

    public void setObjListUsage(String objListUsage) {
        ObjListUsage = objListUsage;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String material) {
        Material = material;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getMaterialDescription() {
        return MaterialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        MaterialDescription = materialDescription;
    }

    public String getEquipmentDescription() {
        return EquipmentDescription;
    }

    public void setEquipmentDescription(String equipmentDescription) {
        EquipmentDescription = equipmentDescription;
    }

    public String getFunctionalLoc() {
        return FunctionalLoc;
    }

    public void setFunctionalLoc(String functionalLoc) {
        FunctionalLoc = functionalLoc;
    }

    public String getFuncDescription() {
        return FuncDescription;
    }

    public void setFuncDescription(String funcDescription) {
        FuncDescription = funcDescription;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPlanningPlant() {
        return PlanningPlant;
    }

    public void setPlanningPlant(String planningPlant) {
        PlanningPlant = planningPlant;
    }


    //End of Setters and Getters Method

    public String getNODescription() {
        return NODescription;
    }

    public void setNODescription(String NODescription) {
        this.NODescription = NODescription;
    }

    public String getNotificationType() {
        return NotificationType;
    }

    public void setNotificationType(String notificationType) {
        NotificationType = notificationType;
    }

    public boolean isInitialized() {
        return (!TextUtils.isEmpty(this.WorkOrderNum));
    }

    public ResponseObject update(ODataEntity entity) {
        ResponseObject result = null;
        try {

        } catch (Exception e) {
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

}