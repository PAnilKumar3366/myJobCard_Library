package com.ods.myjobcard_library.entities.qmentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class InspectionResultsGet extends ZBaseEntity {
    private String EnteredBy;
    private String InspLot;
    private String InspOper;
    private String InspChar;
    private String InspSample;
    private String ResNo;
    private String ExtNo;
    private String LastRes;
    private GregorianCalendar InspDate;
    private Time InspTime;
    private String ResValue;
    private String ResAttr;
    private String ResInval;
    private String ResValuat;
    private String ErrClass;
    private String Defects;
    private String Inspector;
    private String AddInfo1;
    private String AddInfo2;
    private String Remark;
    private String Code1;
    private String CodeGrp1;
    private String Code2;
    private String CodeGrp2;
    private String Code3;
    private String CodeGrp3;
    private String Code4;
    private String CodeGrp4;
    private String Code5;
    private String CodeGrp5;
    private String OriginalValue;
    private String InputProcessing;
    private int DiffDeciPlaces;
    private String Description;

    public InspectionResultsGet(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public InspectionResultsGet() {
        initializeEntityProperties();
    }

    public InspectionResultsGet(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity);
    }

    public static ArrayList<InspectionResultsGet> getInspResultGet(String inspLotNum, String inspOprNum, String inspPoint) {
        ResponseObject result = null;
        ArrayList<InspectionResultsGet> inspResults = null;
        try {
            String entitySetName = ZCollections.WO_INSPECTIONLOT_RESULTGET_COLLECTION;
            String resPath = entitySetName;
            resPath += "?$filter=InspLot eq '" + inspLotNum + "' and InspOper eq '" + inspOprNum + "' and InspSample eq '" + inspPoint + "'&$orderby=ResNo";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities, ZAppSettings.FetchLevel.All);
                inspResults = (ArrayList<InspectionResultsGet>) result.Content();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(InspectionLot.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return inspResults;
    }

    public static ArrayList<InspectionResultsGet> getInspCharResultGet(String inspLotNum, String inspOprNum, String inspPoint, String inspChar) {
        ResponseObject result = null;
        ArrayList<InspectionResultsGet> inspResults = new ArrayList<>();
        try {
            String entitySetName = ZCollections.WO_INSPECTIONLOT_RESULTGET_COLLECTION;
            String resPath = entitySetName;
            resPath += "?$filter=InspLot eq '" + inspLotNum + "' and InspOper eq '" + inspOprNum + "' and InspSample eq '" + inspPoint + "' and InspChar eq '" + inspChar + "'&$orderby=ResNo";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities, ZAppSettings.FetchLevel.All);
                inspResults = (ArrayList<InspectionResultsGet>) result.Content();
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(InspectionLot.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return inspResults;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<InspectionResultsGet> inspectionResultsGets = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    inspectionResultsGets.add(new InspectionResultsGet(entity, ZAppSettings.FetchLevel.All));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", inspectionResultsGets);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(InspectionPoint.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void initializeEntityProperties() {
        this.setEntityType(ZCollections.WO_INSPECTIONLOT_RESULTGET_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.WO_INSPECTIONLOT_RESULTGET_COLLECTION);
        this.addKeyFieldNames("InspLot");
        this.addKeyFieldNames("InspOper");
        this.addKeyFieldNames("InspChar");
        this.addKeyFieldNames("InspSample");
        this.addKeyFieldNames("ResNo");
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
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

    public String getInspChar() {
        return InspChar;
    }

    public void setInspChar(String inspChar) {
        InspChar = inspChar;
    }

    public String getInspSample() {
        return InspSample;
    }

    public void setInspSample(String inspSample) {
        InspSample = inspSample;
    }

    public String getResNo() {
        return ResNo;
    }

    public void setResNo(String resNo) {
        ResNo = resNo;
    }

    public String getExtNo() {
        return ExtNo;
    }

    public void setExtNo(String extNo) {
        ExtNo = extNo;
    }

    public String getLastRes() {
        return LastRes;
    }

    public void setLastRes(String lastRes) {
        LastRes = lastRes;
    }

    public GregorianCalendar getInspDate() {
        return InspDate;
    }

    public void setInspDate(GregorianCalendar inspDate) {
        InspDate = inspDate;
    }

    public Time getInspTime() {
        return InspTime;
    }

    public void setInspTime(Time inspTime) {
        InspTime = inspTime;
    }

    public String getResValue() {
        return ResValue;
    }

    public void setResValue(String resValue) {
        ResValue = resValue;
    }

    public String getResAttr() {
        return ResAttr;
    }

    public void setResAttr(String resAttr) {
        ResAttr = resAttr;
    }

    public String getResInval() {
        return ResInval;
    }

    public void setResInval(String resInval) {
        ResInval = resInval;
    }

    public String getResValuat() {
        return ResValuat;
    }

    public void setResValuat(String resValuat) {
        ResValuat = resValuat;
    }

    public String getErrClass() {
        return ErrClass;
    }

    public void setErrClass(String errClass) {
        ErrClass = errClass;
    }

    public String getDefects() {
        return Defects;
    }

    public void setDefects(String defects) {
        Defects = defects;
    }

    public String getInspector() {
        return Inspector;
    }

    public void setInspector(String inspector) {
        Inspector = inspector;
    }

    public String getAddInfo1() {
        return AddInfo1;
    }

    public void setAddInfo1(String addInfo1) {
        AddInfo1 = addInfo1;
    }

    public String getAddInfo2() {
        return AddInfo2;
    }

    public void setAddInfo2(String addInfo2) {
        AddInfo2 = addInfo2;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getCode1() {
        return Code1;
    }

    public void setCode1(String code1) {
        Code1 = code1;
    }

    public String getCodeGrp1() {
        return CodeGrp1;
    }

    public void setCodeGrp1(String codeGrp1) {
        CodeGrp1 = codeGrp1;
    }

    public String getCode2() {
        return Code2;
    }

    public void setCode2(String code2) {
        Code2 = code2;
    }

    public String getCodeGrp2() {
        return CodeGrp2;
    }

    public void setCodeGrp2(String codeGrp2) {
        CodeGrp2 = codeGrp2;
    }

    public String getCode3() {
        return Code3;
    }

    public void setCode3(String code3) {
        Code3 = code3;
    }

    public String getCodeGrp3() {
        return CodeGrp3;
    }

    public void setCodeGrp3(String codeGrp3) {
        CodeGrp3 = codeGrp3;
    }

    public String getCode4() {
        return Code4;
    }

    public void setCode4(String code4) {
        Code4 = code4;
    }

    public String getCodeGrp4() {
        return CodeGrp4;
    }

    public void setCodeGrp4(String codeGrp4) {
        CodeGrp4 = codeGrp4;
    }

    public String getCode5() {
        return Code5;
    }

    public void setCode5(String code5) {
        Code5 = code5;
    }

    public String getCodeGrp5() {
        return CodeGrp5;
    }

    public void setCodeGrp5(String codeGrp5) {
        CodeGrp5 = codeGrp5;
    }

    public String getOriginalValue() {
        return OriginalValue;
    }

    public void setOriginalValue(String originalValue) {
        OriginalValue = originalValue;
    }

    public String getInputProcessing() {
        return InputProcessing;
    }

    public void setInputProcessing(String inputProcessing) {
        InputProcessing = inputProcessing;
    }

    public int getDiffDeciPlaces() {
        return DiffDeciPlaces;
    }

    public void setDiffDeciPlaces(int diffDeciPlaces) {
        DiffDeciPlaces = diffDeciPlaces;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
