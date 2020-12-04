package com.ods.myjobcard_library.workers;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.viewmodels.WorkInfoRespository;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class ScheduleWork {
    private static final String TAG = "ScheduleWork";
    private final String PERIODICREQUEST = "BackgroundJob";
    private final String TRANSACTION_WORK = "TransactionWork";
    private String workTag;
    private Observer<WorkInfo> workInfoObserver;
    private PeriodicWorkRequest workRequest;

    public ScheduleWork(String workTag) {
        this.workTag = workTag;
        Log.d(TAG, "ScheduleWork: WorkTag Name" + workTag);
    }

    public static LiveData<Boolean> isMyWorkerRunning(String tag) {
        MediatorLiveData<Boolean> result = new MediatorLiveData<>();
        LiveData<List<WorkInfo>> statusesByTag = WorkManager.getInstance().getWorkInfosByTagLiveData(tag);
        result.addSource(statusesByTag, (workInfos) -> {
            boolean isWorking;
            if (workInfos == null || workInfos.isEmpty())
                isWorking = false;
            else {
                WorkInfo.State workState = workInfos.get(0).getState();
                isWorking = !workState.isFinished();
                // WorkInfo workState = statusesByTag.get(0).getState();
                //isWorking = !workState.isFinished();
            }
            result.setValue(isWorking);
            //remove source so you don't get further updates of the status
            result.removeSource(statusesByTag);
        });
        return result;
    }

    public Constraints getNetWorkConstraint() {
        return new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
    }

    public void scheduleMasterDataRefresh() {
        try {
            OneTimeWorkRequest uniqueMasterRefresh = new OneTimeWorkRequest.Builder(MasterDataRefreshWorker.class)
                    .setConstraints(getNetWorkConstraint())
                    .addTag(ZConfigManager.MASTER_DATA_REFRESH_TAG)
                    .build();
            WorkManager.getInstance().enqueueUniqueWork(ZConfigManager.MASTER_DATA_REFRESH_TAG, ExistingWorkPolicy.APPEND, uniqueMasterRefresh);
            obserWorkRequestWithId(uniqueMasterRefresh.getId());
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            Log.e(TAG, "scheduleMasterDataRefresh: ", e);
        }

    }

    public void doSchedulePeriodicRequest() {
        try {
            Data.Builder inputData = new Data.Builder();
            inputData.putInt("RetryCount", 0);
            OneTimeWorkRequest uniqueRequest = new OneTimeWorkRequest.Builder(TimeBasedFlushWorker.class)
                    .setConstraints(getNetWorkConstraint())
                    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5, TimeUnit.MINUTES)
                    .addTag(ZConfigManager.PERIODIC_REQUEST)
                    .setInputData(inputData.build())
                    .build();
            final WorkManager mWorkManager = WorkManager.getInstance();
            mWorkManager.enqueueUniqueWork(PERIODICREQUEST, ExistingWorkPolicy.KEEP, uniqueRequest);
            WorkInfoRespository.getInstance().setWorkId(uniqueRequest.getId());
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public void workSchedule() {
        try {
            Long BACKOFF_DELAY_SECONDS = 60L;
            /*if (isWorkScheduled()) {
                LiveData liveData = isMyWorkerRunning(workTag);
                if (liveData.getValue() != null) {
                    Log.d(TAG, "workSchedule: IsMyWokrkerIsRunning " + liveData.getValue().toString());
                }
                Log.d(TAG, "workSchedule: already work in queue with Work tag is");
                Log.d(TAG, "workSchedule: Previous Work Status");
                final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(BackgroundFlush.class)
                        .addTag(workTag)
                        .setConstraints(getNetWorkConstraint())
                        //Once the doWork is return the result is retry then this request will start after 60 seconds
                        .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, BACKOFF_DELAY_SECONDS, TimeUnit.SECONDS)
                        .build();
                Log.d(TAG, "workSchedule: WorkRequest TAG is "+workTag+" and Id is "+request.getId());

                WorkManager.getInstance().enqueueUniqueWork(TRANSACTION_WORK,ExistingWorkPolicy.KEEP,request);
                obserWorkRequestWithId(request.getId());
                    //observeWorkRequest(workTag);
            }
            else{*/
            Log.d(TAG, "workSchedule: New work in queue");
            Data.Builder inputData = new Data.Builder().putString("Entity", workTag);
            inputData.putInt("RetryCount", 0);
            //Once the doWork is return the result is retry then this request will start after 60 seconds
            OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(EventBasedFlushWorker.class)
                    .addTag(ZConfigManager.TRANSACTION_WORK)
                    //Once the doWork is return the result is retry then this request will start after 60 seconds
                    .setBackoffCriteria(BackoffPolicy.LINEAR, BACKOFF_DELAY_SECONDS, TimeUnit.SECONDS)
                    .setInputData(inputData.build())
                    .setConstraints(getNetWorkConstraint()).build();
            WorkManager.getInstance().enqueueUniqueWork(ZConfigManager.TRANSACTION_WORK, ExistingWorkPolicy.KEEP, oneTimeWorkRequest);
            Log.d(TAG, "workSchedule: WorkRequest TAG is " + workTag + " and Id is " + oneTimeWorkRequest.getId());
            obserWorkRequestWithId(oneTimeWorkRequest.getId());
        } catch (Exception e) {
            Log.e(TAG, "workSchedule: " + e.getMessage());
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    private void obserWorkRequestWithId(UUID id) {
        try {
            WorkManager.getInstance().getWorkInfoByIdLiveData(id).observeForever(new Observer<WorkInfo>() {
                @Override
                public void onChanged(@Nullable WorkInfo workInfo) {
                    try {
                        if (workInfo != null && workInfo.getState() != null) {
                            if (workInfo.getTags().contains(PERIODICREQUEST)) {
                                WorkInfoRespository.getInstance().setCurrentWorkInfo(workInfo);
                                Log.d(TAG, "onChanged: Observe with work tag" + workTag + "id  " + workInfo.getId() + " and the state is " + workInfo.getState().name());
                            } else if (workInfo.getState().isFinished() || workInfo.getState().equals(WorkInfo.State.RUNNING)) {
                                WorkInfoRespository.getInstance().setCurrentWorkInfo(workInfo);
                                Log.d(TAG, "onChanged: Observe with work tag" + workTag + "id  " + workInfo.getId() + " and the state is " + workInfo.getState().name());
                            }
                        }
                    } catch (Exception e) {
                        DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    private boolean isWorkScheduled() {
        try {

            if (WorkInfoRespository.getInstance().getCurrentWorkInfo().getValue() != null) {
                WorkInfo info = WorkInfoRespository.getInstance().getCurrentWorkInfo().getValue();
                if (info.getState().isFinished() || info.getState().equals(WorkInfo.State.FAILED)) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "isWorkScheduled: ", e);
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return false;
    }
}
