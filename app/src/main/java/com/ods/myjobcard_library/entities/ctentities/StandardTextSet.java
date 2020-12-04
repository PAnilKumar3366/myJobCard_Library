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

public class StandardTextSet extends ZBaseEntity {

    public String TextObject;
    public String StandardTextName;
    public String TextID;
    public String Language;
    public String LineWidth;
    public String Lines;
    public String LongText;
    public String EnteredBy;

    public StandardTextSet(ODataEntity oDataEntity) {
        initializingEntityProperties();
        create(oDataEntity);
    }

    public StandardTextSet() {
        initializingEntityProperties();
    }

    public static StandardTextSet getStandardText(String textName) {
        StandardTextSet standardText = null;
        DataHelper dataHelper = null;
        String strQuery;
        ResponseObject result;

        try {
            dataHelper = DataHelper.getInstance();
            String entitySetName = ZCollections.STANDARDTEXT_SET;
            strQuery = entitySetName + "?$filter=StandardTextName eq '" + textName + "'";
            result = dataHelper.getEntities(entitySetName, strQuery);
            result = FromEntity((List<ODataEntity>) result.Content());
            if (!result.isError())
                standardText = ((ArrayList<StandardTextSet>) result.Content()).get(0);
        } catch (Exception e) {
            DliteLogger.WriteLog(StandardTextSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return standardText;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<StandardTextSet> standardTextSets = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    standardTextSets.add(new StandardTextSet(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", standardTextSets);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(StandardTextSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
        }
        return result;
    }

    public static ArrayList<SpinnerItem> getStandardTextNames() {
        ArrayList<SpinnerItem> textNames = new ArrayList<>();
        try {
            String entitySetName = ZCollections.STANDARDTEXT_SET;
            String resPath = entitySetName + "?$select=StandardTextName,TextID";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                if (entities != null && entities.size() > 0) {
                    for (ODataEntity entity : entities) {
                        String textName = String.valueOf(entity.getProperties().get("StandardTextName").getValue());
                        //String textId = String.valueOf(entity.getProperties().get("TextID").getValue());
                        textNames.add(new SpinnerItem(textName, textName));
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(StandardTextSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return textNames;
    }

    public String getTextObject() {
        return TextObject;
    }

    public void setTextObject(String textObject) {
        TextObject = textObject;
    }

    public String getStandardTextName() {
        return StandardTextName;
    }

    public void setStandardTextName(String standardTextName) {
        StandardTextName = standardTextName;
    }

    public String getTextID() {
        return TextID;
    }

    public void setTextID(String textID) {
        TextID = textID;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getLineWidth() {
        return LineWidth;
    }

    public void setLineWidth(String lineWidth) {
        LineWidth = lineWidth;
    }

    public String getLines() {
        return Lines;
    }

    public void setLines(String lines) {
        Lines = lines;
    }

    public String getLongText() {
        return LongText;
    }

    public void setLongText(String longText) {
        LongText = longText;
    }

    public String getFormattedLongText() {
        if (LongText != null && !LongText.isEmpty())
            return LongText.replaceAll(ZConfigManager.STANDARDTEXT_LINE_SEPARATOR, "\n");
        return LongText;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    private void initializingEntityProperties() {
        this.setEntityType(ZCollections.STANDARDTEXT_SET);
        this.setEntitySetName(ZCollections.STANDARDTEXT_SET);
        this.setEntityResourcePath(ZCollections.STANDARDTEXT_SET);
        this.addKeyFieldNames("TextObject");
        this.addKeyFieldNames("StandardTextName");
    }

}
