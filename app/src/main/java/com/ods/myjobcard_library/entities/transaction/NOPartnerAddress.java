package com.ods.myjobcard_library.entities.transaction;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.sap.smp.client.odata.ODataEntity;

public class NOPartnerAddress extends BaseEntity {

    private String ObjectNumber;
    private String PartnerFunction;

    //WO child elements
    //Fields
    private String Counter;
    private String Partner;
    private String EnteredBy;
    private String AddressNumber;
    private String Country;
    private String Name;
    private String City;
    private String PostalCode;
    private String Region;
    private String Street;
    private String Telephone;

    public NOPartnerAddress(String objectNumber, String counter) {
        this.ObjectNumber = objectNumber;
        this.Counter = counter;
    }

    public NOPartnerAddress(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        create(entity);
    }

    //Setters and Getters Method

    public String getObjectNumber() {
        return ObjectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        ObjectNumber = objectNumber;
    }

    public String getPartnerFunction() {
        return PartnerFunction;
    }

    public void setPartnerFunction(String partnerFunction) {
        PartnerFunction = partnerFunction;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public String getPartner() {
        return Partner;
    }

    public void setPartner(String partner) {
        Partner = partner;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getAddressNumber() {
        return AddressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        AddressNumber = addressNumber;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }


    //End of Setters and Getters Method

}