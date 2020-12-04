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
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by lenovo on 05-05-2016.
 */
public class EquipmentClassificationSet extends ZBaseEntity {

    private String Equipment;
    private String Object;
    //Fields
    private String ObjectClass;
    private String ClassType;
    private String IntClassNo;
    private String IntCounter;
    private int ItemNumber;
    private String Status;
    private String StdClass;
    private boolean Recursive;
    private String ChangeNumber;
    private GregorianCalendar ValidFrom;
    private boolean DeletionInd;
    private GregorianCalendar Date;
    private String EnteredBy;
    private String ClassTypeDesc;
    private String ClassDesc;
    private String ClassNum;

    public EquipmentClassificationSet(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public static ResponseObject getEquipmentClassificationSet(String eqpNum) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String resourcePath = ZCollections.EQUIPMENT_CLASSIFICATION_SET_COLLECTION;
        if (eqpNum != null)
            resourcePath += "?$filter=(Equipment eq '" + eqpNum + "' and DeletionInd eq false)";
        try {
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.EQUIPMENT_CLASSIFICATION_SET_COLLECTION, resourcePath);
            result = FromEntity((List<ODataEntity>) result.Content());
            if (result == null)
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(EquipmentClassificationSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
        }
        return result;
    }
    //End of Fields

    //getter & setter methods

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<EquipmentClassificationSet> classifications = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    classifications.add(new EquipmentClassificationSet(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", classifications);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(EquipmentClassificationSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void initializeEntityProperties() {
        this.setEntityType(ZCollections.EQUIPMENT_CLASSIFICATION_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.EQUIPMENT_CLASSIFICATION_SET_COLLECTION);
        this.addKeyFieldNames("Equipment");
        this.addKeyFieldNames("Object");
        this.addKeyFieldNames("ObjectClass");
        this.addKeyFieldNames("ClassType");
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getObject() {
        return Object;
    }

    public void setObject(String object) {
        Object = object;
    }

    public String getObjectClass() {
        return ObjectClass;
    }

    public void setObjectClass(String objectClass) {
        ObjectClass = objectClass;
    }

    public String getClassType() {
        return ClassType;
    }

    public void setClassType(String classType) {
        ClassType = classType;
    }

    public String getIntClassNo() {
        return IntClassNo;
    }

    public void setIntClassNo(String intClassNo) {
        IntClassNo = intClassNo;
    }

    public String getIntCounter() {
        return IntCounter;
    }

    public void setIntCounter(String intCounter) {
        IntCounter = intCounter;
    }

    public int getItemNumber() {
        return ItemNumber;
    }

    public void setItemNumber(int itemNumber) {
        ItemNumber = itemNumber;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStdClass() {
        return StdClass;
    }

    public void setStdClass(String stdClass) {
        StdClass = stdClass;
    }

    public boolean isRecursive() {
        return Recursive;
    }

    public void setRecursive(boolean recursive) {
        Recursive = recursive;
    }

    public String getChangeNumber() {
        return ChangeNumber;
    }

    public void setChangeNumber(String changeNumber) {
        ChangeNumber = changeNumber;
    }

    public GregorianCalendar getValidFrom() {
        return ValidFrom;
    }

    public void setValidFrom(GregorianCalendar validFrom) {
        ValidFrom = validFrom;
    }

    public boolean isDeletionInd() {
        return DeletionInd;
    }

    public void setDeletionInd(boolean deletionInd) {
        DeletionInd = deletionInd;
    }

    public GregorianCalendar getDate() {
        return Date;
    }

    public void setDate(GregorianCalendar date) {
        Date = date;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getClassDesc() {
        return ClassDesc;
    }

    public void setClassDesc(String classDesc) {
        ClassDesc = classDesc;
    }

    public String getClassTypeDesc() {
        return ClassTypeDesc;
    }

    public void setClassTypeDesc(String classTypeDesc) {
        ClassTypeDesc = classTypeDesc;
    }
    //End of getter & setter methods

    public String getClassNum() {
        return ClassNum;
    }

    public void setClassNum(String classNum) {
        ClassNum = classNum;
    }
}
