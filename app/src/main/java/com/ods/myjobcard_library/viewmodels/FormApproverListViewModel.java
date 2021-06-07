package com.ods.myjobcard_library.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.entities.forms.ApproverMasterData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FormApproverListViewModel extends BaseViewModel {

    MutableLiveData<ArrayList<ApproverMasterData>> ApproversListLiveData = new MutableLiveData<>();

    public FormApproverListViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

}
