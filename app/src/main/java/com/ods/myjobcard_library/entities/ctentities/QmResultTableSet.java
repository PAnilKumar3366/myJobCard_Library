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

public class QmResultTableSet extends ZBaseEntity {
    //Fields
    private String CharNums;
    private String Description;
    private String Catalog;
    private String SelectedSet;
    private String Plant;
    private String Equipment;
    private String CodeGroup;
    private String Code;
    private String CodeValuation;
    private String DefectClass;
    private String EnteredBy;

    public QmResultTableSet(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public static ResponseObject getQMResultValues(String charName, String equipmentNum, String selectedSet) {
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);
        try {
            String entitySetName = ZCollections.QM_RESULT_TABLE_COLLECTION;
            String resPath = entitySetName + "?$filter=CharNums eq '" + charName + "' and Equipment eq '" + equipmentNum + "' and SelectedSet eq '" + selectedSet + "'&$orderby=Code";
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                ArrayList<QmResultTableSet> qmValues = (ArrayList<QmResultTableSet>) result.Content();
                ArrayList<SpinnerItem> items = new ArrayList<>();
                SpinnerItem item;
                for (QmResultTableSet value : qmValues) {
                    item = new SpinnerItem();
                    item.setId(value.getCode());
                    item.setDescription(value.getDescription());
                    item.setSelectedCodeGrp(value.getCodeGroup());
                    items.add(item);
                }
                result.setContent(items);
            } else {
                DliteLogger.WriteLog(QmResultTableSet.class, ZAppSettings.LogLevel.Error, "error in getting QMResultValues. Message: " + result.getMessage());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(QmResultTableSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<QmResultTableSet> QmResultValueArrayList = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    QmResultValueArrayList.add(new QmResultTableSet(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", QmResultValueArrayList);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(QmResultTableSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void initializeEntityProperties() {
        this.setEntityType(ZCollections.QM_RESULT_TABLE_COLLECTION_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.QM_RESULT_TABLE_COLLECTION);
        this.addKeyFieldNames("SelectedSet");
        this.addKeyFieldNames("Equipment");
        this.addKeyFieldNames("Code");
    }

    public String getCharNums() {
        return CharNums;
    }

    public void setCharNums(String charNums) {
        CharNums = charNums;
    }

    public String getCatalog() {
        return Catalog;
    }

    public void setCatalog(String catalog) {
        Catalog = catalog;
    }

    public String getSelectedSet() {
        return SelectedSet;
    }

    public void setSelectedSet(String selectedSet) {
        SelectedSet = selectedSet;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
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

    public String getCodeValuation() {
        return CodeValuation;
    }

    public void setCodeValuation(String codeValuation) {
        CodeValuation = codeValuation;
    }

    public String getDefectClass() {
        return DefectClass;
    }

    public void setDefectClass(String defectClass) {
        DefectClass = defectClass;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
