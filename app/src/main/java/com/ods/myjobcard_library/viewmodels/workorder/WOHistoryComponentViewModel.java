package com.ods.myjobcard_library.viewmodels.workorder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.ods.myjobcard_library.entities.transaction.WOHistoryComponent;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class WOHistoryComponentViewModel extends AndroidViewModel
{
    private MutableLiveData<ArrayList<WOHistoryComponent>> woHistoryComponentsLiveData=new MutableLiveData<>();
    private WOHistoryComponentHelper woHistoryComponentHelper;
    private ArrayList<WOHistoryComponent> woHistoryComponentArrayList;

    public WOHistoryComponentViewModel(@NonNull Application application) {
        super(application);
        woHistoryComponentHelper=new WOHistoryComponentHelper();
    }

    /** getting the records from WoHistoryComponent  by filtered with workorder number
     * @param woNum
     */
    public void onFetchWOHistoryComponents(String woNum){
        try {
            ArrayList<ZODataEntity> zoDataEntityArrayList=woHistoryComponentHelper.getWOHistoryComponents(woNum);
            woHistoryComponentsLiveData.setValue(onFetchWOHistoryComponentEntities(zoDataEntityArrayList));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /** Conversting ZODATAEntity to WOHistoryComponent object
     * @param zODataEntities
     * @return
     */
    protected ArrayList<WOHistoryComponent> onFetchWOHistoryComponentEntities(ArrayList<ZODataEntity> zODataEntities) {
        woHistoryComponentArrayList = new ArrayList<>();
        try {
            for (ZODataEntity entity : zODataEntities) {
                WOHistoryComponent capacityLevelData = new WOHistoryComponent(entity);
                woHistoryComponentArrayList.add(capacityLevelData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return woHistoryComponentArrayList;
    }

    /** gwtting Live data for all workorder history components corresponding to the work order number
     * @return
     */
    public MutableLiveData<ArrayList<WOHistoryComponent>> getWoHistoryComponentsLiveData() {
        return woHistoryComponentsLiveData;
    }
}
