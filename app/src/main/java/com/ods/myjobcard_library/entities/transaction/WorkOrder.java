package com.ods.myjobcard_library.entities.transaction;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ods.myjobcard_library.R;
import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.Address;
import com.ods.myjobcard_library.entities.Entity.WOEntity;
import com.ods.myjobcard_library.entities.MeasurementPointReading;
import com.ods.myjobcard_library.entities.PartnerAddress;
import com.ods.myjobcard_library.entities.ResponseMasterModel;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.appsettings.StatusCategory;
import com.ods.myjobcard_library.entities.ctentities.Equipment;
import com.ods.myjobcard_library.entities.ctentities.FunctionalLocation;
import com.ods.myjobcard_library.entities.ctentities.OrderTypeFeature;
import com.ods.myjobcard_library.entities.ctentities.SpinnerItem;
import com.ods.myjobcard_library.entities.ctentities.WorkOrderStatus;
import com.ods.myjobcard_library.entities.forms.FormAssignmentSetModel;
import com.ods.myjobcard_library.entities.forms.FormListObject;
import com.ods.myjobcard_library.entities.forms.FormResponseApprovalStatus;
import com.ods.myjobcard_library.entities.forms.ManualFormAssignmentSetModel;
import com.ods.myjobcard_library.viewmodels.ManualFormAssignmentHelper;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.ConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.client.odata.v4.EntityValue;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.impl.ODataPropertyDefaultImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkOrder extends ZBaseEntity {

    private static WorkOrder CURR_WO;
    public boolean isBadgeLayoutVisible;
    public boolean isOnline;
    public String BOM;
    DataHelper dbHelper = null;
    private Operation CurrentOperation;
    //WO child elements
    private ArrayList<PartnerAddress> partnerAddresses;
    private DataHelper dataHelper = null;
    private Equipment equipment = null;
    private FunctionalLocation functionalLocation = null;
    private ArrayList<StatusCategory> validStatuses = null;
    private boolean isAttachmentAvailable;
    //private ZAppSettings.Features features;
    private StatusCategory statusDetail;
    private ArrayList<Operation> workOrderOperations;
    private PartnerAddress defaultPartnerAddress;
    private com.ods.myjobcard_library.entities.Address defaultAddress;
    //WO header details
    private String WorkOrderNum;                      //AUFNR
    //private String Orderid;                     //AUFNR
    private String OrderType;                   //AUART
    private String BusArea;                     //GSBER
    private String MainWorkCtr;                 //VAPLZ
    private String PlantMainWorkCtr;            //WAWRK
    private String Plant;                       //WERKS
    private String ShortText;                   //KTEXT
    private String Category;                    //AUTYP
    private String Status;                      //ASTNR
    private String Priority;                    //PRIOK
    private String NotificationNum;             //QMNUM
    private String EquipNum;                    //EQUNR
    private String FuncLocation;                //TPLNR
    private String TechObjLocAndAssgnmnt;       //ILOAN
    private String MobileObjStatus;             //MOBILE_STATUS
    private String MobileObjectType;            //MOBILE_STATUS_ORDERTYPE
    //wo related contact persons and dates
    private GregorianCalendar CreatedOn;        //ERDAT
    private String EnteredBy;                   //ERNAM
    private String LastChangedBy;               //AENAM
    private String ResponsiblPlannerGrp;        //PLGRP
    private String PersonResponsible;           //USER2
    private String PersonInchargeTel;           //USER3
    private String PersonResponsibleTechInsp;   //INSPK
    private String AssignedTo;
    private String InChargeContact;
    //Customer Address
    private String Name;
    private String Address;
    private String PhoneNumber;
    private String PostalCode;
    private BigDecimal Latitude;
    private BigDecimal Longitude;
    private String GeoLocation;
    //important dates
    private GregorianCalendar ChangeDtForOrderMaster;        //AEDAT
    private GregorianCalendar StrtDate;                      //SDATE
    private GregorianCalendar BasicFnshDate;                 //GLTRP
    private Time BasicFnshTime;                 //GLUZP
    private GregorianCalendar BasicStrtDate;                 //GSTRP
    private Time BasicStrtTime;                 //GSUZP
    private GregorianCalendar SchdFnshDate;                  //GLTRS
    private GregorianCalendar SchdStrtDate;                  //GSTRS
    private GregorianCalendar ActlFnshDate;                  //GLTRI
    private GregorianCalendar ActlStrtDate;                  //GSTRI
    private GregorianCalendar CnfOrderFinishDate;            //GETRI
    private GregorianCalendar TechCompletionDate;            //IDAT2
    //extra wo details
    private String ControllingArea;             //KOKRS
    private String ResponsibleCostCenter;       //KOSTV
    private String MaintActivityType;           //ILART
    private String MaintPlanningPlant;          //IWERK
    private String MaintPlant;                  //SWERK
    private String SysStatusCode;               //SYSTEM_STATUS_CODE
    private String SysStatus;                   //SYSTEM_STATUS
    private String WBSElem;                     //PSPEL
    private String SysCondition;                //ANLZU
    private String WorkPermitIssued;            //User9
    private String CustomerNum;                 //KUNUM
    private String AddressNumber;
    private String WOAddressNumber;
    private String TempID;
    //long description
    private String longDescription;             //LTEXT
    private String TechObjDescription;          //EQKTX
    private boolean DeletionFlag;               //LOEKZ
    private String ObjectNumber;                //OBJNR
    private String CompanyCode;                 //BUKRS;
    //Status Relevant Fields
    private String StatusFlag;
    private String Notes;
    private String TransferFlag;
    private String TransferReason;
    private String TransferPerson;
    //New Fields
    private String UserStatusCode;
    private String UserStatus;
    private String BusAreaText;
    private String SysContitionText;
    private String MaintActivityTypeText;
    private String CategoryText;
    private String CreateNotifFlag;
    private String EquipCategory;
    private String FuncLocCategory;
    private boolean ErrorEntity;
    private String ErrorMsg;
    private String FollowUpOrder;
    private String FolllowOnFlag;
    private String InspectionLot;
    private String BOMCategory;
    private String ToMainWorkCtr;
    //newly added fileds
    private Double BreakdownDur;
    private GregorianCalendar Effective_TS;
    private String ToPlantMainWorkCtr;
    private String PlannerGroupDes;
    private String Unassigned;
    private String Breakdown;
    //Setters and Getters Method
    private String OnlineSearch;
    private String SuperiorOrder;

    private String TechID;

    public static ResponseObject getWorkOrders(ZAppSettings.FetchLevel woFetchLevel, String OrderNum, String OrderByCriteria) {

        ResponseObject result = null;
        ResponseObject resultAddress = null;
        String resourcePath = null;
        boolean fetchAddress = false;
        ArrayList<PartnerAddress> partnerAddresses = null;
        String strOrderBy = "&$orderby=";
        String strOrderByURI = null;
        String strEntitySet = null;
        String filterStart = "$filter=(";
        String filterQuery = null;
        String filterClose = ")";
        String filterQueryURI = null;
        try {
            if (OrderByCriteria == null || OrderByCriteria.isEmpty())
                OrderByCriteria = "BasicFnshDate, Priority, WorkOrderNum";
            strOrderByURI = strOrderBy + OrderByCriteria;
            strEntitySet = ZCollections.WO_COLLECTION;
            /*filterQuery = "MobileObjStatus ne '" + ZAppSettings.MobileStatus.COMPLETE.getMobileStatusCode() + "' and MobileObjStatus ne '" + ZAppSettings.MobileStatus.SUSPEND.getMobileStatusCode() + "'" +
                    " and MobileObjStatus ne '" + ZAppSettings.MobileStatus.TRANSFER.getMobileStatusCode() + "'";
            filterQueryURI = filterStart + filterQuery + filterClose;*/
            switch (woFetchLevel) {
                case ListMap:
                    resourcePath = strEntitySet + "?$select=WorkOrderNum,OrderType,Status,Priority,ShortText,BasicStrtDate,MobileObjStatus,ObjectNumber,EquipNum,NotificationNum,FuncLocation,AddressNumber,WOAddressNumber,TempID,UserStatus,BasicFnshDate,ErrorEntity,PostalCode,MainWorkCtr,Address,LastChangedBy,PersonResponsible,EnteredBy,MaintPlant,ResponsiblPlannerGrp,SuperiorOrder,MaintPlanningPlant,TechID,MobileObjStatus" + strOrderByURI;
                    fetchAddress = true;
                    break;
                case List:
                    resourcePath = strEntitySet + "?$select=WorkOrderNum,OrderType,Status,Priority,ShortText,BasicStrtDate,MobileObjStatus,TempID,UserStatus,BasicFnshDate,EquipNum,FuncLocation,ErrorEntity,WOAddressNumber,AddressNumber,ObjectNumber,PostalCode,MainWorkCtr,LastChangedBy,EnteredBy,PersonResponsible,EnteredBy,MaintPlant,ResponsiblPlannerGrp,SuperiorOrder,MaintPlanningPlant,TechID,MobileObjStatus" + strOrderByURI;
                    fetchAddress = true;
                    break;
                case ListSpinner:
                    filterQuery = "TempID eq ''";
                    filterQueryURI = filterStart + filterQuery + filterClose;
                    //resourcePath = strEntitySet + "?$select=WorkOrderNum,OrderType&$expand=NAVOPERA";
                    resourcePath = strEntitySet + "?" + filterQueryURI + "&$select=WorkOrderNum,OrderType,EnteredBy";
                    break;
                case Header:
                    resourcePath = strEntitySet;
                    break;
                case Single:
                    if (OrderNum != null && OrderNum.length() > 0) {
                        resourcePath = strEntitySet + "?$filter=(WorkOrderNum eq '" + OrderNum + "')";
                        fetchAddress = !OrderNum.startsWith(ZCollections.TEMP_ID_PREFIX);
                        /*if (OrderNum.startsWith(ZCollections.TEMP_ID_PREFIX)) {
//                            resourcePath = strEntitySet + "?$filter=(TempID%20eq%20%27" + OrderNum + "%27)&$expand=NAVOPERA/OPERATOPRT";
                              resourcePath = strEntitySet + "?$filter=(TempID eq '" + OrderNum + "' or WorkOrderNum eq '"+ OrderNum +"')";
                        } else {
//                            resourcePath = strEntitySet + "?$filter=(WorkOrderNum%20eq%20%27" + OrderNum + "%27)&$expand=NAVOPERA/OPERATOPRT";
                            resourcePath = strEntitySet + "?$filter=(WorkOrderNum%20eq%20%27" + OrderNum + "%27)";
                        }*/
                    }
                    break;
                case All:
//                    resourcePath = strEntitySet + "?$expand=NAVOPERA/OPERATOPRT";
                    resourcePath = strEntitySet;
                    fetchAddress = true;
                    break;
                default:
//                    resourcePath = strEntitySet + "?$expand=NAVOPERA/OPERATOPRT";
                    resourcePath = strEntitySet;
                    fetchAddress = true;
                    break;
            }
            result = DataHelper.getInstance().getEntities(strEntitySet, resourcePath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities, fetchAddress, woFetchLevel);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ResponseObject getFilteredWorkOrders(@NonNull String filterQuery, ZAppSettings.FetchLevel fetchLevel, String OrderByCriteria) {
        ResponseObject result = null;
        String entitySetName = ZCollections.WO_COLLECTION;
        String resPath = entitySetName;
        String orderByUrl = "$orderby=";
        boolean fetchAddress = false;
        try {
            if (OrderByCriteria == null || OrderByCriteria.isEmpty())
                OrderByCriteria = "BasicFnshDate, Priority, WorkOrderNum";
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
                    resPath += "$select=WorkOrderNum,SysStatus,OrderType,Status,Priority,ShortText,BasicStrtDate,MobileObjStatus,TempID,UserStatus,BasicFnshDate,EquipNum,FuncLocation,ErrorEntity,WOAddressNumber,AddressNumber,ObjectNumber,PostalCode,MainWorkCtr,LastChangedBy,PersonResponsible,SuperiorOrder,EnteredBy,MaintPlant,ResponsiblPlannerGrp" + "&" + orderByUrl;
                    break;
                case ListMap:
                    resPath += "$select=WorkOrderNum,SysStatus,OrderType,Status,Priority,ShortText,BasicStrtDate,MobileObjStatus,ObjectNumber,EquipNum,NotificationNum,FuncLocation,AddressNumber,WOAddressNumber,TempID,UserStatus,BasicFnshDate,ErrorEntity,PostalCode,MainWorkCtr,Address,LastChangedBy,PersonResponsible,SuperiorOrder,EnteredBy,MaintPlant,ResponsiblPlannerGrp" + "&" + orderByUrl;
                    fetchAddress = true;
                    break;
            }
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                if (fetchLevel != ZAppSettings.FetchLevel.Count) {
                    List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                    result = FromEntity(entities, fetchAddress, fetchLevel);
                } else
                    return result;
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public WorkOrder(String orderId) {
        super();
        partnerAddresses = new ArrayList<PartnerAddress>();
        initializeEntityProperties();
        this.WorkOrderNum = orderId;
    }

    public WorkOrder(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        create(entity);
        deriveWOStatus();
        initializeEntityProperties();
    }
    /*Added by Anil
     * Customized OData Entity Constructor*/

    /**
     * Constructor  to create or map the new instance with the given ZODataEntity Object.
     *
     * @param entity ZODataEntity Contains the oDataEntity or EntityValue instance map to
     */
    public WorkOrder(ZODataEntity entity) {
        create(entity);
        deriveWOStatus();
        initializeEntityProperties();
    }

    public WorkOrder(ODataEntity entity) {
        create(entity);
        deriveWOStatus();
        initializeEntityProperties();
    }

    public WorkOrder(ODataEntity entity, ZAppSettings.FetchLevel componentsFetchLevel, boolean fetchAddress) {
        try {
            partnerAddresses = new ArrayList<PartnerAddress>();
            initializeEntityProperties();
            create(entity, componentsFetchLevel, fetchAddress);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());

        }

    }

    public WorkOrder(EntityValue entityValue) {
        create(entityValue);
        deriveWOStatus();
        initializeEntityProperties();
        isOnline = true;
    }

    public static WorkOrder getCurrWo() {
        return CURR_WO;
    }

    public static void setCurrWo(WorkOrder workOrder) {
        CURR_WO = workOrder;
    }

    public static WorkOrder getWOByOrderIdFromWorkOrders(List<WorkOrder> orderList, String woNum) {
        for (WorkOrder wo : orderList) {
            if (wo.getWorkOrderNum().equals(woNum))
                return wo;
        }
        return null;
    }

    public static ArrayList<WorkOrder> searchWorkOrders(CharSequence searchedText, ArrayList<WorkOrder> workorders) {
        ArrayList<WorkOrder> searchedWOs = null;
        try {
            searchedWOs = new ArrayList<WorkOrder>();
            for (WorkOrder workOrder : workorders) {
                if (workOrder.getWorkOrderNum().toLowerCase().contains(searchedText.toString().toLowerCase()) || workOrder.getShortText().toLowerCase().contains(searchedText.toString().toLowerCase()) || workOrder.getEquipNum().equalsIgnoreCase(searchedText.toString()) || workOrder.getFuncLocation().toLowerCase().startsWith(searchedText.toString().toLowerCase().trim()) || workOrder.getPostalCode() != null && workOrder.getPostalCode().contains(searchedText.toString().toUpperCase()))
                    searchedWOs.add(workOrder);
                else {
                    String entitySetName = ZCollections.OPR_COLLECTION;
                    String resPath = entitySetName;
                    resPath += "/$count?$filter=WorkOrderNum eq '" + workOrder.getWorkOrderNum() + "' and startswith(SystemStatus,'" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true and (endswith(Equipment, '" + searchedText + "') eq true or FuncLoc eq '" + searchedText + "') and (SubOperation eq '' or SubOperation eq null)";
                    ResponseObject response = DataHelper.getInstance().getEntities(entitySetName, resPath);
                    if (response != null && !response.isError()) {
                        int count = Integer.parseInt(String.valueOf(response.Content()));
                        if (count > 0)
                            searchedWOs.add(workOrder);
                    }
                }
            }
        } catch (NumberFormatException e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            e.printStackTrace();
        }
        return searchedWOs;
    }

    public String getTechID() {
        return TechID;
    }

    public static int getWorkOrdersCountByPriority(String priority) {
        try {
            return getWorkOrdersCount("?$filter=Priority eq '" + priority + "'");
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return 0;
    }

    // getWorkOrderCount() -- Return the total count of WorkOrders based on your filters.
    public static int getWorkOrdersCount(@NonNull String filterQuery) {
        ResponseObject result = null;
        try {

            //FetchLevel:Count -- will give the total count of WorkOrders.
            //filterQueryWo -- your condition (WHERE clause) for filtering total WorkOrders.
            result = getFilteredWorkOrders(filterQuery, ZAppSettings.FetchLevel.Count, null);
            if (result != null && !result.isError()) {
                return Integer.parseInt(result.Content().toString());
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return 0;
    }

    public static int getTotalWorkOrdersCount() {
        ResponseObject result;
        try {
            String entitySetName = ZCollections.WO_COLLECTION;
            String resPath = entitySetName + "/$count";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                return Integer.parseInt(result.Content().toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return 0;
    }

    public void setTechID(String techID) {
        TechID = techID;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, boolean fetchAddress, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<WorkOrder> workOrders = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    workOrders.add(new WorkOrder(entity, fetchLevel, fetchAddress));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", workOrders);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ArrayList<MeasurementPointReading> getWorkOrderMeasurementPoints(WorkOrder workOrder) {
        ResponseObject response = null;
        ArrayList<MeasurementPointReading> woMeasurementPoints = new ArrayList<>();
        try {
            if (workOrder.getEquipNum() != null && !workOrder.getEquipNum().isEmpty()) {
                response = MeasurementPointReading.getEquipmentMeasurementPoint(workOrder.getEquipNum(), ZAppSettings.FetchLevel.List, null, null);
                if (response != null && !response.isError())
                    woMeasurementPoints.addAll((ArrayList<MeasurementPointReading>) response.Content());
            }
            if (workOrder.getFuncLocation() != null && !workOrder.getFuncLocation().isEmpty()) {
                response = MeasurementPointReading.getFLMeasurementPoint(workOrder.getFuncLocation(), ZAppSettings.FetchLevel.List, null, null);
                if (response != null && !response.isError())
                    woMeasurementPoints.addAll((ArrayList<MeasurementPointReading>) response.Content());
            }
            if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED && ZConfigManager.WO_OP_OBJS_DISPLAY.isEmpty()) {

            }
            if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED && ZConfigManager.ENABLE_OPERATION_MEASUREMENTPOINT_READINGS) {
                String entitySetName = ZCollections.OPR_COLLECTION;
                String resPath = entitySetName;
                resPath += "?$filter=WorkOrderNum eq '" + workOrder.getWorkOrderNum() + "' and not startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') and (SubOperation eq '' or SubOperation eq null)&$select=Equipment,FuncLoc,OperationNum&$orderby=OperationNum";
                response = DataHelper.getInstance().getEntities(entitySetName, resPath);
                if (response != null && !response.isError()) {
                    List<ODataEntity> oprEntities = (List<ODataEntity>) response.Content();
                    if (oprEntities != null && oprEntities.size() > 0) {
                        for (ODataEntity entity : oprEntities) {
                            String eqpId = String.valueOf(entity.getProperties().get("Equipment").getValue());
                            String flId = String.valueOf(entity.getProperties().get("FuncLoc").getValue());
                            String oprNum = String.valueOf(entity.getProperties().get("OperationNum").getValue());

                            if (!eqpId.isEmpty()) {
                                response = MeasurementPointReading.getEquipmentMeasurementPoint(eqpId, ZAppSettings.FetchLevel.List, "", oprNum);
                                if (response != null && !response.isError())
                                    woMeasurementPoints.addAll((ArrayList<MeasurementPointReading>) response.Content());
                            }

                            if (!flId.isEmpty()) {
                                response = MeasurementPointReading.getFLMeasurementPoint(flId, ZAppSettings.FetchLevel.List, "", oprNum);
                                if (response != null && !response.isError())
                                    woMeasurementPoints.addAll((ArrayList<MeasurementPointReading>) response.Content());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return woMeasurementPoints;
    }

    /**
     * @return ArrayList of all distinct mobile object statuses among all workorders
     */
    public static ArrayList<String> getAllDistinctStatuses() {
        ArrayList<String> statuses = new ArrayList<>();
        try {
            String resPath = ZCollections.WO_COLLECTION + "?$select=MobileObjStatus";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.WO_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        statuses.add(String.valueOf(entity.getProperties().get("MobileObjStatus").getValue()));
                    }
                    Set<String> strings = new HashSet<String>(statuses);
                    statuses.clear();
                    statuses.addAll(strings);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return statuses;
    }

    /**
     * @return ArrayList of SpinnerItem of all distinct mobile object statuses among all workorders
     */
    public static ArrayList<SpinnerItem> getSpinnerStatuses() {
        ArrayList<SpinnerItem> spinnerStatuses = new ArrayList<>();
        ArrayList<String> statuses = getAllDistinctStatuses();
        for (String status : statuses) {
            String statusDescription = "";
            if (!status.isEmpty())
                for (ZAppSettings.MobileStatus mobileStatus : ZAppSettings.MobileStatus.values()) {
                    if (status.equalsIgnoreCase(mobileStatus.getMobileStatusCode())) {
                        statusDescription = mobileStatus.getMobileStatusDesc();
                        break;
                    }
                }
            spinnerStatuses.add(new SpinnerItem(status, (statusDescription.isEmpty() ? status : statusDescription)));
        }
        return spinnerStatuses;
    }

    /**
     * @return ArrayList of all distinct user statuses among all workorders
     */
    public static ArrayList<String> getAllDistinctUserStatuses() {
        ArrayList<String> statuses = new ArrayList<>();
        try {
            String resPath = ZCollections.WO_COLLECTION + "?$select=UserStatus";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.WO_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        String userStatus = String.valueOf(entity.getProperties().get("UserStatus").getValue());
                        String[] userStatuses;
                        if (userStatus.contains(" ")) {
                            userStatuses = userStatus.split(" ");
                            if (userStatuses.length > 0) {
                                statuses.addAll(Arrays.asList(userStatuses));
                            }
                        } else
                            statuses.add(userStatus);
                    }
                    Set<String> strings = new HashSet<String>(statuses);
                    statuses.clear();
                    statuses.addAll(strings);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return statuses;
    }

    /**
     * @return ArrayList of SpinnerItem of all distinct user statuses among all workorders
     */
    public static ArrayList<SpinnerItem> getSpinnerUserStatuses() {
        ArrayList<SpinnerItem> spinnerStatuses = new ArrayList<>();
        ArrayList<String> statuses = getAllDistinctUserStatuses();
        for (String status : statuses) {
            if (!status.isEmpty())
                spinnerStatuses.add(new SpinnerItem(status, status));
        }
        return spinnerStatuses;
    }

    /**
     * @return ArrayList of SpinnerItem of all distinct systemStatus among all workorders
     */
    public static ArrayList<SpinnerItem> getSpinnerSysStatuses() {
        ArrayList<SpinnerItem> spinnerStatuses = new ArrayList<>();
        ArrayList<String> statuses = getAllDistinctSysStatus();
        for (String status : statuses) {
            if (!status.isEmpty())
                spinnerStatuses.add(new SpinnerItem(status, status));
        }
        return spinnerStatuses;
    }

    /**
     * @return ArrayList of all distinct WorkCenters among all workorders
     */
    public static ArrayList<String> getAllDistinctWorkCenters() {
        ArrayList<String> workCenters = new ArrayList<>();
        try {
            String resPath = ZCollections.WO_COLLECTION + "?$select=MainWorkCtr";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.WO_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        workCenters.add(String.valueOf(entity.getProperties().get("MainWorkCtr").getValue()));
                    }
                    Set<String> strings = new HashSet<String>(workCenters);
                    workCenters.clear();
                    workCenters.addAll(strings);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return workCenters;
    }

    /**
     * @return ArrayList of SpinnerItem of all distinct WorkCenters among all workorders
     */
    public static ArrayList<SpinnerItem> getSpinnerWorkCenters() {
        ArrayList<SpinnerItem> spinnerWorkCenters = new ArrayList<>();
        ArrayList<String> workCenters = getAllDistinctWorkCenters();
        for (String workCenter : workCenters) {
            if (!workCenter.isEmpty())
                spinnerWorkCenters.add(new SpinnerItem(workCenter, workCenter));
        }
        return spinnerWorkCenters;
    }

    /**
     * @return ArrayList of all distinct Priorities among all workorders
     */
    public static ArrayList<String> getAllDistinctPriorities() {
        ArrayList<String> priorities = new ArrayList<>();
        try {
            String resPath = ZCollections.WO_COLLECTION + "?$select=Priority&$orderby=Priority";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.WO_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        priorities.add(String.valueOf(entity.getProperties().get("Priority").getValue()));
                    }
                    Set<String> strings = new HashSet<String>(priorities);
                    priorities.clear();
                    priorities.addAll(strings);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return priorities;
    }

    /**
     * @return ArrayList of SpinnerItem of all distinct Priorities among all workorders
     */
    public static ArrayList<SpinnerItem> getSpinnerPriorities() {
        ArrayList<SpinnerItem> spinnerPriorities = new ArrayList<>();
        ArrayList<SpinnerItem> tempSpinnerPriorities = new ArrayList<>();
        ResponseObject response = com.ods.myjobcard_library.entities.ctentities.Priority.getPriorities();
        if (response != null && !response.isError()) {
            tempSpinnerPriorities = (ArrayList<SpinnerItem>) response.Content();
        }
        ArrayList<String> priorities = getAllDistinctPriorities();
        if (tempSpinnerPriorities != null && tempSpinnerPriorities.size() > 0) {
            for (SpinnerItem item : tempSpinnerPriorities) {
                if (priorities.contains(item.getId()))
                    spinnerPriorities.add(item);
            }
        }
        /*for(String priority: priorities){
            spinnerPriorities.add(new SpinnerItem(priority, priority));
        }*/
        return spinnerPriorities;
    }

    /**
     * @return ArrayList of all distinct PM Activity Types among all workorders
     */
    public static ArrayList<String> getAllDistinctPMActivityTypes() {
        ArrayList<String> activityTypes = new ArrayList<>();
        try {
            String resPath = ZCollections.WO_COLLECTION + "?$select=MaintActivityType,MaintActivityTypeText&$orderby=MaintActivityType";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.WO_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        String activityText = entity.getProperties().get("MaintActivityType").getValue() + "-" + entity.getProperties().get("MaintActivityTypeText").getValue();
                        activityTypes.add(activityText);
                    }
                    Set<String> strings = new HashSet<String>(activityTypes);
                    activityTypes.clear();
                    activityTypes.addAll(strings);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return activityTypes;
    }

    /**
     * @return ArrayList of SpinnerItem of all distinct PM Activity Types among all workorders
     */
    public static ArrayList<SpinnerItem> getSpinnerPMActivityTypes() {
        ArrayList<SpinnerItem> spinnerPMActivityTypes = new ArrayList<>();
        ArrayList<String> pmActivityTypes = getAllDistinctPMActivityTypes();
        for (String actType : pmActivityTypes) {
            try {
                if (!actType.isEmpty() && actType.contains("-")) {
                    spinnerPMActivityTypes.add(new SpinnerItem(actType.split("-")[0], actType.split("-")[1]));
                }
            } catch (Exception e) {
                DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Warning, e.getMessage());
            }
        }
        return spinnerPMActivityTypes;
    }

    /**
     * @return ArrayList of all distinct Technicians among all workorder operations
     */
    public static ArrayList<String> getAllDistinctOperationTechnicians() {
        ArrayList<String> technicians = new ArrayList<>();
        try {
            String resPath = ZCollections.OPR_COLLECTION + "?$select=PersonnelNo&$orderby=PersonnelNo";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.OPR_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        technicians.add(String.valueOf(entity.getProperties().get("PersonnelNo").getValue()));
                    }
                    Set<String> strings = new HashSet<String>(technicians);
                    technicians.clear();
                    technicians.addAll(strings);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return technicians;
    }

    /**
     * @return ArrayList of SpinnerItem of all distinct Technicians among all workorder operations
     */
    public static ArrayList<SpinnerItem> getSpinnerOperationTechnicians() {
        ArrayList<SpinnerItem> spinnerTechnicians = new ArrayList<>();
        ArrayList<String> technicians = getAllDistinctOperationTechnicians();
        for (String technician : technicians) {
            try {
                if (!technician.isEmpty()) {
                    if (technician.equalsIgnoreCase("00000000"))
                        spinnerTechnicians.add(new SpinnerItem(technician, "Unassigned"));
                    else {
                        String techName = "";
                        ResponseObject result = com.ods.myjobcard_library.entities.ctentities.PersonResponsible.getLabour(technician, "");
                        if (!result.isError())
                            techName = ((com.ods.myjobcard_library.entities.ctentities.PersonResponsible) result.Content()).getEmplApplName();
                        spinnerTechnicians.add(new SpinnerItem(technician, (techName.isEmpty() ? technician : techName)));
                    }
                }
            } catch (Exception e) {
                DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Warning, e.getMessage());
            }
        }
        return spinnerTechnicians;
    }

    /**
     * @return ArrayList of all distinct Technicians among all workorder
     */
    public static ArrayList<String> getAllDistinctWorkOrderTechnicians() {
        ArrayList<String> technicians = new ArrayList<>();
        try {
            String resPath = ZCollections.WO_COLLECTION + "?$select=PersonResponsible&$orderby=PersonResponsible";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.WO_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        technicians.add(String.valueOf(entity.getProperties().get("PersonResponsible").getValue()));
                    }
                    Set<String> strings = new HashSet<String>(technicians);
                    technicians.clear();
                    technicians.addAll(strings);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return technicians;
    }

    /**
     * @return ArrayList of SpinnerItem of all distinct Technicians among all workorders
     */
    public static ArrayList<SpinnerItem> getSpinnerWorkOrderTechnicians() {
        ArrayList<SpinnerItem> spinnerTechnicians = new ArrayList<>();
        ArrayList<String> technicians = getAllDistinctWorkOrderTechnicians();
        for (String technician : technicians) {
            try {
                if (!technician.isEmpty()) {
                    if (technician.equalsIgnoreCase("00000000"))
                        spinnerTechnicians.add(new SpinnerItem(technician, "Unassigned"));
                    else {
                        String techName = "";
                        ResponseObject result = com.ods.myjobcard_library.entities.ctentities.PersonResponsible.getLabour(technician, "");
                        if (!result.isError())
                            techName = ((com.ods.myjobcard_library.entities.ctentities.PersonResponsible) result.Content()).getEmplApplName();
                        spinnerTechnicians.add(new SpinnerItem(technician, (techName.isEmpty() ? technician : techName)));
                    }
                }
            } catch (Exception e) {
                DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Warning, e.getMessage());
            }
        }
        return spinnerTechnicians;
    }

    /**
     * @return ArrayList of all distinct System Status among all workorder operations
     */
    public static ArrayList<String> getAllDistinctSysStatus() {
        ArrayList<String> statuses = new ArrayList<>();
        try {
            String resPath = ZCollections.WO_COLLECTION + "?$select=SysStatus";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.WO_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        String userStatus = String.valueOf(entity.getProperties().get("SysStatus").getValue());
                        String[] userStatuses;
                        if (userStatus.contains(" ")) {
                            userStatuses = userStatus.split(" ");
                            if (userStatuses.length > 0) {
                                statuses.addAll(Arrays.asList(userStatuses));
                            }
                        } else
                            statuses.add(userStatus);
                    }
                    Set<String> strings = new HashSet<String>(statuses);
                    statuses.clear();
                    statuses.addAll(strings);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return statuses;
    }

    /**
     * @return ArrayList of all distinct Locations among all workorders
     */
    public static ArrayList<String> getAllDistinctLocations() {
        ArrayList<String> types = new ArrayList<>();
        try {
            String resPath = ZCollections.WO_COLLECTION + "?$select=Location";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.WO_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = BaseEntity.setODataEntityList(response.Content());
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        types.add(String.valueOf(entity.getProperties().get("Location").getValue()));
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
     * @return ArrayList of SpinnerItem of all distinct Locations among all workorders
     */
    public static ArrayList<SpinnerItem> getSpinnerLocations() {
        ArrayList<SpinnerItem> spinnerLocations = new ArrayList<>();
        ArrayList<String> locations = getAllDistinctLocations();
        for (String location : locations) {
            if (!location.isEmpty())
                spinnerLocations.add(new SpinnerItem(location, location));
        }
        return spinnerLocations;
    }

    @Override
    public boolean isLocal() {
        return super.isLocal();
    }

    public String getSuperiorOrder() {
        return SuperiorOrder;
    }

    public void setSuperiorOrder(String superiorOrder) {
        SuperiorOrder = superiorOrder;
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String number) {
        WorkOrderNum = number;
    }

    public String getTrimmedWorkOrderNum() {
        return String.valueOf(Integer.parseInt(WorkOrderNum));
    }

    public String getDisplayableWorkOrderNum() {
        if (getTempID() == null || getTempID().isEmpty())
            return getWorkOrderNum();
        else
            return getTempID().replace(ZConfigManager.LOCAL_ID, ZConfigManager.LOCAL_IDENTIFIER);
       /* if(this.getTempID()==null||this.getTempID().isEmpty())
        return this.getWorkOrderNum().replace(ZConfigManager.WONUM_VALUE_PREFIX, ZConfigManager.LOCAL_IDENTIFIER);*/
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getBusArea() {
        return BusArea;
    }

    public void setBusArea(String busArea) {
        BusArea = busArea;
    }

    public String getMainWorkCtr() {
        return MainWorkCtr;
    }

    public void setMainWorkCtr(String mainWorkCtr) {
        MainWorkCtr = mainWorkCtr;
    }

    public String getPlantMainWorkCtr() {
        return PlantMainWorkCtr;
    }

    public void setPlantMainWorkCtr(String plantMainWorkCtr) {
        PlantMainWorkCtr = plantMainWorkCtr;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getShortText() {
        return ShortText;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getPriorityText() {
        return com.ods.myjobcard_library.entities.ctentities.Priority.getPriorityText(Priority);
    }

    public String getNotificationNum() {
        return NotificationNum;
    }

    public void setNotificationNum(String notificationNum) {
        NotificationNum = notificationNum;
    }

    public String getEquipNum() {
        return EquipNum;
    }

    public void setEquipNum(String equipNum) {
        EquipNum = equipNum;
    }

    public String getFuncLocation() {
        return FuncLocation;
    }

    public void setFuncLocation(String funcLocation) {
        FuncLocation = funcLocation;
    }

    public String getTechObjLocAndAssgnmnt() {
        return TechObjLocAndAssgnmnt;
    }

    public void setTechObjLocAndAssgnmnt(String techObjLocAndAssgnmnt) {
        TechObjLocAndAssgnmnt = techObjLocAndAssgnmnt;
    }

    public String getMobileObjStatus() {
        return MobileObjStatus;
    }

    public void setMobileObjStatus(String mobileObjStatus) {
        MobileObjStatus = mobileObjStatus;
    }

    public String getMobileObjectType() {
        return MobileObjectType;
    }

    public void setMobileObjectType(String mobileObjectType) {
        MobileObjectType = mobileObjectType;
    }

    public GregorianCalendar getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(GregorianCalendar createdOn) {
        CreatedOn = createdOn;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getLastChangedBy() {
        return LastChangedBy;
    }

    public void setLastChangedBy(String lastChangedBy) {
        LastChangedBy = lastChangedBy;
    }

    public String getResponsiblPlannerGrp() {
        return ResponsiblPlannerGrp;
    }

    public void setResponsiblPlannerGrp(String responsiblPlannerGrp) {
        ResponsiblPlannerGrp = responsiblPlannerGrp;
    }

    public String getPersonResponsible() {
        return PersonResponsible;
    }

    public void setPersonResponsible(String personResponsible) {
        PersonResponsible = personResponsible;
    }

    public String getPersonInchargeTel() {
        return PersonInchargeTel;
    }

    public void setPersonInchargeTel(String personInchargeTel) {
        PersonInchargeTel = personInchargeTel;
    }

    public String getPersonResponsibleTechInsp() {
        return PersonResponsibleTechInsp;
    }

    public void setPersonResponsibleTechInsp(String personResponsibleTechInsp) {
        PersonResponsibleTechInsp = personResponsibleTechInsp;
    }

    public String getAssignedTo() {
        return AssignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        AssignedTo = assignedTo;
    }

    public String getInChargeContact() {
        return InChargeContact;
    }

    public void setInChargeContact(String inChargeContact) {
        InChargeContact = inChargeContact;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public BigDecimal getLatitude() {
        parsePoint();
        return Latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        Latitude = latitude != null ? latitude.setScale(6, BigDecimal.ROUND_HALF_DOWN) : latitude;
    }

    public BigDecimal getLongitude() {
        parsePoint();
        return Longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        Longitude = longitude != null ? longitude.setScale(6, BigDecimal.ROUND_HALF_DOWN) : longitude;
    }

    public String getGeoLocation() {
        return GeoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        GeoLocation = geoLocation;
    }

    public GregorianCalendar getChangeDtForOrderMaster() {
        return ChangeDtForOrderMaster;
    }

    public void setChangeDtForOrderMaster(GregorianCalendar changeDtForOrderMaster) {
        ChangeDtForOrderMaster = changeDtForOrderMaster;
    }

    public GregorianCalendar getStrtDate() {
        return StrtDate;
    }

    public void setStrtDate(GregorianCalendar strtDate) {
        StrtDate = strtDate;
    }

    public GregorianCalendar getBasicFnshDate() {
        return BasicFnshDate;
    }

    public void setBasicFnshDate(GregorianCalendar basicFnshDate) {
        BasicFnshDate = basicFnshDate;
    }

    public Time getBasicFnshTime() {
        return BasicFnshTime;
    }

    public void setBasicFnshTime(Time basicFnshTime) {
        BasicFnshTime = basicFnshTime;
    }

    public GregorianCalendar getBasicStrtDate() {
        return BasicStrtDate;
    }

    public void setBasicStrtDate(GregorianCalendar basicStrtDate) {
        BasicStrtDate = basicStrtDate;
    }

    public Time getBasicStrtTime() {
        return BasicStrtTime;
    }

    public void setBasicStrtTime(Time basicStrtTime) {
        BasicStrtTime = basicStrtTime;
    }

    public GregorianCalendar getSchdFnshDate() {
        return SchdFnshDate;
    }

    public void setSchdFnshDate(GregorianCalendar schdFnshDate) {
        SchdFnshDate = schdFnshDate;
    }

    public GregorianCalendar getSchdStrtDate() {
        return SchdStrtDate;
    }

    public void setSchdStrtDate(GregorianCalendar schdStrtDate) {
        SchdStrtDate = schdStrtDate;
    }

    public GregorianCalendar getActlFnshDate() {
        return ActlFnshDate;
    }

    public void setActlFnshDate(GregorianCalendar actlFnshDate) {
        ActlFnshDate = actlFnshDate;
    }

    public GregorianCalendar getActlStrtDate() {
        return ActlStrtDate;
    }

    public void setActlStrtDate(GregorianCalendar actlStrtDate) {
        ActlStrtDate = actlStrtDate;
    }

    public GregorianCalendar getCnfOrderFinishDate() {
        return CnfOrderFinishDate;
    }

    public void setCnfOrderFinishDate(GregorianCalendar cnfOrderFinishDate) {
        CnfOrderFinishDate = cnfOrderFinishDate;
    }

    public GregorianCalendar getTechCompletionDate() {
        return TechCompletionDate;
    }

    public void setTechCompletionDate(GregorianCalendar techCompletionDate) {
        TechCompletionDate = techCompletionDate;
    }

    /*public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }*/

    public String getControllingArea() {
        return ControllingArea;
    }

    public void setControllingArea(String controllingArea) {
        ControllingArea = controllingArea;
    }

    public String getResponsibleCostCenter() {
        return ResponsibleCostCenter;
    }

    public void setResponsibleCostCenter(String responsibleCostCenter) {
        ResponsibleCostCenter = responsibleCostCenter;
    }

    public String getMaintActivityType() {
        return MaintActivityType;
    }

    public void setMaintActivityType(String maintActivityType) {
        MaintActivityType = maintActivityType;
    }

    public String getMaintPlanningPlant() {
        return MaintPlanningPlant;
    }

    public void setMaintPlanningPlant(String maintPlanningPlant) {
        MaintPlanningPlant = maintPlanningPlant;
    }

    public String getMaintPlant() {
        return MaintPlant;
    }

    public void setMaintPlant(String maintPlant) {
        MaintPlant = maintPlant;
    }

    public String getSysStatusCode() {
        return SysStatusCode;
    }

    public void setSysStatusCode(String sysStatusCode) {
        SysStatusCode = sysStatusCode;
    }

    public String getSysStatus() {
        return SysStatus;
    }

    public void setSysStatus(String sysStatus) {
        SysStatus = sysStatus;
    }

    public String getWBSElem() {
        return WBSElem;
    }

    public void setWBSElem(String WBSElem) {
        this.WBSElem = WBSElem;
    }

    public String getSysCondition() {
        return SysCondition;
    }

    public void setSysCondition(String sysCondition) {
        SysCondition = sysCondition;
    }

    public String getWorkPermitIssued() {
        return WorkPermitIssued;
    }

    public void setWorkPermitIssued(String workPermitIssued) {
        WorkPermitIssued = workPermitIssued;
    }

    public String getCustomerNum() {
        return CustomerNum;
    }

    public void setCustomerNum(String customerNum) {
        CustomerNum = customerNum;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getTechObjDescription() {
        return TechObjDescription;
    }

    public void setTechObjDescription(String techObjDescription) {
        TechObjDescription = techObjDescription;
    }

    public boolean isDeletionFlag() {
        return DeletionFlag;
    }

    public void setDeletionFlag(boolean deletionFlag) {
        DeletionFlag = deletionFlag;
    }

    public String getObjectNumber() {
        return ObjectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        ObjectNumber = objectNumber;
    }

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        CompanyCode = companyCode;
    }

    public PartnerAddress getDefaultPartnerAddress() {
        return defaultPartnerAddress;
    }

    public void setDefaultPartnerAddress(PartnerAddress defaultPartnerAddress) {
        this.defaultPartnerAddress = defaultPartnerAddress;
    }

    public Address getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Address defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getAddressNumber() {
        return AddressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        AddressNumber = addressNumber;
    }

    public String getWOAddressNumber() {
        return WOAddressNumber;
    }

    public void setWOAddressNumber(String woAddressNumber) {
        WOAddressNumber = woAddressNumber;
    }

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
    }

    public String getStatusFlag() {
        return StatusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        StatusFlag = statusFlag;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    private void addValidStatus(StatusCategory validStatus) {
        if (validStatuses == null)
            validStatuses = new ArrayList<>();
        validStatuses.add(validStatus);
    }

    public ArrayList<StatusCategory> getValidStatuses() {
        return validStatuses;
    }

    public Operation getCurrentOperation() {
        return CurrentOperation;
    }

    public void setCurrentOperation(Operation currOpr) {
        CurrentOperation = currOpr;
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

    public boolean isAttachmentAvailable() {
        return isAttachmentAvailable;
    }

    public void setIsAttachmentAvailable(boolean isAttachmentAvailable) {
        this.isAttachmentAvailable = isAttachmentAvailable;
    }

    public String getBusAreaText() {
        return BusAreaText;
    }

    public void setBusAreaText(String busAreaText) {
        BusAreaText = busAreaText;
    }

    public String getSysContitionText() {
        return SysContitionText;
    }

    public void setSysContitionText(String sysContitionText) {
        SysContitionText = sysContitionText;
    }

    public String getMaintActivityTypeText() {
        return MaintActivityTypeText;
    }

    public void setMaintActivityTypeText(String maintActivityTypeText) {
        MaintActivityTypeText = maintActivityTypeText;
    }

    public String getCategoryText() {
        return CategoryText;
    }

    public void setCategoryText(String categoryText) {
        CategoryText = categoryText;
    }

    public String getCreateNotifFlag() {
        return CreateNotifFlag;
    }

    public void setCreateNotifFlag(String createNotifFlag) {
        CreateNotifFlag = createNotifFlag;
    }

    public boolean isErrorEntity() {
        return ErrorEntity;
    }

    public void setErrorEntity(boolean errorEntity) {
        ErrorEntity = errorEntity;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public String getFollowUpOrder() {
        return FollowUpOrder;
    }

    public void setFollowUpOrder(String followUpOrder) {
        FollowUpOrder = followUpOrder;
    }

    public String getFolllowOnFlag() {
        return FolllowOnFlag;
    }

    public void setFolllowOnFlag(String folllowOnFlag) {
        FolllowOnFlag = folllowOnFlag;
    }

    public String getBOMCategory() {
        return BOMCategory;
    }

    public void setBOMCategory(String BOMCategory) {
        this.BOMCategory = BOMCategory;
    }

    public String getBOM() {
        return BOM;
    }

    public void setBOM(String BOM) {
        this.BOM = BOM;
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

    public String getToMainWorkCtr() {
        return ToMainWorkCtr;
    }

    public void setToMainWorkCtr(String toMainWorkCtr) {
        ToMainWorkCtr = toMainWorkCtr;
    }

    public Double getBreakdownDur() {
        return BreakdownDur;
    }

    public void setBreakdownDur(Double breakdownDur) {
        BreakdownDur = breakdownDur;
    }

    public String getBreakdown() {
        return Breakdown;
    }

    public void setBreakdown(String breakdown) {
        Breakdown = breakdown;
    }

    public String getToPlantMainWorkCtr() {
        return ToPlantMainWorkCtr;
    }

    public void setToPlantMainWorkCtr(String toPlantMainWorkCtr) {
        ToPlantMainWorkCtr = toPlantMainWorkCtr;
    }

    public GregorianCalendar getEffective_TS() {
        return Effective_TS;
    }

    public void setEffective_TS(GregorianCalendar effective_TS) {
        Effective_TS = effective_TS;
    }

    public String getPlannerGroupDes() {
        return PlannerGroupDes;
    }

    //Status Management

    public void setPlannerGroupDes(String plannerGroupDes) {
        PlannerGroupDes = plannerGroupDes;
    }

    public String getUnassigned() {
        return Unassigned;
    }

    public void setUnassigned(String unassigned) {
        Unassigned = unassigned;
    }

    public String getOnlineSearch() {
        return OnlineSearch;
    }

    public void setOnlineSearch(String onlineSearch) {
        OnlineSearch = onlineSearch;
    }

    public void parsePoint() {
        try {
            if (GeoLocation != null && !GeoLocation.isEmpty()) {
                String[] coordinatesStr = GeoLocation.split(",");
                if (coordinatesStr.length > 0) {
                    BigDecimal longitude = new BigDecimal(coordinatesStr[0].split(":")[1]);
                    BigDecimal latitude = new BigDecimal(coordinatesStr[1].split(":")[1]);
                    /*if(longitude.doubleValue() != 0 && latitude.doubleValue() != 0)
                    {*/
                    Longitude = longitude;
                    Latitude = latitude;
//                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.WO_COLLECTION);
        this.setEntityType(ZCollections.WO_ENTITY_TYPE);
        this.addKeyFieldNames(ZConfigManager.WO_KEY_FIELD);
        this.setParentEntitySetName(ZCollections.WO_COLLECTION);
        this.setParentForeignKeyFieldName(ZConfigManager.WO_KEY_FIELD);
        this.setParentKeyFieldName(ZConfigManager.WO_KEY_FIELD);
    }

    public ArrayList<PartnerAddress> getPartnerAddresses() {
        return partnerAddresses;
    }

    public void setPartnerAddresses(ArrayList<PartnerAddress> partnerAddresses) {
        this.partnerAddresses = partnerAddresses;

        //set Default PartnerAddress
        for (PartnerAddress adr : partnerAddresses) {
            if (adr.getPartnerFunction().equalsIgnoreCase(ZConfigManager.DEFAULT_ADDRESS_TYPE)) {
                setDefaultPartnerAddress(adr);
            }
        }
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public FunctionalLocation getFunctionalLocation() {
        return functionalLocation;
    }

    public void setFunctionalLocation(FunctionalLocation functionalLocation) {
        this.functionalLocation = functionalLocation;
    }

    public boolean isInitialized() {
        return true;
        //return (!TextUtils.isEmpty(this.Orderid));
    }

    public ResponseObject toEntity(boolean onlyHeader, ODataEntity entityToBeFilled) {
        ResponseObject result = null;
        ODataEntity woODataEntity = null;
        WOEntity woEntity = null;
        Class<?> cls = null;
        Field[] declaredFields = null;
        Method method = null;

        try {
            cls = this.getClass();
            declaredFields = cls.getDeclaredFields();

            for (Field field : declaredFields) {
                //field.setAccessible(true);
                //classFields.add(field.getName());
                if (!field.getType().getSimpleName().equalsIgnoreCase("Creator") && !field.getType().getSimpleName().equalsIgnoreCase("DateFormat")) {
                    try {
                        method = cls.getDeclaredMethod("get" + field.getName());
                        entityToBeFilled.getProperties().put(field.getName(),
                                new ODataPropertyDefaultImpl(field.getName(),
                                        method.invoke(this)));

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }
            }
            if (entityToBeFilled != null) {
                woEntity = new WOEntity(entityToBeFilled);
                /*if(!onlyHeader)
                {
                    result = OperationEntities(false);
                    if(!result.isError())
                    {
                        woEntity.setEntityOprs((ArrayList<OprEntity>) result.Content());
                    }
                }*/
            }
            result = new ResponseObject(ZConfigManager.Status.Success, "", woEntity);
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getLocalizedMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public void create(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel, boolean fetchAddress) {
        Operation opr = null;
        ZAppSettings.FetchLevel operationsFetchLevel, componentsFetchLevel;
        /*ODataPropMap properties = null;
        ArrayList<String> classFields = null;
        Field[] declaredFields=null;
        Method method = null;*/
        try {
            super.create(entity);

            if (!fetchLevel.equals(ZAppSettings.FetchLevel.ListSpinner)) {

                ResponseObject resultAttachments = null;
                try {
                    String strResPath = ZCollections.WO_ATTACHMENT_COLLECTION + "/$count?$filter=(endswith(ObjectKey, '" + getWorkOrderNum() + "') eq true)";
                    resultAttachments = DataHelper.getInstance().getEntities(ZCollections.WO_ATTACHMENT_COLLECTION, strResPath);
                    if (!resultAttachments.isError()) {
                        Object rawData = resultAttachments.Content();
                        if (Integer.parseInt(rawData.toString()) > 0)
                            isAttachmentAvailable = true;
                    }
                } catch (Exception e) {
                    DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                }
                deriveWOStatus();
                //Set operations and Components for the Work Order
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public int getPriorityDrawable() {
        if (Priority.trim().equals("4"))
            return R.drawable.emergency_medium;
        if (Priority.trim().equals("3"))
            return R.drawable.emergency_low;
        if (Priority.trim().equals("2"))
            return R.drawable.emergency_high;
        if (Priority.trim().equals("1"))
            return R.drawable.emergency_very_high;
        return R.drawable.emergency_very_high;
    }

    public int getMobileObjStatusDrawable() {
        try {
            if (getStatusDetail() != null && getStatusDetail().woOprStatus != null) {
                return getStatusDetail().woOprStatus.getDrawableResId();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return R.drawable.download;
    }

    public ResponseObject UpdateTransferStatus(StatusCategory status, String Notes, String StatusReason, String Priority, String LabourCode, boolean autoFlush, Location location, String Plant, String WorkCenter) {
        ResponseObject result = null;
        try {
            setPriority(Priority);
            if (ZConfigManager.ASSIGNMENT_TYPE == ZAppSettings.AssignmentType.WorkCenterWorkOrderLevel || ZConfigManager.ASSIGNMENT_TYPE == ZAppSettings.AssignmentType.WorkCenterOperationLevel) {
                setPlant(Plant);
                setToMainWorkCtr(WorkCenter);
            } else
                setTransferPerson(LabourCode);
            setTransferFlag(ZConfigManager.STATUS_SET_FLAG);
            setStatusFlag("");
            //setTransferReason(StatusReason);
            result = UpdateStatus(status, Notes, StatusReason, autoFlush, location);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
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
            if (getTransferFlag().isEmpty())
                setStatusFlag(ZConfigManager.STATUS_SET_FLAG);

            setMobileObjStatus(status.getStatusCode());
            setUserStatus(status.getStatusCode());
            setMobileObjectType(status.getObjectType());
            String statusDesc = status.getStatusDescKey();
            if (status.woOprStatus.equals(ZAppSettings.MobileStatus.STRT))
                setActlStrtDate(ZCommon.getDeviceDateTime());
            if (status.woOprStatus.equals(ZAppSettings.MobileStatus.COMP))
                setActlFnshDate(ZCommon.getDeviceDateTime());

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
                strStatusText = ZConfigManager.AUTO_NOTES_TEXT_LINE1 + " " + statusDesc + " " +
                        ZConfigManager.AUTO_NOTES_TEXT_LINE2 + " " + ZAppSettings.strUser.toUpperCase() + " " +
                        (status.woOprStatus == ZAppSettings.MobileStatus.TRNS ? (ZConfigManager.AUTO_NOTES_TEXT_LINE5 + " " + getTransferPerson() + " ") : "") +
                        ZConfigManager.AUTO_NOTES_TEXT_LINE3 + " " + deviceTime;
                if (ZConfigManager.ENABLE_POST_DEVICE_LOCATION_NOTES && deviceLocation != null) {
                    strStatusText = strStatusText + " at location Lat: " + deviceLocation.getLatitude() + "; Long: " + deviceLocation.getLongitude();
                }
                strNotesText = strStatusText + (strReasonText.isEmpty() ? "" : ("\n" + strReasonText + "\n")) + (Notes != null ? Notes : "");
            }
            //setNotes(strNotesText);
            //Update the WO to offlinestore
            setMode(ZAppSettings.EntityMode.Update);

            if (isOnline) {
                //Update WO to Online.
                result = new ResponseObject(ConfigManager.Status.Success);
                result.setContent(this);
                return result;
            } else {
                if (!strNotesText.isEmpty()) {
                    WOLongText longText = new WOLongText();
                    int count = WOLongText.getLongTextsCount(getWorkOrderNum(), null, null, ZConfigManager.Fetch_Object_Type.WorkOrder);
                    result = longText.SendLongText(strNotesText, getWorkOrderNum(), null, ZConfigManager.Fetch_Object_Type.WorkOrder, count, getTempID(), false);
                }
                result = SaveToStore(autoFlush);
            }
            getWOAllowedStatus(status);

        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getRefId() {
        return this.getTempID() == null || this.getTempID().isEmpty() ? this.getWorkOrderNum() : this.getTempID();
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

    // Getting all allowed statuses with respect to the Current Status
    private void getWOAllowedStatus(StatusCategory status) {
        ResponseObject result = null;
        String strResPath;
        ArrayList<String> allowedStatusList = new ArrayList<>();
        int intNumOfActiveWO = 0;
        try {
            if (status != null) {

                result = WorkOrderStatus.getWorkOrderAllowedStatus(status, getOrderType());
                if (!result.isError()) {
                    allowedStatusList = (ArrayList<String>) result.Content();
                    //Check if any other WO is active, if Yes, remove the Disallowed status from then allowed status list
                    if (!ZConfigManager.ASSIGNMENT_TYPE.equals(ZAppSettings.AssignmentType.WorkCenterWorkOrderLevel) && getNumberOfActiveWO() > 0) {
                        allowedStatusList.remove(ZConfigManager.WO_ACTIVE_STATUS_DISABLED.getMobileStatusCode());
                    }
                    for (String allowedStatus : allowedStatusList) {
                        StatusCategory statusDetails = StatusCategory.getStatusDetails(allowedStatus, getOrderType(), ZConfigManager.Fetch_Object_Type.WorkOrder);
                        if (statusDetails != null)
                            addValidStatus(statusDetails);
                    }
                } else {
                    DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, "error in getting allowed status for WO: " + getWorkOrderNum() + ". Message: " + result.getMessage());
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, "error in getting allowed status for WO: " + getWorkOrderNum() + ". Message: " + e.getMessage());
        }
    }

    //Completion Flow
    public boolean isCompleted() {
        boolean completed = false;
        try {
            /*if (getMobileObjStatus().equalsIgnoreCase(ZAppSettings.MobileStatus.COMPLETE.getMobileStatusCode())) {
                completed = true;
            } else if (getMobileObjStatus().equalsIgnoreCase("")) {
                if (UserStatus.startsWith(ZAppSettings.MobileStatus.COMPLETE.getMobileStatusCode()))
                    completed = true;
            }*/
            completed = getStatusDetail().preCompletionCheckEnabled();
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return completed;
    }

    public boolean isActive() {
        boolean active = false;
        try {
            if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                active = getStatusDetail().isInProcess();
            } else {
                active = getCurrentOperation() != null && getCurrentOperation().getStatusDetail().isInProcess();
            }
            if (isLocal() && this.getEnteredBy() != null && this.getEnteredBy().equalsIgnoreCase(ZAppSettings.strUser))
                active = true;
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

        return active;
    }

    //
    /*public boolean isActiveWorkOrder() {
        ResponseObject responseObject = null;
        String strResPath, strQuery = "", strQuery1 = "";
        int intCounter = 0;
        Object rawData = null;
        try {
            for (ZAppSettings.MobileStatus status : ZAppSettings.MobileStatus.values()) {
                if (status.getMobileStatusIsConsideredActive()) {
                    if (strQuery.length() == 0) {

                        strQuery = "startswith(UserStatus,'" + status.getMobileStatusCode() + "') eq true";
                        strQuery1 = "startswith(MobileObjStatus,'" + status.getMobileStatusCode() + "') eq true";
                    } else {
                        strQuery = strQuery + " or " + "startswith(UserStatus,'" + status.getMobileStatusCode() + "') eq true";
                        strQuery1 = strQuery1 + " or " + "startswith(MobileObjStatus,'" + status.getMobileStatusCode() + "') eq true";
                    }
                }

            }
           *//* strResPath = ZCollections.WO_COLLECTION + "/$count?$filter= (MobileObjStatus eq '' and (" + strQuery + "))";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.WO_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intCounter = Integer.parseInt(rawData.toString());
            }*//*
            strResPath = ZCollections.WO_COLLECTION + "/$count?$filter=" + (getWorkOrderNum() != null && !getWorkOrderNum().isEmpty() ? "WorkOrderNum eq '" + getWorkOrderNum() + "' and " : "") + "(" + strQuery1 + ")";

            //strResPath = ZCollections.WO_COLLECTION + "/$count?$filter= (" + strQuery1 + ")";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.WO_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intCounter = intCounter + Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intCounter > 0;
    }*/
    private int getNumberOfActiveWO() {
        ResponseObject responseObject = null;
        String strResPath, strQuery = "", strQuery1 = "";
        int intCounter = 0;
        Object rawData = null;
        try {
            ArrayList<StatusCategory> statuses = StatusCategory.getStatuses(getOrderType(), ZConfigManager.Fetch_Object_Type.WorkOrder);
            for (StatusCategory status : statuses) {
                if (status.isInProcess()) {
                    if (strQuery.length() == 0) {

                        strQuery = "startswith(UserStatus,'" + status.getStatusCode() + "') eq true";
                        strQuery1 = "startswith(MobileObjStatus,'" + status.getStatusCode() + "') eq true";
                    } else {
                        strQuery = strQuery + " or " + "startswith(UserStatus,'" + status.getStatusCode() + "') eq true";
                        strQuery1 = strQuery1 + " or " + "startswith(MobileObjStatus,'" + status.getStatusCode() + "') eq true";
                    }
                }
            }
            /*for (ZAppSettings.MobileStatus status : ZAppSettings.MobileStatus.values()) {
                if (status.getMobileStatusIsConsideredActive()) {
                    if (strQuery.length() == 0) {

                        strQuery = "startswith(UserStatus,'" + status.getMobileStatusCode() + "') eq true";
                        strQuery1 = "startswith(MobileObjStatus,'" + status.getMobileStatusCode() + "') eq true";
                    } else {
                        strQuery = strQuery + " or " + "startswith(UserStatus,'" + status.getMobileStatusCode() + "') eq true";
                        strQuery1 = strQuery1 + " or " + "startswith(MobileObjStatus,'" + status.getMobileStatusCode() + "') eq true";
                    }
                }

            }*/
           /* strResPath = ZCollections.WO_COLLECTION + "/$count?$filter= (MobileObjStatus eq '' and (" + strQuery + "))";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.WO_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intCounter = Integer.parseInt(rawData.toString());
            }*/
            strResPath = ZCollections.WO_COLLECTION + "/$count?$filter= (" + strQuery1 + ")";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.WO_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intCounter = intCounter + Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intCounter;
    }

    public ResponseObject getCompletionPreCheckList(Context context) {
        ResponseObject result;
        ArrayList<String> errorMessages = new ArrayList<>();
        try {
            ArrayList<OrderTypeFeature> featureList = OrderTypeFeature.getMandatoryFeaturesByObjectType(this.getOrderType());
            for (OrderTypeFeature orderTypeFeature : featureList) {
                //Operations
                if (ZConfigManager.OPERATION_COMPLETION_REQUIRED && orderTypeFeature.getFeature().equalsIgnoreCase(ZAppSettings.Features.OPERATION.getFeatureValue())) {
                    int incompleteOperations = getTotalNumInCompleteOperations();
                    int totalOperations = getTotalNumOperations();
                    if (orderTypeFeature.getMandatoryLevel().equalsIgnoreCase(OrderTypeFeature.LEVEL_ALL)) {
                        if (incompleteOperations > 0)
                            errorMessages.add(context.getString(R.string.msgTotalOperationRequiredToComplete, incompleteOperations));
                    } else if (orderTypeFeature.getMandatoryLevel().equalsIgnoreCase(OrderTypeFeature.LEVEL_PARTIAL)) {
                        if (totalOperations == incompleteOperations)
                            errorMessages.add(context.getString(R.string.msgAtLeastOneOperationRequiredToComplete));
                    } else {
                        if (incompleteOperations > 0)
                            errorMessages.add(context.getString(R.string.msgTotalOperationRequiredToComplete, incompleteOperations));
                    }
                }
                //Components
                if (ZConfigManager.COMPONENT_ISSUE_REQUIRED && orderTypeFeature.getFeature().equalsIgnoreCase(ZAppSettings.Features.COMPONENT.getFeatureValue())) {
                    int remainingComponents = getTotalNumUnIssuedComponents() + (ZConfigManager.PARTIAL_COMPONENT_ISSUE_ALLOWED ? 0 : getTotalNumPartialIssuedComponents());
                    int totalComponents = getTotalNumComponents();
                    if (orderTypeFeature.getMandatoryLevel().equalsIgnoreCase(OrderTypeFeature.LEVEL_ALL)) {
                        if (remainingComponents > 0)
                            errorMessages.add(context.getString(R.string.msgTotalComponentsRequiredToIssued, remainingComponents));
                    } else if (orderTypeFeature.getMandatoryLevel().equalsIgnoreCase(OrderTypeFeature.LEVEL_PARTIAL)) {
                        if (totalComponents == remainingComponents) {
                            errorMessages.add(context.getString(R.string.msgAtLeastOneComponentRequiredToIssued, (ZConfigManager.PARTIAL_COMPONENT_ISSUE_ALLOWED ? "Partially" : "Completely")));
                        }
                    } else {
                        if (remainingComponents > 0)
                            errorMessages.add(context.getString(R.string.msgTotalComponentsRequiredToIssued, remainingComponents));
                    }
                }
                //Attachments
                if (ZConfigManager.ATTACHMENT_REQUIRED && orderTypeFeature.getFeature().equalsIgnoreCase(ZAppSettings.Features.ATTACHMENT.getFeatureValue())) {
                    if (getTotalNumUserUploadedAttachments() <= 0)
                        errorMessages.add(context.getString(R.string.msgAtLeastOneAttachmentRequired));
                }
                //Forms
                if (ZConfigManager.MANDATORY_FORMS_REQUIRED && orderTypeFeature.getFeature().equalsIgnoreCase(ZAppSettings.Features.FORMS.getFeatureValue())) {
                    String formType=ZAppSettings.FormAssignmentType.getFormAssignmentType(ZConfigManager.FORM_ASSIGNMENT_TYPE);
                    if(ZCommon.isPredefinedFormVisible(formType)&&getTotalNumUnSubmittedMandatoryForms() > 0)
                        errorMessages.add(context.getString(R.string.msgAllMandatoryFormsAreRequired));
                    if (ZCommon.isManualAssignedFormsVisible(formType)&&getTotalNumUnSubmittedManualMandatoryForms(true) > 0)
                        errorMessages.add(context.getString(R.string.msgAllMandatoryFormsAreRequired));
                }
                //Record Points
                if (ZConfigManager.MPOINT_READING_REQUIRED && orderTypeFeature.getFeature().equalsIgnoreCase(ZAppSettings.Features.RECORDPOINTS.getFeatureValue())) {
                    int totalPoints = getTotalNumMeasurementPoints();
                    int totalReadingTaken = getTotalNumReadingTaken();
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
                    if (!inspectionLotUDAvailable())
                        errorMessages.add(context.getString(R.string.msgInspectionLotDecisionPending, getInspectionLot()));
                }

                //Notifications
                if (orderTypeFeature.getFeature().contains(ZAppSettings.Features.NOTIFICATION.getFeatureValue())) {
                    Notification woNotification = null;
                    ResponseObject response = Notification.getNotifications(ZAppSettings.FetchLevel.Single, ZAppSettings.Hierarchy.HeaderOnly, getNotificationNum(), "", true);
                    if (response != null && !response.isError()) {
                        woNotification = ((ArrayList<Notification>) response.Content()).get(0);
                        if (woNotification != null) {
                            ArrayList<String> notiPreCheckMsgs = woNotification.getPreCompletionMessages(true);
                            if (notiPreCheckMsgs.size() > 0) {
                                errorMessages.addAll(notiPreCheckMsgs);
                            }
                        }
                    }
                }
            }

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

    public int getTotalNumOperations() {
        int intOperationCount = 0;
        ResponseObject responseObject = null;
        String strResPath, strQuery = null;
        Object rawData = null;
        try {
            strResPath = ZCollections.OPR_COLLECTION + "/$count?$filter= (WorkOrderNum eq '" + getWorkOrderNum() + "' and startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true and (SubOperation eq '' or SubOperation eq null))";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.OPR_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intOperationCount = Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intOperationCount;
    }

    public int getTotalNumCompletedOperations() {
        int intOperationCount = 0;
        ResponseObject responseObject = null;
        String strResPath;
        Object rawData = null;
        try {
            strResPath = ZCollections.OPR_COLLECTION + "/$count?$filter= (WorkOrderNum eq '" + getWorkOrderNum() + "' and (startswith(SystemStatus,'" + ZAppSettings.MobileStatus.CONFIRMED.getMobileStatusCode() + "') eq true or MobileStatus eq '" + ZAppSettings.MobileStatus.COMP.getMobileStatusCode() + "' or startswith(UserStatus, '" + ZAppSettings.MobileStatus.COMP.getMobileStatusCode() + "') eq true) and (SubOperation eq '' or SubOperation eq null))";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.OPR_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intOperationCount = Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intOperationCount;
    }

    public int getTotalNumInCompleteOperations() {
        int intOperationCount = 0;
        ResponseObject responseObject = null;
        String strResPath;
        Object rawData = null;
        try {
            strResPath = ZCollections.OPR_COLLECTION + "/$count?$filter= (WorkOrderNum eq '" + getWorkOrderNum() + "' and startswith(SystemStatus,'" + ZAppSettings.MobileStatus.CONFIRMED.getMobileStatusCode() + "') ne true and startswith(SystemStatus,'" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') ne true and (SubOperation eq '' or SubOperation eq null))";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.OPR_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intOperationCount = Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intOperationCount;
    }

    public int getTotalNumComponents() {
        int intComponentsCount = 0;
        ResponseObject responseObject = null;
        String strResPath;
        Object rawData = null;
        try {
            if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED || !ZConfigManager.WO_OP_OBJS_DISPLAY.equalsIgnoreCase("x"))
                strResPath = ZCollections.COMPONENT_COLLECTION + "/$count?$filter= (WorkOrderNum eq '" + getWorkOrderNum() + "' and Deleted ne true)";
            else
                strResPath = ZCollections.COMPONENT_COLLECTION + "/$count?$filter= (WorkOrderNum eq '" + getWorkOrderNum() + "' and OperAct eq '" + getCurrentOperation().getOperationNum() + "' and Deleted ne true)";
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

    public int getTotalNumUnIssuedComponents() {
        int intComponentsCount = 0;
        ResponseObject responseObject = null;
        String strResPath;
        Object rawData = null;
        try {
            if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED || !ZConfigManager.WO_OP_OBJS_DISPLAY.equalsIgnoreCase("x"))
                strResPath = ZCollections.COMPONENT_COLLECTION + "/$count?$filter= (WorkOrderNum eq '" + getWorkOrderNum() + "' and WithdrawalQty eq 0 and ReqmtQty ne 0 and Deleted ne true)";
            else
                strResPath = ZCollections.COMPONENT_COLLECTION + "/$count?$filter= (WorkOrderNum eq '" + getWorkOrderNum() + "' and OperAct eq '" + getCurrentOperation().getOperationNum() + "' and WithdrawalQty eq 0 and ReqmtQty ne 0 and Deleted ne true)";
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

    public int getTotalNumPartialIssuedComponents() {
        int intComponentsCount = 0;
        ResponseObject responseObject = null;
        String strResPath;
        Object rawData = null;
        try {
            if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED || !ZConfigManager.WO_OP_OBJS_DISPLAY.equalsIgnoreCase("x"))
                strResPath = ZCollections.COMPONENT_COLLECTION + "/$count?$filter= (WorkOrderNum eq '" + getWorkOrderNum() + "' and WithdrawalQty gt 0 and WithdrawalQty lt ReqmtQty and Deleted ne true)";
            else
                strResPath = ZCollections.COMPONENT_COLLECTION + "/$count?$filter= (WorkOrderNum eq '" + getWorkOrderNum() + "' and OperAct eq '" + getCurrentOperation().getOperationNum() + "' and WithdrawalQty gt 0 and WithdrawalQty lt ReqmtQty and Deleted ne true)";
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

    public int getTotalNumUploadedAttachments() {
        int intAttachmentsCount = 0;
        ResponseObject responseObject = null;
        String strResPath;
        Object rawData = null;
        try {
            strResPath = ZCollections.WO_ATTACHMENT_CONTENT_UPLOAD_COLLECTION + "/$count?$filter= (WorkOrderNum eq '" + getWorkOrderNum() + "' and BINARY_FLG ne 'N')";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.WO_ATTACHMENT_CONTENT_UPLOAD_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intAttachmentsCount = Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intAttachmentsCount;
    }

    public int getTotalNumUserUploadedAttachments() {
        int intAttachmentsCount = 0;
        ResponseObject responseObject = null;
        String strResPath;
        Object rawData = null;
        try {
            intAttachmentsCount = getTotalNumUploadedAttachments();
            strResPath = ZCollections.WO_ATTACHMENT_COLLECTION + "/$count?$filter=(endswith(ObjectKey, '" + getWorkOrderNum() + "') eq true and tolower(EnteredBy) eq '" + ZAppSettings.strUser.toLowerCase() + "')";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.WO_ATTACHMENT_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intAttachmentsCount += Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intAttachmentsCount;
    }

    public int getTotalNumAttachments() {
        int intAttachmentsCount = 0;
        ResponseObject responseObject = null;
        String strResPath;
        Object rawData = null;
        try {
            intAttachmentsCount = getTotalNumUploadedAttachments();
            strResPath = ZCollections.WO_ATTACHMENT_COLLECTION + "/$count?$filter=(endswith(ObjectKey, '" + getWorkOrderNum() + "') eq true)";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.WO_ATTACHMENT_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intAttachmentsCount += Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intAttachmentsCount;
    }

    /*get the count of the total assigned forms based on Form Assignment type
    * */

    public int getTotalNumForms() {
        int formsCount = 0;
        ResponseObject responseObject = new ResponseObject(ConfigManager.Status.Error);
        String strResPath = "";
        Object rawData = null;
        try {
            String formAssignType = ZAppSettings.FormAssignmentType.getFormAssignmentType(ZConfigManager.FORM_ASSIGNMENT_TYPE);
            String orderType, controlKey, equipCat, funcLocCat,taskListType,group,groupCounter,internalCounter;
            switch (formAssignType) {
                case "1":
                    orderType = WorkOrder.getCurrWo().getOrderType();
                    strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter=(OrderType eq '" + orderType + "' and ControlKey eq '' and TaskListType eq '' and Group eq '' and GroupCounter eq '' and InternalCounter eq '')";
                    break;
                case "2":
                    controlKey = ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED ? WorkOrder.getCurrWo().getCurrentOperation().getControlKey() : "";
                    orderType = WorkOrder.getCurrWo().getOrderType();
                    strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter=(OrderType eq '" + orderType + "' and ControlKey eq '" + controlKey + "' and TaskListType eq '' and Group eq '' and GroupCounter eq '' and InternalCounter eq '')";
                    break;
                case "3":
                    equipCat = ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED ? WorkOrder.getCurrWo().getCurrentOperation().getEquipCategory().isEmpty() ? WorkOrder.getCurrWo().getEquipCategory() : WorkOrder.getCurrWo().getCurrentOperation().getEquipCategory() : WorkOrder.getCurrWo().getEquipCategory();
                    strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter=(EquipCategory eq '" + equipCat + "' and OrderType eq '' and ControlKey eq '' and TaskListType eq '' and Group eq '' and GroupCounter eq '' and InternalCounter eq '')";
                    break;
                case "4":
                    funcLocCat = ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED ? WorkOrder.getCurrWo().getCurrentOperation().getFuncLocCategory().isEmpty() ? WorkOrder.getCurrWo().getFuncLocCategory() : WorkOrder.getCurrWo().getCurrentOperation().getFuncLocCategory() : WorkOrder.getCurrWo().getFuncLocCategory();
                    strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter=(FuncLocCategory eq '" + funcLocCat + "' and OrderType eq '' and ControlKey eq '' and TaskListType eq '' and Group eq '' and GroupCounter eq '' and InternalCounter eq '')";
                    break;
                case "5":
                    if(ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED){
                        orderType = WorkOrder.getCurrWo().getCurrentOperation().getOrderType();
                        controlKey = WorkOrder.getCurrWo().getCurrentOperation().getControlKey();
                        taskListType = WorkOrder.getCurrWo().getCurrentOperation().getTaskListType();
                        group = WorkOrder.getCurrWo().getCurrentOperation().getGroup();
                        groupCounter = WorkOrder.getCurrWo().getCurrentOperation().getGroupCounter();
                        internalCounter = WorkOrder.getCurrWo().getCurrentOperation().getInternalCounter();
                        strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter= (OrderType eq '" + orderType + "' and ControlKey eq '" + controlKey + "' and TaskListType eq '" + taskListType + "' and Group eq '" + group + "' and GroupCounter eq '" + groupCounter + "' and InternalCounter eq '" + internalCounter + "')";
                        break;
                    }
                    else {
                        ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, WorkOrder.getCurrWo().getWorkOrderNum());
                        ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                        for (Operation operation : totalOperations) {
                            orderType = operation.getOrderType();
                            controlKey = operation.getControlKey();
                            taskListType = operation.getTaskListType();
                            group = operation.getGroup();
                            groupCounter = operation.getGroupCounter();
                            internalCounter = operation.getInternalCounter();
                            strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter= (OrderType eq '" + orderType + "' and ControlKey eq '" + controlKey + "' and TaskListType eq '" + taskListType + "' and Group eq '" + group + "' and GroupCounter eq '" + groupCounter + "' and InternalCounter eq '" + internalCounter + "')";
                            responseObject = DataHelper.getInstance().getEntities(ZCollections.FORM_ASSIGNMENT_COLLECTION, strResPath);
                            if (!responseObject.isError()) {
                                rawData = responseObject.Content();
                                formsCount+= Integer.parseInt(rawData.toString());
                            }
                        }
                        return formsCount;
                    }
                default:
                    strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter=(OrderType eq '" + WorkOrder.getCurrWo().getOrderType() + "' and ControlKey eq '' and TaskListType eq '' and Group eq '' and GroupCounter eq '' and InternalCounter eq '')";
                    break;
            }
            /*if(!WorkOrder.getCurrWo().getEquipCategory().isEmpty()&&ZConfigManager.FORM_ASSIGNMENT_TYPE.equals(ZAppSettings.FormAssignmentType.Equipment.Value))
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter=EquipCategory eq '" + WorkOrder.getCurrWo().getEquipCategory() + "'&$orderby=FlowSequence asc,Mandatory desc";
            else if(!WorkOrder.getCurrWo().getFuncLocCategory().isEmpty()&&ZConfigManager.FORM_ASSIGNMENT_TYPE.equals(ZAppSettings.FormAssignmentType.FuncLoc.Value))
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter=FuncLocCategory eq '" + WorkOrder.getCurrWo().getFuncLocCategory() + "'&$orderby=FlowSequence asc,Mandatory desc";

            else if((!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED || !ZConfigManager.WO_OP_OBJS_DISPLAY.equalsIgnoreCase("x"))&&ZConfigManager.FORM_ASSIGNMENT_TYPE.equals(ZAppSettings.FormAssignmentType.WorkOrderLevel.Value))
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter= (ControlKey eq '' and OrderType eq '" + WorkOrder.getCurrWo().getOrderType() + "')";
            else if (ZConfigManager.FORM_ASSIGNMENT_TYPE.equals(ZAppSettings.FormAssignmentType.OperationLevel.Value))
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter= (ControlKey eq '" + WorkOrder.getCurrWo().getCurrentOperation().getControlKey() + "' and OrderType eq '" + WorkOrder.getCurrWo().getCurrentOperation().getOrderType() + "')";*/

            //if (formsCount == 0)
            responseObject = DataHelper.getInstance().getEntities(ZCollections.FORM_ASSIGNMENT_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                formsCount = Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return formsCount;
    }

    /** get the count of the total approved and rejected Predefined forms count based on Form Assignment type
     * @param formType
     * @return
     */
    public HashMap<String,Integer> getTotalNumOfPredefinedApprovedandRejectedForms(String formType) {
        HashMap<String,Integer> approverejectforms= null;
        try {
            int predefinedformsApprovedCount = 0,predefinedformsRejectCount = 0;
            approverejectforms = new HashMap<>();
            ArrayList<FormAssignmentSetModel> list;
            //String formType=ZAppSettings.FormAssignmentType.getFormAssignmentType(ZConfigManager.FORM_ASSIGNMENT_TYPE);
            if(ZCommon.isPredefinedFormVisible(formType))
            {
                list=fetchPredefinedForms(WorkOrder.getCurrWo(),formType);
                ResponseObject response = new ResponseObject(ConfigManager.Status.Error);
                String resourcePath = null;
                ResponseMasterModel responseMasterModel=null;
                for(FormAssignmentSetModel formAssignmentSetModel:list){
                    String entitySetName = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION;
                    resourcePath = entitySetName;
                    resourcePath += "?$filter=(tolower(FormID) eq '" + formAssignmentSetModel.getFormID().toLowerCase() + "' and Version eq '" + formAssignmentSetModel.getVersion() + "' and WoNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "'&$orderby=Counter desc)";
                    response = DataHelper.getInstance().getEntities(entitySetName, resourcePath);
                    if (response != null && !response.isError()) {
                        List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                        if (entities != null && entities.size() > 0) {
                            responseMasterModel = new ResponseMasterModel(entities.get(0));
                            ResponseObject result = new ResponseObject(ConfigManager.Status.Error);
                            String resPath = ZCollections.FORM_RESPONSE_APPROVAL_STATUS_ENTITY_SET + "?$filter=FormID eq '" + responseMasterModel.getFormID() + "' and Version eq '" + responseMasterModel.getVersion() + "' and FormInstanceID eq '" + responseMasterModel.getInstanceID() + "' and  Counter eq '" + responseMasterModel.getCounter() + "'";
                            result = DataHelper.getInstance().getEntities(ZCollections.FORM_RESPONSE_APPROVAL_STATUS_ENTITY_SET, resPath);
                            if(result!=null&&!result.isError()){
                                List<ODataEntity> entitis = ZBaseEntity.setODataEntityList(result.Content());
                                for (ODataEntity entity : entitis) {
                                    ZODataEntity  zoDataEntity = new ZODataEntity(entity);
                                    FormResponseApprovalStatus formResponseApprovalStatus=new FormResponseApprovalStatus(zoDataEntity);
                                    if(formResponseApprovalStatus.getFormContentStatus().equalsIgnoreCase("APPROVE"))
                                        predefinedformsApprovedCount++;
                                    else
                                        predefinedformsRejectCount++;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            approverejectforms.put("APPROVE",predefinedformsApprovedCount);
            approverejectforms.put("REJECT",predefinedformsRejectCount);
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

        return approverejectforms;
    }

    /** get the count of the total approved and rejected Manual forms count based on Form Assignment type
     * @param formType
     * @return
     */
    public HashMap<String,Integer> getTotalNumOfManualApprovedandRejectedForms(String formType) {
        HashMap<String,Integer> approverejectforms= null;
        try {
            int predefinedformsApprovedCount = 0,predefinedformsRejectCount = 0;
            approverejectforms = new HashMap<>();
            ArrayList<ManualFormAssignmentSetModel> list;
            if(ZCommon.isManualAssignedFormsVisible(formType)){
                list=fetchManulaForms(formType);
                ResponseObject response = new ResponseObject(ConfigManager.Status.Error);
                String resourcePath = null;
                ResponseMasterModel responseMasterModel=null;
                for(ManualFormAssignmentSetModel manualFormAssignmentSetModel:list){
                    String entitySetName = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION;
                    resourcePath = entitySetName;
                    if (ZAppSettings.FormAssignmentType.getFormAssignmentType(ZConfigManager.FORM_ASSIGNMENT_TYPE).equals(ZAppSettings.FormAssignmentType.ManualAssignmentWO.Value))
                        resourcePath += "?$filter=(tolower(FormID) eq '" + manualFormAssignmentSetModel.getFormID().toLowerCase() + "' and Version eq '" + manualFormAssignmentSetModel.getVersion() + "' and WoNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "'  and OperationNum eq '" + WorkOrder.getCurrWo().getCurrentOperation().getOperationNum() + "'&$orderby=Counter desc)";
                    else
                        resourcePath += "?$filter=(tolower(FormID) eq '" + manualFormAssignmentSetModel.getFormID().toLowerCase() + "' and Version eq '" + manualFormAssignmentSetModel.getVersion() + "' and WoNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "'&$orderby=Counter desc)";
                    response = DataHelper.getInstance().getEntities(entitySetName, resourcePath);
                    if (response != null && !response.isError()) {
                        List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                        if (entities != null && entities.size() > 0) {
                            responseMasterModel = new ResponseMasterModel(entities.get(0));
                            ResponseObject result = new ResponseObject(ConfigManager.Status.Error);
                            String resPath = ZCollections.FORM_RESPONSE_APPROVAL_STATUS_ENTITY_SET + "?$filter=FormID eq '" + responseMasterModel.getFormID() + "' and Version eq '" + responseMasterModel.getVersion() + "' and FormInstanceID eq '" + responseMasterModel.getInstanceID() + "' and  Counter eq '" + responseMasterModel.getCounter() + "'";
                            result = DataHelper.getInstance().getEntities(ZCollections.FORM_RESPONSE_APPROVAL_STATUS_ENTITY_SET, resPath);
                            if(result!=null&&!result.isError()){
                                List<ODataEntity> entitis = ZBaseEntity.setODataEntityList(result.Content());
                                for (ODataEntity entity : entitis) {
                                    ZODataEntity  zoDataEntity = new ZODataEntity(entity);
                                    FormResponseApprovalStatus formResponseApprovalStatus=new FormResponseApprovalStatus(zoDataEntity);
                                    if(formResponseApprovalStatus.getFormContentStatus().equalsIgnoreCase("APPROVE"))
                                        predefinedformsApprovedCount++;
                                    else
                                        predefinedformsRejectCount++;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            approverejectforms.put("APPROVE",predefinedformsApprovedCount);
            approverejectforms.put("REJECT",predefinedformsRejectCount);
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return approverejectforms;
    }

    public ArrayList<FormAssignmentSetModel> fetchPredefinedForms(WorkOrder workOrder, String typeValue) {
        ArrayList<FormAssignmentSetModel> predefinedFormlist = null;
        try {
            predefinedFormlist = null;
            try {
                predefinedFormlist = new ArrayList<>();
                if (typeValue.equals(ZAppSettings.FormAssignmentType.WorkOrderLevel.Value)) {
                    predefinedFormlist = FormAssignmentSetModel.getFormAssignmentData_OrderType(workOrder.getOrderType());
                } else if (typeValue.equals(ZAppSettings.FormAssignmentType.OperationLevel.Value)) {
                    if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                        predefinedFormlist.clear();
                        ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, workOrder.getWorkOrderNum());
                        ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                        for (Operation operation : totalOperations) {
                            predefinedFormlist.addAll(FormAssignmentSetModel.getFormAssignmentData_OperationType(operation.getOrderType(), operation.getControlKey()));
                            }
                    } else {
                        predefinedFormlist = FormAssignmentSetModel.getFormAssignmentData_OperationType(workOrder.getOrderType(), workOrder.getCurrentOperation().getControlKey());
                    }
                } else if (typeValue.equals(ZAppSettings.FormAssignmentType.Equipment.Value)) {
                    String equipmentCat="";
                    if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)
                        equipmentCat = workOrder.getEquipCategory();
                    else
                        equipmentCat = workOrder.getCurrentOperation().getEquipCategory().isEmpty() ? workOrder.getEquipCategory() : workOrder.getCurrentOperation().getEquipCategory();
                    predefinedFormlist = FormAssignmentSetModel.getFormAssignmentData_EquipmentType(equipmentCat);
                } else if (typeValue.equals(ZAppSettings.FormAssignmentType.FuncLoc.Value)) {
                    String funcLocCat="";
                    if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)
                        funcLocCat = workOrder.getFuncLocCategory();
                    else
                        funcLocCat = workOrder.getCurrentOperation().getFuncLocCategory().isEmpty() ? workOrder.getFuncLocCategory() : workOrder.getCurrentOperation().getFuncLocCategory();
                    predefinedFormlist = FormAssignmentSetModel.getFormAssignmentData_FunctionalLocType(funcLocCat);
                } else if (typeValue.equals(ZAppSettings.FormAssignmentType.TaskListType.Value)) {
                    if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                        ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, workOrder.getWorkOrderNum());
                        ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                        for (Operation operation : totalOperations) {
                            predefinedFormlist = FormAssignmentSetModel.getFormAssignmentData_TaskListType(operation.getOrderType(), operation.getControlKey(), operation.getTaskListType(), operation.getGroup(), operation.getGroupCounter(), operation.getInternalCounter());
                        }
                    } else {
                        predefinedFormlist = FormAssignmentSetModel.getFormAssignmentData_TaskListType(workOrder.getCurrentOperation().getOrderType(), workOrder.getCurrentOperation().getControlKey(), workOrder.getCurrentOperation().getTaskListType(), workOrder.getCurrentOperation().getGroup(), workOrder.getCurrentOperation().getGroupCounter(), workOrder.getCurrentOperation().getInternalCounter());
                    }
                } else if (typeValue.equals(ZAppSettings.FormAssignmentType.None.Value)) {
                    if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)
                        predefinedFormlist = FormAssignmentSetModel.getFormAssignmentData_OrderType(workOrder.getOrderType());
                    else
                        predefinedFormlist = FormAssignmentSetModel.getFormAssignmentData_OperationType(workOrder.getOrderType(), workOrder.getCurrentOperation().getControlKey());
                }
            } catch (Exception e) {
                DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return predefinedFormlist;
    }

    public ArrayList<ManualFormAssignmentSetModel> fetchManulaForms(String formType) {
        ArrayList<ManualFormAssignmentSetModel> manualFormlist = null;
        try {
            manualFormlist = new ArrayList<>();
            ResponseObject responseObject = null;
            String strResPath = "";
            Object rawData = null;

            switch (formType) {
                case "6":
                    strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '')";
                    break;
                case "7":
                    if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                        manualFormlist.clear();
                        ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, WorkOrder.getCurrWo().getWorkOrderNum());
                        ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                        for (Operation operation : totalOperations) {
                            strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (WorkOrderNum eq '" + operation.getWorkOrderNum() + "' and OprNum eq '" + operation.getOperationNum() + "')";
                            responseObject = ManualFormAssignmentSetModel.getObjectsFromEntity(ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET, strResPath);
                            manualFormlist.addAll((ArrayList<ManualFormAssignmentSetModel>)responseObject.Content());
                        }
                        return manualFormlist;
                    }
                    else {
                        strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '" + WorkOrder.getCurrWo().getCurrentOperation().getOperationNum() + "')";
                        break;
                    }

                case "8":
                    strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (Equipment eq '" + WorkOrder.getCurrWo().getEquipNum() + "')";
                    break;
                case "9":
                    strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (FunctionalLocation eq '" + WorkOrder.getCurrWo().getFuncLocation() + "')";
                    break;
                case "10":
                    if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                        manualFormlist.clear();
                        ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, WorkOrder.getCurrWo().getWorkOrderNum());
                        ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                        for (Operation operation : totalOperations) {
                            strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (WorkOrderNum eq '" + operation.getWorkOrderNum() + "' and OprNum eq '" + operation.getOperationNum() + "')";
                            responseObject = ManualFormAssignmentSetModel.getObjectsFromEntity(ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET, strResPath);
                            manualFormlist.addAll((ArrayList<ManualFormAssignmentSetModel>)responseObject.Content());
                        }
                        return manualFormlist;
                    }
                    else {
                        strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '" + WorkOrder.getCurrWo().getCurrentOperation().getOperationNum() + "')";
                        break;
                    }
                default:
                    strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '')";
                    break;
            }
            responseObject = ManualFormAssignmentSetModel.getObjectsFromEntity(ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET, strResPath);
            manualFormlist.addAll((ArrayList<ManualFormAssignmentSetModel>)responseObject.Content());
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return manualFormlist;
    }
/* get the count of the un-submitted Manadatory forms based on Form Assignment type
* */
    public int getTotalNumUnSubmittedMandatoryForms() {
        int unSubmittedFormsCount = 0;
        ResponseObject responseObject = null;
        String strResPath = "";
        Object rawData = null;
        try {
           // strResPath = getFormResPath(true);
            responseObject=getFormEntities(true);
           // responseObject = FormAssignmentSetModel.getObjectsFromEntity(ZCollections.FORM_ASSIGNMENT_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                ArrayList<FormAssignmentSetModel> forms = (ArrayList<FormAssignmentSetModel>) rawData;
                if (forms != null && forms.size() > 0) {
                    unSubmittedFormsCount = forms.size();
                    for (FormAssignmentSetModel form : forms) {
                        if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED && ZConfigManager.WO_OP_OBJS_DISPLAY.equalsIgnoreCase("x"))
                            strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + getWorkOrderNum() + "' and OperationNum eq '" + WorkOrder.getCurrWo().getCurrentOperation().getOperationNum() + "')";
                        else
                            strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + getWorkOrderNum() + "')";
                        responseObject = DataHelper.getInstance().getEntities(ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION, strResPath);
                        if (!responseObject.isError()) {
                            rawData = responseObject.Content();
                            if (Integer.parseInt(rawData.toString()) > 0) {
                                unSubmittedFormsCount--;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return unSubmittedFormsCount;
    }
/* getting the forms data for manadatory check based on Form Assignment type
* */
    private ResponseObject getFormEntities(boolean mandatoryFormChk) {
        ResponseObject responseObject = null;
        String formAssignType = ZAppSettings.FormAssignmentType.getFormAssignmentType(ZConfigManager.FORM_ASSIGNMENT_TYPE);
        String strResPath = "";
        String strMandatoryChk="";
        Object rawData = null;
        if(mandatoryFormChk)
            strMandatoryChk=" eq 'x'";
        else
            strMandatoryChk=" ne 'x'";

        String orderType, controlKey, equipCat, funcLocCat,taskListType,group,groupCounter,internalCounter;
        switch (formAssignType) {
            case "1":
                orderType = WorkOrder.getCurrWo().getOrderType();
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter= (OrderType eq '" + orderType + "' and ControlKey eq '' and TaskListType eq '' and Group eq '' and GroupCounter eq '' and InternalCounter eq '' and tolower(Mandatory)"+strMandatoryChk+")";
                break;
            case "2":
                controlKey = ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED ? WorkOrder.getCurrWo().getCurrentOperation().getControlKey() : "";
                orderType = WorkOrder.getCurrWo().getOrderType();
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter= (OrderType eq '" + orderType + "' and ControlKey eq '" + controlKey + "' and TaskListType eq '' and Group eq '' and GroupCounter eq '' and InternalCounter eq '' and tolower(Mandatory)"+strMandatoryChk+")";
                break;
            case "3":
                equipCat = ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED ? WorkOrder.getCurrWo().getCurrentOperation().getEquipCategory().isEmpty() ? WorkOrder.getCurrWo().getEquipCategory() : WorkOrder.getCurrWo().getCurrentOperation().getEquipCategory() : WorkOrder.getCurrWo().getEquipCategory();
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter= (EquipCategory eq '" + equipCat + "' and OrderType eq '' and ControlKey eq '' and TaskListType eq '' and Group eq '' and GroupCounter eq '' and InternalCounter eq '' and tolower(Mandatory)"+strMandatoryChk+")";
                break;
            case "4":
                funcLocCat = ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED ? WorkOrder.getCurrWo().getCurrentOperation().getFuncLocCategory().isEmpty() ? WorkOrder.getCurrWo().getFuncLocCategory() : WorkOrder.getCurrWo().getCurrentOperation().getFuncLocCategory() : WorkOrder.getCurrWo().getFuncLocCategory();
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter= (FuncLocCategory eq '" + funcLocCat + "' and OrderType eq '' and ControlKey eq '' and TaskListType eq '' and Group eq '' and GroupCounter eq '' and InternalCounter eq '' and tolower(Mandatory)"+strMandatoryChk+")";
                break;
            case "5":
                if(ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED){
                    orderType = WorkOrder.getCurrWo().getCurrentOperation().getOrderType();
                    controlKey = WorkOrder.getCurrWo().getCurrentOperation().getControlKey();
                    taskListType = WorkOrder.getCurrWo().getCurrentOperation().getTaskListType();
                    group = WorkOrder.getCurrWo().getCurrentOperation().getGroup();
                    groupCounter = WorkOrder.getCurrWo().getCurrentOperation().getGroupCounter();
                    internalCounter = WorkOrder.getCurrWo().getCurrentOperation().getInternalCounter();
                    strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter= (OrderType eq '" + orderType + "' and ControlKey eq '" + controlKey + "' and TaskListType eq '" + taskListType + "' and Group eq '" + group + "' and GroupCounter eq '" + groupCounter + "' and InternalCounter eq '" + internalCounter + "' and tolower(Mandatory)"+strMandatoryChk+")";
                    break;
                }
                else {
                    ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, WorkOrder.getCurrWo().getWorkOrderNum());
                    ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                    ArrayList<FormAssignmentSetModel> contentList = new ArrayList<FormAssignmentSetModel>();
                    for (Operation operation : totalOperations) {
                        orderType = operation.getOrderType();
                        controlKey = operation.getControlKey();
                        taskListType = operation.getTaskListType();
                        group = operation.getGroup();
                        groupCounter = operation.getGroupCounter();
                        internalCounter = operation.getInternalCounter();
                        strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter= (OrderType eq '" + orderType + "' and ControlKey eq '" + controlKey + "' and TaskListType eq '" + taskListType + "' and Group eq '" + group + "' and GroupCounter eq '" + groupCounter + "' and InternalCounter eq '" + internalCounter + "' and tolower(Mandatory)" + strMandatoryChk + ")";
                        responseObject = FormAssignmentSetModel.getObjectsFromEntity(ZCollections.FORM_ASSIGNMENT_COLLECTION, strResPath);
                        if (!responseObject.isError()) {
                            rawData = responseObject.Content();
                            //contentList = (ArrayList<FormAssignmentSetModel>) rawData;
                            if (rawData != null) {
                                contentList.addAll((ArrayList<FormAssignmentSetModel>) rawData);
                            }
                        }
                    }
                    responseObject.setContent(contentList);
                    return responseObject;
                }
            default:
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "?$filter= (OrderType eq '" + WorkOrder.getCurrWo().getOrderType() + "' and ControlKey eq '' and TaskListType eq '' and Group eq '' and GroupCounter eq '' and InternalCounter eq '' and tolower(Mandatory)"+strMandatoryChk+")";
                break;
        }
        responseObject = FormAssignmentSetModel.getObjectsFromEntity(ZCollections.FORM_ASSIGNMENT_COLLECTION, strResPath);
        return responseObject;
    }
    /* get the count of the un-submitted Manula Manadatory forms based on Form Assignment type
     * */
    public int getTotalNumUnSubmittedManualMandatoryForms(boolean mandatoryFormChk) {
        int unSubmittedFormsCount = 0;
        ResponseObject responseObject = null;
        String strResPath = "";
        Object rawData = null;
        try {
            responseObject=getManualFormEntities(mandatoryFormChk);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                ArrayList<ManualFormAssignmentSetModel> forms = (ArrayList<ManualFormAssignmentSetModel>) rawData;
                if (forms != null && forms.size() > 0) {
                    unSubmittedFormsCount = forms.size();
                    for (ManualFormAssignmentSetModel form : forms) {
                        if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED && ZConfigManager.WO_OP_OBJS_DISPLAY.equalsIgnoreCase("x"))
                            strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + getWorkOrderNum() + "' and OperationNum eq '" + WorkOrder.getCurrWo().getCurrentOperation().getOperationNum() + "')";
                        else
                            strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + getWorkOrderNum() + "')";
                        responseObject = DataHelper.getInstance().getEntities(ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION, strResPath);
                        if (!responseObject.isError()) {
                            rawData = responseObject.Content();
                            if (Integer.parseInt(rawData.toString()) > 0) {
                                unSubmittedFormsCount--;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return unSubmittedFormsCount;
    }
    /* getting the forms data for  Manual manadatory check based on Form Assignment type
     * */
    private ResponseObject getManualFormEntities(boolean mandatoryFormChk) {
        ResponseObject responseObject = null;
        String formAssignType = ZAppSettings.FormAssignmentType.getFormAssignmentType(ZConfigManager.FORM_ASSIGNMENT_TYPE);
        String strResPath = "";
        String strMandatoryChk="";
        Object rawData = null;
        if (mandatoryFormChk)
            strMandatoryChk = " eq 'x'";
        else
            strMandatoryChk = " ne 'x'";

        switch (formAssignType) {
            case "6":
                strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '' and tolower(Mandatory)" + strMandatoryChk + ")";
                break;
            case "7":
                strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '" + WorkOrder.getCurrWo().getCurrentOperation().getOperationNum() + "' and tolower(Mandatory)" + strMandatoryChk + ")";
                break;
            case "8":
                strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (Equipment eq '" + WorkOrder.getCurrWo().getEquipNum() + "' and tolower(Mandatory)" + strMandatoryChk + ")";
                break;
            case "9":
                strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (FunctionalLocation eq '" + WorkOrder.getCurrWo().getFuncLocation() + "' and tolower(Mandatory)" + strMandatoryChk + ")";
                break;
            default:
                strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "?$filter= (WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '' and tolower(Mandatory)" + strMandatoryChk + ")";
                break;
        }
        responseObject = ManualFormAssignmentSetModel.getObjectsFromEntity(ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET, strResPath);
        return responseObject;
    }

    /** getting total count of  predefined form entities
     * @return
     */
    public int getTotalPredefinedFormEntitiesCount() {
        int totpredefinedFormsCount = 0;
        ResponseObject responseObject = null;
        String formAssignType = ZAppSettings.FormAssignmentType.getFormAssignmentType(ZConfigManager.FORM_ASSIGNMENT_TYPE);
        String strResPath = "";
        Object rawData = null;

        String orderType, controlKey, equipCat, funcLocCat,taskListType,group,groupCounter,internalCounter;
        switch (formAssignType) {
            case "1":
                orderType = WorkOrder.getCurrWo().getOrderType();
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter= (OrderType eq '" + orderType + "' and ControlKey eq '' and TaskListType eq '' and Group eq '' and GroupCounter eq '' and InternalCounter eq '')";
                break;
            case "2":
                controlKey = ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED ? WorkOrder.getCurrWo().getCurrentOperation().getControlKey() : "";
                orderType = WorkOrder.getCurrWo().getOrderType();
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter= (OrderType eq '" + orderType + "' and ControlKey eq '" + controlKey + "' and TaskListType eq '' and Group eq '' and GroupCounter eq '' and InternalCounter eq '')";
                break;
            case "3":
                equipCat = ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED ? WorkOrder.getCurrWo().getCurrentOperation().getEquipCategory().isEmpty() ? WorkOrder.getCurrWo().getEquipCategory() : WorkOrder.getCurrWo().getCurrentOperation().getEquipCategory() : WorkOrder.getCurrWo().getEquipCategory();
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter= (EquipCategory eq '" + equipCat + "' and OrderType eq '' and ControlKey eq '' and TaskListType eq '' and Group eq '' and GroupCounter eq '' and InternalCounter eq '')";
                break;
            case "4":
                funcLocCat = ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED ? WorkOrder.getCurrWo().getCurrentOperation().getFuncLocCategory().isEmpty() ? WorkOrder.getCurrWo().getFuncLocCategory() : WorkOrder.getCurrWo().getCurrentOperation().getFuncLocCategory() : WorkOrder.getCurrWo().getFuncLocCategory();
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter= (FuncLocCategory eq '" + funcLocCat + "' and OrderType eq '' and ControlKey eq '' and TaskListType eq '' and Group eq '' and GroupCounter eq '' and InternalCounter eq '')";
                break;
            case "5":
                if(ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED){
                    orderType = WorkOrder.getCurrWo().getCurrentOperation().getOrderType();
                    controlKey = WorkOrder.getCurrWo().getCurrentOperation().getControlKey();
                    taskListType = WorkOrder.getCurrWo().getCurrentOperation().getTaskListType();
                    group = WorkOrder.getCurrWo().getCurrentOperation().getGroup();
                    groupCounter = WorkOrder.getCurrWo().getCurrentOperation().getGroupCounter();
                    internalCounter = WorkOrder.getCurrWo().getCurrentOperation().getInternalCounter();
                    strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter= (OrderType eq '" + orderType + "' and ControlKey eq '" + controlKey + "' and TaskListType eq '" + taskListType + "' and Group eq '" + group + "' and GroupCounter eq '" + groupCounter + "' and InternalCounter eq '" + internalCounter + "')";
                    break;
                }
                else {
                    ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, WorkOrder.getCurrWo().getWorkOrderNum());
                    ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                    ArrayList<FormAssignmentSetModel> contentList = new ArrayList<FormAssignmentSetModel>();
                    for (Operation operation : totalOperations) {
                        orderType = operation.getOrderType();
                        controlKey = operation.getControlKey();
                        taskListType = operation.getTaskListType();
                        group = operation.getGroup();
                        groupCounter = operation.getGroupCounter();
                        internalCounter = operation.getInternalCounter();
                        strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter= (OrderType eq '" + orderType + "' and ControlKey eq '" + controlKey + "' and TaskListType eq '" + taskListType + "' and Group eq '" + group + "' and GroupCounter eq '" + groupCounter + "' and InternalCounter eq '" + internalCounter + "')";
                        responseObject = FormAssignmentSetModel.getObjectsFromEntity(ZCollections.FORM_ASSIGNMENT_COLLECTION, strResPath);
                        if (!responseObject.isError()) {
                            rawData = responseObject.Content();
                            if (rawData != null) {
                                totpredefinedFormsCount += Integer.valueOf(String.valueOf(rawData));
                            }
                        }
                    }
                    return totpredefinedFormsCount;
                }
            default:
                strResPath = ZCollections.FORM_ASSIGNMENT_COLLECTION + "/$count?$filter= (OrderType eq '" + WorkOrder.getCurrWo().getOrderType() + "' and ControlKey eq '' and TaskListType eq '' and Group eq '' and GroupCounter eq '' and InternalCounter eq '')";
                break;
        }
        responseObject = FormAssignmentSetModel.getObjectsFromEntity(ZCollections.FORM_ASSIGNMENT_COLLECTION, strResPath);
        if (!responseObject.isError()) {
            rawData = responseObject.Content();
            totpredefinedFormsCount = Integer.valueOf(String.valueOf(rawData));
        }
        return totpredefinedFormsCount;
    }

    /** getting total count of  manual form entities
     * @return
     */
    public int getTotalManualFormEntitiesCount() {
        int totManualFormsCount = 0;
        try {
            ResponseObject responseObject=null;
            totManualFormsCount = 0;
            String formAssignType = ZAppSettings.FormAssignmentType.getFormAssignmentType(ZConfigManager.FORM_ASSIGNMENT_TYPE);
            String strResPath = "";
            Object rawData = null;
            String oprNum="";

            switch (formAssignType) {
                case "6":
                    strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "/$count?$filter= (WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '')";
                    break;
                case "7":
                    if(ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED){
                        strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "/$count?$filter= (WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '" + WorkOrder.getCurrWo().getCurrentOperation().getOperationNum() + "')";
                        break;
                    }
                    else {
                        ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, WorkOrder.getCurrWo().getWorkOrderNum());
                        ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                        for (Operation operation : totalOperations) {
                            strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "/$count?$filter= (WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '" + operation.getOperationNum() + "')";
                            responseObject = ManualFormAssignmentSetModel.getObjectsFromEntity(ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET, strResPath);
                            if (!responseObject.isError()) {
                                rawData = responseObject.Content();
                                if (rawData != null) {
                                    totManualFormsCount += Integer.valueOf(String.valueOf(rawData));
                                }
                            }
                        }
                        return totManualFormsCount;
                    }
                case "8":
                    strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "/$count?$filter= (Equipment eq '" + WorkOrder.getCurrWo().getEquipNum() + "')";
                    break;
                case "9":
                    strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "/$count?$filter= (FunctionalLocation eq '" + WorkOrder.getCurrWo().getFuncLocation() + "')";
                    break;
                case "10":
                    if(ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED){
                        strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "/$count?$filter= (WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '" + WorkOrder.getCurrWo().getCurrentOperation().getOperationNum() + "')";
                        break;
                    }
                    else {
                        ResponseObject result = Operation.getAllWorkOrderOperations(ZAppSettings.FetchLevel.List, WorkOrder.getCurrWo().getWorkOrderNum());
                        ArrayList<Operation> totalOperations = (ArrayList<Operation>) result.Content();
                        for (Operation operation : totalOperations) {
                            strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "/$count?$filter= (WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '" + operation.getOperationNum() + "')";
                            responseObject = ManualFormAssignmentSetModel.getObjectsFromEntity(ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET, strResPath);
                            if (!responseObject.isError()) {
                                rawData = responseObject.Content();
                                if (rawData != null) {
                                    totManualFormsCount += Integer.valueOf(String.valueOf(rawData));
                                }
                            }
                        }
                        return totManualFormsCount;
                    }
                default:
                    strResPath = ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET + "/$count?$filter= (WorkOrderNum eq '" + WorkOrder.getCurrWo().getWorkOrderNum() + "' and OprNum eq '')";
                    break;
            }
            responseObject = ManualFormAssignmentSetModel.getObjectsFromEntity(ZCollections.FORM_MANUAL_ASSIGNMENT_ENTITY_SET, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                totManualFormsCount = Integer.valueOf(String.valueOf(rawData));
            }
        } catch (NumberFormatException e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return totManualFormsCount;
    }
    /* get the count of the un-submitted Optional forms based on Form Assignment type
     * */
    public int getTotalNumUnSubmittedOptionalForms() {
        int unSubmittedFormsCount = 0;
        ResponseObject responseObject = null;
        String strResPath = "";
        Object rawData = null;
        try {
            //strResPath = getFormResPath(false);
            responseObject =getFormEntities(false);
            //responseObject = FormAssignmentSetModel.getObjectsFromEntity(ZCollections.FORM_ASSIGNMENT_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                ArrayList<FormAssignmentSetModel> forms = (ArrayList<FormAssignmentSetModel>) rawData;
                if (forms != null && forms.size() > 0) {
                    unSubmittedFormsCount = forms.size();
                    for (FormAssignmentSetModel form : forms) {
                        if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED && ZConfigManager.WO_OP_OBJS_DISPLAY.equalsIgnoreCase("x"))
                            strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + getWorkOrderNum() + "' and OperationNum eq '" + WorkOrder.getCurrWo().getCurrentOperation().getOperationNum() + "')";
                        else
                            strResPath = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION + "/$count?$filter= (tolower(FormID) eq '" + form.getFormID().toLowerCase() + "' and Version eq '" + form.getVersion() + "' and WoNum eq '" + getWorkOrderNum() + "')";
                        responseObject = DataHelper.getInstance().getEntities(ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION, strResPath);
                        if (!responseObject.isError()) {
                            rawData = responseObject.Content();
                            if (Integer.parseInt(rawData.toString()) > 0) {
                                unSubmittedFormsCount--;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return unSubmittedFormsCount;
    }

    public int getTotalNumReadingTaken() {
        int readingCount = 0;
        ResponseObject responseObject = null;
        String strResPath;
        Object rawData = null;
        String woObjNum;
        try {
            if (!getObjectNumber().isEmpty())
                woObjNum = getObjectNumber();
            else
                woObjNum = getWorkOrderNum();
            strResPath = ZCollections.MEASPOINT_READING_COLLECTION + "/$count?$filter= (WOObjectNum eq '" + woObjNum + "')";
            responseObject = DataHelper.getInstance().getEntities(ZCollections.MEASPOINT_READING_COLLECTION, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                readingCount = Integer.valueOf(String.valueOf(rawData));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return readingCount;
    }

    public boolean inspectionLotUDAvailable() {
        try {
            if (getInspectionLot() != null && !getInspectionLot().isEmpty()) {
                ArrayList<com.ods.myjobcard_library.entities.qmentities.InspectionLot> lots = com.ods.myjobcard_library.entities.qmentities.InspectionLot.getInspectionLot(getWorkOrderNum());
                if (lots != null && lots.size() > 0) {
                    com.ods.myjobcard_library.entities.qmentities.InspectionLot lot = lots.get(0);
                    return lot.getUdCode() != null && !lot.getUdCode().isEmpty();
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return false;
    }

    /**
     * Getting the count of total number of inspection characteristics.
     *
     * @param inspectionLot   Inspection Lot Number.
     * @param operationNumber Operation Number in case of Operation Assignment.
     * @return Count of Inspection Characteristics.
     */
    public int getTotalInspectionCharacteristicsCount(String inspectionLot, @Nullable String operationNumber) {
        int totalNumber = 0;
        ResponseObject responseObject = null;
        try {
            String entitySetName = ZCollections.WO_INSPECTIONLOT_CHARACTERISTIC_ENTITY_COLLECTION;
            String resPath = entitySetName + "/$count?$filter=(InspLot eq '" + inspectionLot + "')";
            if (operationNumber != null)
                resPath = entitySetName + "/$count?$filter=(InspLot eq '" + inspectionLot + "' and InspOper eq '" + operationNumber + "')";
            responseObject = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (!responseObject.isError()) {
                totalNumber = Integer.parseInt(String.valueOf(responseObject.Content()));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return totalNumber;
    }

    /**
     * Getting the count of total saved inspection characteristics.
     *
     * @param inspectionLot   Inspection Lot Number.
     * @param operationNumber Operation Number in case of Operation Assignment.
     * @return Count of Inspection Characteristics.
     */
    public int getSavedInspectionCharacteristicsCount(String inspectionLot, @Nullable String operationNumber) {
        int totalNumber = 0;
        ResponseObject responseObject = null;
        try {
            String entitySetName = ZCollections.WO_INSPECTIONLOT_RESULTGET_COLLECTION;
            String resPath = entitySetName + "/$count?$filter=(InspLot eq '" + inspectionLot + "' and (Code1 ne '' or ResValue ne ''))";
            if (operationNumber != null)
                resPath = entitySetName + "/$count?$filter=(InspLot eq '" + inspectionLot + "' and InspOper eq '" + operationNumber + "' and (Code1 ne '' or ResValue ne ''))";
            responseObject = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (!responseObject.isError()) {
                totalNumber = Integer.parseInt(String.valueOf(responseObject.Content()));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return totalNumber;
    }

    public int getTotalNumMeasurementPoints() {
        int mPointNum = 0;
        ResponseObject responseObject = null;
        String strResPath;
        Object rawData = null;
        String equipNum = "", funcLocNum = "";
        try {
            if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                equipNum = WorkOrder.getCurrWo().getEquipNum();
                funcLocNum = WorkOrder.getCurrWo().getFuncLocation();
            } else if (WorkOrder.getCurrWo().getCurrentOperation() != null) {
                equipNum = WorkOrder.getCurrWo().getCurrentOperation().getEquipment();
                funcLocNum = WorkOrder.getCurrWo().getCurrentOperation().getFuncLoc();
            }
            //if(!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
            if ((equipNum != null && !equipNum.isEmpty())) {
                strResPath = ZCollections.MEASPOINT_READING_COLLECTION + "/$count?$filter= (Equipment eq '" + equipNum + "' and OpObjectNumber eq '' and WOObjectNum eq '' and OperationNum  eq '')";
                responseObject = DataHelper.getInstance().getEntities(ZCollections.MEASPOINT_READING_COLLECTION, strResPath);
                if (!responseObject.isError()) {
                    rawData = responseObject.Content();
                    mPointNum += Integer.valueOf(String.valueOf(rawData));
                }
            }
            if ((funcLocNum != null && !funcLocNum.isEmpty())) {
                strResPath = ZCollections.MEASPOINT_READING_COLLECTION + "/$count?$filter= (FunctionalLocation eq '" + funcLocNum + "' and OpObjectNumber eq '' and WOObjectNum eq '' and OperationNum  eq '')";
                responseObject = DataHelper.getInstance().getEntities(ZCollections.MEASPOINT_READING_COLLECTION, strResPath);
                if (!responseObject.isError()) {
                    rawData = responseObject.Content();
                    mPointNum += Integer.valueOf(String.valueOf(rawData));
                }
            } else {
                if (ZConfigManager.ENABLE_OPERATION_MEASUREMENTPOINT_READINGS) {
                    String entitySetName = ZCollections.OPR_COLLECTION;
                    String resPath = entitySetName;
                    resPath += "?$filter=WorkOrderNum eq '" + getWorkOrderNum() + "' and not startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "') and (SubOperation eq '' or SubOperation eq null)&$select=Equipment,FuncLoc";
                    responseObject = DataHelper.getInstance().getEntities(entitySetName, resPath);
                    if (responseObject != null && !responseObject.isError()) {
                        List<ODataEntity> oprEntities = (List<ODataEntity>) responseObject.Content();
                        if (oprEntities != null && oprEntities.size() > 0) {
                            for (ODataEntity entity : oprEntities) {
                                String eqpId = String.valueOf(entity.getProperties().get("Equipment").getValue());
                                String flId = String.valueOf(entity.getProperties().get("FuncLoc").getValue());

                                if (eqpId != null && !eqpId.isEmpty()) {
                                    responseObject = MeasurementPointReading.getEquipmentMeasurementPoint(eqpId, ZAppSettings.FetchLevel.Count, "", null);
                                    if (responseObject != null && !responseObject.isError())
                                        mPointNum += Integer.valueOf(String.valueOf(responseObject.Content()));
                                }

                                if (flId != null && !flId.isEmpty()) {
                                    responseObject = MeasurementPointReading.getFLMeasurementPoint(flId, ZAppSettings.FetchLevel.Count, "", null);
                                    if (responseObject != null && !responseObject.isError())
                                        mPointNum += Integer.valueOf(String.valueOf(responseObject.Content()));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return mPointNum;
    }

    private void deriveWOStatus() {
        ZAppSettings.MobileStatus mobileStatus = null;
        String status = null;
        try {
            if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                if (getWorkOrderNum().contains("L")) {
                    mobileStatus = ZAppSettings.MobileStatus.NotSet;
                } else {
                    int totalOprs = getTotalNumOperations();
                    int totalActiveOprs = Operation.getNumberOfActiveOperations(getWorkOrderNum());
                    int totalCompletedOprs = getTotalNumCompletedOperations();

                    if (totalActiveOprs > 0)
                        mobileStatus = ZAppSettings.MobileStatus.STRT;
                    else if (totalOprs != 0 && totalCompletedOprs == totalOprs)
                        mobileStatus = ZAppSettings.MobileStatus.COMP;
                    else
                        mobileStatus = ZAppSettings.MobileStatus.MOBI;
                }
                status = mobileStatus.getMobileStatusCode();
            } else {
                if (getMobileObjStatus() != null && getMobileObjStatus().length() > 0) {
                    status = getMobileObjStatus();
                    if (!isOnline && ZConfigManager.DEFAULT_STATUS_TO_CHANGE.contains(status)) {
                        StatusCategory receivedStatus = StatusCategory.getStatusDetails(ZConfigManager.DEFAULT_STATUS_TO_SEND, getOrderType(), ZConfigManager.Fetch_Object_Type.WorkOrder);
                        if (receivedStatus != null) {
                            this.statusDetail = receivedStatus;
                            //Update the WO to offlinestore with MOBI status
                            UpdateStatus(receivedStatus, null, null, false, null);
                            return;
                        }
                    }
                }
            }
            //Derive the status for string scenarios
            if (status != null && !status.isEmpty()) {
                StatusCategory statusDetail = StatusCategory.getStatusDetails(status, getOrderType(), ZConfigManager.Fetch_Object_Type.WorkOrder);
                if (statusDetail != null) {
                    this.statusDetail = statusDetail;
                    getWOAllowedStatus(statusDetail);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public String getTruncated(String number) {
        String normalNum = number;
        try {
            int truncatedNum;
            truncatedNum = Integer.parseInt(number);
            normalNum = String.valueOf(truncatedNum);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }

        return normalNum;
    }

    public ArrayList<Operation> getWorkOrderOperations() {
        return workOrderOperations;
    }

    public void setWorkOrderOperations(ArrayList<Operation> workOrderOperations) {
        this.workOrderOperations = workOrderOperations;
    }

    public String toAddressString() {
        String strAddress = "";
        String strAddressSeprator;
        StringBuilder stringBuilder = null;
        try {
            strAddressSeprator = ZConfigManager.ADDRESS_SEPARATOR;
            stringBuilder = new StringBuilder();
            if (Address != null && !Address.isEmpty()) {
                stringBuilder.append(Address);
                stringBuilder.append(strAddressSeprator);
            }
            if (PostalCode != null && !PostalCode.isEmpty()) {
                stringBuilder.append(PostalCode);
            }
            strAddress = stringBuilder.toString();

        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return strAddress;
    }

    public String getInspectionLot() {
        return InspectionLot;
    }

    public void setInspectionLot(String inspectionLot) {
        InspectionLot = inspectionLot;
    }

    public StatusCategory getStatusDetail() {
        if (statusDetail == null)
            statusDetail = new StatusCategory();
        return statusDetail;
    }

}