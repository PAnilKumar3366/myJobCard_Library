package com.ods.myjobcard_library.viewmodels.workorder;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.entities.transaction.WOPendingHistory;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class HistoryViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<WOPendingHistory>> woHistoryList = new MutableLiveData<ArrayList<WOPendingHistory>>();
    private MutableLiveData<ArrayList<WOPendingHistory>> woPendingList = new MutableLiveData<ArrayList<WOPendingHistory>>();

    public HistoryViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ArrayList<WOPendingHistory>> getWOHistory() {
        return woHistoryList;
    }

    public LiveData<ArrayList<WOPendingHistory>> getPendingList() {
        return woPendingList;
    }

    public void setWoHistoryList(String workOrderNum, boolean isHistory) {
        ResponseObject result = WOPendingHistory.getWOHistoryPendingItems(workOrderNum, isHistory);
        ArrayList<WOPendingHistory> historyItems;
        if (!result.isError()) {
            historyItems = (ArrayList<WOPendingHistory>) result.Content();
        } else {
            historyItems = new ArrayList<>();
        }
        woHistoryList.setValue(historyItems);
    }

    public void setWoPendingList(String workOrderNum, boolean isHistory) {
        ResponseObject result = WOPendingHistory.getWOHistoryPendingItems(workOrderNum, isHistory);
        ArrayList<WOPendingHistory> pendingHistories;
        if (!result.isError()) {
            pendingHistories = (ArrayList<WOPendingHistory>) result.Content();
        } else {
            pendingHistories = new ArrayList<>();
        }
        woPendingList.setValue(pendingHistories);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(HistoryViewModel.class.getSimpleName(), "onCleared: ");
    }
}
