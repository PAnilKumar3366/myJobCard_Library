package com.ods.myjobcard_library.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Color;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ctentities.SpinnerItem;
import com.ods.myjobcard_library.entities.ctentities.UserTable;
import com.ods.myjobcard_library.entities.ctentities.WorkCenter;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.entities.transaction.NotificationItem;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.myjobcard_library.viewmodels.online.OnlineDataList;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.StoreHelpers.TableConfigSet;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.client.odata.v4.EntityValue;
import com.sap.client.odata.v4.EntityValueList;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.model.SliceValue;

public class NewDashboardViewModel extends BaseViewModel {

    public String filterOneCategory = "", filterTwoCategory = "";
    public ArrayList<SpinnerItem> filterOneValue = new ArrayList<>();
    public ArrayList<SpinnerItem> filterTwoValue = new ArrayList<>();
    public HashMap<Integer, String> workOrderFilterMap = new HashMap<>();
    public HashMap<Integer, String> notificationFilterMap = new HashMap<>();
    public HashMap<String, ArrayList<SpinnerItem>> filterValueHashMap = new HashMap<>();
    public ArrayList<SpinnerItem> legendsList = new ArrayList<>();
    public ArrayList<SpinnerItem> filterDropdown = new ArrayList<>();
    public boolean isForWorkOrders = true;
    public boolean isTodaySelected = true;
    private List<SliceValue> mSliceValues = new ArrayList<>();
    private MutableLiveData<ArrayList<WorkOrder>> queriedWorkOrders = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Notification>> queriedNotifications = new MutableLiveData<>();
    private MutableLiveData<BigDecimal> completedPercentage = new MutableLiveData<>();
    private MutableLiveData<ArrayList<WorkOrder>> totalWorkOrders = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Notification>> totalNotifications = new MutableLiveData<>();
    private MutableLiveData<List<SliceValue>> mPieChartData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<WorkOrder>> onlineWorkOrderList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Notification>> onlineNotificationList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Operation>> onlineOperationList = new MutableLiveData<>();
    private ArrayList<WorkOrder> workOrders = new ArrayList<>();
    private ArrayList<Operation> operations = new ArrayList<>();
    private ArrayList<Notification> onlineNotifications = new ArrayList<>();
    private ArrayList<NotificationItem> onlineItemsList = new ArrayList<>();
    public String CurrentWorkCenter = "";
    public NewDashboardViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<WorkOrder>> getQueriedOrders() {
        return queriedWorkOrders;
    }

    public void setQueriedOrders(@NonNull String filterQuery) {
        fetchWorkOrders(filterQuery);
    }

    public MutableLiveData<ArrayList<Notification>> getQueriedNotifications() {
        return queriedNotifications;
    }

    public void setQueriedNotifications(@NonNull String filterQuery) {
        fetchNotifications(filterQuery);
    }

    public MutableLiveData<ArrayList<WorkOrder>> getTotalWorkOrders() {
        return totalWorkOrders;
    }

    public void setTotalWorkOrders() {
        fetchWorkOrders(null);
    }

    public MutableLiveData<ArrayList<Notification>> getTotalNotifications() {
        return totalNotifications;
    }

    public void setTotalNotifications() {
        fetchNotifications(null);
    }

    public MutableLiveData<BigDecimal> getCompletedPercentage() {
        return completedPercentage;
    }

