package com.ods.myjobcard_library.entities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ctentities.CatalogCode;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by lenovo on 31-08-2016.
 */
public class MeasurementPointReading extends ZBaseEntity {

    private String MeasuringPoint;
    private String WOObjectNum;
    private String OperationNum;
    private String Counter;
    private GregorianCalendar MeasDocumentDate;
    private Time MeasDocumentTime;
    private Double MeasReading;
    private String MeasText;
    private String FunctionalLocation;
    private String Equipment;
    private String MeasDocument;
    private String OpObjectNumber;
    private String MeasReadingChar;
    private String MeasDescription;
    private String Description;
    private String MeasRangeUnit;
    private String LimitMinChar;
    private String LimitMaxChar;
    private String MeasValuationCode;
    private boolean ValCodeSuff;
    private String CatalogType;
    private String CodeGroup;
    private boolean PrevDoc;
    private boolean RefCounter;
    //extra fields
    private String ValuationCodeText;
    private MeasurementPointReading PointReading;

    //    Constructors
    public MeasurementPointReading() {
        initializeEntityProperties();
    }

    public MeasurementPointReading(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity);
    }

    public MeasurementPointReading(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public MeasurementPointReading(ODataEntity entity, String oprNum) {
        initializeEntityProperties();
        this.setOperationNum(oprNum);
        createList(entity);
    }

    public static ResponseObject getPointReading(String point, String objNum, String oprNum, String catalogType, String codeGroup) {
        ResponseObject result = null;
        try {
            String resPath = ZCollections.MEASPOINT_READING_COLLECTION;
            if (oprNum == null)
                oprNum = "";
            if (point == null)
                point = "";
            if (objNum == null)
                objNum = "";

            resPath += "?$filter=(MeasuringPoint eq '" + point + "' and WOObjectNum eq '" + objNum + "' and OperationNum eq '" + oprNum + "')";

            result = DataHelper.getInstance().getEntities(ZCollections.MEASPOINT_READING_COLLECTION, resPath);
            if (!result.isError()) {
                ArrayList<MeasurementPointReading> codes = (ArrayList<MeasurementPointReading>) FromEntity((List<ODataEntity>) result.Content()).Content();
                if (codes != null)
                    result.setContent(codes);
            }
            return result;
        } catch (Exception e) {
            DliteLogger.WriteLog(MeasurementPointReading.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
    }

    public static ResponseObject getEquipmentMeasurementPoint(String eqpNum, ZAppSettings.FetchLevel fetchLevel, String mPoint, String oprNum) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String resourcePath = ZCollections.MEASPOINT_READING_COLLECTION;
        String orderByUrl = "$orderby=MeasuringPoint";
        try {
            if (eqpNum == null || eqpNum.isEmpty())
                eqpNum = "";
            /*else
                eqpNum = String.valueOf(Integer.parseInt(eqpNum));*/

            switch (fetchLevel) {
                case List:
                    resourcePath = resourcePath + "?$filter=(Equipment eq '" + eqpNum + "' and OpObjectNumber eq '' and WOObjectNum eq '' and OperationNum  eq '')&$select=MeasuringPoint,MeasDescription,Description,FunctionalLocation,Equipment,MeasReading,MeasDocument,MeasDocumentDate,MeasRangeUnit,MeasReadingChar,LimitMinChar,LimitMaxChar,MeasValuationCode,ValCodeSuff,CatalogType,CodeGroup,PrevDoc,Counter,RefCounter,OpObjectNumber" + "&" + orderByUrl;
                    break;
                case Single:
                    if (mPoint == null)
                        mPoint = "";
                    resourcePath = resourcePath + "?$filter=(MeasuringPoint eq '" + mPoint + "')";
                    break;
                case Count:
                    resourcePath += "/$count?$filter=(Equipment eq '" + eqpNum + "' and OpObjectNumber eq '' and WOObjectNum eq '' and OperationNum  eq '')";
                    break;
                default:
                    resourcePath += "?$filter=(Equipment eq '" + eqpNum + "')" + "&" + orderByUrl;
                    break;
            }
            if (fetchLevel != ZAppSettings.FetchLevel.Count) {
                result = getObjListFromStore(ZCollections.MEASPOINT_READING_COLLECTION, resourcePath, oprNum);
                if (!result.isError()) {
                    ArrayList<MeasurementPointReading> codes = (ArrayList<MeasurementPointReading>) result.Content();
                    if (codes != null)
                        result.setContent(codes);
                }
                return result;
            } else {
                dataHelper = DataHelper.getInstance();
                result = dataHelper.getEntities(ZCollections.MEASPOINT_READING_COLLECTION, resourcePath);

                return result;
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(MeasurementPointReading.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
        }
        return result;
    }

    public static ResponseObject getFLMeasurementPoint(String funcLocNum, ZAppSettings.FetchLevel fetchLevel, String mPoint, String oprNum) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String resourcePath = ZCollections.MEASPOINT_READING_COLLECTION;
        String orderByUrl = "$orderby=MeasuringPoint";
        try {
            if (funcLocNum == null)
                funcLocNum = "";

            switch (fetchLevel) {
                case List:
                    resourcePath = resourcePath + "?$filter=(FunctionalLocation eq '" + funcLocNum + "' and OpObjectNumber eq '' and WOObjectNum eq '' and OperationNum  eq '')&$select=MeasuringPoint,MeasDescription,Description,FunctionalLocation,Equipment,MeasReading,MeasDocumentDate,MeasRangeUnit,LimitMinChar,LimitMaxChar,MeasReadingChar,MeasValuationCode,ValCodeSuff,CatalogType,CodeGroup,PrevDoc,Counter,MeasDocument,RefCounter" + "&" + orderByUrl;
                    break;
                case Single:
                    if (mPoint == null)
                        mPoint = "";
                    resourcePath = resourcePath + "?$filter=(MeasuringPoint eq '" + mPoint + "')";
                    break;
                case Count:
                    resourcePath += "/$count?$filter=(FunctionalLocation eq '" + funcLocNum + "' and OpObjectNumber eq '' and WOObjectNum eq '' and OperationNum  eq '')";
                    break;
                default:
                    resourcePath += "?$filter=(FunctionalLocation eq '" + funcLocNum + "')" + "&" + orderByUrl;
                    break;
            }
            if (fetchLevel != ZAppSettings.FetchLevel.Count) {
                result = getObjListFromStore(ZCollections.MEASPOINT_READING_COLLECTION, resourcePath, oprNum);
                if (!result.isError()) {
                    ArrayList<MeasurementPointReading> codes = (ArrayList<MeasurementPointReading>) result.Content();
                    if (codes != null)
                        result.setContent(codes);
                }
                return result;
            } else {
                dataHelper = DataHelper.getInstance();
                result = dataHelper.getEntities(ZCollections.MEASPOINT_READING_COLLECTION, resourcePath);

                return result;
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(MeasurementPointReading.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<MeasurementPointReading> measurementPoints = new ArrayList<>();
                MeasurementPointReading point;
                for (ODataEntity entity : entities) {
                    point = new MeasurementPointReading(entity);
                    measurementPoints.add(point);
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", measurementPoints);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(MeasurementPointReading.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private static ResponseObject getObjListFromStore(String entitySetName, String resPath, String oprNum) {
        ResponseObject result = null;
        try {
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (!result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                ArrayList<MeasurementPointReading> content = null;
                MeasurementPointReading reading;
                content = new ArrayList<MeasurementPointReading>();
                for (ODataEntity entity : entities) {
                    reading = new MeasurementPointReading(entity, oprNum);
                    //result = wo.fromEntity(entity);
                    if (reading != null) {
                        content.add(reading);
                    } else {
                        //pending: log the error message
                    }
                }
                if (result == null) {
                    result = new ResponseObject(ZConfigManager.Status.Success);
                }
                result.setMessage("");
                result.setContent(content);
            }
            return result;
        } catch (Exception e) {
            DliteLogger.WriteLog(MeasurementPointReading.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
    }

    private void initializeEntityProperties() {
        this.setEntityType(ZCollections.MEASPOINT_READING_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.MEASPOINT_READING_COLLECTION);
        this.addKeyFieldNames("MeasuringPoint");
        this.addKeyFieldNames("OperationNum");
        this.addKeyFieldNames("Counter");
        this.addKeyFieldNames("WOObjectNum");
        this.addKeyFieldNames("MeasDocument");
        this.addKeyFieldNames("OpObjectNumber");
        this.setParentEntitySetName(ZCollections.WO_COLLECTION);
        this.setParentForeignKeyFieldName("WOObjectNum");
        this.setParentKeyFieldName("ObjectNumber");
    }

    public ResponseObject createList(ODataEntity entity) {
        ResponseObject result = null;
        try {
            super.create(entity);

            if (WorkOrder.getCurrWo() != null) {

                String woObjNum = WorkOrder.getCurrWo().getObjectNumber();
                if (WorkOrder.getCurrWo().isLocal() && WorkOrder.getCurrWo().getTempID() != null
                        && !WorkOrder.getCurrWo().getTempID().isEmpty() && !ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)
                    woObjNum = WorkOrder.getCurrWo().getTempID();
                result = MeasurementPointReading.getPointReading(MeasuringPoint, woObjNum, OperationNum, CatalogType, CodeGroup);
                if (result != null && !result.isError()) {
                    ArrayList<MeasurementPointReading> mpReadings = (ArrayList<MeasurementPointReading>) result.Content();
                    if (mpReadings != null && mpReadings.size() > 0) {
                        PointReading = mpReadings.get(0);
                        if (isValCodeSuff()) {
                            result = CatalogCode.getCatalogCode(CatalogType, CodeGroup, mpReadings.get(0).getMeasValuationCode());
                            if (result != null && !result.isError()) {
                                ArrayList<CatalogCode> codes = (ArrayList<CatalogCode>) result.Content();
                                if (codes != null && codes.size() > 0) {
                                    ValuationCodeText = codes.get(0).getCodeText();
                                    PointReading.setValuationCodeText(ValuationCodeText);
                                    return result;
                                }
                            }
                        }
                    } else if (MeasValuationCode != null && !MeasValuationCode.isEmpty() && CatalogType != null && CodeGroup != null) {
                        setValuationCodeText(CatalogType, CodeGroup);
                    }

                }
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public void setValuationCodeText(String catalogType, String codeGroup) {
        try {
            ResponseObject result = CatalogCode.getCatalogCode(catalogType, codeGroup, MeasValuationCode);
            if (result != null && !result.isError()) {
                ArrayList<CatalogCode> codes = (ArrayList<CatalogCode>) result.Content();
                if (codes != null && codes.size() > 0)
                    ValuationCodeText = codes.get(0).getCodeText();
            }
        } catch (Exception e) {

        }
    }

    public String getMeasuringPoint() {
        return MeasuringPoint;
    }

    public void setMeasuringPoint(String measuringPoint) {
        MeasuringPoint = measuringPoint;
    }

    public String getWOObjectNum() {
        return WOObjectNum;
    }

    public void setWOObjectNum(String WOObjectNum) {
        this.WOObjectNum = WOObjectNum;
    }

    public GregorianCalendar getMeasDocumentDate() {
        return MeasDocumentDate;
    }

    public void setMeasDocumentDate(GregorianCalendar measDocumentDate) {
        MeasDocumentDate = measDocumentDate;
    }

    public Double getMeasReading() {
        try {
            if (getMeasReadingChar() != null && !getMeasReadingChar().isEmpty())
                MeasReading = Double.valueOf(getMeasReadingChar());
        } catch (NumberFormatException e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return MeasReading;
    }

    public void setMeasReading(Double measReading) {
        MeasReading = measReading;
    }

    public String getMeasText() {
        return MeasText;
    }

    public void setMeasText(String measText) {
        MeasText = measText;
    }

    public String getMeasDocument() {
        return MeasDocument;
    }

    public void setMeasDocument(String measDocument) {
        MeasDocument = measDocument;
    }

    public String getOpObjectNumber() {
        return OpObjectNumber;
    }

    public void setOpObjectNumber(String opObjectNumber) {
        OpObjectNumber = opObjectNumber;
    }

    public String getMeasReadingChar() {
        return MeasReadingChar;
    }

    public void setMeasReadingChar(String measReadingChar) {
        MeasReadingChar = measReadingChar;
    }

    public String getMeasDescription() {
        return MeasDescription;
    }

    public void setMeasDescription(String measDescription) {
        MeasDescription = measDescription;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMeasRangeUnit() {
        return MeasRangeUnit;
    }

    public void setMeasRangeUnit(String measRangeUnit) {
        MeasRangeUnit = measRangeUnit;
    }

    public String getLimitMinChar() {
        return LimitMinChar;
    }

    public void setLimitMinChar(String limitMinChar) {
        LimitMinChar = limitMinChar;
    }

    public String getLimitMaxChar() {
        return LimitMaxChar;
    }

    public void setLimitMaxChar(String limitMaxChar) {
        LimitMaxChar = limitMaxChar;
    }

    public String getMeasValuationCode() {
        return MeasValuationCode;
    }

    public void setMeasValuationCode(String measValuationCode) {
        MeasValuationCode = measValuationCode;
    }

    public boolean isValCodeSuff() {
        return ValCodeSuff;
    }

    public void setValCodeSuff(boolean valCodeSuff) {
        ValCodeSuff = valCodeSuff;
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

    public boolean isPrevDoc() {
        return PrevDoc;
    }

    public void setPrevDoc(boolean prevDoc) {
        PrevDoc = prevDoc;
    }

    public boolean isRefCounter() {
        return RefCounter;
    }

    public void setRefCounter(boolean refCounter) {
        RefCounter = refCounter;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getFunctionalLocation() {
        return FunctionalLocation;
    }

    public void setFunctionalLocation(String functionalLocation) {
        FunctionalLocation = functionalLocation;
    }

    public Time getMeasDocumentTime() {
        return MeasDocumentTime;
    }

    public void setMeasDocumentTime(Time measDocumentTime) {
        MeasDocumentTime = measDocumentTime;
    }

    public String getOperationNum() {
        return OperationNum;
    }

    public void setOperationNum(String operationNum) {
        OperationNum = operationNum;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public String getValuationCodeText() {
        return ValuationCodeText;
    }

    public void setValuationCodeText(String valuationCodeText) {
        ValuationCodeText = valuationCodeText;
    }

    public MeasurementPointReading getPointReading() {
        return PointReading;
    }

    public void setPointReading(MeasurementPointReading pointReading) {
        PointReading = pointReading;
    }
}
