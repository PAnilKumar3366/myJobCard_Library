package com.ods.myjobcard_library.entities.supervisor;

import androidx.annotation.NonNull;

import com.ods.myjobcard_library.R;
import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.Address;
import com.ods.myjobcard_library.entities.ZBaseEntity;
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
 * Created by lenovo on 22-12-2016.
 */
public class SupervisorWorkOrder extends ZBaseEntity {

    private static SupervisorWorkOrder currentSupervisorWorkOrder;
    private String WorkOrderNum;
    private String ShortText;
    private String OrderType;
    private String Category;
    private String Plant;
    private String MaintPlanningPlant;
    private String MaintActivityType;
    private String NotificationNum;
    private String MaintPlant;
    private GregorianCalendar TechCompletionDate;
    private String MainWorkCtr;
    private String PlantMainWorkCtr;
    private GregorianCalendar BasicFnshDate;
    private GregorianCalendar BasicStrtDate;
    private GregorianCalendar ActlStrtDate;
    private GregorianCalendar ActlFnshDate;
    private Time BasicFnshTime;
    private Time BasicStrtTime;
    private String Priority;
    private String EquipNum;
    private String TechObjDescription;
    private String FuncLocation;
    private String SysStatusCode;
    private String SysStatus;
    private String UserStatusCode;
    private String UserStatus;
    private String MobileObjStatus;
    private String AddressNumber;
    private String TempID;
    private String EnteredBy;
    private GregorianCalendar CreatedOn;
    private String Technician;
    private com.ods.myjobcard_library.entities.Address defaultAddress;
    private TeamMember TeamMember;
    private boolean isReadingOutOfRange;
    private String MaintActivityTypeText;


    //----------------------------------------------------------------------------------------------
    private String CategoryText;
    private String TechObjLocAndAssgnmnt;
    private GregorianCalendar SchdFnshDate;
    private GregorianCalendar SchdStrtDate;
    private String BusArea;
    private String BusAreaText;
    private String ControllingArea;
    private String WBSElem;
    //Location and contact address
    private String Name;
    private String Address;
    private String PhoneNumber;
    private String PostalCode;
    private BigDecimal Latitude;
    private BigDecimal Longitude;
    private String GeoLocation;

    //constructors
    public SupervisorWorkOrder(ODataEntity entity) {
        super.create(entity);
    }


    public SupervisorWorkOrder(ODataEntity entity, boolean fetchAddress, ZAppSettings.FetchLevel fetchLevel) {
        this.create(entity, fetchLevel, fetchAddress);
    }

    public static SupervisorWorkOrder getCurrentSupervisorWorkOrder() {
        return currentSupervisorWorkOrder;
    }

    public static void setCurrentSupervisorWorkOrder(SupervisorWorkOrder currentSupervisorWorkOrder) {
        SupervisorWorkOrder.currentSupervisorWorkOrder = currentSupervisorWorkOrder;
    }

    public static ArrayList<SupervisorWorkOrder> searchWorkOrders(CharSequence searchedText, ArrayList<SupervisorWorkOrder> workorders) {
        ArrayList<SupervisorWorkOrder> searchedWOs = new ArrayList<>();
        for (SupervisorWorkOrder workOrder : workorders) {
            if (workOrder.getWorkOrderNum().toLowerCase().contains(searchedText.toString().toLowerCase()) || workOrder.getShortText().toLowerCase().contains(searchedText.toString().toLowerCase()) || (workOrder.TeamMember != null && workOrder.TeamMember.getName().toLowerCase().contains(searchedText))
                    || workOrder.getEquipNum().equalsIgnoreCase(searchedText.toString()) || workOrder.getFuncLocation().equalsIgnoreCase(searchedText.toString()))
                searchedWOs.add(workOrder);
        }
        return searchedWOs;
    }

    public static SupervisorWorkOrder getOrderByIdFromWorkOrders(List<SupervisorWorkOrder> orderList, String woNum) {
        for (SupervisorWorkOrder wo : orderList) {
            if (wo.getWorkOrderNum().equals(woNum))
                return wo;
        }
        return null;
    }

