package com.ods.myjobcard_library.entities.qmentities;

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

/**
 * Created by lenovo on 02-04-2020.
 */
public class InspectionCharacteristic extends ZBaseEntity {

    private String InspLot;
    private String SelectedSet;
    private String InspOper;
    private String InspPoint;
    private String InspChar;
    private String Scope;
    private String Status;
    private String StatusDesc;
    private String CharDescr;
    private String CharType;
    private String ConfirmNo;
    private String Obligatory;
    private String SingleRes;
    private String SampleRes;
    private String CharRes;
    private String Samples;
    private String MstrChar;
    private String VerionNo;
    private String PlantChar;
    private String Method;
    private String MethodTxt;
    private String VrsnMethd;
    private String PlntMthd;
    private String SmplQuant;
    private String SmplUnit;
    private String SmplUnitt;
    private String SmplUnitc;
    private String PhysSmpl;
    private String CharWeigh;
    private String CharWeighFac;
    private String CharWeighTxt;
    private String InfoField1;
    private String InfoField2;
    private String InfoField3;
    private String DecPlaces;
    private String MeasUnit;
    private String MeasUnitt;
    private String MeasUnitc;
    private String TargetVal;
    private String UpTolLmt;
    private String LwTolLmt;
    private String UpLmt1;
    private String LwLmt1;
    private String UpLmt2;
    private String LwLmt2;
    private String UpPlsLmt;
    private String LwPlsLmt;
    private String UpCtrl1;
    private String LwCtrl1;
    private String UpCtrl2;
    private String LwCtrl2;
    private String ValnType;
    private String AcceptNo;
    private String RejectNo;
    private String EnteredBy;


    public InspectionCharacteristic(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public InspectionCharacteristic(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity);
    }

