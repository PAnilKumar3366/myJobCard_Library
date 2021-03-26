package com.ods.myjobcard_library.viewmodels.notification;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.ConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.ods.ods_sdk.utils.OnlineAsyncHelper;
import com.sap.client.odata.v4.EntityValue;
import com.sap.client.odata.v4.EntityValueList;

import java.util.ArrayList;
import java.util.HashMap;

public class NoLongTextHelper {

    private MutableLiveData<ResponseObject> longTextLiveData;

    public NoLongTextHelper() {

    }

    public MutableLiveData<ResponseObject> getLongTextLiveData() {
        return longTextLiveData;
    }

    public String getOnlineQuery(HashMap<String, String> onlineQueryMap) {
        StringBuilder NoLongTextQuery = null;
        longTextLiveData = new MutableLiveData<>();
        try {
            String baseQuery = "?$filter=(OnlineSearch eq 'X' and ";
            NoLongTextQuery = new StringBuilder();
            NoLongTextQuery.append(baseQuery);
            if (onlineQueryMap.containsKey("Notification")) {
                NoLongTextQuery.append("Notification eq'").append(onlineQueryMap.get("Notification")).append("' and ");
            }
            if (onlineQueryMap.containsKey("Item"))
                NoLongTextQuery.append("Item eq'").append(onlineQueryMap.get("WorkOrderNum")).append("' and ");
            if (onlineQueryMap.containsKey("NotificationItem"))
                NoLongTextQuery.append("NotificationItem eq'").append(onlineQueryMap.get("NotificationItem")).append("' and ");
            String finalQuery = " " + NoLongTextQuery.toString();
            NoLongTextQuery.delete(finalQuery.length() - 6, NoLongTextQuery.length());
            NoLongTextQuery.append(")");
            Log.d("WOLongTextHelper", "getOnlineQuery: final Query " + NoLongTextQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            return "";
        }

        return NoLongTextQuery.toString();
    }

    public void getOnlineNOLongText(String finalQuery) {
        ArrayList<ZODataEntity> entityList = new ArrayList<>();
        String resPath = ZCollections.NOTIFICATION_LONG_TEXT_COLLECTION + finalQuery;
        OnlineAsyncHelper onlineAsyncHelper = new OnlineAsyncHelper(resPath, ZCollections.NOTIFICATION_LONG_TEXT_COLLECTION, false, new OnlineAsyncHelper.Callbacks() {
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
                            longTextLiveData.postValue(responseObject);
                            //TaskInterface.onTaskPostExecute(entityList, false, "");
                        } else
                            longTextLiveData.postValue(responseObject);
                        //TaskInterface.onTaskPostExecute(entityList, true, responseObject.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                    longTextLiveData.postValue(new ResponseObject(ConfigManager.Status.Error, e.getMessage(), null));
                }

            }
        });
        onlineAsyncHelper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
