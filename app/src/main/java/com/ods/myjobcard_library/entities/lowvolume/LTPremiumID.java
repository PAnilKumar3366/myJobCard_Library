package com.ods.myjobcard_library.entities.lowvolume;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ctentities.Equipment;
import com.ods.myjobcard_library.entities.ctentities.SpinnerItem;
import com.ods.myjobcard_library.entities.ctentities.StandardTextSet;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

public class LTPremiumID extends BaseEntity {
    private String PSGrouping;
    private String PremiumNo;
    private String PremiumID;
    private String PremiumText;

    public LTPremiumID(ODataEntity entity) {
        initializingEntityProperties();
        create(entity);
    }

    public static ArrayList<SpinnerItem> getPermiumIdItems() {
        ArrayList<SpinnerItem> premiumIdlist = new ArrayList<>();
        try {
            String entitySetName = ZCollections.LT_PREMIUM_ID_SET;
            String resPath = entitySetName + "?$select=PremiumID,PremiumNo,PremiumText";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            result = FromEntity((List<ODataEntity>) result.Content());
            if (result != null && !result.isError()) {
                ArrayList<LTPremiumID> spinnerItems = new ArrayList<>();
                spinnerItems = (ArrayList<LTPremiumID>) result.Content();
                if (spinnerItems != null) {
                    for (LTPremiumID entity : spinnerItems)
                        premiumIdlist.add(new SpinnerItem(entity.getPremiumID(), entity.getPremiumText(), entity.getPremiumNo()));
                }
            }

        } catch (Exception e) {
            DliteLogger.WriteLog(StandardTextSet.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return premiumIdlist;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<LTPremiumID> standardTextSets = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    standardTextSets.add(new LTPremiumID(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", standardTextSets);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
        }
        return result;
    }

    public String getPSGrouping() {
        return PSGrouping;
    }

    public void setPSGrouping(String PSGrouping) {
        this.PSGrouping = PSGrouping;
    }

    public String getPremiumNo() {
        return PremiumNo;
    }

    public void setPremiumNo(String premiumNo) {
        PremiumNo = premiumNo;
    }

    public String getPremiumID() {
        return PremiumID;
    }

    public void setPremiumID(String premiumID) {
        PremiumID = premiumID;
    }

    public String getPremiumText() {
        return PremiumText;
    }

    public void setPremiumText(String premiumText) {
        PremiumText = premiumText;
    }

    private void initializingEntityProperties() {
        this.setEntityType(ZCollections.LT_PREMIUM_ID_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.LT_PREMIUM_ID_SET);
        this.setEntityResourcePath(ZCollections.LT_PREMIUM_ID_SET);
        this.addKeyFieldNames("PSGrouping");
        this.addKeyFieldNames("PremiumNo");
        this.addKeyFieldNames("PremiumID");
    }

}

