package com.ods.myjobcard_library.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.ods.myjobcard_library.R;
import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.attachment.UploadAttachmentContent;
import com.ods.myjobcard_library.entities.attachment.UploadNotificationAttachmentContent;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 04-08-2016.
 */
public class DocsUtil {

    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 201;
    public static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 202;
    public static final int PICK_IMAGE_REQUEST = 203;
    public static final int PICK_DOC_REQUEST = 204;

    public static final int MEDIA_TYPE_DOCS = 0;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_SIGN_IMG = 3;
    public static final int EDIT_IMAGE = 4;
    public static final String IMAGE_DIRECTORY_NAME = "ODS_File_Upload";
    private static final int PICK_EDIT_IMAGE = 5;
    public static Uri fileUri; // file url to store image/video
    public static String filePath = null;
    private static String capturedImageTitle;

    public static Intent captureImage(Context context) {
        Intent intent = null;
        try {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, context);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
//        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intent;
    }

    public static Intent recordVideo() {
        Intent intent = null;
        try {
            intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            //fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

            // set video quality
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, .4);
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
            // name

            // start the video capture Intent
//            startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intent;
    }

    public static Intent showFileChooser(boolean image) {

/*
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
*/
        Intent intent = null;
        String fileTypes;
        String strMsg;
        int intRequestType;
        try {
            intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (image) {
                fileTypes = "image/* video/*";
                strMsg = "Select Picture";
                intRequestType = PICK_IMAGE_REQUEST;
                //intent.setAction(Intent.ACTION_GET_CONTENT);
            } else {
                fileTypes = "*/*";
                strMsg = "Choose File";
                intRequestType = PICK_DOC_REQUEST;
                //intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            }
            intent.setType(fileTypes);
            //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            startActivityForResult(Intent.createChooser(intent, strMsg), intRequestType);
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intent;
    }

    private static Intent getIntentForFinalUpload(boolean isImage, boolean isGallery) {
        Intent i = null;
        try {
            i = new Intent();
            //Intent i = new Intent(UploadAttachment.this, UploadActivity.class);
            if (isGallery) {
                i.putExtra("filePath", filePath);
                i.putExtra("fileType", isImage ? MEDIA_TYPE_IMAGE : MEDIA_TYPE_DOCS);
            } else {
                i.putExtra("filePath", filePath);
                i.putExtra("fileType", isImage ? MEDIA_TYPE_IMAGE : MEDIA_TYPE_VIDEO);
            }
//            startActivity(i);
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return i;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage, String title) {
        Uri uri = null;
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, title, null);
            uri = Uri.parse(path);
            uri = Uri.parse(getRealPathFromURI(inContext, uri));
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return uri;
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        String path = "";
        try {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                //cursor.moveToFirst();
                int idx = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return path;
    }

    public static Intent getIntentForFinalUpload(int requestCode, int resultCode, Intent data, int reqSuccessCode, int reqCancelledCode, Context context) {
        Intent intent = null;
        try {
            if (resultCode == reqSuccessCode) {

                if (data != null && requestCode != CAMERA_CAPTURE_IMAGE_REQUEST_CODE && requestCode != EDIT_IMAGE) {
                    fileUri = data.getData();
                    if (fileUri != null)
                        filePath = FilePath.getPath(context, fileUri);

                    String picturePath = FilePath.getPath(context, fileUri);
                    fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(picturePath));

                    //filePath = fileUri.toString();
                }

                /*if (filePath == null && fileUri != null)
                    filePath = FilePath.getPath(context, fileUri);*/

                if (filePath != null && !filePath.isEmpty()) {
                    File file = new File(filePath);
                    if (ZCommon.convertBytesToMB(file.length()) > ZConfigManager.MAX_UPLOAD_FILE_SIZE) {
                        //DialogsUtility.showAlertPopup(context, context.getString(R.string.alertTitle), context.getString(R.string.alertUploadFileSizeAboveLimit, String.valueOf(ZConfigManager.MAX_UPLOAD_FILE_SIZE)));
                        Toast.makeText(context, context.getString(R.string.alertUploadFileSizeAboveLimit, String.valueOf(ZConfigManager.MAX_UPLOAD_FILE_SIZE)), Toast.LENGTH_LONG).show();
                        return null;
                    }
                }

                if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE || requestCode == EDIT_IMAGE) {
                    if (Build.VERSION.SDK_INT < 24) {
                        if (fileUri != null) {
                        /*Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        fileUri = getImageUri(context, imageBitmap, capturedImageTitle);*/
                            //filePath = getRealPathFromURI(context, fileUri);
                            filePath = FilePath.getPath(context, fileUri);
                        }
                    }

                    if (filePath != null)
                        intent = getIntentForFinalUpload(true, false);

                } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
                    // video successfully recorded
                    // launching upload activity
                    intent = getIntentForFinalUpload(false, false);
                }

                if (requestCode == PICK_IMAGE_REQUEST) {
                    try {
                /*String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Uri selectedImage = data.getData();
                Cursor cursor = context.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();*/

                        intent = getIntentForFinalUpload(true, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (requestCode == PICK_DOC_REQUEST && resultCode == reqSuccessCode && data != null && data.getData() != null) {
                    try {
                /*if(data == null){
                    //no data present
                    return null;
                }*/

                /*fileUri = data.getData();
                filePath = FilePath.getPath(context, fileUri);*/
                        if (Build.VERSION.SDK_INT >= 24 && (filePath == null || filePath.isEmpty())) {
                            filePath = getRealPathFromURI(context, fileUri);
                        }

                        if (filePath != null && !filePath.equals("")) {
                            intent = getIntentForFinalUpload(false, true);
                        } else {
                            Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } /*else if (resultCode == reqCancelledCode) {

                // user cancelled Image capture
                Toast.makeText(context,
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            }*/ else {
                // failed to get the file
                //Toast.makeText(context,"Sorry! Failed to get the file", Toast.LENGTH_SHORT).show();
                if (resultCode == reqCancelledCode) {
                    if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

                        Toast.makeText(context,
                                "User cancelled image capture", Toast.LENGTH_SHORT)
                                .show();

                    } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
                        Toast.makeText(context,
                                "User cancelled video capture", Toast.LENGTH_SHORT)
                                .show();
                    } else if (requestCode == PICK_IMAGE_REQUEST) {
                        Toast.makeText(context,
                                "User cancelled image pick", Toast.LENGTH_SHORT)
                                .show();
                    } else if (resultCode == PICK_DOC_REQUEST) {

                        // user cancelled Image capture
                        Toast.makeText(context,
                                "User cancelled document pick", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(context, "Sorry! Failed to get the file", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intent;
    }

    //Edit Image
    public static Intent editImage(Uri fileUri, Context context) {
        Intent editIntent = null;
        try {
            editIntent = new Intent(Intent.ACTION_EDIT);
            editIntent.setDataAndType(fileUri, "image/*");
            editIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            editIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(editIntent, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return editIntent;
    }

    private static Uri getOutputMediaFileUri(int type, Context context) {
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                File file = getOutputMediaFile(type, context);
                if (file != null) {
                    Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                    return fileUri;
                }
            }
            return Uri.fromFile(getOutputMediaFile(type, context));
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return null;
    }

    public static File getOutputMediaFile(int type, Context context) {
        try {
            // External sdcard location
            String TAG = DocsUtil.class.getSimpleName();
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+context.getCacheDir());
//            mediaStorageDir.mkdirs();

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            File mediaFile;

            if (type == MEDIA_TYPE_IMAGE) {
                capturedImageTitle = "IMG_" + timeStamp + ".jpg";
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + capturedImageTitle);
                //mediaFile = File.createTempFile(capturedImageTitle, ".jpg", mediaStorageDir);
                filePath = mediaFile.getAbsolutePath();
            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return null;
    }

    public static File getPhotoFileUri(Context context) {
        File file = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            capturedImageTitle = "IMG_" + timeStamp;
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = context.getCacheDir();

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            //file = new File(mediaStorageDir.getPath() + File.separator + capturedImageTitle);
            file = File.createTempFile(
                    capturedImageTitle,  /* prefix */
                    ".jpg",         /* suffix */
                    mediaStorageDir      /* directory */
            );
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

        return file;
    }

    public static Intent onLaunchCamera(Context context) {
        Intent intent = null;
        try {
            // create Intent to take a picture and return control to the calling application
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Create a File reference to access to future access
            File photoFile = getPhotoFileUri(context);
            filePath = photoFile.getAbsolutePath();
            // wrap File object into a content provider
            // required for API >= 24
            Uri fileProvider = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
        /*if (intent.resolveActivity(context.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }*/
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return intent;
    }

    public static String[] createGeodatabaseFilePath(int count) {
        String[] filePaths = new String[count];
        try {
            for (int i = 0; i < count; i++) {
                filePaths[i] =
                        Environment
                                .getExternalStorageDirectory() + "/ArcGIS/ODS/geoDB/ODS_GeoDB_" + i + ".geodatabase";
            }
            return filePaths;
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return filePaths;
    }

    public static String createBasemapFilePath() {
        String filePath = "";
        try {
            return Environment.getExternalStorageDirectory().getPath() + "/ArcGIS/samples/tiledcache/";
            /*if(gdbFile.exists()) {
                gdbFile = new File(gdbFile.getPath() + File.separator
                        + "basemap_assets.tpk");
            }*/

//            return gdbFile.getPath();
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return filePath;
    }

    public static String convertStringToBase64(String content) {

        ByteArrayOutputStream output = null;
        try {
            InputStream in = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));
            byte[] buffer = new byte[8192];
            int bytesRead;
            output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, 0);
            try {
                while ((bytesRead = in.read(buffer)) != -1) {
                    output64.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
            }
            output64.close();
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

        return output != null ? output.toString() : "";
    }

    public static String convertToBase64(String filePath) {

        ByteArrayOutputStream output = null;
        try {
            FileInputStream in = new FileInputStream(filePath);
            byte[] buffer = new byte[8192];
            int bytesRead;
            output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, 0);
            try {
                while ((bytesRead = in.read(buffer)) != -1) {
                    output64.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
            }
            output64.close();
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

        return output != null ? output.toString() : "";
    }

    public static ByteArrayOutputStream convertToBase64(String filePath, boolean isImage) {

        ByteArrayOutputStream output = null;
        FileInputStream in;
        byte[] buffer;
        int bytesRead;
        Base64OutputStream output64;
        File imageFile;
        Bitmap bitmap;
        try {
            in = new FileInputStream(filePath);
            buffer = new byte[8192];
            output = new ByteArrayOutputStream();
            output64 = new Base64OutputStream(output, 0);
            if (isImage) {
                /*if(!isCompressed)
                {
                    BitMap.compress(Bitmap.CompressFormat.JPEG, ZAppSettings.Bitmap_Compression_Quality, output64);
                    output64.flush();
                    *//*output64 = output64Stream;
                    output64 = new Base64OutputStream(output, 0);
                    //output64Stream.flush();
                    output64.flush();*//*
                }
                else {*/
                //write the compresssion logic here
                imageFile = new File(filePath);
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, ZConfigManager.Bitmap_Compression_Quality, output64);
                output64.flush();
                /*}*/
            } else {
                try {
                    while ((bytesRead = in.read(buffer)) != -1) {
                        output64.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
                }
            }
            output64.close();
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

        return output;
    }

    public static String convertToBase64(Bitmap bMap) {
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, 0);
            bMap.compress(Bitmap.CompressFormat.JPEG, ZConfigManager.Bitmap_Compression_Quality, output64);
            output64.flush();
            output64.close();
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return output != null ? output.toString() : "";
    }

    public static void deleteDuplicatesInBackground(final ArrayList<UploadAttachmentContent> woDuplicateDocs, final ArrayList<UploadNotificationAttachmentContent> notiDuplicateDocs) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                int count = 0;
                if (woDuplicateDocs != null && woDuplicateDocs.size() > 0) {
                    for (UploadAttachmentContent doc : woDuplicateDocs) {
                        count++;
                        doc.setMode(ZAppSettings.EntityMode.Delete);
                        doc.SaveToStore(count == woDuplicateDocs.size());
                    }
                } else if (notiDuplicateDocs != null && notiDuplicateDocs.size() > 0) {
                    for (UploadNotificationAttachmentContent doc : notiDuplicateDocs) {
                        count++;
                        doc.setMode(ZAppSettings.EntityMode.Delete);
                        doc.SaveToStore(count == notiDuplicateDocs.size());
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //baseFragment.onTabAlertCountChange();
            }
        }.execute();
    }

    public static String getMimeTypeFromFile(File file) {
        String mimeType = "";
        try {
            String filePathEncoded = Uri.fromFile(file).getEncodedPath();
            String extension = MimeTypeMap.getFileExtensionFromUrl(filePathEncoded);
            if (extension != null) {
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getLocalizedMessage());
        }
        return mimeType == null ? "" : mimeType;
    }

    /**
     * Extract the MIME type from a base64 string
     *
     * @param encoded Base64 string
     * @return MIME type string
     */
    public static String extractMimeType(final String encoded) {
        final Pattern mime = Pattern.compile("^data:([a-zA-Z0-9]+/[a-zA-Z0-9]+).*,.*");
        final Matcher matcher = mime.matcher(encoded);
        if (!matcher.find())
            return "";
        return matcher.group(1).toLowerCase();
    }

    public static byte[] hexStringToByteArray(String s) {
        byte[] b = null;
        try {
            b = new byte[s.length() / 2];
            for (int i = 0; i < b.length; i++) {
                int index = i * 2;
                int v = Integer.parseInt(s.substring(index, index + 2), 16);
                b[i] = (byte) v;
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getLocalizedMessage());
        }
        return b;
    }

    public static void RemoveUnRequiredUploadEntities() {
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);
        try {
            String entitySetName = ZCollections.WO_ATTACHMENT_CONTENT_UPLOAD_COLLECTION;
            String attResPath = entitySetName + "?$filter=BINARY_FLG ne 'N'";
            result = DataHelper.getInstance().getEntities(entitySetName, attResPath);
            if (!result.isError()) {
                List<ODataEntity> uploadedEntities = (List<ODataEntity>) result.Content();
                UploadAttachmentContent uploadAttachment;
                for (ODataEntity entity : uploadedEntities) {
                    uploadAttachment = new UploadAttachmentContent(entity, ZAppSettings.FetchLevel.Header);
                    uploadAttachment.setBINARY_FLG("N");
                    uploadAttachment.setMode(ZAppSettings.EntityMode.Update);
                    entitySetName = ZCollections.WO_ATTACHMENT_COLLECTION;
                    String resPath = entitySetName + "/$count?$filter=endswith(ObjectKey,'" + uploadAttachment.getWorkOrderNum() + "') eq true and " +
                            "(tolower(Extension) eq 'url' or tolower(CompID) eq '" + uploadAttachment.getFILE_NAME().toLowerCase() + "') and tolower(PropValue) eq '" + uploadAttachment.getDescription().toLowerCase() + "'";
                    result = DataHelper.getInstance().getEntities(entitySetName, resPath);
                    if (!result.isError()) {
                        int attachmentCount = Integer.parseInt(String.valueOf(result.Content()));
                        if (attachmentCount > 0) {
                            uploadAttachment.SaveToStore(false);
                        }
                    }
                }
            }
            entitySetName = ZCollections.NO_ATTACHMENT_CONTENT_UPLOAD_COLLECTION;
            attResPath = entitySetName + "?$filter=BINARY_FLG ne 'N'";
            result = DataHelper.getInstance().getEntities(entitySetName, attResPath);
            if (!result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                UploadNotificationAttachmentContent uploadAttachment;
                for (ODataEntity entity : entities) {
                    uploadAttachment = new UploadNotificationAttachmentContent(entity, ZAppSettings.FetchLevel.Header);
                    uploadAttachment.setBINARY_FLG("N");
                    uploadAttachment.setMode(ZAppSettings.EntityMode.Update);
                    entitySetName = ZCollections.NO_ATTACHMENT_COLLECTION;
                    String resPath = entitySetName + "/$count?$filter=(ObjectKey eq '" + uploadAttachment.getNotification() + "' or ObjectKey eq '" + uploadAttachment.getNotification() + uploadAttachment.getItem() + uploadAttachment.getTask() + "')  and " +
                            "(tolower(Extension) eq 'url' or tolower(CompID) eq '" + uploadAttachment.getFILE_NAME().toLowerCase() + "') and tolower(PropValue) eq '" + uploadAttachment.getDescription().toLowerCase() + "'";
                    result = DataHelper.getInstance().getEntities(entitySetName, resPath);
                    if (!result.isError()) {
                        int attachmentCount = Integer.parseInt(String.valueOf(result.Content()));
                        if (attachmentCount > 0) {
                            uploadAttachment.SaveToStore(false);
                        }
                    }
                }
                for (ODataEntity entity : entities) {
                    uploadAttachment = new UploadNotificationAttachmentContent(entity, ZAppSettings.FetchLevel.Header);
                    uploadAttachment.setBINARY_FLG("N");
                    uploadAttachment.setMode(ZAppSettings.EntityMode.Update);
                    entitySetName = ZCollections.WO_NO_ATTACHMENT_COLLECTION;
                    String resPath = entitySetName + "/$count?$filter=endswith(ObjectKey,'" + uploadAttachment.getNotification() + "') eq true and " +
                            "(tolower(Extension) eq 'url' or tolower(CompID) eq '" + uploadAttachment.getFILE_NAME().toLowerCase() + "') and tolower(PropValue) eq '" + uploadAttachment.getDescription().toLowerCase() + "'";
                    result = DataHelper.getInstance().getEntities(entitySetName, resPath);
                    if (!result.isError()) {
                        int attachmentCount = Integer.parseInt(String.valueOf(result.Content()));
                        if (attachmentCount > 0) {
                            uploadAttachment.SaveToStore(false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(DocsUtil.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    /**
     * @param filePath Path of the image to be compressed
     * @return Resized Base64 string
     */
    public static String resizeFileContent(String filePath){
        Bitmap scaledBitmap = null;
        String resizedBase64 = "";
        File file = new File(filePath);
        byte[] b = new byte[(int) file.length()];
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
            scaledBitmap = getDownScaledBitmap(b);
            if(scaledBitmap != null) {
                byte[] bo = compressBitmap(scaledBitmap);
                if(bo != null)
                    resizedBase64 = Base64.encodeToString(bo, Base64.NO_WRAP);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resizedBase64;
    }

    /**
     * @param base64image Base64 string of an image to be compressed
     * @return Resized Base64 string
     */
    public static String resizeBase64Content(String base64image) {
        Bitmap scaledBitmap = null;
        String resizedBase64 = "";
        byte[] encodeByte = Base64.decode(base64image.getBytes(), Base64.DEFAULT);
        scaledBitmap = getDownScaledBitmap(encodeByte);
        if(scaledBitmap != null) {
            byte[] b = compressBitmap(scaledBitmap);
            if(b != null)
                resizedBase64 = Base64.encodeToString(b, Base64.NO_WRAP);
        }
        return resizedBase64;

    }

    private static byte[] compressBitmap(Bitmap bitmap){
        byte[] bo = null;
        if(bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, ZConfigManager.Bitmap_Compression_Quality, baos);
            bo = baos.toByteArray();
            System.gc();
        }
        return bo;
    }

    private static Bitmap getDownScaledBitmap(byte[] decodedByteArray){
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap image = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        //      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap
            image = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }

        try {
            scaledBitmap = Bitmap.createScaledBitmap(image, actualWidth, actualHeight, false);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        /*float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(new ByteArrayInputStream(encodeByte));

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*if(image.getHeight() <= 400 && image.getWidth() <= 400){
            return base64image;
        }
        image = Bitmap.createScaledBitmap(image, actualWidth, actualHeight, false);*/
        return scaledBitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}
