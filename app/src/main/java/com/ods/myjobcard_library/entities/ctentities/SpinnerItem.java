package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

/**
 * Created by lenovo on 25-05-2016.
 */
public class SpinnerItem {

    private String Id;
    private String Description;
    private String PlantId;
    private String EnteredBy, LastChangedBy;
    private boolean selected;
    private String ObjectID;
    private String selectedCodeGrp;


    public SpinnerItem(String id, String description, String number) {
        this.Id = id;
        this.Description = description;
        this.ObjectID = number;
    }

    //constructors
    public SpinnerItem(String id, String description) {
        this.Id = id;
        this.Description = description;
    }

    public SpinnerItem() {
    }

    public static int getItemPosition(ArrayList<SpinnerItem> items, String id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equalsIgnoreCase(id))
                return i;
        }
        return -1;
    }

    public static int getItemPosition(ArrayList<SpinnerItem> items, String id, String codeGrp) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equalsIgnoreCase(id) && items.get(i).getSelectedCodeGrp().equalsIgnoreCase(codeGrp))
                return i;
        }
        return -1;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getLastChangedBy() {
        return LastChangedBy;
    }

    public void setLastChangedBy(String lastChangedBy) {
        LastChangedBy = lastChangedBy;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTruncatedId() {
        String truncatedId = Id;
        try {
            return String.valueOf(Integer.parseInt(Id));
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Warning, e.getMessage());
        }
        return truncatedId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPlantId() {
        return PlantId;
    }

    public void setPlantId(String plantId) {
        PlantId = plantId;
    }

    public String getObjectID() {
        return ObjectID;
    }

    public void setObjectID(String objectID) {
        ObjectID = objectID;
    }

    public String getSelectedCodeGrp() {
        return selectedCodeGrp;
    }

    public void setSelectedCodeGrp(String selectedCodeGrp) {
        this.selectedCodeGrp = selectedCodeGrp;
    }
}
