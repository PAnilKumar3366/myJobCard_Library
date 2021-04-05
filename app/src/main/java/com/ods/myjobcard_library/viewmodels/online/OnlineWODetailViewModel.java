package com.ods.myjobcard_library.viewmodels.online;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.appsettings.StatusCategory;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WOLongText;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.myjobcard_library.viewmodels.workorder.WOLongTextHelper;
import com.ods.myjobcard_library.viewmodels.workorder.WorkOrderHelper;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.ConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;
import java.util.HashMap;

public class OnlineWODetailViewModel extends BaseViewModel {
    private MutableLiveData<WorkOrder> selectedWorkOrder = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Operation>> woOprList = new MutableLiveData<>();
    private MutableLiveData<WorkOrder> updatedWo = new MutableLiveData<>();

    private Observer<ResponseObject> updateObserver, longtextObserver;
    private MutableLiveData<ResponseObject> updateWOResult = new MutableLiveData<>();
    private MutableLiveData<ResponseObject> longTextLiveData = new MutableLiveData<>();

    private MutableLiveData<ArrayList<String>> onlineWoLongText = new MutableLiveData<>();
    private MutableLiveData<WorkOrder> updatedWorkorder = new MutableLiveData<>();
    private WorkOrderHelper mWorkOrderHelper;
    private WOLongTextHelper helper;
    private WorkOrder currentOrder;

    public OnlineWODetailViewModel(@NonNull Application application) {
        super(application);
        helper = new WOLongTextHelper();
        mWorkOrderHelper = new WorkOrderHelper();
    }

    public LiveData<ResponseObject> getUpdateWOResult() {
        return updateWOResult;
    }

    public void setUpdateWOResult(MutableLiveData<ResponseObject> updateWOResult) {
        this.updateWOResult = updateWOResult;
    }

    public MutableLiveData<ArrayList<String>> getOnlineWoLongText() {
        return onlineWoLongText;
    }

    public void setOnlineWoLongText(MutableLiveData<ArrayList<String>> onlineWoLongText) {
        this.onlineWoLongText = onlineWoLongText;
    }

    public void setUpdatedWorkorder(WorkOrder updatedWorkorder) {
        this.updatedWorkorder.setValue(updatedWorkorder);
        currentOrder = updatedWorkorder;
    }


    public MutableLiveData<WorkOrder> getUpdatedWo() {
        return updatedWo;
    }

    public void setUpdatedWo(WorkOrder updatedWo) {
        this.updatedWo.setValue(updatedWo);
    }

    public MutableLiveData<WorkOrder> getSelectedWorkOrder() {
        return selectedWorkOrder;
    }

    public void setSelectedWorkOrder(WorkOrder workOrder) {
        if (workOrder != null) {
            workOrder.isOnline = true;
            WorkOrder.setCurrWo(workOrder);
        }
        currentOrder = workOrder;
        selectedWorkOrder.setValue(workOrder);
    }

    public MutableLiveData<ArrayList<Operation>> getWoOprList() {
        return woOprList;
    }

    public void setWoOprList(ArrayList<Operation> woOprList) {
        this.woOprList.setValue(woOprList);
    }

    /**
     * this method is used to fetch the online Work Order LongText with the help of Helper class and observes the result.
     *
     * @param hashMapQuery contains filter Parameters key-values
     */

