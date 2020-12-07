package com.ods.myjobcard_library.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.appsetting.AppStoreSet;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EventBasedFlushWorker extends Worker {

    private static final String TAG = "BackgroundFlush";
    private static final String PROGRESS = "PROGRESS";
    private DataHelper helper;
    private boolean isErrorFlush;
    private String ErrorMsg = "Error";
    private ResponseObject result = null;


    public EventBasedFlushWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        helper = DataHelper.getInstance();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Debug, "Do work Called and tag is : " + this.getTags().toString() + " and Id  " + this.getId());
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            if (ZAppSettings.isLoggedIn && !ZAppSettings.IS_DEMO_MODE) {

                if (isScheduleWorkRunning()) {
                    WorkManager.getInstance(getApplicationContext()).cancelWorkById(this.getId());
                    Data errorData = new Data.Builder().
                            putBoolean("isSchedule", false).
                            putString("Result", "").build();
                    return Result.success(errorData);
                }
                DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Debug, "Do work Called and tag is : " + this.getTags().toString() + " and Id  " + this.getId());
                //DataHelper.isBGFlushInProgress = true;
               /* result = helper.Flush();
                if (result != null && !result.isError() && ZConfigManager.EventBased_Sync_Type == 2)
                    result = helper.Refresh(AppStoreSet.getStoresForNormalTransmit());*/
                String errorMessage = "";
                boolean error = false;
                result = helper.getErrors();
                if (result.isError()) {
                    errorMessage = result.getMessage();
                    error = true;
                }
                /*for (AppStoreSet store : AppStoreSet.getStoresForNormalTransmit()) {
                    result = helper.ReadErrors(store);
                    if (result.isError()) {
                        errorMessage = result.getMessage();
                        error = true;
                    }
                }*/
                result.setError(error);
                result.setMessage(errorMessage);
                //result = helper.ReadErrors(AppStoreSet.getStoresForNormalTransmit());

                //DataHelper.isBGFlushInProgress = false;
                if (result.isError()) {
                    Data errorData = new Data.Builder().
                            putBoolean("isSchedule", false).
                            putString("ErrorMsg", result.getMessage()).build();
                    return Result.failure(errorData);
                }
                return Result.success(new Data.Builder().putString("Result", "Success").build());
            } else {
                WorkManager.getInstance(getApplicationContext()).cancelWorkById(this.getId());
                DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Debug, "Schedule Task is cancelled and tag is : " + this.getTags().toString() + " and Id  " + this.getId());
            }
        } catch (Exception e) {
            Log.d(TAG, "doWork: Exception ", e);
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            return Result.failure(new Data.Builder().putString("ErrorMsg", e.getMessage()).build());
        }
        return Result.failure(new Data.Builder().putString("ErrorMsg", "Failed").build());
    }

    public boolean isScheduleWorkRunning() {
        try {
            List<WorkInfo> workInfoList = WorkManager.getInstance(getApplicationContext()).getWorkInfosByTag(ZConfigManager.PERIODIC_REQUEST).get();
            for (WorkInfo info : workInfoList) {
                if (info.getState().equals(WorkInfo.State.RUNNING))
                    return true;
                break;
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

}


