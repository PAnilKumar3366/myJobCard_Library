package com.ods.myjobcard_library.viewmodels.workorder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.CapacityLevelData;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class CapacityLevelViewModel extends AndroidViewModel
{
    private MutableLiveData<ArrayList<CapacityLevelData>> capacitylistLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> postCapacityData=new MutableLiveData<>();
    private CapacityLevelHelper capacityLevelHelper;
    private ArrayList<CapacityLevelData> CapacityLevelArrayList;

    public CapacityLevelViewModel(@NonNull Application application) {
        super(application);
        capacityLevelHelper=new CapacityLevelHelper();
    }

    /**fetching all capacity level data for the workorder's operation by calling through its helper instance
     * @param woNum
     * @param oprNum
     */
    public void onFetchCapacityLevelList(String woNum, String oprNum) {
        try {
            ArrayList<ZODataEntity> zoDataEntityArrayList=capacityLevelHelper.getCapacityData(woNum, oprNum);
            capacitylistLiveData.setValue(onFetchCapacityLevelEntities(zoDataEntityArrayList));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /** Converting the ZODataEntity list to CapacityData object
     * @param zODataEntities
     * @return
     */
    protected ArrayList<CapacityLevelData> onFetchCapacityLevelEntities(ArrayList<ZODataEntity> zODataEntities) {
        CapacityLevelArrayList = new ArrayList<>();
        try {
            for (ZODataEntity entity : zODataEntities) {
                CapacityLevelData capacityLevelData = new CapacityLevelData(entity);
                CapacityLevelArrayList.add(capacityLevelData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return CapacityLevelArrayList;
    }

    /** getting live data for all capacity data for the workorder's operation
     * @return
     */
    public MutableLiveData<ArrayList<CapacityLevelData>> getCapacitylistLiveData() {
        return capacitylistLiveData;
    }

    /** saving the capacity data including all CURD operations
     * @param capacityData
     * @param autoFlush
     */
    public void setPostCapacityData(CapacityLevelData capacityData,boolean autoFlush) {
        try {
            postCapacityData.setValue(capacityLevelHelper.postCapacityData(capacityData,autoFlush));
        } catch (Exception e) {
            DliteLogger.WriteLog(CapacityLevelHelper.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**getting the saved capacity data response
     * @return
     */
    public MutableLiveData<Boolean> getPostCapacityData() {
        return postCapacityData;
    }
}
