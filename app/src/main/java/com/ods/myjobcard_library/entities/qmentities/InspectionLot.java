package com.ods.myjobcard_library.entities.qmentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by lenovo on 02-04-2020.
 */

public class InspectionLot extends BaseEntity {
    private String InspLot;
    private String Plant;
    private String InspType;
    private String InspLotOrigin;
    private String ObjectNo;
    private String EnteredBy;
    private GregorianCalendar CreatedOnDate;
    private Time CreatedAtTime;
    private GregorianCalendar InspectionStartsOnDate;
    private Time InspectionStartsAtTime;
    private GregorianCalendar InspectionEndsOnDate;
    private Time InspectionEndsAtTime;
    private String InspPointType;
    private String OrderId;
    private String TxtInspObject;
    private BigDecimal InspLotSize;
    private String InspLotBaseUom;
    private BigDecimal SampleSize;
    private String SampleBaseUom;
    private BigDecimal SampleQtyLongTermInspChar;
    private BigDecimal SampleQtyActuallyInspected;
    private BigDecimal SampleQtyDestroyed;
    private BigDecimal SampleQtyDefective;
    private String TaskListType;
    private String TaskListNumber;
    private String TaskListUsage;
    private String TaskListCounter;
    private String UdMode;
    private String UdCatalogType;
    private String UdPlant;
    private String UdSelectedSet;
    private String UdCodeGroup;
    private String UdCode;
    private String CodeValuation;
    private String UdRecordedByUser;
    private GregorianCalendar UdRecordedOnDate;
    private Time UdRecordedAtTime;
    private String UdChangedByUser;
    private GregorianCalendar UdChangedOnDate;
    private Time UdChangedAtTime;
    private String SysStatus;
    private String SyStText;
    private String SyStDscr;

    public InspectionLot(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public InspectionLot(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity);
    }

