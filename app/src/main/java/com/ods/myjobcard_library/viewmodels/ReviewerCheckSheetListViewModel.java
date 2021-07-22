package com.ods.myjobcard_library.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.entities.forms.ReviewerFormResponse;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReviewerCheckSheetListViewModel extends BaseViewModel {

    ReviewerFormResponseHelper helper;
    MutableLiveData<ArrayList<ReviewerFormResponse>> checkSheetInstanceList = new MutableLiveData<>();

    public ReviewerCheckSheetListViewModel(@NonNull @NotNull Application application) {
        super(application);
        helper = new ReviewerFormResponseHelper();
    }

    public void setCheckSheetInstanceList() {
        checkSheetInstanceList.setValue(onFetchReviewerFormResponse(helper.getCheckSheetInstanceList()));
    }

    /**
     * This method is used to convert the ZOData Entites to the Required pojo and returns the list of objects
     *
     * @param zoDataEntities
     * @return Reviewer CheckSheet Instance List
     */
    protected ArrayList<ReviewerFormResponse> onFetchReviewerFormResponse(ArrayList<ZODataEntity> zoDataEntities) {
        ArrayList<ReviewerFormResponse> instanceList = new ArrayList<>();
        try {
            for (ZODataEntity entity : zoDataEntities) {
                ReviewerFormResponse response = new ReviewerFormResponse(entity);
                instanceList.add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setError(e.getMessage());
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return instanceList;
    }

    public MutableLiveData<ArrayList<ReviewerFormResponse>> getCheckSheetInstanceList() {
        return checkSheetInstanceList;
    }
}
