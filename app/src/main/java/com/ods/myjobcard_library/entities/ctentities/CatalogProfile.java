package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 01-08-2016.
 */
public class CatalogProfile extends BaseEntity {

    private String CatalogProfile;
    private String CatalogCode;
    private String CodeGroup;
    private String Status;
    private String ShortText;

    //constructors
    public CatalogProfile(ODataEntity entity) {
        create(entity);
    }

    //get methods
    public static ResponseObject getCodeGroups(String catalogCode) {
        ResponseObject result = null;
        try {
            String resPath = ZCollections.CATALOG_PROFILE_COLLECTION;
            if (catalogCode != null && !catalogCode.isEmpty())
                resPath += "?$filter=(CatalogCode eq '" + catalogCode + "')";
            result = getObjListFromStore(ZCollections.CATALOG_PROFILE_COLLECTION, resPath);
            if (!result.isError()) {
                ArrayList<CatalogProfile> catalogProfiles = (ArrayList<CatalogProfile>) result.Content();
                ArrayList<SpinnerItem> items = new ArrayList<>();
                if (catalogProfiles != null) {
                    for (CatalogProfile catalog : catalogProfiles) {
                        items.add(new SpinnerItem(catalog.getCodeGroup(), catalog.getShortText()));
                    }
                }
                result.setContent(items);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(CatalogProfile.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        if (result != null)
            return result;
        else
            return new ResponseObject(ZConfigManager.Status.Error);
    }

    public static ResponseObject getObjListFromStore(String entitySetName, String resPath) {
        ResponseObject result = null;
        try {
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (!result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                ArrayList<CatalogProfile> content = null;
                CatalogProfile catalogProfile;
                content = new ArrayList<CatalogProfile>();
                for (ODataEntity entity : entities) {
                    catalogProfile = new CatalogProfile(entity);
                    //result = wo.fromEntity(entity);
                    if (catalogProfile != null) {
                        content.add(catalogProfile);
                    } else {
                        //pending: log the error message
                    }
                }
                if (result == null) {
                    result = new ResponseObject(ZConfigManager.Status.Success);
                }
                result.setMessage("");
                result.setContent(content);
            }
        } catch (Exception e) {
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        if (result != null)
            return result;
        else
            return new ResponseObject(ZConfigManager.Status.Error);
    }

    public String getCatalogProfile() {
        return CatalogProfile;
    }

    public void setCatalogProfile(String catalogProfile) {
        CatalogProfile = catalogProfile;
    }

    public String getCatalogCode() {
        return CatalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        CatalogCode = catalogCode;
    }

    public String getCodeGroup() {
        return CodeGroup;
    }

    public void setCodeGroup(String codeGroup) {
        CodeGroup = codeGroup;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getShortText() {
        return ShortText;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }
}
