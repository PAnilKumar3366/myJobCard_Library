package com.ods.myjobcard_library.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ods.myjobcard_library.R;
import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.appsetting.AppStoreSet;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


public class TimeBasedFlushWorker extends Worker {
    private static final String TAG = "WorkRequest";
    Context context;
    ResponseObject result = new ResponseObject(ZConfigManager.Status.Success);
    private DataHelper helper;

    public TimeBasedFlushWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        helper = DataHelper.getInstance();
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            if (ZAppSettings.isLoggedIn && !ZAppSettings.IsDemoModeEnabled) {
                if (isTRWorkerRunning()) {
                    Data inputData = getInputData();
                    int retryCount = inputData.getInt("RetryCount", 0);
                    Data.Builder outputBuilder = new Data.Builder();
                    if (retryCount >= ZConfigManager.BG_SYNC_RETRY_COUNT) {
                        outputBuilder.putString("Result", "Retry");
                        retryCount = 0;
                        outputBuilder.putInt("RetryCount", retryCount);
                        outputBuilder.putLong("NextRefreshTime", 1);
                        return Result.Retry.success(outputBuilder.build());
                    } else
                        retryCount = retryCount + 1;
                    outputBuilder.putInt("RetryCount", retryCount);
                    outputBuilder.putString("Result", "Retry").build();
                    return Result.Retry.success(outputBuilder.build());
                }
                ResponseObject resultPending = helper.PendingRequestExists();
                /*if (resultPending.getStatus().equals(ZConfigManager.Status.Success)) {
                    Data.Builder outPutData = new Data.Builder().putLong("NextRefreshTime", ZConfigManager.BG_SYNC_TIME_INTERVAL).
                            putBoolean("isSchedule", false)
                            .putInt("RetryCount", 0)
                            .putString("Result", "NoPending");
                    Log.d(TAG, "doWork: Nothing is updated");
                    return Result.success(outPutData.build());
                }*/
                Long start_time = System.currentTimeMillis();
                //DataHelper.isBGFlushInProgress = true;
                Log.d(TAG, "doWork: Periodic Request is started " + this.getId());
                DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Debug, "Do work Called in Schedule Task Begin time " + ZCommon.getDevicDateTime().getTime().toString() + "tag is : " + this.getTags().toString() + " and Id  " + this.getId());
                showNotification("Refreshing Data", "Background Refresh Started");

                if (resultPending.getStatus().equals(ZConfigManager.Status.Warning))
                    result = helper.Flush(AppStoreSet.getStoresForNormalTransmit());

                if (!result.isError() && ZConfigManager.TimeBased_Sync_Type == 2)
                    result = helper.Refresh(AppStoreSet.getStoresForNormalTransmit());
                //result = helper.ReadErrors(AppStoreSet.getStoresForNormalTransmit());
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

                Long finish_time = System.currentTimeMillis();
                Long next_time = finish_time - start_time;
                next_time = TimeUnit.MILLISECONDS.toMinutes(next_time);
                long nextInterValTime = ZConfigManager.BG_SYNC_TIME_INTERVAL;
                if (next_time < nextInterValTime)
                    next_time = nextInterValTime - next_time;
                else
                    next_time = 2L;
                //DataHelper.isBGFlushInProgress = false;
                Calendar instance = Calendar.getInstance();
                instance.add(Calendar.MINUTE, next_time.intValue());
                Data.Builder outPutData = new Data.Builder().putLong("NextRefreshTime", next_time).
                        putBoolean("isSchedule", false)
                        .putInt("RetryCount", 0);
                DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Info, "Next Background Refresh" + instance.getTime().toString());
                Log.d(TAG, "doWork: " + "Next Background Refresh" + instance.getTime().toString());
                if (result.isError()) {
                    showNotification("Refresh Data", "Background Refresh Failed");
                    return Result.failure(outPutData.putString("ErrorMsg", result.getMessage()).putString("Result", "Fail").build());
                } else if (result.getStatus().equals(ZConfigManager.Status.Success)) {
                    showNotification("Refresh Data", "Background Refresh Completed");
                    DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Debug, "Do work Called in Schedule Task is Completed. tag is : " + this.getTags().toString() + " and Id  " + this.getId());
                    return Result.Success.success(outPutData.putString("Result", "Success").build());
                }
            } else {
                WorkManager.getInstance().cancelWorkById(this.getId());
                DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Debug, "Schedule Task is cancelled and tag is : " + this.getTags().toString() + " and Id  " + this.getId());
            }

        } catch (Exception e) {
            Log.d(TAG, "doWork: Exception ", e);
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            Result.retry();
        }

        return Result.failure(new Data.Builder().putString("result", "Failed").build());

    }

    public boolean isTRWorkerRunning() {
        try {
            List<WorkInfo> workInfoList = WorkManager.getInstance(getApplicationContext()).getWorkInfosByTag(ZConfigManager.TRANSACTION_WORK).get();
            for (WorkInfo info : workInfoList) {
                if (info.getState().equals(WorkInfo.State.RUNNING) || info.getState().equals(WorkInfo.State.ENQUEUED))
                    return true;
                break;
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    //workManager.enqueue(oneTimeWorkRequest);
    private void showNotification(String task, String desc) {
        try {
            NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "task_channel";
            String channelName = "task_name";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new
                        NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                    .setContentTitle(task)
                    .setContentText(desc)
                    .setAutoCancel(true)
                    .setChannelId(channelId)
                    .setSmallIcon(R.mipmap.app_icon);
            manager.notify(1, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}