    public static ArrayList<InspectionCharacteristic> getInspOprCharList(String inspLot, String inspOpr, String inspPoint) {
        ResponseObject result = null;
        ArrayList<InspectionCharacteristic> inspOprPointCharList = null;
        try {
            String entitySetName = ZCollections.WO_INSPECTIONLOT_CHARACTERISTIC_ENTITY_COLLECTION;
            String resPath = entitySetName;
            resPath += "?$filter=InspLot eq '" + inspLot + "' and InspOper eq '" + inspOpr + "' and InspPoint eq '" + inspPoint + "'&$orderby=InspChar";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities, ZAppSettings.FetchLevel.All);
                inspOprPointCharList = (ArrayList<InspectionCharacteristic>) result.Content();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(InspectionLot.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return inspOprPointCharList;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<InspectionCharacteristic> inspOprPointCharList = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    inspOprPointCharList.add(new InspectionCharacteristic(entity, ZAppSettings.FetchLevel.All));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", inspOprPointCharList);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(InspectionCharacteristic.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void initializeEntityProperties() {
        this.setEntityType(ZCollections.WO_INSPECTIONLOT_CHARACTERISTIC_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.WO_INSPECTIONLOT_CHARACTERISTIC_ENTITY_COLLECTION);
        this.addKeyFieldNames("InspLot");
        this.addKeyFieldNames("InspOper");
        this.addKeyFieldNames("InspPoint");
        this.addKeyFieldNames("InspChar");
    }

    public String getScope() {
        return Scope;
    }

    public void setScope(String scope) {
        Scope = scope;
    }

    public String getInfoField1() {
        return InfoField1;
    }

    public void setInfoField1(String infoField1) {
        InfoField1 = infoField1;
    }

    public String getInfoField2() {
        return InfoField2;
    }

    public void setInfoField2(String infoField2) {
        InfoField2 = infoField2;
    }

    public String getInfoField3() {
        return InfoField3;
    }

    public void setInfoField3(String infoField3) {
        InfoField3 = infoField3;
    }

    public String getInspChar() {
        return InspChar;
    }

    public void setInspChar(String inspChar) {
        InspChar = inspChar;
    }

    public String getCharDescr() {
        return CharDescr;
    }

    public void setCharDescr(String charDescr) {
        CharDescr = charDescr;
    }

    public String getCharType() {
        return CharType;
    }

    public void setCharType(String charType) {
        CharType = charType;
    }

    public String getConfirmNo() {
        return ConfirmNo;
    }

    public void setConfirmNo(String confirmNo) {
        ConfirmNo = confirmNo;
    }

    public String getObligatory() {
        return Obligatory;
    }

    public void setObligatory(String obligatory) {
        Obligatory = obligatory;
    }

    public String getSingleRes() {
        return SingleRes;
    }

    public void setSingleRes(String singleRes) {
        SingleRes = singleRes;
    }

    public String getSampleRes() {
        return SampleRes;
    }

    public void setSampleRes(String sampleRes) {
        SampleRes = sampleRes;
    }

    public String getCharRes() {
        return CharRes;
    }

    public void setCharRes(String charRes) {
        CharRes = charRes;
    }

    public String getSamples() {
        return Samples;
    }

    public void setSamples(String samples) {
        Samples = samples;
    }

    public String getMstrChar() {
        return MstrChar;
    }

    public void setMstrChar(String mstrChar) {
        MstrChar = mstrChar;
    }

    public String getVerionNo() {
        return VerionNo;
    }

    public void setVerionNo(String verionNo) {
        VerionNo = verionNo;
    }

    public String getPlantChar() {
        return PlantChar;
    }

    public void setPlantChar(String plantChar) {
        PlantChar = plantChar;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public String getMethodTxt() {
        return MethodTxt;
    }

    public void setMethodTxt(String methodTxt) {
        MethodTxt = methodTxt;
    }

    public String getVrsnMethd() {
        return VrsnMethd;
    }

    public void setVrsnMethd(String vrsnMethd) {
        VrsnMethd = vrsnMethd;
    }

    public String getPlntMthd() {
        return PlntMthd;
    }

    public void setPlntMthd(String plntMthd) {
        PlntMthd = plntMthd;
    }

    public String getSmplQuant() {
        return SmplQuant;
    }

    public void setSmplQuant(String smplQuant) {
        SmplQuant = smplQuant;
    }

    public String getSmplUnit() {
        return SmplUnit;
    }

    public void setSmplUnit(String smplUnit) {
        SmplUnit = smplUnit;
    }

    public String getSmplUnitt() {
        return SmplUnitt;
    }

    public void setSmplUnitt(String smplUnitt) {
        SmplUnitt = smplUnitt;
    }

    public String getSmplUnitc() {
        return SmplUnitc;
    }

    public void setSmplUnitc(String smplUnitc) {
        SmplUnitc = smplUnitc;
    }

    public String getPhysSmpl() {
        return PhysSmpl;
    }

    public void setPhysSmpl(String physSmpl) {
        PhysSmpl = physSmpl;
    }

    public String getCharWeigh() {
        return CharWeigh;
    }

    public void setCharWeigh(String charWeigh) {
        CharWeigh = charWeigh;
    }

    public String getCharWeighFac() {
        return CharWeighFac;
    }

    public void setCharWeighFac(String charWeighFac) {
        CharWeighFac = charWeighFac;
    }

    public String getCharWeighTxt() {
        return CharWeighTxt;
    }

    public void setCharWeighTxt(String charWeighTxt) {
        CharWeighTxt = charWeighTxt;
    }

    public String getDecPlaces() {
        return DecPlaces;
    }

    public void setDecPlaces(String decPlaces) {
        DecPlaces = decPlaces;
    }

    public String getMeasUnit() {
        return MeasUnit;
    }

    public void setMeasUnit(String measUnit) {
        MeasUnit = measUnit;
    }

    public String getMeasUnitt() {
        return MeasUnitt;
    }

    public void setMeasUnitt(String measUnitt) {
        MeasUnitt = measUnitt;
    }

    public String getMeasUnitc() {
        return MeasUnitc;
    }

    public void setMeasUnitc(String measUnitc) {
        MeasUnitc = measUnitc;
    }

    public String getTargetVal() {
        return TargetVal;
    }

    public void setTargetVal(String targetVal) {
        TargetVal = targetVal;
    }

    public String getUpTolLmt() {
        return UpTolLmt;
    }

    public void setUpTolLmt(String upTolLmt) {
        UpTolLmt = upTolLmt;
    }

    public String getLwTolLmt() {
        return LwTolLmt;
    }

    public void setLwTolLmt(String lwTolLmt) {
        LwTolLmt = lwTolLmt;
    }

    public String getUpLmt1() {
        return UpLmt1;
    }

    public void setUpLmt1(String upLmt1) {
        UpLmt1 = upLmt1;
    }

    public String getLwLmt1() {
        return LwLmt1;
    }

    public void setLwLmt1(String lwLmt1) {
        LwLmt1 = lwLmt1;
    }

    public String getUpLmt2() {
        return UpLmt2;
    }

    public void setUpLmt2(String upLmt2) {
        UpLmt2 = upLmt2;
    }

    public String getLwLmt2() {
        return LwLmt2;
    }

    public void setLwLmt2(String lwLmt2) {
        LwLmt2 = lwLmt2;
    }

    public String getUpPlsLmt() {
        return UpPlsLmt;
    }

    public void setUpPlsLmt(String upPlsLmt) {
        UpPlsLmt = upPlsLmt;
    }

    public String getLwPlsLmt() {
        return LwPlsLmt;
    }

    public void setLwPlsLmt(String lwPlsLmt) {
        LwPlsLmt = lwPlsLmt;
    }

    public String getUpCtrl1() {
        return UpCtrl1;
    }

    public void setUpCtrl1(String upCtrl1) {
        UpCtrl1 = upCtrl1;
    }

    public String getLwCtrl1() {
        return LwCtrl1;
    }

    public void setLwCtrl1(String lwCtrl1) {
        LwCtrl1 = lwCtrl1;
    }

    public String getUpCtrl2() {
        return UpCtrl2;
    }

    public void setUpCtrl2(String upCtrl2) {
        UpCtrl2 = upCtrl2;
    }

    public String getLwCtrl2() {
        return LwCtrl2;
    }

    public void setLwCtrl2(String lwCtrl2) {
        LwCtrl2 = lwCtrl2;
    }

    public String getValnType() {
        return ValnType;
    }

    public void setValnType(String valnType) {
        ValnType = valnType;
    }

    public String getAcceptNo() {
        return AcceptNo;
    }

    public void setAcceptNo(String acceptNo) {
        AcceptNo = acceptNo;
    }

    public String getRejectNo() {
        return RejectNo;
    }

    public void setRejectNo(String rejectNo) {
        RejectNo = rejectNo;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatusDesc() {
        return StatusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        StatusDesc = statusDesc;
    }

    public String getInspLot() {
        return InspLot;
    }

    public void setInspLot(String inspLot) {
        InspLot = inspLot;
    }

    public String getInspOper() {
        return InspOper;
    }

    public void setInspOper(String inspOper) {
        InspOper = inspOper;
    }

    public String getInspPoint() {
        return InspPoint;
    }

    public void setInspPoint(String inspPoint) {
        InspPoint = inspPoint;
    }

    public String getSelectedSet() {
        return SelectedSet;
    }

    public void setSelectedSet(String selectedSet) {
        SelectedSet = selectedSet;
    }
}
