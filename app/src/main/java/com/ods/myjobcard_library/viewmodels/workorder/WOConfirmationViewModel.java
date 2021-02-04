package com.ods.myjobcard_library.viewmodels.workorder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.entities.transaction.WOConfirmation;

public class WOConfirmationViewModel extends AndroidViewModel {

    private MutableLiveData<WOConfirmation> currentOprCNF = new MutableLiveData<WOConfirmation>();

    public WOConfirmationViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<WOConfirmation> getCurrentOprCNF() {
        return currentOprCNF;
    }

    /* getting the selected operation's confirmation data by passing WoNum and its selected operation number
     * */

    public void setCurrentOprCNF(String woNum, String oprNum) {
        WOConfirmation woConfirmation=WOConfirmation.getOperationFinalConfirmation(oprNum,woNum);
        if(woConfirmation!=null&&!woConfirmation.isError()){
            currentOprCNF.setValue(woConfirmation);
        }
        else
            currentOprCNF.setValue(null);
    }
}
