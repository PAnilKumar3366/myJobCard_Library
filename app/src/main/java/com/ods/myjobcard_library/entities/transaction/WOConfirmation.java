package com.ods.myjobcard_library.entities.transaction;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ctentities.UserTable;
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
import java.util.UUID;

public class WOConfirmation extends BaseEntity {


    //Compoent child elements
    private DataHelper dataHelper = null;

    //Fields
    private String ConfNo;
    private String ConfCounter;
    private String WorkOrderNum;
    private String OperationNum;
    private String SubOper;
    private String CapaCategory;
    private Short Split;
    private String FinConf;

    private String Complete;
    private boolean ClearRes;

    private GregorianCalendar PostgDate;
    private String DevReason;
    private String ConfText;
    private String Plant;
    private String WorkCntr;
    private BigDecimal ActWork;
    private String UnWork;

    private BigDecimal RemWork;
    private String UnRemWrk;

    private BigDecimal ActualDur;
    private String UnActDur;
    private GregorianCalendar ExecStartDate;
    private Time ExecStartTime;
    private GregorianCalendar ExecFinDate;
    private Time ExecFinTime;
    private GregorianCalendar FcstFinDate;
    private Time FcstFinTime;
    private String PersNo;
    private String TimeidNo;
    private String ActType;
    private String Wagetype;
    private String CalcMotive;
    private String ExCreatedBy;
    private GregorianCalendar ExCreatedDate;
    private Time ExCreatedTime;
    private UUID ExIdent;
    private BigDecimal ActWork2;
    private BigDecimal RemWork2;
    private String EnteredBy;
    private String OnlineSearch;


    //Setters and Getters Method

