package com.ods.myjobcard_library.entities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ctentities.EquipmentMeasurementPoint;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by MY HOME on 5/23/2017.
 */
public class MeasurementDocumentHistory extends ZBaseEntity {

    private String MeasDocument;
    private String MeasuringPoint;
    private String ObjectNumber;
    private String OrderObjectNumber;
    private GregorianCalendar Date;
    private java.sql.Time MeasurementTime;
    private String InvTimeStamp;
    private String Text;
    private String ReadBy;
    private GregorianCalendar CreatedOn;
    private java.sql.Time Time;
    private Double MeasRdg;
    private boolean ContainsValueMeasRdg;
    private Double MeasReading;
    private boolean ContainsValueMeasReading;
    private String DocMeasUnit;
    private Double CntrReadg;
    private boolean ContainsValueCntrReadg;
    private Double Difference;
    private boolean ContainsValueDifference;
    private String CatalogType;
    private String CodeGroup;
    private String ValuationCode;
    private String Equipment;
    private String FunctionalLocation;

    private String EnteredBy;

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public MeasurementDocumentHistory(ODataEntity entity) {
        create(entity);
    }

    public static ResponseObject getMeasurementDocHistory(String mPoint) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String entitySetName = ZCollections.MEASUREMENT_DOCUMENT_HISTORY_COLLECTION;
        String resourcePath = entitySetName;
        try {

            if (mPoint == null)
                mPoint = "";

            resourcePath += "?$filter=(MeasuringPoint eq '" + mPoint + "')&$top=" + ZConfigManager.MAX_MEASUREMENT_DOC_COUNT + "&$orderby=Date desc,Time desc";
            dataHelper = DataHelper.getInstance();
            //result = dataHelper.getEntities(resourcePath, StoreSettings.Stores.Tx);
            result = dataHelper.getEntities(entitySetName, resourcePath);
            result = FromEntity((List<ODataEntity>) result.Content());
            if (result == null)
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(EquipmentMeasurementPoint.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<MeasurementDocumentHistory> measurementDosHistory = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    measurementDosHistory.add(new MeasurementDocumentHistory(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", measurementDosHistory);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(EquipmentMeasurementPoint.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getMeasDocument() {
        return MeasDocument;
    }

    public void setMeasDocument(String measDocument) {
        MeasDocument = measDocument;
    }

    public String getMeasuringPoint() {
        return MeasuringPoint;
    }

    public void setMeasuringPoint(String measuringPoint) {
        MeasuringPoint = measuringPoint;
    }

    public String getObjectNumber() {
        return ObjectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        ObjectNumber = objectNumber;
    }

    public String getOrderObjectNumber() {
        return OrderObjectNumber;
    }

    public void setOrderObjectNumber(String orderObjectNumber) {
        OrderObjectNumber = orderObjectNumber;
    }

    public GregorianCalendar getDate() {
        return Date;
    }

    public void setDate(GregorianCalendar date) {
        Date = date;
    }

    public java.sql.Time getMeasurementTime() {
        return MeasurementTime;
    }

    public void setMeasurementTime(java.sql.Time measurementTime) {
        MeasurementTime = measurementTime;
    }

    public String getInvTimeStamp() {
        return InvTimeStamp;
    }

    public void setInvTimeStamp(String invTimeStamp) {
        InvTimeStamp = invTimeStamp;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getReadBy() {
        return ReadBy;
    }

    public void setReadBy(String readBy) {
        ReadBy = readBy;
    }

    public GregorianCalendar getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(GregorianCalendar createdOn) {
        CreatedOn = createdOn;
    }

    public java.sql.Time getTime() {
        return Time;
    }

    public void setTime(java.sql.Time time) {
        Time = time;
    }

    public Double getMeasRdg() {
        return MeasRdg;
    }

    public void setMeasRdg(Double measRdg) {
        MeasRdg = measRdg;
    }

    public boolean getContainsValueMeasRdg() {
        return ContainsValueMeasRdg;
    }

    public void setContainsValueMeasRdg(boolean containsValueMeasRdg) {
        ContainsValueMeasRdg = containsValueMeasRdg;
    }

    public Double getMeasReading() {
        return MeasReading;
    }

    public void setMeasReading(Double measReading) {
        MeasReading = measReading;
    }

    public boolean getContainsValueMeasReading() {
        return ContainsValueMeasReading;
    }

    public void setContainsValueMeasReading(boolean containsValueMeasReading) {
        ContainsValueMeasReading = containsValueMeasReading;
    }

    public String getDocMeasUnit() {
        return DocMeasUnit;
    }

    public void setDocMeasUnit(String docMeasUnit) {
        DocMeasUnit = docMeasUnit;
    }

    public Double getCntrReadg() {
        return CntrReadg;
    }

    public void setCntrReadg(Double cntrReadg) {
        CntrReadg = cntrReadg;
    }

    public boolean getContainsValueCntrReadg() {
        return ContainsValueCntrReadg;
    }

    public void setContainsValueCntrReadg(boolean containsValueCntrReadg) {
        ContainsValueCntrReadg = containsValueCntrReadg;
    }

    public Double getDifference() {
        return Difference;
    }

    public void setDifference(Double difference) {
        Difference = difference;
    }

    public boolean getContainsValueDifference() {
        return ContainsValueDifference;
    }

    public void setContainsValueDifference(boolean containsValueDifference) {
        ContainsValueDifference = containsValueDifference;
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

    public String getValuationCode() {
        return ValuationCode;
    }

    public void setValuationCode(String valuationCode) {
        ValuationCode = valuationCode;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    //end of getters setters

    public String getFunctionalLocation() {
        return FunctionalLocation;
    }

    public void setFunctionalLocation(String functionalLocation) {
        FunctionalLocation = functionalLocation;
    }
}
