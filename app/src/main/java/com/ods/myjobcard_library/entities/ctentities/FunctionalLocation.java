package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.TreeViewData;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by lenovo on 09-05-2016.
 */
public class FunctionalLocation extends BaseEntity {

    private static FunctionalLocation CURR_FL;
    private ArrayList<FLClassificationSet> classifications;
    private ArrayList<FLMeasurementPoint> measurementPoints;
    private String FunctionalLoc;
    private String PrimaryLang;
    private String StrIndicator;
    private String FunctLocCat;
    private String SupFunctLoc;
    private GregorianCalendar CreatedOn;
    private String CreatedBy;
    private GregorianCalendar ChangedOn;
    private String ChangedBy;
    private GregorianCalendar StartupDate;
    private String RefLocation;
    private String ConstType;
    private String ConsTypeorig;
    private String ManufPartNo;
    private boolean EquipInstall;
    private String PlanningPlant;
    private String PlannerGroup;
    private String ObjectType;
    private String WorkCenter;
    private String CatalogProfile;
    private String LocAccAssmt;
    private String ObjectNumber;
    private String Position;
    private String IScategory;
    private String Premise;
    private String EQObjecttype;
    private String InventoryNo;
    private String SizeDimens;
    private BigDecimal GrossWeight;
    private String WeightUnit;
    private BigDecimal AcquistnValue;
    private String Currency;
    private GregorianCalendar AcquistionDate;
    private String Manufacturer;
    private String ManufCountry;
    private String ConstructYear;
    private String ConstructMth;
    private String ModelNumber;
    private String MPNMaterial;
    private String ManufSerialNo;
    private String Template;
    private String Description;
    private String MainWorkcenter;
    private String WorkcenterText;
    private String SystemStatus;
    private String UserStatus;
    private String SystemStatusCode;
    private String UserStatusCode;
    private String ABCIndicator;
    private String Sortfield;
    private String MaintPlant;
    private String Location;
    private String EnteredBy;
    private String Room;
    private String PlantSection;
    private String PPworkctr;
    private String BusinessArea;
    private String COArea;
    private String CostCenter;
    private String WBSElement;
    private String CompanyCode;
    private String Asset;
    private String Subnumber;
    private String StandgOrder;
    private String SettlementOrder;
    private String AddressNumber;

    public FunctionalLocation() {
    }

    public FunctionalLocation(ODataEntity entity) {
        create(entity);
    }

    public static FunctionalLocation getCurrFl() {
        return CURR_FL;
    }

    public static void setCurrFl(FunctionalLocation currFl) {
        CURR_FL = currFl;
    }

