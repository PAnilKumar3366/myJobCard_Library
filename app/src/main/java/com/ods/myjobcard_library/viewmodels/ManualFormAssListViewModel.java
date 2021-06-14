package com.ods.myjobcard_library.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.forms.FormListObject;
import com.ods.myjobcard_library.entities.forms.FormMasterMetadata;
import com.ods.myjobcard_library.entities.forms.ManualFormAssignmentSetModel;
import com.ods.myjobcard_library.utils.ManualCheckSheetData;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class ManualFormAssListViewModel extends BaseViewModel {

    private MutableLiveData<ArrayList<ManualFormAssignmentSetModel>> manualListLiveData;
    private MutableLiveData<ArrayList<FormListObject>> manualFormList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<FormMasterMetadata>> manualFormMasterLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> postManualFormAssignment=new MutableLiveData<>();
    ManualCheckSheetData manualCheckSheetData;
    private MutableLiveData<ArrayList<ManualFormAssignmentSetModel>> newCheckSheetsLiveData = new MutableLiveData<>();
    private ArrayList<ManualFormAssignmentSetModel> dummyList = new ArrayList<>();
    private ArrayList<FormMasterMetadata> formMasterMetadataArrayList=new ArrayList<>();
    private FormMasterMetaDataHelper formMasterMetaDataHelper;
    private ManualFormAssignmentHelper manualFormAssignmentHelper;

    public ManualFormAssListViewModel(@NonNull @org.jetbrains.annotations.NotNull Application application) {
        super(application);
        manualListLiveData = new MutableLiveData<>();
        formMasterMetaDataHelper=new FormMasterMetaDataHelper();
        manualFormAssignmentHelper=new ManualFormAssignmentHelper();
    }

    public void setManualListLiveData(ArrayList<ManualFormAssignmentSetModel> existedList) {
        try {
            if (existedList.size() > 0) {
                dummyList.clear();
                ManualCheckSheetData.getInstance().getManualCheckSheetList().clear();
            }
            dummyList.addAll(existedList);
           /* if (dummyList.size() == 0) {
                String[] FormName = {"CreateNotification", "InspectionForm", "Covid-Form"};
                String[] Version = {"1.0", "2.0", "3.0"};
                String[] Mandatory = {"X", "X", ""};
                String[] MultipleSub = {"X", "X", "X"};
                String[] Occur = {"4", "5", "3"};
                for (int i = 0; i < 3; i++)
                    dummyList.add(new ManualFormAssignmentSetModel(Version[i], FormName[i], Mandatory[i], MultipleSub[i], Occur[i]));
            }*/
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

    public void onFetchFormMasterMetadata(int skipValue, int numRecords){
        ArrayList<ZODataEntity> zoDataEntityArrayList=formMasterMetaDataHelper.getZoDataManualFormMasterEntities(skipValue,numRecords);
        formMasterMetadataArrayList=onFetchManualFormMasterEntities(zoDataEntityArrayList);
        manualFormMasterLiveData.setValue(formMasterMetadataArrayList);
    }
    protected ArrayList<FormMasterMetadata> onFetchManualFormMasterEntities(ArrayList<ZODataEntity> zODataEntities) {
        formMasterMetadataArrayList = new ArrayList<>();
        try {
            for (ZODataEntity entity : zODataEntities) {
                FormMasterMetadata formMasterMetadata = new FormMasterMetadata(entity);
                formMasterMetadataArrayList.add(formMasterMetadata);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Error, e.getMessage());
        }
        return formMasterMetadataArrayList;
    }

    public MutableLiveData<ArrayList<FormMasterMetadata>> getManualFormMasterList() {
        return manualFormMasterLiveData;
    }

    public void setPostManualFormAssignment(ArrayList<ManualFormAssignmentSetModel> manualFormAssignmentSetList){
        try {
            postManualFormAssignment.setValue(manualFormAssignmentHelper.postManualFormAssignment(manualFormAssignmentSetList,true));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public MutableLiveData<Boolean> getPostManualFormAssignment() {
        return postManualFormAssignment;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        dummyList.clear();
    }
}
