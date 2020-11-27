package com.ods.myjobcard_library.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Patterns;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.ods.myjobcard_library.R;
import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A utility that downloads a file from a URL.
 *
 * @author www.codejava.net
 */
public class DownloadAttachment {
    private static final int BUFFER_SIZE = 4096;
    private static NotificationManager mNotifyManager;
    private static NotificationCompat.Builder mBuilder;
    private static Notification.Builder notificationBuilder;
    /*public static void downloadInBackground(final String url) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String msg = "";
                try {
                    //String encodedURL = url; //java.net.URLEncoder.encode(url,"UTF-8");
                    downloadFile(url);
                }catch(Exception e)
                { DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error,e.getMessage());}
                return msg;
            }
            @Override
            protected void onPostExecute(String msg) {

            }
        }.execute(url, null, null);
    }*/

    public static void downloadInBackground(final String url, final String fileName, final String MimeType, final boolean isURL, final Context ctx, final String entitySetName) {

        new AsyncTask<String, Integer, String>() {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                if (!isURL) {
                    String channelId = "AttachmentChannelId";
                    mNotifyManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
                    Toast.makeText(ctx, "Downloading " + fileName, Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= 26) {
                        CharSequence name = "Upload / Download Attachments";
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel mChannel = new NotificationChannel(channelId, name, importance);
                        notificationBuilder = new Notification.Builder(ctx, channelId)
                                .setContentTitle("File Download")
                                .setContentText("Download in progress")
                                .setSmallIcon(android.R.drawable.stat_sys_download)
                                .setChannelId(channelId)
                                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.app_icon))
                                .setAutoCancel(true)
                                .setTicker("Downloading file " + fileName + "");
                        mNotifyManager.createNotificationChannel(mChannel);
                        mNotifyManager.notify(0, notificationBuilder.build());
                    } else {
                        mBuilder = new NotificationCompat.Builder(ctx);
                        mBuilder.setContentTitle("File Download")
                                .setContentText("Download in progress")
                                .setSmallIcon(android.R.drawable.stat_sys_download)
                                .setTicker("Downloading file " + fileName + "")
                                .setAutoCancel(true)
                                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.app_icon));
                        mNotifyManager.notify(0, mBuilder.build());
                    }
                } else {
                    progressDialog = ProgressDialog.show(ctx, "", "downloading...", true);
                }
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                if (!isURL) {
                    // Displays the progress bar on notification
                    if (Build.VERSION.SDK_INT >= 26) {
                        notificationBuilder.setProgress(100, values[0], false);
                        mNotifyManager.notify(0, notificationBuilder.build());
                    } else {
                        mBuilder.setProgress(100, values[0], false);
                        mNotifyManager.notify(0, mBuilder.build());
                    }
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String msg = "";
                try {
                    //String encodedURL = url; //java.net.URLEncoder.encode(url,"UTF-8");
                    return downloadFile(url, fileName, MimeType, isURL, ctx, entitySetName);
                } catch (Exception e) {
                    DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String urlStr) {
                if (urlStr.contains("Error") || urlStr.contains("FileSizeUnavailable")) {
                    Toast.makeText(ctx, (urlStr.contains("FileSizeUnavailable") ? ctx.getString(R.string.alertDownloadSizeZeroErrorMsg) : ctx.getString(R.string.alertDownloadFailedErrorMsg, urlStr.substring(urlStr.indexOf(":") + 1))), Toast.LENGTH_LONG).show();
                    //DialogsUtility.showAlertPopup(ctx, ctx.getString(R.string.errorTitle), (urlStr.contains("FileSizeUnavailable") ? ctx.getString(R.string.alertDownloadSizeZeroErrorMsg) : ctx.getString(R.string.alertDownloadFailedErrorMsg, urlStr.substring(urlStr.indexOf(":") + 1))));
                    if (Build.VERSION.SDK_INT >= 26) {
                        notificationBuilder.setContentText("File download has failed");
                        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_warning);
                        // Removes the progress bar
                        notificationBuilder.setProgress(0, 0, false);
                        mNotifyManager.notify(0, notificationBuilder.build());
                    } else {
                        mBuilder.setContentText("File download has failed");
                        mBuilder.setSmallIcon(android.R.drawable.stat_sys_warning);
                        // Removes the progress bar
                        mBuilder.setProgress(0, 0, false);
                        mNotifyManager.notify(0, mBuilder.build());
                    }
                    return;
                }
                if (!isURL) {
                    Toast.makeText(ctx, "Download Completed!", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= 26) {
                        notificationBuilder.setContentText("File is downloaded");
                        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
                        // Removes the progress bar
                        notificationBuilder.setProgress(0, 0, false);
                        notificationBuilder.setStyle(new Notification.BigTextStyle().bigText("Please click on file again to view."));
                        mNotifyManager.notify(0, notificationBuilder.build());
                    } else {
                        mBuilder.setContentText("File is downloaded");
                        mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
                        // Removes the progress bar
                        mBuilder.setProgress(0, 0, false);
                        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText("Please click on file again to view."));
                        mNotifyManager.notify(0, mBuilder.build());
                    }
                } else {
                    try {
                        progressDialog.dismiss();
                        if (!urlStr.isEmpty() && Patterns.WEB_URL.matcher(urlStr).matches()) {
                            ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlStr)));
                        } else {
                            File f = new File(urlStr);
                            if (f.exists()) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                String mimeType = DocsUtil.getMimeTypeFromFile(f);
                                if (mimeType.isEmpty())
                                    mimeType = MimeType;
                                intent.setDataAndType(Uri.fromFile(f), mimeType);
                                ctx.startActivity(intent);
                            } else {
                                Toast.makeText(ctx, ctx.getString(R.string.msgNoFileFound), Toast.LENGTH_SHORT).show();
                                //DialogsUtility.showAlertPopup(ctx, ctx.getString(R.string.alertTitle), ctx.getString(R.string.msgNoFileFound));
                            }
                        }
                    } catch (Exception e) {
                        if (e instanceof ActivityNotFoundException)
                            Toast.makeText(ctx, ctx.getString(R.string.msgNoApplicationFound), Toast.LENGTH_SHORT).show();
                        //DialogsUtility.showAlertPopup(ctx, ctx.getString(R.string.errorTitle), ctx.getString(R.string.msgNoApplicationFound));
                    }
                }
            }
        }.execute(url, fileName, MimeType, null, null);
    }

    /**
     * Downloads a file from a URL
     *
     * @param fileURL HTTP URL of the file to be downloaded
     * @throws IOException
     */
    public static String downloadFile(String fileURL, String fileName, String mime, boolean isURL, Context ctx, String entitySetName)
            throws IOException {
        String saveDir, saveFilePath, uid = null, pwd = null, attachURL = "";
        InputStream inputStream;
        FileOutputStream outputStream;
        int bytesRead;
        byte[] buffer;
        File direct, attachmentDirectory, actualFile;
        URL url;
        HttpURLConnection httpConn;
        try {

                /*uid = ZConfigManager.DEFAULT_ASSIGNMENT_TYPE.equalsIgnoreCase(ZAppSettings.AssignmentType.WorkCenterSingleIdLevel.getAssignmentTypeText()) ? ZAppSettings.strPrimaryUser : ZAppSettings.strUser;
                pwd = ZAppSettings.strPswd;
                if (uid != null && pwd != null) {*/

                    /*url = new URL(fileURL);
                    httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setReadTimeout(60 * 1000);
                    httpConn.setConnectTimeout(60 * 1000);
                    String authorization = uid + ":" + pwd;
                    DliteLogger.WriteLog(DownloadAttachment.class, ZAppSettings.LogLevel.Debug,"Download Attachment Url:"+fileURL);
                    //String authorization="mkanungo:maddy5294";
                    String encodedAuth = "Basic " + Base64.encodeToString(authorization.getBytes(), Base64.DEFAULT);
                    httpConn.setRequestProperty("Authorization", encodedAuth);

                    int responseCode = httpConn.getResponseCode();*/
            int responseCode = 0;

            ResponseObject result = DataHelper.getInstance().downloadMedia(entitySetName, fileURL);

            // always check HTTP response code first
            //if (responseCode == HttpURLConnection.HTTP_OK) {
            if (result != null && !result.isError()) {

                // opens input stream from the HTTP connection
                //inputStream = httpConn.getInputStream();
                inputStream = new ByteArrayInputStream(DocsUtil.hexStringToByteArray(String.valueOf(result.Content())));

                if (!isURL) {
            /*String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();*/
                    saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + ZConfigManager.AttachmentsFolder).getAbsolutePath();
                    saveFilePath = saveDir + File.separator + fileName;
                    actualFile = new File(saveFilePath);
                    if (!actualFile.exists()) {
                        //"/Downloads";
                        direct = new File(saveDir);
                        if (!direct.exists()) {
                            attachmentDirectory = new File(saveDir);
                            attachmentDirectory.mkdirs();
                        }

                        // opens an output stream to save into file
                        outputStream = new FileOutputStream(saveFilePath);

                        bytesRead = -1;
                        buffer = new byte[BUFFER_SIZE];
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }

                        outputStream.close();
                        File downloadedFile = new File(saveFilePath);
                        if (downloadedFile.exists() && downloadedFile.length() == 0) {
                            attachURL = "FileSizeUnavailable";
                        }
                        System.out.println("File downloaded");
                    }
                } else {

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            inputStream));
                    StringBuffer sb = new StringBuffer();
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    String urlPath = "";
                    urlPath = result.Content().toString();
                    //String json=sb.toString();
                            /*JSONObject jsonObj = new JSONObject(json);
                            jsonObj = jsonObj.getJSONObject("d");
                            */
                    //String urlStr = jsonObj.getString("Line");
                    if (!urlPath.isEmpty() && urlPath.contains("KEY&")) {
                        attachURL = urlPath.split("KEY&")[1];
                    }
                    br.close();
                }
                inputStream.close();
            } else {
                attachURL = "Error" + ":" + responseCode;
            }
            //httpConn.disconnect();

            /*}*/
        } catch (Exception e) {
            attachURL = "Error" + ":Exception Occurred";
            DliteLogger.WriteLog(DownloadAttachment.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return attachURL;
    }
}
