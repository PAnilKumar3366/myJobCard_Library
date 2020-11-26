package com.ods.myjobcard_library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.utils.Common;
import com.ods.ods_sdk.utils.ConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.ods.ods_sdk.utils.DliteLogger.WriteLog;

/**
 * Created by LENOVO on 11-07-2016.
 */
public class ZCommon extends Common {

    private static DateFormat df = new SimpleDateFormat(ConfigManager.DATE_FORMAT);
    private static DateFormat tf = new SimpleDateFormat(ConfigManager.TIME_FORMAT);
    private static final String TAG = "Common";
    public static Date getDeviceTime() {
        GregorianCalendar dt = null;
        try {
            dt = (GregorianCalendar) GregorianCalendar.getInstance();
        } catch (Exception e) {
            WriteLog(Common.class.getClass(), AppSettings.LogLevel.Error, e.getMessage());
            return null;
        }
        return dt.getTime();
    }

    public static String getReqTimeStamp(int reqLength) {
        try {
            String mSecsStr = String.valueOf(getDeviceTime().getTime());
            int length = mSecsStr.length();
            if (length > reqLength) {
                return mSecsStr.substring(length - reqLength);
            }
            return mSecsStr;
        } catch (Exception e) {
            WriteLog(Common.class.getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return null;
    }

    public static GregorianCalendar getDeviceDateTime() {
        GregorianCalendar dt = null;
        try {
            dt = (GregorianCalendar) GregorianCalendar.getInstance();
        } catch (Exception e) {
            WriteLog(Common.class.getClass(), AppSettings.LogLevel.Error, e.getMessage());
            return null;
        }
        return dt;
    }
    public static Calendar getDevicDateTime() {
        Calendar dt = null;
        try {
            dt = Calendar.getInstance();
        } catch (Exception e) {
            WriteLog(Common.class.getClass(), AppSettings.LogLevel.Error, e.getMessage());
            return null;
        }
        return dt;
    }

    public static String getFormattedDate(Date date) {
        if (date == null || date.getTime() == ConfigManager.getDefaultCalendarVal().getTimeInMillis())
            return "";
        return df.format(date);
    }

    public static String getFormattedTime(Date date) {
        if (date == null || date.getTime() == ConfigManager.getDefaultCalendarVal().getTimeInMillis())
            return "";
        return tf.format(date);
    }

    public static String getFormattedTime(Time time) {
        if (time == null)
            return "";
        return tf.format(time);
    }

    public static String getFormattedInt(int numOfDigits, int value) {
        return String.format("%0" + numOfDigits + "d", value);
    }

    public static String getFormattedInt(int numOfDigits, BigInteger value) {
        return String.format("%0" + numOfDigits + "d", value);
    }

    public static String convertDoubleToString(Double value) {
        if (value == null)
            return "";

        return BigDecimal.valueOf(value).toPlainString();
    }

    public static Double convertStringToDouble(String value) {
        Double doubleVal = null;
        try {
            if (value != null && !value.isEmpty()) {
                value = value.contains(",") ? value.replace(",", ".") : value;
                doubleVal = Double.valueOf(value);
            }
        } catch (Exception e) {
            WriteLog(Common.class, AppSettings.LogLevel.Error, e.getMessage());
        }
        return doubleVal;
    }

    public static String getTimeDurationInHoursString(long startTimeInMilli, long endTimeInMilli) {
        String duration = "0";
        try {
            if (startTimeInMilli > 0 && endTimeInMilli > startTimeInMilli) {
                long millis = endTimeInMilli - startTimeInMilli;
                long minuteInMilli = 1000 * 60;
                long hourInMilli = minuteInMilli * 60;
                long elapsedHours = millis / hourInMilli;
                millis = millis % hourInMilli;
                long elapsedMinutes = millis / minuteInMilli;

                float fractionHour = elapsedMinutes / 60.0f;
                float totalHours = elapsedHours + fractionHour;
                return "" + totalHours;
            }
        } catch (Exception e) {
            WriteLog(Common.class, AppSettings.LogLevel.Error, e.getMessage());
        }
        return duration;
    }

    public static BigDecimal getDurationInHours(int hours, int minutes){
        return BigDecimal.valueOf(hours + (minutes / 60.0f));
    }

    public static String getTimeDurationInString(long startTimeInMilli, long endTimeInMilli) {
        String duration = "0";
        try {
            if (startTimeInMilli > 0 && endTimeInMilli > startTimeInMilli) {
                long millis = endTimeInMilli - startTimeInMilli;
                long minuteInMilli = 1000 * 60;
                long hourInMilli = minuteInMilli * 60;
                long elapsedHours = millis / hourInMilli;
                millis = millis % hourInMilli;
                long elapsedMinutes = millis / minuteInMilli;

                /*float fractionHour = elapsedMinutes / 60.0f;
                float totalHours = elapsedHours + fractionHour;
                return ""+totalHours;*/
                if (elapsedMinutes < 10)
                    return elapsedHours + ".0" + elapsedMinutes;
                return elapsedHours + "." + elapsedMinutes;
            }
        } catch (Exception e) {
            WriteLog(Common.class, AppSettings.LogLevel.Error, e.getMessage());
        }
        return duration;
    }
    public static String getTimeDuration(long startTimeInMilli, long endTimeInMilli) {
        String duration = "0";

        long difference;
        try {
            if(startTimeInMilli<endTimeInMilli) {
                difference = endTimeInMilli - startTimeInMilli;
            }
            else {
                difference = startTimeInMilli - endTimeInMilli;
            }

            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours))
                    / (1000 * 60);
            hours = (hours < 0 ? -hours : hours);
            return String.valueOf(Double.valueOf(hours)+(Double.valueOf(min)/60));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duration;
    }

    public static String convertStrWithCommaToProperDoubleStr(String value) {
        return convertDoubleToString(convertStringToDouble(value));
    }

    public static String getEntitySetNameFromPath(String resPath) {
        String entitySetName = "";
        try {
            if (resPath.contains("/"))
                resPath = resPath.replace("/", "");
            if (resPath.contains("(")) {
                resPath = resPath.substring(0, resPath.indexOf("("));
            }
            entitySetName = resPath;
        } catch (Exception e) {
            WriteLog(Common.class, AppSettings.LogLevel.Error, e.getMessage());
        }
        return entitySetName;
    }

   /* public static void ShowTransmitProgress(final FrameLayout frameLayout, final TextView textView, final ArrayList<AppStoreSet> storeList, final BaseActivity activityContext) {
        try {
            final Context context = frameLayout.getContext();
            if (chkNetworkAvailable(context)) {
                if (DataHelper.isBGFlushInProgress) {
                    frameLayout.setVisibility(View.GONE);
                    ((BaseActivity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    DialogsUtility.showAlertPopup(context, context.getString(R.string.warningAlertComponentAvailabilityTitle), context.getString(R.string.alertBackgroundSyncInProgressMsg));

                    return;
                }
                new AsyncTask<Void, String, ResponseObject>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        frameLayout.setVisibility(View.VISIBLE);
                        ((BaseActivity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }

                    @Override
                    protected ResponseObject doInBackground(Void... params) {
                        ResponseObject res = null;
                        //StoreSettings.Stores[] storeList = null;
                        try {
                            //storeList = new StoreSettings.Stores[1];
                            //storeList[0] = StoreSettings.Stores.Tx;
                            res = DataHelper.getInstance().PendingRequestExists(storeList);
                            if (res != null && res.getStatus() == ConfigManager.Status.Warning) {
                                publishProgress("Uploading...");
                                res = DataHelper.getInstance().Flush(storeList);
                            }
                            if (res != null && !res.isError()) {
//                            publishProgress("uploaded successfully!");
//                            Thread.sleep(1000);
                                publishProgress("Downloading...");
                                res = DataHelper.getInstance().Refresh(storeList);
                                if (!res.isError()) {
                                    //publishProgress("downloaded successfully!");
                                    //-----------------------------------------------
                                    publishProgress("Synchronisation completed");
                                    //-----------------------------------------------


                                    Thread.sleep(1000);
                                    SharedPreferences preferences = context.getSharedPreferences(Collections.SERVER_DETAILS_SP_NAME, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    if (storeList.get(0).getFlush().equalsIgnoreCase("1"))
                                        editor.putLong(Collections.ARG_LAST_SYNC_TIME, getDeviceDateTime().getTimeInMillis());
                                    else
                                        editor.putLong(Collections.ARG_LAST_MASTER_DATA_SYNC_TIME, getDeviceDateTime().getTimeInMillis());
                                    editor.apply();


                                    return res;
                                } else {
                                    publishProgress("Something went wrong while downloading!");
                                    Thread.sleep(1000);
                                    return res;
                                }
                            } else {
                                publishProgress("Something went wrong while uploading!");
                                Thread.sleep(1000);
                                return res;
                            }
                        } catch (Exception e) {
                            DliteLogger.WriteLog(Common.class, AppSettings.LogLevel.Error, e.getMessage());
                            return new ResponseObject(ConfigManager.Status.Error, e.getMessage(), null);
                        }
                    }

                    @Override
                    protected void onProgressUpdate(String... values) {
                        super.onProgressUpdate(values);
                        textView.setText(values[0]);
                    }

                    @Override
                    protected void onPostExecute(ResponseObject response) {
                        super.onPostExecute(response);
                        try {
                            boolean isError = false;
                            frameLayout.setVisibility(View.GONE);
                            ((BaseActivity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            if (response.isError()) {
                                ErrorViewDialog errorViewDialog = new ErrorViewDialog(context, storeList, (BaseActivity) (context), false);
                                errorViewDialog.show();
                                errorViewDialog.setWindowSize();
                                isError = true;
                                *//*if (response.Content() != null) {
                                    ArrayList<ErrorObject> errors = (ArrayList<ErrorObject>) response.Content();
                                    if (errors != null && errors.size() > 0) {
                                        StringBuilder stringBuilder = new StringBuilder();
                                        int count = 1;
                                        for (ErrorObject error : errors) {
                                            if (error.getMessage() != null && error.getMessage().getMessage() != null) {
                                                stringBuilder.append(error.getMessage().getMessage().getValue())
                                                        //.append("\nDetails: " + error.getObjectName())
                                                        .append(count == errors.size() ? "" : "\n---------------------------------\n");
                                                DliteLogger.WriteLog(Common.class, AppSettings.LogLevel.Error, error.getMessage().getMessage().getValue() + "\n[Request Method] " + error.getRequestMethod() + "\n[Request Body] " + error.getRequestBody());
                                            }
                                            count++;
                                        }
                                        isError = true;
                                        //todo remover hardcoding 'Error'
//                                    DialogsUtility.showAlertPopup(pDialog.getContext(), "Error", stringBuilder.toString());
                                    *//**//*DialogsUtility.showAlertPopupWithOneOpt(pDialog.getContext(), "Error", stringBuilder.toString(), pDialog.getContext().getString(R.string.txtNeutralBtn),
                                            new DialogsUtility.OnPositiveBtnClickListener(){
                                                @Override
                                                public void onPositiveBtnClick(Dialog dialog) {
                                                    DataHelper.getInstance().DeleteErrors();
                                                    dialog.dismiss();
                                                }
                                            });*//**//*

                                        DialogsUtility.showAlertPopupWithTwoOpt(context, activityContext.getString(R.string.errorTitle), !stringBuilder.toString().isEmpty() ? stringBuilder.toString() : context.getString(R.string.backendError), "Clear Errors", "Cancel",
                                                new DialogsUtility.OnPositiveBtnClickListener() {
                                                    @Override
                                                    public void onPositiveBtnClick(Dialog dialog) {
                                                        DataHelper.getInstance().DeleteErrors(storeList);
                                                        activityContext.onTransmitCompleted();
                                                        dialog.dismiss();
                                                    }
                                                },
                                                new DialogsUtility.OnNegativeBtnClickListener() {
                                                    @Override
                                                    public void onNegativeBtnClick(Dialog dialog) {
                                                        activityContext.onTransmitCompleted();
                                                        dialog.dismiss();
                                                    }
                                                });
                                    }
                                }*//*
                            }
                            *//*if (ConfigManager.DEFAULT_ASSIGNMENT_TYPE.equalsIgnoreCase(AppSettings.AssignmentType.WorkCenterSingleIdLevel.getAssignmentTypeText()) && (storeList.get(0).getFlush().equalsIgnoreCase("1"))) {
                                SharedPreferences preferences = activityContext.getSharedPreferences(Collections.SERVER_DETAILS_SP_NAME, Context.MODE_PRIVATE);
                                PersonResponsible user = new PersonResponsible();
                                user.setEmplApplName(preferences.getString(Collections.ARG_SECONDARY_USER_FULLNAME, ""));
                                user.setPersonnelNo(preferences.getString(Collections.ARG_SECONDARY_USER_ID, ""));
                                UserTable.setUserDetails(user);
                            }*//*
                            if(storeList.get(0).getRefresh().equalsIgnoreCase("2")){
                                AppStoreSet.getAppOfflineStores(true,false);
                                TableConfigSet.getTableDetails();
                                ConfigManager.setAppConfigurations();
                                AppFeature.setUserRoleFeatures();
                            }
                            if(!isError)
                                activityContext.onTransmitCompleted();
                        } catch (Exception e) {
                            DliteLogger.WriteLog(Common.class, AppSettings.LogLevel.Error, e.getMessage());
                        }
                    }

                    @Override
                    protected void onCancelled() {
                        isCancelled();
                    }
                }.execute(null, null, null);
            } else {
                frameLayout.setVisibility(View.GONE);
                ((BaseActivity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                DialogsUtility.showAlertPopup(context, context.getString(R.string.alertNetworkUnAvailableTitle), context.getString(R.string.alertNetworkUnAvailableMsg));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Common.class, AppSettings.LogLevel.Error, e.getMessage());
        }
    }
    public static void ShowErrorLogProgress(final FrameLayout frameLayout, final TextView textView, final ArrayList<AppStoreSet> storeList, final BaseActivity activityContext) {
        try {
            final Context context = frameLayout.getContext();
            new AsyncTask<ArrayList<AppStoreSet>, String, ResponseObject>() {

                @SuppressLint("WrongThread")
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    frameLayout.setVisibility(View.VISIBLE);
                    publishProgress("Erro Logs are loading ... Please wait");
                    ((BaseActivity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

                @Override
                protected ResponseObject doInBackground(ArrayList<AppStoreSet>... params) {
                    ResponseObject res = null;
                    try {
                        res = DataHelper.getInstance().getErrorLogs(storeList);
                        return res;
                    } catch (Exception e) {
                        DliteLogger.WriteLog(Common.class, AppSettings.LogLevel.Error, e.getMessage());
                        return new ResponseObject(ConfigManager.Status.Error, e.getMessage(), null);
                    }
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    super.onProgressUpdate(values);
                    textView.setText(values[0]);
                }

                @Override
                protected void onPostExecute(ResponseObject response) {
                    super.onPostExecute(response);
                    try {
                        frameLayout.setVisibility(View.GONE);
                        ((BaseActivity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if (response.isError()) {
                            if (response.Content() != null) {
                                ArrayList<ErrorObject> errors = (ArrayList<ErrorObject>) response.Content();
                                if (errors != null && errors.size() > 0) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    int count = 1;
                                    for (ErrorObject error : errors) {
                                        if (error.getMessage() != null && error.getMessage().getMessage() != null) {
                                            stringBuilder.append(error.getMessage().getMessage().getValue())
                                                    //.append("\nDetails: " + error.getObjectName())
                                                    .append(count == errors.size() ? "" : "\n---------------------------------\n");
                                            DliteLogger.WriteLog(Common.class, AppSettings.LogLevel.Error, error.getMessage().getMessage().getValue() + "\n[Request Method] " + error.getRequestMethod() + "\n[Request Body] " + error.getRequestBody());
                                        }
                                        count++;
                                    }
                                    DialogsUtility.showAlertPopupWithTwoOpt(context, activityContext.getString(R.string.errorTitle), !stringBuilder.toString().isEmpty() ? stringBuilder.toString() : context.getString(R.string.backendError), "Clear Errors", "Cancel",
                                            new DialogsUtility.OnPositiveBtnClickListener() {
                                                @Override
                                                public void onPositiveBtnClick(Dialog dialog) {
                                                    DataHelper.getInstance().DeleteErrors(storeList);
                                                    //activityContext.onTransmitCompleted();
                                                    dialog.dismiss();
                                                }
                                            },
                                            new DialogsUtility.OnNegativeBtnClickListener() {
                                                @Override
                                                public void onNegativeBtnClick(Dialog dialog) {
                                                    //activityContext.onTransmitCompleted();
                                                    dialog.dismiss();
                                                }
                                            });
                                }
                            }
                        }
                        else
                        {
                            DialogsUtility.showAlertPopup(context, activityContext.getString(R.string.errorTitle), activityContext.getString(R.string.errorLogsMsg));
                            return;
                        }
                    } catch (Exception e) {
                        DliteLogger.WriteLog(Common.class, AppSettings.LogLevel.Error, e.getMessage());
                    }
                }

                @Override
                protected void onCancelled() {
                    isCancelled();
                }
            }.execute(storeList, null, null);
        } catch (Exception e) {
            DliteLogger.WriteLog(Common.class, AppSettings.LogLevel.Error, e.getMessage());
        }
    }*/

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static float convertBytesToMB(long byteSize) {
        return convertBytesToKB(byteSize) / ZCollections.Convert_KB_MB;
    }

    public static float convertBytesToKB(long byteSize) {
        return byteSize / ZCollections.Convert_KB_MB;
    }

    public static float getTwoDecimalPlaces(float value) {
        return Math.round(value * 100f) / 100f;
    }

    public static String getTwoDecimalPlacesStr(float value) {
        return new DecimalFormat("##.##").format(value);
    }

    public static String getThreeDecimalPlacesStr(float value) {
        return new DecimalFormat("##.###").format(value);
    }

    public static boolean isNetworkAvailable(Context context) {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
                //we are connected to a network but checking whether internet is working or not
                return new AsyncTask<Void, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(Void... params) {
                        try {
                            if (AppSettings.isHttps)
                                return isInternetAvailable();
                            Socket socket = new Socket();
                            socket.connect(new InetSocketAddress(AppSettings.App_IP, AppSettings.App_Port), 5000);
                            return true;//InetAddress.getByName(AppSettings.App_IP).isReachable(1000);
                        } catch (Exception e) {
                            return false;
                        }
                    }

                }.execute().get();
            }
        } catch (Exception e) {
            WriteLog(Common.class, AppSettings.LogLevel.Error, e.getMessage());
        }
        return false;
    }

    public static boolean chkNetworkAvailable(Context paramContext) {
        ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext.getSystemService(CONNECTIVITY_SERVICE);
        if (localConnectivityManager == null) ;
        NetworkInfo localNetworkInfo;
        do {

            localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
            if (localNetworkInfo == null)
                return false;

        } while (localNetworkInfo == null);
        return localNetworkInfo.isConnected();
    }

    private static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validateTextLength(String text, int length) {
        try {
            if (text != null)
                return text.length() <= length;
        } catch (Exception e) {
            WriteLog(Common.class, AppSettings.LogLevel.Error, e.getMessage());
        }
        return false;
    }

 /*   public static Class getNextClass(String screenName) {
        Class nextClass = null;
        try {
            nextClass = Class.forName(ScreenMapping.getClassName(screenName));
        } catch (Exception e) {
            DliteLogger.WriteLog(Common.class, AppSettings.LogLevel.Error, e.getMessage());
        }
        return nextClass;
    }*/

    public static Object getClassInstance(Class nextClass){
        Object nextObject = null;
        try {
            Constructor constructor = nextClass.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            WriteLog(Common.class, AppSettings.LogLevel.Error, e.getMessage());
        }
        return nextObject;
    }

    public static Object getClassInstanceWithContext(Class nextClass,  Context context){
        Object nextObject = null;
        try {
            Constructor constructor = nextClass.getConstructor(Context.class);
            return constructor.newInstance(context);
        } catch (Exception e) {
            WriteLog(Common.class, AppSettings.LogLevel.Error, e.getMessage());
        }
        return nextObject;
    }



    public static boolean isTwoNumericStringValuesEqual(String one, String two) {
        boolean result = false;
        try {
            if (one != null && two != null) {
                if (Long.parseLong(one) == Long.parseLong(two))
                    result = true;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            WriteLog(Common.class, AppSettings.LogLevel.Error, e.getLocalizedMessage());
            result = false;
        }
        return result;
    }

}
