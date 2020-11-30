package com.ods.myjobcard_library.viewmodels.workorder;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;
import java.util.List;

public class WorkOrdersListLiveData extends MutableLiveData<List<WorkOrder>> {

    private static final String TAG = "WorkOrdersListLiveData";
    private static String filterquery;
    private final Context context;
    private ResponseObject result;
    private ArrayList<WorkOrder> workOrders = null;
    private ArrayList<WorkOrder> workOrderSubset = null;
    private ArrayList<WorkOrder> workOrdersList = new ArrayList<>();

    public WorkOrdersListLiveData(Context context) {
        this.context = context;
        workOrders = new ArrayList<>();
        //loadData();
    }

    public void loadData() {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               /* pDialog = new ProgressDialog(context);
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.setMessage(context.getString(R.string.msg_fetching_workorders));
                pDialog.show();*/
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    if (filterquery != null && !filterquery.isEmpty())
                        workOrderSubset = (ArrayList<WorkOrder>) WorkOrder.getFilteredWorkOrders(filterquery, ZAppSettings.FetchLevel.ListWithStatusAllowed, null).Content();
                    result = WorkOrder.getWorkOrders(ZAppSettings.FetchLevel.List, null, null);
                    if (!result.isError()) {
                        workOrders = (ArrayList<WorkOrder>) result.Content();
                    }
                    return true;
                } catch (Exception e) {
                    DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                //  pDialog.dismiss();
                if (!aBoolean) {
                    workOrders = new ArrayList<WorkOrder>();
                    setValue(workOrders);
                } else {
                    onDataLoadSuccess();
                }
               /* if (context.searchEditText.getText().toString().isEmpty())
                   // onDataLoadSuccess();
                else
                   // refreshListAfterSearch(searchEditText.getText().toString());*/
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void onDataLoadSuccess() {
        Log.d("WorkOrderLivedata", "onDataLoadSuccess: ");
        if (workOrderSubset != null && workOrderSubset.size() > 0 && !filterquery.isEmpty()) {
            setValue(workOrderSubset);
        } else {
            setValue(workOrders);
        }
    }

    @Override
    protected void onActive() {
        super.onActive();
        Log.d(TAG, "onActive: ");
    }

    @Override
    protected void onInactive() {
        super.onInactive();
    }

    public void setFilterQuery(String filterQuery) {
        filterquery = filterQuery;
        loadData();
    }

    public ArrayList<WorkOrder> getWorkOrdersList() {
        if (workOrders.size() > 0) {
            return workOrders;
        }
        return new ArrayList<>();
    }

}
