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

public class ClassCharacteristicValue extends ZBaseEntity {
    private String CharId;
    private String CharName;
    private String CharValCounter;
    private String IntCounter;
    private Double CharValFrom;
    private Double CharValTo;
    private String CharValue;
    private String CharValueDescr;
    private String UoM;
    private String ValueRel;

    public ClassCharacteristicValue(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public static ResponseObject getClassCharacteristicValues(String charName) {
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);
        try {
            String entitySetName = ZCollections.CLASS_CHARACTERISTICVALUE_SET_COLLECTION;
            String resPath = entitySetName + "?$filter=CharName eq '" + charName + "'";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                ArrayList<ClassCharacteristicValue> classCharacteristicValues = (ArrayList<ClassCharacteristicValue>) result.Content();
                ArrayList<SpinnerItem> items = new ArrayList<>();
                SpinnerItem item;
                for (ClassCharacteristicValue value : classCharacteristicValues) {
                    item = new SpinnerItem();
                    item.setId(value.getCharValue());
                    item.setDescription(value.getCharValueDescr());
                    items.add(item);
                }
                result.setContent(items);
            } else {
                DliteLogger.WriteLog(ClassCharacteristicValue.class, ZAppSettings.LogLevel.Error, "error in getting ClassCharacteristicValue. Message: " + result.getMessage());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(ClassCharacteristicValue.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<ClassCharacteristicValue> characteristicValueArrayList = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    characteristicValueArrayList.add(new ClassCharacteristicValue(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", characteristicValueArrayList);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(ClassCharacteristicValue.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void initializeEntityProperties() {
        this.setEntityType(ZCollections.CLASS_CHARACTERISTICVALUE_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.CLASS_CHARACTERISTICVALUE_SET_COLLECTION);
        this.addKeyFieldNames("CharId");
        this.addKeyFieldNames("CharName");
        this.addKeyFieldNames("CharValCounter");
        this.addKeyFieldNames("IntCounter");
    }

    public String getCharId() {
        return CharId;
    }

    public void setCharId(String charId) {
        CharId = charId;
    }

    public String getCharName() {
        return CharName;
    }

    public void setCharName(String charName) {
        CharName = charName;
    }

    public String getCharValCounter() {
        return CharValCounter;
    }

    public void setCharValCounter(String charValCounter) {
        CharValCounter = charValCounter;
    }

    public String getIntCounter() {
        return IntCounter;
    }

    public void setIntCounter(String intCounter) {
        IntCounter = intCounter;
    }

    public Double getCharValFrom() {
        return CharValFrom;
    }

    public void setCharValFrom(Double charValFrom) {
        CharValFrom = charValFrom;
    }

    public Double getCharValTo() {
        return CharValTo;
    }

    public void setCharValTo(Double charValTo) {
        CharValTo = charValTo;
    }

    public String getCharValue() {
        return CharValue;
    }

    public void setCharValue(String charValue) {
        CharValue = charValue;
    }

    public String getCharValueDescr() {
        return CharValueDescr;
    }

    public void setCharValueDescr(String charValueDescr) {
        CharValueDescr = charValueDescr;
    }

    public String getUoM() {
        return UoM;
    }

    public void setUoM(String uoM) {
        UoM = uoM;
    }

    public String getValueRel() {
        return ValueRel;
    }

    public void setValueRel(String valueRel) {
        ValueRel = valueRel;
    }

}
