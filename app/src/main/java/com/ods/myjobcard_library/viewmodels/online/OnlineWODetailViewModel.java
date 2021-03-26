package com.ods.myjobcard_library.viewmodels.online;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WOLongText;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.myjobcard_library.viewmodels.workorder.WOLongTextHelper;
import com.ods.myjobcard_library.viewmodels.workorder.WorkOrderHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
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

    public void updateWorkOrderOnline(WorkOrder workOrder) {
        if (mWorkOrderHelper == null)
            mWorkOrderHelper = new WorkOrderHelper();
        mWorkOrderHelper.UpdateWorkOrderOnline(workOrder);
        updateObserver = new Observer<ResponseObject>() {
            @Override
            public void onChanged(ResponseObject responseObject) {
                updateWorkorder(responseObject);
            }
        };
        mWorkOrderHelper.getUpdatedWoResult().observeForever(updateObserver);
    }

    private void updateWorkorder(ResponseObject responseObject) {
        updateWOResult.postValue(responseObject);
        updateWOResult.removeObserver(updateObserver);
    }

}