    public void fetchOnlineWOLongText(HashMap<String, String> hashMapQuery) {
        try {

            String finalQuery = helper.getOnlineQuery(hashMapQuery);
            if (!finalQuery.isEmpty())
                helper.getOnlineWOLongText(finalQuery);
            else
                setError("Query Error");
            longTextLiveData = helper.getOnlineLongText();
            longtextObserver = new Observer<ResponseObject>() {
                @Override
                public void onChanged(ResponseObject responseObject) {
                    if (!responseObject.isError()) {
                        ArrayList<ZODataEntity> zoDataEntities = new ArrayList<>();
                        zoDataEntities = (ArrayList<ZODataEntity>) responseObject.Content();
                        onFetchWOLongText(zoDataEntities);
                    } else
                        setError(responseObject.getMessage());
                    longTextLiveData.removeObserver(longtextObserver);
                }
            };
            longTextLiveData.observeForever(longtextObserver);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**
     * this method helps to convert the ZODataEntity to WorkOrderLongText object and set it to LiveData.
     *
     * @param zoDataEntities contains NotificationLongtext
     */

    private void onFetchWOLongText(ArrayList<ZODataEntity> zoDataEntities) {
        try {
            ArrayList<WOLongText> longText = new ArrayList<>();
            ArrayList<String> woLongTextList = new ArrayList<>();
            for (ZODataEntity entity : zoDataEntities) {
                WOLongText woLongText = new WOLongText(entity);
                longText.add(woLongText);
            }
            for (WOLongText woLongText : longText) {
                woLongTextList.add(woLongText.getTextLine());
            }
            onlineWoLongText.setValue(woLongTextList);
        } catch (Exception e) {
            e.printStackTrace();
            setError(e.getMessage());
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**
     * This method calls from UI Module and calls the internal UpdateOrder method with updated Work Order.
     *
     * @param priority Updated Priority Value
     * @param des      Updated Short Text
     * @param orderNum Current Order Number
     */
    public void EditOnline(String priority, String des, String orderNum) {
        try {
            if (currentOrder == null || !currentOrder.getWorkOrderNum().equals(orderNum)) {
                currentOrder = WorkOrder.getCurrWo();
            }
            currentOrder.setPriority(priority);
            currentOrder.setShortText(des);
            updateWorkOrderOnline(currentOrder);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**
     * This method calls from UI Module and calls the internal UpdateOrder method with updated Work Order.
     *
     * @param priority   Updated Priority Value
     * @param labourCode updated Personal Number
     * @param orderNum   current Order Number.
     */
    public void AssignOrder(String priority, String labourCode, String orderNum) {
        try {
            if (currentOrder == null || !currentOrder.getWorkOrderNum().equals(orderNum)) {
                currentOrder = WorkOrder.getCurrWo();
            }
            currentOrder.setPriority(priority);
            currentOrder.setPersonResponsible(labourCode);
            updateWorkOrderOnline(currentOrder);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            setError(e.getMessage());
        }
    }

    /**
     * This method calls from UI Module. Fetch the status details of current order then its updates the current order status with current location and transfer flag.
     * if there is any error occured,while updating the current order status then automatically sets the updated result with error value, Otherwise calls internal updateOrder Method with latest order.
     *
     * @param orderNum   current Order Number.
     * @param notes      Transfer Notes
     * @param reason     Transfer Reason
     * @param priority   Updated Priority
     * @param labourCode Transfer Personal Number
     * @param plant      Plant
     * @param workcenter WorkCenter
     * @param location   user Location.
     */
    public void TransferOrder(String orderNum, String notes, String reason, String priority, String labourCode, String plant, String workcenter, Location location) {
        try {
            ResponseObject result = new ResponseObject(ConfigManager.Status.Error);
            if (currentOrder == null || !currentOrder.getWorkOrderNum().equals(orderNum))
                currentOrder = WorkOrder.getCurrWo();
            StatusCategory status = StatusCategory.getStatusDetails(ZAppSettings.MobileStatus.TRNS.getMobileStatusCode(), currentOrder.getOrderType(), ZConfigManager.Fetch_Object_Type.WorkOrder);
            if (status != null)
                result = currentOrder.UpdateTransferStatus(status, notes, reason, priority, labourCode, true, null, plant, workcenter);
            if (!result.isError())
                updateWorkOrderOnline(currentOrder);
            else
                updateWOResult.setValue(result);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            setError(e.getMessage());
        }
    }

    /**
     * this method helps to update the workOrder in online with help of WorkOrderLongTextHelper Class.
     *
     * @param workOrder updated workOrder which is comes from UI
     */

    private void updateWorkOrderOnline(WorkOrder workOrder) {
        try {
            if (mWorkOrderHelper == null)
                mWorkOrderHelper = new WorkOrderHelper();
            workOrder.setMode(AppSettings.EntityMode.Update);
            mWorkOrderHelper.UpdateWorkOrderOnline(workOrder);
            updateObserver = new Observer<ResponseObject>() {
                @Override
                public void onChanged(ResponseObject responseObject) {
                    updateWorkorder(responseObject);
                }
            };
            mWorkOrderHelper.getUpdatedWoResult().observeForever(updateObserver);
        } catch (Exception e) {
            e.printStackTrace();
            setError(e.getMessage());
        }
    }

    /**
     * @param responseObject contains the updated result
     */

    private void updateWorkorder(ResponseObject responseObject) {
        updateWOResult.postValue(responseObject);
        updateWOResult.removeObserver(updateObserver);
    }

}
