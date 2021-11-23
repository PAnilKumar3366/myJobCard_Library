package com.ods.myjobcard_library.entities.ctentities;

import android.os.Build;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.myjobcard_library.entities.assettree.TreeViewData;
import com.ods.myjobcard_library.entities.highvolume.AssetHierarchy;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lenovo on 05-05-2016.
 */
public class Equipment extends ZBaseEntity {

    private static Equipment CURR_EQUIPMENT;
    private ArrayList<EquipmentClassificationSet> classifications;
    private ZAppSettings.MobileStatus EquipmentStatus;
    private String Equipment;
    private GregorianCalendar CreatedOn;
    private String CreatedBy;
    private String PrimaryLang;
    private GregorianCalendar ChangedOn;
    private String ChangedBy;
    private String AuthorizGroup;
    private String EquipCategory;
    private String ObjectType;
    private String InventoryNo;
    private String SizeDimens;
    private BigDecimal Weight;
    private String UnitofWeight;
    private GregorianCalendar AcquistionDate;
    private BigDecimal AcquistnValue;
    private String Currency;
    private String Vendor;
    private GregorianCalendar WarrantyEnd;
    private BigDecimal ReplValue;
    private String Manufacturer;
    private String ManufCountry;
    private String Drawingno;
    private String ManufSerialNo;
    private String ModelNumber;
    private String ConstructYear;
    private String ConstructMth;
    private String Tasklist;
    private GregorianCalendar DeliveryDate;
    private GregorianCalendar StartupDate;
    private GregorianCalendar WarrantyDate;
    private GregorianCalendar Date;
    private String Note;
    private String Field;
    private String ObjectNumber;
    private String Number;
    private String Material;
    private String Serialnumber;
    private String Plant;
    private String StorLocation;
    private String Charge;
    private String CurCustomer;
    private String MaintenancePlan;
    private String MeasuringPoint;
    private String RevisionLevel;
    private String MasterWarranty;
    private boolean EquipmentData;
    private boolean Configuration;
    private boolean Sales;
    private boolean ProdResTools;
    private String Division;
    private String MPNMaterial;
    private GregorianCalendar ValidFrom;
    private String PlanningPlant;
    private String ConstType;
    private String ManufPartNo;
    private String SuperiorEquipment;
    private String Position;
    private String PlannerGroup;
    private String ObjectTypes;
    private String PlantWorkCenter;
    private String LocAccAssmt;
    private String Customer;
    private String Operator;
    private String LicenseNo;
    private String CatalogProfile;
    private String PhysInvDoc;
    private GregorianCalendar DocumentDate;
    private String Language;
    private String EquipDescription;
    private boolean EquipLongText;
    private String FuncLocation;
    private String ABCIndicator;
    private String MaintPlant;
    private String Location;
    private String Room;
    private String PlantSection;
    private String PPWorkCtr;
    private String BusinessArea;
    private String COArea;
    private String CostCenter;
    private String WBSElement;
    private String CompanyCode;
    private String Asset;
    private String SubNumber;
    private String SettlementOrder;
    private String AddNumber;
    private String WorkCenter;
    private String SystemStatus;
    private String UserStatus;
    private String SystemStatusCode;
    private String UserStatusCode;
    private String StockType;
    private String StockBatch;
    private String SpecialStock;
    private String StockCustomer;
    private String StockVendor;
    private String BomFlag;
    private String UpdateFlag;
    private String TechIdentNo;
    private GregorianCalendar CusWarrantyStartDate;
    private GregorianCalendar CusWarrantyEndDate;
    private String CusWarrantyStatus;
    private GregorianCalendar VenWarrantyStartDate;
    private GregorianCalendar VenWarrantyEndDate;
    private String CusMasterWarranty;
    private String CusWarrantyType;
    private String VenWarrantyType;
    private String VenMasterWarranty;
    private String VenWarrantyStatus;
    private String StandingOrder;
    private String EnteredBy;

    public Equipment() {
        initializingEntityProperties();
    }

    public Equipment(ODataEntity entity) {
        initializingEntityProperties();
        create(entity);
    }

    public static Equipment getCurrEquipment() {
        return CURR_EQUIPMENT;
    }

    public static void setCurrEquipment(Equipment currEquipment) {
        CURR_EQUIPMENT = currEquipment;
    }

