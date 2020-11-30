package com.ods.myjobcard_library.viewmodels.workorder;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.entities.transaction.WOPendingHistory;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class PendingHistoryViewModel extends ViewModel {

    private MutableLiveData<ArrayList<WOPendingHistory>> woPendingList = new MutableLiveData<ArrayList<WOPendingHistory>>();

    public LiveData<ArrayList<WOPendingHistory>> getPendingList() {
        return woPendingList;
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
        Log.d(PendingHistoryViewModel.class.getSimpleName(), "onCleared: ");
    }
}
