package com.ods.myjobcard_library.viewmodels;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.R;
import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.appsetting.AppStoreSet;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.ConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.ods.ods_sdk.utils.OfflineAsyncHelper;
import com.sap.smp.client.odata.ODataEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BaseViewModel extends AndroidViewModel implements ZCommon.TransmitProgressCallback {

    public String errorMessage;
    public boolean isError = false;
    protected ResponseObject response;
    protected MutableLiveData<ResponseObject> transmitResponse;
    protected MutableLiveData<String> transmitUpdateMsg;
    protected SharedPreferences preferences;
    protected SharedPreferences.Editor editor;
    private MutableLiveData<Boolean> posting = new MutableLiveData<>();
    private MutableLiveData<String > error=new MutableLiveData<>();
    public static String QUERIABLE_DATE_FORMAT = "yyyy-MM-dd'T'00:00:00";
    private String TAG = BaseViewModel.class.getSimpleName();

    public BaseViewModel(@NonNull Application application) {
        super(application);
        preferences = getApplication().getSharedPreferences(ZCollections.SERVER_DETAILS_SP_NAME, MODE_PRIVATE);
        transmitResponse = new MutableLiveData<>();
        transmitUpdateMsg = new MutableLiveData<>();
    }


    public MutableLiveData<Boolean> getPosting() {
        return posting;
    }

    public void setPosting(MutableLiveData<Boolean> posting) {
        this.posting = posting;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void setError(String errorMessage) {
        error.setValue(errorMessage);
    }

    public MutableLiveData<ResponseObject> getTransmitResponse() {
        return transmitResponse;
    }

    public void setTransmitResponse(MutableLiveData<ResponseObject> transmitResponse) {
        this.transmitResponse = transmitResponse;
    }

    public MutableLiveData<String> getTransmitUpdateMsg() {
        return transmitUpdateMsg;
    }

    public void setTransmitUpdateMsg(MutableLiveData<String> transmitUpdateMsg) {
        this.transmitUpdateMsg = transmitUpdateMsg;
    }

    /*protected void fetchEntitiesOnline(String resPath, String entitySetName) {
        OnlineAsyncHelper asyncHelper = new OnlineAsyncHelper(resPath, entitySetName, true, new OnlineAsyncHelper.Callbacks() {
            @Override
            public void onResult(ResponseObject responseObject) {
                ArrayList<ZODataEntity> entityList = new ArrayList<>();
                try {
                    if (responseObject != null && !responseObject.isError()) {
                        isError = false;
                        List<ODataEntity> entities = ZBaseEntity.setODataEntityList(responseObject.Content());
                        for (ODataEntity entity : entities) {
                            ZODataEntity zoDataEntity = new ZODataEntity(entity);
                            entityList.add(zoDataEntity);
                        }
                        onFetchEntitiesResult(entityList);
                    } else {
                        isError = true;
                        errorMessage = response.getMessage();
                        transmitResponse.setValue(response);
                    }
                } catch (Exception e) {
                    DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
                }
            }
        });
        asyncHelper.execute((Void) null);
    }*/

    protected void fetchEntitiesOffline(String resPath, String entitySetName) {
        OfflineAsyncHelper helper = new OfflineAsyncHelper(resPath, entitySetName, new OfflineAsyncHelper.Callbacks() {
            @Override
            public void onResult(ResponseObject response) {
                ArrayList<ZODataEntity> entityList = new ArrayList<>();
                if (response != null && !response.isError()) {
                    isError = false;
                    List<ODataEntity> entities = ZBaseEntity.setODataEntityList(response.Content());
                    for (ODataEntity entity : entities) {
                        ZODataEntity item = new ZODataEntity(entity);
                        entityList.add(item);
                    }
                    onFetchEntitiesResult(entityList);
                } else {
                    isError = true;
                    errorMessage = response.getMessage();
                    transmitResponse.setValue(response);
                }
            }
        });
        helper.execute((Void) null);
    }
    public void onFetchEntitiesResult(ArrayList<ZODataEntity> entities) {

    }

    @Override
    public void update(String text) {
        transmitUpdateMsg.setValue(text);
    }

    @Override
    public void errorCallback(ResponseObject response) {
        transmitResponse.setValue(response);
    }

    @Override
    public void onSuccess(ResponseObject response) {
        transmitResponse.setValue(response);
    }

    @Override
    public void noNetworkError() {
        ResponseObject result = new ResponseObject(ConfigManager.Status.Error);
        result.setMessage(getApplication().getString(R.string.alertNetworkUnAvailableTitle));
        transmitResponse.setValue(result);
    }
    public void refreshStores(ArrayList<AppStoreSet> storeList) {
        ZCommon.showTransmitProgress(getApplication(), this,storeList);
    }
    public void copyRAWtoSDCard(){
        try {
            boolean isCopyRawFiles=ZCommon.copyRAWtoSDCard(getApplication());
           if(!isCopyRawFiles) {
              // ResponseObject responseObject = new ResponseObject(ConfigManager.Status.Error, "Error in offline data", null);
               error.setValue("Error in offline data");
           }
           /*else
               ZCommon.copyAssetsToSDCard(getApplication());*/

        } catch (IOException e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }
    public final void logout() {
        closeStoresGoToLoginScreen();
    }

    /**
     * To make user Logout
     */
    public final void logoutUser() {
        try {
            if (pendingRequestsExist()) return;
            if (ZConfigManager.SHOW_LOGOUT_ALERT) {
                transmitUpdateMsg.setValue("logoutPopup");
            } else {
                closeStoresGoToLoginScreen();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }
    public boolean pendingRequestsExist() {
        response = DataHelper.getInstance().PendingRequestExists();
        if (response.getStatus() == ZConfigManager.Status.Warning) {
            transmitUpdateMsg.setValue("Pending requests exist. " + response.getMessage());
            return true;
        } else return false;
    }
    /**To close the stores at the time when user is logging-out*/
    private void closeStoresGoToLoginScreen() {
        try {
            response = DataHelper.getInstance().CloseAllStores();
            if (!response.isError()) {
                //SharedPreferences preferences = getApplication().getSharedPreferences(ZCollections.SERVER_DETAILS_SP_NAME, MODE_PRIVATE);
                if (preferences.contains(ZCollections.ARG_IS_LOGGED_IN)) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove(ZCollections.ARG_IS_LOGGED_IN);
                    editor.apply();
                }
                ZAppSettings.isLoggedIn = false;
            }
            transmitUpdateMsg.setValue("loggedOut");
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }

    }

    public void printErrorLog(Class cls, String errorMessage) {
        DliteLogger.WriteLog(cls.getClass(), ZAppSettings.LogLevel.Error, errorMessage);
    }
}