    public static ArrayList<Equipment> getInstalledEquipments(String superiorEquipId, String funcLocationId) {
        DataHelper dataHelper = null;
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);
        ArrayList<Equipment> installedEqps = new ArrayList<>();
        String resPath = ZCollections.EQUIPMENT_COLLECTION;
        try {
            if (superiorEquipId != null && !superiorEquipId.isEmpty())
                resPath += "?$filter=SuperiorEquipment eq '" + superiorEquipId + "' and SystemStatus eq '" + ZAppSettings.MobileStatus.ASSOCIATEDTOEQUIPMENT.getMobileStatusCode() + "'&$select=Equipment,EquipDescription,ManufSerialNo,SystemStatus,WorkCenter,MaintPlant";
            else if (funcLocationId != null && !funcLocationId.isEmpty())
                resPath += "?$filter=FuncLocation eq '" + funcLocationId + "' and SystemStatus eq '" + ZAppSettings.MobileStatus.INSTALLED.getMobileStatusCode() + "'&$select=Equipment,EquipDescription,ManufSerialNo,SystemStatus,WorkCenter,MaintPlant";
            else
                return installedEqps;
            dataHelper = DataHelper.getInstance();
            result = dataHelper.getEntities(ZCollections.EQUIPMENT_COLLECTION, resPath);
            result = FromEntity((List<ODataEntity>) result.Content());
            if (result != null && !result.isError())
                installedEqps = (ArrayList<Equipment>) result.Content();
        } catch (Exception e) {
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return installedEqps;
    }

