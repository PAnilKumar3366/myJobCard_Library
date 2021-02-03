package com.ods.myjobcard_library.viewmodels;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.Notification;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.StoreHelpers.TableConfigSet;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.client.odata.v4.EntityValue;
import com.sap.client.odata.v4.EntityValueList;

import java.util.ArrayList;

public class OnlinePendingListViewModel extends BaseViewModel {
    private static final String TAG = "OnlinePendingListViewMo";
    private MutableLiveData<ArrayList<WorkOrder>> onlineWoList = new MutableLiveData<>();
    private MutableLiveData<Notification> onlineNotificationList = new MutableLiveData<>();
    private ArrayList<WorkOrder> onlineWorkorders = new ArrayList<>();

    public OnlinePendingListViewModel(@NonNull Application application) {
        super(application);
    }

    public ArrayList<WorkOrder> getOnlineWorkorders() {
        return onlineWorkorders;
    }

    public MutableLiveData<ArrayList<WorkOrder>> getOnlineWoList() {
        return onlineWoList;
    }

    public void setOnlineWoList(String woQuery) {
        String entitySetName = "WoHeaderSet";
        String resPath = entitySetName + woQuery;
        try {
            new AsyncTask<Void, Void, ResponseObject>() {

                @Override
                protected ResponseObject doInBackground(Void... voids) {
                    ResponseObject result = DataHelper.getInstance().getEntitiesOnline(resPath, entitySetName, TableConfigSet.getStore(entitySetName));
                    return result;
                }

                @Override
                protected void onPostExecute(ResponseObject responseObject) {
                    super.onPostExecute(responseObject);
                    try {
                        getWorkordersList(responseObject);
                    } catch (Exception e) {
                        DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    private void getWorkordersList(ResponseObject responseObject) {
        try {
            if (!responseObject.isError()) {
                //operations.clear();
                onlineWorkorders.clear();
                EntityValueList entityList = (EntityValueList) responseObject.Content();
                EntityValueList oprEntityList;
                ArrayList<WorkOrder> onlineworkOrdersList = new ArrayList<>();
                ArrayList<Operation> workOrderOperations;
                for (EntityValue entityValue : entityList) {

                    WorkOrder order = new WorkOrder(entityValue);
                    oprEntityList = entityValue.getEntityType().getProperty("NAVOPERA").getEntityList(entityValue);

                    workOrderOperations = new ArrayList<>();
                        /*for (EntityValue oprEntity : oprEntityList) {
                            workOrderOperations.add(new Operation(oprEntity));
                            operations.add(new Operation(oprEntity));
                        }*/
                    order.setWorkOrderOperations(workOrderOperations);
                    onlineworkOrdersList.add(order);
                    //oprEntityValueList.add( entityValue.getEntityType().getProperty("NAVOPERA").getEntityList(entityValue));
                }
                onlineWorkorders.addAll(onlineworkOrdersList);
                Log.d(TAG, "getWorkordersList: " + onlineworkOrdersList.size());
                onlineWoList.setValue(onlineWorkorders);
                  /*  if (onlineworkOrdersList.size() == 0) {
                        showSnackbar(context.getString(R.string.msgNoWorkOrdersAvailable), mBinding.getRoot());
                    } else {
                        DialogsUtility.showOnlineWOListAlertPopupWithOneOpt(context, "", onlineWorkorders, context.getString(R.string.txtNeutralBtn),
                                new DialogsUtility.OnPositiveBtnClickListener() {
                                    @Override
                                    public void onPositiveBtnClick(Dialog dialog) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                } else {
                    showSnackbar(context.getString(R.string.workorders_fetch_failed), getRootView());
                }
                pDialog.dismiss();*/


            } else
                setError(responseObject.getMessage());
        } catch (Exception e) {
            DliteLogger.WriteLog(OnlinePendingListViewModel.class, ZAppSettings.LogLevel.Error, e.getMessage());
            setError(e.getMessage());
            e.printStackTrace();
        }

    }
}
