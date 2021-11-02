package com.ods.myjobcard_library.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ResponseMasterModel;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.ctentities.SpinnerItem;
import com.ods.myjobcard_library.entities.ctentities.UserTable;
import com.ods.myjobcard_library.entities.ctentities.WorkCenter;
import com.ods.myjobcard_library.entities.forms.FormApproverSetModel;
import com.ods.myjobcard_library.entities.forms.FormResponseApprovalStatus;
import com.ods.myjobcard_library.entities.supervisor.SupervisorWorkOrder;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.entities.transaction.NotificationItem;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.myjobcard_library.viewmodels.online.OnlineDataList;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.StoreHelpers.TableConfigSet;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.client.odata.v4.EntityValue;
import com.sap.client.odata.v4.EntityValueList;
import com.sap.smp.client.odata.ODataEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import lecho.lib.hellocharts.model.SliceValue;

public class DashBoardViewModel extends BaseViewModel {

    private List<SliceValue> pieData;

    public ArrayList<SpinnerItem> dateFilterOptions = new ArrayList<>();
    public ArrayList<SpinnerItem> legendsMap = new ArrayList<>();
    public ArrayList<SpinnerItem> woSelectedValues1 = new ArrayList<>();
    public ArrayList<SpinnerItem> woSelectedValues2 = new ArrayList<>();
    private ArrayList<SpinnerItem> woStatuses = new ArrayList<>();
    private ArrayList<SpinnerItem> noStatuses = new ArrayList<>();
    private ArrayList<SpinnerItem> woPriorities = new ArrayList<>();
    private ArrayList<SpinnerItem> noPriorities = new ArrayList<>();
    private ArrayList<SpinnerItem> woWorkCenters = new ArrayList<>();
    private ArrayList<SpinnerItem> noWorkCenters = new ArrayList<>();
    private ArrayList<SpinnerItem> woMaintActivityTypes = new ArrayList<>();
    private ArrayList<SpinnerItem> woUserStatuses = new ArrayList<>();
    private ArrayList<SpinnerItem> noUserStatuses = new ArrayList<>();
    private ArrayList<SpinnerItem> woTechnicians = new ArrayList<>();
    private ArrayList<SpinnerItem> noWOConversion = new ArrayList<>();
    public ArrayList<SpinnerItem> filterCategories = new ArrayList<>();
    public ArrayList<SpinnerItem> woSysStatusSpinnerItems = new ArrayList<>();
    public ArrayList<SpinnerItem> noSysStatusSpinnerItems = new ArrayList<>();
    public ArrayList<SpinnerItem> woLocations = new ArrayList<>();
    public ArrayList<SpinnerItem> noLocations = new ArrayList<>();
    public ArrayList<SpinnerItem> woInspLotSpinnerItems = new ArrayList<>();
    public ArrayList<SpinnerItem> woTechIdentifNoItems = new ArrayList<>();
    public ArrayList<SpinnerItem> noTechIdentifNoItems = new ArrayList<>();
    public ArrayList<SpinnerItem> checkSheetSpinnerItems = new ArrayList<>();
    private boolean isWorkOrder = true;
    public String woSelectedCategory1 = "";
    public String woSelectedCategory2 = "";
    public String noSelectedCategory1 = "";
    public String noSelectedCategory2 = "";

    public ArrayList<SpinnerItem> createdByMeNO = new ArrayList<>();
    public ArrayList<SpinnerItem> createdByMeWo = new ArrayList<>();
    private String CurrentUserWorkCenter = "";
    //   private final WorkOrdersListLiveData woLiveData;

    public DashBoardViewModel(@NonNull Application application) {
        super(application);
        //Adding CreatedBy filter for work orders
        if (ZConfigManager.DOWNLOAD_CREATEDBY_WO.equalsIgnoreCase("X")) {
            createdByMeWo.add(new SpinnerItem("EnteredBy", "Created By Me"));

            int assignmentType = Integer.parseInt(UserTable.getUserWorkAssignmentType());
            if (assignmentType == 3 || assignmentType == 4)
                createdByMeWo.add(new SpinnerItem("MainWorkCtr", "Assigned To Me"));
            else
                createdByMeWo.add(new SpinnerItem("PersonResponsible", "Assigned To Me"));
        }
        //Adding CreatedBy filter for Notifications
        if (ZConfigManager.DOWNLOAD_CREATEDBY_NOTIF.equalsIgnoreCase("X")) {
            createdByMeNO.add(new SpinnerItem("EnteredBy", "Created By Me"));

            if (UserTable.getUserNotificationAssignmentType().equalsIgnoreCase("2"))
                createdByMeNO.add(new SpinnerItem("WorkCenter", "Assigned To Me"));
            else
                createdByMeNO.add(new SpinnerItem("Partner", "Assigned To Me"));
            CurrentUserWorkCenter = WorkCenter.getWorkCenterObjId(UserTable.getUserWorkCenter());
        }





        /*ResponseObject response = Priority.getPriorities();
        /*ResponseObject response = Priority.getPriorities();
        if(response != null && !response.isError()){
            woPriorities = (ArrayList<SpinnerItem>) response.Content();
        }*/
        setFilterData(true);
    }

    private MutableLiveData<ArrayList<WorkOrder>> onlineWoList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Operation>> onlineOprList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<WorkOrder>> filterListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<SliceValue>> ordersPieChartData = new MutableLiveData<>();
    private MutableLiveData<List<SliceValue>> notificationLiveData = new MutableLiveData<>();
    private MutableLiveData<List<SliceValue>> supervisorsLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Notification>> onlineNotifiList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<NotificationItem>> OnlineItemlist = new MutableLiveData<>();
    private ArrayList<WorkOrder> workOrders = new ArrayList<>();
    private ArrayList<Operation> operations = new ArrayList<>();
    private ArrayList<Notification> onlineNotifications = new ArrayList<>();
    private ArrayList<NotificationItem> onlineItemsList = new ArrayList<>();
    private static final String TAG = "DashBoardViewModel";

    public MutableLiveData<ArrayList<Notification>> getOnlineNotifiList() {
        return onlineNotifiList;
    }

    public void setOnlineNotifiList(MutableLiveData<ArrayList<Notification>> onlineNotifiList) {
        this.onlineNotifiList = onlineNotifiList;
    }

    public MutableLiveData<List<SliceValue>> getOrdersPieChartData() {
        return ordersPieChartData;
    }

    public MutableLiveData<List<SliceValue>> getNotificationLiveData() {
        return notificationLiveData;
    }

