package com.ods.myjobcard_library.viewmodels.workorder;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.Components;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class ComponentsViewModel extends AndroidViewModel {

    private static final String TAG = "ComponentsViewModel";
    private MutableLiveData<ArrayList<Components>> components = new MutableLiveData<>();
    private MutableLiveData<Components> selectedComponent = new MutableLiveData<>();

    public ComponentsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ArrayList<Components>> getComponents() {
        return components;
    }

    public void setComponents(WorkOrder workOrder) {
        String oprNum = null;
        if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)
            oprNum = workOrder.getCurrentOperation().getOperationNum();
        ResponseObject result = Components.getComponents(ZAppSettings.FetchLevel.Header, workOrder.getWorkOrderNum(), oprNum, null, null);
        if (!result.isError()) {
            ArrayList<Components> componentsList = (ArrayList<Components>) result.Content();
            components.setValue(componentsList);
        }
    }

    public LiveData<Components> getSelectedComponent() {
        return selectedComponent;
    }

    public void setSelectedComponent(WorkOrder workOrder, String selectedComponentNum) {
        ResponseObject result = Components.getComponents(ZAppSettings.FetchLevel.Single, workOrder.getWorkOrderNum(), null, selectedComponentNum, null);
        if (!result.isError()) {
            Components currSelectedComp = ((ArrayList<Components>) result.Content()).get(0);
            selectedComponent.setValue(currSelectedComp);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
    }
}
