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

/**
 * Created by lenovo on 02-04-2020.
 */

public class InspectionPoint extends ZBaseEntity {

    private String EnteredBy;
    private Time Usert1;
    private String InspLot;
    private String InspOper;
    private String InspPoint;
    private String PointDesc;
    private String PartialLot;
    private String Quantity;
    private String Unit;
    private String UnitText;
    private String Equipment;
    private String FunctLoc;
    private String PhysSmpl;
    private String Userc1;
    private String Userc2;
    private String Usern1;
    private String Usern2;
    private GregorianCalendar Userd1;
    private String CatType;
    private String PlantSelSet;
    private String SelSet;
    private String CodeGrp;
    private String Code;
    private String Remark;
    private GregorianCalendar InspDate;
    private Time InspTime;
    private String Inspector;

    public InspectionPoint(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public InspectionPoint(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity);
    }

    public static ArrayList<InspectionPoint> getInspLotOprPoints(String inspLotNum, String inspOprNum) {
        ResponseObject result = null;
        ArrayList<InspectionPoint> inspOprPoints = null;
        try {
            String entitySetName = ZCollections.WO_INSPECTIONLOT_POINT_ENTITY_COLLECTION;
            String resPath = entitySetName;
            resPath += "?$filter=InspLot eq '" + inspLotNum + "' and InspOper eq '" + inspOprNum + "'&$orderby=InspPoint";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities, ZAppSettings.FetchLevel.All);
                inspOprPoints = (ArrayList<InspectionPoint>) result.Content();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(InspectionLot.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return inspOprPoints;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<InspectionPoint> inspOprPoints = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    inspOprPoints.add(new InspectionPoint(entity, ZAppSettings.FetchLevel.All));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", inspOprPoints);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(InspectionPoint.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void initializeEntityProperties() {
        this.setEntityType(ZCollections.WO_INSPECTIONLOT_POINT_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.WO_INSPECTIONLOT_POINT_ENTITY_COLLECTION);
        this.addKeyFieldNames("InspLot");
        this.addKeyFieldNames("InspOper");
        this.addKeyFieldNames("InspPoint");
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public Time getUsert1() {
        return Usert1;
    }

    public void setUsert1(Time usert1) {
        Usert1 = usert1;
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

    public String getPartialLot() {
        return PartialLot;
    }

    public void setPartialLot(String partialLot) {
        PartialLot = partialLot;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getUnitText() {
        return UnitText;
    }

    public void setUnitText(String unitText) {
        UnitText = unitText;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getFunctLoc() {
        return FunctLoc;
    }

    public void setFunctLoc(String functLoc) {
        FunctLoc = functLoc;
    }

    public String getPhysSmpl() {
        return PhysSmpl;
    }

    public void setPhysSmpl(String physSmpl) {
        PhysSmpl = physSmpl;
    }

    public String getUserc1() {
        return Userc1;
    }

    public void setUserc1(String userc1) {
        Userc1 = userc1;
    }

    public String getUserc2() {
        return Userc2;
    }

    public void setUserc2(String userc2) {
        Userc2 = userc2;
    }

    public String getUsern1() {
        return Usern1;
    }

    public void setUsern1(String usern1) {
        Usern1 = usern1;
    }

    public String getUsern2() {
        return Usern2;
    }

    public void setUsern2(String usern2) {
        Usern2 = usern2;
    }

    public GregorianCalendar getUserd1() {
        return Userd1;
    }

    public void setUserd1(GregorianCalendar userd1) {
        Userd1 = userd1;
    }

    public String getCatType() {
        return CatType;
    }

    public void setCatType(String catType) {
        CatType = catType;
    }

    public String getPlantSelSet() {
        return PlantSelSet;
    }

    public void setPlantSelSet(String plantSelSet) {
        PlantSelSet = plantSelSet;
    }

    public String getSelSet() {
        return SelSet;
    }

    public void setSelSet(String selSet) {
        SelSet = selSet;
    }

    public String getCodeGrp() {
        return CodeGrp;
    }

    public void setCodeGrp(String codeGrp) {
        CodeGrp = codeGrp;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
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

    public String getInspector() {
        return Inspector;
    }

    public void setInspector(String inspector) {
        Inspector = inspector;
    }

    public String getPointDesc() {
        return PointDesc;
    }

    public void setPointDesc(String pointDesc) {
        PointDesc = pointDesc;
    }
}

