package com.ods.myjobcard_library.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.entities.transaction.WOHistoryOpLongText;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WoHistoryOprLongTextViewModel extends BaseViewModel {

    private WOHistoryOprLongTextHelper helper;
    private MutableLiveData<ArrayList<WOHistoryOpLongText>> historyOprLongText = new MutableLiveData<>();

    public WoHistoryOprLongTextViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<WOHistoryOpLongText>> getHistoryOprLongText() {
        return historyOprLongText;
    }

    /**
     * This method is used to fetch the HistoryOperationLongText and set the result to live data.
     *
     * @param WorkorderNum
     * @param OperationNum
     */
    public void fetchHisOprLongText(String WorkorderNum, String OperationNum) {
        helper = new WOHistoryOprLongTextHelper();
        ResponseObject result = helper.fetchHistoryOprLongtext(WorkorderNum, OperationNum);
        try {
            if (result != null && !result.isError()) {
                ArrayList<ZODataEntity> entities = (ArrayList<ZODataEntity>) result.Content();
                historyOprLongText.setValue(getHistoryOprLongText(entities));
            } else
                setError(result.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
            setError(e.getMessage());
        }

    }

    /**
     * This method is used to convert the ZOData Entites to the Required pojo and returns the list of objects
     *
     * @param entities
     * @return list of WOHistoryOpLongText
     */
    private ArrayList<WOHistoryOpLongText> getHistoryOprLongText(ArrayList<ZODataEntity> entities) {
        ArrayList<WOHistoryOpLongText> longTextArrayList = new ArrayList<>();
        try {
            for (ZODataEntity entity : entities) {
                WOHistoryOpLongText longText = new WOHistoryOpLongText(entity);
                longTextArrayList.add(longText);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
            setError(e.getMessage());
        }
        return longTextArrayList;
    }
}
