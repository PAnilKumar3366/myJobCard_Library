package com.ods.myjobcard_library.viewmodels.workorder;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.attachment.Connection;
import com.ods.myjobcard_library.entities.attachment.UploadAttachmentContent;
import com.ods.myjobcard_library.entities.attachment.WorkOrderAttachment;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.myjobcard_library.utils.DocsUtil;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class DocsViewModel extends AndroidViewModel {
    private static final String TAG = "DocsViewModel";
    ResponseObject result;
    ArrayList<UploadAttachmentContent> uploadList;
    ArrayList<WorkOrderAttachment> downloadLists;
    private MutableLiveData<ArrayList<WorkOrderAttachment>> mWoAttachmentLiveData = new MutableLiveData<ArrayList<WorkOrderAttachment>>();
    private MutableLiveData<ArrayList<UploadAttachmentContent>> mWoUploadAttachmentLiveData = new MutableLiveData<ArrayList<UploadAttachmentContent>>();

    public DocsViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<WorkOrderAttachment>> getmWoAttachmentLiveData() {
        return mWoAttachmentLiveData;
    }

    public void setmWoAttachmentLiveData(String objectKey, String className) {

        try {
            result = WorkOrderAttachment.getAttachments(objectKey, ZAppSettings.Screens.WorkOrder.getClassName(), false, className);

            if (result != null && !result.isError()) {
                downloadLists = (ArrayList<WorkOrderAttachment>) result.Content();
                if (downloadLists.size() > 0 && !downloadLists.isEmpty()) {
                    mWoAttachmentLiveData.setValue(downloadLists);
                } else {
                    mWoAttachmentLiveData.setValue(downloadLists);
                }
                result = null;
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public MutableLiveData<ArrayList<UploadAttachmentContent>> getmWoUploadAttachmentLiveData() {
        return mWoUploadAttachmentLiveData;
    }

    public void setmWoUploadAttachmentLiveData(WorkOrder workOrder) {
        uploadList = new ArrayList<>();
        result = UploadAttachmentContent.getUploadedAttachments(workOrder.getWorkOrderNum());
        if (result != null && !result.isError()) {
            uploadList = (ArrayList<UploadAttachmentContent>) result.Content();
            mWoUploadAttachmentLiveData.setValue(uploadList);
            // checkUploadAttachments(uploadList);
        }
    }

    private void checkUploadAttachments(ArrayList<UploadAttachmentContent> uploadLists) {
        if (uploadLists != null && uploadLists.size() > 0) {
            ArrayList<UploadAttachmentContent> uploaded = new ArrayList<>();
            if (downloadLists != null && downloadLists.size() > 0) {
                ArrayList<UploadAttachmentContent> duplicates = null;
                for (UploadAttachmentContent upload : uploadLists) {
                    if (Connection.uploadedFileExistsInDownloads(upload.getFILE_NAME(), upload.getDescription(), downloadLists)) {
                        if (duplicates == null)
                            duplicates = new ArrayList<>();
                        duplicates.add(upload);
                    } else {
                        uploaded.add(upload);
                    }
                }
                uploadLists = uploaded;
                if (duplicates != null && duplicates.size() > 0) {
                    DocsUtil.deleteDuplicatesInBackground(duplicates, null);
                }


            }
        }
        mWoUploadAttachmentLiveData.setValue(uploadLists);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
    }
}
