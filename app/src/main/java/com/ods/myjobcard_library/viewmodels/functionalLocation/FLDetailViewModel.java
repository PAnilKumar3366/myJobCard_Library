package com.ods.myjobcard_library.viewmodels.functionalLocation;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ctentities.BreakdownReport;
import com.ods.myjobcard_library.entities.ctentities.Equipment;
import com.ods.myjobcard_library.entities.ctentities.FLCharacteristics;
import com.ods.myjobcard_library.entities.ctentities.FLClassificationSet;
import com.ods.myjobcard_library.entities.ctentities.FunctionalLocation;
import com.ods.myjobcard_library.viewmodels.BaseViewModel;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.StoreHelpers.TableConfigSet;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.client.odata.v4.EntityValue;
import com.sap.client.odata.v4.EntityValueList;

import java.util.ArrayList;

public class FLDetailViewModel extends BaseViewModel {

    public MutableLiveData<ArrayList<FLCharacteristics>> mFLCharacteristics = new MutableLiveData<>();
    private MutableLiveData<FunctionalLocation> flLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Equipment>> InstalledEQList = new MutableLiveData<>();
    private ArrayList<Equipment> installedEquipments = new ArrayList<>();
    private MutableLiveData<ArrayList<BreakdownReport>> onlineBDRList = new MutableLiveData<>();
    private ArrayList<BreakdownReport> breakdownReports = new ArrayList<>();
    private MutableLiveData<ArrayList<FLClassificationSet>> FlClassificationList = new MutableLiveData<>();

    public FLDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<FLClassificationSet>> getFlClassificationList() {
        return FlClassificationList;
    }

    public void setFlClassificationList(FunctionalLocation functionalLocation) {
        try {
            ArrayList<FLClassificationSet> FLClassificationsArrayList = functionalLocation.getClassifications();
            ResponseObject result = FLClassificationSet.getFLClassificationSet(functionalLocation.getFunctionalLoc());
            if (!result.isError())
                FLClassificationsArrayList = (ArrayList<FLClassificationSet>) result.Content();
            FlClassificationList.setValue(FLClassificationsArrayList);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public MutableLiveData<FunctionalLocation> getFlLiveData() {
        return flLiveData;
    }

    public MutableLiveData<ArrayList<BreakdownReport>> getOnlineBreakDownReports() {
        return onlineBDRList;
    }

    public void setFlLiveData() {
        flLiveData.setValue(FunctionalLocation.getCurrFl());
    }

    public MutableLiveData<ArrayList<FLCharacteristics>> getmFLCharacteristics() {
        return mFLCharacteristics;
    }

    public void setInstalledEQList(String superiorEquipmentId, String functionalLocationId) {
        loadInstallEQ(superiorEquipmentId, functionalLocationId);
    }

    public void setmFLCharacteristics(FLClassificationSet currentClassification, String FLocation) {
        try {
            ArrayList<FLCharacteristics> characteristics = new ArrayList<>();
            ResponseObject result = FLCharacteristics.getFLCharacteristics(currentClassification.getClassType(), FLocation);
            if (!result.isError())
                characteristics = (ArrayList<FLCharacteristics>) result.Content();
            mFLCharacteristics.setValue(characteristics);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    private void loadInstallEQ(String superiorEquipmentId, String functionalLocationId) {
        try {
            new AsyncTask<Void, Void, Boolean>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Boolean doInBackground(Void... params) {
                    installedEquipments = Equipment.getInstalledEquipments(superiorEquipmentId, functionalLocationId);
                    return true;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    if (aBoolean) {
                        InstalledEQList.setValue(installedEquipments);
                    }
                }

            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public MutableLiveData<ArrayList<Equipment>> getInstalledEQList() {
        return InstalledEQList;
    }

    @SuppressLint("StaticFieldLeak")
    public void fetchBDROnlineData(String filterQuery) {
        String resPath = ZCollections.BREAKDOWN_REPORT_ENTITY_COLLECTION + filterQuery;
        try {
            new AsyncTask<Void, Void, ResponseObject>() {

                @Override
                protected ResponseObject doInBackground(Void... voids) {
                    ResponseObject result = DataHelper.getInstance().getEntitiesOnline(resPath, ZCollections.BREAKDOWN_REPORT_ENTITY_COLLECTION, TableConfigSet.getStore(ZCollections.BREAKDOWN_REPORT_ENTITY_COLLECTION));
                    return result;
                }

                @Override
                protected void onPostExecute(ResponseObject responseObject) {
                    super.onPostExecute(responseObject);
                    try {
                        getBreakDownReportList(responseObject);
                    } catch (Exception e) {
                        DliteLogger.WriteLog(FLDetailViewModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            DliteLogger.WriteLog(FLDetailViewModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

    }

    private void getBreakDownReportList(ResponseObject responseObject) {
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
        onlineBDRList.setValue(breakdownReports);
    }
}
