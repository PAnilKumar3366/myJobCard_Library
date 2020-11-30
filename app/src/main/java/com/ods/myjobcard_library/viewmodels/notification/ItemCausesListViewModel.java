package com.ods.myjobcard_library.viewmodels.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.transaction.NotificationItemCauses;
import com.ods.ods_sdk.entities.ResponseObject;

import java.util.ArrayList;

public class ItemCausesListViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<NotificationItemCauses>> CausesList = new MutableLiveData<>();
    private MutableLiveData<NotificationItemCauses> SingleItem = new MutableLiveData<>();

    public ItemCausesListViewModel(@NonNull Application application) {
        super(application);
    }

    public void setCausesList(String notification, String itemNum, boolean isWONotif) {
        ResponseObject result = NotificationItemCauses.getItemCauses(ZAppSettings.FetchLevel.List, notification, itemNum, isWONotif);
        ArrayList<NotificationItemCauses> itemCauses;
        if (!result.isError())
            itemCauses = (ArrayList<NotificationItemCauses>) result.Content();
        else
            itemCauses = new ArrayList<>();
        CausesList.setValue(itemCauses);
    }

    public MutableLiveData<ArrayList<NotificationItemCauses>> getCausesList() {
        return CausesList;
    }

    public MutableLiveData<NotificationItemCauses> getSingleItem() {
        return SingleItem;
    }

    public void setSingleItem(NotificationItemCauses singleCause) {
     /*ResponseObject   result = NotificationItemCauses.getItemCauses(ZAppSettings.FetchLevel.Single, notification,
                itemNum, isWONotif);
        NotificationItemCauses currSelectedCause = null;
        if (!result.isError())
            currSelectedCause = ((ArrayList<NotificationItemCauses>) result.Content()).get(0);*/

        SingleItem.setValue(singleCause);
    }
}
