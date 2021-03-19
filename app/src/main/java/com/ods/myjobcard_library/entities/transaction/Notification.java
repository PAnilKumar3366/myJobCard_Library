package com.ods.myjobcard_library.entities.transaction;

import android.location.Location;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.ods.myjobcard_library.R;
import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.Address;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.appsettings.StatusCategory;
import com.ods.myjobcard_library.entities.ctentities.OrderTypeFeature;
import com.ods.myjobcard_library.entities.ctentities.SpinnerItem;
import com.ods.myjobcard_library.entities.ctentities.UserTable;
import com.ods.myjobcard_library.entities.ctentities.WorkOrderStatus;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.client.odata.v4.EntityValue;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Notification extends ZBaseEntity {

    //WO child elements
    private static Notification currNotification;
    public boolean isBadgeLayoutVisible;
    private DataHelper dataHelper;
    private boolean isAttachmentAvailable;
    private com.ods.myjobcard_library.entities.Address DefaultAddress;
    //Fields
    private String Notification;
    private String NotificationType;
    private String ShortText;
    private String PriorityType;
    private String Priority;
    private String EnteredBy;
    private GregorianCalendar CreatedOn;
    private String ChangedBy;
    private GregorianCalendar ChangedOn;
    private Time NotifTime;
    private GregorianCalendar NotifDate;
    private String ReportedBy;
    private String WorkOrderNum;
    private String Customer;
    private String ObjectNumber;
    private GregorianCalendar CompletionDate;
    private Time CompletionTime;
    private String Vendor;
    private String AddressNumber;
    private String LocationAddress;
    private String CatalogType;
    private String CodeGroup;
    private String Coding;
    private String Batch;
    private String WorkCenter;
    private String MainWorkCenter;
    private String PltforWorkCtr;
    private String Delete;
    private String PartnerFunctn;
    private String Partner;
    private String SystemStatus;
    private String UserStatus;
    private String MobileStatus;
    private String PlanningPlant;
    private String LocAccAssmt;
    private String Assembly;
    private String Breakdown;
    private GregorianCalendar MalfunctStart;
    private GregorianCalendar MalfunctEnd;
    private Double BreakdownDur;
    private String Unit;
    private String Equipment;
    private String FunctionalLoc;
    private String ABCIndicator;
    private String MaintPlant;
    private String BusinessArea;
    private String ControllingArea;
    private String CostCenter;
    private Time MalFunctStartTime;
    private Time MalfunctEndTime;
    private GregorianCalendar RequiredStartDate;
    private Time RequiredStartTime;
    private GregorianCalendar RequiredEndDate;
    private Time RequiredEndTime;
    private String PlannerGroup;
    private String TempID;
    private String OnlineSearch;
    private String PlannerGroupDes;

    private boolean ErrorEntity;
    private String ErrorMsg;

    //Location and contact address
    private String Name;
    private String Address;
    private String PhoneNumber;
    private String PostalCode;
    private BigDecimal Latitude;
    private BigDecimal Longitude;
    private String GeoLocation;
    private String StatusFlag;
    private String MobileObjectType;

    private StatusCategory statusDetail;
    private ArrayList<StatusCategory> validStatuses;

    private ArrayList<NotificationItem> notificationItems = new ArrayList<>();

    //End of Setters and Getters Method
    public Notification() {
        initializeEntityProperties(false);
    }

    public Notification(String notification) {
        super();
        this.Notification = notification;
        initializeEntityProperties(false);
    }
    //Setters and Getters Method

    public Notification(EntityValue entityValue) {
        create(entityValue);
        initializeEntityProperties(false);
        deriveNotificationStatus();
    }
    /*Added by Anil
     * Customized OData Entity Constructor*/

    public Notification(ZODataEntity zoDataEntity) {
        create(zoDataEntity);
        initializeEntityProperties(false);
        deriveNotificationStatus();
    }

    public Notification(ODataEntity entity, boolean isWONotif, boolean fetchAddress) {
        initializeEntityProperties(isWONotif);
        create(entity, isWONotif, fetchAddress);
    }

    public Notification(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties(false);
        create(entity);
        deriveNotificationStatus();
    }

    public static Notification getCurrNotification() {
        return currNotification;
    }

    public static void setCurrNotification(Notification notification) {
        currNotification = notification;
    }

    public static ResponseObject getNotifications(ZAppSettings.FetchLevel fetchLevel, ZAppSettings.Hierarchy hierarchy, String notificationNum, String orderByCriteria, boolean isForWO) {
        ResponseObject result = null;
        String resourcePath = null;
        String strOrderBy = "&$orderby=";
        String strOrderByURI = null;
        String strEntitySet = null;
        ZConfigManager.Fetch_Object_Type notifType;
        boolean fetchAddress = false;

        try {
            if (orderByCriteria == null || orderByCriteria.isEmpty()) {
                orderByCriteria = "Notification";
            }
            strOrderByURI = strOrderBy + orderByCriteria;
            if (isForWO) {
                strEntitySet = ZCollections.WO_NOTIFICATION_COLLECTION;
                notifType = ZConfigManager.Fetch_Object_Type.WONotification;
            } else {
                strEntitySet = ZCollections.NOTIFICATION_COLLECTION;
                notifType = ZConfigManager.Fetch_Object_Type.Notification;
            }
            switch (fetchLevel) {
                case ListMap:
                    resourcePath = strEntitySet + "?$select=Notification,NotificationType,SystemStatus,Priority,ShortText,Breakdown,NotifDate,PostalCode,NotifTime,MobileStatus,Equipment,FunctionalLoc,TempID,LocationAddress,RequiredStartDate,RequiredEndDate" + strOrderByURI;
                    fetchAddress = true;
                    break;
                case List:
                    resourcePath = strEntitySet + "?$select=Notification,NotificationType,SystemStatus,Priority,ShortText,Breakdown,NotifDate,PostalCode,NotifTime,MobileStatus,Equipment,FunctionalLoc,TempID,RequiredStartDate,RequiredEndDate" + strOrderByURI;
                    break;
                case Header:
                    resourcePath = strEntitySet;
                    break;
                case Single:
                    if (notificationNum != null && notificationNum.length() > 0) {
//                        resourcePath = strEntitySet + "?$filter=(Notification%20eq%20%27"+ NotificationNum +"%27)&$expand="+strExpandQuery;
                        resourcePath = strEntitySet + "?$filter=(Notification%20eq%20%27" + notificationNum + "%27)";
                        fetchAddress = true;
                    }
                    break;
                case SingleWithItemCauses:
                    if (notificationNum != null && notificationNum.length() > 0) {
//                        resourcePath = strEntitySet + "?$filter=(Notification%20eq%20%27"+ NotificationNum +"%27)&$expand="+strExpandQuery;
                        resourcePath = strEntitySet + "?$filter=(Notification%20eq%20%27" + notificationNum + "%27)";
                        fetchAddress = true;
                    }
                    break;
                case All:
//                    resourcePath = strEntitySet + "?$expand="+strExpandQuery;
                    resourcePath = strEntitySet;
                    fetchAddress = true;
                    break;
                default:
//                    resourcePath = strEntitySet + "?$expand="+strExpandQuery;
                    resourcePath = strEntitySet;
                    fetchAddress = true;
                    break;
            }
            //offlineManager.openOfflineStore(ctx, StoreSettings.Stores.Tx);
            result = DataHelper.getInstance().getEntities(strEntitySet, resourcePath);
            if (!result.isError()) {
                //parse data for WO from payload
                result = FromEntity((List<ODataEntity>) result.Content(), isForWO, fetchAddress);
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static int getTotalNotificationCount() {
        ResponseObject result;
        try {
            String entitySetName = ZCollections.NOTIFICATION_COLLECTION;
            String resPath = entitySetName + "/$count";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                return Integer.parseInt(result.Content().toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return 0;
    }

    public static ResponseObject getFilteredNotifications(@NonNull String filterQuery, ZAppSettings.FetchLevel fetchLevel, String orderByCriteria) {
        ResponseObject result = null;
        String entitySetName = ZCollections.NOTIFICATION_COLLECTION;
        String resPath = entitySetName;
        String orderByUrl = "$orderby=";
        boolean fetchAddress = false;
        try {
            if (orderByCriteria == null || orderByCriteria.isEmpty()) {
                orderByCriteria = "Notification";
            }
            orderByUrl += orderByCriteria;
            if (!filterQuery.isEmpty()) {
                if (fetchLevel == ZAppSettings.FetchLevel.Count)
                    resPath += "/$count" + filterQuery + "&";
                else
                    resPath += filterQuery + "&";
            } else
                resPath += "?";
            switch (fetchLevel) {
                case ListMap:
                    resPath += "$select=Notification,NotificationType,SystemStatus,Priority,ShortText,Breakdown,NotifDate,PostalCode,NotifTime,MobileStatus,Equipment,FunctionalLoc,TempID,LocationAddress,RequiredStartDate,RequiredEndDate" + "&" + orderByUrl;
                    fetchAddress = true;
                    break;
                case List:
                    resPath += "$select=Notification,NotificationType,SystemStatus,Priority,ShortText,Breakdown,NotifDate,PostalCode,NotifTime,MobileStatus,Equipment,FunctionalLoc,TempID,RequiredStartDate,RequiredEndDate" + "&" + orderByUrl;
                    break;
                default:
                    resPath += orderByUrl;
                    fetchAddress = true;
                    break;
            }
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError() && !fetchLevel.equals(ZAppSettings.FetchLevel.Count)) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities, false, fetchAddress);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, boolean isWoNotif, boolean fetchAddress) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<Notification> notifications = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    notifications.add(new Notification(entity, isWoNotif, fetchAddress));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", notifications);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ArrayList<Notification> searchNotifications(String searchedTxt, ArrayList<Notification> notifications) {
        ArrayList<Notification> searchedNotifs = new ArrayList<Notification>();
        for (Notification notification : notifications) {
            if (notification.getNotification().toLowerCase().contains(searchedTxt.toLowerCase()) || notification.getShortText().toLowerCase().contains(searchedTxt.toString().toLowerCase()) || notification.getEquipment().toLowerCase().contains(searchedTxt.toString().toLowerCase()) || notification.getFunctionalLoc().contains(searchedTxt.toString().toLowerCase()) || notification.getPostalCode() != null && notification.getPostalCode().contains(searchedTxt.toString().toUpperCase()))
                searchedNotifs.add(notification);
        }
        return searchedNotifs;
    }

    public static Notification getNotifByIDFromCollection(String notificationNum, ArrayList<Notification> notifications) {
        for (Notification notification : notifications) {
            if (notification.getNotification().equalsIgnoreCase(notificationNum)) {
                return notification;
            }
        }
        return null;
    }

    public static int getNotificationCountByPriority(String priority) {
        try {
            return getNotificationCount("?$filter=Priority eq '" + priority + "'");
        } catch (Exception e) {
            DliteLogger.WriteLog(Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return 0;
    }

    public static int getNotificationCount(@NonNull String filterQuery) {
        try {
            ResponseObject result = null;
            result = getFilteredNotifications(filterQuery, ZAppSettings.FetchLevel.Count, null);

            if (result != null && !result.isError())
                return Integer.parseInt(result.Content().toString());

        } catch (Exception e) {
            DliteLogger.WriteLog(Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return 0;
    }

    /**
     * @return ArrayList of all distinct mobile object statuses among all notifications
     */
    public static ArrayList<String> getAllDistinctStatuses() {
        ArrayList<String> statuses = new ArrayList<>();
        try {
            String resPath = ZCollections.NOTIFICATION_COLLECTION + "?$select=MobileStatus";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.NOTIFICATION_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        statuses.add(String.valueOf(entity.getProperties().get("MobileStatus").getValue()));
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
     * @return ArrayList of SpinnerItem of all distinct mobile object statuses among all notifications
     */
    public static ArrayList<SpinnerItem> getSpinnerStatuses() {
        ArrayList<SpinnerItem> spinnerStatuses = new ArrayList<>();
        ArrayList<String> statuses = getAllDistinctStatuses();
        for (String status : statuses) {
            if (!status.isEmpty())
                spinnerStatuses.add(new SpinnerItem(status, status));
        }
        return spinnerStatuses;
    }

    /**
     * @return ArrayList of all distinct WorkCenters among all notifications
     */
    public static ArrayList<String> getAllDistinctWorkCenters() {
        ArrayList<String> workCenters = new ArrayList<>();
        try {
            String resPath = ZCollections.NOTIFICATION_COLLECTION + "?$select=WorkCenter";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.NOTIFICATION_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        workCenters.add(String.valueOf(entity.getProperties().get("WorkCenter").getValue()));
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
     * @return ArrayList of SpinnerItem of all distinct WorkCenters among all notifications
     */
    public static ArrayList<SpinnerItem> getSpinnerWorkCenters() {
        ArrayList<SpinnerItem> spinnerWorkCenters = new ArrayList<>();
        ArrayList<String> workCenters = getAllDistinctWorkCenters();
        ArrayList<SpinnerItem> spinnerItems = new ArrayList<>();
        try {
            ResponseObject result = com.ods.myjobcard_library.entities.ctentities.WorkCenter.getWorkCenters();
            if (result != null && !result.isError())
                spinnerWorkCenters = (ArrayList<SpinnerItem>) result.Content();
            for (String workCenter : workCenters) {
                for (SpinnerItem item : spinnerWorkCenters) {
                    if (item.getObjectID().equalsIgnoreCase(workCenter))
                        spinnerItems.add(item);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(com.ods.myjobcard_library.entities.transaction.Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return spinnerItems;
    }

    /**
     * @return ArrayList of all distinct Priorities among all notifications
     */
    public static ArrayList<String> getAllDistinctPriorities() {
        ArrayList<String> priorities = new ArrayList<>();
        try {
            String resPath = ZCollections.NOTIFICATION_COLLECTION + "?$select=Priority&$orderby=Priority";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.NOTIFICATION_COLLECTION, resPath);
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
     * @return ArrayList of SpinnerItem of all distinct Priorities among all notifications
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
     * @return ArrayList of all distinct Types among all notifications
     */
    public static ArrayList<String> getAllDistinctTypes() {
        ArrayList<String> types = new ArrayList<>();
        try {
            String resPath = ZCollections.NOTIFICATION_COLLECTION + "?$select=NotificationType";
            ResponseObject response = DataHelper.getInstance().getEntities(ZCollections.NOTIFICATION_COLLECTION, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) response.Content();
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        types.add(String.valueOf(entity.getProperties().get("NotificationType").getValue()));
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
     * @return ArrayList of SpinnerItem of all distinct Types among all notifications
     */
    public static ArrayList<SpinnerItem> getSpinnerTypes() {
        ArrayList<SpinnerItem> spinnerTypes = new ArrayList<>();
        ArrayList<String> types = getAllDistinctTypes();
        for (String type : types) {
            if (!type.isEmpty())
                spinnerTypes.add(new SpinnerItem(type, type));
        }
        return spinnerTypes;
    }

    @Override
    public boolean isLocal() {
        boolean local = super.isLocal();
        if (!local) {
            return this.getEnteredBy() != null && this.getEnteredBy().equalsIgnoreCase(ZAppSettings.strUser);
        } else return true;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }

    public String getNotificationType() {
        return NotificationType;
    }

    public void setNotificationType(String notificationType) {
        NotificationType = notificationType;
    }

    public String getShortText() {
        return ShortText;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }

    public String getPriorityType() {
        return PriorityType;
    }

    public void setPriorityType(String priorityType) {
        PriorityType = priorityType;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public GregorianCalendar getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(GregorianCalendar createdOn) {
        CreatedOn = createdOn;
    }

    public String getChangedBy() {
        return ChangedBy;
    }

    public void setChangedBy(String changedBy) {
        ChangedBy = changedBy;
    }

    public GregorianCalendar getChangedOn() {
        return ChangedOn;
    }

    public void setChangedOn(GregorianCalendar changedOn) {
        ChangedOn = changedOn;
    }

    public Time getNotifTime() {
        return NotifTime;
    }

    public void setNotifTime(Time notifTime) {
        NotifTime = notifTime;
    }

    public GregorianCalendar getNotifDate() {
        return NotifDate;
    }

    public void setNotifDate(GregorianCalendar notifDate) {
        NotifDate = notifDate;
    }

    public String getReportedBy() {
        return ReportedBy;
    }

    public void setReportedBy(String reportedBy) {
        ReportedBy = reportedBy;
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        Customer = customer;
    }

    public String getObjectNumber() {
        return ObjectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        ObjectNumber = objectNumber;
    }

    public GregorianCalendar getCompletionDate() {
        return CompletionDate;
    }

    public void setCompletionDate(GregorianCalendar completionDate) {
        CompletionDate = completionDate;
    }

    public Time getCompletionTime() {
        return CompletionTime;
    }

    public void setCompletionTime(Time completionTime) {
        CompletionTime = completionTime;
    }

    public String getVendor() {
        return Vendor;
    }

    public void setVendor(String vendor) {
        Vendor = vendor;
    }

    public String getAddressNumber() {
        return AddressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        AddressNumber = addressNumber;
    }

    public String getLocationAddress() {
        return LocationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        LocationAddress = locationAddress;
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

    public String getCoding() {
        return Coding;
    }

    public void setCoding(String coding) {
        Coding = coding;
    }

    public String getBatch() {
        return Batch;
    }

    public void setBatch(String batch) {
        Batch = batch;
    }

    public String getWorkCenter() {
        return WorkCenter;
    }

    public void setWorkCenter(String workCenter) {
        WorkCenter = workCenter;
    }

    public String getPltforWorkCtr() {
        return PltforWorkCtr;
    }

    public void setPltforWorkCtr(String pltforWorkCtr) {
        PltforWorkCtr = pltforWorkCtr;
    }

    public String getDelete() {
        return Delete;
    }

    public void setDelete(String delete) {
        Delete = delete;
    }

    public String getPartnerFunctn() {
        return PartnerFunctn;
    }

    public void setPartnerFunctn(String partnerFunctn) {
        PartnerFunctn = partnerFunctn;
    }

    public String getPartner() {
        return Partner;
    }

    public void setPartner(String partner) {
        Partner = partner;
    }

    public String getSystemStatus() {
        return SystemStatus;
    }

    public void setSystemStatus(String systemStatus) {
        SystemStatus = systemStatus;
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

    public String getPlanningPlant() {
        return PlanningPlant;
    }

    public void setPlanningPlant(String planningPlant) {
        PlanningPlant = planningPlant;
    }

    public String getLocAccAssmt() {
        return LocAccAssmt;
    }

    public void setLocAccAssmt(String locAccAssmt) {
        LocAccAssmt = locAccAssmt;
    }

    public String getAssembly() {
        return Assembly;
    }

    public void setAssembly(String assembly) {
        Assembly = assembly;
    }

    public String getBreakdown() {
        return Breakdown;
    }

    public void setBreakdown(String breakdown) {
        Breakdown = breakdown;
    }

    public GregorianCalendar getMalfunctStart() {
        return MalfunctStart;
    }

    public void setMalfunctStart(GregorianCalendar malfunctStart) {
        MalfunctStart = malfunctStart;
    }

    public GregorianCalendar getMalfunctEnd() {
        return MalfunctEnd;
    }

    public void setMalfunctEnd(GregorianCalendar malfunctEnd) {
        MalfunctEnd = malfunctEnd;
    }

    public Double getBreakdownDur() {
        return BreakdownDur;
    }

    public void setBreakdownDur(Double breakdownDur) {
        BreakdownDur = breakdownDur;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getFunctionalLoc() {
        return FunctionalLoc;
    }

    public void setFunctionalLoc(String functionalLoc) {
        FunctionalLoc = functionalLoc;
    }

    public String getABCIndicator() {
        return ABCIndicator;
    }

    public void setABCIndicator(String ABCIndicator) {
        this.ABCIndicator = ABCIndicator;
    }

    public String getMaintPlant() {
        return MaintPlant;
    }

    public void setMaintPlant(String maintPlant) {
        MaintPlant = maintPlant;
    }

    public String getBusinessArea() {
        return BusinessArea;
    }

    public void setBusinessArea(String businessArea) {
        BusinessArea = businessArea;
    }

    public String getControllingArea() {
        return ControllingArea;
    }

    public void setControllingArea(String controllingArea) {
        ControllingArea = controllingArea;
    }

    public String getCostCenter() {
        return CostCenter;
    }

    public void setCostCenter(String costCenter) {
        CostCenter = costCenter;
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

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
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

    public int getPriorityDrawable() {
        if (Priority.trim().equals("4"))
            return R.drawable.emergency_medium;
        if (Priority.trim().equals("3"))
            return R.drawable.emergency_low;
        if (Priority.trim().equals("1"))
            return R.drawable.emergency_very_high;
        if (Priority.trim().equals("2"))
            return R.drawable.emergency_high;
        return R.drawable.emergency_very_high;
    }

    public Time getMalFunctStartTime() {
        return MalFunctStartTime;
    }

    public void setMalFunctStartTime(Time malFunctStartTime) {
        MalFunctStartTime = malFunctStartTime;
    }

    public Time getMalfunctEndTime() {
        return MalfunctEndTime;
    }

    public void setMalfunctEndTime(Time malfunctEndTime) {
        MalfunctEndTime = malfunctEndTime;
    }

    public GregorianCalendar getRequiredStartDate() {
        return RequiredStartDate;
    }

    public void setRequiredStartDate(GregorianCalendar requiredStartDate) {
        RequiredStartDate = requiredStartDate;
    }

    public Time getRequiredStartTime() {
        return RequiredStartTime;
    }

    public void setRequiredStartTime(Time requiredStartTime) {
        RequiredStartTime = requiredStartTime;
    }

    public GregorianCalendar getRequiredEndDate() {
        return RequiredEndDate;
    }

    public void setRequiredEndDate(GregorianCalendar requiredEndDate) {
        RequiredEndDate = requiredEndDate;
    }

    public Time getRequiredEndTime() {
        return RequiredEndTime;
    }

    public void setRequiredEndTime(Time requiredEndTime) {
        RequiredEndTime = requiredEndTime;
    }

    public String getPlannerGroup() {
        return PlannerGroup;
    }

    public void setPlannerGroup(String plannerGroup) {
        PlannerGroup = plannerGroup;
    }

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
    }

    public boolean isAttachmentAvailable() {
        return isAttachmentAvailable;
    }

    public void setIsAttachmentAvailable(boolean isAttachmentAvailable) {
        this.isAttachmentAvailable = isAttachmentAvailable;
    }

    public String getRefId() {
        return this.getTempID() == null || this.getTempID().isEmpty() ? this.getNotification() : this.getTempID();
    }

    public String getDisplayableNotificationNum() {
        if (getTempID() == null || getTempID().isEmpty())
            return getNotification();
        else
            return getTempID().replace(ZConfigManager.LOCAL_ID, ZConfigManager.LOCAL_IDENTIFIER);
    }

    public String getMainWorkCenter() {
        return MainWorkCenter;
    }

    public void setMainWorkCenter(String mainWorkCenter) {
        MainWorkCenter = mainWorkCenter;
    }

    public Address getDefaultAddress() {
        return DefaultAddress;
    }

    public void setDefaultAddress(Address defaultAddress) {
        DefaultAddress = defaultAddress;
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

    private void initializeEntityProperties(boolean isWONotification) {
        String entitySetName = isWONotification ? ZCollections.WO_NOTIFICATION_COLLECTION : ZCollections.NOTIFICATION_COLLECTION;
        String entityType = isWONotification ? ZCollections.WO_NOTIFICATION_ENTITY_TYPE : ZCollections.NOTIFICATION_ENTITY_TYPE;
        this.setEntitySetName(entitySetName);
        this.setEntityType(entityType);
        this.addKeyFieldNames(ZConfigManager.NOTIFICATION_KEY_FIELD);
        this.setParentEntitySetName(entitySetName);
        this.setParentForeignKeyFieldName(ZConfigManager.NOTIFICATION_KEY_FIELD);
        this.setParentKeyFieldName(ZConfigManager.NOTIFICATION_KEY_FIELD);
    }

    public boolean isInitialized() {
        return (!TextUtils.isEmpty(this.Notification));
    }

    public ResponseObject create(ODataEntity entity, boolean isWONotif, boolean fetchAddress) {
        ResponseObject result = null;
        try {
            super.create(entity);
            ResponseObject resultAttachments = null;
            try {
                if (dataHelper == null) {
                    DataHelper.getInstance();
                }
                String entitySetName = isWONotif ? ZCollections.WO_NO_ATTACHMENT_COLLECTION : ZCollections.NO_ATTACHMENT_COLLECTION;
                String strResPath = entitySetName + "/$count?$filter=(endswith(ObjectKey, '" + getNotification() + "') eq true)";
                resultAttachments = DataHelper.getInstance().getEntities(ZCollections.NO_ATTACHMENT_COLLECTION, strResPath);
                if (!resultAttachments.isError()) {
                    Object rawData = resultAttachments.Content();
                    if (Integer.parseInt(rawData.toString()) > 0)
                        isAttachmentAvailable = true;
                }
            } catch (Exception e) {
                DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            }

            try {
                deriveNotificationStatus();
            } catch (Exception e) {
                DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            }

            try {
                if (fetchAddress) {
                    //get Notification
                    if (result != null && result.Content() != null) {
                        String addrNum = "";
                        if (getLocationAddress() != null && !getLocationAddress().isEmpty()) {
                            addrNum = getLocationAddress();
                        } else if (getAddressNumber() != null && !getAddressNumber().isEmpty()) {
                            addrNum = getAddressNumber();
                        }
                        if (addrNum.isEmpty()) {
                                /*resultAddress = getWOPartnerAddress(n.getObjectNumber());
                                if(!resultAddress.isError())
                                {
                                    partnerAddresses = (ArrayList<PartnerAddress>) resultAddress.Content();
                                    n.setPartnerAddresses(partnerAddresses);
                                }*/
                        } else {
                            ResponseObject resultAddress = getDefaultAddress().getNotificationAddress(addrNum);
                            if (!resultAddress.isError() && ((ArrayList) resultAddress.Content()).size() > 0) {
                                Address address = ((ArrayList<Address>) resultAddress.Content()).get(0);
                                setDefaultAddress(address);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            }

            result = new ResponseObject(ZConfigManager.Status.Success, "", this);
        }
                /*catch(Exception e)
                {
                    result = new ResponseObject(ZConfigManager.Status.Error,e.getMessage(),null);
                }
            }
        }*/ catch (Exception e) {
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);

        }
        return result;
    }

    public boolean isComplete() {
        boolean result = false;

        try {
            result = getStatusDetail().getStatusCode().contains(ZAppSettings.MobileStatus.COMP.getMobileStatusCode());
        } catch (Exception e) {
            DliteLogger.WriteLog(Operation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public boolean isAssignedToCurrentUser() {
        boolean result = false, isPartner = false;
        try {
            if (UserTable.getUserNotificationAssignmentType().equalsIgnoreCase("2")) {
                String workCenterObjId = com.ods.myjobcard_library.entities.ctentities.WorkCenter.getWorkCenterObjId(UserTable.getUserWorkCenter());
                return result = this.WorkCenter.equalsIgnoreCase(workCenterObjId);
            }
            if (Partner != null && !Partner.isEmpty())
                return isPartner = Integer.valueOf(Partner).equals(Integer.valueOf(UserTable.getUserPersonnelNumber()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return isPartner;
    }

    public boolean isActive() {
        return getStatusDetail().isInProcess();
    }


    public int getTotalNumItems(boolean isWONotif) {
        int itemsCount = 0;
        ResponseObject responseObject = null;
        String entitySetName = !isWONotif ? ZCollections.NOTIFICATION_ITEMS_COLLECTION : ZCollections.WO_NOTIFICATION_ITEMS_COLLECTION;
        String strResPath = entitySetName;
        Object rawData = null;
        try {
            strResPath += "/$count?$filter= (Notification eq '" + getNotification() + "')";
            responseObject = DataHelper.getInstance().getEntities(entitySetName, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                itemsCount = Integer.valueOf(String.valueOf(rawData));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return itemsCount;
    }

    public int getTotalNumItemCauses(boolean isWONotif) {
        int itemsCount = 0;
        ResponseObject responseObject = null;
        String entitySetName = !isWONotif ? ZCollections.NOTIFICATION_ITEM_CAUSES_COLLECTION : ZCollections.WO_NOTIFICATION_ITEM_CAUSES_COLLECTION;
        String strResPath = entitySetName;
        Object rawData = null;
        try {
            strResPath += "/$count?$filter= (Notification eq '" + getNotification() + "')";
            responseObject = DataHelper.getInstance().getEntities(entitySetName, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                itemsCount = Integer.valueOf(String.valueOf(rawData));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return itemsCount;
    }

    public int getTotalNumActivities(boolean isWONotif) {
        int activitiesCount = 0;
        ResponseObject responseObject = null;
        String entitySetName = !isWONotif ? ZCollections.NOTIFICATION_ACTIVITY_COLLECTION : ZCollections.WO_NOTIFICATION_ACTIVITY_COLLECTION;
        String strResPath = entitySetName;
        Object rawData = null;
        try {
            strResPath += "/$count?$filter= (Notification eq '" + getNotification() + "' and Item eq '0000')";
            responseObject = DataHelper.getInstance().getEntities(entitySetName, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                activitiesCount = Integer.valueOf(String.valueOf(rawData));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return activitiesCount;
    }

    public int getTotalNumTasks(boolean isWONotif) {
        int tasksCount = 0;
        ResponseObject responseObject = null;
        String entitySetName = !isWONotif ? ZCollections.NOTIFICATION_TASKS_COLLECTION : ZCollections.WO_NOTIFICATION_TASKS_COLLECTION;
        String strResPath = entitySetName;
        Object rawData = null;
        try {
            strResPath += "/$count?$filter= (Notification eq '" + getNotification() + "' and Item eq '0000')";
            responseObject = DataHelper.getInstance().getEntities(entitySetName, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                tasksCount = Integer.valueOf(String.valueOf(rawData));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return tasksCount;
    }

    public int getTotalNumUploadedAttachments() {
        int intAttachmentsCount = 0;
        ResponseObject responseObject = null;
        String entitySetName = ZCollections.NO_ATTACHMENT_CONTENT_UPLOAD_COLLECTION;
        String strResPath;
        Object rawData = null;
        try {
            strResPath = entitySetName + "/$count?$filter= (Notification eq '" + getNotification() + "' and BINARY_FLG ne 'N')";
            responseObject = DataHelper.getInstance().getEntities(entitySetName, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intAttachmentsCount = Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intAttachmentsCount;
    }

    public int getTotalNumAttachments(boolean isWONotif) {
        int intAttachmentsCount = 0;
        ResponseObject responseObject = null;
        String entitySetName = isWONotif ? ZCollections.WO_NO_ATTACHMENT_COLLECTION : ZCollections.NO_ATTACHMENT_COLLECTION;
        String strResPath;
        Object rawData = null;
        try {
            intAttachmentsCount = getTotalNumUploadedAttachments();
            strResPath = entitySetName + "/$count?$filter=(endswith(ObjectKey, '" + getNotification() + "') eq true)";
            responseObject = DataHelper.getInstance().getEntities(entitySetName, strResPath);
            if (!responseObject.isError()) {
                rawData = responseObject.Content();
                intAttachmentsCount += Integer.parseInt(rawData.toString());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Notification.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intAttachmentsCount;
    }

    public String getCreatedWorkOrder() {
        String orderNum = getWorkOrderNum();
        try {
            if (orderNum == null || orderNum.isEmpty()) {
                String resPath = ZCollections.WO_COLLECTION + "?$filter=NotificationNum eq '" + getRefId() + "'&$select=WorkOrderNum";
                ResponseObject result = DataHelper.getInstance().getEntities(ZCollections.WO_COLLECTION, resPath);
                if (result != null && !result.isError()) {
                    List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                    if (entities != null && entities.size() > 0) {
                        orderNum = String.valueOf(entities.get(0).getProperties().get("WorkOrderNum").getValue());
                        orderNum = new WorkOrder(orderNum).getDisplayableWorkOrderNum();
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return orderNum;
    }

    public boolean updateStatus(StatusCategory status, String notes, boolean autoFlush, Location deviceLocation) {
        boolean result = false;
        DataHelper dbHelper = null;
        String deviceTime = " ";
        String strNotesText = "";
        String strReasonText = "";
        String strStatusText = "";
        try {
            setStatusFlag(ZConfigManager.STATUS_SET_FLAG);
            setMobileStatus(status.getStatusCode());
            setMobileObjectType(status.getObjectType());
            String statusDesc = status.getStatusDescKey();
            if (!ZConfigManager.AUTO_NOTES_ON_STATUS) {
                strNotesText = (notes != null ? notes : "");
            } else {
                Date timeStamp = ZCommon.getDeviceTime();
                if (timeStamp != null) {
                    deviceTime = timeStamp.toString();
                }
                strStatusText = ZConfigManager.AUTO_NOTES_TEXT_LINE1 + " " + statusDesc + " " +
                        ZConfigManager.AUTO_NOTES_TEXT_LINE2 + " " + ZAppSettings.strUser.toUpperCase() + " " +
                        ZConfigManager.AUTO_NOTES_TEXT_LINE3 + " " + deviceTime;
                if (ZConfigManager.ENABLE_POST_DEVICE_LOCATION_NOTES && deviceLocation != null) {
                    strStatusText = strStatusText + " at location Lat: " + deviceLocation.getLatitude() + "; Long: " + deviceLocation.getLongitude();
                }
                strNotesText = strStatusText + (notes != null ? notes : "");
            }
            //setNotes(strNotesText);
            //Update the WO to offlinestore
            setMode(ZAppSettings.EntityMode.Update);
            ResponseObject response = SaveToStore(strNotesText.isEmpty());
            if (!strNotesText.isEmpty() && response != null && !response.isError()) {
                NotifLongText longText = new NotifLongText();
                response = longText.SendNotificationText(strNotesText, getNotification(), getTempID(), autoFlush);
            }
            result = response != null && !response.isError();
            deriveNotificationStatus();
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private ResponseObject getAllowedStatus() {
        ResponseObject result = null;
        String strResPath;
        ArrayList<String> allowedStatusList;
        int intNumOfActiveWO = 0;
        try {
            if (getStatusDetail() != null) {

                result = WorkOrderStatus.getWorkOrderAllowedStatus(getStatusDetail(), getNotificationType());
                if (!result.isError()) {
                    result = WorkOrderStatus.getWorkOrderAllowedStatus(getStatusDetail(), getNotificationType());
                    if (result != null && !result.isError()) {
                        allowedStatusList = (ArrayList<String>) result.Content();
                        for (String allowedStatus : allowedStatusList) {
                            StatusCategory status = StatusCategory.getStatusDetails(allowedStatus, getNotificationType(), ZConfigManager.Fetch_Object_Type.Notification);
                            if (status != null)
                                addValidStatus(status);
                        }
                    } else {
                        DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, "error in getting allowed status for Notification: " + getNotification() + ". Message: " + result.getMessage());
                    }
                }
            }


        } catch (Exception e) {
            DliteLogger.WriteLog(WorkOrder.class, ZAppSettings.LogLevel.Error, "error in getting allowed status for Notification: " + getNotification() + ". Message: " + e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void deriveNotificationStatus() {
        try {
            StatusCategory statusDetail = StatusCategory.getStatusDetails(getMobileStatus(), getNotificationType(), ZConfigManager.Fetch_Object_Type.Notification);
            if (statusDetail != null) {
                this.statusDetail = statusDetail;
                getAllowedStatus();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public ArrayList<NotificationItem> getNotificationItems() {
        return notificationItems;
    }

    public void setNotificationItems(ArrayList<NotificationItem> notificationItems) {
        this.notificationItems = notificationItems;
    }

    public StatusCategory getStatusDetail() {
        if (statusDetail == null)
            statusDetail = new StatusCategory();
        return statusDetail;
    }

    public String getStatusFlag() {
        return StatusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        StatusFlag = statusFlag;
    }

    public String getMobileObjectType() {
        return MobileObjectType;
    }

    public void setMobileObjectType(String mobileObjectType) {
        MobileObjectType = mobileObjectType;
    }

    public ArrayList<StatusCategory> getValidStatuses() {
        return validStatuses;
    }

    private void addValidStatus(StatusCategory status) {
        if (validStatuses == null)
            validStatuses = new ArrayList<>();
        validStatuses.add(status);
    }

    public ArrayList<String> getPreCompletionMessages(boolean isWONotification) {
        ArrayList<String> errorMessages = new ArrayList<>();
        ArrayList<OrderTypeFeature> featureList = OrderTypeFeature.getMandatoryFeaturesByObjectType(getNotificationType());
        for (OrderTypeFeature orderTypeFeature : featureList) {
            if (orderTypeFeature.getFeature().contains(ZAppSettings.Features.ITEM.getFeatureValue()) && getTotalNumItems(isWONotification) == 0) {
                errorMessages.add("Please provide object / part and damage details by adding Item to the notification");
            }
            if (orderTypeFeature.getFeature().contains(ZAppSettings.Features.ITEMCAUSE.getFeatureValue()) && getTotalNumItemCauses(isWONotification) == 0) {
                errorMessages.add("Please provide cause details for the notification");
            }
            if (getBreakdown().equalsIgnoreCase("x") && (getMalfunctEnd() == null || getMalfunctEndTime() == null)) {
                errorMessages.add("Please provide the malfunction end date time as the breakdown is notified for the notification");
            }
        }
        return errorMessages;
    }

    public String getOnlineSearch() {
        return OnlineSearch;
    }

    public void setOnlineSearch(String onlineSearch) {
        OnlineSearch = onlineSearch;
    }

    public String getPlannerGroupDes() {
        return PlannerGroupDes;
    }

    public void setPlannerGroupDes(String plannerGroupDes) {
        PlannerGroupDes = plannerGroupDes;
    }
}