    //get methods
    public static ResponseObject getSupervisorWorkOrders(String woNum, ZAppSettings.FetchLevel fetchLevel, String orderByCriteria) {
        ResponseObject result = null;
        String entitySetName = ZCollections.SUPERVISOR_WO_COLLECTIONS;
        String resPath = entitySetName;
        String orderByUrl = "$orderby=";
        boolean fetchAddress = false;
        try {
            if (woNum == null)
                woNum = "";

            if (orderByCriteria == null || orderByCriteria.isEmpty()) {
                orderByCriteria = "BasicFnshDate";
            }
            orderByUrl += orderByCriteria;
            switch (fetchLevel) {
                case List:
                    resPath += "?$select=WorkOrderNum,Priority,ShortText,BasicStrtDate,EquipNum,NotificationNum,FuncLocation,AddressNumber,UserStatus,BasicFnshDate,Technician,OrderType,PostalCode" + "&" + orderByUrl;
                    break;
                case ListMap:
                    resPath += "?$select=WorkOrderNum,Priority,ShortText,BasicStrtDate,EquipNum,NotificationNum,FuncLocation,AddressNumber,UserStatus,BasicFnshDate,Technician,OrderType,PostalCode" + "&" + orderByUrl;
                    fetchAddress = true;
                    break;
                case Single:
                    resPath += "?$filter=WorkOrderNum eq '" + woNum + "'";
                    fetchAddress = true;
                    break;
                default:
                    resPath += "?" + orderByUrl;
                    fetchAddress = true;
                    break;
            }
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities, fetchAddress, fetchLevel);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(SupervisorWorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ResponseObject getSupervisorWorkOrdersCount(ZAppSettings.MobileStatus status, String priority, String technicianId) {
        ResponseObject result = null;
        String entitySetName = ZCollections.SUPERVISOR_WO_COLLECTIONS;
        String resPath = entitySetName;
        try {
            if (technicianId == null || technicianId.isEmpty())
                resPath += "/$count?$filter=Technician ne ''";
            else
                resPath += "/$count?$filter=Technician eq '" + technicianId + "'";

            if (priority != null && !priority.isEmpty()) {
                resPath += "and Priority eq '" + priority + "'";
            }
            if (status != null) {
                switch (status) {
                    case ACCEPT:
                        resPath += " and endswith(UserStatus, '" + status.getMobileStatusCode() + "') eq true and (startswith(UserStatus, '" + ZAppSettings.MobileStatus.CREATED.getMobileStatusCode() + "') eq true or startswith(UserStatus,'" + ZAppSettings.MobileStatus.RECEIVED.getMobileStatusCode() + "') eq true)";
                        break;
                    case RECEIVED:
                        resPath += " and endswith(UserStatus, '" + ZAppSettings.MobileStatus.ACCEPT.getMobileStatusCode() + "') eq false and (UserStatus eq '" + status.getMobileStatusCode() + "' or startswith(UserStatus, '" + ZAppSettings.MobileStatus.CREATED.getMobileStatusCode() + "') eq true)";
                        break;
                    default:
                        resPath += " and (startswith(UserStatus, '" + status.getMobileStatusCode() + "') eq true or endswith(UserStatus, '" + status.getMobileStatusCode() + "') eq true)";
                        break;
                }
            }
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                result.setContent(String.valueOf(result.Content()));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(SupervisorWorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ResponseObject getFilteredSupervisorWorkOrders(@NonNull String filterQuery, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        String entitySetName = ZCollections.SUPERVISOR_WO_COLLECTIONS;
        String resPath = entitySetName;
        String orderByUrl = "$orderby=BasicFnshDate";
        boolean fetchAddress = false;
        try {
            if (!filterQuery.isEmpty())
                resPath += filterQuery + "&";
            else
                resPath += "?";
            switch (fetchLevel) {
                case List:
                    resPath += "$select=WorkOrderNum,Priority,ShortText,BasicStrtDate,WOAddressNumber,UserStatus,BasicFnshDate,Technician" + "&" + orderByUrl;
                    break;
                case ListMap:
                    resPath += "$select=WorkOrderNum,Priority,ShortText,BasicStrtDate,WOAddressNumber,UserStatus,BasicFnshDate,Technician" + "&" + orderByUrl;
                    fetchAddress = true;
                    break;
                default:
                    resPath += orderByUrl;
                    fetchAddress = true;
                    break;
            }
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities, fetchAddress, fetchLevel);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(SupervisorWorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ResponseObject getFilteredSupervisorWorkOrders(@NonNull String filterQuery, ZAppSettings.FetchLevel fetchLevel, String orderByCriteria) {
        ResponseObject result = null;
        String entitySetName = ZCollections.SUPERVISOR_WO_COLLECTIONS;
        String resPath = entitySetName;
        String orderByUrl = "$orderby=";
        boolean fetchAddress = false;
        try {
            if (orderByCriteria == null || orderByCriteria.isEmpty())
                orderByCriteria = "BasicFnshDate";
            orderByUrl += orderByCriteria;
            if (!filterQuery.isEmpty())
                resPath += filterQuery + "&";
            else
                resPath += "?";
            switch (fetchLevel) {
                case List:
                    resPath += "$select=WorkOrderNum,Priority,ShortText,BasicStrtDate,EquipNum,NotificationNum,FuncLocation,AddressNumber,UserStatus,BasicFnshDate,Technician" + "&" + orderByUrl;
                    break;
                case ListMap:
                    resPath += "$select=WorkOrderNum,Priority,ShortText,BasicStrtDate,EquipNum,NotificationNum,FuncLocation,AddressNumber,UserStatus,BasicFnshDate,Technician" + "&" + orderByUrl;
                    fetchAddress = true;
                    break;
                default:
                    resPath += orderByUrl;
                    fetchAddress = true;
                    break;
            }
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities, fetchAddress, fetchLevel);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(SupervisorWorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ResponseObject getSupWorkOrderMeasurementPoints(SupervisorWorkOrder workOrder) {
        ResponseObject response = null;
        ArrayList<SupMeasurementPointReading> eqpPoints = new ArrayList<>();
        ArrayList<SupMeasurementPointReading> flPoints = new ArrayList<>();
        try {
            if (workOrder.getEquipNum() != null && !workOrder.getEquipNum().isEmpty()) {
                response = SupMeasurementPointReading.getEquipmentMeasurementPoint(workOrder.getEquipNum(), ZAppSettings.FetchLevel.List, null, null);
                if (response != null && !response.isError())
                    eqpPoints.addAll((ArrayList<SupMeasurementPointReading>) response.Content());
            }
            if (workOrder.getFuncLocation() != null && !workOrder.getFuncLocation().isEmpty()) {
                response = SupMeasurementPointReading.getFLMeasurementPoint(workOrder.getFuncLocation(), ZAppSettings.FetchLevel.List, null, null);
                if (response != null && !response.isError())
                    flPoints.addAll((ArrayList<SupMeasurementPointReading>) response.Content());
            }
            String entitySetName = ZCollections.SUPERVISOR_OPERATION_COLLECTIONS;
            String resPath = entitySetName;
            resPath += "?$filter=WorkOrderNum eq '" + workOrder.getWorkOrderNum() + "' and not startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "')&$select=Equipment,FuncLoc,OperationNum&$orderby=OperationNum";
            response = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (response != null && !response.isError()) {
                List<ODataEntity> oprEntities = (List<ODataEntity>) response.Content();
                if (oprEntities != null && oprEntities.size() > 0) {
                    for (ODataEntity entity : oprEntities) {
                        String eqpId = String.valueOf(entity.getProperties().get("Equipment").getValue());
                        String flId = String.valueOf(entity.getProperties().get("FuncLoc").getValue());
                        String oprNum = String.valueOf(entity.getProperties().get("OperationNum").getValue());

                        if (eqpId != null && !eqpId.isEmpty()) {
                            response = SupMeasurementPointReading.getEquipmentMeasurementPoint(eqpId, ZAppSettings.FetchLevel.List, "", oprNum);
                            if (response != null && !response.isError())
                                eqpPoints.addAll((ArrayList<SupMeasurementPointReading>) response.Content());
                        }

                        if (flId != null && !flId.isEmpty()) {
                            response = SupMeasurementPointReading.getFLMeasurementPoint(flId, ZAppSettings.FetchLevel.List, "", oprNum);
                            if (response != null && !response.isError())
                                flPoints.addAll((ArrayList<SupMeasurementPointReading>) response.Content());
                        }
                    }
                }
            }
            ArrayList<Object> objList = new ArrayList<>();
            for (SupMeasurementPointReading eqpPoint : eqpPoints) {
                objList.add(eqpPoint);
            }
            for (SupMeasurementPointReading flPoint : flPoints) {
                objList.add(flPoint);
            }
            if (response != null && !response.isError())
                response.setContent(objList);
        } catch (Exception e) {
            DliteLogger.WriteLog(SupervisorWorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            response = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return response;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities, boolean fetchAddress, ZAppSettings.FetchLevel fetchLevel) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<SupervisorWorkOrder> workOrders = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    workOrders.add(new SupervisorWorkOrder(entity, fetchAddress, fetchLevel));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", workOrders);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(SupervisorWorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
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

    public String getTechObjLocAndAssgnmnt() {
        return TechObjLocAndAssgnmnt;
    }

    public void setTechObjLocAndAssgnmnt(String techObjLocAndAssgnmnt) {
        TechObjLocAndAssgnmnt = techObjLocAndAssgnmnt;
    }

    public GregorianCalendar getSchdStrtDate() {
        return SchdStrtDate;
    }


    //----------------------------------------------------------------------------------------------

    public void setSchdStrtDate(GregorianCalendar schdStrtDate) {
        SchdStrtDate = schdStrtDate;
    }

    public GregorianCalendar getSchdFnshDate() {
        return SchdFnshDate;
    }

    public void setSchdFnshDate(GregorianCalendar schdFnshDate) {
        SchdFnshDate = schdFnshDate;
    }

    public String getBusAreaText() {
        return BusAreaText;
    }

    public void setBusAreaText(String busAreaText) {
        BusAreaText = busAreaText;
    }

    public String getBusArea() {
        return BusArea;
    }

    public void setBusArea(String busArea) {
        BusArea = busArea;
    }

    public String getControllingArea() {
        return ControllingArea;
    }

    public void setControllingArea(String controllingArea) {
        ControllingArea = controllingArea;
    }

    public String getWBSElem() {
        return WBSElem;
    }

    public void setWBSElem(String WBSElem) {
        this.WBSElem = WBSElem;
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getShortText() {
        return ShortText;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getMaintPlanningPlant() {
        return MaintPlanningPlant;
    }

    public void setMaintPlanningPlant(String maintPlanningPlant) {
        MaintPlanningPlant = maintPlanningPlant;
    }

    public String getMaintActivityType() {
        return MaintActivityType;
    }

    public void setMaintActivityType(String maintActivityType) {
        MaintActivityType = maintActivityType;
    }

    public String getNotificationNum() {
        return NotificationNum;
    }

    public void setNotificationNum(String notificationNum) {
        NotificationNum = notificationNum;
    }

    public String getMaintPlant() {
        return MaintPlant;
    }

    public void setMaintPlant(String maintPlant) {
        MaintPlant = maintPlant;
    }

    public GregorianCalendar getTechCompletionDate() {
        return TechCompletionDate;
    }

    public void setTechCompletionDate(GregorianCalendar techCompletionDate) {
        TechCompletionDate = techCompletionDate;
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

    public GregorianCalendar getBasicFnshDate() {
        return BasicFnshDate;
    }

    public void setBasicFnshDate(GregorianCalendar basicFnshDate) {
        BasicFnshDate = basicFnshDate;
    }

    public GregorianCalendar getBasicStrtDate() {
        return BasicStrtDate;
    }

    public void setBasicStrtDate(GregorianCalendar basicStrtDate) {
        BasicStrtDate = basicStrtDate;
    }

    public GregorianCalendar getActlStrtDate() {
        return ActlStrtDate;
    }

    public void setActlStrtDate(GregorianCalendar actlStrtDate) {
        ActlStrtDate = actlStrtDate;
    }

    public GregorianCalendar getActlFnshDate() {
        return ActlFnshDate;
    }

    public void setActlFnshDate(GregorianCalendar actlFnshDate) {
        ActlFnshDate = actlFnshDate;
    }

    public Time getBasicFnshTime() {
        return BasicFnshTime;
    }

    public void setBasicFnshTime(Time basicFnshTime) {
        BasicFnshTime = basicFnshTime;
    }

    public Time getBasicStrtTime() {
        return BasicStrtTime;
    }

    public void setBasicStrtTime(Time basicStrtTime) {
        BasicStrtTime = basicStrtTime;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getEquipNum() {
        return EquipNum;
    }

    public void setEquipNum(String equipNum) {
        EquipNum = equipNum;
    }

    public String getTechObjDescription() {
        return TechObjDescription;
    }

    public void setTechObjDescription(String techObjDescription) {
        TechObjDescription = techObjDescription;
    }

    public String getFuncLocation() {
        return FuncLocation;
    }

    public void setFuncLocation(String funcLocation) {
        FuncLocation = funcLocation;
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

    public String getAddressNumber() {
        return AddressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        AddressNumber = addressNumber;
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

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
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

    public String getTechnician() {
        return Technician;
    }

    public void setTechnician(String technician) {
        Technician = technician;
    }

    public String getMobileObjStatus() {
        return MobileObjStatus;
    }

    public void setMobileObjStatus(String mobileObjStatus) {
        MobileObjStatus = mobileObjStatus;
    }

    public Address getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Address defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public TeamMember getTeamMember() {
        return TeamMember;
    }

    public void setTeamMember(TeamMember teamMember) {
        this.TeamMember = teamMember;
    }

    public boolean isReadingOutOfRange() {
        return isReadingOutOfRange;
    }

    //helper methods
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

    public int getMobileObjStatusDrawable() {
        try {
            ZAppSettings.MobileStatus status = deriveWOStatus();
            if (status != null) {
                /*if(status == ZAppSettings.MobileStatus.RECEIVED || status == ZAppSettings.MobileStatus.ASSIGNED)
                    return R.drawable.download;
                if(status == ZAppSettings.MobileStatus.ACCEPT)
                    return R.drawable.accept;
                if(status == ZAppSettings.MobileStatus.ENROUTE)
                    return R.drawable.enrote;
                if(status == ZAppSettings.MobileStatus.ARRIVED)
                    return R.drawable.onsite;
                if(status == ZAppSettings.MobileStatus.START)
                    return R.drawable.start;
                if(status == ZAppSettings.MobileStatus.HOLD)
                    return R.drawable.hold;
                if(status == ZAppSettings.MobileStatus.COMPLETE)
                    return R.drawable.complete;
                if(status == ZAppSettings.MobileStatus.SUSPEND)
                    return R.drawable.suspend;
                if(status == ZAppSettings.MobileStatus.REJECT)
                    return R.drawable.reject;
                if(status == ZAppSettings.MobileStatus.TRANSFER)
                    return R.drawable.ic_trans_horiz;*/
                return status.getDrawableResId();
            }
            return R.drawable.download;
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            return R.drawable.download;
        }

    }

    public ZAppSettings.MobileStatus getDisplayableMobileObjStatus() {
        ZAppSettings.MobileStatus status = deriveWOStatus();
        return status == null ? ZAppSettings.MobileStatus.NotSet : status;
    }

    public ResponseObject create(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel, boolean fetchAddress) {
        ResponseObject result = null;
        try {
            super.create(entity);

            //set team member details
            try {
                result = com.ods.myjobcard_library.entities.supervisor.TeamMember.getTeamMembers(Technician);
                if (result != null && !result.isError()) {
                    this.setTeamMember(((ArrayList<TeamMember>) result.Content()).get(0));
                }
            } catch (Exception e) {
                DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
            }

            if (fetchAddress && AddressNumber != null && !AddressNumber.isEmpty()) {
                result = com.ods.myjobcard_library.entities.Address.getSupervisorWOAddress(AddressNumber);
                if (result != null && !result.isError()) {
                    defaultAddress = ((ArrayList<Address>) result.Content()).get(0);
                }
            }

            if (ZConfigManager.ENABLE_SUPERVISOR_READING_ALERT) {
                try {
                    String entitySetName;
                    String resPath;
                    int count = 0;
                    if (getEquipNum() != null && !getEquipNum().isEmpty()) {
                        entitySetName = ZCollections.SUPERVISOR_MEASPOINT_READING_COLLECTION;
                        resPath = entitySetName;
                        resPath += "/$count?$filter=Equipment eq '" + String.valueOf(Integer.parseInt(getEquipNum())) + "' and ((LimitMinChar ne '' and MeasReading lt LoMRLimit) or (LimitMaxChar ne '' and MeasReading gt UpMRLimit)) and PrevDoc eq true and ValCodeSuff eq false";
                        ResponseObject response = DataHelper.getInstance().getEntities(entitySetName, resPath);
                        if (response != null && !response.isError()) {
                            count = Integer.parseInt(String.valueOf(response.Content()));
                        }
                    }
                    if (count == 0 && getFuncLocation() != null && !getFuncLocation().isEmpty()) {
                        entitySetName = ZCollections.SUPERVISOR_MEASPOINT_READING_COLLECTION;
                        resPath = entitySetName;
                        resPath += "/$count?$filter=FunctionalLoc eq '" + getFuncLocation() + "' and ((LimitMinChar ne '' and MeasReading lt LoMRLimit) or (LimitMaxChar ne '' and MeasReading gt UpMRLimit)) and PrevDoc eq true and ValCodeSuff eq false";
                        ResponseObject response = DataHelper.getInstance().getEntities(entitySetName, resPath);
                        if (response != null && !response.isError()) {
                            count += Integer.parseInt(String.valueOf(response.Content()));
                        }
                    }
                    if (ZConfigManager.ENABLE_OPERATION_MEASUREMENTPOINT_READINGS) {
                        entitySetName = ZCollections.SUPERVISOR_OPERATION_COLLECTIONS;
                        resPath = entitySetName;
                        resPath += "?$filter=WorkOrderNum eq '" + getWorkOrderNum() + "' and not startswith(SystemStatus, '" + ZAppSettings.MobileStatus.Deleted.getMobileStatusCode() + "')&$select=Equipment,FuncLoc";
                        ResponseObject responseObject = DataHelper.getInstance().getEntities(entitySetName, resPath);
                        if (responseObject != null && !responseObject.isError()) {
                            List<ODataEntity> oprEntities = (List<ODataEntity>) responseObject.Content();
                            if (oprEntities != null && oprEntities.size() > 0) {
                                for (ODataEntity oprEntity : oprEntities) {
                                    String eqpId = String.valueOf(oprEntity.getProperties().get("Equipment").getValue());
                                    String flId = String.valueOf(oprEntity.getProperties().get("FuncLoc").getValue());

                                    if (eqpId != null && !eqpId.isEmpty()) {
                                        entitySetName = ZCollections.EQUIPMENT_MEASUREMENT_POINT_COLLECTION;
                                        resPath = entitySetName;
                                        resPath += "/$count?$filter=Equipment eq '" + String.valueOf(Integer.parseInt(eqpId)) + "' and ((LimitMinChar ne '' and MeasReading lt LoMRLimit) or (LimitMaxChar ne '' and MeasReading gt UpMRLimit)) and PrevDoc eq true and ValCodeSuff eq false";
                                        responseObject = DataHelper.getInstance().getEntities(entitySetName, resPath);
                                        if (responseObject != null && !responseObject.isError()) {
                                            count = Integer.parseInt(String.valueOf(responseObject.Content()));
                                        }
                                    }

                                    if (flId != null && !flId.isEmpty()) {
                                        entitySetName = ZCollections.FL_MEASUREMENT_POINT_COLLECTION;
                                        resPath = entitySetName;
                                        resPath += "/$count?$filter=FunctionalLoc eq '" + flId + "' and ((LimitMinChar ne '' and MeasReading lt LoMRLimit) or (LimitMaxChar ne '' and MeasReading gt UpMRLimit)) and PrevDoc eq true and ValCodeSuff eq false";
                                        ResponseObject response = DataHelper.getInstance().getEntities(entitySetName, resPath);
                                        if (response != null && !response.isError()) {
                                            count += Integer.parseInt(String.valueOf(response.Content()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (count > 0) {
                        isReadingOutOfRange = true;
                    }

                } catch (Exception e) {
                    DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private ZAppSettings.MobileStatus deriveWOStatus() {
        ZAppSettings.MobileStatus mobileStatus = null;
        String status = null;
        try {
            if (getMobileObjStatus() != null && getMobileObjStatus().length() > 0) {
                status = getMobileObjStatus();
            } else {
                if (UserStatus.equalsIgnoreCase(ZAppSettings.MobileStatus.CREATED.getMobileStatusCode()) ||
                        UserStatus.equalsIgnoreCase(ZAppSettings.MobileStatus.ASSIGNED.getMobileStatusCode())) {
                    //mobileStatus = ZAppSettings.MobileStatus.ASSIGNED;
                    //Set WO status as Received
//                    UpdateStatus(ZAppSettings.MobileStatus.RECEIVED,null,null);
                    mobileStatus = ZAppSettings.MobileStatus.RECEIVED;
                } else if (UserStatus.startsWith(ZAppSettings.MobileStatus.CREATED.getMobileStatusCode()) ||
                        UserStatus.startsWith(ZAppSettings.MobileStatus.RECEIVED.getMobileStatusCode())) {
                    String strDerivedStatus;
                    strDerivedStatus = UserStatus.substring(4).trim();
                    switch (strDerivedStatus) {
                        case "ACCP":
                            mobileStatus = ZAppSettings.MobileStatus.ACCEPT;
                            break;
                        case "REJC":
                            mobileStatus = ZAppSettings.MobileStatus.REJECT;
                            break;
                        default:
                            mobileStatus = ZAppSettings.MobileStatus.RECEIVED;
                            break;
                    }
                } else if (UserStatus.length() > 4) {
                    status = UserStatus.substring(0, 4).trim();
                } else {
                    status = UserStatus;
                }
            }

            //Derive the status for string scenarios
            if (status != null) {
                for (ZAppSettings.MobileStatus mobilestatus : ZAppSettings.MobileStatus.values()) {
                    if (status.length() == 4) {
                        if (mobilestatus.getMobileStatusCode().equalsIgnoreCase(status)) {
                            mobileStatus = mobilestatus;
                            break;
                        }
                    } else if (status.length() > 4) {
                        if (mobilestatus.getMobileStatusDesc().equalsIgnoreCase(status)) {
                            mobileStatus = mobilestatus;
                            break;
                        }
                    }
                }
            } else if (mobileStatus == null) {
                mobileStatus = ZAppSettings.MobileStatus.NotSet;
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            status = null;
            mobileStatus = ZAppSettings.MobileStatus.NotSet;
        }
        return mobileStatus;
    }
}