    public static ArrayList<Equipment> getAvailableEquipments(String funcLocation, String plant, String workCenter, String equipCategory, String description) {
        ArrayList<Equipment> equipments = new ArrayList<>();
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String strQuery;
        String filterQry = "?$filter=(SystemStatus ne '" + ZAppSettings.MobileStatus.INSTALLED.getMobileStatusCode() + "' and SystemStatus ne '" + ZAppSettings.MobileStatus.ASSOCIATEDTOEQUIPMENT.getMobileStatusCode() + "')";
        String selectQry = "&$select=FuncLocation,Equipment,EquipDescription";
        try {
            dataHelper = DataHelper.getInstance();
            strQuery = ZCollections.EQUIPMENT_COLLECTION;
            String finalFilterQry = filterQry;
            if (funcLocation != null && !funcLocation.isEmpty())
                finalFilterQry += " and (FuncLocation eq '" + funcLocation + "'";
            if (plant != null && !plant.isEmpty())
                finalFilterQry += (finalFilterQry.equals(filterQry) ? " and (" : " and ") + "MaintPlant eq '" + plant + "'";
            if (equipCategory != null && !equipCategory.isEmpty())
                finalFilterQry += (finalFilterQry.equals(filterQry) ? " and (" : " and ") + "EquipCategory eq '" + equipCategory + "'";
            if (description != null && !description.isEmpty())
                finalFilterQry += (finalFilterQry.equals(filterQry) ? " and (" : " and ") + "indexof(EquipDescription,'" + description + "') gt 0";
            if (workCenter != null && !workCenter.isEmpty())
                finalFilterQry += (finalFilterQry.equals(filterQry) ? " and (" : " or ") + "WorkCenter eq '" + workCenter + "'";

            finalFilterQry += finalFilterQry.equals(filterQry) ? "" : ")";
            strQuery += finalFilterQry + selectQry + "&$orderby=Equipment asc";

            result = dataHelper.getEntities(ZCollections.EQUIPMENT_COLLECTION, strQuery);
            result = FromEntity((List<ODataEntity>) result.Content());
            if (result != null && !result.isError())
                equipments = (ArrayList<Equipment>) result.Content();
        } catch (Exception e) {
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return equipments;
    }

    public static ArrayList<Equipment> getFilteredEquipments(String searchText, String searchOptions, int skipValue, int numRecords, String functionalLocation) {
        ArrayList<Equipment> arrayList = new ArrayList<>();
        ResponseObject result;
        try {
            String resPath = ZCollections.EQUIPMENT_COLLECTION;
            if (searchOptions.equalsIgnoreCase(ZCollections.SEARCH_OPTION_ID))
                resPath += "?$filter=(indexof(tolower(Equipment), '" + searchText.toLowerCase() + "') ne -1)";
            else if (searchOptions.equalsIgnoreCase(ZCollections.SEARCH_OPTION_DESCRIPTION))
                resPath += "?$filter=(indexof(tolower(EquipDescription), '" + searchText.toLowerCase() + "') ne -1)";
            else if (searchOptions.equalsIgnoreCase(ZCollections.SEARCH_OPTION_TECH_ID))
                resPath += "?$filter=(indexof(tolower(TechIdentNo), '" + searchText.toLowerCase() + "') ne -1)";
            if (functionalLocation != null && !functionalLocation.isEmpty()) {
                resPath = resPath.substring(0, resPath.lastIndexOf(")")) + " and tolower(FuncLocation) eq '" + functionalLocation.toLowerCase() + "')";
            }
            resPath += "&$orderby=Equipment asc&$skip=" + skipValue + "&$top=" + numRecords;
            result = DataHelper.getInstance().getEntities(ZCollections.EQUIPMENT_COLLECTION, resPath);
            result = FromEntity((List<ODataEntity>) result.Content());
            if (result != null && !result.isError()) {
                arrayList = (ArrayList<Equipment>) result.Content();
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return arrayList;
    }

    public static ResponseObject getEquipments(int skipValue, int numRecords, String funcLocation) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String resPath = "";
        try {
            dataHelper = DataHelper.getInstance();
            String entitySet = ZCollections.EQUIPMENT_COLLECTION;
            resPath = entitySet;
            if (funcLocation != null && !funcLocation.isEmpty())
                resPath += "?$filter=tolower(FuncLocation) eq '" + funcLocation.toLowerCase() + "'";
            if(numRecords > 0){
                resPath += (resPath.equalsIgnoreCase(entitySet) ? "?" : "&");
                resPath += ("$skip=" + skipValue + " &$top=" + numRecords);
            }
            resPath += (resPath.equalsIgnoreCase(entitySet) ? "?" : "&") + "$orderby=Equipment asc";
            result = dataHelper.getEntities(entitySet, resPath);
            result = FromEntity(ZBaseEntity.setODataEntityList(result.Content()));
            if (result == null)
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
        }
        return result;
    }

    public static ResponseObject getEquipmentsForSpinner(int skipValue, int numRecords) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String strQuery;
        try {
            dataHelper = DataHelper.getInstance();
            strQuery = ZCollections.EQUIPMENT_COLLECTION + "?$orderby=Equipment asc&$select=Equipment,EquipDescription&$skip=" + skipValue + " &$top=" + numRecords;
            result = dataHelper.getEntities(ZCollections.EQUIPMENT_COLLECTION, strQuery);
            if (!result.isError()) {
                result = FromEntity((List<ODataEntity>) result.Content());
                ArrayList<Equipment> equipments = (ArrayList<Equipment>) result.Content();
                ArrayList<SpinnerItem> items = new ArrayList<>();
                SpinnerItem item;
                for (Equipment eqp : equipments) {
                    item = new SpinnerItem();
                    item.setId(eqp.getEquipment());
                    item.setDescription(eqp.getEquipDescription());
                    items.add(item);
                }
                result.setContent(items);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
        }
        return result;
    }

    //helper methods
    public static ArrayList<Equipment> searchEquipments(final String searchText, ArrayList<Equipment> equipments, String searchOption) {
        ArrayList<Equipment> searchedEquips = new ArrayList<Equipment>();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (searchOption.equals(ZCollections.SEARCH_OPTION_ID))
                    return searchedEquips = (ArrayList<Equipment>) equipments.stream().filter(equipment -> equipment.getEquipment().trim().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
                if (searchOption.equals(ZCollections.SEARCH_OPTION_DESCRIPTION))
                    return searchedEquips = (ArrayList<Equipment>) equipments.stream().filter(equipment -> equipment.getEquipDescription().trim().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
                if (searchOption.equals(ZCollections.SEARCH_OPTION_TECH_ID))
                    return searchedEquips = (ArrayList<Equipment>) equipments.stream().filter(equipment -> equipment.getTechIdentNo().trim().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
                else
                    return searchedEquips = (ArrayList<Equipment>) equipments.stream().filter(equipment -> equipment.getTechIdentNo().trim().toLowerCase().contains(searchText.toLowerCase()) || equipment.getEquipment().trim().toLowerCase().contains(searchText.toLowerCase()) || equipment.getEquipDescription().trim().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
            } else if (searchOption.equals(ZCollections.SEARCH_OPTION_ID)) {
                for (Equipment equip : equipments) {
                    if (equip.getEquipment().trim().toLowerCase().contains(searchText.toLowerCase())) {
                        searchedEquips.add(equip);
                    }
                }
            } else if (searchOption.equals(ZCollections.SEARCH_OPTION_DESCRIPTION)) {
                for (Equipment equip : equipments) {
                    if (equip.getEquipDescription().trim().toLowerCase().contains(searchText.toLowerCase())) {
                        searchedEquips.add(equip);
                    }
                }
            } else if (searchOption.equals(ZCollections.SEARCH_OPTION_TECH_ID)) {
                for (Equipment equip : equipments) {
                    if (equip.getTechIdentNo().trim().toLowerCase().contains(searchText.toLowerCase())) {
                        searchedEquips.add(equip);
                    }
                }
            } else {
                for (Equipment equip : equipments) {
                    if (equip.getEquipment().trim().toLowerCase().contains(searchText.toLowerCase()) || equip.getEquipDescription().trim().toLowerCase().contains(searchText.toLowerCase())) {
                        searchedEquips.add(equip);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return searchedEquips = new ArrayList<>();
        }
        return searchedEquips;
    }

    public static ArrayList<Equipment> getQueriedEquipments(String searchText, String searchOption, String funcLocation) {
        ArrayList<Equipment> equipments = new ArrayList<>();
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String strQuery;
        String filterQry = "?$filter=";
        try {
            dataHelper = DataHelper.getInstance();
            strQuery = ZCollections.EQUIPMENT_COLLECTION;
            String finalFilterQry = filterQry;
            if (funcLocation != null && !funcLocation.isEmpty())
                finalFilterQry += "tolower(FuncLocation) eq '" + funcLocation.toLowerCase() + "' and ";
            if (searchOption.equalsIgnoreCase(ZCollections.SEARCH_OPTION_ID))
                finalFilterQry += "(indexof(tolower(Equipment), '" + searchText.toLowerCase() + "') ne -1)";
            else if (searchOption.equalsIgnoreCase(ZCollections.SEARCH_OPTION_DESCRIPTION))
                finalFilterQry += "(indexof(tolower(EquipDescription), '" + searchText.toLowerCase() + "') ne -1)";
            else if (searchOption.equalsIgnoreCase(ZCollections.SEARCH_OPTION_TECH_ID))
                finalFilterQry += "(indexof(tolower(TechIdentNo), '" + searchText.toLowerCase() + "') ne -1)";

            //finalFilterQry += "indexof(tolower("+ (searchOption.equalsIgnoreCase(Collections.SEARCH_OPTION_ID) ? "Equipment" : searchOption.equalsIgnoreCase(Collections.SEARCH_OPTION_DESCRIPTION)?"EquipDescription": searchOption.equalsIgnoreCase(Collections.SEARCH_OPTION_TECH_ID)?"TechIdentNo":"" +"),'"+ searchText.toLowerCase() +"') ne -1&$select=Equipment,EquipDescription,TechIdentNo");
            //finalFilterQry += "indexof(tolower("+ (searchOption.equalsIgnoreCase(Collections.SEARCH_OPTION_ID) ? "Equipment" : "EquipDescription") +"),'"+ searchText.toLowerCase() +"') ne -1&$select=Equipment,EquipDescription";
            strQuery += finalFilterQry + "&$orderby=Equipment asc";

            result = dataHelper.getEntities(ZCollections.EQUIPMENT_COLLECTION, strQuery);
            result = FromEntity((List<ODataEntity>) result.Content());
            if (result != null && !result.isError())
                equipments = (ArrayList<Equipment>) result.Content();
        } catch (Exception e) {
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return equipments;
    }

    public static ResponseObject getEquipment(String eqpNum, boolean fetchClassifications) {
        DataHelper dataHelper = null;
        ResponseObject result = null;
        String resourcePath = ZCollections.EQUIPMENT_COLLECTION;
        if (eqpNum != null && !eqpNum.isEmpty()) {
            resourcePath += "('" + eqpNum + "')";
            try {
                dataHelper = DataHelper.getInstance();
                result = dataHelper.getEntities(ZCollections.EQUIPMENT_COLLECTION, resourcePath);
                if (!result.isError()) {
                    Equipment equipment = new Equipment((ODataEntity) result.Content());
                    if (fetchClassifications) {
                        result = EquipmentClassificationSet.getEquipmentClassificationSet(eqpNum);
                        if (!result.isError())
                            equipment.classifications = (ArrayList<EquipmentClassificationSet>) result.Content();
                    }

                    result.setContent(equipment);
                }
            } catch (Exception e) {
                DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
                result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
            }
        }
        return result;
    }

    public static Equipment searchSpecificEquipment(String equipNum, ArrayList<Equipment> equipments) {
        for (Equipment equip : equipments) {
            if (equip.getEquipment().trim().toLowerCase().equals(equipNum.toLowerCase())) {
                return equip;
            }
        }
        return null;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = null;
        try {
            if (entities != null) {
                ArrayList<Equipment> equipments = new ArrayList<>();
                for (ODataEntity entity : entities) {
                    equipments.add(new Equipment(entity));
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "", equipments);
            } else
                result = new ResponseObject(ZConfigManager.Status.Error);
        } catch (Exception e) {
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result = new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), e);
        }
        return result;
    }

    public static ArrayList<TreeViewData> loadAssetTreeData() {
        ArrayList<TreeViewData> assetData = new ArrayList<TreeViewData>();
        try {
            String entitySetName = ZCollections.EQUIPMENT_COLLECTION;
            String orderByUrl = "&$orderby=";
            String orderByCriteria = "EquipDescription";
            orderByUrl += orderByCriteria;
            String resPath = entitySetName + "?$filter=(FuncLocation eq null or FuncLocation eq '') and (SuperiorEquipment eq null or SuperiorEquipment eq '')&$select=EquipDescription,Equipment" + orderByUrl;
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                ArrayList<Equipment> equipments = (ArrayList<Equipment>) FromEntity((List<ODataEntity>) result.Content()).Content();
                if (equipments != null && equipments.size() > 0) {
                    for (Equipment equipment : equipments) {
                        TreeViewData treeData = new TreeViewData(0, equipment.getEquipDescription() + " (" + equipment.getEquipment() + ")", equipment.getEquipment(), "", true);
                        assetData.add(treeData);
                        assetData.addAll(getAssetsForSuperEquipment(equipment.getEquipment(), 1));
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return assetData;
    }

    public static ArrayList<TreeViewData> getAssetsForSuperEquipment(String superEqpId, int level) {
        ArrayList<TreeViewData> assetData = new ArrayList<TreeViewData>();
        try {
            String entitySetName = ZCollections.EQUIPMENT_COLLECTION;
            String orderByUrl = "&$orderby=";
            String orderByCriteria = "EquipDescription";
            orderByUrl += orderByCriteria;
            String resPath = entitySetName + "?$filter=(SuperiorEquipment eq '" + superEqpId + "')&$select=EquipDescription,Equipment" + orderByUrl;
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                ArrayList<Equipment> equipments = (ArrayList<Equipment>) FromEntity((List<ODataEntity>) result.Content()).Content();
                if (equipments != null && equipments.size() > 0) {
                    for (Equipment equipment : equipments) {
                        TreeViewData treeData = new TreeViewData(level, equipment.getEquipDescription() + " (" + equipment.getEquipment() + ")", equipment.getEquipment(), superEqpId, true);
                        assetData.add(treeData);
                        int newLevel = level + 1;
                        assetData.addAll(getAssetsForSuperEquipment(equipment.getEquipment(), newLevel));
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return assetData;
    }

    public static ArrayList<TreeViewData> getAssetsForFLoc(String fLocId, int level) {
        ArrayList<TreeViewData> assetData = new ArrayList<TreeViewData>();
        try {
            String entitySetName = ZCollections.EQUIPMENT_COLLECTION;
            String orderByUrl = "&$orderby=";
            String orderByCriteria = "EquipDescription";
            orderByUrl += orderByCriteria;
            String resPath = entitySetName + "?$filter=(FuncLocation eq '" + fLocId + "') and (SuperiorEquipment eq null or SuperiorEquipment eq '')&$select=EquipDescription,Equipment" + orderByUrl;
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                ArrayList<Equipment> equipments = (ArrayList<Equipment>) FromEntity((List<ODataEntity>) result.Content()).Content();
                if (equipments != null && equipments.size() > 0) {
                    for (Equipment equipment : equipments) {
                        TreeViewData treeData = new TreeViewData(level, equipment.getEquipDescription() + " (" + equipment.getEquipment() + ")", equipment.getEquipment(), fLocId, true);
                        assetData.add(treeData);
                        int newLevel = level + 1;
                        assetData.addAll(getAssetsForSuperEquipment(equipment.getEquipment(), newLevel));
                    }
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return assetData;
    }

    public static int getEquipmentChildrenCount(String parentEquipmentId){
        int count = 0;
        try{
            String entitySetName = ZCollections.EQUIPMENT_COLLECTION;
            String resPath = entitySetName + "/$count?$filter=(SuperiorEquipment eq '" + parentEquipmentId + "')";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                count = Integer.parseInt(String.valueOf(result.Content()));
            }
        } catch (Exception e){
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return count;
    }

    public static ArrayList<AssetHierarchy> getEquipmentChildren(String parentEquipmentId){
        ArrayList<AssetHierarchy> children = new ArrayList<>();
        try {
            String entitySetName = ZCollections.EQUIPMENT_COLLECTION;
            String resPath = entitySetName + "?$filter=(SuperiorEquipment eq '" + parentEquipmentId + "')&$orderby=Equipment asc&$select=EquipDescription,Equipment";
            ResponseObject result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (result != null && !result.isError()) {
                List<ODataEntity> entities = ZBaseEntity.setODataEntityList(result.Content());
                String description, eqp;
                for (ODataEntity entity : entities) {
                    eqp = String.valueOf(entity.getProperties().get("Equipment").getValue());
                    description = String.valueOf(entity.getProperties().get("EquipDescription").getValue());
                    AssetHierarchy hierarchy = new AssetHierarchy();
                    hierarchy.setDescription(description);
                    hierarchy.setObjectId(eqp);
                    hierarchy.setType("EQ");
                    hierarchy.setParentId(parentEquipmentId);
                    children.add(hierarchy);
                }
            }
        } catch (Exception e){
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return children;
    }

    public static String warrantyInfoMsg(String cusWarrantyStatus, String venWarrantyStatus) {
        String msg = "No Equipment Warranty Info is Available";
        boolean IsCusWarranty = false, IsVenWarranty = false;
        try {
            if (cusWarrantyStatus == null && venWarrantyStatus == null)
                return msg;
            if (cusWarrantyStatus != null && cusWarrantyStatus.isEmpty() && venWarrantyStatus != null && venWarrantyStatus.isEmpty())
                return msg;
            if (cusWarrantyStatus != null)
                if (cusWarrantyStatus.equalsIgnoreCase("Active"))
                    IsCusWarranty = true;
            if (venWarrantyStatus != null)
                if (venWarrantyStatus.equalsIgnoreCase("Active"))
                    IsVenWarranty = true;
            if (IsCusWarranty && IsVenWarranty)
                return msg = " Equipment has both Customer and Vendor Warranties active";
            if (!IsCusWarranty && !IsVenWarranty)
                return msg = "Equipment Warranty has Expired";
            if (IsCusWarranty)
                return msg = "Equipment Customer Warranty is available and Vendor Warranty is Expired";
            if (IsVenWarranty)
                return msg = "Equipment Vendor Warranty is Available and Customer Warranty has Expired";
            //return msg="Equipment Warranty has Expired";
        } catch (Exception e) {
            DliteLogger.WriteLog(Equipment.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return msg;
        }
        return msg;
    }

    public boolean isEquipmentData() {
        return EquipmentData;
    }

    public boolean isConfiguration() {
        return Configuration;
    }

    public boolean isSales() {
        return Sales;
    }

    public boolean isProdResTools() {
        return ProdResTools;
    }

    public String getStandingOrder() {
        return StandingOrder;
    }

    public void setStandingOrder(String standingOrder) {
        StandingOrder = standingOrder;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    private void initializingEntityProperties() {
        this.setEntityType(ZCollections.EQUIPMENT_ENTITY_TYPE);
        this.setEntitySetName(ZCollections.EQUIPMENT_COLLECTION);
        this.setEntityResourcePath(ZCollections.EQUIPMENT_COLLECTION);
        this.addKeyFieldNames("Equipment");
    }

    public GregorianCalendar getCusWarrantyStartDate() {
        return CusWarrantyStartDate;
    }

    public void setCusWarrantyStartDate(GregorianCalendar cusWarrantyStartDate) {
        CusWarrantyStartDate = cusWarrantyStartDate;
    }

    public GregorianCalendar getCusWarrantyEndDate() {
        return CusWarrantyEndDate;
    }

    public void setCusWarrantyEndDate(GregorianCalendar cusWarrantyEndDate) {
        CusWarrantyEndDate = cusWarrantyEndDate;
    }

    public String getCusWarrantyStatus() {
        return CusWarrantyStatus;
    }

    public void setCusWarrantyStatus(String cusWarrantyStatus) {
        CusWarrantyStatus = cusWarrantyStatus;
    }

    public GregorianCalendar getVenWarrantyStartDate() {
        return VenWarrantyStartDate;
    }

    public void setVenWarrantyStartDate(GregorianCalendar venWarrantyStartDate) {
        VenWarrantyStartDate = venWarrantyStartDate;
    }

    public GregorianCalendar getVenWarrantyEndDate() {
        return VenWarrantyEndDate;
    }

    public void setVenWarrantyEndDate(GregorianCalendar venWarrantyEndDate) {
        VenWarrantyEndDate = venWarrantyEndDate;
    }

    public String getCusMasterWarranty() {
        return CusMasterWarranty;
    }

    public void setCusMasterWarranty(String cusMasterWarranty) {
        CusMasterWarranty = cusMasterWarranty;
    }

    public String getCusWarrantyType() {
        return CusWarrantyType;
    }

    public void setCusWarrantyType(String cusWarrantyType) {
        CusWarrantyType = cusWarrantyType;
    }

    public String getVenWarrantyType() {
        return VenWarrantyType;
    }

    public void setVenWarrantyType(String venWarrantyType) {
        VenWarrantyType = venWarrantyType;
    }

    public String getVenMasterWarranty() {
        return VenMasterWarranty;
    }

    public void setVenMasterWarranty(String venMasterWarranty) {
        VenMasterWarranty = venMasterWarranty;
    }

    public String getVenWarrantyStatus() {
        return VenWarrantyStatus;
    }

    public void setVenWarrantyStatus(String venWarrantyStatus) {
        VenWarrantyStatus = venWarrantyStatus;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
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

    public String getPrimaryLang() {
        return PrimaryLang;
    }

    public void setPrimaryLang(String primaryLang) {
        PrimaryLang = primaryLang;
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

    public String getAuthorizGroup() {
        return AuthorizGroup;
    }

    public void setAuthorizGroup(String authorizGroup) {
        AuthorizGroup = authorizGroup;
    }

    public String getEquipCategory() {
        return EquipCategory;
    }

    public void setEquipCategory(String equipCategory) {
        EquipCategory = equipCategory;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
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

    public BigDecimal getWeight() {
        return Weight;
    }

    public void setWeight(BigDecimal weight) {
        Weight = weight;
    }

    public String getUnitofWeight() {
        return UnitofWeight;
    }

    public void setUnitofWeight(String unitofWeight) {
        UnitofWeight = unitofWeight;
    }

    public GregorianCalendar getAcquistionDate() {
        return AcquistionDate;
    }

    public void setAcquistionDate(GregorianCalendar acquistionDate) {
        AcquistionDate = acquistionDate;
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

    public String getVendor() {
        return Vendor;
    }

    public void setVendor(String vendor) {
        Vendor = vendor;
    }

    public GregorianCalendar getWarrantyEnd() {
        return WarrantyEnd;
    }

    public void setWarrantyEnd(GregorianCalendar warrantyEnd) {
        WarrantyEnd = warrantyEnd;
    }

    public BigDecimal getReplValue() {
        return ReplValue;
    }

    public void setReplValue(BigDecimal replValue) {
        ReplValue = replValue;
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

    public String getDrawingno() {
        return Drawingno;
    }

    public void setDrawingno(String drawingno) {
        Drawingno = drawingno;
    }

    public String getManufSerialNo() {
        return ManufSerialNo;
    }

    public void setManufSerialNo(String manufSerialNo) {
        ManufSerialNo = manufSerialNo;
    }

    public String getModelNumber() {
        return ModelNumber;
    }

    public void setModelNumber(String modelNumber) {
        ModelNumber = modelNumber;
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

    public String getTasklist() {
        return Tasklist;
    }

    public void setTasklist(String tasklist) {
        Tasklist = tasklist;
    }

    public GregorianCalendar getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(GregorianCalendar deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public GregorianCalendar getStartupDate() {
        return StartupDate;
    }

    public void setStartupDate(GregorianCalendar startupDate) {
        StartupDate = startupDate;
    }

    public GregorianCalendar getWarrantyDate() {
        return WarrantyDate;
    }

    public void setWarrantyDate(GregorianCalendar warrantyDate) {
        WarrantyDate = warrantyDate;
    }

    public GregorianCalendar getDate() {
        return Date;
    }

    public void setDate(GregorianCalendar date) {
        Date = date;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getField() {
        return Field;
    }

    public void setField(String field) {
        Field = field;
    }

    public String getObjectNumber() {
        return ObjectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        ObjectNumber = objectNumber;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String material) {
        Material = material;
    }

    public String getSerialnumber() {
        return Serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        Serialnumber = serialnumber;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getStorLocation() {
        return StorLocation;
    }

    public void setStorLocation(String storLocation) {
        StorLocation = storLocation;
    }

    public String getCharge() {
        return Charge;
    }

    public void setCharge(String charge) {
        Charge = charge;
    }

    public String getCurCustomer() {
        return CurCustomer;
    }

    public void setCurCustomer(String curCustomer) {
        CurCustomer = curCustomer;
    }

    public String getMaintenancePlan() {
        return MaintenancePlan;
    }

    public void setMaintenancePlan(String maintenancePlan) {
        MaintenancePlan = maintenancePlan;
    }

    public String getMeasuringPoint() {
        return MeasuringPoint;
    }

    public void setMeasuringPoint(String measuringPoint) {
        MeasuringPoint = measuringPoint;
    }

    public String getRevisionLevel() {
        return RevisionLevel;
    }

    public void setRevisionLevel(String revisionLevel) {
        RevisionLevel = revisionLevel;
    }

    public String getMasterWarranty() {
        return MasterWarranty;
    }

    public void setMasterWarranty(String masterWarranty) {
        MasterWarranty = masterWarranty;
    }

    public boolean getEquipmentData() {
        return EquipmentData;
    }

    public void setEquipmentData(boolean equipmentData) {
        EquipmentData = equipmentData;
    }

    public boolean getConfiguration() {
        return Configuration;
    }

    public void setConfiguration(boolean configuration) {
        Configuration = configuration;
    }

    public boolean getSales() {
        return Sales;
    }

    public void setSales(boolean sales) {
        Sales = sales;
    }

    public boolean getProdResTools() {
        return ProdResTools;
    }

    public void setProdResTools(boolean prodResTools) {
        ProdResTools = prodResTools;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }

    public String getMPNMaterial() {
        return MPNMaterial;
    }

    public void setMPNMaterial(String MPNMaterial) {
        this.MPNMaterial = MPNMaterial;
    }

    public GregorianCalendar getValidFrom() {
        return ValidFrom;
    }

    public void setValidFrom(GregorianCalendar validFrom) {
        ValidFrom = validFrom;
    }

    public String getPlanningPlant() {
        return PlanningPlant;
    }

    public void setPlanningPlant(String planningPlant) {
        PlanningPlant = planningPlant;
    }

    public String getConstType() {
        return ConstType;
    }

    public void setConstType(String constType) {
        ConstType = constType;
    }

    public String getManufPartNo() {
        return ManufPartNo;
    }

    public void setManufPartNo(String manufPartNo) {
        ManufPartNo = manufPartNo;
    }

    public String getSuperiorEquipment() {
        return SuperiorEquipment;
    }

    public void setSuperiorEquipment(String superiorEquipment) {
        SuperiorEquipment = superiorEquipment;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getPlannerGroup() {
        return PlannerGroup;
    }

    public void setPlannerGroup(String plannerGroup) {
        PlannerGroup = plannerGroup;
    }

    public String getObjectTypes() {
        return ObjectTypes;
    }

    public void setObjectTypes(String objectTypes) {
        ObjectTypes = objectTypes;
    }

    public String getPlantWorkCenter() {
        return PlantWorkCenter;
    }

    public void setPlantWorkCenter(String plantWorkCenter) {
        PlantWorkCenter = plantWorkCenter;
    }

    public String getLocAccAssmt() {
        return LocAccAssmt;
    }

    public void setLocAccAssmt(String locAccAssmt) {
        LocAccAssmt = locAccAssmt;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        Customer = customer;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public String getLicenseNo() {
        return LicenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        LicenseNo = licenseNo;
    }

    public String getCatalogProfile() {
        return CatalogProfile;
    }

    public void setCatalogProfile(String catalogProfile) {
        CatalogProfile = catalogProfile;
    }

    public String getPhysInvDoc() {
        return PhysInvDoc;
    }

    public void setPhysInvDoc(String physInvDoc) {
        PhysInvDoc = physInvDoc;
    }

    public GregorianCalendar getDocumentDate() {
        return DocumentDate;
    }

    public void setDocumentDate(GregorianCalendar documentDate) {
        DocumentDate = documentDate;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getEquipDescription() {
        return EquipDescription;
    }

    public void setEquipDescription(String equipDescription) {
        EquipDescription = equipDescription;
    }

    public boolean isEquipLongText() {
        return EquipLongText;
    }

    public void setEquipLongText(boolean equipLongText) {
        EquipLongText = equipLongText;
    }

    public String getFuncLocation() {
        return FuncLocation;
    }

    public void setFuncLocation(String funcLocation) {
        FuncLocation = funcLocation;
    }

    public String getABCIndicator() {
        return ABCIndicator;
    }

    public void setABCIndicator(String ABCIndicator) {
        this.ABCIndicator = ABCIndicator;
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

    public String getPPWorkCtr() {
        return PPWorkCtr;
    }

    public void setPPWorkCtr(String PPWorkCtr) {
        this.PPWorkCtr = PPWorkCtr;
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

    public String getSubNumber() {
        return SubNumber;
    }

    public void setSubNumber(String subNumber) {
        SubNumber = subNumber;
    }

    public String getSettlementOrder() {
        return SettlementOrder;
    }

    public void setSettlementOrder(String settlementOrder) {
        SettlementOrder = settlementOrder;
    }

    public String getAddNumber() {
        return AddNumber;
    }

    public void setAddNumber(String addNumber) {
        AddNumber = addNumber;
    }

    public String getWorkCenter() {
        return WorkCenter;
    }

    public void setWorkCenter(String workCenter) {
        WorkCenter = workCenter;
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

    public String getStockType() {
        return StockType;
    }

    public void setStockType(String stockType) {
        StockType = stockType;
    }

    public String getStockBatch() {
        return StockBatch;
    }

    public void setStockBatch(String stockBatch) {
        StockBatch = stockBatch;
    }

    public String getSpecialStock() {
        return SpecialStock;
    }

    public void setSpecialStock(String specialStock) {
        SpecialStock = specialStock;
    }

    public String getStockCustomer() {
        return StockCustomer;
    }

    public void setStockCustomer(String stockCustomer) {
        StockCustomer = stockCustomer;
    }

    public String getStockVendor() {
        return StockVendor;
    }

    public void setStockVendor(String stockVendor) {
        StockVendor = stockVendor;
    }

    public String getBomFlag() {
        return BomFlag;
    }

    public void setBomFlag(String bomFlag) {
        BomFlag = bomFlag;
    }

    public String getUpdateFlag() {
        return UpdateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        UpdateFlag = updateFlag;
    }

    public String getTechIdentNo() {
        return TechIdentNo;
    }

    public void setTechIdentNo(String techIdentNo) {
        TechIdentNo = techIdentNo;
    }

    public ZAppSettings.MobileStatus getEquipmentStatus() {
        if (EquipmentStatus == null) {
            for (ZAppSettings.MobileStatus status : ZAppSettings.MobileStatus.values()) {
                if (status.getMobileStatusCode().equalsIgnoreCase(getSystemStatus())) {
                    EquipmentStatus = status;
                    break;
                }
            }
        }
        return EquipmentStatus;
    }

    public void setEquipmentStatus(ZAppSettings.MobileStatus equipmentStatus) {
        EquipmentStatus = equipmentStatus;
    }

    public ArrayList<EquipmentClassificationSet> getClassifications() {
        return classifications;
    }
}
