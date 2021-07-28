package com.ods.myjobcard_library.viewmodels;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.appsettings.AppFeature;
import com.ods.myjobcard_library.entities.ctentities.UserTable;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.Collections;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.StoreHelpers.RegisterHelper;
import com.ods.ods_sdk.StoreHelpers.StoreSettings;
import com.ods.ods_sdk.StoreHelpers.StoreStatusAsyncHelper;
import com.ods.ods_sdk.StoreHelpers.TableConfigSet;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;

public class LoginActivityViewModel extends BaseViewModel implements RegisterHelper.Callbacks {

    MutableLiveData<String> update;
    MutableLiveData<String> error;
    private String userName, password, host, port, appname;
    private boolean isHttps;
    private boolean isDemoMode;
    private boolean isFirstDemoLogin;
    private boolean oldUserLogin;
    private boolean saml2Auth;
    RegisterHelper helper;


    //constructor
    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        update = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public void login(String userName, String oldUser, String password, String host, String port, String appname, boolean isHttps, Context context, Boolean oldUserLogin, boolean isDemo, boolean isFirstDemoLogin)
    {
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
        this.appname = appname;
        this.isHttps = isHttps;
        this.isDemoMode=isDemo;
        this.isFirstDemoLogin = isFirstDemoLogin;
        this.oldUserLogin = oldUserLogin;
        String oldPass = preferences.getString(ZCollections.ARG_USER_PASSWORD, "");
        ZAppSettings.AppStoreName="APLLICATIONSTORE";
        UserTable.resetUserDetails();
        helper = RegisterHelper.getInstance(context, this);
        helper.setIsFirstDemoLogin(isFirstDemoLogin);
        helper.enableSAML2Auth(saml2Auth);
        helper.initRegistration(userName, oldUser, password, oldPass, isHttps, host, port, appname, oldUserLogin,isDemoMode);
    }
    public boolean fireBaseTokenConfiguration(String appConnID,String tokenID){
       // helper.fireBaseTokenConfiguration(appConnID,tokenID);
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return helper.fireBaseTokenConfiguration(appConnID,tokenID);
                //return null;
            }
        }.execute();
        return true;
    }

    public LiveData<String> getError() {
        return error;
    }

    @Override
    public void setError(String s) {
        error.setValue(s);
        Log.e("LoginVM: ", s);
    }

    public LiveData<String> getUpdate() {
        return update;
    }

    public void setSAML2Auth(boolean saml2Auth) {
        this.saml2Auth = saml2Auth;
    }

    @Override
    public void setSAML2UserName(String username) {
        ZAppSettings.strUser = username;
    }

    @Override
    public void onUserChangeDeletePushSubscription() {
        DataHelper.getInstance().changeStoreStatus(StoreSettings.SyncOptions.DeletePushSubScription);
    }

    @Override
    public void updateUI(String update) {
        this.update.setValue(update);
        Log.d("LoginVM: ", update);
    }

    @Override
    public void putSharedPreferences(String s, String s1) {
        Log.d("LoginVM: ", s + " : " + s1);
        editor = preferences.edit();
        editor.putString(s, s1);
        editor.commit();
    }

    @Override
    public void putSharedPreferences(String s, Boolean aBoolean) {
        Log.d("LoginVM: ", s + " : " + aBoolean);
        editor = preferences.edit();
        editor.putBoolean(s, aBoolean);
        editor.commit();
    }

    @Override
    public void putSharedPreferences(String s, Long aLong) {
        Log.d("LoginVM: ", s + " : " + aLong);
        editor = preferences.edit();
        editor.putLong(s, aLong);
        editor.commit();
    }

    @Override
    public void onSuccess() {
        Log.d("Login: ", "Success!!!");
        if(!ZAppSettings.IS_DEMO_MODE) {
            ZAppSettings.App_ID = appname;
            ZAppSettings.App_IP = host;
            ZAppSettings.App_Port = Integer.parseInt(port);
            if(! saml2Auth)
                ZAppSettings.strUser = userName;
            ZAppSettings.strPassword = password;
            ZAppSettings.isLoggedIn = true;
            ZAppSettings.isOpenOnlineAPStore=preferences.getBoolean(ZCollections.IS_ONLINE_APPSTORE,true);
        }
        //ZConfigManager.setAppConfigurations();
        updateUI("success");
    }

    @Override
    public void onDeregisterSuccess(){
        preferences.edit().remove(Collections.ARG_USER_ID).remove(Collections.ARG_USER_PASSWORD).remove(Collections.ARG_APP_CONNECTION_ID).remove(Collections.ARG_FCM_TOKEN).remove(ZCollections.IS_ONLINE_APPSTORE).apply();
        AppFeature.resetUserRoleFeatures();
    }

    @Override
    public void onRegisterSuccess() {

       /* if (AppSettings.IS_DEMO_MODE && isFirstDemoLogin)
            new StoreStatusAsyncHelper(StoreSettings.SyncOptions.InitDemoMode, new StoreStatusAsyncHelper.Callbacks() {
                @Override
                public void onResult(ResponseObject response) {
                    new StoreStatusAsyncHelper(StoreSettings.SyncOptions.InitStores, new StoreStatusAsyncHelper.Callbacks() {
                        @Override
                        public void onResult(ResponseObject response) {
                            putSharedPreferences(Collections.ARG_IS_LOGGED_IN, true);
                            ZAppSettings.isLoggedIn = true;
                            onSuccess();
                        }

                        @Override
                        public void update(String update) {
                        }
                    }).execute((Void) null);
                }

                @Override
                public void update(String update) {

                }
            }).execute((Void) null);
        else {*/

        if(AppSettings.IS_DEMO_MODE) {
            copyRAWtoSDCard();
            AppSettings.App_IP="mobile-mpyi7hx6ox.eu3.hana.ondemand.com";
        }
        else
            ZAppSettings.isOpenOnlineAPStore=preferences.getBoolean("IS_ONLINE_APPSTORE",true);
        updateUI("We are keeping the things ready. Please Wait...");
        ZCommon.copyAssetsToSDCard(getApplication());
        new StoreStatusAsyncHelper(StoreSettings.SyncOptions.InitStores, new StoreStatusAsyncHelper.Callbacks() {
            @Override
            public void onResult(ResponseObject response) {
                if(response.getMessage().equalsIgnoreCase("Error opening App Settings store"))
                    putSharedPreferences(ZCollections.IS_ONLINE_APPSTORE, true);
                else {
                    putSharedPreferences(ZCollections.IS_ONLINE_APPSTORE, false);
                    ZAppSettings.userFirstName = UserTable.getUserFirstName();
                    ZAppSettings.userLastName = UserTable.getUserLastName();
                }
                putSharedPreferences(ZCollections.ARG_IS_LOGGED_IN, true);

                if (ZConfigManager.ENABLE_PUSH_SUBCRIPTION&&!AppSettings.IS_DEMO_MODE&&response.getStatus().equals(ZConfigManager.Status.Success) && (ZAppSettings.App_FCM_Token == null || ZAppSettings.App_FCM_Token.isEmpty())) {
                    updateUI("FCM Registration");
                }
                else
                    onSuccess();
            }

            @Override
            public void update(String update) {
                if(update.equalsIgnoreCase("AppStore Open")){
                    // copyAssetsToSDCard(getApplication());
                    ZConfigManager.setAppConfigurations();
                    if(ZConfigManager.BackEndUser != null && !ZConfigManager.BackEndUser.isEmpty()){
                        ZAppSettings.strUser = ZConfigManager.BackEndUser;
                    }
                }
            }
        }).execute((Void) null);
        //}
    }

    public void PushSubscription(String appConn) {
        boolean result = false;
        com.ods.myjobcard_library.entities.appsettings.PushSubscription pushSubscription = null;
        ResponseObject responseObject;
        try {
            //ClientLogManager.writeLogDebug("Start push subscription to server");
            pushSubscription=new com.ods.myjobcard_library.entities.appsettings.PushSubscription(ZAppSettings.strUser,appConn,ZCollections.APP_STORE_COLLECTION);
            responseObject = pushSubscription.SaveToStore(true);
            //flush to SAP for subscription
            if (!responseObject.isError()) {
                //if (!ConfigManager.ENABLE_BG_SYNC) {
              /*  ArrayList<AppStoreSet> stores = new ArrayList<>();
                stores.add(com.ods.ods_sdk.StoreHelpers.TableConfigSet.getStore(ZCollections.PUSH_ENTITY_COLLECTION));*/
                responseObject= DataHelper.getInstance().FlushPushSub_App(TableConfigSet.getStore(ZCollections.PUSH_ENTITY_COLLECTION));
                //}
                if (!responseObject.isError())
                    result = true;
                //ClientLogManager.writeLogDebug("Subscription saved successfully");
                updateUI("success");
            }

        } catch (Exception ex) {
            result = false;
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, ex.getMessage());
            //ClientLogManager.writeLogDebug("Subscription pushed to server failed");
        }
    }

}