    public void setCompletedPercentage(boolean areWorkOrders) {
        try {
            BigDecimal percentage = new BigDecimal(BigInteger.ZERO);
            ArrayList<WorkOrder> availableWorkOrders = new ArrayList<>();
            ArrayList<WorkOrder> completedOrders = new ArrayList<>();
            ArrayList<Notification> completedNotifications = new ArrayList<>();
            ArrayList<Notification> availableNotifications = new ArrayList<>();
            if (areWorkOrders && totalWorkOrders.getValue() != null) {
                if (isTodaySelected) {
                    for (WorkOrder order : totalWorkOrders.getValue()) {
                        if (order.getBasicFnshDate() != null && (order.getBasicFnshDate().getTime().before(new GregorianCalendar().getTime())
                                || order.getBasicFnshDate().getTime().equals(new GregorianCalendar().getTime()))) {
                            availableWorkOrders.add(order);
                        }
                    }
                } else
                    availableWorkOrders.addAll(totalWorkOrders.getValue());
                for (WorkOrder workOrder : availableWorkOrders) {
                    if (workOrder.isCompleted())
                        completedOrders.add(workOrder);
                }
                percentage = (new BigDecimal(completedOrders.size()).multiply(new BigDecimal(100)))
                        .divide(new BigDecimal(totalWorkOrders.getValue().size()), RoundingMode.DOWN);
            } else if (totalNotifications.getValue() != null) {
                if (isTodaySelected) {
                    for (Notification notification : totalNotifications.getValue()) {
                        if (notification.getRequiredEndDate() != null &&
                                (notification.getRequiredEndDate().getTime().before(new GregorianCalendar().getTime())
                                        || notification.getRequiredEndDate().getTime().equals(new GregorianCalendar().getTime()))) {
                            availableNotifications.add(notification);
                        }
                    }
                } else
                    availableNotifications.addAll(totalNotifications.getValue());
                for (Notification notification : availableNotifications) {
                    if (notification.isComplete())
                        completedNotifications.add(notification);
                }
                percentage = (new BigDecimal(completedNotifications.size()).multiply(new BigDecimal(100)))
                        .divide(new BigDecimal(totalNotifications.getValue().size()), RoundingMode.DOWN);
            }
            completedPercentage.setValue(percentage);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public MutableLiveData<List<SliceValue>> getPieChartData() {
        return mPieChartData;
    }

    public void setPieChartData(boolean areWorkOrders, String filterOneCategory, String filterTwoCategory) {
        try {
            List<SliceValue> sliceValues;
            if (areWorkOrders)
                sliceValues = setWorkOrderFilterMap(filterOneCategory, filterTwoCategory);
            else
                sliceValues = setNotificationFilterMap(filterOneCategory, filterTwoCategory);
            mPieChartData.setValue(sliceValues);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public MutableLiveData<ArrayList<WorkOrder>> getOnlineWorkOrderList() {
        return onlineWorkOrderList;
    }

    public MutableLiveData<ArrayList<Notification>> getOnlineNotificationList() {
        return onlineNotificationList;
    }

    public MutableLiveData<ArrayList<Operation>> getOnlineOperationList() {
        return onlineOperationList;
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchWorkOrders(@Nullable String filterQuery) {
        try {
            new AsyncTask<Void, Void, Boolean>() {
                ResponseObject result = null;
                ArrayList<WorkOrder> arrayList = new ArrayList<>();

                @Override
                protected Boolean doInBackground(Void... voids) {
                    if (filterQuery == null) {
                        result = WorkOrder.getWorkOrders(ZAppSettings.FetchLevel.List, null, null);
                    } else {
                        result = WorkOrder.getFilteredWorkOrders(filterQuery, ZAppSettings.FetchLevel.List, null);
                    }
                    if (result != null && !result.isError()) {
                        arrayList = (ArrayList<WorkOrder>) result.Content();
                        return true;
                    }
                    return false;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    if (aBoolean)
                        if (filterQuery == null)
                            totalWorkOrders.setValue(arrayList);
                        else
                            queriedWorkOrders.setValue(arrayList);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchNotifications(@Nullable String filterQuery) {
        try {
            new AsyncTask<Void, Void, Boolean>() {
                ResponseObject result = null;
                ArrayList<Notification> arrayList = new ArrayList<>();

                @Override
                protected Boolean doInBackground(Void... voids) {
                    if (filterQuery == null) {
                        result = Notification.getNotifications(ZAppSettings.FetchLevel.List, null, null, null, false);
                    } else {
                        result = Notification.getFilteredNotifications(filterQuery, ZAppSettings.FetchLevel.List, null);
                    }
                    if (result != null && !result.isError()) {
                        arrayList = (ArrayList<Notification>) result.Content();
                        return true;
                    }
                    return false;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    if (aBoolean)
                        if (filterQuery == null)
                            totalNotifications.setValue(arrayList);
                        else
                            queriedNotifications.setValue(arrayList);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    //Maintaining filter dropdown lists.
    public void maintainFilterDropdownList(boolean isForWorkOrders) {
        try {
            filterDropdown.clear();
            if (isForWorkOrders) {
                filterDropdown.add(new SpinnerItem("0", "Select"));
                filterDropdown.add(new SpinnerItem("Priority", "Priority"));
                filterDropdown.add(new SpinnerItem("MobileObjStatus", "Status"));
                filterDropdown.add(new SpinnerItem("UserStatus", "User Status"));
                filterDropdown.add(new SpinnerItem("MainWorkCtr", "Work Center"));
                filterDropdown.add(new SpinnerItem("MaintActivityType", "Mant. Activity Type"));
                filterDropdown.add(new SpinnerItem("Date", "Date"));
                if (ZConfigManager.DOWNLOAD_CREATEDBY_WO.equalsIgnoreCase("X"))
                    filterDropdown.add(new SpinnerItem("CreatedBy", "Created By Order"));
            } else {
                filterDropdown.add(new SpinnerItem("0", "Select"));
                filterDropdown.add(new SpinnerItem("Priority", "Priority"));
                filterDropdown.add(new SpinnerItem("MobileStatus", "Status"));
                filterDropdown.add(new SpinnerItem("WorkCenter", "Work Center"));
                filterDropdown.add(new SpinnerItem("WOConversion", "Order Conversion"));
                filterDropdown.add(new SpinnerItem("Date", "Date"));
                if (ZConfigManager.DOWNLOAD_CREATEDBY_NOTIF.equalsIgnoreCase("X"))
                    filterDropdown.add(new SpinnerItem("CreatedBy", "Created By Notification"));
            }
            maintainFilterValueDropdownList(isForWorkOrders);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    private void maintainFilterValueDropdownList(boolean isForWorkOrders) {
        try {
            ArrayList<SpinnerItem> priorityFilterValue;
            ArrayList<SpinnerItem> statusFilterValue;
            ArrayList<SpinnerItem> workCenterFilterValue;
            ArrayList<SpinnerItem> activityTypeFilterValue = new ArrayList<>();
            ArrayList<SpinnerItem> userStatusFilterValue = new ArrayList<>();
            ArrayList<SpinnerItem> techniciansFilterValue = new ArrayList<>();
            ArrayList<SpinnerItem> workOrderConversionFilterValue = new ArrayList<>();
            ArrayList<SpinnerItem> dateFilterValue = new ArrayList<>();
            ArrayList<SpinnerItem> createdByWOFilterValue = new ArrayList<>();
            ArrayList<SpinnerItem> createdByNOFilterValue = new ArrayList<>();
            ArrayList<SpinnerItem> selectArray = new ArrayList<>();
            selectArray.add(new SpinnerItem("0","Select"));

            if (isForWorkOrders) {
                priorityFilterValue = WorkOrder.getSpinnerPriorities();
                statusFilterValue = WorkOrder.getSpinnerStatuses();
                workCenterFilterValue = WorkOrder.getSpinnerWorkCenters();
                activityTypeFilterValue = WorkOrder.getSpinnerPMActivityTypes();
                userStatusFilterValue = WorkOrder.getSpinnerUserStatuses();
                if (ZConfigManager.DEFAULT_ASSIGNMENT_TYPE.equalsIgnoreCase(ZAppSettings.AssignmentType.OperationLevel.getAssignmentTypeText())) {
                    techniciansFilterValue = WorkOrder.getSpinnerOperationTechnicians();
                } else if (ZConfigManager.DEFAULT_ASSIGNMENT_TYPE.equalsIgnoreCase(ZAppSettings.AssignmentType.WorkOrderLevel.getAssignmentTypeText())) {
                    techniciansFilterValue = WorkOrder.getSpinnerWorkOrderTechnicians();
                }
            } else {
                priorityFilterValue = Notification.getSpinnerPriorities();
                statusFilterValue = Notification.getSpinnerStatuses();
                workCenterFilterValue = Notification.getSpinnerWorkCenters();
                workOrderConversionFilterValue.add(new SpinnerItem("0", "Order Created"));
                workOrderConversionFilterValue.add(new SpinnerItem("1", "Order Not Created"));
            }
            dateFilterValue.add(new SpinnerItem("1", "Planned for tomorrow"));
            dateFilterValue.add(new SpinnerItem("2", "Planned for next week"));
            dateFilterValue.add(new SpinnerItem("3", "Overdue for last 2 days"));
            dateFilterValue.add(new SpinnerItem("4", "Overdue for a week"));
            dateFilterValue.add(new SpinnerItem("5", "All overdue"));

            filterValueHashMap.put("Select", selectArray);
            filterValueHashMap.put("Priority", priorityFilterValue);
            filterValueHashMap.put("Status", statusFilterValue);
            filterValueHashMap.put("Work Center", workCenterFilterValue);
            filterValueHashMap.put("Mant. Activity Type", activityTypeFilterValue);
            filterValueHashMap.put("User Status", userStatusFilterValue);
            filterValueHashMap.put("Technicians", techniciansFilterValue);
            filterValueHashMap.put("WorkOrder Conversion", workOrderConversionFilterValue);
            filterValueHashMap.put("Date", dateFilterValue);
            int assignmentType = Integer.parseInt(UserTable.getUserWorkAssignmentType());
            if (ZConfigManager.DOWNLOAD_CREATEDBY_WO.equalsIgnoreCase("X") && isForWorkOrders) {
                createdByWOFilterValue.add(new SpinnerItem("EnteredBy", "Created By Me"));
                if (assignmentType == 3 || assignmentType == 4)
                    createdByWOFilterValue.add(new SpinnerItem("MainWorkCtr", "Assigned To Me"));
                else
                    createdByWOFilterValue.add(new SpinnerItem("PersonResponsible", "Assigned To Me"));
                ///createdByWOFilterValue.add(new SpinnerItem("PersonResponsible", "Assigned To Me"));
                filterValueHashMap.put("CreatedByWO", createdByWOFilterValue);
            }
            if (ZConfigManager.DOWNLOAD_CREATEDBY_NOTIF.equalsIgnoreCase("X") && !isForWorkOrders) {
                createdByNOFilterValue.add(new SpinnerItem("EnteredBy", "Created By Me"));
                if (UserTable.getUserNotificationAssignmentType().equalsIgnoreCase("2")) {
                    createdByNOFilterValue.add(new SpinnerItem("WorkCenter", "Assigned To Me"));
                    CurrentWorkCenter = WorkCenter.getWorkCenterObjId(UserTable.getUserWorkCenter());
                } else
                    createdByNOFilterValue.add(new SpinnerItem("Partner", "Assigned To Me"));
                filterValueHashMap.put("CreatedByNO", createdByNOFilterValue);
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    //Maintaining the FilterMaps creating filter queries for filters and Pie Chart.
    private List<SliceValue> setWorkOrderFilterMap(String filterOne, String filterTwo) {
        filterOneCategory = filterOne;
        filterTwoCategory = filterTwo;
        try {
            mSliceValues.clear();
            legendsList.clear();
            String finalQuery;
            String queryOne = "", queryTwo = "";
            int count = 0;
            int orderCount;
            if ((filterOneCategory.isEmpty() || filterOneCategory.equals("0"))
                    && (filterTwoCategory.isEmpty() || filterTwoCategory.equals("0"))) {
                filterOneCategory = "Priority";
                filterTwoCategory = "MobileObjStatus";
                filterOneValue = WorkOrder.getSpinnerPriorities();
                filterTwoValue = WorkOrder.getSpinnerStatuses();
            }
            for (SpinnerItem filterOneValue : filterOneValue) {
                if (filterOneCategory.equalsIgnoreCase("UserStatus"))
                    queryOne = "?$filter=indexof(" + filterOneCategory + ",'" + filterOneValue.getId() + "') ne -1";
                else if (filterOneCategory.equalsIgnoreCase("date"))
                    queryOne = "?$filter=" + getWorkOrderDateFilterQuery(filterOneValue.getId());
                else if (filterOneCategory.equalsIgnoreCase("OperationTechnician"))
                    queryOne = "?$filter=(" + ZCollections.WO_OPR_NAV_PROPERTY + "/any(d:d/PersonnelNo eq '" + filterOneValue.getId() + "'))";
                else if (filterOneCategory.equalsIgnoreCase("FuncLocation"))
                    queryOne = "?$filter=indexof(" + filterOneCategory + ", '" + filterOneValue.getId() + "') ne -1";
                else if (filterOneCategory.equalsIgnoreCase("CreatedBy")) {
                    if (filterOneValue.getId().equalsIgnoreCase("EnteredBy"))
                        queryOne = "?$filter=" + filterOneValue.getId() + " eq '" + ZAppSettings.strUser + "'";
                    else if (filterOneValue.getId().equalsIgnoreCase("PersonResponsible"))
                        queryOne = "?$filter=" + filterOneValue.getId() + " eq '" + UserTable.getUserPersonnelNumber() + "'";
                    else if (filterOneValue.getId().equalsIgnoreCase("MainWorkCtr"))
                        queryOne = "?$filter=" + "MainWorkCtr" + " eq '" + UserTable.getUserWorkCenter() + "'";
                } else
                    queryOne = "?$filter=" + filterOneCategory + " eq '" + filterOneValue.getId() + "'";
                finalQuery = queryOne;

                if (filterTwoValue.size() > 0) {
                    for (SpinnerItem filterTwoValue : filterTwoValue) {
                        if (filterTwoCategory.equalsIgnoreCase("UserStatus"))
                            queryTwo = " and (indexof(" + filterTwoCategory + ",'" + filterTwoValue.getId() + "') ne -1)";
                        else if (filterTwoCategory.equalsIgnoreCase("date"))
                            queryTwo = " and " + getWorkOrderDateFilterQuery(filterTwoValue.getId());
                        else if (filterTwoCategory.equalsIgnoreCase("OperationTechnician"))
                            queryTwo = " and (" + ZCollections.WO_OPR_NAV_PROPERTY + "/any(d:d/PersonnelNo eq '" + filterTwoValue.getId() + "'))";
                        else if (filterTwoCategory.equalsIgnoreCase("FuncLocation"))
                            queryTwo = " and (indexof(" + filterTwoCategory + ", '" + filterTwoValue.getId() + "') ne -1)";
                        else if (filterTwoCategory.equalsIgnoreCase("CreatedBy")) {
                            if (filterTwoValue.getId().equalsIgnoreCase("EnteredBy"))
                                queryTwo = "?$filter=" + filterTwoValue.getId() + " eq '" + ZAppSettings.strUser + "'";
                            else if (filterTwoValue.getId().equalsIgnoreCase("PersonResponsible"))
                                queryTwo = "?$filter=" + filterTwoValue.getId() + " eq '" + UserTable.getUserPersonnelNumber() + "'";
                            else if (filterTwoValue.getId().equalsIgnoreCase("MainWorkCtr"))
                                queryOne = "?$filter=" + "MainWorkCtr" + " eq '" + UserTable.getUserWorkCenter() + "'";
                        } else {
                            queryTwo = " and (" + filterTwoCategory + " eq '" + filterTwoValue.getId() + "')";
                        }
                        finalQuery = queryOne + queryTwo;
                        orderCount = WorkOrder.getWorkOrdersCount(finalQuery);
                        if (orderCount > 0) {
                            workOrderFilterMap.put(count, finalQuery);
                            int colorCount = count > 15 ? 0 : count;
                            ZAppSettings.ColorMap color = ZAppSettings.ColorMap.valueOf("C" + colorCount);
                            SliceValue slice = new SliceValue(orderCount, Color.parseColor(color.getColorCode()));
                            mSliceValues.add(slice);
                            legendsList.add(new SpinnerItem(color.getColorCode(), (filterTwoValue.getDescription() + " - " + filterOneValue.getDescription())));
                            count++;
                        }
                    }
                } else {
                    orderCount = WorkOrder.getWorkOrdersCount(finalQuery);
                    if (orderCount > 0) {
                        workOrderFilterMap.put(count, finalQuery);
                        int colorCount = count > 15 ? 0 : count;
                        ZAppSettings.ColorMap color = ZAppSettings.ColorMap.valueOf("C" + colorCount);
                        SliceValue slice = new SliceValue(orderCount, Color.parseColor(color.getColorCode()));
                        mSliceValues.add(slice);
                        legendsList.add(new SpinnerItem(color.getColorCode(), filterOneValue.getDescription()));
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return mSliceValues;
    }

    private List<SliceValue> setNotificationFilterMap(String filterOne, String filterTwo) {
        filterOneCategory = filterOne;
        filterTwoCategory = filterTwo;
        try {
            mSliceValues.clear();
            legendsList.clear();
            String finalQuery;
            String queryOne = "", queryTwo = "";
            int count = 0;
            int notificationCount;
            if ((filterOneCategory.isEmpty() || filterOneCategory.equals("0"))
                    && (filterTwoCategory.isEmpty() || filterTwoCategory.equals("0"))) {
                filterOneCategory = "Priority";
                filterTwoCategory = "MobileStatus";
                filterOneValue = Notification.getSpinnerPriorities();
                filterTwoValue = Notification.getSpinnerStatuses();
            }
            for (SpinnerItem filterOneValue : filterOneValue) {
                if (filterOneCategory.equalsIgnoreCase("WOConversion"))
                    queryOne = "?$filter=" + getNotificationWOCreatedFilterQuery(filterOneValue.getId());
                else if (filterOneCategory.equalsIgnoreCase("date"))
                    queryOne = "?$filter=" + getNotificationDateFilterQuery(filterOneValue.getId());
                else if (filterOneCategory.equalsIgnoreCase("CreatedBy")) {
                    if (filterOneValue.getId().equalsIgnoreCase("EnteredBy"))
                        queryOne = "?$filter=" + filterOneValue.getId() + " eq '" + ZAppSettings.strUser + "'";
                    else if (filterOneValue.getId().equalsIgnoreCase("PersonResponsible"))
                        queryOne = "?$filter=" + "Partner" + " eq '" + UserTable.getUserPersonnelNumber() + "'";
                    else if (filterOneValue.getId().equalsIgnoreCase("WorkCenter"))
                        queryOne = "?$filter=" + "WorkCenter" + " eq '" + CurrentWorkCenter + "'";
                } else
                    queryOne = "?$filter=" + filterOneCategory + " eq '" + filterOneValue.getId() + "'";
                finalQuery = queryOne;

                if (filterTwoValue.size() > 0) {
                    for (SpinnerItem filterTwoValue : filterTwoValue) {
                        if (filterTwoCategory.equalsIgnoreCase("WOConversion"))
                            queryTwo = " and " + getNotificationWOCreatedFilterQuery(filterTwoValue.getId());
                        else if (filterTwoCategory.equalsIgnoreCase("date"))
                            queryTwo = " and " + getNotificationDateFilterQuery(filterTwoValue.getId());
                        else if (filterTwoCategory.equalsIgnoreCase("CreatedBy")) {
                            if (filterTwoValue.getId().equalsIgnoreCase("EnteredBy"))
                                queryTwo = "?$filter=" + filterTwoValue.getId() + " eq '" + ZAppSettings.strUser + "'";
                            else if (filterTwoValue.getId().equalsIgnoreCase("PersonResponsible"))
                                queryTwo = "?$filter=" + "Partner" + " eq '" + UserTable.getUserPersonnelNumber() + "'";
                            else if (filterTwoValue.getId().equalsIgnoreCase("WorkCenter"))
                                queryOne = "?$filter=" + "WorkCenter" + " eq '" + CurrentWorkCenter + "'";
                        } else {
                            queryTwo = " and (" + filterTwoCategory + " eq '" + filterTwoValue.getId() + "')";
                        }
                        finalQuery = queryOne + queryTwo;
                        notificationCount = Notification.getNotificationCount(finalQuery);
                        if (notificationCount > 0) {
                            notificationFilterMap.put(count, finalQuery);
                            int colorCount = count > 15 ? 0 : count;
                            ZAppSettings.ColorMap color = ZAppSettings.ColorMap.valueOf("C" + colorCount);
                            SliceValue slice = new SliceValue(notificationCount, Color.parseColor(color.getColorCode()));
                            mSliceValues.add(slice);
                            legendsList.add(new SpinnerItem(color.getColorCode(), (filterTwoValue.getDescription() + " - " + filterOneValue.getDescription())));
                            count++;
                        }

                    }
                } else {
                    notificationCount = Notification.getNotificationCount(finalQuery);
                    if (notificationCount > 0) {
                        notificationFilterMap.put(count, finalQuery);
                        int colorCount = count > 15 ? 0 : count;
                        ZAppSettings.ColorMap color = ZAppSettings.ColorMap.valueOf("C" + colorCount);
                        SliceValue slice = new SliceValue(notificationCount, Color.parseColor(color.getColorCode()));
                        mSliceValues.add(slice);
                        legendsList.add(new SpinnerItem(color.getColorCode(), filterOneValue.getDescription()));
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return mSliceValues;
    }

    private String getWorkOrderDateFilterQuery(String filterValue) {
        String filterQuery = "";
        try {
            GregorianCalendar currDate = ZCommon.getDeviceDateTime();
            GregorianCalendar date = ZCommon.getDeviceDateTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat(ZConfigManager.QUERIABLE_DATE_FORMAT, Locale.getDefault());
            String formattedDate;
            if (date != null && currDate != null) {
                if ("1".equals(filterValue)) {
                    date.add(Calendar.DAY_OF_MONTH, 2);
                    formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
                    filterQuery = "(BasicStrtDate lt datetime'" + formattedDate + "' and BasicStrtDate ge datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
                } else if ("2".equals(filterValue)) {
                    date.add(Calendar.DAY_OF_MONTH, 8);
                    formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
                    filterQuery = "(BasicStrtDate lt datetime'" + formattedDate + "' and BasicStrtDate ge datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
                } else if ("3".equals(filterValue)) {
                    date.add(Calendar.DAY_OF_MONTH, -2);
                    currDate.add(Calendar.DAY_OF_MONTH, 1);
                    formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
                    filterQuery = "(BasicFnshDate gt datetime'" + formattedDate + "' and BasicFnshDate lt datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
                } else if ("4".equals(filterValue)) {
                    date.add(Calendar.DAY_OF_MONTH, -7);
                    currDate.add(Calendar.DAY_OF_MONTH, 1);
                    formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
                    filterQuery = "(BasicFnshDate gt datetime'" + formattedDate + "' and BasicFnshDate lt datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
                } else if ("5".equals(filterValue)) {
                    currDate.add(Calendar.DAY_OF_MONTH, 1);
                    filterQuery = "(BasicFnshDate lt datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return filterQuery;
    }

    private String getNotificationDateFilterQuery(String filterValue) {
        String filterQuery = "";
        GregorianCalendar currDate = ZCommon.getDeviceDateTime();
        GregorianCalendar date = ZCommon.getDeviceDateTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(ZConfigManager.QUERIABLE_DATE_FORMAT, Locale.getDefault());
        String formattedDate;
        if (date != null && currDate != null) {
            if ("1".equals(filterValue)) {
                date.add(Calendar.DAY_OF_MONTH, 2);
                formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
                filterQuery = "(RequiredStartDate lt datetime'" + formattedDate + "' and RequiredStartDate ge datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
            } else if ("2".equals(filterValue)) {
                date.add(Calendar.DAY_OF_MONTH, 8);
                formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
                filterQuery = "(RequiredStartDate lt datetime'" + formattedDate + "' and RequiredStartDate ge datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
            } else if ("3".equals(filterValue)) {
                date.add(Calendar.DAY_OF_MONTH, -2);
                currDate.add(Calendar.DAY_OF_MONTH, 1);
                formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
                filterQuery = "(RequiredEndDate gt datetime'" + formattedDate + "' and RequiredEndDate le datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
            } else if ("4".equals(filterValue)) {
                date.add(Calendar.DAY_OF_MONTH, -7);
                currDate.add(Calendar.DAY_OF_MONTH, 1);
                formattedDate = dateFormat.format(new Date(date.getTimeInMillis()));
                filterQuery = "(RequiredEndDate gt datetime'" + formattedDate + "' and RequiredEndDate lt datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
            } else if ("5".equals(filterValue)) {
                currDate.add(Calendar.DAY_OF_MONTH, 1);
                filterQuery = "(RequiredEndDate lt datetime'" + dateFormat.format(new Date(currDate.getTimeInMillis())) + "')";
            }
        }
        return filterQuery;
    }

    private String getNotificationWOCreatedFilterQuery(String selectedValue) {
        String filterQuery = "";
        if ("0".equals(selectedValue)) {
            filterQuery = "(WorkOrderNum ne '')";
        } else if ("1".equals(selectedValue)) {
            filterQuery = "(WorkOrderNum eq '')";
        }
        return filterQuery;
    }

    //For Online Search functionality
    @SuppressLint("StaticFieldLeak")
    public void searchOnlineData(String filterQuery, Boolean isWoSearch) {
        String entitySetName;
        if (isWoSearch)
            entitySetName = ZCollections.WO_COLLECTION;
        else
            entitySetName = ZCollections.NOTIFICATION_COLLECTION;
        String resPath = entitySetName + filterQuery;
        try {
            new AsyncTask<Void, Void, ResponseObject>() {
                @Override
                protected ResponseObject doInBackground(Void... voids) {
                    return DataHelper.getInstance().getEntitiesOnline(resPath, entitySetName, TableConfigSet.getStore(entitySetName));
                }

                @Override
                protected void onPostExecute(ResponseObject responseObject) {
                    super.onPostExecute(responseObject);
                    try {
                        if (isWoSearch)
                            getOnlineWorkOrdersList(responseObject);
                        else
                            getOnlineNotificationsList(responseObject);
                    } catch (Exception e) {
                        DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }

    }

    private void getOnlineWorkOrdersList(ResponseObject responseObject) {
        try {
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
                }
                workOrders.addAll(onlineworkOrders);
                OnlineDataList.getInstance().setOnlineWorkOrderList(workOrders);
                OnlineDataList.getInstance().setWorkOrdersOperationsList(operations);
            } else {
                setError(responseObject.getMessage());
                DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, responseObject.getMessage());
            }
            if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)
                onlineOperationList.setValue(operations);
            else
                onlineWorkOrderList.setValue(workOrders);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    private void getOnlineNotificationsList(ResponseObject responseObject) {
        try {
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
                    }
                    notification.setNotificationItems(notificationItems);
                    notifications.add(notification);
                }
                onlineNotifications.addAll(notifications);
                OnlineDataList.getInstance().setOnLineNotifications(notifications);
                OnlineDataList.getInstance().setOnlineNotificationItems(onlineItemsList);
            } else {
                setError(responseObject.getMessage() != null ? responseObject.getMessage() : responseObject.Content().toString());
                DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, responseObject.getMessage());
            }
            onlineNotificationList.setValue(onlineNotifications);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }
}
