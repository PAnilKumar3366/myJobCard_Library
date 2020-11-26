package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.MeasurementPointReading;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by lenovo on 05-05-2016.
 */
public class EquipmentMeasurementPoint extends BaseEntity {

    private String MeasuringPoint;

    //Fields
    private String OBjNoMeasPObj;
    private String MeasPosition;
    private boolean RefMeasPos;
    private String Description;
    private String Language;
    private boolean LongText;
    private String MeasPtCategory;
    private boolean RefMeasPoint;
    private GregorianCalendar CreatedOn;
    private String CreatedbBy;
    private GregorianCalendar ChangedOn;
    private String ChangedBy;
    private String AuthorizGroup;
    private boolean MeasPtInact;
    private String DeletionFlag;
    private String Assembly;
    private String Internalchar;
    private boolean RefCharact;
    private int FloatPointExp;
    private Double TargetValue;
    private boolean ContainsValue;
    private boolean RefTargetVal;
    private String Text;
    private boolean RefText;
    private Double LoMRLimit;
    private boolean LoMRContainsvalue;
    private Double UpMRLimit;
    private boolean UpMRContainvalue;
    private String MeasRangeUnit;
    private boolean Counter;
    private boolean Backwards;
    private boolean Supported;
    private String CatalogType;
    private String CodeGroup;
    private boolean RefCodeGroup;
    private boolean ValCodeSuff;
    private String Characteristic;
    private String Format;
    private boolean NegValsAllowed;
    private String IntMeasUnit;
    private String FunctionalLoc;
    private String Equipment;
    private String LimitMinChar;
    private String LimitMinChar1;
    private String LimitMaxChar;
    private String LimitMaxChar1;
    private String RolloverChar;
    private String AnnualChar;
    private String TargetChar;
    private String UomChar;
    private boolean PrevDoc;
    private String MeasDocument;
    private GregorianCalendar MeasDocumentDate;
    private Time MeasDocumentTime;
    private Double MeasReading;
    private Double CntrReading;
    private Double ReadingDifference;
    private String MeasCatalogType;
    private String MeasCatalogGroup;
    private String MeasValuationCode;
    private String MeasText;
    private String MeasCodeText;
    private String MeasDescription;
    private String PReadgChar;
    private boolean measurementPointIsEquip = true;
    private String OperationNum;
    private MeasurementPointReading PointReading;
    private String ValuationCodeText;

    public EquipmentMeasurementPoint(ODataEntity entity, String oprNum) {
        this.setOperationNum(oprNum);
        create(entity);
    }
    //End of Fields

