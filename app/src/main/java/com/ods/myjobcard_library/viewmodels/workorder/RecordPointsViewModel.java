package com.ods.myjobcard_library.viewmodels.workorder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.MeasurementPointReading;
import com.ods.myjobcard_library.entities.transaction.Operation;
import com.ods.myjobcard_library.entities.transaction.WorkOrder;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class RecordPointsViewModel extends ViewModel {

    String opObjNumber = "";
    private MutableLiveData<ArrayList<MeasurementPointReading>> measurementPoints = new MutableLiveData<ArrayList<MeasurementPointReading>>();
    private ArrayList<MeasurementPointReading> measurmentPointCollection;
    private ArrayList<MeasurementPointReading> equipmentMeasurementPoints = null;
    private ArrayList<MeasurementPointReading> flMeasurementPoints = null;
    private ResponseObject resultEquip, resultFL;


    public LiveData<ArrayList<MeasurementPointReading>> getMeasurementPoints() {
        return measurementPoints;
    }

    public void setMeasurementPoints(WorkOrder workOrder) {
        getMeasurementPointsList(workOrder);
    }

    private void getMeasurementPointsList(WorkOrder workOrder) {
        measurmentPointCollection = new ArrayList<>();
        equipmentMeasurementPoints = new ArrayList<>();

        ArrayList<MeasurementPointReading> oprEQMesPoints = new ArrayList<>();
        ArrayList<MeasurementPointReading> oprFLMesPoints = new ArrayList<>();
        ArrayList<MeasurementPointReading> woMesPoints = new ArrayList<>();
        String WoequipNum = "";
        String WofuncLocNum = "";
        String currentOprEquipment = "";
        String currentOprFLNum = "";
        String oprNum = "";

        try {
            flMeasurementPoints = new ArrayList<>();

            if (ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                Operation currentOperation = workOrder.getCurrentOperation();
                oprNum = currentOperation.getOperationNum();
                currentOprEquipment = currentOperation.getEquipment();
                currentOprFLNum = currentOperation.getFuncLoc();
                if (!currentOprEquipment.isEmpty()) {
                    resultEquip = MeasurementPointReading.getEquipmentMeasurementPoint(currentOprEquipment, ZAppSettings.FetchLevel.List, null, oprNum);
                    if (!resultEquip.isError())
                        oprEQMesPoints = (ArrayList<MeasurementPointReading>) resultEquip.Content();
                }
                if (!currentOprFLNum.isEmpty()) {
                    resultFL = MeasurementPointReading.getFLMeasurementPoint(currentOprFLNum, ZAppSettings.FetchLevel.List, null, oprNum);
                    if (!resultFL.isError())
                        oprFLMesPoints = (ArrayList<MeasurementPointReading>) resultFL.Content();
                }
                if (ZConfigManager.WO_OP_OBJS_DISPLAY.isEmpty()) {
                    woMesPoints.addAll(WorkOrder.getWorkOrderMeasurementPoints(workOrder));
                    measurmentPointCollection.addAll(woMesPoints);
                }
                measurmentPointCollection.addAll(oprFLMesPoints);
                measurmentPointCollection.addAll(oprEQMesPoints);
            } else {
                //getWOOperations(workOrder);
                woMesPoints.addAll(WorkOrder.getWorkOrderMeasurementPoints(workOrder));
                measurmentPointCollection.addAll(woMesPoints);
            }
            measurementPoints.setValue(measurmentPointCollection);
        } catch (Exception e) {
            e.printStackTrace();
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }

    }

    private void getWOOperations(WorkOrder workOrder) {

    }

    private void getMeasurementPoints(WorkOrder workOrder) {

        measurmentPointCollection = new ArrayList<>();
        equipmentMeasurementPoints = new ArrayList<>();
        String equipNum = "";
        String funcLocNum = "";
        flMeasurementPoints = new ArrayList<>();
        try {
            String oprNum = "";
            if (!(ZConfigManager.ENABLE_OPERATION_MEASUREMENTPOINT_READINGS && !ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED)) {
                if (!ZConfigManager.OPERATION_LEVEL_ASSIGNMENT_ENABLED) {
                    equipNum = workOrder.getEquipNum();
                    funcLocNum = workOrder.getFuncLocation();
                } else if (workOrder.getCurrentOperation() != null) {
                    equipNum = workOrder.getCurrentOperation().getEquipment();
                    funcLocNum = workOrder.getCurrentOperation().getFuncLoc();
                    oprNum = workOrder.getCurrentOperation().getOperationNum();
                }
                if (equipNum != null && !equipNum.isEmpty()) {
                    resultEquip = MeasurementPointReading.getEquipmentMeasurementPoint(equipNum, ZAppSettings.FetchLevel.List, null, oprNum);
                    if (!resultEquip.isError()) {
                        equipmentMeasurementPoints = (ArrayList<MeasurementPointReading>) resultEquip.Content();
                    } else {
                        equipmentMeasurementPoints = new ArrayList<>();
                    }
                }
                if (funcLocNum != null && !funcLocNum.isEmpty()) {
                    ResponseObject resultFL = MeasurementPointReading.getFLMeasurementPoint(funcLocNum, ZAppSettings.FetchLevel.List, null, oprNum);
                    if (!resultFL.isError()) {
                        flMeasurementPoints = (ArrayList<MeasurementPointReading>) resultFL.Content();
                    } else
                        flMeasurementPoints = new ArrayList<>();
                }
                measurmentPointCollection.addAll(equipmentMeasurementPoints);
                measurmentPointCollection.addAll(flMeasurementPoints);
            } else {
                measurmentPointCollection.addAll(WorkOrder.getWorkOrderMeasurementPoints(workOrder));
            }
            measurementPoints.setValue(measurmentPointCollection);
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

}
