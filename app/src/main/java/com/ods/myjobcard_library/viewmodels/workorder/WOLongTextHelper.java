package com.ods.myjobcard_library.viewmodels.workorder;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.interfaces.BackgroundTaskInterface;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.ConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.ods.ods_sdk.utils.OnlineAsyncHelper;
import com.sap.client.odata.v4.EntityValue;
import com.sap.client.odata.v4.EntityValueList;

import java.util.ArrayList;
import java.util.HashMap;

public class WOLongTextHelper {
    public BackgroundTaskInterface TaskInterface;
    private MutableLiveData<ResponseObject> OnlineLongText;

    public WOLongTextHelper() {

    }

    public MutableLiveData<ResponseObject> getOnlineLongText() {
        return OnlineLongText;
    }

    public void setTaskInterface(BackgroundTaskInterface taskInterface) {
        TaskInterface = taskInterface;
    }

    public String getOnlineQuery(HashMap<String, String> onlineQueryMap) {
        OnlineLongText = new MutableLiveData<>();
        StringBuilder WoLongTextQuery = null;
        try {
            String baseQuery = "?$filter=(OnlineSearch eq 'X' and ";
            WoLongTextQuery = new StringBuilder();
            WoLongTextQuery.append(baseQuery);
            if (onlineQueryMap.containsKey("WorkOrderNum")) {
                WoLongTextQuery.append("WorkOrderNum eq'").append(onlineQueryMap.get("WorkOrderNum")).append("' and ");
            }
            if (onlineQueryMap.containsKey("Item"))
                WoLongTextQuery.append("Item eq'").append(onlineQueryMap.get("WorkOrderNum")).append("' and ");
            String finalQuery = " " + WoLongTextQuery.toString();
            WoLongTextQuery.delete(finalQuery.length() - 6, WoLongTextQuery.length());
            WoLongTextQuery.append(")");
            Log.d("WOLongTextHelper", "getOnlineQuery: final Query " + WoLongTextQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            return "";
        }

        return WoLongTextQuery.toString();
    }

    public void getOnlineWOLongText(String finalQuery) {
        ArrayList<ZODataEntity> entityList = new ArrayList<>();
        String resPath = ZCollections.WO_LONG_TEXT_COLLECTION + finalQuery;
        OnlineAsyncHelper onlineAsyncHelper = new OnlineAsyncHelper(resPath, ZCollections.WO_LONG_TEXT_COLLECTION, false, new OnlineAsyncHelper.Callbacks() {
            @Override
            public void onResult(ResponseObject responseObject) {
                try {
                    if (responseObject != null) {
                        if (!responseObject.isError()) {
                            EntityValueList entities = (EntityValueList) responseObject.Content();
                            for (EntityValue entity : entities) {
                                ZODataEntity item = new ZODataEntity(entity);
                                entityList.add(item);
                            }
                            responseObject.setContent(entityList);
                            OnlineLongText.postValue(responseObject);
                            //TaskInterface.onTaskPostExecute(entityList, false, "");
                        } else
                            OnlineLongText.postValue(responseObject);
                        //TaskInterface.onTaskPostExecute(entityList, true, responseObject.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                    OnlineLongText.postValue(new ResponseObject(ConfigManager.Status.Error, e.getMessage(), null));
                }

            }
        });
        onlineAsyncHelper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