    public static ResponseObject getEquipmentMeasurementPoint(String eqpNum, ZAppSettings.FetchLevel fetchLevel, String mPoint, String oprNum) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String resourcePath = ZCollections.EQUIPMENT_MEASUREMENT_POINT_COLLECTION;
        String orderByUrl = "$orderby=MeasuringPoint";
        try {
            if (eqpNum == null || eqpNum.isEmpty())
                eqpNum = "";
            else
                eqpNum = String.valueOf(Integer.parseInt(eqpNum));

            switch (fetchLevel) {
                case List:
                    resourcePath = resourcePath + "?$filter=(Equipment eq '" + eqpNum + "')&$select=MeasuringPoint,MeasDescription,Description,FunctionalLoc,Equipment,MeasReading,MeasDocument,MeasDocumentDate,MeasRangeUnit,PReadgChar,LimitMinChar,LimitMaxChar,MeasValuationCode,ValCodeSuff,CatalogType,CodeGroup,PrevDoc,Counter" + "&" + orderByUrl;
                    break;
                case Single:
                    if (mPoint == null)
                        mPoint = "";
                    resourcePath = resourcePath + "?$filter=(MeasuringPoint eq '" + mPoint + "')";
                    break;
                case Count:
                    resourcePath += "/$count?$filter=Equipment eq '" + eqpNum + "'";
                    break;
                default:
                    resourcePath += "?$filter=(Equipment eq '" + eqpNum + "')" + "&" + orderByUrl;
                    break;
            }

            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.EQUIPMENT_MEASUREMENT_POINT_COLLECTION, resourcePath);
            if (fetchLevel != ZAppSettings.FetchLevel.Count)
                result = FromEntity((List<ODataEntity>) result.Content(), oprNum);
            else
                return result;
            if (result == null)
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(EquipmentMeasurementPoint.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
        }
        return result;
    }

    //getter & setter methods

    private static ResponseObject FromEntity(List<ODataEntity> entities, String oprNum) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<EquipmentMeasurementPoint> measurementPoints = new ArrayList<>();
                EquipmentMeasurementPoint point;
                for (ODataEntity entity : entities) {
                    point = new EquipmentMeasurementPoint(entity, oprNum);
                    measurementPoints.add(point);
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", measurementPoints);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(EquipmentMeasurementPoint.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    @Override
    public void create(ODataEntity entity) {
        ResponseObject result = null;
        try {
            super.create(entity);
            if (WorkOrder.getCurrWo() != null) {
                String woObjNum = WorkOrder.getCurrWo().getObjectNumber();
                result = MeasurementPointReading.getPointReading(MeasuringPoint, woObjNum, OperationNum, CatalogType, CodeGroup);
                if (!result.isError()) {
                    ArrayList<MeasurementPointReading> mpReadings = (ArrayList<MeasurementPointReading>) result.Content();
                    if (mpReadings != null && mpReadings.size() > 0) {
                        PointReading = mpReadings.get(0);
                    }
                }
            }
            if (isValCodeSuff()) {
                result = CatalogCode.getCatalogCode(CatalogType, CodeGroup, MeasValuationCode);
                if (!result.isError()) {
                    ArrayList<CatalogCode> codes = (ArrayList<CatalogCode>) result.Content();
                    if (codes != null && codes.size() > 0)
                        ValuationCodeText = codes.get(0).getCodeText();
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public String getMeasuringPoint() {
        return MeasuringPoint;
    }

    public void setMeasuringPoint(String measuringPoint) {
        MeasuringPoint = measuringPoint;
    }

    public String getOBjNoMeasPObj() {
        return OBjNoMeasPObj;
    }

    public void setOBjNoMeasPObj(String OBjNoMeasPObj) {
        this.OBjNoMeasPObj = OBjNoMeasPObj;
    }

    public String getMeasPosition() {
        return MeasPosition;
    }

    public void setMeasPosition(String measPosition) {
        MeasPosition = measPosition;
    }

    public boolean isRefMeasPos() {
        return RefMeasPos;
    }

    public void setRefMeasPos(boolean refMeasPos) {
        RefMeasPos = refMeasPos;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public boolean isLongText() {
        return LongText;
    }

    public void setLongText(boolean longText) {
        LongText = longText;
    }

    public String getMeasPtCategory() {
        return MeasPtCategory;
    }

    public void setMeasPtCategory(String measPtCategory) {
        MeasPtCategory = measPtCategory;
    }

    public boolean isRefMeasPoint() {
        return RefMeasPoint;
    }

    public void setRefMeasPoint(boolean refMeasPoint) {
        RefMeasPoint = refMeasPoint;
    }

    public GregorianCalendar getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(GregorianCalendar createdOn) {
        CreatedOn = createdOn;
    }

    public String getCreatedbBy() {
        return CreatedbBy;
    }

    public void setCreatedbBy(String createdbBy) {
        CreatedbBy = createdbBy;
    }

    public GregorianCalendar getChangedOn() {
        return ChangedOn;
    }

    public void setChangedOn(GregorianCalendar changedOn) {
        ChangedOn = changedOn;
    }

    public String getChangedBy() {
        return ChangedBy;
    }

    public void setChangedBy(String changedBy) {
        ChangedBy = changedBy;
    }

    public String getAuthorizGroup() {
        return AuthorizGroup;
    }

    public void setAuthorizGroup(String authorizGroup) {
        AuthorizGroup = authorizGroup;
    }

    public boolean isMeasPtInact() {
        return MeasPtInact;
    }

    public void setMeasPtInact(boolean measPtInact) {
        MeasPtInact = measPtInact;
    }

    public String getDeletionFlag() {
        return DeletionFlag;
    }

    public void setDeletionFlag(String deletionFlag) {
        DeletionFlag = deletionFlag;
    }

    public String getAssembly() {
        return Assembly;
    }

    public void setAssembly(String assembly) {
        Assembly = assembly;
    }

    public String getInternalchar() {
        return Internalchar;
    }

    public void setInternalchar(String internalchar) {
        Internalchar = internalchar;
    }

    public boolean isRefCharact() {
        return RefCharact;
    }

    public void setRefCharact(boolean refCharact) {
        RefCharact = refCharact;
    }

    public int getFloatPointExp() {
        return FloatPointExp;
    }

    public void setFloatPointExp(int floatPointExp) {
        FloatPointExp = floatPointExp;
    }

    public Double getTargetValue() {
        return TargetValue;
    }

    public void setTargetValue(Double targetValue) {
        TargetValue = targetValue;
    }

    public boolean isContainsValue() {
        return ContainsValue;
    }

    public void setContainsValue(boolean containsValue) {
        ContainsValue = containsValue;
    }

    public boolean isRefTargetVal() {
        return RefTargetVal;
    }

    public void setRefTargetVal(boolean refTargetVal) {
        RefTargetVal = refTargetVal;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public boolean isRefText() {
        return RefText;
    }

    public void setRefText(boolean refText) {
        RefText = refText;
    }

    public Double getLoMRLimit() {
        return LoMRLimit;
    }

    public void setLoMRLimit(Double loMRLimit) {
        LoMRLimit = loMRLimit;
    }

    public boolean isLoMRContainsvalue() {
        return LoMRContainsvalue;
    }

    public void setLoMRContainsvalue(boolean loMRContainsvalue) {
        LoMRContainsvalue = loMRContainsvalue;
    }

    public Double getUpMRLimit() {
        return UpMRLimit;
    }

    public void setUpMRLimit(Double upMRLimit) {
        UpMRLimit = upMRLimit;
    }

    public boolean isUpMRContainvalue() {
        return UpMRContainvalue;
    }

    public void setUpMRContainvalue(boolean upMRContainvalue) {
        UpMRContainvalue = upMRContainvalue;
    }

    public String getMeasRangeUnit() {
        return MeasRangeUnit;
    }

    public void setMeasRangeUnit(String measRangeUnit) {
        MeasRangeUnit = measRangeUnit;
    }

    public boolean getCounter() {
        return Counter;
    }

    public void setCounter(boolean counter) {
        Counter = counter;
    }

    public boolean isBackwards() {
        return Backwards;
    }

    public void setBackwards(boolean backwards) {
        Backwards = backwards;
    }

    public boolean isSupported() {
        return Supported;
    }

    public void setSupported(boolean supported) {
        Supported = supported;
    }

    public String getCatalogType() {
        return CatalogType;
    }

    public void setCatalogType(String catalogType) {
        CatalogType = catalogType;
    }

    public String getCodeGroup() {
        return CodeGroup;
    }

    public void setCodeGroup(String codeGroup) {
        CodeGroup = codeGroup;
    }

    public boolean isRefCodeGroup() {
        return RefCodeGroup;
    }

    public void setRefCodeGroup(boolean refCodeGroup) {
        RefCodeGroup = refCodeGroup;
    }

    public boolean isValCodeSuff() {
        return ValCodeSuff;
    }

    public void setValCodeSuff(boolean valCodeSuff) {
        ValCodeSuff = valCodeSuff;
    }

    public String getCharacteristic() {
        return Characteristic;
    }

    public void setCharacteristic(String characteristic) {
        Characteristic = characteristic;
    }

    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {
        Format = format;
    }

    public boolean isNegValsAllowed() {
        return NegValsAllowed;
    }

    public void setNegValsAllowed(boolean negValsAllowed) {
        NegValsAllowed = negValsAllowed;
    }

    public String getIntMeasUnit() {
        return IntMeasUnit;
    }

    public void setIntMeasUnit(String intMeasUnit) {
        IntMeasUnit = intMeasUnit;
    }

    public String getFunctionalLoc() {
        return FunctionalLoc;
    }

    public void setFunctionalLoc(String functionalLoc) {
        FunctionalLoc = functionalLoc;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getLimitMinChar() {
        return LimitMinChar;
    }

    public void setLimitMinChar(String limitMinChar) {
        LimitMinChar = limitMinChar;
    }

    public String getLimitMinChar1() {
        return LimitMinChar1;
    }

    public void setLimitMinChar1(String limitMinChar1) {
        LimitMinChar1 = limitMinChar1;
    }

    public String getLimitMaxChar() {
        return LimitMaxChar;
    }

    public void setLimitMaxChar(String limitMaxChar) {
        LimitMaxChar = limitMaxChar;
    }

    public String getLimitMaxChar1() {
        return LimitMaxChar1;
    }

    public void setLimitMaxChar1(String limitMaxChar1) {
        LimitMaxChar1 = limitMaxChar1;
    }

    public String getRolloverChar() {
        return RolloverChar;
    }

    public void setRolloverChar(String rolloverChar) {
        RolloverChar = rolloverChar;
    }

    public String getAnnualChar() {
        return AnnualChar;
    }

    public void setAnnualChar(String annualChar) {
        AnnualChar = annualChar;
    }

    public String getTargetChar() {
        return TargetChar;
    }

    public void setTargetChar(String targetChar) {
        TargetChar = targetChar;
    }

    public String getUomChar() {
        return UomChar;
    }

    public void setUomChar(String uomChar) {
        UomChar = uomChar;
    }

    public boolean isPrevDoc() {
        return PrevDoc;
    }

    public void setPrevDoc(boolean prevDoc) {
        PrevDoc = prevDoc;
    }

    public String getMeasDocument() {
        return MeasDocument;
    }

    public void setMeasDocument(String measDocument) {
        MeasDocument = measDocument;
    }

    public GregorianCalendar getMeasDocumentDate() {
        return MeasDocumentDate;
    }

    public void setMeasDocumentDate(GregorianCalendar measDocumentDate) {
        MeasDocumentDate = measDocumentDate;
    }

    public Time getMeasDocumentTime() {
        return MeasDocumentTime;
    }

    public void setMeasDocumentTime(Time measDocumentTime) {
        MeasDocumentTime = measDocumentTime;
    }

    public Double getMeasReading() {
        return MeasReading;
    }

    public void setMeasReading(Double measReading) {
        MeasReading = measReading;
    }

    public Double getCntrReading() {
        return CntrReading;
    }

    public void setCntrReading(Double cntrReading) {
        CntrReading = cntrReading;
    }

    public Double getReadingDifference() {
        return ReadingDifference;
    }

    public void setReadingDifference(Double readingDifference) {
        ReadingDifference = readingDifference;
    }

    public String getMeasCatalogType() {
        return MeasCatalogType;
    }

    public void setMeasCatalogType(String measCatalogType) {
        MeasCatalogType = measCatalogType;
    }

    public String getMeasCatalogGroup() {
        return MeasCatalogGroup;
    }

    public void setMeasCatalogGroup(String measCatalogGroup) {
        MeasCatalogGroup = measCatalogGroup;
    }

    public String getMeasValuationCode() {
        return MeasValuationCode;
    }

    public void setMeasValuationCode(String measValuationCode) {
        MeasValuationCode = measValuationCode;
    }

    public String getMeasText() {
        return MeasText;
    }

    public void setMeasText(String measText) {
        MeasText = measText;
    }

    public String getMeasCodeText() {
        return MeasCodeText;
    }

    public void setMeasCodeText(String measCodeText) {
        MeasCodeText = measCodeText;
    }

    public String getMeasDescription() {
        return MeasDescription;
    }

    public void setMeasDescription(String measDescription) {
        MeasDescription = measDescription;
    }

    public boolean isMeasurementPointIsEquip() {
        return measurementPointIsEquip;
    }

    public void setMeasurementPointIsEquip(boolean measurementPointIsEquip) {
        this.measurementPointIsEquip = measurementPointIsEquip;
    }

    public String getPReadgChar() {
        return PReadgChar;
    }

    public void setPReadgChar(String PReadgChar) {
        this.PReadgChar = PReadgChar;
    }
    //End of getter & setter methods

    public String getOperationNum() {
        return OperationNum;
    }

    public void setOperationNum(String operationNum) {
        OperationNum = operationNum;
    }

    public boolean isReadingOutOfRange() {
        try {
            String resPath = ZCollections.EQUIPMENT_MEASUREMENT_POINT_COLLECTION + "/$count?$filter=MeasuringPoint eq '" + getMeasuringPoint() + "' and ((LimitMinChar ne '' and MeasReading lt LoMRLimit) or (LimitMaxChar ne '' and MeasReading gt UpMRLimit)) and PrevDoc eq true and ValCodeSuff eq false";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.EQUIPMENT_MEASUREMENT_POINT_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                if (Integer.parseInt(String.valueOf(response.Content())) > 0)
                    return true;
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(EquipmentMeasurementPoint.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return false;
    }

    public MeasurementPointReading getPointReading() {
        return PointReading;
    }

    public void setPointReading(MeasurementPointReading pointReading) {
        PointReading = pointReading;
    }

    public String getValuationCodeText() {
        return ValuationCodeText;
    }

    public void setValuationCodeText(String valuationCodeText) {
        ValuationCodeText = valuationCodeText;
    }
}
