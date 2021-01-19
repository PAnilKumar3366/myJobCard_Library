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
 * Created by lenovo on 02-08-2016.
 */
public class CatalogCode extends ZBaseEntity {

    private String Catalog;
    private String CodeGroup;
    private String Code;
    private String Version;
    private String CodeGroupText;
    private String CodeText;

    //constructors
    public CatalogCode(ODataEntity entity) {
        create(entity);
    }

    /////////////////////////////////
    public static ResponseObject getCodes(String catalog, String codeGroup) {
        ResponseObject result = null;
        try {
            String resPath = ZCollections.CATALOG_CODE_COLLECTION;
            if (catalog != null && !catalog.isEmpty())
                resPath += "?$filter=(Catalog eq '" + catalog + "'";
            if (codeGroup != null && !codeGroup.isEmpty())
                resPath += " and CodeGroup eq '" + codeGroup + "'";
            resPath += ")";
            result = getObjListFromStore(ZCollections.CATALOG_CODE_COLLECTION, resPath);
            if (!result.isError()) {
                ArrayList<CatalogCode> codes = (ArrayList<CatalogCode>) result.Content();
                ArrayList<SpinnerItem> items = new ArrayList<>();
                if (codes != null) {
                    for (CatalogCode code : codes) {
                        items.add(new SpinnerItem(code.getCode(), code.getCodeText()));
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

    public static ResponseObject getCatalogCode(String catalog, String codeGroup, String code) {
        ResponseObject result = null;
        try {
            String resPath = ZCollections.CATALOG_CODE_COLLECTION;
            if (catalog != null && !catalog.isEmpty())
                resPath += "?$filter=(Catalog eq '" + catalog + "'";
            if (codeGroup != null && !codeGroup.isEmpty())
                resPath += " and CodeGroup eq '" + codeGroup + "'";
            if (code != null && !code.isEmpty())
                resPath += " and Code eq '" + code + "'";
            resPath += ")";
            result = getObjListFromStore(ZCollections.CATALOG_CODE_COLLECTION, resPath);
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
                ArrayList<CatalogCode> content = null;
                CatalogCode code;
                content = new ArrayList<CatalogCode>();
                for (ODataEntity entity : entities) {
                    code = new CatalogCode(entity);
                    //result = wo.fromEntity(entity);
                    if (code != null) {
                        content.add(code);
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
    public static ResponseObject getDamageGrpandCodeDescritions(String catalogCode,String damagecodeGrp,String damageCode){
        ResponseObject result = null;
        String resPath = ZCollections.CATALOG_CODE_COLLECTION;
        if (catalogCode != null && !catalogCode.isEmpty()&&damagecodeGrp != null && !damagecodeGrp.isEmpty()&&damageCode != null && !damageCode.isEmpty()) {
            resPath += "?$filter=(Catalog eq '" + catalogCode + "' and CodeGroup eq '" + damagecodeGrp + "' and Code eq '" + damageCode + "')";
            result = DataHelper.getInstance().getEntities(ZCollections.CATALOG_CODE_COLLECTION, resPath);
            if (!result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                ArrayList<CatalogCode> content = null;
                CatalogCode catlogCode;
                content = new ArrayList<CatalogCode>();
                for (ODataEntity entity : entities) {
                    catlogCode = new CatalogCode(entity);
                    if (catlogCode != null) {
                        content.add(catlogCode);
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
        }
        return result;
    }
    public String getCatalog() {
        return Catalog;
    }

    public void setCatalog(String catalog) {
        Catalog = catalog;
    }

    public String getCodeGroup() {
        return CodeGroup;
    }

    public void setCodeGroup(String codeGroup) {
        CodeGroup = codeGroup;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getCodeGroupText() {
        return CodeGroupText;
    }

    public void setCodeGroupText(String codeGroupText) {
        CodeGroupText = codeGroupText;
    }

    public String getCodeText() {
        return CodeText;
    }

    public void setCodeText(String codeText) {
        CodeText = codeText;
    }
}
