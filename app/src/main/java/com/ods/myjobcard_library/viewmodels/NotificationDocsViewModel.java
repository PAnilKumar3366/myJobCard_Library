package com.ods.myjobcard_library.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.attachment.UploadNotificationAttachmentContent;
import com.ods.myjobcard_library.entities.attachment.WorkOrderAttachment;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class NotificationDocsViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<WorkOrderAttachment>> mAttachements = new MutableLiveData<>();
    private MutableLiveData<ArrayList<UploadNotificationAttachmentContent>> uploadList = new MutableLiveData<>();
    private ArrayList<WorkOrderAttachment> downloadLists = new ArrayList<>();
    private ArrayList<UploadNotificationAttachmentContent> uploadLists = new ArrayList<>();

    public NotificationDocsViewModel(@NonNull Application application) {
        super(application);
    }

    public void setAttachements(String notification, boolean isWONotif, String className) {
        ResponseObject result = WorkOrderAttachment.getAttachments(notification, ZAppSettings.Screens.Notification.getClassName(), isWONotif, className);
        if (downloadLists.size() > 0)
            downloadLists.clear();
        if (!result.isError())
            downloadLists = (ArrayList<WorkOrderAttachment>) result.Content();
        mAttachements.setValue(downloadLists);
    }

    public MutableLiveData<ArrayList<WorkOrderAttachment>> getAttachements() {
        return mAttachements;
    }

    public MutableLiveData<ArrayList<UploadNotificationAttachmentContent>> getUploadList() {
        return uploadList;
    }

    public void setUploadList(String notification) {
        ResponseObject result = UploadNotificationAttachmentContent.getUploadedAttachments(notification);

        if (uploadLists.size() > 0)
            uploadLists.clear();

        if (!result.isError() && result != null) {
            uploadLists = (ArrayList<UploadNotificationAttachmentContent>) result.Content();
            uploadList.setValue(uploadLists);
        }
    }
}