    //get methods
    public static ResponseObject getFuncLocations(int skipValue, int numRecords) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            dataHelper = DataHelper.getInstance();
            String strQuery = ZCollections.FL_COLLECTION + "?$skip=" + skipValue + " &$top=" + numRecords;
            result = dataHelper.getEntities(ZCollections.FL_COLLECTION, strQuery);
            result = FromEntity((List<ODataEntity>) result.Content());
            if (result == null)
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(FunctionalLocation.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
        }
        return result;
    }

    public static ArrayList<FunctionalLocation> getFilteredFunctionalLocations(String searchText, String searchOptions, int skipValue, int numRecords) {
        ArrayList<FunctionalLocation> arrayList = new ArrayList<>();
        ResponseObject result;
        try {
            String resPath = ZCollections.FL_COLLECTION;
            if (searchOptions.equalsIgnoreCase(ZCollections.SEARCH_OPTION_ID))
                resPath += "?$filter=indexof(FunctionalLoc, '" + searchText + "') ne -1&$skip=" + skipValue + "&$top=" + numRecords;
            else if (searchOptions.equalsIgnoreCase(ZCollections.SEARCH_OPTION_DESCRIPTION))
                resPath += "?$filter=indexof(Description, '" + searchText + "') ne -1&$skip=" + skipValue + "&$top=" + numRecords;
            result = DataHelper.getInstance().getEntities(ZCollections.FL_COLLECTION, resPath);
            result = FromEntity((List<ODataEntity>) result.Content());
            if (result != null && !result.isError()) {
                arrayList = (ArrayList<FunctionalLocation>) result.Content();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(FunctionalLocation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return arrayList;
    }

    public static ResponseObject getFuncLocation(String funcLocNum, boolean fetchClassifications) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String resourcePath = ZCollections.FL_COLLECTION;
        if (funcLocNum != null && !funcLocNum.isEmpty()) {
            resourcePath += "('" + funcLocNum + "')";
            try {
                dataHelper = DataHelper.getInstance();
                result = dataHelper.getEntities(ZCollections.FL_COLLECTION, resourcePath);
                if (!result.isError()) {
                    FunctionalLocation fLocation = new FunctionalLocation((ODataEntity) result.Content());
                    if (fetchClassifications) {
                        result = FLClassificationSet.getFLClassificationSet(funcLocNum);
                        result = FromEntity((List<ODataEntity>) result.Content());
                        if (!result.isError())
                            fLocation.classifications = (ArrayList<FLClassificationSet>) result.Content();
                    }
                    result.setContent(fLocation);
                }
            } catch (Exception e) {
                DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
                result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
            }
        }
        return result;
    }

    //helper methods
    public static ArrayList<FunctionalLocation> searchFuncLocations(String searchText, ArrayList<FunctionalLocation> funcLocs, String searchOption) {
        ArrayList<FunctionalLocation> searchedFuncLocs = new ArrayList<FunctionalLocation>();
        if (searchOption.equals(ZCollections.SEARCH_OPTION_ID)) {
            for (FunctionalLocation fLoc : funcLocs) {
                if (fLoc.getFunctionalLoc().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchedFuncLocs.add(fLoc);
                }
            }
        } else if (searchOption.equals(ZCollections.SEARCH_OPTION_DESCRIPTION)) {
            for (FunctionalLocation fLoc : funcLocs) {
                if (fLoc.getDescription().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchedFuncLocs.add(fLoc);
                }
            }
        } else {
            for (FunctionalLocation fLoc : funcLocs) {
                if (fLoc.getFunctionalLoc().trim().toLowerCase().contains(searchText.toLowerCase()) || fLoc.getDescription().trim().toLowerCase().contains(searchText.toLowerCase())) {
                    searchedFuncLocs.add(fLoc);
                }
            }
        }
        return searchedFuncLocs;
    }

    public static ArrayList<FunctionalLocation> getQueriedFunctionalLocations(String searchText, String searchOption) {
        ArrayList<FunctionalLocation> searchedFunLocs = new ArrayList<>();
        DataHelper dataHelper = null;
        ResponseObject result = null;
        try {
            dataHelper = DataHelper.getInstance();
            String strQuery = ZCollections.FL_COLLECTION + "?$filter=indexof(tolower(" + (searchOption.equalsIgnoreCase(ZCollections.SEARCH_OPTION_ID) ? "FunctionalLoc" : "Description") + "),'" + searchText.toLowerCase() + "') ne -1&$select=FunctionalLoc,Description";
            result = dataHelper.getEntities(ZCollections.FL_COLLECTION, strQuery);
            result = FromEntity((List<ODataEntity>) result.Content());
            if (result != null && !result.isError())
                searchedFunLocs = (ArrayList<FunctionalLocation>) result.Content();
        } catch (Exception e) {
            DliteLogger.WriteLog(FunctionalLocation.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
        }
        return searchedFunLocs;
    }

    public static ArrayList<FunctionalLocation> searchSpecificFuncLocation(String searchText, ArrayList<FunctionalLocation> funcLocs, String searchOption) {
        ArrayList<FunctionalLocation> searchFuncLoc = new ArrayList<FunctionalLocation>();
        try {
            if (searchOption.equals(ZCollections.SEARCH_OPTION_ID)) {
                for (FunctionalLocation funcLoc : funcLocs) {
                    if (funcLoc.getFunctionalLoc().trim().toLowerCase().contains(searchText.toLowerCase())) {
                        searchFuncLoc.add(funcLoc);
                    }
                }
            } else if (searchOption.equals(ZCollections.SEARCH_OPTION_DESCRIPTION)) {
                for (FunctionalLocation funcLoc : funcLocs) {
                    if (funcLoc.getDescription().trim().toLowerCase().contains(searchText.toLowerCase())) {
                        searchFuncLoc.add(funcLoc);
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(FunctionalLocation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return searchFuncLoc;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<FunctionalLocation> functionalLocations = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    functionalLocations.add(new FunctionalLocation(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", functionalLocations);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(FunctionalLocation.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public static ArrayList<TreeViewData> loadFLocTreeData() {
        ArrayList<TreeViewData> assetData = new ArrayList<TreeViewData>();
        try {
            String entitySetName = ZCollections.FL_COLLECTION;
            String orderByUrl = "&$orderby=";
            String orderByCriteria = "Description";
            orderByUrl += orderByCriteria;
            String resPath = entitySetName + "?$filter=SupFunctLoc eq null or SupFunctLoc eq ''&$select=Description,FunctionalLoc" + orderByUrl;
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                ArrayList<FunctionalLocation> fLocations = (ArrayList<FunctionalLocation>) FromEntity((List<ODataEntity>) result.Content()).Content();
                if (fLocations != null && fLocations.size() > 0) {
                    for (FunctionalLocation fl : fLocations) {
                        TreeViewData treeData = new TreeViewData(0, fl.getDescription() + " (" + fl.getFunctionalLoc() + ")", fl.getFunctionalLoc(), "", false);
                        assetData.add(treeData);
                        assetData.addAll(getFLocChildrenTreeData(fl.getFunctionalLoc(), 1));
                        assetData.addAll(Equipment.getAssetsForFLoc(fl.getFunctionalLoc(), 1));
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(FunctionalLocation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return assetData;
    }

    public static ArrayList<TreeViewData> getFLocChildrenTreeData(String fLocId, int level) {
        ArrayList<TreeViewData> assetData = new ArrayList<TreeViewData>();
        try {
            String entitySetName = ZCollections.FL_COLLECTION;
            String orderByUrl = "&$orderby=";
            String orderByCriteria = "Description";
            orderByUrl += orderByCriteria;
            String resPath = entitySetName + "?$filter=SupFunctLoc eq '" + fLocId + "'&$select=Description,FunctionalLoc" + orderByUrl;
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                ArrayList<FunctionalLocation> fLocations = (ArrayList<FunctionalLocation>) FromEntity((List<ODataEntity>) result.Content()).Content();
                if (fLocations != null && fLocations.size() > 0) {
                    for (FunctionalLocation fl : fLocations) {
                        TreeViewData treeData = new TreeViewData(level, fl.getDescription() + " (" + fl.getFunctionalLoc() + ")", fl.getFunctionalLoc(), fLocId, false);
                        assetData.add(treeData);
                        int newLevel = level + 1;
                        assetData.addAll(getFLocChildrenTreeData(fl.getFunctionalLoc(), newLevel));
                        assetData.addAll(Equipment.getAssetsForFLoc(fl.getFunctionalLoc(), newLevel));
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(FunctionalLocation.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return assetData;
    }

    public boolean isEquipInstall() {
        return EquipInstall;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getFunctionalLoc() {
        return FunctionalLoc;
    }

    public void setFunctionalLoc(String functionalLoc) {
        FunctionalLoc = functionalLoc;
    }

    public String getPrimaryLang() {
        return PrimaryLang;
    }

    public void setPrimaryLang(String primaryLang) {
        PrimaryLang = primaryLang;
    }

    public String getStrIndicator() {
        return StrIndicator;
    }

    public void setStrIndicator(String strIndicator) {
        StrIndicator = strIndicator;
    }

    public String getFunctLocCat() {
        return FunctLocCat;
    }

    public void setFunctLocCat(String functLocCat) {
        FunctLocCat = functLocCat;
    }

    public String getSupFunctLoc() {
        return SupFunctLoc;
    }

    public void setSupFunctLoc(String supFunctLoc) {
        SupFunctLoc = supFunctLoc;
    }

    public GregorianCalendar getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(GregorianCalendar createdOn) {
        CreatedOn = createdOn;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public GregorianCalendar getChangedOn() {
        return ChangedOn;
    }

    public void setChangedOn(GregorianCalendar changedOn) {
        ChangedOn = changedOn;
    }

    public String getChangedBy() {
        return ChangedBy;
    }

    public void setChangedBy(String changedBy) {
        ChangedBy = changedBy;
    }

    public GregorianCalendar getStartupDate() {
        return StartupDate;
    }

    public void setStartupDate(GregorianCalendar startupDate) {
        StartupDate = startupDate;
    }

    public String getRefLocation() {
        return RefLocation;
    }

    public void setRefLocation(String refLocation) {
        RefLocation = refLocation;
    }

    public String getConstType() {
        return ConstType;
    }

    public void setConstType(String constType) {
        ConstType = constType;
    }

    public String getConsTypeorig() {
        return ConsTypeorig;
    }

    public void setConsTypeorig(String consTypeorig) {
        ConsTypeorig = consTypeorig;
    }

    public String getManufPartNo() {
        return ManufPartNo;
    }

    public void setManufPartNo(String manufPartNo) {
        ManufPartNo = manufPartNo;
    }

    public boolean getEquipInstall() {
        return EquipInstall;
    }

    public void setEquipInstall(boolean equipInstall) {
        EquipInstall = equipInstall;
    }

    public String getPlanningPlant() {
        return PlanningPlant;
    }

    public void setPlanningPlant(String planningPlant) {
        PlanningPlant = planningPlant;
    }

    public String getPlannerGroup() {
        return PlannerGroup;
    }

    public void setPlannerGroup(String plannerGroup) {
        PlannerGroup = plannerGroup;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public String getWorkCenter() {
        return WorkCenter;
    }

    public void setWorkCenter(String workCenter) {
        WorkCenter = workCenter;
    }

    public String getCatalogProfile() {
        return CatalogProfile;
    }

    public void setCatalogProfile(String catalogProfile) {
        CatalogProfile = catalogProfile;
    }

    public String getLocAccAssmt() {
        return LocAccAssmt;
    }

    public void setLocAccAssmt(String locAccAssmt) {
        LocAccAssmt = locAccAssmt;
    }

    public String getObjectNumber() {
        return ObjectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        ObjectNumber = objectNumber;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getIScategory() {
        return IScategory;
    }

    public void setIScategory(String IScategory) {
        this.IScategory = IScategory;
    }

    public String getPremise() {
        return Premise;
    }

    public void setPremise(String premise) {
        Premise = premise;
    }

    public String getEQObjecttype() {
        return EQObjecttype;
    }

    public void setEQObjecttype(String EQObjecttype) {
        this.EQObjecttype = EQObjecttype;
    }

    public String getInventoryNo() {
        return InventoryNo;
    }

    public void setInventoryNo(String inventoryNo) {
        InventoryNo = inventoryNo;
    }

    public String getSizeDimens() {
        return SizeDimens;
    }

    public void setSizeDimens(String sizeDimens) {
        SizeDimens = sizeDimens;
    }

    public BigDecimal getGrossWeight() {
        return GrossWeight;
    }

    public void setGrossWeight(BigDecimal grossWeight) {
        GrossWeight = grossWeight;
    }

    public String getWeightUnit() {
        return WeightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        WeightUnit = weightUnit;
    }

    public BigDecimal getAcquistnValue() {
        return AcquistnValue;
    }

    public void setAcquistnValue(BigDecimal acquistnValue) {
        AcquistnValue = acquistnValue;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public GregorianCalendar getAcquistionDate() {
        return AcquistionDate;
    }

    public void setAcquistionDate(GregorianCalendar acquistionDate) {
        AcquistionDate = acquistionDate;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getManufCountry() {
        return ManufCountry;
    }

    public void setManufCountry(String manufCountry) {
        ManufCountry = manufCountry;
    }

    public String getConstructYear() {
        return ConstructYear;
    }

    public void setConstructYear(String constructYear) {
        ConstructYear = constructYear;
    }

    public String getConstructMth() {
        return ConstructMth;
    }

    public void setConstructMth(String constructMth) {
        ConstructMth = constructMth;
    }

    public String getModelNumber() {
        return ModelNumber;
    }

    public void setModelNumber(String modelNumber) {
        ModelNumber = modelNumber;
    }

    public String getMPNMaterial() {
        return MPNMaterial;
    }

    public void setMPNMaterial(String MPNMaterial) {
        this.MPNMaterial = MPNMaterial;
    }

    public String getManufSerialNo() {
        return ManufSerialNo;
    }

    public void setManufSerialNo(String manufSerialNo) {
        ManufSerialNo = manufSerialNo;
    }

    public String getTemplate() {
        return Template;
    }

    public void setTemplate(String template) {
        Template = template;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMainWorkcenter() {
        return MainWorkcenter;
    }

    public void setMainWorkcenter(String mainWorkcenter) {
        MainWorkcenter = mainWorkcenter;
    }

    public String getWorkcenterText() {
        return WorkcenterText;
    }

    public void setWorkcenterText(String workcenterText) {
        WorkcenterText = workcenterText;
    }

    public String getSystemStatus() {
        return SystemStatus;
    }

    public void setSystemStatus(String systemStatus) {
        SystemStatus = systemStatus;
    }

    public String getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(String userStatus) {
        UserStatus = userStatus;
    }

    public String getSystemStatusCode() {
        return SystemStatusCode;
    }

    public void setSystemStatusCode(String systemStatusCode) {
        SystemStatusCode = systemStatusCode;
    }

    public String getUserStatusCode() {
        return UserStatusCode;
    }

    public void setUserStatusCode(String userStatusCode) {
        UserStatusCode = userStatusCode;
    }

    public String getABCIndicator() {
        return ABCIndicator;
    }

    public void setABCIndicator(String ABCIndicator) {
        this.ABCIndicator = ABCIndicator;
    }

    public String getSortfield() {
        return Sortfield;
    }

    public void setSortfield(String sortfield) {
        Sortfield = sortfield;
    }

    public String getMaintPlant() {
        return MaintPlant;
    }

    public void setMaintPlant(String maintPlant) {
        MaintPlant = maintPlant;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getPlantSection() {
        return PlantSection;
    }

    public void setPlantSection(String plantSection) {
        PlantSection = plantSection;
    }

    public String getPPworkctr() {
        return PPworkctr;
    }

    public void setPPworkctr(String PPworkctr) {
        this.PPworkctr = PPworkctr;
    }

    public String getBusinessArea() {
        return BusinessArea;
    }

    public void setBusinessArea(String businessArea) {
        BusinessArea = businessArea;
    }

    public String getCOArea() {
        return COArea;
    }

    public void setCOArea(String COArea) {
        this.COArea = COArea;
    }

    public String getCostCenter() {
        return CostCenter;
    }

    public void setCostCenter(String costCenter) {
        CostCenter = costCenter;
    }

    public String getWBSElement() {
        return WBSElement;
    }

    public void setWBSElement(String WBSElement) {
        this.WBSElement = WBSElement;
    }

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        CompanyCode = companyCode;
    }

    public String getAsset() {
        return Asset;
    }

    public void setAsset(String asset) {
        Asset = asset;
    }

    public String getSubnumber() {
        return Subnumber;
    }

    public void setSubnumber(String subnumber) {
        Subnumber = subnumber;
    }

    public String getStandgOrder() {
        return StandgOrder;
    }

    public void setStandgOrder(String standgOrder) {
        StandgOrder = standgOrder;
    }

    public String getSettlementOrder() {
        return SettlementOrder;
    }

    public void setSettlementOrder(String settlementOrder) {
        SettlementOrder = settlementOrder;
    }

    public String getAddressNumber() {
        return AddressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        AddressNumber = addressNumber;
    }

    public ArrayList<FLClassificationSet> getClassifications() {
        return classifications;
    }

    public ArrayList<FLMeasurementPoint> getMeasurementPoints() {
        return measurementPoints;
    }
}