    public MutableLiveData<List<SliceValue>> getSupervisorsLiveData() {
        return supervisorsLiveData;
    }

    private HashMap<Integer, String> orderMap = new HashMap<>();
    private HashMap<Integer, String> notificationMap = new HashMap<>();


    public HashMap<Integer, String> getOrderMap() {
        return orderMap;
    }

    private void setOrderMap(HashMap<Integer, String> orderMap) {
        this.orderMap = orderMap;
    }

    private HashMap<Integer, String> supOrderMap = new HashMap<>();

    public HashMap<Integer, String> getNotificationMap() {
        return notificationMap;
    }

    public void setNotificationMap(HashMap<Integer, String> notificationMap) {
        this.notificationMap = notificationMap;
    }

    public void setSupOrderMap(HashMap<Integer, String> supOrderMap) {
        this.supOrderMap = supOrderMap;
    }

    public void resetLists() {
        this.woStatuses = new ArrayList<>();
        this.noStatuses = new ArrayList<>();
        this.woPriorities = new ArrayList<>();
        this.noPriorities = new ArrayList<>();
        this.woWorkCenters = new ArrayList<>();
        this.noWorkCenters = new ArrayList<>();
        this.woMaintActivityTypes = new ArrayList<>();
        this.woUserStatuses = new ArrayList<>();
        this.woTechnicians = new ArrayList<>();
        this.noWOConversion = new ArrayList<>();
        this.woSysStatusSpinnerItems = new ArrayList<>();
        this.woTechIdentifNoItems=new ArrayList<>();
        this.noTechIdentifNoItems=new ArrayList<>();
        this.checkSheetSpinnerItems=new ArrayList<>();
    }

    public HashMap<Integer, String> getSupOrderMap() {
        return supOrderMap;
    }

    public void setFilterData(boolean isWorkOrder) {
        this.isWorkOrder = isWorkOrder;
        this.filterCategories = new ArrayList<>();
        this.woSelectedValues1 = new ArrayList<>();
        this.woSelectedValues2 = new ArrayList<>();
        this.dateFilterOptions = new ArrayList<>();
        dateFilterOptions.add(new SpinnerItem("1", "Planned for tomorrow"));
        dateFilterOptions.add(new SpinnerItem("2", "Planned for next week"));
        if (isWorkOrder) {
            dateFilterOptions.add(new SpinnerItem("3", "Overdue for last "+ ZConfigManager.DASHBOARD_FILTER_WO_OVERDUE_FOR_LAST_DAYS +" days"));
            dateFilterOptions.add(new SpinnerItem("4", "Overdue for a week"));
            dateFilterOptions.add(new SpinnerItem("5", "All overdue"));
            dateFilterOptions.add(new SpinnerItem("6", "Created in last "+ ZConfigManager.DASHBOARD_FILTER_WO_CREATED_IN_LAST_DAYS +" days"));
            dateFilterOptions.add(new SpinnerItem("7", "Scheduling Compliant"));
            dateFilterOptions.add(new SpinnerItem("8", "Scheduling Non-compliant"));
            filterCategories.add(new SpinnerItem("0", "Select"));
            filterCategories.add(new SpinnerItem("Priority", "Priority"));
            filterCategories.add(new SpinnerItem("MobileObjStatus", "Status"));
            filterCategories.add(new SpinnerItem("UserStatus", "User Status"));
            filterCategories.add(new SpinnerItem("SysStatus", "System Status"));
            filterCategories.add(new SpinnerItem("MainWorkCtr", "WorkCenter"));
            filterCategories.add(new SpinnerItem("MaintActivityType", "Maint. Activity Type"));
            filterCategories.add(new SpinnerItem("InspectionLot", "Inspection Lot"));
            filterCategories.add(new SpinnerItem("FuncLocation", "Functional Location"));
            filterCategories.add(new SpinnerItem("Location", "Location"));
            filterCategories.add(new SpinnerItem("Date", "Date"));
            filterCategories.add(new SpinnerItem("CheckSheetStatus","CheckSheet Status"));
            filterCategories.add(new SpinnerItem("EquipNum", "Technical Id No."));
            //filterCategories.add(new SpinnerItem("InspectionLot","InspectionLot"));
            ArrayList<SpinnerItem> woPriorities = getPriorities();
            ArrayList<SpinnerItem> woStatuses = getStatuses();
            woSelectedCategory1 = "Priority";
            if (woPriorities.size() > 3) {
                woSelectedValues1.add(woPriorities.get(0));
                woSelectedValues1.add(woPriorities.get(1));
                woSelectedValues1.add(woPriorities.get(2));
            } else
                woSelectedValues1.addAll(woPriorities);
            woSelectedCategory2 = "MobileObjStatus";
            woSelectedValues2.addAll(woStatuses);
            if (ZConfigManager.DEFAULT_ASSIGNMENT_TYPE.equalsIgnoreCase(ZAppSettings.AssignmentType.OperationLevel.getAssignmentTypeText())) {
                filterCategories.add(new SpinnerItem("OperationTechnician", "Technician"));
            } else if (ZConfigManager.DEFAULT_ASSIGNMENT_TYPE.equalsIgnoreCase(ZAppSettings.AssignmentType.WorkOrderLevel.getAssignmentTypeText())) {
                filterCategories.add(new SpinnerItem("PersonResponsible", "Technician"));
            }
        } else {
            dateFilterOptions.add(new SpinnerItem("3", "Overdue for last "+ ZConfigManager.DASHBOARD_FILTER_NO_OVERDUE_FOR_LAST_DAYS +" days"));
            dateFilterOptions.add(new SpinnerItem("4", "Overdue for a week"));
            dateFilterOptions.add(new SpinnerItem("5", "All overdue"));
            dateFilterOptions.add(new SpinnerItem("6", "Created in last "+ ZConfigManager.DASHBOARD_FILTER_NO_CREATED_IN_LAST_DAYS +" days"));
            filterCategories.add(new SpinnerItem("0", "Select"));
            filterCategories.add(new SpinnerItem("Priority", "Priority"));
            filterCategories.add(new SpinnerItem("MobileStatus", "Status"));
            filterCategories.add(new SpinnerItem("NoUserStatus", "User Status"));
            filterCategories.add(new SpinnerItem("NoSystemStatus", "System Status"));
            filterCategories.add(new SpinnerItem("WorkCenter", "WorkCenter"));
            filterCategories.add(new SpinnerItem("WOConversion", "Order Conversion"));
            filterCategories.add(new SpinnerItem("Location", "Location"));
            filterCategories.add(new SpinnerItem("Date", "Date"));
            filterCategories.add(new SpinnerItem("Equipment", "Technical Id No."));
            ArrayList<SpinnerItem> noPriorities = getPriorities();
            ArrayList<SpinnerItem> noStatuses = getStatuses();
            woSelectedCategory1 = "Priority";
            if (noPriorities.size() > 3) {
                woSelectedValues1.add(noPriorities.get(0));
                woSelectedValues1.add(noPriorities.get(1));
                woSelectedValues1.add(noPriorities.get(2));
            } else
                woSelectedValues1.addAll(noPriorities);
            woSelectedCategory2 = "MobileStatus";
            woSelectedValues2.addAll(noStatuses);
        }
        if (ZConfigManager.DOWNLOAD_CREATEDBY_WO.equalsIgnoreCase("X") || ZConfigManager.DOWNLOAD_CREATEDBY_NOTIF.equalsIgnoreCase("X"))
            filterCategories.add(new SpinnerItem("CreatedBy", "Created By Me/Assigned To Me"));
    }

