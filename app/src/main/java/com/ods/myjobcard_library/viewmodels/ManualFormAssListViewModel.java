package com.ods.myjobcard_library.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.forms.FormListObject;
import com.ods.myjobcard_library.entities.forms.ManualFormAssignmentSetModel;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class ManualFormAssListViewModel extends BaseViewModel {

    private MutableLiveData<ArrayList<ManualFormAssignmentSetModel>> manualListLiveData;
    private MutableLiveData<ArrayList<FormListObject>> manualFormList = new MutableLiveData<>();

    public ManualFormAssListViewModel(@NonNull @org.jetbrains.annotations.NotNull Application application) {
        super(application);
        manualListLiveData = new MutableLiveData<>();
    }

    public void setManualFormList() {

        ArrayList<FormListObject> formListObjects = new ArrayList<>();
        String[] FormName = {"CreateNotification", "InspectionForm", "Covid-Form"};
        String[] Version = {"1.0", "2.0", "3.0"};
        String[] Mandatory = {"X", "X", ""};
        String[] MultipleSub = {"X", "X", ""};
        int[] Occur = {4, 5, 3};
        int[] filledForm = {2, 2, 2};
        for (int i = 0; i < 3; i++)
            formListObjects.add(new FormListObject(FormName[i], "", Version[i], Mandatory[i], Occur[i], MultipleSub[i], filledForm[i], "", false));
        manualFormList.setValue(formListObjects);
    }

    public MutableLiveData<ArrayList<FormListObject>> getManualFormList() {
        return manualFormList;
    }

    public void setManualFormList(ArrayList<FormListObject> listObjects) {
        manualFormList.setValue(listObjects);
    }

    public void setManualListLiveData() {
        try {

            String[] FormName = {"CreateNotification", "InspectionForm", "Covid-Form"};
            String[] Version = {"1.0", "2.0", "3.0"};
            String[] Mandatory = {"Yes", "Yes", "No"};
            ArrayList<ManualFormAssignmentSetModel> dummyList = new ArrayList<>();
            for (int i = 0; i < 3; i++)
                dummyList.add(new ManualFormAssignmentSetModel(Version[i], FormName[i], Mandatory[i]));
            manualListLiveData.setValue(dummyList);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            manualListLiveData.setValue(new ArrayList<ManualFormAssignmentSetModel>());
        }
    }

    public MutableLiveData<ArrayList<ManualFormAssignmentSetModel>> getManualListLiveData() {
        return manualListLiveData;
    }

    public void setManualListLiveData(ArrayList<ManualFormAssignmentSetModel> selectedList) {
        manualListLiveData.setValue(selectedList);
    }
}
