package com.ods.myjobcard_library.viewmodels.workorder;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.HashMap;

public class WorkorderDetailFragmentViewModel extends ViewModel {

    private HashMap<String, Integer> workorderTabCount = new HashMap<String, Integer>();
    private MutableLiveData<HashMap<String, Integer>> mTabCount = new MutableLiveData<HashMap<String, Integer>>();

    public MutableLiveData<HashMap<String, Integer>> getTabCount() {
        return mTabCount;
    }

    public void setTabCount(WorkOrder workOrder) {
        resetTabCount(workOrder);
    }

    private void resetTabCount(final WorkOrder mItem) {
        try {
            new AsyncTask<Void, Void, Boolean>() {

                @Override
                protected Boolean doInBackground(Void... params) {
                    try {
                        //if (tabList == ZAppSettings.TabList.Operations) {
                        workorderTabCount.put("totalOperations", mItem.getTotalNumOperations());
                        workorderTabCount.put("compOperations", mItem.getTotalNumCompletedOperations());
                        workorderTabCount.put("activeOperations", Operation.getNumberOfActiveOperations(mItem.getWorkOrderNum()));

                        //} else if (tabList == ZAppSettings.TabList.Parts) {
                        workorderTabCount.put("unIssuedComponents", mItem.getTotalNumUnIssuedComponents());
                        workorderTabCount.put("partialIssuedComponents", mItem.getTotalNumPartialIssuedComponents());
                        workorderTabCount.put("totalComponents", mItem.getTotalNumComponents());
                        //} else if (tabList == ZAppSettings.TabList.Attachments) {
                        workorderTabCount.put("totalAttachments", mItem.getTotalNumAttachments());
                        workorderTabCount.put("userUploaded", mItem.getTotalNumUserUploadedAttachments());
                        //} else if (tabList == ZAppSettings.TabList.Forms) {
                        workorderTabCount.put("unSubmittedForm", mItem.getTotalNumUnSubmittedMandatoryForms());
                        workorderTabCount.put("unSubmittedOptionalForms", mItem.getTotalNumUnSubmittedOptionalForms());
                        workorderTabCount.put("totalForms", mItem.getTotalNumForms());
                        //} else if (tabList == ZAppSettings.TabList.MeasurementPoints) {
                        workorderTabCount.put("totalPoints", mItem.getTotalNumMeasurementPoints());
                        workorderTabCount.put("totalReadingTaken", mItem.getTotalNumReadingTaken());
                        //}
                        //Inspection Lot
                        workorderTabCount.put("totalCharacteristics", mItem.getTotalInspectionCharacteristicsCount(mItem.getInspectionLot(),
                                ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED ? mItem.getCurrentOperation().getOperationNum() : null));
                        workorderTabCount.put("savedCharacteristics", mItem.getSavedInspectionCharacteristicsCount(mItem.getInspectionLot(),
                                ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED ? mItem.getCurrentOperation().getOperationNum() : null));
                    } catch (Exception e) {
                        return false;
                    }
                    return true;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    if (aBoolean) {
                        //updateTabAlertCount(tab, tabList);
                        mTabCount.setValue(workorderTabCount);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }
}