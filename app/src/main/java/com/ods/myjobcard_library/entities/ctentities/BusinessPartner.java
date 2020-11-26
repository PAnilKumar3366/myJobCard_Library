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
 * Created by lenovo on 16-05-2016.
 */
public class BusinessPartner extends BaseEntity {

    private String User;
    private String PersonNumber;
    private String AddressNumber;
    private String FirstName;
    private String LastName;
    private String CompleteName;
    private String PersonGroup;

    public BusinessPartner(ODataEntity entity) {
        create(entity);
    }

    public static ResponseObject getBusinessArea() {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.BUSINESS_PARTNER_COLLECTION, ZCollections.BUSINESS_PARTNER_COLLECTION);
            result = FromEntity((List<ODataEntity>) result.Content());
        } catch (Exception e) {
            DliteLogger.WriteLog(BusinessPartner.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<BusinessPartner> businessPartners = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    businessPartners.add(new BusinessPartner(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", businessPartners);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(BusinessPartner.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getPersonNumber() {
        return PersonNumber;
    }

    public void setPersonNumber(String personNumber) {
        PersonNumber = personNumber;
    }

    public String getAddressNumber() {
        return AddressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        AddressNumber = addressNumber;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getCompleteName() {
        return CompleteName;
    }

    public void setCompleteName(String completeName) {
        CompleteName = completeName;
    }

    public String getPersonGroup() {
        return PersonGroup;
    }

    public void setPersonGroup(String personGroup) {
        PersonGroup = personGroup;
    }

    //get methods

}
