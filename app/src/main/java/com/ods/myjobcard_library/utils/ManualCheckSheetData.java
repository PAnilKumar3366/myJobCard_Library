package com.ods.myjobcard_library.utils;

import com.ods.myjobcard_library.entities.forms.ManualFormAssignmentSetModel;

import java.util.ArrayList;

public class ManualCheckSheetData {
    public static volatile ManualCheckSheetData checkSheetData;
    private static ArrayList<ManualFormAssignmentSetModel> manualCheckSheetList = new ArrayList<>();

    private ManualCheckSheetData() {
        //Prevent form the reflection api.
        if (checkSheetData != null)
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
    }

    public static ManualCheckSheetData getInstance() {
        if (checkSheetData == null)
            synchronized (ManualCheckSheetData.class) {
                if (checkSheetData == null)
                    checkSheetData = new ManualCheckSheetData();
            }
        return checkSheetData;
    }

    public ArrayList<ManualFormAssignmentSetModel> getManualCheckSheetList() {
        return manualCheckSheetList;
    }

    public void setManualCheckSheetList(ArrayList<ManualFormAssignmentSetModel> manualCheckSheetList) {
        this.manualCheckSheetList = manualCheckSheetList;
    }
}
