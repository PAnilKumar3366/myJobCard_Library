package com.ods.myjobcard_library;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;

import com.ods.myjobcard_library.entities.appsettings.AppFeature;
import com.ods.myjobcard_library.utils.DocsUtil;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.StoreHelpers.StoreSettings;
import com.ods.ods_sdk.StoreHelpers.TableConfigSet;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.appsetting.AppStoreSet;
import com.ods.ods_sdk.utils.Common;
import com.ods.ods_sdk.utils.ConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.ods.ods_sdk.utils.DliteLogger.WriteLog;

/**
 * Created by LENOVO on 11-07-2016.
 */
public class ZCommon extends Common {

    private static final String TAG = "Common";
    private static DateFormat df = new SimpleDateFormat(ZConfigManager.DATE_FORMAT);
    private static DateFormat tf = new SimpleDateFormat(ZConfigManager.TIME_FORMAT);

    public static Date getDeviceTime() {
        GregorianCalendar dt = null;
        try {
            dt = (GregorianCalendar) GregorianCalendar.getInstance();
        } catch (Exception e) {
            WriteLog(Common.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
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
            WriteLog(Common.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return null;
    }

    public static GregorianCalendar getDeviceDateTime() {
        GregorianCalendar dt = null;
        try {
            dt = (GregorianCalendar) GregorianCalendar.getInstance();
        } catch (Exception e) {
            WriteLog(Common.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            return null;
        }
        return dt;
    }

    public static Calendar getDevicDateTime() {
        Calendar dt = null;
        try {
            dt = Calendar.getInstance();
        } catch (Exception e) {
            WriteLog(Common.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            return null;
        }
        return dt;
    }

    public static String getFormattedDate(Date date) {
        if (date == null || date.getTime() == ZConfigManager.getDefaultCalendarVal().getTimeInMillis())
            return "";
        return df.format(date);
    }

    public static String getFormattedTime(Date date) {
        if (date == null || date.getTime() == ZConfigManager.getDefaultCalendarVal().getTimeInMillis())
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
            WriteLog(Common.class, ZAppSettings.LogLevel.Error, e.getMessage());
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
            WriteLog(Common.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return duration;
    }

    public static BigDecimal getDurationInHours(int hours, int minutes) {
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
            WriteLog(Common.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return duration;
    }

    public static String getTimeDuration(long startTimeInMilli, long endTimeInMilli) {
        String duration = "0";

        long difference;
        try {
            if (startTimeInMilli < endTimeInMilli) {
                difference = endTimeInMilli - startTimeInMilli;
            } else {
                difference = startTimeInMilli - endTimeInMilli;
            }

            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours))
                    / (1000 * 60);
            hours = (hours < 0 ? -hours : hours);
            return String.valueOf(Double.valueOf(hours) + (Double.valueOf(min) / 60));
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
            WriteLog(Common.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return entitySetName;
    }

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
                            if (ZAppSettings.isHttps)
                                return isInternetAvailable();
                            Socket socket = new Socket();
                            socket.connect(new InetSocketAddress(ZAppSettings.App_IP, ZAppSettings.App_Port), 5000);
                            return true;//InetAddress.getByName(ZAppSettings.App_IP).isReachable(1000);
                        } catch (Exception e) {
                            return false;
                        }
                    }

                }.execute().get();
            }
        } catch (Exception e) {
            WriteLog(Common.class, ZAppSettings.LogLevel.Error, e.getMessage());
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
            WriteLog(Common.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return false;
    }

/*    public static Class getNextClass(String screenName) {
        Class nextClass = null;
        try {
            nextClass = Class.forName(ScreenMapping.getClassName(screenName));
        } catch (Exception e) {
            DliteLogger.WriteLog(Common.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return nextClass;
    }*/

    public static Object getClassInstance(Class nextClass) {
        Object nextObject = null;
        try {
            Constructor constructor = nextClass.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            WriteLog(Common.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return nextObject;
    }

    public static Object getClassInstanceWithContext(Class nextClass, Context context) {
        Object nextObject = null;
        try {
            Constructor constructor = nextClass.getConstructor(Context.class);
            return constructor.newInstance(context);
        } catch (Exception e) {
            WriteLog(Common.class, ZAppSettings.LogLevel.Error, e.getMessage());
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
            WriteLog(Common.class, ZAppSettings.LogLevel.Error, e.getLocalizedMessage());
            result = false;
        }
        return result;
    }

    public static void showTransmitProgress(final Context context, final TransmitProgressCallback callback, final ArrayList<AppStoreSet> storeList) {
        try {

            if (chkNetworkAvailable(context)) {
                if (ZConfigManager.isBGFlushInProgress) {
                    ResponseObject response = new ResponseObject(ZConfigManager.Status.Warning);
                    response.setMessage("Background sync in progress");
                    callback.errorCallback(response);
                    return;
                }
                new AsyncTask<Void, String, ResponseObject>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected ResponseObject doInBackground(Void... voids) {
                        ResponseObject res = null;
                        try {
                            boolean isTxStore = storeList.get(0).getFlush().equalsIgnoreCase("1");
                            if (isTxStore) {
                                res = DataHelper.getInstance().PendingRequestExists(storeList);
                                if (res != null && res.getStatus() == ConfigManager.Status.Warning) {
                                    publishProgress(context.getString(R.string.msg_uploading));
                                    res = DataHelper.getInstance().changeStoreStatus(StoreSettings.SyncOptions.Flush_Tx_Only);
                                }
                            } else
                                res = new ResponseObject(ConfigManager.Status.Success, "", null);

                            res = DataHelper.getInstance().getErrorLogs(storeList);
                            if ((res != null && !res.isError())) {
                                publishProgress(context.getString(R.string.msg_downloading));
                                res = DataHelper.getInstance().changeStoreStatus(isTxStore ? StoreSettings.SyncOptions.Refresh_All_Trans_Stores : StoreSettings.SyncOptions.Refresh_All_Master_Stores);
                                res = DataHelper.getInstance().getErrorLogs(storeList);
                                if (!res.isError()) {
                                    publishProgress(context.getString(R.string.msg_sync_complete));
                                    Thread.sleep(1000);
                                    SharedPreferences preferences = context.getSharedPreferences(ZCollections.SERVER_DETAILS_SP_NAME, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    GregorianCalendar deviceTime = getDeviceDateTime();
                                    if (deviceTime != null) {
                                        if (isTxStore)
                                            editor.putLong(ZCollections.ARG_LAST_SYNC_TIME, getDeviceDateTime().getTimeInMillis());
                                        else
                                            editor.putLong(ZCollections.ARG_LAST_MASTER_DATA_SYNC_TIME, getDeviceDateTime().getTimeInMillis());
                                        editor.apply();
                                    }
                                    DocsUtil.RemoveUnRequiredUploadEntities();
                                } else {
                                    publishProgress(context.getString(R.string.msg_something_went_wrong_downloading));
                                    Thread.sleep(1000);
                                }
                            } else {
                                publishProgress(context.getString(R.string.msg_something_went_wrong_uploading));
                                Thread.sleep(1000);
                            }
                            return res;
                        } catch (Exception e) {
                            DliteLogger.WriteLog(Common.class, ZAppSettings.LogLevel.Error, e.getMessage());
                            return new ResponseObject(ConfigManager.Status.Error, e.getMessage(), null);
                        }
                    }

                    @Override
                    protected void onProgressUpdate(String... values) {
                        super.onProgressUpdate(values);
                        callback.update(values[0]);
                    }

                    @Override
                    protected void onPostExecute(ResponseObject response) {
                        super.onPostExecute(response);
                        try {
                            boolean isError = false;
                            if (response.isError()) {
                                isError = true;
                                response.setMessage("BEErrors");
                                callback.errorCallback(response);
                            }
                            if (storeList.get(0).getRefresh().equalsIgnoreCase("2")) {
                                AppStoreSet.getStoreList();
                                TableConfigSet.getTableDetails();
                                ConfigManager.setAppConfigurations();
                                AppFeature.setUserRoleFeatures();
                            }

                            if (!isError)
                                callback.onSuccess(response);
                        } catch (Exception e) {
                            DliteLogger.WriteLog(Common.class, ZAppSettings.LogLevel.Error, e.getMessage());
                        }
                    }
                }.execute(null, null, null);
            } else {
                callback.noNetworkError();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Common.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    //Copy raw files to SDCard
    public static boolean copyRAWtoSDCard(final Context context) throws IOException {
        boolean result = false;
        try {
            int[] fileResIds = new int[]{R.raw.apllicationstore, R.raw.apllicationstore_rq, R.raw.formenginestore, R.raw.formenginestore_rq,
                    R.raw.highvolumestore, R.raw.highvolumestore_rq, R.raw.lowvolumestore, R.raw.lowvolumestore_rq,
                    R.raw.qmstore, R.raw.qmstore_rq, R.raw.supervisorstore, R.raw.supervisorstore_rq, R.raw.userstore, R.raw.userstore_rq, R.raw.wmstore, R.raw.wmstore_rq};
            String logsPath = Environment.getExternalStorageDirectory() + File.separator + ZAppSettings.App_Name + File.separator + "logs";
            String path = logsPath + File.separator + "stores";
            String[] fileNames = new String[]{"apllicationstore", "apllicationstore_rq", "formenginestore", "formenginestore_rq"
                    , "highvolumestore", "highvolumestore_rq", "lowvolumestore", "lowvolumestore_rq"
                    , "qmstore", "qmstore_rq", "supervisorstore", "supervisorstore_rq", "userstore", "userstore_rq", "wmstore", "wmstore_rq"};
            InputStream in;
            FileOutputStream out;
            byte[] buff;
            int read;
            String filePath;
            File storesDirectory = new File(logsPath, "stores");
            if (!storesDirectory.exists())
                storesDirectory.mkdirs();
            for (int i = 0; i < fileResIds.length; i++) {
                in = context.getResources().openRawResource(fileResIds[i]);
                filePath = path + File.separator + fileNames[i] + ".udb";
                out = new FileOutputStream(filePath);
                buff = new byte[1024];
                try {
                    while ((read = in.read(buff)) > 0) {
                        out.write(buff, 0, read);
                    }
                } finally {
                    in.close();
                    out.close();
                }
            }

            File storeDirectory = new File(path);
            File[] fromStoreFiles = storeDirectory.listFiles();
            File toFile;
            String fromFileName, toFileName, toFilePath;
            for (File fromFile : fromStoreFiles) {
                fromFileName = fromFile.getName();
                toFileName = fromFileName.replace("_rq", ".rq");
                toFilePath = path + File.separator + toFileName;
                toFile = new File(toFilePath);
                fromFile.renameTo(toFile);
            }
            result = true;
        } catch (Exception e) {
            WriteLog(Common.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    //Copy form relevant js and css files to sd card
    public static boolean copyAssetsToSDCard(final Context ctx) {
        boolean result = false;
        try {
            String appPath = Environment.getExternalStorageDirectory() + File.separator + ZAppSettings.App_Name;
            String formsPath = appPath + File.separator + "forms";
            ZAppSettings.HTML_FOMRS_PATH = formsPath;
            String jsPath = formsPath + File.separator + "js";
            String cssPath = formsPath + File.separator + "css";
            String[] filePaths = new String[]{jsPath, cssPath};
            String[] cssFileNames = new String[]{"formhub.css", "grid.css"};
            String[] jsFileNames = new String[]{"enketo-bundle.js", "enketo-bundle-original.js", "jquery.min.js"};
            InputStream in;
            FileOutputStream out;
            byte[] buff;
            int read;
            String filePath;
            File storesDirectory = new File(appPath, "forms");
            if (!storesDirectory.exists())
                storesDirectory.mkdirs();
            for (String filePath1 : filePaths) {
                storesDirectory = new File(filePath1);
                if (!storesDirectory.exists())
                    storesDirectory.mkdir();
            }
            for (String cssFile : cssFileNames) {
                in = ctx.getResources().getAssets().open("css/" + cssFile);
                filePath = cssPath + File.separator + cssFile;
                out = new FileOutputStream(filePath);
                buff = new byte[1024];
                try {
                    while ((read = in.read(buff)) > 0) {
                        out.write(buff, 0, read);
                    }
                } finally {
                    in.close();
                    out.close();
                }
            }
            for (String jsFile : jsFileNames) {
                in = ctx.getResources().getAssets().open("js/" + jsFile);
                filePath = jsPath + File.separator + jsFile;
                out = new FileOutputStream(filePath);
                buff = new byte[1024];
                try {
                    while ((read = in.read(buff)) > 0) {
                        out.write(buff, 0, read);
                    }
                } finally {
                    in.close();
                    out.close();
                }
            }
            result = true;
        } catch (Exception e) {
            WriteLog(Common.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public interface TransmitProgressCallback {
        void update(String text);

        void errorCallback(ResponseObject response);

        void onSuccess(ResponseObject response);

        void noNetworkError();
    }

}