    public void setOrderData() {
        List<SliceValue> mWorkorderPieDataList = new ArrayList<>();
        mWorkorderPieDataList = getOrderData();
        ordersPieChartData.setValue(mWorkorderPieDataList);
    }

    public void setNotificationData() {
        List<SliceValue> mNotificationLiveDataList = new ArrayList<>();
        mNotificationLiveDataList = getNotificationData();
        notificationLiveData.setValue(mNotificationLiveDataList);
    }

    public void setSupervisorsData() {
        List<SliceValue> mSupervisoursLiveDataList = new ArrayList<>();
        mSupervisoursLiveDataList = getSupervisorsData();
        supervisorsLiveData.setValue(mSupervisoursLiveDataList);
    }

    private List<SliceValue> getNotificationData() {
        pieData = new ArrayList<>();
        int notificationCount;
        int count = 0;
        /*for (ZAppSettings.Priorities priority : ZAppSettings.Priorities.values()) {
            notificationCount = Notification.getNotificationCountByPriority(priority.getValue());
            if (notificationCount > 0) {
                pieData.add(new SliceValue(notificationCount, Color.parseColor(priority.getColorCode())));
                notificationMap.put(count, priority.getValue());
                count++;
            }
            setNotificationMap(notificationMap);
        }*/
        legendsMap = new ArrayList<>();
        for (SpinnerItem item1 : woSelectedValues1) {
            String filterQuery1 = "";
            if (woSelectedCategory1.equalsIgnoreCase("NoUserStatus"))
                filterQuery1 = "?$filter=indexof(UserStatus,'" + item1.getId() + "') ne -1";
            else if (woSelectedCategory1.equalsIgnoreCase("NoSystemStatus"))
                filterQuery1 = "?$filter=indexof(SystemStatus,'" + item1.getId() + "') ne -1";
            else if (woSelectedCategory1.equalsIgnoreCase("WOConversion"))
                filterQuery1 = "?$filter=" + getNotificationWOCreatedFilterQuery(item1.getId());
            else if (woSelectedCategory1.equalsIgnoreCase("date"))
                filterQuery1 = "?$filter=" + getNotificationDateFilterQuery(item1.getId());
            else if (woSelectedCategory1.equalsIgnoreCase("WorkCenter"))
                filterQuery1 = "?$filter=" + woSelectedCategory1 + " eq '" + item1.getObjectID() + "'";
            else if (woSelectedCategory1.equalsIgnoreCase("CreatedBy")) {
                filterQuery1 = "?$filter=" + getNotificationAssignmentQuery(item1.getId());
            } else
                filterQuery1 = "?$filter=" + woSelectedCategory1 + " eq '" + item1.getId() + "'";
            if (woSelectedValues2.size() > 0) {
                for (SpinnerItem item2 : woSelectedValues2) {
                    String filterQuery2 = "";
                    if (woSelectedCategory2.equalsIgnoreCase("NoUserStatus"))
                        filterQuery2 = filterQuery1 + " and (indexof(UserStatus,'" + item2.getId() + "') ne -1)";
                    else if (woSelectedCategory2.equalsIgnoreCase("NoSystemStatus"))
                        filterQuery2 = filterQuery1 + " and (indexof(SystemStatus,'" + item2.getId() + "') ne -1)";
                    else if (woSelectedCategory2.equalsIgnoreCase("WOConversion"))
                        filterQuery2 = filterQuery1 + " and " + getNotificationWOCreatedFilterQuery(item2.getId());
                    else if (woSelectedCategory2.equalsIgnoreCase("date"))
                        filterQuery2 = filterQuery1 + " and " + getNotificationDateFilterQuery(item2.getId());
                    else if (woSelectedCategory2.equalsIgnoreCase("WorkCenter"))
                        filterQuery2 = filterQuery1 + " and (" + woSelectedCategory2 + " eq '" + item2.getObjectID() + "')";
                    else if (woSelectedCategory2.equalsIgnoreCase("CreatedBy")) {
                        filterQuery2 = filterQuery1 + " and " + getNotificationAssignmentQuery(item2.getId());
                    } else {
                        filterQuery2 = filterQuery1 + " and (" + woSelectedCategory2 + " eq '" + item2.getId() + "')";
                    }
                    notificationCount = Notification.getNotificationCount(filterQuery2);
                    if (notificationCount > 0) {
                        notificationMap.put(count, filterQuery2);
                        int colorCount = count > 15 ? 0 : count;
                        ZAppSettings.ColorMap color = ZAppSettings.ColorMap.valueOf("C" + colorCount);
                        SliceValue slice = new SliceValue(notificationCount, Color.parseColor(color.getColorCode()));
                        pieData.add(slice);
                        legendsMap.add(new SpinnerItem(color.getColorCode(), (item2.getDescription() + " - " + item1.getDescription())));
                        count++;
                    }
                }
            } else {
                notificationCount = Notification.getNotificationCount(filterQuery1);
                if (notificationCount > 0) {
                    notificationMap.put(count, filterQuery1);
                    int colorCount = count > 15 ? 0 : count;
                    ZAppSettings.ColorMap color = ZAppSettings.ColorMap.valueOf("C" + colorCount);
                    SliceValue slice = new SliceValue(notificationCount, Color.parseColor(color.getColorCode()));
                    pieData.add(slice);
                    legendsMap.add(new SpinnerItem(color.getColorCode(), item1.getDescription()));
                    count++;
                }
            }
        }
        return pieData;
    }

