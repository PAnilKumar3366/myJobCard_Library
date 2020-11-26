package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.MeasurementPointReading;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by lenovo on 05-05-2016.
 */
public class FLMeasurementPoint extends BaseEntity {

    private String MeasuringPoint;

    //Fields
    private String FunctionalLoc;
    private String Equipment;
    private String Description;
    private String MeasPtCategory;
    private Double LoMRLimit;
    private boolean LoMRContainsValue;
    private Double UpMRLimit;
    private boolean UpMRContainsvalue;
    private String MeasRangeUnit;
    private String CatalogType;
    private String CodeGroup;
    private String Characteristic;
    private String LimitMinChar;
    private String LimitMinChar1;
    private String LimitMaxChar;
    private String LimitMaxChar1;
    private String UomChar;
    private boolean PrevDoc;
    private String MeasDocument;
    private GregorianCalendar MeasDocumentDate;
    private Time MeasDocumentTime;
    private Double MeasReading;
    private Double MeasCntrReading;
    private Double MeasDifference;
    private String MeasCatlaogType;
    private String MeasCodeGroup;
    private String MeasValuationCode;
    private String MeasText;
    private String MeasureCodeTxt;
    private String MeasDescription;
    private boolean ValCodeSuff;
    private String PReadgChar;
    private boolean Counter;
    private boolean measurementPointIsEquip = false;
    private String OperationNum;
    private MeasurementPointReading PointReading;
    private String ValuationCodeText;

    public FLMeasurementPoint(ODataEntity entity, String oprNum) {
        this.OperationNum = oprNum;
        create(entity);
    }
    //End of Fields

