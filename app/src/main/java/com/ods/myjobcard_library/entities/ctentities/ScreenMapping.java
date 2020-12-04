package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

/**
 * Created by MY HOME on 11/8/2018.
 */
public class ScreenMapping extends ZBaseEntity {

    private String ScreenName;
    private String AndroidClass;
    private String IosClass;
    private String WindowsClass;
    private String HybridClass;

    public ScreenMapping() {
    }

    public ScreenMapping(ODataEntity entity) {
        create(entity);
    }

    //get methods
    public static String getClassName(String screenName) {

        String className = "";

        try {
            if (screenName.toLowerCase().equals("loginscreen")) {
                className = "com.ods.myjobcard.LoginActivity";
            } else if (screenName.toLowerCase().equals("dashboardscreen"))
                className = "com.ods.myjobcard.UI.DashboardActivity";
            else if (screenName.toLowerCase().equals("completejobscreen"))
                className = "com.ods.myjobcard.UI.WOStatus.CompleteWorkOrderActivity";
            else if (screenName.toLowerCase().equals("operationdetailsfragment"))
                className = "com.ods.myjobcard.UI.fragments.workorders.OperationDetailsFragment";
            else if (screenName.toLowerCase().equals("operationdetailsfragment_oprassgin"))
                className = "com.ods.myjobcard.UI.fragments.workorders.OperationDetailsFragment_OprAssgin";
            else if (screenName.toLowerCase().equals("workorderdetailactivity"))
                className = "com.ods.myjobcard.UI.WorkorderDetailActivity";
            else if (screenName.toLowerCase().equals("workorderdetailactivity_opr"))
                className = "com.ods.myjobcard.UI.WorkorderDetailActivity_Opr";
            else if (screenName.toLowerCase().equals("wooverviewfragment"))
                className = "com.ods.myjobcard.UI.fragments.workorders.WOOverviewFragment";
            else if (screenName.toLowerCase().equals("workorderlistactivity"))
                className = "com.ods.myjobcard.UI.WorkorderListActivity";
            else if (screenName.toLowerCase().equals("workorderswithoperationslistactivity"))
                className = "com.ods.myjobcard.UI.WorkordersWithOperationsListActivity";
            else if (screenName.toLowerCase().equals("workorderaddedit"))
                className = "com.ods.myjobcard.UI.WorkOrderAddEdit";
            else if (screenName.toLowerCase().equals(ZCollections.NOTIFICATION_DOCS_FRAGMENT))
                className = "com.ods.myjobcard.UI.fragments.notifications.DocsFragment";
            else if (screenName.toLowerCase().equals(ZCollections.EQUIPMENT_OVERVIEW_FRAGMENT))
                className = "com.ods.myjobcard.UI.equipment.OverviewFragment";
            else if (screenName.toLowerCase().equals(ZCollections.SUPERVISOR_VIEW_ACTIVITY))
                className = "com.ods.myjobcard.UI.supervisor.SupervisorViewActivity";
            else if (screenName.toLowerCase().equals(ZCollections.NOTIFICATION_LIST_ACTIVITY))
                className = "com.ods.myjobcard.UI.NotificationListActivity";
            else if (screenName.toLowerCase().equals("generalformactivity"))
                className = "com.ods.myjobcard.formsEngine.activity.GeneralFormActivity";
        } catch (Exception e) {
            DliteLogger.WriteLog(ScreenMapping.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return className;
    }

    public String getScreenName() {
        return ScreenName;
    }

    public void setScreenName(String screenName) {
        ScreenName = screenName;
    }

    public String getAndroidClass() {
        return AndroidClass;
    }

    public void setAndroidClass(String androidClass) {
        AndroidClass = androidClass;
    }

    public String getIosClass() {
        return IosClass;
    }

    public void setIosClass(String iosClass) {
        IosClass = iosClass;
    }

    public String getWindowsClass() {
        return WindowsClass;
    }

    public void setWindowsClass(String windowsClass) {
        WindowsClass = windowsClass;
    }

    public String getHybridClass() {
        return HybridClass;
    }

    public void setHybridClass(String hybridClass) {
        HybridClass = hybridClass;
    }
}
