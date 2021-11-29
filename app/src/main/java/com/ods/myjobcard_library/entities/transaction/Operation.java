package com.ods.myjobcard_library.entities.transaction;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ods.myjobcard_library.R;
import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.PartnerAddress;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.appsettings.StatusCategory;
import com.ods.myjobcard_library.entities.ctentities.OrderTypeFeature;
import com.ods.myjobcard_library.entities.ctentities.SpinnerItem;
import com.ods.myjobcard_library.entities.ctentities.UserTable;
import com.ods.myjobcard_library.entities.ctentities.WorkOrderStatus;
import com.ods.myjobcard_library.entities.forms.FormAssignmentSetModel;
import com.ods.myjobcard_library.entities.forms.ManualFormAssignmentSetModel;
import com.ods.myjobcard_library.entities.supervisor.SupervisorWorkOrder;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.client.odata.v4.EntityValue;
import com.sap.smp.client.odata.ODataEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Operation extends ZBaseEntity implements Serializable {

    private static DataHelper dbHelper = null;
    public boolean isOnline;   //Added by Anil
    //WO child elements
    private DataHelper dataHelper = null;
    public ArrayList<StatusCategory> validStatuses = null;
    private ZAppSettings.MobileStatus oprMobileStatus;
    private StatusCategory statusDetail;
    private String OperationNum;
    private String OrderType;
    private String WorkOrderNum;
    private String Counter;
    private String MaterialGroup;
    private String ControlKey;
    private String Plant;
    private String ActivityType;
    private String ShortText;
    private String SystemCondition;
    private String Priority;
    private String BusinessArea;
    private String PlanningPlant;
    private String InspectionType;
    private String FuncLoc;
    private String Equipment;
    private String SubOperation;
    private String WorkCenter;
    private String NotificationNum;
    private BigDecimal ActualWork;
    private String SystemStatusCode;
    private String SystemStatus;
    private String UserStatusCode;
    private String UserStatus;
    private String MobileStatus;
    private String MobileObjectType;
    private String StatusFlag;
    private String TransferFlag;
    private String TransferReason;
    private String TransferPerson;
    public ArrayList<PartnerAddress> partnerAddresses;
    private String PlannofOpera;
    private String Sequence;
    private String StdTextkey;
    private String SupOpNode;
    private String SortTerm;
    private String PersonnelNo;
    private Boolean DeletionFlag;
    private GregorianCalendar EarlSchStartExecDate;
    private Time EarlSchStartExecTime;
    private GregorianCalendar EarlSchFinishExecDate;
    private Time EarlSchFinishExecTime;
    private GregorianCalendar ActStartExecDate;
    private Time ActStartExecTime;
    private GregorianCalendar ActFinishExecDate;
    private Time ActFinishExectime;
    private GregorianCalendar LatestSchStartDate;
    private Time LatestSchStartTime;
    private GregorianCalendar LatestSchFinishDate;
    private Time LatestSchFinishTime;
    private String TempID;
    private String Notes;
    private String ConfNo;
    private String OpObjectNum;
    private String EquipCategory;
    private String FuncLocCategory;
    private String ToWorkCenter;
    private String TrackingNu;
    private BigDecimal NormalDuration;
    private BigDecimal Work;
    private String WoPriority;
    private String PurchaseGroup;
    private String UnitWork;
    private String CalculationKey;
    private String EnteredBy;
    private Short NumberPerson;
    private String OnlineSearch;
    private String NormalDurationUnit;
    private String Vendor;


    //Added new fields for getting tasktype operation's forms data
    private String TaskListType;
    private String Group;
    private String GroupCounter;
    private String InternalCounter;

    public Operation(EntityValue oprEntity) {
        create(oprEntity);
        initializeEntityProperties();
        isOnline = true;
        deriveOperationStatus();
    }

    public Operation(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel, boolean fetchAddress) {
        try {
            partnerAddresses = new ArrayList<PartnerAddress>();
            validStatuses = new ArrayList<>();
            initializeEntityProperties();
            create(entity, fetchLevel);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());

        }

    }

    //Setters and Getters Method

    public Operation() {
    }

    public Operation(String orderId, String operationID) {
        super();
        initializeEntityProperties();
        this.WorkOrderNum = orderId;
        this.OperationNum = operationID;
        this.SubOperation = "";
    }

    public Operation(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity, fetchLevel);
    }
    public Operation(ZODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity, fetchLevel);
    }

    public static Operation getOprByOrderIdFromOperations(List<Operation> opertionList, String oprNum) {
        for (Operation opr : opertionList) {
            if (opr.getOperationNum().equals(oprNum))
                return opr;
        }
        return null;
    }

    public static ArrayList<Operation> getFilteredWorkOrdersOperations(@NonNull String filterQuery, ZAppSettings.FetchLevel fetchLevel, String OrderByCriteria) {
        ResponseObject result = null;
        String entitySetName = ZCollections.OPR_COLLECTION;
        String resPath = entitySetName;
        String orderByUrl = "$orderby=";
        ArrayList<Operation> operations = new ArrayList<>();
        boolean fetchAddress = false;
        try {
            if (OrderByCriteria == null || OrderByCriteria.isEmpty())
                OrderByCriteria = "OperationNum";
            orderByUrl += OrderByCriteria;
            if (!filterQuery.isEmpty()) {
                if (fetchLevel == ZAppSettings.FetchLevel.Count)
                    resPath += "/$count" + filterQuery;
                else
                    resPath += filterQuery + "&";
            } else
                resPath += "?";
            switch (fetchLevel) {
                case List:
                case ListWithStatusAllowed:
                    resPath = resPath + "$select=OperationNum,SubOperation,WorkOrderNum,PlannofOpera,Counter,ControlKey,ShortText,MobileStatus,EarlSchStartExecDate,EarlSchStartExecTime,EarlSchFinishExecDate,EarlSchFinishExecTime,SystemStatus,UserStatus,OrderType,PlanningPlant" + "&" + orderByUrl;
                    //resPath += "$select=WorkOrderNum,OrderType,Status,Priority,ShortText,BasicStrtDate,MobileObjStatus,TempID,UserStatus,BasicFnshDate,EquipNum,FuncLocation,ErrorEntity,MainWorkCtr" + "&" + orderByUrl;
                    break;
                /*case ListMap:
                    resPath += "$select=WorkOrderNum,OrderType,Status,Priority,ShortText,BasicStrtDate,MobileObjStatus,ObjectNumber,EquipNum,NotificationNum,FuncLocation,AddressNumber,WOAddressNumber,TempID,UserStatus,BasicFnshDate,ErrorEntity,MainWorkCtr" + "&" + orderByUrl;
                    fetchAddress = true;
                    break;*/
            }
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                if (fetchLevel != ZAppSettings.FetchLevel.Count) {
                    List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                    result = FromEntity(entities, fetchAddress, fetchLevel);
                    operations = (ArrayList<Operation>) result.Content();
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return operations;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, boolean fetchAddress, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<Operation> operations = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    operations.add(new Operation(entity, fetchLevel, fetchAddress));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", operations);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    //get methods
    public static ResponseObject getOperations(ZAppSettings.FetchLevel fetchLevel, String OrderNum, String oprNum) {
        ResponseObject result = null;
        String resPath = "";
        String strEntitySet = ZCollections.OPR_COLLECTION;
        String strOrderByURI = "&$orderby=OperationNum";
        try {
            resPath = strEntitySet;

            switch (fetchLevel) {
                case List:
                    if (OrderNum != null && OrderNum.length() > 0) {

                        if (OrderNum.startsWith(ZCollections.TEMP_ID_PREFIX)) {
                            resPath = strEntitySet + "?$filter=(TempID%20eq%20%27" + OrderNum + "%27 and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true and (SubOperation eq '' or SubOperation eq null))&$select=OperationNum,WorkOrderNum,PlannofOpera,Counter,ControlKey,ShortText,MobileStatus,EarlSchStartExecDate,EarlSchStartExecTime,EarlSchFinishExecDate,EarlSchFinishExecTime,SystemStatus,UserStatus,ActivityType,OpObjectNum,OrderType,PlanningPlant" + strOrderByURI;
                        } else {
                            resPath = strEntitySet + "?$filter=(WorkOrderNum%20eq%20%27" + OrderNum + "%27 and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true and (SubOperation eq '' or SubOperation eq null))&$select=OperationNum,WorkOrderNum,PlannofOpera,Counter,ControlKey,ShortText,MobileStatus,EarlSchStartExecDate,EarlSchStartExecTime,EarlSchFinishExecDate,EarlSchFinishExecTime,SystemStatus,UserStatus,ActivityType,OpObjectNum,OrderType,PlanningPlant" + strOrderByURI;
                        }
                    }
                    break;
                case ListSpinner:
                    if (OrderNum != null && OrderNum.length() > 0) {

                        if (OrderNum.startsWith(ZCollections.TEMP_ID_PREFIX)) {
                            resPath = strEntitySet + "?$filter=(TempID%20eq%20%27" + OrderNum + "%27 and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true and (SubOperation eq '' or SubOperation eq null))&$select=OperationNum,WorkOrderNum,PlannofOpera,Counter,ShortText,OrderType" + strOrderByURI;
                        } else {
                            resPath = strEntitySet + "?$filter=(WorkOrderNum%20eq%20%27" + OrderNum + "%27 and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true and (SubOperation eq '' or SubOperation eq null))&$select=OperationNum,WorkOrderNum,PlannofOpera,Counter,ShortText,OrderType" + strOrderByURI;
                        }
                    }
                    break;
                case Single:
                    if (OrderNum != null && OrderNum.length() > 0 && oprNum != null && oprNum.length() > 0) {

                        if (OrderNum.startsWith(ZCollections.TEMP_ID_PREFIX)) {
                            resPath = strEntitySet + "?$filter=(TempID%20eq%20%27" + OrderNum + "%27%20and%20OperationNum%20eq%20%27" + oprNum + "%27 and (SubOperation eq '' or SubOperation eq null))";
                        } else {
                            resPath = strEntitySet + "?$filter=(WorkOrderNum%20eq%20%27" + OrderNum + "%27%20and%20OperationNum%20eq%20%27" + oprNum + "%27 and (SubOperation eq '' or SubOperation eq null))";
                        }
                    }
                    break;
                default:
                    break;
            }

            result = DataHelper.getInstance().getEntities(strEntitySet, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities, fetchLevel);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ResponseObject getAllWorkOrderOperations(ZAppSettings.FetchLevel fetchLevel, String OrderNum) {
        ResponseObject result = null;
        String resPath = "";
        String strEntitySet = ZCollections.OPR_COLLECTION;
        String strOrderByURI = "&$orderby=OperationNum,SubOperation";

        try {
            resPath = strEntitySet;

            switch (fetchLevel) {
                case List:
                    if (OrderNum != null && OrderNum.length() > 0) {

                        if (OrderNum.startsWith(ZCollections.TEMP_ID_PREFIX)) {
                            resPath = strEntitySet + "?$filter=(TempID%20eq%20%27" + OrderNum + "%27 and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true)&$select=OperationNum,WorkOrderNum,PlannofOpera,Counter,ControlKey,ShortText,MobileStatus,EarlSchStartExecDate,EarlSchStartExecTime,EarlSchFinishExecDate,EarlSchFinishExecTime,SystemStatus,UserStatus,ActivityType,SubOperation,ConfNo,ActivityType,Plant,WorkCenter,PersonnelNo,OpObjectNum,Equipment,FuncLoc,OrderType,TaskListType,Group,GroupCounter,InternalCounter,ActualWork,Work,PlanningPlant" + strOrderByURI;
                        } else {
                            resPath = strEntitySet + "?$filter=(WorkOrderNum%20eq%20%27" + OrderNum + "%27 and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true)&$select=OperationNum,WorkOrderNum,PlannofOpera,Counter,ControlKey,ShortText,MobileStatus,EarlSchStartExecDate,EarlSchStartExecTime,EarlSchFinishExecDate,EarlSchFinishExecTime,SystemStatus,UserStatus,ActivityType,SubOperation,ConfNo,ActivityType,Plant,WorkCenter,PersonnelNo,OpObjectNum,Equipment,FuncLoc,OrderType,TaskListType,Group,GroupCounter,InternalCounter,ActualWork,Work,PlanningPlant" + strOrderByURI;
                        }
                    }
                    break;
                case ListSpinner:
                    if (OrderNum != null && OrderNum.length() > 0) {

                        if (OrderNum.startsWith(ZCollections.TEMP_ID_PREFIX)) {
                            resPath = strEntitySet + "?$filter=(TempID%20eq%20%27" + OrderNum + "%27 and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true)&$select=OperationNum,WorkOrderNum,PlannofOpera,Counter,ShortText,OrderType" + strOrderByURI;
                        } else {
                            resPath = strEntitySet + "?$filter=(WorkOrderNum%20eq%20%27" + OrderNum + "%27 and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true)&$select=OperationNum,WorkOrderNum,PlannofOpera,Counter,ShortText,OrderType" + strOrderByURI;
                        }
                    }
                    break;
                default:
                    break;
            }

            result = DataHelper.getInstance().getEntities(strEntitySet, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities, fetchLevel);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ArrayList<Operation> getAllOperations() {
        ArrayList<Operation> operations = new ArrayList<>();
        try {
            String entitySetName = ZCollections.OPR_COLLECTION;
            String resPath = entitySetName + "?$filter=startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true&$orderby=";
            ResponseObject response = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities != null && entities.size() > 0) {
                    response = FromEntity(entities, ZAppSettings.FetchLevel.List);
                    operations = (ArrayList<Operation>) response.Content();
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return operations;
    }

    public static Operation getOperation(String workOrderNum, String operationNum, String subOperation) {
        Operation operation = null;
        try {
            String entitySetName = ZCollections.OPR_COLLECTION;
            String resPath = entitySetName + "(WorkOrderNum='" + workOrderNum + "',OperationNum='" + operationNum + "',SubOperation='" + subOperation + "')";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                operation = new Operation((ODataEntity) result.Content(), ZAppSettings.FetchLevel.Single);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return operation;
    }

    public static Operation getSupervisorOperation(String workOrderNum, String operationNum, String subOperation) {
        Operation operation = null;
        try {
            //String entitySetName = ZCollections.OPR_COLLECTION;
            String entitySetName = ZCollections.SUPERVISOR_OPERATION_COLLECTIONS;
            //String resPath = entitySetName + "(WorkOrderNum='"+ workOrderNum +"',OperationNum='"+ operationNum +"',SubOperation='"+ subOperation +"')";
            String resPath = entitySetName + "(WorkOrderNum='" + workOrderNum + "',OperationNum='" + operationNum + "')";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                operation = new Operation((ODataEntity) result.Content(), ZAppSettings.FetchLevel.Single);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return operation;
    }

    public static ResponseObject getSupervisorOperations(ZAppSettings.FetchLevel fetchLevel, String OrderNum, String oprNum) {
        ResponseObject result = null;
        String resPath = "";
        String strEntitySet = ZCollections.SUPERVISOR_OPERATION_COLLECTIONS;
        String strOrderByURI = "&$orderby=OperationNum";
        try {
            resPath = strEntitySet;

            switch (fetchLevel) {
                case List:
                    if (OrderNum != null && OrderNum.length() > 0) {
                        //resPath = strEntitySet + "?$filter=(WorkOrderNum%20eq%20%27" + OrderNum + "%27 and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true)&$select=OperationNum,WorkOrderNum,PlannofOpera,ControlKey,ShortText,EarlSchStartExecDate,EarlSchStartExecTime,EarlSchFinishExecDate,EarlSchFinishExecTime,SystemStatus" + strOrderByURI;
                        resPath = strEntitySet + "?$filter=(WorkOrderNum%20eq%20%27" + OrderNum + "%27 and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true)&$select=OperationNum,WorkOrderNum,PlannofOpera,ControlKey,ShortText,EarlSchStartExecDate,EarlSchStartExecTime,EarlSchFinishExecDate,EarlSchFinishExecTime,SystemStatus,SubOperation,PlanningPlant" + strOrderByURI;
                    }
                    break;
                case ListSpinner:
                    if (OrderNum != null && OrderNum.length() > 0) {
                        resPath = strEntitySet + "?$filter=(WorkOrderNum%20eq%20%27" + OrderNum + "%27 and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true)&$select=OperationNum,WorkOrderNum,PlannofOpera" + strOrderByURI;
                    }
                    break;
                case Single:
                    if (OrderNum != null && OrderNum.length() > 0 && oprNum != null && oprNum.length() > 0) {
                        resPath = strEntitySet + "?$filter=(WorkOrderNum%20eq%20%27" + OrderNum + "%27%20and%20OperationNum%20eq%20%27" + oprNum + "%27)";
                    }
                    break;
                default:
                    break;
            }

            result = DataHelper.getInstance().getEntities(strEntitySet, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities, fetchLevel);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ResponseObject getWorkOrderOperations(String orderNum) {
        ResponseObject result = null;
        try {
            String resPath;
            List<ODataEntity> entities = new ArrayList<>();
            resPath = ZCollections.OPR_COLLECTION + "?$filter=WorkOrderNum eq '" + orderNum + "' and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true" +
                    "&$select=OperationNum,WorkOrderNum,PlannofOpera,Counter,ControlKey,ShortText,MobileStatus,EarlSchStartExecDate,EarlSchStartExecTime,EarlSchFinishExecDate,EarlSchFinishExecTime,SystemStatus,UserStatus,SubOperation,ConfNo,ActivityType,Plant,WorkCenter,PersonnelNo,PlanningPlant" +
                    "&$orderby=OperationNum,SubOperation";
            result = DataHelper.getInstance().getEntities(ZCollections.OPR_COLLECTION, resPath);
            if (result != null && !result.isError()) {
                entities.addAll((List<ODataEntity>) result.Content());
            }
            if (entities.size() > 0) {
                result = FromEntity(entities, ZAppSettings.FetchLevel.List);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<Operation> operations = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    operations.add(new Operation(entity, fetchLevel));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", operations);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(SupervisorWorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ResponseObject getHighestOperationNum(@NonNull String workOrderNum) {
        ResponseObject response = null;
        String resPath = ZCollections.OPR_COLLECTION;
        resPath += "?$filter=(WorkOrderNum eq '" + workOrderNum + "')&$orderby=OperationNum desc&$select=OperationNum&$top=1";
        try {
            response = DataHelper.getInstance().getEntities(ZCollections.OPR_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities.size() > 0) {
                    Operation operation = new Operation(entities.get(0), ZAppSettings.FetchLevel.Header);
                    if (operation != null) {
                        String oprNumStr = operation.getOperationNum();
                        int oprNumInt = (Integer.valueOf(oprNumStr) / 10);
                        response.setContent(oprNumInt);
                        return response;
                    }
                    response.setError(true);
                }
                response.setContent(0);
            } else {
                response = new ResponseObject(ZConfigManager.Status.Error);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
            response = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return response;
    }

    public static Operation getFirstOperation(@NonNull String workOrderNum) {
        ResponseObject response = null;
        String resPath = ZCollections.OPR_COLLECTION;
        resPath += "?$filter=(WorkOrderNum eq '" + workOrderNum + "')&$orderby=OperationNum&$select=OperationNum&$top=1";
        try {
            response = DataHelper.getInstance().getEntities(ZCollections.OPR_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities.size() > 0) {
                    return new Operation(entities.get(0), ZAppSettings.FetchLevel.Header);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return null;
    }

    public static ResponseObject getOperationPositionInList(ArrayList<Operation> operations, String operationNum) {
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);
        try {
            if (operations.size() > 0) {
                for (int i = 0; i < operations.size(); i++) {
                    if (operationNum.equalsIgnoreCase(operations.get(i).getOperationNum())) {
                        result.setStatus(ZConfigManager.Status.Success);
                        result.setContent(i);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result.setMessage(e.getMessage());
        }
        return result;
    }

    public static int getNumberOfActiveOperation(String woNum, String oprNum) {
        ResponseObject responseObject = null;
        String strResPath, strQuery = "", strQuery1 = "";
        int intCounter = 0;
        Object rawData = null;
        try {
            for (ZAppSettings.MobileStatus status : ZAppSettings.MobileStatus.values()) {
                if (status.getMobileStatusIsConsideredActive()) {
                    if (strQuery.length() == 0) {

                        strQuery = "startswith(UserStatus,'" + status.getMobileStatusCode() + "') eq true";
                        strQuery1 = "startswith(MobileStatus,'" + status.getMobileStatusCode() + "') eq true";
                    } else {
                        strQuery = strQuery + " or " + "startswith(UserStatus,'" + status.getMobileStatusCode() + "') eq true";
                        strQuery1 = strQuery1 + " or " + "startswith(MobileStatus,'" + status.getMobileStatusCode() + "') eq true";
                    }
                }

            }
            /*strResPath = ZCollections.OPR_COLLECTION + "/$count?$filter=" + (woNum != null && !woNum.isEmpty() ? "WorkOrderNum eq '" + woNum + "' and " : "") + "MobileStatus eq '' and (" + strQuery + ") and (SubOperation eq '' or SubOperation eq null)";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.OPR_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intCounter = Integer.parseInt(rawData.toString());
            }*/
            strResPath = ZCollections.OPR_COLLECTION + "/$count?$filter=" + (woNum != null && !woNum.isEmpty() ? "WorkOrderNum eq '" + woNum + "' and " : "") + (oprNum != null && !oprNum.isEmpty() ? "OperationNum eq '" + oprNum + "' and " : "") + "(" + strQuery1 + ") and (SubOperation eq '' or SubOperation eq null)";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.OPR_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intCounter = intCounter + Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intCounter;
    }

    public static int getNumberOfActiveOperations(String woNum) {
        ResponseObject responseObject = null;
        String strResPath, strQuery = "", strQuery1 = "";
        int intCounter = 0;
        Object rawData = null;
        try {
            for (ZAppSettings.MobileStatus status : ZAppSettings.MobileStatus.values()) {
                if (status.getMobileStatusIsConsideredActive()) {
                    if (strQuery.length() == 0) {

                        strQuery = "startswith(UserStatus,'" + status.getMobileStatusCode() + "') eq true";
                        strQuery1 = "startswith(MobileStatus,'" + status.getMobileStatusCode() + "') eq true";
                    } else {
                        strQuery = strQuery + " or " + "startswith(UserStatus,'" + status.getMobileStatusCode() + "') eq true";
                        strQuery1 = strQuery1 + " or " + "startswith(MobileStatus,'" + status.getMobileStatusCode() + "') eq true";
                    }
                }

            }
            /*strResPath = ZCollections.OPR_COLLECTION + "/$count?$filter=" + (woNum != null && !woNum.isEmpty() ? "WorkOrderNum eq '" + woNum + "' and " : "") + "MobileStatus eq '' and (" + strQuery + ") and (SubOperation eq '' or SubOperation eq null)";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.OPR_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intCounter = Integer.parseInt(rawData.toString());
            }*/
            strResPath = ZCollections.OPR_COLLECTION + "/$count?$filter=" + (woNum != null && !woNum.isEmpty() ? "WorkOrderNum eq '" + woNum + "' and " : "") + "(" + strQuery1 + ") and (SubOperation eq '' or SubOperation eq null)";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.OPR_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intCounter = intCounter + Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intCounter;
    }

    public static String getTotalPlannedWork(String workOrderNum) {
        String plannedWork = "";
        BigDecimal totalWork = new BigDecimal(BigInteger.ZERO);
        Operation operation = null;
        try {
            String entitySetName = ZCollections.OPR_COLLECTION;
            String resPath = entitySetName + "?$filter=(WorkOrderNum eq '" + workOrderNum + "')";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                for (ODataEntity entity : entities) {
                    operation = new Operation(entity, ZAppSettings.FetchLevel.All);
                    totalWork = totalWork.add(operation.getWork());
                }
                if (operation != null)
                    plannedWork = totalWork.toString() + " " + operation.getUnitWork();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return plannedWork;
    }

    @Override
    public boolean isLocal() {
        boolean local = super.isLocal();
        if (!local) {
            return this.getEnteredBy() != null && this.getEnteredBy().equalsIgnoreCase(ZAppSettings.strUser);
        } else return true;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.OPR_COLLECTION);
        this.setEntityType(ZCollections.OPR_ENTITY_TYPE1);
        this.addKeyFieldNames(ZConfigManager.WO_KEY_FIELD);
        this.addKeyFieldNames(ZConfigManager.OPR_KEY_FIELD1);
        this.addKeyFieldNames(ZConfigManager.OPR_KEY_FIELD2);
        this.setParentEntitySetName(ZCollections.WO_COLLECTION);
        this.setParentForeignKeyFieldName(ZConfigManager.WO_KEY_FIELD);
        this.setParentKeyFieldName(ZConfigManager.WO_KEY_FIELD);
    }

    public String getOperationNum() {
        return OperationNum;
    }

    public void setOperationNum(String operationNum) {
        OperationNum = operationNum;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public String getMaterialGroup() {
        return MaterialGroup;
    }

    public void setMaterialGroup(String materialGroup) {
        MaterialGroup = materialGroup;
    }

    public String getControlKey() {
        return ControlKey;
    }

    public void setControlKey(String controlKey) {
        ControlKey = controlKey;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getActivityType() {
        return ActivityType;
    }

    public void setActivityType(String activiityType) {
        ActivityType = activiityType;
    }

    public String getShortText() {
        return ShortText;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }

    public String getSystemCondition() {
        return SystemCondition;
    }

    public void setSystemCondition(String systemCondition) {
        SystemCondition = systemCondition;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getBusinessArea() {
        return BusinessArea;
    }

    public void setBusinessArea(String businessArea) {
        BusinessArea = businessArea;
    }

    public String getPlanningPlant() {
        return PlanningPlant;
    }

    public void setPlanningPlant(String planningPlant) {
        PlanningPlant = planningPlant;
    }

    public String getInspectionType() {
        return InspectionType;
    }

    public void setInspectionType(String inspectionType) {
        InspectionType = inspectionType;
    }

    public String getFuncLoc() {
        return FuncLoc;
    }

    public void setFuncLoc(String funcLoc) {
        FuncLoc = funcLoc;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getSubOperation() {
        return SubOperation;
    }

    public void setSubOperation(String subOperation) {
        SubOperation = subOperation;
    }

    public String getWorkCenter() {
        return WorkCenter;
    }

    public void setWorkCenter(String workCenter) {
        WorkCenter = workCenter;
    }

    public String getNotificationNum() {
        return NotificationNum;
    }

    public void setNotificationNum(String notificationNum) {
        NotificationNum = notificationNum;
    }

    public BigDecimal getActualWork() {
        return ActualWork;
    }

    public void setActualWork(BigDecimal actualWork) {
        ActualWork = actualWork;
    }

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
    }

    public String getSystemStatusCode() {
        return SystemStatusCode;
    }

    public void setSystemStatusCode(String systemStatusCode) {
        SystemStatusCode = systemStatusCode;
    }

    public String getSystemStatus() {
        return SystemStatus;
    }

    public void setSystemStatus(String systemStatus) {
        SystemStatus = systemStatus;
    }

    public String getUserStatusCode() {
        return UserStatusCode;
    }

    public void setUserStatusCode(String userStatusCode) {
        UserStatusCode = userStatusCode;
    }

    public String getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(String userStatus) {
        UserStatus = userStatus;
    }

    public String getMobileStatus() {
        return MobileStatus;
    }

    public void setMobileStatus(String mobileStatus) {
        MobileStatus = mobileStatus;
    }

    public String getMobileObjectType() {
        return MobileObjectType;
    }

    public void setMobileObjectType(String mobileObjectType) {
        MobileObjectType = mobileObjectType;
    }

    public String getEquipCategory() {
        return EquipCategory;
    }

    public void setEquipCategory(String equipCategory) {
        EquipCategory = equipCategory;
    }

    public String getFuncLocCategory() {
        return FuncLocCategory;
    }

    public void setFuncLocCategory(String funcLocCategory) {
        FuncLocCategory = funcLocCategory;
    }

    public String getToWorkCenter() {
        return ToWorkCenter;
    }

    public void setToWorkCenter(String toWorkCenter) {
        ToWorkCenter = toWorkCenter;
    }

    public String getTrackingNu() {
        return TrackingNu;
    }

    public void setTrackingNu(String trackingNu) {
        TrackingNu = trackingNu;
    }

    public BigDecimal getNormalDuration() {
        return NormalDuration;
    }

    public void setNormalDuration(BigDecimal normalDuration) {
        NormalDuration = normalDuration;
    }

    public BigDecimal getWork() {
        return Work;
    }

    public void setWork(BigDecimal work) {
        Work = work;
    }

    public String getWoPriority() {
        return WoPriority;
    }

    public void setWoPriority(String woPriority) {
        WoPriority = woPriority;
    }

    //New fields Setter and Getter Methods 11 May 2016

    public String getPurchaseGroup() {
        return PurchaseGroup;
    }

    public void setPurchaseGroup(String purchaseGroup) {
        PurchaseGroup = purchaseGroup;
    }

    public String getUnitWork() {
        return UnitWork;
    }

    public void setUnitWork(String unitWork) {
        UnitWork = unitWork;
    }

    public String getCalculationKey() {
        return CalculationKey;
    }

    public void setCalculationKey(String calculationKey) {
        CalculationKey = calculationKey;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public Short getNumberPerson() {
        return NumberPerson;
    }

    public void setNumberPerson(Short numberPerson) {
        NumberPerson = numberPerson;
    }

    public String getOnlineSearch() {
        return OnlineSearch;
    }

    public void setOnlineSearch(String onlineSearch) {
        OnlineSearch = onlineSearch;
    }

    public String getNormalDurationUnit() {
        return NormalDurationUnit;
    }

    public void setNormalDurationUnit(String normalDurationUnit) {
        NormalDurationUnit = normalDurationUnit;
    }

    public String getVendor() {
        return Vendor;
    }

    public void setVendor(String vendor) {
        Vendor = vendor;
    }

    public String getOpObjectNum() {
        return OpObjectNum;
    }

    public void setOpObjectNum(String opObjectNum) {
        OpObjectNum = opObjectNum;
    }

    public String getPlannofOpera() {
        return PlannofOpera;
    }

    public void setPlannofOpera(String plannofOpera) {
        PlannofOpera = plannofOpera;
    }

    public String getSequence() {
        return Sequence;
    }

    public void setSequence(String sequence) {
        Sequence = sequence;
    }

    public String getStdTextkey() {
        return StdTextkey;
    }

    public void setStdTextkey(String stdTextkey) {
        StdTextkey = stdTextkey;
    }

    public String getSupOpNode() {
        return SupOpNode;
    }

    public void setSupOpNode(String supOpNode) {
        SupOpNode = supOpNode;
    }

    public String getSortTerm() {
        return SortTerm;
    }

    public void setSortTerm(String sortTerm) {
        SortTerm = sortTerm;
    }

    public String getPersonnelNo() {
        return PersonnelNo;
    }

    public void setPersonnelNo(String personnelNo) {
        PersonnelNo = personnelNo;
    }

    public Boolean getDeletionFlag() {
        return DeletionFlag;
    }

    public void setDeletionFlag(Boolean deletionFlag) {
        DeletionFlag = deletionFlag;
    }

    public GregorianCalendar getEarlSchStartExecDate() {
        return EarlSchStartExecDate;
    }

    public void setEarlSchStartExecDate(GregorianCalendar earlSchStartExecDate) {
        EarlSchStartExecDate = earlSchStartExecDate;
    }

    public Time getEarlSchStartExecTime() {
        return EarlSchStartExecTime;
    }

    public void setEarlSchStartExecTime(Time earlSchStartExecTime) {
        EarlSchStartExecTime = earlSchStartExecTime;
    }

    public GregorianCalendar getEarlSchFinishExecDate() {
        return EarlSchFinishExecDate;
    }

    public void setEarlSchFinishExecDate(GregorianCalendar earlSchFinishExecDate) {
        EarlSchFinishExecDate = earlSchFinishExecDate;
    }

    public Time getEarlSchFinishExecTime() {
        return EarlSchFinishExecTime;
    }

    public void setEarlSchFinishExecTime(Time earlSchFinishExecTime) {
        EarlSchFinishExecTime = earlSchFinishExecTime;
    }

    /*public boolean getStatusFlag() {
        return StatusFlag;
    }

    public void setStatusFlag(boolean statusFlag) {
        StatusFlag = statusFlag;
    }*/

    public GregorianCalendar getActStartExecDate() {
        return ActStartExecDate;
    }

    public void setActStartExecDate(GregorianCalendar actStartExecDate) {
        ActStartExecDate = actStartExecDate;
    }

    public Time getActStartExecTime() {
        return ActStartExecTime;
    }

    public void setActStartExecTime(Time actStartExecTime) {
        ActStartExecTime = actStartExecTime;
    }

    public GregorianCalendar getActFinishExecDate() {
        return ActFinishExecDate;
    }

    public void setActFinishExecDate(GregorianCalendar actFinishExecDate) {
        ActFinishExecDate = actFinishExecDate;
    }

    public Time getActFinishExectime() {
        return ActFinishExectime;
    }

    public void setActFinishExectime(Time actFinishExectime) {
        ActFinishExectime = actFinishExectime;
    }

    public GregorianCalendar getLatestSchStartDate() {
        return LatestSchStartDate;
    }

    public void setLatestSchStartDate(GregorianCalendar latestSchStartDate) {
        LatestSchStartDate = latestSchStartDate;
    }

    public Time getLatestSchStartTime() {
        return LatestSchStartTime;
    }

    public void setLatestSchStartTime(Time latestSchStartTime) {
        LatestSchStartTime = latestSchStartTime;
    }

    //End of new Fields Setter and Getter Methods 11 May 2016

    //End of Setters and Getters Method

    public GregorianCalendar getLatestSchFinishDate() {
        return LatestSchFinishDate;
    }

    public void setLatestSchFinishDate(GregorianCalendar latestSchFinishDate) {
        LatestSchFinishDate = latestSchFinishDate;
    }

    public Time getLatestSchFinishTime() {
        return LatestSchFinishTime;
    }

    public void setLatestSchFinishTime(Time latestSchFinishTime) {
        LatestSchFinishTime = latestSchFinishTime;
    }

    public String getStatusFlag() {
        return StatusFlag;
    }

    /*public ResponseObject toEntity() {
        ResponseObject result = null;
        ODataEntity oprEntity;
        Class<?> cls = null;
        Field[] declaredFields = null;
        Method method = null;
        try {
            oprEntity = new ODataEntityDefaultImpl(ZCollections.OPR_ENTITY_TYPE1);
            oprEntity.setResourcePath(ZCollections.OPR_COLLECTION, ZCollections.OPR_COLLECTION);
            cls = this.getClass();
            declaredFields = cls.getDeclaredFields();

            for (Field field : declaredFields) {
                //field.setAccessible(true);
                //classFields.add(field.getName());
                if (!field.getType().getSimpleName().equalsIgnoreCase("Creator") && !field.getType().getSimpleName().equalsIgnoreCase("DateFormat")) {
                    try {
                        method = cls.getDeclaredMethod("get" + field.getName(), field.getType());
                        oprEntity.getProperties().put(field.getName(),
                                new ODataPropertyDefaultImpl(field.getName(),
                                        method.invoke(this, null)));

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }
            }
            result = new ResponseObject(ZConfigManager.Status.Success,"",oprEntity);
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getLocalizedMessage());
            result = new ResponseObject(ZConfigManager.Status.Error,e.getMessage(),null);
        }
        return result;
    }*/

    public void setStatusFlag(String statusFlag) {
        StatusFlag = statusFlag;
    }

    public String getTransferFlag() {
        return TransferFlag;
    }

    public void setTransferFlag(String transferFlag) {
        TransferFlag = transferFlag;
    }

    public String getTransferReason() {
        return TransferReason;
    }

    public void setTransferReason(String transferReason) {
        TransferReason = transferReason;
    }

    public String getTransferPerson() {
        return TransferPerson;
    }

    public void setTransferPerson(String transferPerson) {
        TransferPerson = transferPerson;
    }

    public void addValidStatus(StatusCategory validStatus) {
        if (validStatuses == null)
            validStatuses = new ArrayList<>();
        validStatuses.add(validStatus);
    }

    public ArrayList<StatusCategory> getValidStatuses() {
        return validStatuses;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getConfNo() {
        return ConfNo;
    }

    public void setConfNo(String confNo) {
        ConfNo = confNo;
    }

    public boolean isInitialized() {
        return (!TextUtils.isEmpty(this.WorkOrderNum) && !TextUtils.isEmpty(this.OperationNum));
    }

    public String getTaskListType() {
        return TaskListType;
    }

    public void setTaskListType(String taskListType) {
        TaskListType = taskListType;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }

    public String getGroupCounter() {
        return GroupCounter;
    }

    public void setGroupCounter(String groupCounter) {
        GroupCounter = groupCounter;
    }

    public String getInternalCounter() {
        return InternalCounter;
    }

    public void setInternalCounter(String internalCounter) {
        InternalCounter = internalCounter;
    }

    /*public static ResponseObject getWorkOrderOperations(String orderByCriteria) {
        ResponseObject result = null;
        try {
            result = WorkOrder.getWorkOrders(ZAppSettings.FetchLevel.List, null, orderByCriteria);
            if (result != null && !result.isError()) {
                ArrayList<WorkOrder> workOrders = (ArrayList<WorkOrder>) result.Content();
                String resPath;
                List<ODataEntity> entities = new ArrayList<>();
                for (WorkOrder order : workOrders) {
                    resPath = ZCollections.OPR_COLLECTION + "?$filter=WorkOrderNum eq '" + order.getWorkOrderNum() + "' and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true and (SubOperation eq '' or SubOperation eq null)" +
                            "&$select=OperationNum,WorkOrderNum,PlannofOpera,Counter,ControlKey,ShortText,MobileStatus,EarlSchStartExecDate,EarlSchStartExecTime,EarlSchFinishExecDate,EarlSchFinishExecTime,SystemStatus,UserStatus" +
                            "&$orderby=OperationNum";
                    result = DataHelper.getInstance().getEntities(ZCollections.OPR_COLLECTION, resPath);
                    if (result != null && !result.isError()) {
                        entities.addAll((List<ODataEntity>) result.Content());
                    }
                }
                if (entities.size() > 0) {
                    result = FromEntity(entities, ZAppSettings.FetchLevel.List);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }*/

    public ResponseObject create(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        try {
            super.create(entity);
            //Extract PRT and Components only when Operation get iformation is not limited to header only
            if (fetchLevel.equals(ZAppSettings.Hierarchy.All) || fetchLevel.equals(ZAppSettings.FetchLevel.Single) || fetchLevel.equals(ZAppSettings.FetchLevel.List) || fetchLevel.equals(ZAppSettings.FetchLevel.ListWithStatusAllowed)) {
                deriveOperationStatus();
            }
            result = new ResponseObject(ZConfigManager.Status.Success, "", this);
        } catch (Exception e) {
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }
    public ResponseObject create(ZODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        try {
            super.create(entity);
            //Extract PRT and Components only when Operation get iformation is not limited to header only
            if (fetchLevel.equals(ZAppSettings.Hierarchy.All) || fetchLevel.equals(ZAppSettings.FetchLevel.Single) || fetchLevel.equals(ZAppSettings.FetchLevel.List) || fetchLevel.equals(ZAppSettings.FetchLevel.ListWithStatusAllowed)) {
                deriveOperationStatus();
            }
            result = new ResponseObject(ZConfigManager.Status.Success, "", this);
        } catch (Exception e) {
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    //Mark Complete
    public boolean isComplete() {
        boolean result = false;
        ZAppSettings.MobileStatus oprStatus;
        try {
            /*oprStatus = getOprMobileStatus();
            if (oprStatus.equals(ZAppSettings.MobileStatus.COMPLETE) || oprStatus.equals(ZAppSettings.MobileStatus.CONFIRMED)) {
                result = true;
            }*/
            result = getStatusDetail().preCompletionCheckEnabled() || (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED && getSystemStatus().contains(ZAppSettings.MobileStatus.CONFIRMED.getMobileStatusCode()) && !getSystemStatus().contains(ZAppSettings.MobileStatus.PARTIALCONFIRMED.getMobileStatusCode()));
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public boolean isActive() {
        try {
            return getStatusDetail().isInProcess();
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return false;
        }
    }

    public ResponseObject markComplete(@Nullable BigDecimal actWork, boolean autoFlush, boolean sendConfirmation, boolean isCompleted) {
        ResponseObject result = null;
        WOConfirmation confirmation;
        try {

            //Create confirmation for the Operation if operation is updated as completed
            if (sendConfirmation) { //!result.isError() &&
                confirmation = createCompletionConfirmation(isCompleted);
                if (actWork != null && actWork.doubleValue() != 0) {
                    confirmation.setActWork(actWork);
                    confirmation.setUnWork("H");
                }
                result = confirmation.SaveToStore(autoFlush);
            }

            //Set Operation flag as complete
            setMobileStatus(isCompleted ? ZAppSettings.MobileStatus.CONFIRMED.getMobileStatusCode() : ZAppSettings.MobileStatus.PARTIALCONFIRMED.getMobileStatusCode());
            setSystemStatus(isCompleted ? ZAppSettings.MobileStatus.CONFIRMED.getMobileStatusCode() : ZAppSettings.MobileStatus.PARTIALCONFIRMED.getMobileStatusCode());
            setStatusFlag(ZConfigManager.STATUS_SET_FLAG);
            //setStatusFlag(true);
            setMode(ZAppSettings.EntityMode.Update);
            result = SaveToStore(autoFlush);

        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public ResponseObject markInComplete(boolean autoFlush) {
        ResponseObject result = null;
        WOConfirmation confirmation=null;
        try {
            StatusCategory status = StatusCategory.getStatusDetails(ZConfigManager.OPERATION_STATUS_TO_MARK_INCOMPLETE, getOrderType(), ZConfigManager.Fetch_Object_Type.Operation);
            //Set Operation flag as complete
            setMobileStatus(ZConfigManager.OPERATION_STATUS_TO_MARK_INCOMPLETE);
            setSystemStatus(ZAppSettings.MobileStatus.Released.getMobileStatusCode());
            setStatusFlag(ZConfigManager.STATUS_SET_FLAG);
            if (status != null) {
                setMobileObjectType(status.getObjectType());
            } else
                setMobileObjectType("X");
            setMode(ZAppSettings.EntityMode.Update);
            result = SaveToStore(true);//confirmation == null
            if (!result.isError()) {
                if (ZConfigManager.ENABLE_CANCEL_FINAL_CONFIRMATION) {
                    //get the final confirmation for the operation to mark it cancel
                    confirmation = WOConfirmation.getOperationFinalConfirmation(getOperationNum(), getWorkOrderNum());
                    if (confirmation != null) {
                        //Create confirmation for the Operation if operation is updated as completed
                        confirmation.setConfText(ZConfigManager.CANCELLED_FINAL_CNF_TEXT);
                        confirmation.setMode(ZAppSettings.EntityMode.Update);
                        result = confirmation.SaveToStore(autoFlush);
                    }
                } else {
                    int intCount=0;
                    confirmation = new WOConfirmation();
                    try {
                        result = confirmation.getConfirmations(ZAppSettings.FetchLevel.Count, getWorkOrderNum(), getOperationNum(), getConfNo());
                        if (!result.isError()) {
                            intCount = (int) result.Content();
                        }
                    } catch (Exception e) {
                        DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                    }
                    confirmation.setWorkOrderNum(getWorkOrderNum());
                    confirmation.setOperationNum(getOperationNum());
                    confirmation.setConfNo(getConfNo());
                    confirmation.setConfCounter(String.format("%08d", Integer.valueOf(intCount + 1)));
                    confirmation.setActWork(new BigDecimal(0));
                    confirmation.setUnWork("H");
                    confirmation.setClearRes(false);
                    confirmation.setPostgDate(ZCommon.getDeviceDateTime());
                    confirmation.setPlant(getPlant());
                    confirmation.setWorkCntr(getWorkCenter());
                    confirmation.setSubOper(getSubOperation());
                    String personnelNum = getPersonnelNo();
                    try {
                        if (personnelNum == null || personnelNum.isEmpty() || Integer.parseInt(personnelNum) == 0)
                            personnelNum = UserTable.getUserPersonnelNumber();
                    } catch (Exception e) {
                        DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                    }
                    confirmation.setPersNo(personnelNum);
                    confirmation.setExCreatedBy(ZAppSettings.strUser);
                    confirmation.setExCreatedTime(new Time(ZCommon.getDeviceTime().getTime()));
                    confirmation.setExecStartDate(ZCommon.getDeviceDateTime());
                    confirmation.setExecStartTime(new Time(ZCommon.getDeviceTime().getTime()));
                    confirmation.setExecFinDate(ZCommon.getDeviceDateTime());
                    confirmation.setExecFinTime(new Time(ZCommon.getDeviceTime().getTime()));
                    confirmation.setFinConf("");
                    confirmation.setComplete("");
                    confirmation.setConfText(ZConfigManager.REVERT_FINAL_CNF_TEXT);
                    confirmation.setMode(ZAppSettings.EntityMode.Create);
                    result = confirmation.SaveToStore(autoFlush);
                }
            }

            /*if (!result.isError()) {
                if (confirmation != null) {
                    confirmation.setConfText("Cancelled confirmation");
                    confirmation.setMode(ZAppSettings.EntityMode.Update);
                    result = confirmation.SaveToStore(autoFlush);
                }
            }*/

        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    //Status Management

    public WOConfirmation createCompletionConfirmation(boolean isCompleted) {
        WOConfirmation confirmation = null;
        try {
            confirmation = new WOConfirmation(this);
            if (isCompleted) {
                confirmation.setComplete(ZConfigManager.STATUS_SET_FLAG);
                confirmation.setFinConf(ZConfigManager.STATUS_SET_FLAG);
                confirmation.setClearRes(true);
                confirmation.setConfText(ZCollections.OPERATION_COMPLETE_TEXT);
            } else {
                confirmation.setComplete("");
                confirmation.setFinConf("");
                confirmation.setClearRes(false);
                confirmation.setConfText("Operation Checked");
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return confirmation;
    }

    public WOConfirmation getCancelConfirmation() {
        WOConfirmation confirmation = null;
        ArrayList<WOConfirmation> confirmations = null;
        ResponseObject result = null;
        int intCount = 0;
        try {
            try {
                result = WOConfirmation.getConfirmations(ZAppSettings.FetchLevel.Count, getWorkOrderNum(), getOperationNum(), getConfNo());
                if (!result.isError()) {
                    intCount = (int) result.Content();
                }
            } catch (Exception e) {
                DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            }
            result = WOConfirmation.getConfirmations(ZAppSettings.FetchLevel.Single, getWorkOrderNum(), getOperationNum(), String.format("%08d", Integer.valueOf(intCount)));
            if (!result.isError()) {
                confirmations = (ArrayList<WOConfirmation>) result.Content();
                if (confirmations != null && confirmations.size() > 0) {
                    confirmation = confirmations.get(0);
                    if (confirmation != null) {
                        confirmation.setComplete("");
                        confirmation.setFinConf("");
                        confirmation.setConfText(ZCollections.OPERATION_INCOMPLETE_TEXT);
                        confirmation.setMode(ZAppSettings.EntityMode.Update);
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return confirmation;
    }

    public int getOprStatusDrawable() {
        try {
            if (getStatusDetail() != null && getStatusDetail().woOprStatus != null) {
                return getStatusDetail().woOprStatus.getDrawableResId();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return R.drawable.download;
    }

    public boolean isStatusAllowed(StatusCategory allowedStatus) {
        boolean result = false;
        try {
            for (StatusCategory status : validStatuses) {
                if (status.getStatusCode().equalsIgnoreCase(allowedStatus.getStatusCode())) {
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public ResponseObject UpdateStatus(StatusCategory status, String Notes, String StatusReason, boolean autoFlush, long oprStartTime, long oprFinishTime, BigDecimal actualWork, Location deviceLocation) {
        ResponseObject result = null;
        try {
            result = UpdateStatus(status, Notes, StatusReason, false, deviceLocation);
            if (result != null && !result.isError()) {
                if ((status.postConfirmationEnabled()) || ZConfigManager.POST_CONFIRMATIONS) {
                    result.setError(!PostConfirmation(status, oprStartTime, oprFinishTime, actualWork, autoFlush, status.preCompletionCheckEnabled()));
                    /*if (result != null && !result.isError()) {
                        int hours = actualWork.intValue();
                        int minutes = (int) ((actualWork.floatValue() - hours) * 100);

                        GregorianCalendar startDate = (GregorianCalendar) Calendar.getInstance();
                        startDate.setTimeInMillis(oprStartTime);
                        Time startTime = new Time(oprStartTime);
                        GregorianCalendar finishDate = (GregorianCalendar) Calendar.getInstance();
                        finishDate.setTimeInMillis(oprFinishTime);
                        Time finishTime = new Time(oprFinishTime);

                        GregorianCalendar actFinishTime = (GregorianCalendar) Calendar.getInstance();
                        actFinishTime.setTimeInMillis(oprStartTime);
                        actFinishTime.add(Calendar.HOUR_OF_DAY, hours);
                        actFinishTime.add(Calendar.MINUTE, minutes);
                        BigDecimal actDuration = new BigDecimal(ZCommon.getTimeDurationInHoursString(oprStartTime, actFinishTime.getTimeInMillis()));

                        WOConfirmation confirmation = createCompletionConfirmation();
                        confirmation.setActualDur(actDuration);
                        confirmation.setExecStartDate(startDate);
                        confirmation.setExecStartTime(startTime);
                        confirmation.setExecFinDate(finishDate);
                        confirmation.setExecFinTime(finishTime);
                        result = confirmation.SaveToStore(autoFlush);
                    }*/
                }
                /*else if (status == ZAppSettings.MobileStatus.ARRIVED) {
                    result.setError(PostConfirmation(ZConfigManager.ACTIVITY_TYPE_TRAVEL_TIME, oprStartTime, oprFinishTime, actualWork, autoFlush));
                }*/
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public boolean PostConfirmation(StatusCategory status, long actStartTime, long actFinishTime, BigDecimal actualDur, boolean autoFlush, boolean finalConfirmation) {
        boolean result = false;
        try {
            WOConfirmation confirmation = createCompletionConfirmation(true);
            confirmation.setUnActDur("MIN");
            //actDuration = actDuration.multiply(new BigDecimal(60)).abs();

            GregorianCalendar startDate = (GregorianCalendar) Calendar.getInstance();
            startDate.setTimeInMillis(actStartTime);
            Time startTime = new Time(actStartTime);
            GregorianCalendar finishDate = (GregorianCalendar) Calendar.getInstance();
            finishDate.setTimeInMillis(actFinishTime);
            Time finishTime = new Time(actFinishTime);

            BigDecimal actDuration = new BigDecimal(ZCommon.getTimeDurationInHoursString(actStartTime, actFinishTime));
            if (actualDur != null) {
                int hours = actualDur.intValue();
                int minutes = (int) ((actualDur.floatValue() - hours) * 100);

                GregorianCalendar actFinishDateTime = (GregorianCalendar) Calendar.getInstance();
                actFinishDateTime.setTimeInMillis(actStartTime);
                actFinishDateTime.add(Calendar.HOUR_OF_DAY, hours);
                actFinishDateTime.add(Calendar.MINUTE, minutes);
                actDuration = new BigDecimal(ZCommon.getTimeDurationInHoursString(actStartTime, actFinishDateTime.getTimeInMillis()));
            }
            actDuration = actDuration.multiply(new BigDecimal(60)).setScale(0, RoundingMode.HALF_DOWN);
            confirmation.setActualDur(actDuration);
            //confirmation.setUnWork("MIN");
            //confirmation.setActWork(actDuration);
            startDate.set(Calendar.HOUR_OF_DAY, 0);
            startDate.set(Calendar.MINUTE, 0);
            startDate.set(Calendar.SECOND, 0);
            startDate.set(Calendar.MILLISECOND, 0);
            confirmation.setExecStartDate(startDate);
            confirmation.setExecStartTime(startTime);
            finishDate.set(Calendar.HOUR_OF_DAY, 0);
            finishDate.set(Calendar.MINUTE, 0);
            finishDate.set(Calendar.SECOND, 0);
            finishDate.set(Calendar.MILLISECOND, 0);
            confirmation.setExecFinDate(finishDate);
            confirmation.setExecFinTime(finishTime);
            confirmation.setActType(getActivityType() == null || getActivityType().isEmpty() ? status.getActivityType() : getActivityType());
            confirmation.setPersNo(UserTable.getUserPersonnelNumber());
            if (!finalConfirmation) {
                confirmation.setFinConf("");
                //confirmation.setComplete("");
                confirmation.setConfText(status.getActivityTypeDesc());
                //-----------------------------------------------------
                confirmation.setComplete("");
                confirmation.setClearRes(false);
                //-----------------------------------------------------
            } else {
                //-----------------------------------------------------
                confirmation.setFinConf(ZConfigManager.STATUS_SET_FLAG);
                confirmation.setComplete(ZConfigManager.STATUS_SET_FLAG);
                confirmation.setClearRes(true);
                //-----------------------------------------------------
            }


            /*if(!finalConfirmation){
                confirmation.setFinConf("");
                confirmation.setComplete("");
                if(activityType.equalsIgnoreCase(ZConfigManager.ACTIVITY_TYPE_TRAVEL_TIME))
                    confirmation.setConfText("Travel time confirmation.");
                else
                    confirmation.setConfText("Work time confirmation.");
            }*/


            ResponseObject response = confirmation.SaveToStore(autoFlush);
            result = response != null && !response.isError();
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public ResponseObject UpdateStatus(StatusCategory status, String Notes, String StatusReason, boolean autoFlush, Location deviceLocation) {
        ResponseObject result = null;
        DataHelper dbHelper = null;
        String deviceTime = " ";
        String strNotesText = "";
        String strReasonText = "";
        String strStatusText = "";
        try {
//Set Status and Flag
            if (getTransferFlag() == null || getTransferFlag().isEmpty())
                setStatusFlag(ZConfigManager.STATUS_SET_FLAG);

            setMobileStatus(status.getStatusCode());
            setUserStatus(status.getStatusCode());
            setMobileObjectType(status.getObjectType());
            String statusDesc = status.getStatusDescKey();
            if (StatusReason != null && !StatusReason.isEmpty()) {
                strReasonText = ZConfigManager.AUTO_NOTES_TEXT_LINE4 + " " + StatusReason;
            }
            if (!ZConfigManager.AUTO_NOTES_ON_STATUS) {
                strNotesText = (strReasonText.isEmpty() ? "" : (strReasonText + "\n")) + (Notes != null ? Notes : "");
            } else {
                Date timeStamp = ZCommon.getDeviceTime();
                if (timeStamp != null) {
                    deviceTime = timeStamp.toString();
                }
                strStatusText = "Operation " + getOperationNum() + ": " + ZConfigManager.AUTO_NOTES_TEXT_LINE1 + " " + statusDesc + " " +
                        ZConfigManager.AUTO_NOTES_TEXT_LINE2 + " " + ZAppSettings.strUser.toUpperCase() + " " +
                        (status.woOprStatus == ZAppSettings.MobileStatus.TRNS ? (ZConfigManager.AUTO_NOTES_TEXT_LINE5 + " " + getTransferPerson() + " ") : "") +
                        ZConfigManager.AUTO_NOTES_TEXT_LINE3 + " " + deviceTime;
                if (ZConfigManager.ENABLE_POST_DEVICE_LOCATION_NOTES && deviceLocation != null) {
                    strStatusText = strStatusText + " at location Lat: " + deviceLocation.getLatitude() + "; Long: " + deviceLocation.getLongitude();
                }
                strNotesText = strStatusText + (strReasonText.isEmpty() ? "" : ("\n" + strReasonText + "\n")) + (Notes != null ? Notes : "");
            }
            //setNotes(strNotesText);
            //Update the Operation to offlinestore
            setMode(ZAppSettings.EntityMode.Update);
            if (isOnline) {
                result = SaveToOnlineStore();
            } else
                result = SaveToStore(strNotesText.isEmpty());
            if (!strNotesText.isEmpty() && result != null && !result.isError()) {
                WOLongText longText = new WOLongText();
                int count = WOLongText.getLongTextsCount(getWorkOrderNum(), getOperationNum(), getCounter(), ZConfigManager.Fetch_Object_Type.Operation);
                result = longText.SendOperationLongText(strNotesText, this, count, getTempID(), !ZConfigManager.ENABLE_POST_DEVICE_LOCATION_NOTES);
                if (ZConfigManager.SHOW_OPERATION_NOTES_IN_WO) {
                    int countwo = WOLongText.getLongTextsCount(getWorkOrderNum(), null, getCounter(), ZConfigManager.Fetch_Object_Type.WorkOrder);
                    result = longText.SendLongText(strNotesText, getWorkOrderNum(), null, ZConfigManager.Fetch_Object_Type.WorkOrder, countwo, getTempID(), autoFlush);
                }
            }
            getAllowedStatus(status);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void updateOnlineOprResult(ResponseObject responseObject) {

    }

    // Getting all allowed statuses with respect to the Current Status
    private ResponseObject getAllowedStatus(StatusCategory CurrentStatus) {
        ResponseObject result = null;
        String strResPath;
        ArrayList<String> allowedStatusList;
        try {
            if (CurrentStatus != null) {
                result = WorkOrderStatus.getWorkOrderAllowedStatus(CurrentStatus, getOrderType());
                if (!result.isError()) {
                    allowedStatusList = (ArrayList<String>) result.Content();
                    //Check if any other Operation is active, if Yes, remove the Disallowed status from then allowed status list
                    if (!ZConfigManager.ASSIGNMENT_TYPE.equals(ZAppSettings.AssignmentType.WorkCenterOperationLevel) && getNumberOfActiveOperations(null) > 0) {
                        allowedStatusList.remove(ZConfigManager.WO_ACTIVE_STATUS_DISABLED.getMobileStatusCode());
                    }
                    for (String allowedStatus : allowedStatusList) {
                        StatusCategory status = StatusCategory.getStatusDetails(allowedStatus, getOrderType(), ZConfigManager.Fetch_Object_Type.Operation);
                        if (status != null)
                            addValidStatus(status);
                    }
                } else {
                    DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, "error in getting allowed status for WO: " + getWorkOrderNum() + ". Message: " + result.getMessage());
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, "error in getting allowed status for WO: " + getWorkOrderNum() + ". Message: " + e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public ResponseObject UpdateTransferStatus(StatusCategory status, String Notes, String StatusReason, String Priority, String LabourCode, Location location, String Plant, String WorkCenter) {
        ResponseObject result = null;
        try {
            setPriority(Priority);
            if (ZConfigManager.ASSIGNMENT_TYPE == ZAppSettings.AssignmentType.WorkCenterWorkOrderLevel || ZConfigManager.ASSIGNMENT_TYPE == ZAppSettings.AssignmentType.WorkCenterOperationLevel) {
                setPlant(Plant);
                setToWorkCenter(WorkCenter);
            } else
                setTransferPerson(LabourCode);
            setTransferFlag(ZConfigManager.STATUS_SET_FLAG);
            setStatusFlag("");

            //setTransferReason(StatusReason);
            result = UpdateStatus(status, Notes, StatusReason, true, location);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getDisplayableWorkOrderNum() {
        return this.getWorkOrderNum().replace(ZConfigManager.WONUM_VALUE_PREFIX, ZConfigManager.LOCAL_IDENTIFIER);
    }

    public ResponseObject getCompletionPreCheckList(Context context) {
        ResponseObject result;
        ArrayList<String> errorMessages = new ArrayList<>();
        try {
            ArrayList<String> notificationMsgs = new ArrayList<>();
            ArrayList<OrderTypeFeature> featureList = OrderTypeFeature.getMandatoryFeaturesByObjectType(WorkOrder.getCurrWo().getCurrentOperation().getOrderType());
            for (OrderTypeFeature orderTypeFeature : featureList) {
                //Components
                if (ZConfigManager.COMPONENT_ISSUE_REQUIRED && orderTypeFeature.getFeature().equalsIgnoreCase(ZAppSettings.Features.COMPONENT.getFeatureValue())) {
                    int remainingComponents = getTotalNumUnIssuedComponents() + (ZConfigManager.PARTIAL_COMPONENT_ISSUE_ALLOWED ? 0 : getTotalNumPartialIssuedComponents());
                    int totalComponents = getTotalComponents();
                    if (orderTypeFeature.getMandatoryLevel().equalsIgnoreCase(OrderTypeFeature.LEVEL_ALL)) {
                        if (remainingComponents > 0)
                            errorMessages.add(context.getString(R.string.msgTotalComponentsRequiredToIssued, remainingComponents));
                    } else if (orderTypeFeature.getMandatoryLevel().equalsIgnoreCase(OrderTypeFeature.LEVEL_PARTIAL)) {
                        if (totalComponents!=0&&remainingComponents!=0&&totalComponents == remainingComponents)
                            errorMessages.add(context.getString(R.string.msgAtLeastOneComponentRequiredToIssued, (ZConfigManager.PARTIAL_COMPONENT_ISSUE_ALLOWED ? "Partially" : "Completely")));
                    } else {
                        if (remainingComponents > 0)
                            errorMessages.add(context.getString(R.string.msgTotalComponentsRequiredToIssued, remainingComponents));
                    }
                }
                //Attachments
                if (ZConfigManager.ATTACHMENT_REQUIRED && orderTypeFeature.getFeature().equalsIgnoreCase(ZAppSettings.Features.ATTACHMENT.getFeatureValue())) {
                    if (WorkOrder.getCurrWo().getTotalNumUserUploadedAttachments() <= 0)
                        errorMessages.add(context.getString(R.string.msgAtLeastOneAttachmentRequired));
                }
                //Forms
                if (ZConfigManager.MANDATORY_FORMS_REQUIRED && orderTypeFeature.getFeature().equalsIgnoreCase(ZAppSettings.Features.OPERATIONFORM.getFeatureValue())) {
                    try {
                        String formType=ZAppSettings.FormAssignmentType.getFormAssignmentType(ZConfigManager.FORM_ASSIGNMENT_TYPE);
                        if(ZCommon.isPredefinedFormVisible(formType)&&getTotalNumUnSubmittedMandatoryForms() > 0)
                                errorMessages.add(context.getString(R.string.msgAllMandatoryPredefinedFormsAreRequired));
                        if(ZCommon.isManualAssignedFormsVisible(formType)&&getTotalNumUnSubmittedManualMandatoryForms() > 0)
                                errorMessages.add(context.getString(R.string.msgAllMandatoryManualFormsAreRequired));
                    } catch (Exception e) {
                        DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
                    }
                }
                //Record Points
                if (ZConfigManager.MPOINT_READING_REQUIRED && orderTypeFeature.getFeature().equalsIgnoreCase(ZAppSettings.Features.RECORDPOINTS.getFeatureValue())) {
                    int totalPoints = WorkOrder.getCurrWo().getTotalNumMeasurementPoints();
                    int totalReadingTaken = WorkOrder.getCurrWo().getTotalNumReadingTaken();
                    if (orderTypeFeature.getMandatoryLevel().equalsIgnoreCase(OrderTypeFeature.LEVEL_ALL)) {
                        if (totalPoints > 0 && totalPoints != totalReadingTaken)
                            errorMessages.add(context.getString(R.string.msgAllReadingPointsAreMandatory));
                    } else if (orderTypeFeature.getMandatoryLevel().equalsIgnoreCase(OrderTypeFeature.LEVEL_PARTIAL)) {
                        if (totalPoints > 0 && totalReadingTaken <= 0)
                            errorMessages.add(context.getString(R.string.msgAtLeastOneReadingPointRequired));
                    } else {
                        if (totalPoints > 0 && totalPoints != totalReadingTaken)
                            errorMessages.add(context.getString(R.string.msgAllReadingPointsAreMandatory));
                    }
                }
                //Inspection Lot
                if (orderTypeFeature.getFeature().equalsIgnoreCase(ZAppSettings.Features.INSPECTIONLOT.getFeatureValue())) {
                    if (getSystemStatus().toLowerCase().contains(ZConfigManager.OPR_INSP_ENABLE_STATUS.toLowerCase())
                            && !getSystemStatus().toLowerCase().contains(ZConfigManager.OPR_INSP_RESULT_RECORDED_STATUS.toLowerCase())) {
                        errorMessages.add(context.getString(R.string.msgInspectionResultPending));
                    }
                }

                //Notifications
                if (orderTypeFeature.getFeature().contains(ZAppSettings.Features.NOTIFICATION.getFeatureValue())) {
                    Notification woNotification = null;
                    ResponseObject response = Notification.getNotifications(ZAppSettings.FetchLevel.Single, ZAppSettings.Hierarchy.HeaderOnly, WorkOrder.getCurrWo().getNotificationNum(), "", true);
                    if (response != null && !response.isError()) {
                        woNotification = ((ArrayList<Notification>) response.Content()).get(0);
                        if (woNotification != null) {
                            notificationMsgs = woNotification.getPreCompletionMessages(true);
                        }
                    }
                }

            }

            if(notificationMsgs.size() > 0)
                errorMessages.addAll(notificationMsgs);

            if (errorMessages.size() > 0) {
                result = new ResponseObject(ZConfigManager.Status.Error, "Error", errorMessages);
            } else {
                result = new ResponseObject(ZConfigManager.Status.Success, "Success", null);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, "Error", e.getMessage());
        }
        return result;
    }
    /* get the count of the un-submitted Predefined Manadatory forms based on Form Assignment type
     * */
    public int getTotalNumUnSubmittedMandatoryForms() {
        int unSubmittedFinalFormsSubmissionCount = 0;
        ResponseObject responseObject = null;
        String strResPath = "";
        Object rawData = null;
        try {
            responseObject=WorkOrder.getCurrWo().getFormEntities(true);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                ArrayList<FormAssignmentSetModel> forms = (ArrayList<FormAssignmentSetModel>) rawData;
                if (forms != null && forms.size() > 0) {
                    for (FormAssignmentSetModel form : forms) {
                        strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + getWorkOrderNum() + "' and OperationNum eq '" + WorkOrder.getCurrWo().getCurrentOperation().getOperationNum() + "')";
                        responseObject = DataHelper.getInstance().getEntities(ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION, strResPath);
                        if (!responseObject.isError()) {
                            rawData = responseObject.Content();
                            if (Integer.parseInt(rawData.toString()) > 0) {
                                strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + getWorkOrderNum() + "' and OperationNum eq '" + WorkOrder.getCurrWo().getCurrentOperation().getOperationNum() + "'and IsDraft eq 'X')";
                                responseObject = DataHelper.getInstance().getEntities(ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION, strResPath);
                                if (!responseObject.isError()) {
                                    rawData = responseObject.Content();
                                    if (Integer.parseInt(rawData.toString()) > 0) {
                                        unSubmittedFinalFormsSubmissionCount++;
                                    }
                                }
                            }
                            else{
                                unSubmittedFinalFormsSubmissionCount++;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return unSubmittedFinalFormsSubmissionCount;
    }
    /* get the count of the un-submitted Manula Manadatory forms based on Form Assignment type
     * */
    public int getTotalNumUnSubmittedManualMandatoryForms() {
        int unSubmittedFinalFormsSubmissionCount = 0;
        ResponseObject responseObject = null;
        String strResPath = "";
        Object rawData = null;
        try {
            responseObject=WorkOrder.getCurrWo().getManualFormEntities(true);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                ArrayList<ManualFormAssignmentSetModel> forms = (ArrayList<ManualFormAssignmentSetModel>) rawData;
                if (forms != null && forms.size() > 0) {
                    for (ManualFormAssignmentSetModel form : forms) {
                        strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + getWorkOrderNum() + "' and OperationNum eq '" + WorkOrder.getCurrWo().getCurrentOperation().getOperationNum() + "')";
                        responseObject = DataHelper.getInstance().getEntities(ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION, strResPath);
                        if (!responseObject.isError()) {
                            rawData = responseObject.Content();
                            if (Integer.parseInt(rawData.toString()) > 0) {
                                strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + getWorkOrderNum() + "' and OperationNum eq '" + WorkOrder.getCurrWo().getCurrentOperation().getOperationNum() + "'and IsDraft eq 'X')";
                                responseObject = DataHelper.getInstance().getEntities(ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION, strResPath);
                                if (!responseObject.isError()) {
                                    rawData = responseObject.Content();
                                    if (Integer.parseInt(rawData.toString()) > 0) {
                                        unSubmittedFinalFormsSubmissionCount++;
                                    }
                                }
                            }
                            else{
                                unSubmittedFinalFormsSubmissionCount++;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return unSubmittedFinalFormsSubmissionCount;
    }
    public int getTotalNumUnIssuedComponents() {
        int intComponentsCount = 0;
        ResponseObject responseObject = null;
        String strResPath;
        Object rawData = null;
        try {
            strResPath = ZCollections.COMPONENT_COLLECTION + "/$count?$filter= (WorkOrderNum eq '" + getWorkOrderNum() + "' and OperAct eq '" + getOperationNum() + "' and WithdrawalQty eq 0 and Deleted ne true)";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.COMPONENT_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intComponentsCount = Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intComponentsCount;
    }

    public int getTotalComponents() {
        int numTotalComponents = 0;
        try {
            String entitySetName = ZCollections.COMPONENT_COLLECTION;
            String resPath = entitySetName + "/$count?$filter=(WorkOrderNum eq '" + getWorkOrderNum() + "' and OperAct eq '" + getOperationNum() + "' and Deleted ne true)";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                Object rawData = result.Content();
                numTotalComponents = Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return numTotalComponents;
    }

    public int getTotalNumPartialIssuedComponents() {
        int intComponentsCount = 0;
        ResponseObject responseObject = null;
        String strResPath;
        Object rawData = null;
        try {
            strResPath = ZCollections.COMPONENT_COLLECTION + "/$count?$filter= (WorkOrderNum eq '" + getWorkOrderNum() + "' and OperAct eq '" + getOperationNum() + "' and WithdrawalQty gt 0 and WithdrawalQty lt ReqmtQty and Deleted ne true)";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.COMPONENT_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intComponentsCount = Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intComponentsCount;
    }

    /**
     * Fetch the all operations from WOOperationCollection based on the flag
     *
     * @param fetchUnAssinged
     * @return operations
     */
    public static ArrayList<Operation> getAllOperations(boolean fetchUnAssinged) {
        ArrayList<Operation> operationsList = new ArrayList<>();
        ResponseObject result = null;
        String resPath = "";
        String strEntitySet = ZCollections.OPR_COLLECTION;
        String strOrderByURI = "&$orderby=OperationNum,SubOperation";
        try {
            if (fetchUnAssinged)
                resPath = strEntitySet + "?$filter=( EnteredBy eq '' and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true)&$select=OperationNum,WorkOrderNum,PlannofOpera,Counter,ControlKey,ShortText,MobileStatus,EarlSchStartExecDate,EarlSchStartExecTime,EarlSchFinishExecDate,EarlSchFinishExecTime,SystemStatus,UserStatus,ActivityType,SubOperation,ConfNo,ActivityType,Plant,WorkCenter,PersonnelNo,OpObjectNum,Equipment,FuncLoc,OrderType,TaskListType,Group,GroupCounter,InternalCounter,ActualWork,Work,EnteredBy,PlanningPlant" + strOrderByURI;
            else
                resPath = strEntitySet + "?$filter=( EnteredBy ne '' and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true)&$select=OperationNum,WorkOrderNum,PlannofOpera,Counter,ControlKey,ShortText,MobileStatus,EarlSchStartExecDate,EarlSchStartExecTime,EarlSchFinishExecDate,EarlSchFinishExecTime,SystemStatus,UserStatus,ActivityType,SubOperation,ConfNo,ActivityType,Plant,WorkCenter,PersonnelNo,OpObjectNum,Equipment,FuncLoc,OrderType,TaskListType,Group,GroupCounter,InternalCounter,ActualWork,Work,EnteredBy,PlanningPlant" + strOrderByURI;
            //resPath = strEntitySet + "?$filter=startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true&$orderby=";
            ResponseObject response = DataHelper.getInstance().getEntities(strEntitySet, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities != null && entities.size() > 0) {
                    response = FromEntity(entities, ZAppSettings.FetchLevel.List);
                    operationsList = (ArrayList<Operation>) response.Content();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(Operation.class, AppSettings.LogLevel.Error, e.getMessage());
            Log.e(Operation.class.getName(), "getAllOperations: ", e);
        }
        return operationsList;
    }

    public String getTruncatedOprNum(String trucOprNum) {
        String truncatedOprNumStr = trucOprNum;
        try {
            int truncatedOprNum = Integer.parseInt(trucOprNum);
            truncatedOprNumStr = truncatedOprNum + "";
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return truncatedOprNumStr;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public StatusCategory getStatusDetail() {
        if (statusDetail == null)
            statusDetail = new StatusCategory();
        return statusDetail;
    }

    /**
     * @return ArrayList of all distinct Equipment among all Operations
     */
    public static ArrayList<String> getAllDistinctEquipment() {
        ArrayList<String> types = new ArrayList<>();
        try {
            String resPath = ZCollections.OPR_COLLECTION + "?$filter=Equipment ne ''&$select=Equipment";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.OPR_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = BaseEntity.setODataEntityList(response.Content());
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        types.add(String.valueOf(entity.getProperties().get("Equipment").getValue()));
                    }
                    Set<String> strings = new HashSet<String>(types);
                    types.clear();
                    types.addAll(strings);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return types;
    }

    /**
     * @return ArrayList of SpinnerItem of all distinct Equipment with Technical Identification Number among all operations
     */
    public static ArrayList<SpinnerItem> getSpinnerEquipmentTechIDs() {
        ArrayList<SpinnerItem> spinnerEqps = new ArrayList<>();
        ArrayList<String> equipmentList = getAllDistinctEquipment();
        for (String equipment : equipmentList) {
            String resPath = ZCollections.EQUIPMENT_COLLECTION + "('" + equipment + "')?$select=TechIdentNo";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.EQUIPMENT_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                ODataEntity entity = (ODataEntity) response.Content();
                spinnerEqps.add(new SpinnerItem(equipment, String.valueOf(entity.getProperties().get("TechIdentNo").getValue())));
            }
        }
        return spinnerEqps;
    }

    protected void deriveOperationStatus() {
        ZAppSettings.MobileStatus mobileStatus = null;
        String status = null;
        //UserStatus = ZAppSettings.MobileStatus.Created.getMobileStatusCode();//todo remove this after testing
        try {
            if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                status = SystemStatus;
                switch (status.startsWith("CNF") ? "CNF" : status.equals("REL") ? "REL" : status.startsWith("DLT") ? "DLT" : status.equals("INCP") ? "INCP" : status.startsWith("PCNF") ? "PCNF" : "") {
                    case "REL":
                        mobileStatus = ZAppSettings.MobileStatus.Released;
                        break;
                    case "CNF":
                        mobileStatus = ZAppSettings.MobileStatus.CONFIRMED;
                        break;
                    case "PCNF":
                        mobileStatus = ZAppSettings.MobileStatus.PARTIALCONFIRMED;
                        break;
                    case "DLT":
                        mobileStatus = ZAppSettings.MobileStatus.Deleted;
                        break;
                    case "INCP":
                        mobileStatus = ZAppSettings.MobileStatus.InComplete;
                        break;
                    default:
                        mobileStatus = ZAppSettings.MobileStatus.NotSet;
                        break;
                }
                status = mobileStatus.getMobileStatusCode();
                StatusCategory statusDetail = StatusCategory.getStatusDetails(status, getOrderType(), ZConfigManager.Fetch_Object_Type.Operation);
                if (statusDetail != null) {
                    this.statusDetail = statusDetail;
                }
                if (getStatusDetail().woOprStatus.equals(ZAppSettings.MobileStatus.NotSet))
                    getStatusDetail().woOprStatus = mobileStatus;
            } else {
                status = MobileStatus != null ? MobileStatus : "";
                if (!status.isEmpty() && !isOnline && ZConfigManager.DEFAULT_STATUS_TO_CHANGE.contains(status)) {
                    StatusCategory receivedStatus = StatusCategory.getStatusDetails(ZConfigManager.DEFAULT_STATUS_TO_SEND, getOrderType(), ZConfigManager.Fetch_Object_Type.Operation);
                    if (receivedStatus != null) {
                        this.statusDetail = receivedStatus;
                        //Update the Operation to offlinestore with MOBI status
                        UpdateStatus(receivedStatus, null, null, false, null);
                        return;
                    }
                }
                if (!status.isEmpty()) {
                    StatusCategory statusDetail = StatusCategory.getStatusDetails(status, getOrderType(), ZConfigManager.Fetch_Object_Type.Operation);
                    if (statusDetail != null) {
                        this.statusDetail = statusDetail;
                        getAllowedStatus(statusDetail);
                    }
                }
            }
            /*if (mobileStatus == null && status != null) {
                for (ZAppSettings.MobileStatus mobilestatus : ZAppSettings.MobileStatus.values()) {
                    if (mobilestatus.getMobileStatusCode().equalsIgnoreCase(status)) {
                        mobileStatus = mobilestatus;
                        break;
                    }
                }
            }
            if (mobileStatus == null) {
                mobileStatus = ZAppSettings.MobileStatus.NotSet;
            }*/
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            //mobileStatus = ZAppSettings.MobileStatus.NotSet;
        }
        //setMobileStatus(mobileStatus.getMobileStatusCode());
        //return mobileStatus;
    }
}