package com.ods.myjobcard_library.viewmodels.workorder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.entities.ctentities.WoHistoryOperation;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.myjobcard_library.viewmodels.WOHistoryOprHelper;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WoHistoryOprViewModel extends BaseViewModel {

    private ArrayList<WoHistoryOperation> woHistoryOperationsList;
    private WOHistoryOprHelper woHistoryOprHelper;
    private MutableLiveData<ArrayList<WoHistoryOperation>> woHistoryOprListLiveData = new MutableLiveData<>();
    private MutableLiveData<WoHistoryOperation> selectedHistoryOpr = new MutableLiveData<>();

    public WoHistoryOprViewModel(@NonNull @NotNull Application application) {
        super(application);
        woHistoryOprHelper = new WOHistoryOprHelper();
    }

    public MutableLiveData<WoHistoryOperation> getSelectedHistoryOpr() {
        return selectedHistoryOpr;
    }

    public void setSelectedHistoryOpr(WoHistoryOperation operation) {
        this.selectedHistoryOpr.setValue(operation);
    }

    public MutableLiveData<ArrayList<WoHistoryOperation>> getWoHistoryOprListLiveData() {
        return woHistoryOprListLiveData;
    }

    /**
     * This is called from UI and pass the required fields to get the WoHistoryOpr list from Offline.
     *
     * @param woNumber WorkOrder Number
     */
    public void onFetchWoHistoryOprList(String woNumber) {
        onFetchWoHistoryOprList(woHistoryOprHelper.fetchWoHistoryOprList(woNumber));
    }

    /**
     * Converting ZODATAEntity to WOHistoryOperation object
     *
     * @param entities
     * @return list of woHistoryOperations
     */
    private ArrayList<WoHistoryOperation> onFetchWoHistoryOprList(ArrayList<ZODataEntity> entities) {
        woHistoryOperationsList = new ArrayList<>();
        try {
            for (ZODataEntity entity : entities) {
                WoHistoryOperation woHistoryOperation = new WoHistoryOperation(entity);
                woHistoryOperationsList.add(woHistoryOperation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
            setError(e.getMessage());
        }
        return woHistoryOperationsList;
    }

    /**
     * this method is used for the observe the live data.
     *
     * @return WoHistoryOprList liveData
     */
    public ArrayList<WoHistoryOperation> getWoHistoryOperationsList() {
        return woHistoryOperationsList;
    }
}
