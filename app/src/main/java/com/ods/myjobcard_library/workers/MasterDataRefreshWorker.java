package com.ods.myjobcard_library.workers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ods.myjobcard_library.R;
import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.appsetting.AppStoreSet;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MasterDataRefreshWorker extends Worker {
    private static final String TAG = "MasterDataRefreshWorker";
    public static UUID retryUID;
    public static int retryCount = 0;
    ResponseObject result;


    public MasterDataRefreshWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            if (ZAppSettings.isLoggedIn && !ZAppSettings.IS_DEMO_MODE) {
                if (retryCount > ZConfigManager.MasterData_BG_Refresh_Retry_Attempts && retryUID.equals(getId())) {
                    DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Info, "Master data refresh retry reaches the maximum attempts");
                    retryCount = 0;
                    WorkManager.getInstance(getApplicationContext()).cancelWorkById(getId());
                    scheduleNextRequest();
                }

                sendNotificationWithChannel("Master Data Refresh Started");
                DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Info, "MasterDataRefresh is started : " + this.getTags().toString() + " and Id  " + this.getId());
                ArrayList<AppStoreSet> storesList = AppStoreSet.getStoresForMasterDataTransmit();
                result = DataHelper.getInstance().PendingRequestExists(storesList);

                if (result != null && !result.isError())
                    result = DataHelper.getInstance().Refresh(storesList);
                // result = DataHelper.getInstance().ReadErrors(storesList);
                /*for (AppStoreSet store : AppStoreSet.getStoresForMasterDataTransmit()) {
                    result = DataHelper.getInstance().getErrorLogs();
                    if (result.isError()) {
                        errorMessage = result.getMessage();
                        error = true;
                    }
                }
                result.setError(error);
                result.setMessage(errorMessage);*/
                if (result.isError()) {
                    retryCount++;
                    retryUID = getId();
                    DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, result.getMessage());
                    return Result.Retry.retry();
                }
                if (result.getStatus().equals(ZConfigManager.Status.Success)) {
                    DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Info, "Master data refresh is completed " + getId());
                    sendNotificationWithChannel("Master Data Refresh Completed");
                    scheduleNextRequest();
                    return Result.Success.success();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            retryCount++;
            retryUID = getId();
            return Result.retry();
        }
        return Result.Retry.retry();
    }

    private void scheduleNextRequest() {
        try {

            Data inputData = new Data.Builder().putLong("start_time", System.currentTimeMillis()).build();
            OneTimeWorkRequest.Builder nextRequest = new OneTimeWorkRequest.Builder(MasterDataRefreshWorker.class)
                    .addTag(ZConfigManager.MASTER_DATA_REFRESH_TAG)
                    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, ZConfigManager.MasterData_BG_Refresh_Retry_Interval_In_Min, TimeUnit.MINUTES)
                    .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                    .setInputData(inputData);
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Info, "Next MasterDataRefresh id " + getId());

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(ZCollections.Last_Refresh_Time, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            long targetMillSeconds = sharedPreferences.getLong(ZConfigManager.MasterDataRefresh_TargetDate, 0);
            Date targetDate = new Date();
            targetDate.setTime(targetMillSeconds);
            Calendar nextCal = Calendar.getInstance();
            Calendar previousDate = Calendar.getInstance();
            if (ZConfigManager.MasterData_BG_Refresh_Unit_In_Hours) {
                int currentHour = nextCal.get(Calendar.HOUR_OF_DAY);
                previousDate.setTime(targetDate);
                int previousHour = previousDate.get(Calendar.HOUR_OF_DAY);//currenthour=17,previousHour=16
                int nextTargetHour = 0;
                if (currentHour == previousHour || currentHour < previousHour) {
                    nextTargetHour = ZConfigManager.MasterData_BG_Refresh_Interval_Value;
                    nextCal.add(Calendar.HOUR_OF_DAY, nextTargetHour);
                }
                if (currentHour > previousHour) {
                    nextTargetHour = currentHour - previousHour;//currentHour=13,previousHour=6,interval=4
                    if (nextTargetHour > ZConfigManager.MasterData_BG_Refresh_Interval_Value) {
                        nextCal.add(Calendar.HOUR_OF_DAY, ZConfigManager.MasterData_BG_Refresh_Interval_Value);
                        nextTargetHour = ZConfigManager.MasterData_BG_Refresh_Interval_Value;
                    } else
                        nextCal.add(Calendar.HOUR_OF_DAY, nextTargetHour);
                }
                nextRequest.setInitialDelay(nextTargetHour, TimeUnit.HOURS);
                editor.putLong(ZConfigManager.MasterDataRefresh_TargetDate, nextCal.getTimeInMillis());
                DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Info, "Next Master Data Refresh time " + nextCal.getTime());
            } else {
                long difference = Math.abs(nextCal.getTimeInMillis() - targetDate.getTime());
                int differenceDates = (int) (difference / (24 * 60 * 60 * 1000));
                if (differenceDates >= ZConfigManager.MasterData_BG_Refresh_Interval_Value)
                    differenceDates = ZConfigManager.MasterData_BG_Refresh_Interval_Value;
                else
                    differenceDates = ZConfigManager.MasterData_BG_Refresh_Interval_Value - differenceDates;

                nextCal.add(Calendar.DAY_OF_MONTH, differenceDates);
                nextRequest.setInitialDelay(differenceDates, TimeUnit.DAYS);
                editor.putLong(ZConfigManager.MasterDataRefresh_TargetDate, nextCal.getTimeInMillis());
                DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Info, "Next Master Data Refresh date" + nextCal.getTime());
            }
            editor.putString("Result_M", "Success");
            editor.putLong("LastSyncTime", System.currentTimeMillis());
            editor.apply();

            WorkManager.getInstance(getApplicationContext()).enqueueUniqueWork(ZConfigManager.MASTER_DATA_REFRESH_TAG, ExistingWorkPolicy.KEEP, nextRequest.build());
            SharedPreferences preferences = getApplicationContext().getSharedPreferences(ZCollections.SERVER_DETAILS_SP_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = preferences.edit();
            editor1.putLong(ZCollections.ARG_LAST_MASTER_DATA_SYNC_TIME, ZCommon.getDeviceDateTime().getTimeInMillis());
            editor1.apply();
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    private void sendNotificationWithChannel(String messageBody) {
        try {

            String channelId = "MasterDataRefresh";
            CharSequence name = "Refresh Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationManager notificationManager =
                    (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new
                        NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            Notification notification = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification = new Notification.Builder(getApplicationContext(), channelId)
                        .setContentTitle("myJobCard")
                        .setContentText(messageBody)
                        .setSmallIcon(android.R.drawable.stat_notify_sync)
                        .setChannelId(channelId)
                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.app_icon))
                        .setAutoCancel(true)
                        .setStyle(new Notification.BigTextStyle().bigText(messageBody))
                        .build();
            }

            Random rand = new Random();
            int randomNum = rand.nextInt((99 - 1) + 1) + 1;
            notificationManager.notify(randomNum, notification);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }
}
