package com.ods.myjobcard_library.viewmodels.equipment;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ctentities.BreakdownReport;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.StoreHelpers.TableConfigSet;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.client.odata.v4.EntityValue;
import com.sap.client.odata.v4.EntityValueList;

import java.util.ArrayList;

public class BreakDownReportViewModel extends BaseViewModel {

    private MutableLiveData<ArrayList<BreakdownReport>> onlineBDRList = new MutableLiveData<>();
    private ArrayList<BreakdownReport> breakdownReports = new ArrayList<>();

    public BreakDownReportViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("StaticFieldLeak")
    public void fetchBDROnlineData(String filterQuery) {
        String resPath = ZCollections.BREAKDOWN_REPORT_ENTITY_COLLECTION + filterQuery;
        try {
            new AsyncTask<Void, Void, ResponseObject>() {

                @Override
                protected ResponseObject doInBackground(Void... voids) {
                    return DataHelper.getInstance().getEntitiesOnline(resPath, ZCollections.BREAKDOWN_REPORT_ENTITY_COLLECTION, TableConfigSet.getStore(ZCollections.BREAKDOWN_REPORT_ENTITY_COLLECTION));
                }

                @Override
                protected void onPostExecute(ResponseObject responseObject) {
                    super.onPostExecute(responseObject);
                    getBreakDownReportList(responseObject);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            DliteLogger.WriteLog(EquipmentDetailViewModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

    }

    public MutableLiveData<ArrayList<BreakdownReport>> getOnlineBreakDownReports() {
        return onlineBDRList;
    }

    private void getBreakDownReportList(ResponseObject responseObject) {
        try {
            if (!responseObject.isError()) {
                breakdownReports.clear();
                EntityValueList entityList = (EntityValueList) responseObject.Content();
                EntityValueList oprEntityList;
                ArrayList<BreakdownReport> onlineBDRdata = new ArrayList<>();
                for (EntityValue entityValue : entityList) {
                    BreakdownReport order = new BreakdownReport(entityValue);
                    onlineBDRdata.add(order);
                }
                breakdownReports.addAll(onlineBDRdata);
            } else
                setError(responseObject.getMessage());
        } catch (Exception e) {
            DliteLogger.WriteLog(EquipmentDetailViewModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        onlineBDRList.setValue(breakdownReports);
    }
}
