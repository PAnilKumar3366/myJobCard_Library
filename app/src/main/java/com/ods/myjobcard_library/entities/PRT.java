package com.ods.myjobcard_library.entities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.sap.smp.client.odata.ODataEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Created by lenovo on 27-12-2015.
 */
public class PRT extends BaseEntity {

    private static DateFormat df = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss Z");
    //header details
    private boolean Inactive;
    private String RoutingNum;
    private String Counter;
    private GregorianCalendar Createdon;
    private String Createdby;
    private String ObjectType;
    private GregorianCalendar Changedon;
    private String Objectid;
    private String Changedby;
    private String Item;
    private String ObjectNumber;
    private String PlntyKey;
    private String PlnnrKey;
    private String PRTitemcount;
    private String Counters;
    private String Object;
    private String OperaCounter;
    private String ControlKey;
    private boolean LoadRecords;
    private String ReferenceDate;
    private float Offset;
    private String Unit_1;
    private String Reference;
    private float Offsettofinsh;
    private String Unit_2;
    private String Unit_3;
    private float Quantity;
    private String Unit_4;
    private float Quant;
    private String Unit_5;
    private float Actuals;
    private String Unit_6;
    private float Remaining;
    private String Units_7;
    private float Checkedoutqty;
    private String Formula;
    private String Unit_8;
    private float Usagevalue1;
    private String Unit_9;
    private float Usagevalue2;
    private String Unit_10;
    private float Actual;
    private String Unit_11;
    private float Remainings;
    private String Formuls;
    private String Language;
    private String Stdtextkey;
    private String TextforPRT;
    private String Order;
    private String PRTcategory;
    private String PRT;
    private String Plants;
    private String Material;
    private String PRTs;
    private String Equipment;
    private String MaintenancePlan;
    private String MaintPlanText;
    private String Measuringpoint;
    private String Description;
    private String Characteristic;
    private boolean NegValsAllowed;
    private String Intmeasunit;
    private int Exponent;
    private int DecimalPlaces;
    private String Descriptions;
    private String Baseunits;
    private String BaseUnit;
    private String Plant;
    private String Location;
    private String SystemStatus;
    private String PRTitemcounts;
    private String Equipments;
    private String Functionalloc;
    //New Fields 11 May
    private String TaskListType;
    private String Group;
    private String PlannofOpera;

    //constructor
    public PRT(String routingNum, String item) {
        super();
        this.RoutingNum = routingNum;
        this.Item = item;
    }

    public PRT(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        create(entity);
    }

    //End of New Fields 11 may

//Setter and Getter methods

    public boolean isInactive() {
        return Inactive;
    }

    public void setInactive(boolean inactive) {
        Inactive = inactive;
    }

    public String getRoutingNum() {
        return RoutingNum;
    }