    /*
    Always pass the Date filter values as selectedValues2  / selectedCategory2
     */
    private List<SliceValue> getOrderData() {
        pieData = new ArrayList<SliceValue>();
        int orderCount = 0;
        int count = 0;
        /*for (ZAppSettings.Priorities priority : ZAppSettings.Priorities.values()) {
            orderCount = WorkOrder.getWorkOrdersCountByPriority(priority.getValue());
            if (orderCount > 0) {
                //pieData.add(new SliceValue(orderCount, Color.parseColor(priority.getColorCode())));
                ZAppSettings.ColorMap color = ZAppSettings.ColorMap.valueOf("C"+count);
                SliceValue slice = new SliceValue(orderCount, Color.parseColor(color.getColorCode()));
                pieData.add(slice);
                orderMap.put(count, priority.getValue());
                count++;
            }
            setOrderMap(orderMap);
        }*/
        legendsMap = new ArrayList<>();
        try {
            for (SpinnerItem item1 : woSelectedValues1) {
                String filterQuery1 = "";
                if (woSelectedCategory1.equalsIgnoreCase("UserStatus"))
                    filterQuery1 = "?$filter=indexof(" + woSelectedCategory1 + ",'" + item1.getId() + "') ne -1";
                else if (woSelectedCategory1.equalsIgnoreCase("SysStatus"))
                    filterQuery1 = "?$filter=indexof(" + woSelectedCategory1 + ",'" + item1.getId() + "') ne -1";
                /*else if(woSelectedCategory1.equalsIgnoreCase("InspectionLot"))
                    filterQuery1="?$filter="+getWOInspFilterQuery(item1.getId());*/
                else if (woSelectedCategory1.equalsIgnoreCase("date"))
                    filterQuery1 = "?$filter=" + getWorkOrderDateFilterQuery(item1.getId());
                else if (woSelectedCategory1.equalsIgnoreCase("OperationTechnician"))
                    filterQuery1 = "?$filter=(" + ZCollections.WO_OPR_NAV_PROPERTY + "/any(d:d/PersonnelNo eq '" + item1.getId() + "'))";
                else if (woSelectedCategory1.equalsIgnoreCase("InspectionLot"))
                    filterQuery1 = "?$filter=" + getWOInspectionFilterQuery(item1.getId());
                else if (woSelectedCategory1.equalsIgnoreCase("FuncLocation"))
                    filterQuery1 = "?$filter=indexof(" + woSelectedCategory1 + ", '" + item1.getId() + "') ne -1";
                else if (woSelectedCategory1.equalsIgnoreCase("CreatedBy")) {
                    filterQuery1 = "?$filter=" + getOrderAssignmentQuery(item1.getId());
                } else if(woSelectedCategory1.equalsIgnoreCase("CheckSheetStatus"))
                    filterQuery1 = "?$filter=(" + getChecksheetStatusOrderCount(item1.getId()) + ")";
                else
                    filterQuery1 = "?$filter=" + woSelectedCategory1 + " eq '" + item1.getId() + "'";
                if (woSelectedValues2.size() > 0) {
                    for (SpinnerItem item2 : woSelectedValues2) {
                        String filterQuery2 = "";
                        if (woSelectedCategory2.equalsIgnoreCase("UserStatus"))
                            filterQuery2 = filterQuery1 + " and (indexof(" + woSelectedCategory2 + ",'" + item2.getId() + "') ne -1)";
                        else if (woSelectedCategory2.equalsIgnoreCase("SysStatus"))
                            filterQuery2 = filterQuery1 + " and (indexof(" + woSelectedCategory2 + ",'" + item2.getId() + "') ne -1)";
                        /*else if(woSelectedCategory2.equalsIgnoreCase("InspectionLot"))
                            filterQuery1="?$filter="+getWOInspFilterQuery(item2.getId());*/
                        else if (woSelectedCategory2.equalsIgnoreCase("date"))
                            filterQuery2 = filterQuery1 + " and " + getWorkOrderDateFilterQuery(item2.getId());
                        else if (woSelectedCategory2.equalsIgnoreCase("OperationTechnician"))
                            filterQuery2 = filterQuery1 + " and (" + ZCollections.WO_OPR_NAV_PROPERTY + "/any(d:d/PersonnelNo eq '" + item2.getId() + "'))";
                        else if (woSelectedCategory2.equalsIgnoreCase("InspectionLot"))
                            filterQuery2 = filterQuery1 + " and " + getWOInspectionFilterQuery(item2.getId());
                        else if (woSelectedCategory2.equalsIgnoreCase("FuncLocation"))
                            filterQuery2 = filterQuery1 + " and (indexof(" + woSelectedCategory2 + ", '" + item2.getId() + "') ne -1)";
                        else if (woSelectedCategory2.equalsIgnoreCase("CreatedBy")) {
                            filterQuery2 = filterQuery1 + " and " + getOrderAssignmentQuery(item2.getId());
                        } else if(woSelectedCategory2.equalsIgnoreCase("CheckSheetStatus"))
                            filterQuery2 = filterQuery1 + " and (" + getChecksheetStatusOrderCount(item2.getId()) + ")";
                        else {
                            filterQuery2 = filterQuery1 + " and (" + woSelectedCategory2 + " eq '" + item2.getId() + "')";
                        }
                        orderCount = WorkOrder.getWorkOrdersCount(filterQuery2);
                        if (orderCount > 0) {
                            orderMap.put(count, filterQuery2);
                            int colorCount = count > 15 ? 0 : count;
                            ZAppSettings.ColorMap color = ZAppSettings.ColorMap.valueOf("C" + colorCount);
                            SliceValue slice = new SliceValue(orderCount, Color.parseColor(color.getColorCode()));
                            pieData.add(slice);
                            legendsMap.add(new SpinnerItem(color.getColorCode(), (item2.getDescription() + " - " + item1.getDescription())));
                            count++;
                        }
                    }
                } else {
                    orderCount = WorkOrder.getWorkOrdersCount(filterQuery1);
                    if (orderCount > 0) {
                        orderMap.put(count, filterQuery1);
                        int colorCount = count > 15 ? 0 : count;
                        ZAppSettings.ColorMap color = ZAppSettings.ColorMap.valueOf("C" + colorCount);
                        SliceValue slice = new SliceValue(orderCount, Color.parseColor(color.getColorCode()));
                        pieData.add(slice);
                        legendsMap.add(new SpinnerItem(color.getColorCode(), item1.getDescription()));
                        count++;
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return pieData;
    }

    private String getWorkOrderDateFilterQuery(String selectedDateFilterValue) {
        String filterQuery = "";
        GregorianCalendar currDate = ZCommon.getDeviceDateTime();
        GregorianCalendar date = ZCommon.getDeviceDateTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(ZConfigManager.QUERIABLE_DATE_FORMAT, Locale.getDefault());
        String formattedDate;
        if ("1".equals(selectedDateFilterValue)) {
            date.add(Calendar.DAY_OF_MONTH, 2);
            formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
            filterQuery = "(BasicStrtDate lt datetime'" + formattedDate + "' and BasicStrtDate ge datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
        } else if ("2".equals(selectedDateFilterValue)) {
            date.add(Calendar.DAY_OF_MONTH, 8);
            formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
            filterQuery = "(BasicStrtDate lt datetime'" + formattedDate + "' and BasicStrtDate ge datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
        } else if ("3".equals(selectedDateFilterValue)) {
            date.add(Calendar.DAY_OF_MONTH, -ZConfigManager.DASHBOARD_FILTER_WO_OVERDUE_FOR_LAST_DAYS);
            currDate.add(Calendar.DAY_OF_MONTH, 1);
            formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
            filterQuery = "(BasicFnshDate gt datetime'" + formattedDate + "' and BasicFnshDate lt datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
        } else if ("4".equals(selectedDateFilterValue)) {
            date.add(Calendar.DAY_OF_MONTH, -7);
            currDate.add(Calendar.DAY_OF_MONTH, 1);
            formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
            filterQuery = "(BasicFnshDate gt datetime'" + formattedDate + "' and BasicFnshDate lt datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
        } else if ("5".equals(selectedDateFilterValue)) {
            currDate.add(Calendar.DAY_OF_MONTH, 1);
            filterQuery = "(BasicFnshDate lt datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
        } else if ("6".equals(selectedDateFilterValue)) {
            date.add(Calendar.DAY_OF_MONTH, -ZConfigManager.DASHBOARD_FILTER_WO_CREATED_IN_LAST_DAYS);
            currDate.add(Calendar.DAY_OF_MONTH, 1);
            formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
            filterQuery = "(CreatedOn gt datetime'" + formattedDate + "' and CreatedOn lt datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
        } else if("7".equalsIgnoreCase(selectedDateFilterValue)){
            filterQuery = "(ActlFnshDate le BasicFnshDate)";
        } else if("8".equalsIgnoreCase(selectedDateFilterValue)){
            filterQuery = "(ActlFnshDate gt BasicFnshDate)";
        }
        return filterQuery;
    }

    private String getNotificationDateFilterQuery(String selectedDateFilterValue) {
        String filterQuery = "";
        GregorianCalendar currDate = ZCommon.getDeviceDateTime();
        GregorianCalendar date = ZCommon.getDeviceDateTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(ZConfigManager.QUERIABLE_DATE_FORMAT, Locale.getDefault());
        String formattedDate = "";
        if ("1".equals(selectedDateFilterValue)) {
            date.add(Calendar.DAY_OF_MONTH, 2);
            formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
            filterQuery = "(RequiredStartDate lt datetime'" + formattedDate + "' and RequiredStartDate ge datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
        } else if ("2".equals(selectedDateFilterValue)) {
            date.add(Calendar.DAY_OF_MONTH, 8);
            formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
            filterQuery = "(RequiredStartDate lt datetime'" + formattedDate + "' and RequiredStartDate ge datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
        } else if ("3".equals(selectedDateFilterValue)) {
            date.add(Calendar.DAY_OF_MONTH, -ZConfigManager.DASHBOARD_FILTER_NO_OVERDUE_FOR_LAST_DAYS);
            currDate.add(Calendar.DAY_OF_MONTH, 1);
            formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
            filterQuery = "(RequiredEndDate gt datetime'" + formattedDate + "' and RequiredEndDate lt datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
        } else if ("4".equals(selectedDateFilterValue)) {
            date.add(Calendar.DAY_OF_MONTH, -7);
            currDate.add(Calendar.DAY_OF_MONTH, 1);
            formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
            filterQuery = "(RequiredEndDate gt datetime'" + formattedDate + "' and RequiredEndDate lt datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
        } else if ("5".equals(selectedDateFilterValue)) {
            currDate.add(Calendar.DAY_OF_MONTH, 1);
            filterQuery = "(RequiredEndDate lt datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
        } else if ("6".equals(selectedDateFilterValue)) {
            date.add(Calendar.DAY_OF_MONTH, -ZConfigManager.DASHBOARD_FILTER_NO_CREATED_IN_LAST_DAYS);
            currDate.add(Calendar.DAY_OF_MONTH, 1);
            formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
            filterQuery = "(CreatedOn gt datetime'" + formattedDate + "' and CreatedOn lt datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
        }
        return filterQuery;
    }

    private String getNotificationWOCreatedFilterQuery(String selectedValue) {
        String filterQuery = "";
        if ("0".equals(selectedValue)) { // order created
            filterQuery = "(WorkOrderNum ne '')";
        } else if ("1".equals(selectedValue)) { // order not created
            filterQuery = "(WorkOrderNum eq '')";
        }
        return filterQuery;
    }


    /**
     * @param selectedValue selected option from the drop down list
     * @return filter query for QM inspection completed or not based upon selected option
     */
    private String getWOInspectionFilterQuery(String selectedValue){
        String filterQuery="";
        if("0".equals(selectedValue)) // inspection completed
            filterQuery = "(" + ZCollections.WO_OPR_NAV_PROPERTY + "/all(d:indexof(tolower(d/SystemStatus),'"+ ZConfigManager.OPR_INSP_ENABLE_STATUS.toLowerCase() +"') ne -1 and indexof(tolower(d/SystemStatus),'"+ ZConfigManager.OPR_INSP_RESULT_RECORDED_STATUS.toLowerCase() +"') ne -1))";
        else if("1".equals(selectedValue)) // inspection pending
            filterQuery = "(" + ZCollections.WO_OPR_NAV_PROPERTY + "/all(d:indexof(tolower(d/SystemStatus),'"+ ZConfigManager.OPR_INSP_ENABLE_STATUS.toLowerCase() +"') ne -1 and indexof(tolower(d/SystemStatus),'"+ ZConfigManager.OPR_INSP_RESULT_RECORDED_STATUS.toLowerCase() +"') eq -1))";
        return filterQuery;
    }

    /** getting orderCount for selected checksheet status
     * @return
     */
    private String getChecksheetStatusOrderCount(String selectedValue){
        StringBuilder filterQuery = new StringBuilder();
        ResponseObject response=null;
        HashSet<String> workOrders = new HashSet<>();
        String formStatusToBeChecked = "";
        if(selectedValue.equals("0"))
            formStatusToBeChecked = "APPROVE";
        else if(selectedValue.equals("1") || selectedValue.equals("3"))
            formStatusToBeChecked = "REJECT";
        String entitySetName = ZCollections.FORMS_RESPONSE_CAPTURE_COLLECTION;
        String resPath = entitySetName + "?$filter=IsDraft eq ''&$select=FormID,Version,InstanceID,Counter,WoNum";
        response = DataHelper.getInstance().getEntities(entitySetName, resPath);
        if(response != null && !response.isError()) {
            List<ODataEntity> entities = ZBaseEntity.setODataEntityList(response.Content());
            for(ODataEntity entity : entities) {
                ResponseMasterModel formResponse = new ResponseMasterModel(entity);
                entitySetName = ZCollections.FROM_APPROVER_ENTITY_SET;
                resPath = entitySetName + "?$filter=WorkOrderNum eq '"+ formResponse.getWoNum() +"' and FormID eq '"+ formResponse.getFormID() +"' and Version eq '"+ formResponse.getVersion() +"' &$select=ApproverID";
                response = DataHelper.getInstance().getEntities(entitySetName, resPath);
                if(response != null && !response.isError()){
                    List<ODataEntity> reviewerEntities = ZBaseEntity.setODataEntityList(response.Content());
                    entitySetName = ZCollections.FORM_RESPONSE_APPROVAL_STATUS_ENTITY_SET;
                    for(ODataEntity reviewerEntity : reviewerEntities){
                        FormApproverSetModel formReviewer = new FormApproverSetModel(new ZODataEntity(reviewerEntity));
                        resPath = entitySetName + "?$filter=(FormID eq '"+ formResponse.getFormID() +"' and Version eq '"+ formResponse.getVersion() +"' and FormInstanceID eq '"+ formResponse.getInstanceID() +"' and Counter eq '"+ formResponse.getCounter() +"' and ApproverID eq '"+ formReviewer.getApproverID() +"')&$select=FormContentStatus,IterationRequired";
                        response = DataHelper.getInstance().getEntities(entitySetName, resPath);
                        if(response != null && !response.isError()) {
                            List<ODataEntity> statusEntities = ZBaseEntity.setODataEntityList(response.Content());
                            if(statusEntities.size() > 0) {
                                FormResponseApprovalStatus statusEntity = new FormResponseApprovalStatus(statusEntities.get(0));
                                if(!formStatusToBeChecked.isEmpty() && statusEntity.getFormContentStatus().equalsIgnoreCase(formStatusToBeChecked)){
                                    if((selectedValue.equals("3") && statusEntity.getIterationRequired().equalsIgnoreCase("x")) || selectedValue.equals("0") || (selectedValue.equals("1") && statusEntity.getIterationRequired().isEmpty()))
                                        workOrders.add(formResponse.getWoNum());
                                }
                            } else if(selectedValue.equals("2")){
                                workOrders.add(formResponse.getWoNum());
                            }
                        }
                    }
                }
            }
        }
        if(workOrders.size() > 0) {
            for (String order : workOrders) {
                filterQuery.append((filterQuery.length() == 0) ? "" : " or ");
                filterQuery.append("WorkOrderNum eq '").append(order).append("'");
            }
        } else {
            filterQuery.append("WorkOrderNum eq ''");
        }

        /*int orderCnt=0;
        ArrayList<String> woNumbersList=new ArrayList<>();
        String formType=ZAppSettings.FormAssignmentType.getFormAssignmentType(ZConfigManager.FORM_ASSIGNMENT_TYPE);
        HashMap<String,Integer> apprRejCheckSheetCount=new HashMap<>();
        ArrayList<WorkOrder> workOrders=new ArrayList<>();
        response = WorkOrder.getWorkOrders(ZAppSettings.FetchLevel.List, null, null);
        workOrders = (ArrayList<WorkOrder>) response.Content();
        if(ZCommon.isPredefinedFormVisible(formType)) {
            for(WorkOrder workOrder:workOrders){
                apprRejCheckSheetCount=WorkOrder.getPredefinedApprovedandRejectedFormsCount(formType,workOrder.getWorkOrderNum(),workOrder.getOrderType(),true);
                if("0".equals(selectedValue))
                    orderCnt=orderCnt+apprRejCheckSheetCount.get("APPROVE");
                else if("1".equals(selectedValue))
                    orderCnt=orderCnt+apprRejCheckSheetCount.get("REJECT");
                else
                    orderCnt=orderCnt+apprRejCheckSheetCount.get("Not reviewed");
                woNumbersList.add(workOrder.getWorkOrderNum());
            }
            if(orderCnt>0) {
                Set<String> woStr = new HashSet<String>(woNumbersList);
                woNumbersList.addAll(woStr);
            }
        }
        if(ZCommon.isManualAssignedFormsVisible(formType)){
            for(WorkOrder workOrder:workOrders){
                apprRejCheckSheetCount=WorkOrder.getManualApprovedandRejectedFormsCount(formType,workOrder.getWorkOrderNum(),true);
                if("0".equals(selectedValue))
                    orderCnt=orderCnt+apprRejCheckSheetCount.get("APPROVE");
                else if("1".equals(selectedValue))
                    orderCnt=orderCnt+apprRejCheckSheetCount.get("REJECT");
                else
                    orderCnt=orderCnt+apprRejCheckSheetCount.get("Not reviewed");
                if(orderCnt>0) {
                    Set<String> woStr = new HashSet<String>(woNumbersList);
                    woNumbersList.addAll(woStr);
                }
            }
        }*/
        return filterQuery.toString();
    }

    private String getOrderAssignmentQuery(String selectedValue){
        StringBuilder query = new StringBuilder();

        if (selectedValue.equalsIgnoreCase("EnteredBy"))
            query = new StringBuilder("(" + selectedValue + " eq '" + ZAppSettings.strUser + "')");
        else if (selectedValue.equalsIgnoreCase("PersonResponsible"))
            query = new StringBuilder("(" + selectedValue + " eq '" + UserTable.getUserPersonnelNumber() + "')");
        else if (selectedValue.equalsIgnoreCase("MainWorkCtr")) {
            if(!UserTable.getUserWorkCenters().isEmpty()) {
                for (String workCenter : UserTable.getUserWorkCenters()) {
                    query.append((query.length() == 0) ? "(" : " or ");
                    query.append(selectedValue).append(" eq '").append(workCenter).append("'");
                }
                query.append(")");
            }
        }

        return query.toString();
    }

    private String getNotificationAssignmentQuery(String selectedValue){
        StringBuilder query = new StringBuilder();

        if (selectedValue.equalsIgnoreCase("EnteredBy"))
            query = new StringBuilder("(" + selectedValue + " eq '" + ZAppSettings.strUser + "')");
        else if (selectedValue.equalsIgnoreCase("Partner"))
            query = new StringBuilder("(" + selectedValue + " eq '" + Integer.valueOf(UserTable.getUserPersonnelNumber()) + "')");
        else if (selectedValue.equalsIgnoreCase("WorkCenter")) {
            if(!UserTable.getUserWorkCenterObjectIds().isEmpty()) {
                for (String objectId : UserTable.getUserWorkCenterObjectIds()) {
                    query.append((query.length() == 0) ? "(" : " or ");
                    query.append(selectedValue).append(" eq '").append(objectId).append("'");
                }
                query.append(")");
            }
        }
        return query.toString();
    }

    private List<SliceValue> getSupervisorsData() {
        pieData = new ArrayList<SliceValue>();
        int supOrderCount;
        int count = 0;
        for (ZAppSettings.Priorities priority : ZAppSettings.Priorities.values()) {
            supOrderCount = Integer.valueOf((String) (SupervisorWorkOrder.getSupervisorWorkOrdersCount(null, priority.getValue(), null).Content()));
            if (supOrderCount > 0) {
                pieData.add(new SliceValue(supOrderCount, Color.parseColor(priority.getColorCode())));
                supOrderMap.put(count, priority.getValue());
                count++;
            }
            setSupOrderMap(supOrderMap);
        }
        return pieData;
    }

    public MutableLiveData<ArrayList<WorkOrder>> getOnlineWoList() {
        return onlineWoList;
    }

    public void setOnlineWoList(MutableLiveData<ArrayList<WorkOrder>> onlineWoList) {
        this.onlineWoList = onlineWoList;
    }

    public MutableLiveData<ArrayList<Operation>> getOnlineOprList() {
        return onlineOprList;
    }

    public void setOnlineOprList(MutableLiveData<ArrayList<Operation>> onlineOprList) {
        this.onlineOprList = onlineOprList;
    }

    @SuppressLint("StaticFieldLeak")
    public void fetchOnlineData(String filterQuery, Boolean isWoSearch) {
        String entitySetName;
        if (isWoSearch)
            entitySetName = "WoHeaderSet";
            //filterQuery = "?$filter=(OnlineSearch eq 'X' and Unassigned eq '' and CreatedOn eq datetime'2020-02-20T00:00:00' and ChangeDtForOrderMaster eq datetime'2020-03-05T00:00:00' and Plant eq 'OD01' and MainWorkCtr eq '0DEE0001')&$expand=NAVOPERA";

        else
            entitySetName = "NotificationHeaderSet";

        //filterQueryWo="?$filter=(OnlineSearch eq 'X' and CreatedOn eq datetime'2020-01-21T00:00:00' and ChangeDtForOrderMaster eq datetime'2020-01-22T00:00:00')&$expand=NAVOPERA";
        String resPath = entitySetName + filterQuery;
        try {
            new AsyncTask<Void, Void, ResponseObject>() {

                @Override
                protected ResponseObject doInBackground(Void... voids) {
                    ResponseObject result = DataHelper.getInstance().getEntitiesOnline(resPath, entitySetName, TableConfigSet.getStore(entitySetName));
                    return result;
                }

                @Override
                protected void onPostExecute(ResponseObject responseObject) {
                    super.onPostExecute(responseObject);

                    try {
                        if (isWoSearch)
                            getWorkOrdersList(responseObject);
                        else
                            getNotificationsList(responseObject);
                    } catch (Exception e) {
                        DliteLogger.WriteLog(DashBoardViewModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
                    }
                    //updateUI(onlineworkorders);


                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            Log.e(TAG, "fetchOnlineData: Exception" + e.getMessage());
        }

    }

    private void getWorkOrdersList(ResponseObject responseObject) {
        if (!responseObject.isError()) {
            operations.clear();
            workOrders.clear();
            EntityValueList entityList = (EntityValueList) responseObject.Content();
            EntityValueList oprEntityList;
            ArrayList<WorkOrder> onlineworkOrders = new ArrayList<>();
            ArrayList<Operation> workOrderOperations;
            for (EntityValue entityValue : entityList) {

                WorkOrder order = new WorkOrder(entityValue);
                oprEntityList = entityValue.getEntityType().getProperty("NAVOPERA").getEntityList(entityValue);

                workOrderOperations = new ArrayList<>();
                for (EntityValue oprEntity : oprEntityList) {
                    workOrderOperations.add(new Operation(oprEntity));
                    operations.add(new Operation(oprEntity));
                }
                order.setWorkOrderOperations(workOrderOperations);
                onlineworkOrders.add(order);
                //oprEntityValueList.add( entityValue.getEntityType().getProperty("NAVOPERA").getEntityList(entityValue));
            }
            workOrders.addAll(onlineworkOrders);
            OnlineDataList.getInstance().setOnlineWorkOrderList(workOrders);
            OnlineDataList.getInstance().setWorkOrdersOperationsList(operations);
        } else {
            setError(responseObject.getMessage());
            Log.d(TAG, "onPostExecute: " + responseObject.getMessage());
        }

        if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)
            onlineOprList.setValue(operations);
        else
            onlineWoList.setValue(workOrders);
    }

    private void getNotificationsList(ResponseObject responseObject) {
        if (!responseObject.isError()) {
            onlineItemsList.clear();
            onlineNotifications.clear();

            EntityValueList entityList = (EntityValueList) responseObject.Content();
            EntityValueList oprEntityList;
            ArrayList<Notification> notifications = new ArrayList<>();
            ArrayList<NotificationItem> notificationItems;
            for (EntityValue entityValue : entityList) {

                Notification notification = new Notification(entityValue);
                oprEntityList = entityValue.getEntityType().getProperty("NavNOItem").getEntityList(entityValue);

                notificationItems = new ArrayList<>();
                for (EntityValue oprEntity : oprEntityList) {
                    notificationItems.add(new NotificationItem(oprEntity));
                    onlineItemsList.add(new NotificationItem(oprEntity));
                    Log.d(TAG, "onPostExecute: Notification items" + notification.getNotification() + " item num");
                }
                notification.setNotificationItems(notificationItems);
                notifications.add(notification);

                //oprEntityValueList.add( entityValue.getEntityType().getProperty("NAVOPERA").getEntityList(entityValue));
                Log.d(TAG, "onPostExecute: Notification" + notification.toString());
            }
            onlineNotifications.addAll(notifications);
            OnlineDataList.getInstance().setOnLineNotifications(notifications);
            OnlineDataList.getInstance().setOnlineNotificationItems(onlineItemsList);
        } else {
            setError(responseObject.getMessage() != null ? responseObject.getMessage() : responseObject.Content().toString());
            Log.d(TAG, "onPostExecute: " + responseObject.getMessage());
        }
        onlineNotifiList.setValue(onlineNotifications);
    }

    public ArrayList<SpinnerItem> getStatuses() {
        if (!isWorkOrder) {
            if (noStatuses.size() == 0)
                noStatuses = Notification.getSpinnerStatuses();
            return noStatuses;
        } else {
            if (woStatuses.size() == 0)
                woStatuses = WorkOrder.getSpinnerStatuses();
            return woStatuses;
        }
    }

    public ArrayList<SpinnerItem> getPriorities() {
        if (!isWorkOrder) {
            if (noPriorities.size() == 0)
                noPriorities = Notification.getSpinnerPriorities();
            return noPriorities;
        } else {
            if (woPriorities.size() == 0)
                woPriorities = WorkOrder.getSpinnerPriorities();
            return woPriorities;
        }
    }

    public ArrayList<SpinnerItem> getWorkCenters() {
        if (!isWorkOrder) {
            if (noWorkCenters.size() == 0)
                noWorkCenters = Notification.getSpinnerWorkCenters();
            return noWorkCenters;
        } else {
            if (woWorkCenters.size() == 0)
                woWorkCenters = WorkOrder.getSpinnerWorkCenters();
            return woWorkCenters;
        }
    }

    public ArrayList<SpinnerItem> getWoMaintActivityTypes() {
        if (woMaintActivityTypes.size() == 0)
            woMaintActivityTypes = WorkOrder.getSpinnerPMActivityTypes();
        return woMaintActivityTypes;
    }

    public ArrayList<SpinnerItem> getWoUserStatuses() {
        if (woUserStatuses.size() == 0)
            woUserStatuses = WorkOrder.getSpinnerUserStatuses();
        return woUserStatuses;
    }

    /**
     * @return ArrayList of SpinnerItems for all distinct User Statuses in Notifications
     */
    public ArrayList<SpinnerItem> getNoUserStatuses(){
        if (noUserStatuses.size() == 0)
            noUserStatuses = Notification.getSpinnerUserStatuses();
        return noUserStatuses;
    }

    public ArrayList<SpinnerItem> getWoTechnicians() {
        if (woTechnicians.size() == 0) {
            if (ZConfigManager.DEFAULT_ASSIGNMENT_TYPE.equalsIgnoreCase(ZAppSettings.AssignmentType.OperationLevel.getAssignmentTypeText())) {
                woTechnicians = WorkOrder.getSpinnerOperationTechnicians();
            } else if (ZConfigManager.DEFAULT_ASSIGNMENT_TYPE.equalsIgnoreCase(ZAppSettings.AssignmentType.WorkOrderLevel.getAssignmentTypeText())) {
                woTechnicians = WorkOrder.getSpinnerWorkOrderTechnicians();
            }
        }
        return woTechnicians;
    }

    public ArrayList<SpinnerItem> getNoWOConversion() {
        if(noWOConversion.size() == 0) {
            noWOConversion.add(new SpinnerItem("0", "Order Created"));
            noWOConversion.add(new SpinnerItem("1", "Order Not Created"));
        }
        return noWOConversion;
    }

    public ArrayList<SpinnerItem> getWoInspLotSpinnerItems() {
        if(woInspLotSpinnerItems.size() == 0) {
            woInspLotSpinnerItems.add(new SpinnerItem("0", "Inspection Completed"));
            woInspLotSpinnerItems.add(new SpinnerItem("1", "Inspection Pending"));
        }
        return woInspLotSpinnerItems;
    }

    public ArrayList<SpinnerItem> getCheckSheetStatusSpinnerItems() {
        if(checkSheetSpinnerItems.size() == 0) {
            checkSheetSpinnerItems.add(new SpinnerItem("0", "Approved"));
            checkSheetSpinnerItems.add(new SpinnerItem("1", "Rejected"));
            checkSheetSpinnerItems.add(new SpinnerItem("2", "Not reviewed"));
            checkSheetSpinnerItems.add(new SpinnerItem("3", "Correction Required"));
            //checkSheetSpinnerItems.add(new SpinnerItem("4", "Not submitted"));
        }
        return checkSheetSpinnerItems;
    }
    /**
     * @return ArrayList of SpinnerItems for all distinct TechIdentification number for WorkOrder and  Notifications based on selected filter
     */
    public ArrayList<SpinnerItem> getTechIdentificationNoItems(){
        if(isWorkOrder) {
            if (woTechIdentifNoItems.size() == 0) {
                woTechIdentifNoItems = WorkOrder.getSpinnerEquipmentTechIDs();
            }
            return woTechIdentifNoItems;
        }
        else {
            if (noTechIdentifNoItems.size() == 0) {
                noTechIdentifNoItems = Notification.getSpinnerEquipmentTechIDs();
            }
            return noTechIdentifNoItems;
        }
    }
    public ArrayList<SpinnerItem> getWOSysStatus() {
        if (woSysStatusSpinnerItems.size() == 0)
            woSysStatusSpinnerItems = WorkOrder.getSpinnerSysStatuses();
        return woSysStatusSpinnerItems;
    }

    /**
     * @return ArrayList of SpinnerItems for all distinct System Statuses in Notifications
     */
    public ArrayList<SpinnerItem> getNOSysStatus() {
        if (noSysStatusSpinnerItems.size() == 0)
            noSysStatusSpinnerItems = Notification.getSpinnerSysStatuses();
        return noSysStatusSpinnerItems;
    }

    /**
     * @return ArrayList of SpinnerItems for all distinct Locations in Work Orders or Notifications based on filter is being applied on
     */
    public ArrayList<SpinnerItem> getLocations() {
        ArrayList<SpinnerItem> result;
        if (!isWorkOrder) {
            if (noLocations.size() == 0)
                noLocations = Notification.getSpinnerLocations();
            result = noLocations;
        } else {
            if (woLocations.size() == 0)
                woLocations = WorkOrder.getSpinnerLocations();
            result = woLocations;
        }
        return result;
    }
}
