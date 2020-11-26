package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 05-05-2016.
 */
public class Priority extends BaseEntity {

    private String PriorityType;
    private String Priority;
    private String EnteredBy;
    private String PriorityText;
    public Priority(ODataEntity entity) {
        create(entity);
    }

    /**
     * @return ResponseObject instance with ArrayList of SpinnerItem as Content
     */
    //get methods
    public static ResponseObject getPriorities() {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            dataHelper = DataHelper.getInstance();
            String resPath = ZCollections.PRIORITY_COLLECTION;
            String orderByCriteria = "?$orderby=Priority";
            resPath += orderByCriteria;
            result = dataHelper.getEntities(ZCollections.PRIORITY_COLLECTION, resPath);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                ArrayList<Priority> priorities = (ArrayList<Priority>) result.Content();
                ArrayList<SpinnerItem> items = new ArrayList<>();
                SpinnerItem item;
                for (Priority priority : priorities) {
                    item = new SpinnerItem();
                    item.setId(priority.getPriority());
                    item.setDescription(priority.getPriorityText());
                    items.add(item);
                }
                result.setContent(items);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Priority.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public static String getPriorityText(String priority) {
        String priorityText = "";
        try {
            String resPath = ZCollections.PRIORITY_COLLECTION + "?$filter=Priority eq '" + priority + "'&$select=PriorityText";
            ResponseObject result = DataHelper.getInstance().getEntities(ZCollections.PRIORITY_COLLECTION, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                if (entities != null && entities.size() > 0) {
                    priorityText = String.valueOf(entities.get(0).getProperties().get("PriorityText").getValue());
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Priority.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return priorityText;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<Priority> priorities = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    priorities.add(new Priority(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", priorities);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(Priority.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getPriorityType() {
        return PriorityType;
    }

    public void setPriorityType(String priorityType) {
        PriorityType = priorityType;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getPriorityText() {
        return PriorityText;
    }

    public void setPriorityText(String priorityText) {
        PriorityText = priorityText;
    }

}