    public WOConfirmation(Operation operation) {
        ResponseObject result = null;
        int intCount = 0;
        try {
            try {
                result = getConfirmations(ZAppSettings.FetchLevel.Count, operation.getWorkOrderNum(), operation.getOperationNum(), operation.getConfNo());
                if (!result.isError()) {
                    intCount = (int) result.Content();
                }
            } catch (Exception e) {
                DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            }
            initializeEntityProperties();
            setWorkOrderNum(operation.getWorkOrderNum());
            setOperationNum(operation.getOperationNum());
            setConfNo(operation.getConfNo());
            setConfCounter(String.format("%08d", Integer.valueOf(intCount + 1)));
            setClearRes(false);
            setPostgDate(ZCommon.getDeviceDateTime());
            setPlant(operation.getPlant());
            setWorkCntr(operation.getWorkCenter());
            setSubOper(operation.getSubOperation());
            String personnelNum = operation.getPersonnelNo();
            try {
                if (personnelNum == null || personnelNum.isEmpty() || Integer.parseInt(personnelNum) == 0)
                    personnelNum = UserTable.getUserPersonnelNumber();
            } catch (Exception e) {
                DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            }
            setPersNo(personnelNum);
            setExCreatedBy(ZAppSettings.strUser);
            //setExCreatedDate(ZCommon.getDeviceDateTime());
            setExCreatedTime(new Time(ZCommon.getDeviceTime().getTime()));
            setExecStartDate(ZCommon.getDeviceDateTime());
            setExecStartTime(new Time(ZCommon.getDeviceTime().getTime()));
            setExecFinDate(ZCommon.getDeviceDateTime());
            setExecFinTime(new Time(ZCommon.getDeviceTime().getTime()));
            //setFcstFinDate(ZCommon.getDeviceDateTime());
            //setFcstFinTime(new Time(ZCommon.getDeviceTime().getTime()));
            setMode(ZAppSettings.EntityMode.Create);
            setEntityResourcePath(ZCollections.WO_CONFIRMATION__COLLECTION);

        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public WOConfirmation(String workOrderNum, String operationNum, String persNo, GregorianCalendar startDateTime, GregorianCalendar finishDateTime) {
        try {
            initializeEntityProperties();
            setWorkOrderNum(workOrderNum);
            setOperationNum(operationNum);
            setPersNo(persNo);
            setExecStartDate(startDateTime);
            setExecStartTime(new Time(startDateTime.getTimeInMillis()));
            setExecFinDate(finishDateTime);
            setExecFinTime(new Time(finishDateTime.getTimeInMillis()));
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public WOConfirmation(ODataEntity entity) {
        create(entity);
        initializeEntityProperties();
    }

    public WOConfirmation() {
        initializeEntityProperties();
    }

    public static WOConfirmation getOperationFinalConfirmation(String operationNum, String workOrderNum) {
        WOConfirmation confirmation = null;
        try {
            String entitySetName = ZCollections.WO_CONFIRMATION__COLLECTION;
            String resPath = entitySetName + "?$filter=OperationNum eq '" + operationNum + "'" +
                    " and WorkOrderNum eq '" + workOrderNum + "' and tolower(FinConf) eq 'x'&$orderby=ConfCounter desc";
            ResponseObject response = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities != null && entities.size() > 0) {
                    confirmation = new WOConfirmation(entities.get(0));
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WOConfirmation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return confirmation;
    }

    public static ResponseObject getConfirmations(ZAppSettings.FetchLevel confirmationFetchLevel, String OrderNum, String operationNum, String confirmationNum) {

        ResponseObject result = null;
        String resourcePath = null;
        String strOrderBy = "&$orderby=";
        String strEntitySet = null;

        try {
            strEntitySet = ZCollections.WO_CONFIRMATION__COLLECTION;
            switch (confirmationFetchLevel) {
                case Count:
                    resourcePath = strEntitySet + "/$count?$filter=(WorkOrderNum eq '" + OrderNum + "' and OperationNum eq '" + operationNum + "' and ConfNo eq '" + confirmationNum + "')";
                    break;
                case Single:
                    if (OrderNum != null && OrderNum.length() > 0 && operationNum != null && operationNum.length() > 0 && confirmationNum != null && confirmationNum.length() > 0) {
                        resourcePath = strEntitySet + "?$filter=(WorkOrderNum eq '" + OrderNum + "' and OperationNum eq '" + operationNum + "' and ConfNo eq '" + confirmationNum + "')";//" + strOrderByURI +" )";
                    }
                    break;
                case All:
                    resourcePath = strEntitySet + "?$filter=(WorkOrderNum eq '" + OrderNum + "' and OperationNum eq '" + operationNum + "')";//" + strOrderByURI +" )";
                    break;
                default:
                    resourcePath = strEntitySet;
                    break;
            }
            result = getObjListFromStore(strEntitySet, resourcePath, confirmationFetchLevel);
            return result;
        } catch (Exception e) {
            DliteLogger.WriteLog(WOConfirmation.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
    }

    public static ResponseObject getObjListFromStore(String entitySetName, String resPath, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        try {
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (!result.isError()) {
                if (fetchLevel != ZAppSettings.FetchLevel.Count) {
                    List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                    ArrayList<WOConfirmation> confirmations = new ArrayList<WOConfirmation>();
                    WOConfirmation confirmation;
                    for (ODataEntity entity : entities) {
                        confirmation = new WOConfirmation(entity);
                        if (confirmation != null) {
                            confirmations.add(confirmation);
                        } else {
                            DliteLogger.WriteLog(WOConfirmation.class, ZAppSettings.LogLevel.Error, "Unable to add Work Order Confirmations");
                            result = new ResponseObject(ZConfigManager.Status.Error, "unable to add Work order Confirmations", null);
                        }
                    }
                    if (result == null) {
                        result = new ResponseObject(ZConfigManager.Status.Success);
                    }
                    result.setContent(confirmations);
                } else {
                    int intCount = Integer.valueOf(String.valueOf(result.Content()));
                    if (result == null) {
                        result = new ResponseObject(ZConfigManager.Status.Success);
                    }
                    result.setContent(intCount);
                }
            }
            return result;
        } catch (Exception e) {
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }

    }

    public String getConfNo() {
        return ConfNo;
    }

    public void setConfNo(String confNo) {
        ConfNo = confNo;
    }

    public String getConfCounter() {
        return ConfCounter;
    }

    public void setConfCounter(String confCounter) {
        ConfCounter = confCounter;
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getOperationNum() {
        return OperationNum;
    }

    public void setOperationNum(String operationNum) {
        OperationNum = operationNum;
    }

    public String getSubOper() {
        return SubOper;
    }

    public void setSubOper(String subOper) {
        SubOper = subOper;
    }

    public String getCapaCategory() {
        return CapaCategory;
    }

    public void setCapaCategory(String capaCategory) {
        CapaCategory = capaCategory;
    }

    public Short getSplit() {
        return Split;
    }

    public void setSplit(Short split) {
        Split = split;
    }

    public String getFinConf() {
        return FinConf;
    }

    public void setFinConf(String finConf) {
        FinConf = finConf;
    }

    public boolean isClearRes() {
        return ClearRes;
    }

    public void setClearRes(boolean clearRes) {
        ClearRes = clearRes;
    }

    public GregorianCalendar getPostgDate() {
        return PostgDate;
    }

    public void setPostgDate(GregorianCalendar postgDate) {
        PostgDate = postgDate;
    }

    public String getDevReason() {
        return DevReason;
    }

    public void setDevReason(String devReason) {
        DevReason = devReason;
    }

    public String getConfText() {
        return ConfText;
    }

    public void setConfText(String confText) {
        ConfText = confText;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getWorkCntr() {
        return WorkCntr;
    }

    public void setWorkCntr(String workCntr) {
        WorkCntr = workCntr;
    }

    public BigDecimal getActWork() {
        return ActWork;
    }

    public void setActWork(BigDecimal actWork) {
        ActWork = actWork;
    }

    public String getUnWork() {
        return UnWork;
    }

    public void setUnWork(String unWork) {
        UnWork = unWork;
    }

    public BigDecimal getRemWork() {
        return RemWork;
    }

    public void setRemWork(BigDecimal remWork) {
        RemWork = remWork;
    }

    public String getUnRemWrk() {
        return UnRemWrk;
    }

    public void setUnRemWrk(String unRemWrk) {
        UnRemWrk = unRemWrk;
    }

    public BigDecimal getActualDur() {
        return ActualDur;
    }

    public void setActualDur(BigDecimal actualDur) {
        ActualDur = actualDur;
    }

    public String getUnActDur() {
        return UnActDur;
    }

    public void setUnActDur(String unActDur) {
        UnActDur = unActDur;
    }

    public GregorianCalendar getExecStartDate() {
        return ExecStartDate;
    }

    public void setExecStartDate(GregorianCalendar execStartDate) {
        ExecStartDate = execStartDate;
    }

    public Time getExecStartTime() {
        return ExecStartTime;
    }

    public void setExecStartTime(Time execStartTime) {
        ExecStartTime = execStartTime;
    }

    public GregorianCalendar getExecFinDate() {
        return ExecFinDate;
    }

    public void setExecFinDate(GregorianCalendar execFinDate) {
        ExecFinDate = execFinDate;
    }

    public Time getExecFinTime() {
        return ExecFinTime;
    }

    public void setExecFinTime(Time execFinTime) {
        ExecFinTime = execFinTime;
    }

    public GregorianCalendar getFcstFinDate() {
        return FcstFinDate;
    }

    public void setFcstFinDate(GregorianCalendar fcstFinDate) {
        FcstFinDate = fcstFinDate;
    }

    public Time getFcstFinTime() {
        return FcstFinTime;
    }

    public void setFcstFinTime(Time fcstFinTime) {
        FcstFinTime = fcstFinTime;
    }

    public String getPersNo() {
        String truncatedPerNo = PersNo;
        try {
            return String.valueOf(Integer.parseInt(PersNo));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return truncatedPerNo;
    }

    public void setPersNo(String persNo) {
        PersNo = persNo;
    }

    public String getTimeidNo() {
        return TimeidNo;
    }

    public void setTimeidNo(String timeidNo) {
        TimeidNo = timeidNo;
    }

    public String getActType() {
        return ActType;
    }

    public void setActType(String actType) {
        ActType = actType;
    }

    public String getWagetype() {
        return Wagetype;
    }

    public void setWagetype(String wagetype) {
        Wagetype = wagetype;
    }

    public String getCalcMotive() {
        return CalcMotive;
    }

    public void setCalcMotive(String calcMotive) {
        CalcMotive = calcMotive;
    }

    public String getExCreatedBy() {
        return ExCreatedBy;
    }

    public void setExCreatedBy(String exCreatedBy) {
        ExCreatedBy = exCreatedBy;
    }

    public GregorianCalendar getExCreatedDate() {
        return ExCreatedDate;
    }

    public void setExCreatedDate(GregorianCalendar exCreatedDate) {
        ExCreatedDate = exCreatedDate;
    }

    public Time getExCreatedTime() {
        return ExCreatedTime;
    }

    public void setExCreatedTime(Time exCreatedTime) {
        ExCreatedTime = exCreatedTime;
    }

    public UUID getExIdent() {
        return ExIdent;
    }

    public void setExIdent(UUID exIdent) {
        ExIdent = exIdent;
    }

    public BigDecimal getActWork2() {
        return ActWork2;
    }

    //End of Setters and Getters Method


   /* public WOConfirmation(String confNo, String confCounter, String workOrderNum, String operationNum) {
        super();
        ConfNo = confNo;
        ConfCounter = confCounter;
        WorkOrderNum = workOrderNum;
        OperationNum = operationNum;
        setEntitySetName(ZCollections.WO_CONFIRMATION__COLLECTION);
        setEntityType(ZCollections.WO_CONFIRMATION_ENTITY_TYPE);
    }*/

    public void setActWork2(BigDecimal actWork2) {
        ActWork2 = actWork2;
    }

    public BigDecimal getRemWork2() {
        return RemWork2;
    }

    public void setRemWork2(BigDecimal remWork2) {
        RemWork2 = remWork2;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getComplete() {
        return Complete;
    }

    public void setComplete(String complete) {
        Complete = complete;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.WO_CONFIRMATION__COLLECTION);
        this.setEntityType(ZCollections.WO_CONFIRMATION_ENTITY_TYPE);
        this.addKeyFieldNames(ZConfigManager.WO_KEY_FIELD);
        this.addKeyFieldNames(ZConfigManager.OPR_KEY_FIELD1);
        this.addKeyFieldNames("ConfNo");
        this.addKeyFieldNames("ConfCounter");
        this.setParentEntitySetName(ZCollections.WO_COLLECTION);
        this.setParentForeignKeyFieldName(ZConfigManager.WO_KEY_FIELD);
        this.setParentKeyFieldName(ZConfigManager.WO_KEY_FIELD);
    }

    public int getConfirmationsCount(String workOrderNum, String operationNum, String confNo) {
        int count = 0;
        try {
            if (confNo == null)
                confNo = "";
            ResponseObject result = getConfirmations(ZAppSettings.FetchLevel.Count, workOrderNum, operationNum, confNo);
            if (!result.isError()) {
                count = (int) result.Content();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return count;
    }

    public void setConfirmationCounter(Operation operation) {
        try {
            setConfCounter(String.format("%08d", (getConfirmationsCount(operation.getWorkOrderNum(), operation.getOperationNum(), operation.getConfNo()) + 1)));
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public String getOnlineSearch() {
        return OnlineSearch;
    }

    public void setOnlineSearch(String onlineSearch) {
        OnlineSearch = onlineSearch;
    }
}