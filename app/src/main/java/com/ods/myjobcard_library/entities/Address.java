package com.ods.myjobcard_library.entities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.supervisor.SupervisorWorkOrder;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 01-07-2016.
 */
public class Address extends BaseEntity {

    private String AddrNumber;
    private String Title;
    private String Name;
    private String City;
    private String PostalCode;
    private String Street;
    private String HouseNum;
    private String Region;
    private String Country;
    private String AddrGroup;
    private String Sort;
    private BigDecimal Latitude;
    private BigDecimal Longitude;
    private String Telephone;
    private String EnteredBy;
    private String GeoLocation;
    public Address() {
        super();
        initializeEntityProperties();
    }
    public Address(ODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }
    public Address(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity);
    }

    //get methods
    public static ResponseObject getWOAddress(String addrNum) {
        ResponseObject result = null;
        String resourcePath = null;// "WoHeaderSet?$expand=NavOpera/OperaToPRT";
        String strAddEntitySet = null;
        try {
            strAddEntitySet = ZCollections.ADDR_COLLECTION;
            resourcePath = strAddEntitySet;
            if (addrNum != null && !addrNum.isEmpty())
                resourcePath += "?$filter=AddrNumber eq '" + addrNum + "'";
            result = DataHelper.getInstance().getEntities(strAddEntitySet, resourcePath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities);
            }
        } catch (Exception e) {
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ResponseObject getNotificationAddress(String addrNum) {
        ResponseObject result = null;
        String resourcePath = null;
        String strAddEntitySet = null;
        try {
            strAddEntitySet = ZCollections.NOTIFICATION_ADDR_COLLECTION;
            resourcePath = strAddEntitySet;
            if (addrNum != null && !addrNum.isEmpty())
                resourcePath += "?$filter=AddrNumber eq '" + addrNum + "'";
            result = DataHelper.getInstance().getEntities(strAddEntitySet, resourcePath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities);
            }
        } catch (Exception e) {
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ResponseObject getSupervisorWOAddress(String addrNum) {
        ResponseObject result = null;
        String resourcePath = null;// "WoHeaderSet?$expand=NavOpera/OperaToPRT";
        String strAddEntitySet = null;
        try {
            strAddEntitySet = ZCollections.SUPERVISOR_ADDRESS_COLLECTIONS;
            resourcePath = strAddEntitySet;
            if (addrNum != null && !addrNum.isEmpty())
                resourcePath += "?$filter=AddrNumber eq '" + addrNum + "'";
            result = DataHelper.getInstance().getEntities(strAddEntitySet, resourcePath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                result = FromEntity(entities);
            }
        } catch (Exception e) {
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<Address> addresses = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    addresses.add(new Address(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", addresses);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(SupervisorWorkOrder.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.ADDR_COLLECTION);
        this.setEntityType(ZCollections.ADDR_ENTITY_TYPE);
        this.addKeyFieldNames("AddrNumber");
    }

    public String getAddrNumber() {
        return AddrNumber;
    }

    public void setAddrNumber(String addrNumber) {
        AddrNumber = addrNumber;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
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

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getHouseNum() {
        return HouseNum;
    }

    public void setHouseNum(String houseNum) {
        HouseNum = houseNum;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getAddrGroup() {
        return AddrGroup;
    }

    public void setAddrGroup(String addrGroup) {
        AddrGroup = addrGroup;
    }

    public String getSort() {
        return Sort;
    }

    public void setSort(String sort) {
        Sort = sort;
    }

    public BigDecimal getLatitude() {
        parsePoint();
        return Latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        Latitude = latitude != null ? latitude.setScale(6, BigDecimal.ROUND_HALF_DOWN) : latitude;
    }

    public BigDecimal getLongitude() {
        parsePoint();
        return Longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        Longitude = longitude != null ? longitude.setScale(6, BigDecimal.ROUND_HALF_DOWN) : longitude;
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

    public String getGeoLocation() {
        return GeoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        GeoLocation = geoLocation;
    }

    public String toString(boolean shortAdd) {
        String strAddress = null;
        String strAddressSeprator;
        StringBuilder stringBuilder = null;
        try {
            strAddressSeprator = ZConfigManager.ADDRESS_SEPARATOR;
            stringBuilder = new StringBuilder();
            if (shortAdd) {
                if (HouseNum != null && !HouseNum.isEmpty()) {
                    stringBuilder.append(HouseNum);
                    stringBuilder.append(strAddressSeprator);
                }
                if (Street != null && !Street.isEmpty()) {
                    stringBuilder.append(Street);
                    stringBuilder.append(strAddressSeprator);
                }
                if (City != null && !City.isEmpty()) {
                    stringBuilder.append(City);
                    stringBuilder.append(strAddressSeprator);
                }
                if (Country != null && !Country.isEmpty()) {
                    stringBuilder.append(Country);
                    stringBuilder.append(strAddressSeprator);
                }
                if (PostalCode != null && !PostalCode.isEmpty()) {
                    stringBuilder.append(PostalCode);
                }/*stringBuilder.append(strAddressSeprator);
                stringBuilder.append(Telephone);stringBuilder.append(strAddressSeprator);*/
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

    public ResponseObject updateAddress() {
        ResponseObject result = null;
        try {
            this.setMode(ZAppSettings.EntityMode.Update);
            if (this.getEntityResourcePath().toLowerCase().contains(ZCollections.SUPERVISOR_ADDRESS_COLLECTIONS.toLowerCase()))
                this.setEntitySetName(ZCollections.SUPERVISOR_ADDRESS_COLLECTIONS);
            result = this.SaveToStore(false);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void parsePoint() {
        try {
            if (GeoLocation != null && !GeoLocation.isEmpty()) {
                String[] coordinatesStr = GeoLocation.split(",");
                if (coordinatesStr.length > 0) {
                    BigDecimal longitude = new BigDecimal(coordinatesStr[0].split(":")[1]);
                    BigDecimal latitude = new BigDecimal(coordinatesStr[1].split(":")[1]);
                    /*if(longitude.doubleValue() != 0 && latitude.doubleValue() != 0)
                    {*/
                    Longitude = longitude;
                    Latitude = latitude;
//                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }
}
