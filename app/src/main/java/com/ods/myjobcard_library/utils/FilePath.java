package com.ods.myjobcard_library.utils;

/**
 * Created by LENOVO on 15-07-2016.
 */

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.ods_sdk.utils.DliteLogger;

import java.io.File;

@TargetApi(19)
public class FilePath {

    /**
     * Method for return file path of Gallery image/ Document / Video / Audio
     *
     * @param context Activity Context.
     * @param uri     URI object.
     * @return path of the selected image file from gallery
     */
    public static String getPath(final Context context, final Uri uri) {

        try {
            // check here to KITKAT or new version
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return ZAppSettings.ExternalDirectoryPath + "/"
                                + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    String fileName = getFilePath(context, uri);
                    if (fileName != null) {
                        return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                    }

                    String id = DocumentsContract.getDocumentId(uri);
                    if (id.startsWith("raw:")) {
                        id = id.replaceFirst("raw:", "");
                        File file = new File(id);
                        if (file.exists())
                            return id;
                    }
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                    /*final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri;

                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                        contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"),
                                Long.valueOf(id));
                    String[] contentUriPrefixesToTry = new String[]{
                            "content://downloads/public_downloads",
                            "content://downloads/my_downloads",
                            "content://downloads/all_downloads"
                    };
                    for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri1 = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    try {
                        String path = getDataColumn(context, contentUri1, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception e) {
                        DliteLogger.WriteLog(FilePath.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                    }
                }
                    return getDataColumn(context, contentUri, null, null);*/
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    } else if ("document".equalsIgnoreCase(type)) {
                        contentUri = MediaStore.Files.getContentUri("external");
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
                        }
                        return getPDFPath(uri,context);*/
                        //contentUri=uri;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};

                    return getDataColumn(context, contentUri, selection,
                            selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(FilePath.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return null;
    }

    public static String getFilePath(Context context, Uri uri) {

        Cursor cursor = null;
        final String[] projection = {
                MediaStore.MediaColumns.DISPLAY_NAME
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, null, null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                return cursor.getString(index);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(FilePath.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }
    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(FilePath.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        try {
            return "com.android.externalstorage.documents".equals(uri
                    .getAuthority());
        } catch (Exception e) {
            DliteLogger.WriteLog(FilePath.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return false;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        try {
            return "com.android.providers.downloads.documents".equals(uri
                    .getAuthority());
        } catch (Exception e) {
            DliteLogger.WriteLog(FilePath.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return false;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        try {
            return "com.android.providers.media.documents".equals(uri
                    .getAuthority());
        } catch (Exception e) {
            DliteLogger.WriteLog(FilePath.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return false;
    }
    /**/
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        try {
            return "com.google.android.apps.photos.content".equals(uri
                    .getAuthority());
        } catch (Exception e) {
            DliteLogger.WriteLog(FilePath.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return false;
    }
}