    public static ResponseObject getFLMeasurementPoint(String funcLocNum, ZAppSettings.FetchLevel fetchLevel, String mPoint, String oprNum) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String resourcePath = ZCollections.FL_MEASUREMENT_POINT_COLLECTION;
        String orderByUrl = "$orderby=MeasuringPoint";
        try {
            if (funcLocNum == null)
                funcLocNum = "";

            switch (fetchLevel) {
                case List:
                    resourcePath = resourcePath + "?$filter=(FunctionalLoc eq '" + funcLocNum + "')&$select=MeasuringPoint,MeasDescription,Description,FunctionalLoc,Equipment,MeasReading,MeasDocumentDate,MeasRangeUnit,LimitMinChar,LimitMaxChar,PReadgChar,MeasValuationCode,ValCodeSuff,CatalogType,CodeGroup,PrevDoc,Counter,MeasDocument" + "&" + orderByUrl;
                    break;
                case Single:
                    if (mPoint == null)
                        mPoint = "";
                    resourcePath = resourcePath + "?$filter=(MeasuringPoint eq '" + mPoint + "')";
                    break;
                case Count:
                    resourcePath += "/$count?$filter=FunctionalLoc eq '" + funcLocNum + "'";
                    break;
                default:
                    resourcePath += "?$filter=(FunctionalLoc eq '" + funcLocNum + "')" + "&" + orderByUrl;
                    break;
            }
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.FL_MEASUREMENT_POINT_COLLECTION, resourcePath);
            if (fetchLevel != ZAppSettings.FetchLevel.Count)
                result = FromEntity((List<ODataEntity>) result.Content(), oprNum);
            else
                return result;
            if (result == null)
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(FLMeasurementPoint.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, String oprNum) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<FLMeasurementPoint> measurementPoints = new ArrayList<>();
                FLMeasurementPoint point;
                for (ODataEntity entity : entities) {
                    point = new FLMeasurementPoint(entity, oprNum);
                    measurementPoints.add(point);
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", measurementPoints);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(FLMeasurementPoint.class, ZAppSettings.LogLevel.Error, e.getMessage());
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
                if (result != null && !result.isError()) {
                    ArrayList<MeasurementPointReading> mpReadings = (ArrayList<MeasurementPointReading>) result.Content();
                    if (mpReadings != null && mpReadings.size() > 0) {
                        PointReading = mpReadings.get(0);
                    }
                }
            }
            if (isValCodeSuff()) {
                result = CatalogCode.getCatalogCode(CatalogType, CodeGroup, MeasValuationCode);
                if (result != null && !result.isError()) {
                    ArrayList<CatalogCode> codes = (ArrayList<CatalogCode>) result.Content();
                    if (codes != null && codes.size() > 0)
                        ValuationCodeText = codes.get(0).getCodeText();
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public boolean isReadingOutOfRange() {
        try {
            String resPath = ZCollections.FL_MEASUREMENT_POINT_COLLECTION + "/$count?$filter=MeasuringPoint eq '" + getMeasuringPoint() + "' and ((LimitMinChar ne '' and MeasReading lt LoMRLimit) or (LimitMaxChar ne '' and MeasReading gt UpMRLimit)) and PrevDoc eq true and ValCodeSuff eq false";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.FL_MEASUREMENT_POINT_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                if (Integer.parseInt(String.valueOf(response.Content())) > 0)
                    return true;
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(FLMeasurementPoint.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return false;
    }

    //getter & setter methods

    public String getMeasuringPoint() {
        return MeasuringPoint;
    }

    public void setMeasuringPoint(String measuringPoint) {
        MeasuringPoint = measuringPoint;
    }

    public String getMeasDescription() {
        return MeasDescription;
    }

    public void setMeasDescription(String measDescription) {
        this.MeasDescription = measDescription;
    }

    public String getMeasPtCategory() {
        return MeasPtCategory;
    }

    public void setMeasPtCategory(String measPtCategory) {
        MeasPtCategory = measPtCategory;
    }

    public Double getLoMRLimit() {
        return LoMRLimit;
    }

    public void setLoMRLimit(Double loMRLimit) {
        LoMRLimit = loMRLimit;
    }

    public boolean isLoMRContainsValue() {
        return LoMRContainsValue;
    }

    public void setLoMRContainsValue(boolean loMRContainsValue) {
        LoMRContainsValue = loMRContainsValue;
    }

    public Double getUpMRLimit() {
        return UpMRLimit;
    }

    public void setUpMRLimit(Double upMRLimit) {
        UpMRLimit = upMRLimit;
    }

    public boolean isUpMRContainsvalue() {
        return UpMRContainsvalue;
    }

    public void setUpMRContainsvalue(boolean upMRContainsvalue) {
        UpMRContainsvalue = upMRContainsvalue;
    }

    public String getMeasRangeUnit() {
        return MeasRangeUnit;
    }

    public void setMeasRangeUnit(String measRangeUnit) {
        MeasRangeUnit = measRangeUnit;
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

    public String getCharacteristic() {
        return Characteristic;
    }

    public void setCharacteristic(String characteristic) {
        Characteristic = characteristic;
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

    public Double getMeasCntrReading() {
        return MeasCntrReading;
    }

    public void setMeasCntrReading(Double measCntrReading) {
        MeasCntrReading = measCntrReading;
    }

    public Double getMeasDifference() {
        return MeasDifference;
    }

    public void setMeasDifference(Double measDifference) {
        MeasDifference = measDifference;
    }

    public String getMeasCatlaogType() {
        return MeasCatlaogType;
    }

    public void setMeasCatlaogType(String measCatlaogType) {
        MeasCatlaogType = measCatlaogType;
    }

    public String getMeasCodeGroup() {
        return MeasCodeGroup;
    }

    public void setMeasCodeGroup(String measCodeGroup) {
        MeasCodeGroup = measCodeGroup;
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

    public String getMeasureCodeTxt() {
        return MeasureCodeTxt;
    }

    public void setMeasureCodeTxt(String measureCodeTxt) {
        MeasureCodeTxt = measureCodeTxt;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isMeasurementPointIsEquip() {
        return measurementPointIsEquip;
    }

    public void setMeasurementPointIsEquip(boolean measurementPointIsEquip) {
        this.measurementPointIsEquip = measurementPointIsEquip;
    }

    public boolean isValCodeSuff() {
        return ValCodeSuff;
    }

    public void setValCodeSuff(boolean valCodeSuff) {
        ValCodeSuff = valCodeSuff;
    }

    public String getPReadgChar() {
        return PReadgChar;
    }

    public void setPReadgChar(String PReadgChar) {
        this.PReadgChar = PReadgChar;
    }

    public boolean getCounter() {
        return Counter;
    }

    public void setCounter(boolean counter) {
        Counter = counter;
    }

    public String getOperationNum() {
        return OperationNum;
    }

    public void setOperationNum(String operationNum) {
        OperationNum = operationNum;
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

//End of getter & setter methods


}
