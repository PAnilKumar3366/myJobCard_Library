package com.ods.myjobcard_library.entities.ctentities;


import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 23-06-2016.
 */
public class AttendanceType extends ZBaseEntity {

    private String PSGrouping;
    private String AttAbsType;
    private String AATypeText;
    private String PersonnelArea;
    private String PersSubArea;
    private String PSText;

    public AttendanceType() {
    }

    public AttendanceType(ODataEntity entity) {
        create(entity);
    }

    public static ResponseObject getAttendanceTypes(String attAbsType) {
        ResponseObject result = null;
        String entitySetName = ZCollections.ATTENDANCE_TYPE_COLLECTION;
        ;
        String resPath = entitySetName;
        if (attAbsType != null && !attAbsType.isEmpty())
            resPath += "?$filter=(AttAbsType eq '" + attAbsType + "')";
        result = DataHelper.getInstance().getEntities(entitySetName, resPath);
        if (!result.isError()) {
            result = FromEntity((List<ODataEntity>) result.Content());
            ArrayList<AttendanceType> attendanceTypes = (ArrayList<AttendanceType>) result.Content();
            ArrayList<SpinnerItem> items = new ArrayList<>();
            SpinnerItem item;
            for (AttendanceType type : attendanceTypes) {
                item = new SpinnerItem();
                item.setId(type.getAttAbsType());
                item.setDescription(type.getAATypeText());
                items.add(item);
            }
            result.setContent(items);
        } else {
            DliteLogger.WriteLog(AttendanceType.class, ZAppSettings.LogLevel.Error, "error in getting Attendance Types. Message: " + result.getMessage());
        }
        return result;
    }

    public static ArrayList<SpinnerItem> searchAttendanceTypes(String searchText, String searchOption, ArrayList<SpinnerItem> types) {

        ArrayList<SpinnerItem> searchedTypes = new ArrayList<SpinnerItem>();
        if (searchOption.equals(ZCollections.SEARCH_OPTION_ID)) {
            for (SpinnerItem type : types) {
                if (type.getId().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchedTypes.add(type);
                }
            }
        } else if (searchOption.equals(ZCollections.SEARCH_OPTION_DESCRIPTION)) {
            for (SpinnerItem type : types) {
                if (type.getDescription().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchedTypes.add(type);
                }
            }
        } else {
            for (SpinnerItem type : types) {
                if (type.getId().trim().toLowerCase().contains(searchText.toLowerCase()) || type.getDescription().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchedTypes.add(type);
                }
            }
        }
        return searchedTypes;

    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<AttendanceType> attendanceTypes = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    attendanceTypes.add(new AttendanceType(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", attendanceTypes);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(AttendanceType.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getPSGrouping() {
        return PSGrouping;
    }

    public void setPSGrouping(String PSGrouping) {
        this.PSGrouping = PSGrouping;
    }

    public String getAttAbsType() {
        return AttAbsType;
    }

    public void setAttAbsType(String attAbsType) {
        AttAbsType = attAbsType;
    }

    public String getAATypeText() {
        return AATypeText;
    }

    public void setAATypeText(String AATypeText) {
        this.AATypeText = AATypeText;
    }

    public String getPersonnelArea() {
        return PersonnelArea;
    }

    public void setPersonnelArea(String personnelArea) {
        PersonnelArea = personnelArea;
    }

    public String getPersSubArea() {
        return PersSubArea;
    }

    public void setPersSubArea(String persSubArea) {
        PersSubArea = persSubArea;
    }

    public String getPSText() {
        return PSText;
    }

    public void setPSText(String PSText) {
        this.PSText = PSText;
    }
}
