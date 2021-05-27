package com.ods.myjobcard_library.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.forms.FormListObject;
import com.ods.myjobcard_library.entities.forms.ManualFormAssignmentSetModel;
import com.ods.myjobcard_library.utils.ManualCheckSheetData;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class ManualFormAssListViewModel extends BaseViewModel {

    private MutableLiveData<ArrayList<ManualFormAssignmentSetModel>> manualListLiveData;
    private MutableLiveData<ArrayList<FormListObject>> manualFormList = new MutableLiveData<>();
    ManualCheckSheetData manualCheckSheetData;
    private MutableLiveData<ArrayList<ManualFormAssignmentSetModel>> newCheckSheetsLiveData = new MutableLiveData<>();
    private ArrayList<ManualFormAssignmentSetModel> dummyList = new ArrayList<>();

    public ManualFormAssListViewModel(@NonNull @org.jetbrains.annotations.NotNull Application application) {
        super(application);
        manualListLiveData = new MutableLiveData<>();
        // manualCheckSheetData=ManualCheckSheetData.getInstance();
    }

    public void setManualListLiveData(ArrayList<ManualFormAssignmentSetModel> existedList) {
        try {
            if (existedList.size() > 0) {
                dummyList.clear();
                ManualCheckSheetData.getInstance().getManualCheckSheetList().clear();
            }
            dummyList.addAll(existedList);
            if (dummyList.size() == 0) {
                String[] FormName = {"CreateNotification", "InspectionForm", "Covid-Form"};
                String[] Version = {"1.0", "2.0", "3.0"};
                String[] Mandatory = {"X", "X", ""};
                String[] MultipleSub = {"X", "X", "X"};
                String[] Occur = {"4", "5", "3"};
                for (int i = 0; i < 3; i++)
                    dummyList.add(new ManualFormAssignmentSetModel(Version[i], FormName[i], Mandatory[i], MultipleSub[i], Occur[i]));
            }
            ManualCheckSheetData.getInstance().setManualCheckSheetList(dummyList);
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

    public MutableLiveData<ArrayList<ManualFormAssignmentSetModel>> getNewCheckSheetsLiveData() {
        return newCheckSheetsLiveData;
    }

    public void setNewCheckSheetsLiveData(ArrayList<ManualFormAssignmentSetModel> selectedList) {
        dummyList.addAll(selectedList);
        ManualCheckSheetData.getInstance().setManualCheckSheetList(selectedList);
        manualListLiveData.setValue(selectedList);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        dummyList.clear();
    }
}
