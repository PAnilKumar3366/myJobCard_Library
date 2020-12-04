package com.ods.myjobcard_library.entities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 14-03-2016.
 */
public class PartnerAddress extends ZBaseEntity {

    public static ArrayList<PartnerAddress> dummyPartnerAddresses = new ArrayList<PartnerAddress>();
    private DataHelper dataHelper = null;
    private String City;
    private String Region;
    private String PostalCode;
    private String ObjectNumber;
    private String Counter;
    private String PartnerFunction;
    private String Partner;
    private String Name;
    private String Telephone;
    private String EnteredBy;
    private String AddressNumber;
    private BigDecimal Latitude;
    private BigDecimal Longitude;
    private String Street;
    private String Country;

    public PartnerAddress() {
        super();
    }

    public PartnerAddress(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    public static void initializeData(int length) {
        dummyPartnerAddresses.clear();
        for (int i = 0; i < length; i++) {
            PartnerAddress partnerAddress = new PartnerAddress();
            partnerAddress.City = "Hyderabad";
            partnerAddress.Country = "India";
            partnerAddress.Region = "Telangana";
            if (i == 0)
                partnerAddress.Region = "Balkampet";
            else if (i == 1)
                partnerAddress.Region = "Begumpet";
            else if (i == 2)
                partnerAddress.Region = "Ameerpet";
            else
                partnerAddress.Region = "S.R.Nagar";

            dummyPartnerAddresses.add(partnerAddress);
        }
    }

    public static ResponseObject getWOPartnerAddress(String ObjectNum) {
        ResponseObject result = null;
        String resourcePath = null;// "WoHeaderSet?$expand=NavOpera/OperaToPRT";
        String strAddEntitySet = null;
        try {
            strAddEntitySet = ZCollections.PARTNER_ADDR_COLLECTION;
            resourcePath = strAddEntitySet + "?$filter=(ObjectNumber eq '" + ObjectNum + "')";
            result = DataHelper.getInstance().getEntities(strAddEntitySet, resourcePath);
            if (!result.isError()) {
                //parse data for WO from payload
                result = FromEntity((List<ODataEntity>) result.Content());
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(PartnerAddress.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<PartnerAddress> woPartnerAddresses = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    woPartnerAddresses.add(new PartnerAddress(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", woPartnerAddresses);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(PartnerAddress.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.PARTNER_ADDR_COLLECTION);
        this.setEntityType(ZCollections.PARTNER_ADDR_ENTITY_TYPE);
        this.addKeyFieldNames("ObjectNumber");
        this.addKeyFieldNames("Counter");
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getObjectNumber() {
        return ObjectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        ObjectNumber = objectNumber;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public BigDecimal getLatitude() {
        return Latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        Latitude = latitude != null ? latitude.setScale(6, BigDecimal.ROUND_HALF_DOWN) : latitude;
    }

    public BigDecimal getLongitude() {
        return Longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        Longitude = longitude != null ? longitude.setScale(6, BigDecimal.ROUND_HALF_DOWN) : longitude;
    }

    public String getPartnerFunction() {
        return PartnerFunction;
    }

    public void setPartnerFunction(String partnerFunction) {
        PartnerFunction = partnerFunction;
    }

    public String getPartner() {
        return Partner;
    }

    public void setPartner(String partner) {
        Partner = partner;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
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

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String toString(boolean shortAdd) {
        String strAddress = null;
        String strAddressSeprator;
        StringBuilder stringBuilder = null;
        try {
            strAddressSeprator = ZConfigManager.ADDRESS_SEPARATOR;
            stringBuilder = new StringBuilder();
            if (shortAdd) {
                stringBuilder.append(Street);
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append(City);
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append(Country);/*stringBuilder.append(strAddressSeprator);
                stringBuilder.append(PostalCode);stringBuilder.append(strAddressSeprator);
                //stringBuilder.append("Street");stringBuilder.append(strAddressSeprator);
                stringBuilder.append(Telephone);stringBuilder.append(strAddressSeprator);*/
                /*stringBuilder.append(City);stringBuilder.append(strAddressSeprator);
                stringBuilder.append(Country);stringBuilder.append(strAddressSeprator);*/
            } else {
                stringBuilder.append("Name");
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append("FlatNum");
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append("HouseNum");
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append("StreetNum");
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append("SocietyName");
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append("Street");
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append("Country");
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append("City");
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append("Region");
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append("Province");
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append("Country");
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append("PostalCode");
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append("Telephone");
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append("Latitude");
                stringBuilder.append(strAddressSeprator);
                stringBuilder.append("Longitude");
                stringBuilder.append(strAddressSeprator);
            }


            /*stringBuilder.append("ObjectNumber");stringBuilder.append(strAddressSeprator);
            stringBuilder.append("Counter");stringBuilder.append(strAddressSeprator);
            stringBuilder.append("PartnerFunction");stringBuilder.append(strAddressSeprator);
            stringBuilder.append("Partner");stringBuilder.append(strAddressSeprator);
            stringBuilder.append("EnteredBy");stringBuilder.append(strAddressSeprator);
            stringBuilder.append("AddressNumber");stringBuilder.append(strAddressSeprator);*/

            strAddress = stringBuilder.toString();

        } catch (Exception e) {

        }
        return strAddress;
    }

    public ResponseObject UpdateAddress() {
        ResponseObject result = null;
        try {
            setMode(ZAppSettings.EntityMode.Update);
            result = SaveToStore(false);
            if (result.isError())
                DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, result.getMessage());

        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

}
