package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
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
public class EquipmentCharacteristics extends BaseEntity {

    private String DataType;
    private String Equipment;
    private int Characters;
    //Fields
    private String Value1;
    private int Decimals;
    private String Object;
    private String Required;
    private String Value2;
    private String Description;
    private String InternalChar;
    private String UnitofMeasure;
    private String Counter;
    private String ObjectClass;
    private String CharacteristicName;
    private String ClassType;
    private String IntCounter;
    private String CharValue;
    private Double ValueFrom;
    private String IntMeasunit;
    private Double ValueTo;
    private String IntMeasUnit;
    private String Code;
    private Double ToleranceFrom;
    private Double ToleranceTo;
    private String Percentage;
    private Double Increment;
    private String Author;
    private String ChangeNumber;
    private GregorianCalendar ValidFrom;
    private boolean DeletionInd;
    private String InternalChara;
    private String InstanceCntr;
    private String Position;
    private String CompType;
    private GregorianCalendar Date;
    private String EnteredBy;
    private boolean SingleValue;

    public EquipmentCharacteristics() {
        initializeEntityProperties();
    }

    public EquipmentCharacteristics(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public static ResponseObject getEquipmentCharacteristics(String classType, String equipment) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String resourcePath = ZCollections.EQUIPMENT_CHARACTERISTICS_COLLECTION;
        if (classType != null && equipment != null)
            resourcePath += "?$filter=(ClassType eq '" + classType + "' and Equipment eq '" + equipment + "' and DeletionInd eq false)";
        try {
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.EQUIPMENT_CHARACTERISTICS_COLLECTION, resourcePath);
            result = FromEntity((List<ODataEntity>) result.Content());
            if (result == null)
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(EquipmentCharacteristics.class, ZAppSettings.LogLevel.Error, e.getMessage());
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
                ArrayList<EquipmentCharacteristics> characteristics = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    characteristics.add(new EquipmentCharacteristics(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", characteristics);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(EquipmentCharacteristics.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void initializeEntityProperties() {
        this.setEntityType(ZCollections.EQUIPMENT_CHARACTERISTICS_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.EQUIPMENT_CHARACTERISTICS_COLLECTION);
        this.addKeyFieldNames("Equipment");
        this.addKeyFieldNames("CharacteristicName");
        this.addKeyFieldNames("ClassNum");
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getValue1() {
        return Value1;
    }

    public void setValue1(String value1) {
        Value1 = value1;
    }

    public String getObject() {
        return Object;
    }

    public void setObject(String object) {
        Object = object;
    }

    public String getValue2() {
        return Value2;
    }

    public void setValue2(String value2) {
        Value2 = value2;
    }

    public String getInternalChar() {
        return InternalChar;
    }

    public void setInternalChar(String internalChar) {
        InternalChar = internalChar;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
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

    public String getIntCounter() {
        return IntCounter;
    }

    public void setIntCounter(String intCounter) {
        IntCounter = intCounter;
    }

    public String getCharValue() {
        return CharValue;
    }

    public void setCharValue(String charValue) {
        CharValue = charValue;
    }

    public Double getValueFrom() {
        return ValueFrom;
    }

    public void setValueFrom(Double valueFrom) {
        ValueFrom = valueFrom;
    }

    public String getIntMeasunit() {
        return IntMeasunit;
    }

    public void setIntMeasunit(String intMeasunit) {
        IntMeasunit = intMeasunit;
    }

    public Double getValueTo() {
        return ValueTo;
    }

    public void setValueTo(Double valueTo) {
        ValueTo = valueTo;
    }

    public String getIntMeasUnit() {
        return IntMeasUnit;
    }

    public void setIntMeasUnit(String intMeasUnit) {
        IntMeasUnit = intMeasUnit;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public Double getToleranceFrom() {
        return ToleranceFrom;
    }

    public void setToleranceFrom(Double toleranceFrom) {
        ToleranceFrom = toleranceFrom;
    }

    public Double getToleranceTo() {
        return ToleranceTo;
    }

    public void setToleranceTo(Double toleranceTo) {
        ToleranceTo = toleranceTo;
    }

    public String getPercentage() {
        return Percentage;
    }

    public void setPercentage(String percentage) {
        Percentage = percentage;
    }

    public Double getIncrement() {
        return Increment;
    }

    public void setIncrement(Double increment) {
        Increment = increment;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
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

    public String getInternalChara() {
        return InternalChara;
    }

    public void setInternalChara(String internalChara) {
        InternalChara = internalChara;
    }

    public String getInstanceCntr() {
        return InstanceCntr;
    }

    public void setInstanceCntr(String instanceCntr) {
        InstanceCntr = instanceCntr;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getCompType() {
        return CompType;
    }

    public void setCompType(String compType) {
        CompType = compType;
    }

    public GregorianCalendar getDate() {
        return Date;
    }

    public void setDate(GregorianCalendar date) {
        Date = date;
    }

    public String getDataType() {
        return DataType;
    }

    public void setDataType(String dataType) {
        DataType = dataType;
    }

    public String getCharacteristicName() {
        return CharacteristicName;
    }

    public void setCharacteristicName(String characteristicName) {
        CharacteristicName = characteristicName;
    }

    public int getCharacters() {
        return Characters;
    }

    public void setCharacters(int characters) {
        Characters = characters;
    }

    public String getRequired() {
        return Required;
    }

    public void setRequired(String required) {
        Required = required;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUnitofMeasure() {
        return UnitofMeasure;
    }

    public void setUnitofMeasure(String unitofMeasure) {
        UnitofMeasure = unitofMeasure;
    }

    public int getDecimals() {
        return Decimals;
    }

    public void setDecimals(int decimals) {
        Decimals = decimals;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }
    //End of getter & setter methods

    public boolean isSingleValue() {
        return SingleValue;
    }

    public void setSingleValue(boolean singleValue) {
        SingleValue = singleValue;
    }
}
