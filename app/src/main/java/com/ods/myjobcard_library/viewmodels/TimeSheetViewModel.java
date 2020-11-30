package com.ods.myjobcard_library.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.UserTimeSheet;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class TimeSheetViewModel extends ViewModel {

    private static final String TAG = "TimeSheetViewModel";
    private final MutableLiveData<ArrayList<UserTimeSheet>> mTimeSheetLiveData = new MutableLiveData<>();
    private ArrayList<UserTimeSheet> timeSheets;
    private GregorianCalendar currentSelectedDate;
    private ResponseObject result;

    public TimeSheetViewModel() {

    }

    public void setCurrentSelectedDate(GregorianCalendar currentSelectedDate) {
        this.currentSelectedDate = currentSelectedDate;
        boolean response = getLivedata();
    }

    public LiveData<ArrayList<UserTimeSheet>> getmTimeSheetLiveData() {

        return mTimeSheetLiveData;
    }

    private boolean getLivedata() {
        try {
            result = UserTimeSheet.getUserTimeSheets(currentSelectedDate, ZAppSettings.FetchLevel.List);
            if (!result.isError()) {
                timeSheets = (ArrayList<UserTimeSheet>) result.Content();
                mTimeSheetLiveData.setValue(timeSheets);
                return true;
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return false;
    }
}
