package com.ods.myjobcard_library.entities.supervisor;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.ctentities.CatalogCode;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.StoreHelpers.TableConfigSet;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.ConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by lenovo on 02=01-2020.
 */
public class SupMeasurementPointReading extends ZBaseEntity {

    private String MeasuringPoint;
    private String WOObjectNum;
    private String OperationNum;
    private String Counter;
    private GregorianCalendar MeasDocumentDate;
    private Time MeasDocumentTime;
    private Double MeasReading;
    private String MeasText;
    private String ValuationCode;
    private String FunctionalLocation;
    private String Equipment;
    private String MeasDocument;
    private String OpObjectNumber;
    private String MeasReadingChar;
    private String MeasDescription;
    private String Description;
    private String IntMeasUnit;
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
    private SupMeasurementPointReading PointReading;
    //    Constructors
    public SupMeasurementPointReading() {
        initializeEntityProperties();
    }
    public SupMeasurementPointReading(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity);
    }
    public SupMeasurementPointReading(ODataEntity entity, String oprNum) {
        this.setOperationNum(oprNum);
        create(entity);
    }
    public SupMeasurementPointReading(ODataEntity entity, String catalogType, String codeGroup) {
        initializeEntityProperties();
        create(entity, catalogType, codeGroup);
    }

    public static ResponseObject getPointReading(String point, String objNum, String oprNum, String catalogType, String codeGroup) {
        ResponseObject result = null;
        try {
            String resPath = ZCollections.SUPERVISOR_MEASPOINT_READING_COLLECTION;
            if (oprNum == null)
                oprNum = "";
            if (point == null)
                point = "";
            if (objNum == null)
                objNum = "";

            resPath += "?$filter=(MeasuringPoint eq '" + point + "' and WOObjectNum eq '" + objNum + "' and OperationNum eq '" + oprNum + "')";
            result = getObjListFromStore(ZCollections.SUPERVISOR_MEASPOINT_READING_COLLECTION, resPath, catalogType, codeGroup);
            if (!result.isError()) {
                ArrayList<SupMeasurementPointReading> codes = (ArrayList<SupMeasurementPointReading>) result.Content();
                if (codes != null)
                    result.setContent(codes);
            }
            return result;
        } catch (Exception e) {
            DliteLogger.WriteLog(SupMeasurementPointReading.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ConfigManager.Status.Error, e.getMessage(), null);
        }
    }

    public static ResponseObject getEquipmentMeasurementPoint(String eqpNum, ZAppSettings.FetchLevel fetchLevel, String mPoint, String oprNum) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String resourcePath = ZCollections.SUPERVISOR_MEASPOINT_READING_COLLECTION;
        String orderByUrl = "$orderby=MeasuringPoint";
        try {
            if (eqpNum == null || eqpNum.isEmpty())
                eqpNum = "";
            else
                eqpNum = String.valueOf(Integer.parseInt(eqpNum));

            switch (fetchLevel) {
                case List:
                    resourcePath = resourcePath + "?$filter=(Equipment eq '" + eqpNum + "' and OpObjectNumber eq '' and WOObjectNum eq '' and OperationNum  eq '')&$select=MeasuringPoint,MeasDescription,Description,FunctionalLocation,Equipment,MeasReading,MeasDocument,MeasDocumentDate,IntMeasUnit,MeasReadingChar,LimitMinChar,LimitMaxChar,MeasValuationCode,ValCodeSuff,CatalogType,CodeGroup,PrevDoc,Counter,RefCounter" + "&" + orderByUrl;
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
            result = dataHelper.getEntities(resourcePath, TableConfigSet.getStore(ZCollections.SUPERVISOR_MEASPOINT_READING_COLLECTION));
            if (fetchLevel != ZAppSettings.FetchLevel.Count)
                result = FromEntity((List<ODataEntity>) result.Content(), oprNum);
            else
                return result;
            if (result == null)
                result = new ResponseObject(ConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(SupMeasurementPointReading.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ConfigManager.Status.Error, e.getMessage(), e);
        }
        return result;
    }

    public static ResponseObject getFLMeasurementPoint(String funcLocNum, ZAppSettings.FetchLevel fetchLevel, String mPoint, String oprNum) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String resourcePath = ZCollections.SUPERVISOR_MEASPOINT_READING_COLLECTION;
        String orderByUrl = "$orderby=MeasuringPoint";
        try {
            if (funcLocNum == null)
                funcLocNum = "";

            switch (fetchLevel) {
                case List:
                    resourcePath = resourcePath + "?$filter=(FunctionalLocation eq '" + funcLocNum + "')&$select=MeasuringPoint,MeasDescription,Description,FunctionalLocation,Equipment,MeasReading,MeasDocumentDate,IntMeasUnit,LimitMinChar,LimitMaxChar,MeasReadingChar,MeasValuationCode,ValCodeSuff,CatalogType,CodeGroup,PrevDoc,Counter,MeasDocument,RefCounter" + "&" + orderByUrl;
                    break;
                case Single:
                    if (mPoint == null)
                        mPoint = "";
                    resourcePath = resourcePath + "?$filter=(MeasuringPoint eq '" + mPoint + "')";
                    break;
                case Count:
                    resourcePath += "/$count?$filter=FunctionalLocation eq '" + funcLocNum + "'";
                    break;
                default:
                    resourcePath += "?$filter=(FunctionalLocation eq '" + funcLocNum + "')" + "&" + orderByUrl;
                    break;
            }
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(resourcePath, TableConfigSet.getStore(ZCollections.SUPERVISOR_MEASPOINT_READING_COLLECTION));
            if (fetchLevel != ZAppSettings.FetchLevel.Count)
                result = FromEntity((List<ODataEntity>) result.Content(), oprNum);
            else
                return result;
            if (result == null)
                result = new ResponseObject(ConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(SupMeasurementPointReading.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ConfigManager.Status.Error, e.getMessage(), e);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, String oprNum) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<SupMeasurementPointReading> measurementPoints = new ArrayList<>();
                SupMeasurementPointReading point;
                for (ODataEntity entity : entities) {
                    point = new SupMeasurementPointReading(entity, oprNum);
                    measurementPoints.add(point);
                }
                result = new ResponseObject(ConfigManager.Status.Success, "", measurementPoints);
            } else
                result = new ResponseObject(ConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(SupMeasurementPointReading.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private static ResponseObject getObjListFromStore(String entitySetName, String resPath, String catalogType, String codeGroup) {
        ResponseObject result = null;
        try {
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (!result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                ArrayList<SupMeasurementPointReading> content = null;
                SupMeasurementPointReading reading;
                content = new ArrayList<SupMeasurementPointReading>();
                for (ODataEntity entity : entities) {
                    reading = new SupMeasurementPointReading(entity, catalogType, codeGroup);
                    //result = wo.fromEntity(entity);
                    if (reading != null) {
                        content.add(reading);
                    } else {
                        //pending: log the error message
                    }
                }
                if (result == null) {
                    result = new ResponseObject(ConfigManager.Status.Success);
                }
                result.setMessage("");
                result.setContent(content);
            }
            return result;
        } catch (Exception e) {
            DliteLogger.WriteLog(SupMeasurementPointReading.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ConfigManager.Status.Error, e.getMessage(), null);
        }
    }

    private void initializeEntityProperties() {
        this.setEntityType(ZCollections.SUPERVISOR_MEASPOINT_READING_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.SUPERVISOR_MEASPOINT_READING_COLLECTION);
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

    public ResponseObject create(ODataEntity entity, String catalogType, String codeGroup) {
        ResponseObject result = null;
        try {
            super.create(entity);
            if (ValuationCode != null && !ValuationCode.isEmpty() && catalogType != null && codeGroup != null) {
                setValuationCodeText(catalogType, codeGroup);
            }
            if (WorkOrder.getCurrWo() != null) {
                String woObjNum = WorkOrder.getCurrWo().getObjectNumber();
                result = SupMeasurementPointReading.getPointReading(MeasuringPoint, woObjNum, OperationNum, CatalogType, CodeGroup);
                if (result != null && !result.isError()) {
                    ArrayList<SupMeasurementPointReading> mpReadings = (ArrayList<SupMeasurementPointReading>) result.Content();
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
        return result;
    }

    public void setValuationCodeText(String catalogType, String codeGroup) {
        try {
            ResponseObject result = CatalogCode.getCatalogCode(catalogType, codeGroup, ValuationCode);
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
        return MeasReading;
    }

    // end of getter setters

    public void setMeasReading(Double measReading) {
        MeasReading = measReading;
    }

    public String getMeasText() {
        return MeasText;
    }

    public void setMeasText(String measText) {
        MeasText = measText;
    }

    public String getValuationCode() {
        return ValuationCode;
    }

    public void setValuationCode(String valuationCode) {
        ValuationCode = valuationCode;
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

    public String getIntMeasUnit() {
        return IntMeasUnit;
    }

    public void setIntMeasUnit(String intMeasUnit) {
        IntMeasUnit = intMeasUnit;
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

    public SupMeasurementPointReading getPointReading() {
        return PointReading;
    }

    public void setPointReading(SupMeasurementPointReading pointReading) {
        PointReading = pointReading;
    }

    public boolean isReadingOutOfRange() {
        try {
            String resPath = ZCollections.SUPERVISOR_MEASPOINT_READING_COLLECTION + "/$count?$filter=MeasuringPoint eq '" + getMeasuringPoint() + "' and ((LimitMinChar ne '' and MeasReading lt LoMRLimit) or (LimitMaxChar ne '' and MeasReading gt UpMRLimit)) and PrevDoc eq true and ValCodeSuff eq false";
            ResponseObject response = DataHelper.getInstance().getEntities(resPath, TableConfigSet.getStore(ZCollections.SUPERVISOR_MEASPOINT_READING_COLLECTION));
            if (response != null && !response.isError()) {
                if (Integer.parseInt(String.valueOf(response.Content())) > 0)
                    return true;
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(SupMeasurementPointReading.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return false;
    }
}
