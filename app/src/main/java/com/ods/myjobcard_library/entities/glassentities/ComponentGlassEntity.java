package com.ods.myjobcard_library.entities.glassentities;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.Components;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComponentGlassEntity implements Serializable {

    private String WorkOrderNum;
    private int WithdrawalQty;
    private String Reservation;
    private int ReqmtQty;
    private String Item;
    private String Material;
    private String MaterialDescription;

    public static ArrayList<ComponentGlassEntity> getWorkOrderComponent(String workOrderNum) {
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);
        ArrayList<ComponentGlassEntity> components = new ArrayList<>();
        String entitySetName = ZCollections.COMPONENT_COLLECTION;
        String resPath = entitySetName + "?$filter=WorkOrderNum eq '" + workOrderNum + "'&$select=WorkOrderNum,ReqmtQty,WithdrawalQty,Reservation,Material,MaterialDescription,Item&$orderby=Item";
        try {
            result = DataHelper.getInstance().getEntities(entitySetName, resPath);
            if (!result.isError()) {
                List<ODataEntity> entities = (List<ODataEntity>) result.Content();
                for (ODataEntity entity : entities) {
                    Components component = new Components(entity, ZAppSettings.FetchLevel.Header);
                    ComponentGlassEntity componentGlassEntity = new ComponentGlassEntity();
                    componentGlassEntity.setWorkOrderNum(component.getWorkOrderNum());
                    componentGlassEntity.setReservation(component.getReservation());
                    componentGlassEntity.setReqmtQty(component.getReqmtQty().intValue());
                    componentGlassEntity.setWithdrawalQty(component.getWithdrawalQty().intValue());
                    componentGlassEntity.setItem(component.getItem());
                    componentGlassEntity.setMaterial(component.getMaterial());
                    componentGlassEntity.setMaterialDescription(component.getMaterialDescription());
                    components.add(componentGlassEntity);
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(OperationGlassEntity.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return components;

    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public int getWithdrawalQty() {
        return WithdrawalQty;
    }

    public void setWithdrawalQty(int withdrawalQty) {
        WithdrawalQty = withdrawalQty;
    }

    public int getReqmtQty() {
        return ReqmtQty;
    }

    public void setReqmtQty(int reqmtQty) {
        ReqmtQty = reqmtQty;
    }

    public String getReservation() {
        return Reservation;
    }

    public void setReservation(String reservation) {
        Reservation = reservation;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getMaterialDescription() {
        return MaterialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        MaterialDescription = materialDescription;
    }

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String material) {
        Material = material;
    }
}
