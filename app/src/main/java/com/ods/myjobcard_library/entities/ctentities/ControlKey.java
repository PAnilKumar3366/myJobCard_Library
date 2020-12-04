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
 * Created by lenovo on 16-05-2016.
 */
public class ControlKey extends ZBaseEntity {

    private String Application;
    private String ControlKey;
    private String ControlKeyTxt;

    public ControlKey(ODataEntity entity) {
        create(entity);
    }

    //get methods
    public static ResponseObject getControlKeys() {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.CTRL_KEY_COLLECTION, ZCollections.CTRL_KEY_COLLECTION);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                ArrayList<ControlKey> controlKeys = (ArrayList<ControlKey>) result.Content();
                ArrayList<SpinnerItem> items = new ArrayList<>();
                SpinnerItem item;
                for (ControlKey key : controlKeys) {
                    item = new SpinnerItem();
                    item.setId(key.getControlKey());
                    item.setDescription(key.getControlKeyTxt());
                    items.add(item);
                }
                result.setContent(items);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(ControlKey.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<ControlKey> controlKeys = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    controlKeys.add(new ControlKey(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", controlKeys);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(ControlKey.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getApplication() {
        return Application;
    }

    public void setApplication(String application) {
        Application = application;
    }

    public String getControlKey() {
        return ControlKey;
    }

    public void setControlKey(String controlKey) {
        ControlKey = controlKey;
    }

    public String getControlKeyTxt() {
        return ControlKeyTxt;
    }

    public void setControlKeyTxt(String controlKeyTxt) {
        ControlKeyTxt = controlKeyTxt;
    }
}