    public static ArrayList<InspectionLot> getInspectionLot(String workOrderNum) {
        ResponseObject result = null;
        ArrayList<InspectionLot> inspectionLot = null;
        try {
            String entitySetName = ZCollections.WO_INSPECTIONLOT_ENTITY_COLLECTION;
            String resPath = entitySetName;
            resPath += "?$filter=OrderId eq '" + workOrderNum + "'";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities, ZAppSettings.FetchLevel.Single);
                inspectionLot = (ArrayList<InspectionLot>) result.Content();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(InspectionLot.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return inspectionLot;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<InspectionLot> inspectionLots = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    inspectionLots.add(new InspectionLot(entity, ZAppSettings.FetchLevel.Single));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", inspectionLots);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(InspectionLot.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void initializeEntityProperties() {
        this.setEntityType(ZCollections.WO_INSPECTIONLOT_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.WO_INSPECTIONLOT_ENTITY_COLLECTION);
        this.addKeyFieldNames("InspLot");
    }

    public String getInspLot() {
        return InspLot;
    }

    public void setInspLot(String inspLot) {
        InspLot = inspLot;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getInspType() {
        return InspType;
    }

    public void setInspType(String inspType) {
        InspType = inspType;
    }

    public String getInspLotOrigin() {
        return InspLotOrigin;
    }

    public void setInspLotOrigin(String inspLotOrigin) {
        InspLotOrigin = inspLotOrigin;
    }

    public String getObjectNo() {
        return ObjectNo;
    }

    public void setObjectNo(String objectNo) {
        ObjectNo = objectNo;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public GregorianCalendar getCreatedOnDate() {
        return CreatedOnDate;
    }

    public void setCreatedOnDate(GregorianCalendar createdOnDate) {
        CreatedOnDate = createdOnDate;
    }

    public Time getCreatedAtTime() {
        return CreatedAtTime;
    }

    public void setCreatedAtTime(Time createdAtTime) {
        CreatedAtTime = createdAtTime;
    }

    public GregorianCalendar getInspectionStartsOnDate() {
        return InspectionStartsOnDate;
    }

    public void setInspectionStartsOnDate(GregorianCalendar inspectionStartsOnDate) {
        InspectionStartsOnDate = inspectionStartsOnDate;
    }

    public Time getInspectionStartsAtTime() {
        return InspectionStartsAtTime;
    }

    public void setInspectionStartsAtTime(Time inspectionStartsAtTime) {
        InspectionStartsAtTime = inspectionStartsAtTime;
    }

    public GregorianCalendar getInspectionEndsOnDate() {
        return InspectionEndsOnDate;
    }

    public void setInspectionEndsOnDate(GregorianCalendar inspectionEndsOnDate) {
        InspectionEndsOnDate = inspectionEndsOnDate;
    }

    public Time getInspectionEndsAtTime() {
        return InspectionEndsAtTime;
    }

    public void setInspectionEndsAtTime(Time inspectionEndsAtTime) {
        InspectionEndsAtTime = inspectionEndsAtTime;
    }

    public String getInspPointType() {
        return InspPointType;
    }

    public void setInspPointType(String inspPointType) {
        InspPointType = inspPointType;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getTxtInspObject() {
        return TxtInspObject;
    }

    public void setTxtInspObject(String txtInspObject) {
        TxtInspObject = txtInspObject;
    }

    public BigDecimal getInspLotSize() {
        return InspLotSize;
    }

    public void setInspLotSize(BigDecimal inspLotSize) {
        InspLotSize = inspLotSize;
    }

    public String getInspLotBaseUom() {
        return InspLotBaseUom;
    }

    public void setInspLotBaseUom(String inspLotBaseUom) {
        InspLotBaseUom = inspLotBaseUom;
    }

    public BigDecimal getSampleSize() {
        return SampleSize;
    }

    public void setSampleSize(BigDecimal sampleSize) {
        SampleSize = sampleSize;
    }

    public String getSampleBaseUom() {
        return SampleBaseUom;
    }

    public void setSampleBaseUom(String sampleBaseUom) {
        SampleBaseUom = sampleBaseUom;
    }

    public BigDecimal getSampleQtyLongTermInspChar() {
        return SampleQtyLongTermInspChar;
    }

    public void setSampleQtyLongTermInspChar(BigDecimal sampleQtyLongTermInspChar) {
        SampleQtyLongTermInspChar = sampleQtyLongTermInspChar;
    }

    public BigDecimal getSampleQtyActuallyInspected() {
        return SampleQtyActuallyInspected;
    }

    public void setSampleQtyActuallyInspected(BigDecimal sampleQtyActuallyInspected) {
        SampleQtyActuallyInspected = sampleQtyActuallyInspected;
    }

    public BigDecimal getSampleQtyDestroyed() {
        return SampleQtyDestroyed;
    }

    public void setSampleQtyDestroyed(BigDecimal sampleQtyDestroyed) {
        SampleQtyDestroyed = sampleQtyDestroyed;
    }

    public BigDecimal getSampleQtyDefective() {
        return SampleQtyDefective;
    }

    public void setSampleQtyDefective(BigDecimal sampleQtyDefective) {
        SampleQtyDefective = sampleQtyDefective;
    }

    public String getTaskListType() {
        return TaskListType;
    }

    public void setTaskListType(String taskListType) {
        TaskListType = taskListType;
    }

    public String getTaskListNumber() {
        return TaskListNumber;
    }

    public void setTaskListNumber(String taskListNumber) {
        TaskListNumber = taskListNumber;
    }

    public String getTaskListUsage() {
        return TaskListUsage;
    }

    public void setTaskListUsage(String taskListUsage) {
        TaskListUsage = taskListUsage;
    }

    public String getTaskListCounter() {
        return TaskListCounter;
    }

    public void setTaskListCounter(String taskListCounter) {
        TaskListCounter = taskListCounter;
    }

    public String getUdMode() {
        return UdMode;
    }

    public void setUdMode(String udMode) {
        UdMode = udMode;
    }

    public String getUdCatalogType() {
        return UdCatalogType;
    }

    public void setUdCatalogType(String udCatalogType) {
        UdCatalogType = udCatalogType;
    }

    public String getUdPlant() {
        return UdPlant;
    }

    public void setUdPlant(String udPlant) {
        UdPlant = udPlant;
    }

    public String getUdSelectedSet() {
        return UdSelectedSet;
    }

    public void setUdSelectedSet(String udSelectedSet) {
        UdSelectedSet = udSelectedSet;
    }

    public String getUdCodeGroup() {
        return UdCodeGroup;
    }

    public void setUdCodeGroup(String udCodeGroup) {
        UdCodeGroup = udCodeGroup;
    }

    public String getUdCode() {
        return UdCode;
    }

    public void setUdCode(String udCode) {
        UdCode = udCode;
    }

    public String getCodeValuation() {
        return CodeValuation;
    }

    public void setCodeValuation(String codeValuation) {
        CodeValuation = codeValuation;
    }

    public String getUdRecordedByUser() {
        return UdRecordedByUser;
    }

    public void setUdRecordedByUser(String udRecordedByUser) {
        UdRecordedByUser = udRecordedByUser;
    }

    public GregorianCalendar getUdRecordedOnDate() {
        return UdRecordedOnDate;
    }

    public void setUdRecordedOnDate(GregorianCalendar udRecordedOnDate) {
        UdRecordedOnDate = udRecordedOnDate;
    }

    public Time getUdRecordedAtTime() {
        return UdRecordedAtTime;
    }

    public void setUdRecordedAtTime(Time udRecordedAtTime) {
        UdRecordedAtTime = udRecordedAtTime;
    }

    public String getUdChangedByUser() {
        return UdChangedByUser;
    }

    public void setUdChangedByUser(String udChangedByUser) {
        UdChangedByUser = udChangedByUser;
    }

    public GregorianCalendar getUdChangedOnDate() {
        return UdChangedOnDate;
    }

    public void setUdChangedOnDate(GregorianCalendar udChangedOnDate) {
        UdChangedOnDate = udChangedOnDate;
    }

    public Time getUdChangedAtTime() {
        return UdChangedAtTime;
    }

    public void setUdChangedAtTime(Time udChangedAtTime) {
        UdChangedAtTime = udChangedAtTime;
    }

    public String getSysStatus() {
        return SysStatus;
    }

    public void setSysStatus(String sysStatus) {
        SysStatus = sysStatus;
    }

    public String getSyStText() {
        return SyStText;
    }

    public void setSyStText(String syStText) {
        SyStText = syStText;
    }

    public String getSyStDscr() {
        return SyStDscr;
    }

    public void setSyStDscr(String syStDscr) {
        SyStDscr = syStDscr;
    }

}
