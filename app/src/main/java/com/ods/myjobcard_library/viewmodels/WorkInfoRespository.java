package com.ods.myjobcard_library.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.UUID;

public class WorkInfoRespository {

    public static WorkInfoRespository respository = null;
    public PeriodicWorkRequest mWorkRequest;
    private MutableLiveData<WorkInfo> mCurrentWorkInfo;
    private MutableLiveData<WorkInfo> mBackgroundScheduler;

    public static WorkInfoRespository getInstance() {
        if (respository == null) {
            respository = new WorkInfoRespository();
        }
        return respository;
    }

    public PeriodicWorkRequest getWorkRequest() {
        return mWorkRequest;
    }

    public void setWorkRequest(PeriodicWorkRequest mWorkRequest) {
        this.mWorkRequest = mWorkRequest;
    }

    public void setmBackgroundScheduler(WorkInfo workInfo) {
        this.mBackgroundScheduler.setValue(workInfo);
    }

    /*public static PeriodicWorkRequest getWorkRequest() {
        return mWorkRequest;
    }

    public static void setWorkRequest(PeriodicWorkRequest mWorkRequest) {
        WorkInfoRespository.mWorkRequest = mWorkRequest;
    }*/

    public MutableLiveData<WorkInfo> getBackgroundScheduler() {
        if (mBackgroundScheduler == null) {
            mBackgroundScheduler = new MutableLiveData<>();
        }
        return mBackgroundScheduler;
    }

    public MutableLiveData<WorkInfo> getCurrentWorkInfo() {
        if (mCurrentWorkInfo == null)
            mCurrentWorkInfo = new MutableLiveData<>();
        return mCurrentWorkInfo;
    }

    //setCurrnetWorkInfo
    public void setCurrentWorkInfo(WorkInfo workInfo) {
        this.mCurrentWorkInfo.setValue(workInfo);
    }

    public void setWorkId(UUID workId) {
        observeWorkInfo(workId);
    }

    public void observeWorkInfo(UUID workId) {
        WorkManager.getInstance().getWorkInfoByIdLiveData(workId).observeForever(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                mBackgroundScheduler.setValue(workInfo);
            }
        });
    }
}