    public void setRoutingNum(String routingNum) {
        RoutingNum = routingNum;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public GregorianCalendar getCreatedon() {
        return Createdon;
    }

    public void setCreatedon(GregorianCalendar createdon) {
        Createdon = createdon;
    }

    public String getCreatedby() {
        return Createdby;
    }

    public void setCreatedby(String createdby) {
        Createdby = createdby;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public GregorianCalendar getChangedon() {
        return Changedon;
    }

    public void setChangedon(GregorianCalendar changedon) {
        Changedon = changedon;
    }

    public String getObjectid() {
        return Objectid;
    }

    public void setObjectid(String objectid) {
        Objectid = objectid;
    }

    public String getChangedby() {
        return Changedby;
    }

    public void setChangedby(String changedby) {
        Changedby = changedby;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getObjectNumber() {
        return ObjectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        ObjectNumber = objectNumber;
    }

    public String getPlntyKey() {
        return PlntyKey;
    }

    public void setPlntyKey(String plntyKey) {
        PlntyKey = plntyKey;
    }

    public String getPlnnrKey() {
        return PlnnrKey;
    }

    public void setPlnnrKey(String plnnrKey) {
        PlnnrKey = plnnrKey;
    }

    public String getPRTitemcount() {
        return PRTitemcount;
    }

    public void setPRTitemcount(String PRTitemcount) {
        this.PRTitemcount = PRTitemcount;
    }

    public String getCounters() {
        return Counters;
    }

    public void setCounters(String counters) {
        Counters = counters;
    }

    public String getObject() {
        return Object;
    }

    public void setObject(String object) {
        Object = object;
    }

    public String getOperaCounter() {
        return OperaCounter;
    }

    public void setOperaCounter(String operaCounter) {
        OperaCounter = operaCounter;
    }

    public String getControlKey() {
        return ControlKey;
    }

    public void setControlKey(String controlkey) {
        ControlKey = controlkey;
    }

    public boolean isLoadrecords() {
        return LoadRecords;
    }

    public void setLoadRecords(boolean loadRecords) {
        LoadRecords = loadRecords;
    }

    public String getReferenceDate() {
        return ReferenceDate;
    }

    public void setReferenceDate(String referenceDate) {
        ReferenceDate = referenceDate;
    }

    public float getOffset() {
        return Offset;
    }

    public void setOffset(float offset) {
        Offset = offset;
    }

    public String getUnit_1() {
        return Unit_1;
    }

    public void setUnit_1(String units_1) {
        Unit_1 = units_1;
    }

    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }

    public float getOffsettofinsh() {
        return Offsettofinsh;
    }

    public void setOffsettofinsh(float offsettofinsh) {
        Offsettofinsh = offsettofinsh;
    }

    public String getUnit_2() {
        return Unit_2;
    }

    public void setUnit_2(String units_2) {
        Unit_2 = units_2;
    }

    public String getUnit_3() {
        return Unit_3;
    }

    public void setUnit_3(String units_3) {
        Unit_3 = units_3;
    }

    public float getQuantity() {
        return Quantity;
    }

    public void setQuantity(float quantity) {
        Quantity = quantity;
    }

    public String getUnit_4() {
        return Unit_4;
    }

    public void setUnit_4(String units_4) {
        Unit_4 = units_4;
    }

    public float getQuant() {
        return Quant;
    }

    public void setQuant(float quant) {
        Quant = quant;
    }

    public String getUnit_5() {
        return Unit_5;
    }

    public void setUnit_5(String units_5) {
        Unit_5 = units_5;
    }

    public float getActuals() {
        return Actuals;
    }

    public void setActuals(float actuals) {
        Actuals = actuals;
    }

    public String getUnit_6() {
        return Unit_6;
    }

    public void setUnit_6(String units_6) {
        Unit_6 = units_6;
    }

    public float getRemaining() {
        return Remaining;
    }

    public void setRemaining(float remaining) {
        Remaining = remaining;
    }

    public String getUnit_7() {
        return Units_7;
    }

    public void setUnit_7(String units_7) {
        Units_7 = units_7;
    }

    public float getCheckedoutqty() {
        return Checkedoutqty;
    }

    public void setCheckedoutqty(float checkedoutqty) {
        Checkedoutqty = checkedoutqty;
    }

    public String getFormula() {
        return Formula;
    }

    public void setFormula(String formula) {
        Formula = formula;
    }

    public String getUnit_8() {
        return Unit_8;
    }

    public void setUnit_8(String units_8) {
        Unit_8 = units_8;
    }

    public float getUsagevalue1() {
        return Usagevalue1;
    }

    public void setUsagevalue1(float usagevalue1) {
        Usagevalue1 = usagevalue1;
    }

    public String getUnit_9() {
        return Unit_9;
    }

    public void setUnit_9(String units_9) {
        Unit_9 = units_9;
    }

    public float getUsagevalue2() {
        return Usagevalue2;
    }

    public void setUsagevalue2(float usagevalue2) {
        Usagevalue2 = usagevalue2;
    }

    public String getUnit_10() {
        return Unit_10;
    }

    public void setUnit_10(String units_10) {
        Unit_10 = units_10;
    }

    public float getActual() {
        return Actual;
    }

    public void setActual(float actual) {
        Actual = actual;
    }

    public String getUnit_11() {
        return Unit_11;
    }

    public void setUnit_11(String units_11) {
        Unit_11 = units_11;
    }

    public float getRemainings() {
        return Remainings;
    }

    public void setRemainings(float remainings) {
        Remainings = remainings;
    }

    public String getFormuls() {
        return Formuls;
    }

    public void setFormuls(String formuls) {
        Formuls = formuls;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getStdtextkey() {
        return Stdtextkey;
    }

    public void setStdtextkey(String stdtextkey) {
        Stdtextkey = stdtextkey;
    }

    public String getTextforPRT() {
        return TextforPRT;
    }

    public void setTextforPRT(String textforPRT) {
        TextforPRT = textforPRT;
    }

    public String getOrder() {
        return Order;
    }

    public void setOrder(String order) {
        Order = order;
    }

    public String getPRTcategory() {
        return PRTcategory;
    }

    public void setPRTcategory(String PRTcategory) {
        this.PRTcategory = PRTcategory;
    }

    public String getPRT() {
        return PRT;
    }

    public void setPRT(String PRT) {
        this.PRT = PRT;
    }

    public String getPlants() {
        return Plants;
    }

    public void setPlants(String plants) {
        Plants = plants;
    }

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String material) {
        Material = material;
    }

    public String getPRTs() {
        return PRTs;
    }

    public void setPRTs(String PRTs) {
        this.PRTs = PRTs;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getMaintenancePlan() {
        return MaintenancePlan;
    }

    public void setMaintenancePlan(String maintenancePlan) {
        MaintenancePlan = maintenancePlan;
    }

    public String getMaintPlanText() {
        return MaintPlanText;
    }

    public void setMaintPlanText(String maintPlanText) {
        MaintPlanText = maintPlanText;
    }

    public String getMeasuringpoint() {
        return Measuringpoint;
    }

    public void setMeasuringpoint(String measuringpoint) {
        Measuringpoint = measuringpoint;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCharacteristic() {
        return Characteristic;
    }

    public void setCharacteristic(String characteristic) {
        Characteristic = characteristic;
    }

    public boolean isNegValsAllowed() {
        return NegValsAllowed;
    }

    public void setNegValsAllowed(boolean negValsAllowed) {
        NegValsAllowed = negValsAllowed;
    }

    public String getIntmeasunit() {
        return Intmeasunit;
    }

    public void setIntmeasunit(String intmeasunit) {
        Intmeasunit = intmeasunit;
    }

    public int getExponent() {
        return Exponent;
    }

    public void setExponent(int exponent) {
        Exponent = exponent;
    }

    public int getDecimalPlaces() {
        return DecimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        DecimalPlaces = decimalPlaces;
    }

    public String getDescriptions() {
        return Descriptions;
    }

    public void setDescriptions(String descriptions) {
        Descriptions = descriptions;
    }

    public String getBaseunits() {
        return Baseunits;
    }

    public void setBaseunits(String baseunits) {
        Baseunits = baseunits;
    }

    public String getBaseUnit() {
        return BaseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        BaseUnit = baseUnit;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getSystemStatus() {
        return SystemStatus;
    }

    public void setSystemStatus(String systemStatus) {
        SystemStatus = systemStatus;
    }

    public String getPRTitemcounts() {
        return PRTitemcounts;
    }

    public void setPRTitemcounts(String PRTitemcounts) {
        this.PRTitemcounts = PRTitemcounts;
    }

    public String getEquipments() {
        return Equipments;
    }

    public void setEquipments(String equipments) {
        Equipments = equipments;
    }

    public String getFunctionalloc() {
        return Functionalloc;
    }

    public void setFunctionalloc(String functionalloc) {
        Functionalloc = functionalloc;
    }

//new Fields added on 11 May

    public String getTaskListType() {
        return TaskListType;
    }

    public void setTaskListType(String taskListType) {
        TaskListType = taskListType;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }

    public String getPlannofOpera() {
        return PlannofOpera;
    }

    public void setPlannofOpera(String plannofOpera) {
        PlannofOpera = plannofOpera;
    }

    //End of New Fields


//End of Setter and Getter methods


}
