package com.ods.myjobcard_library.entities.transaction;

import android.text.TextUtils;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZCommon;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class WOLongText extends ZBaseEntity {

    private static ZAppSettings.FetchLevel FetchLevel;
    //Fields
    public boolean isOnline;   //Added by Anil
    DataHelper dbHelper;
    private String TextName;
    private String TextObject;
    private String Item;
    private String TagColumn;
    private String TextLine;
    private String EnteredBy;
    private String TextID;
    private String TempID;
    private String WorkOrderNum;
    private String OperationNum;
    private String ComponentItem;
    private String PlannofOpera;
    private String Counter;

//    private String TextStart = "00000000";
//    private String TextEnd = "00000000";

    //Setters and Getters Method
    private String TextString;

    private WOLongText(String textLine, String TextObjectID, String RefNum, ZConfigManager.Fetch_Object_Type Type, int Count, String WOTempID) {
        String deviceTime = "";
        try {
            initializeEntityProperties();
            InitLongText(TextObjectID, RefNum, Type, Count, WOTempID);
           /* Date timeStamp = ZCommon.getDeviceTime();
            if(timeStamp != null)
            {
                deviceTime = timeStamp.toString();
            }
            this.TextLine = deviceTime + " " + textLine;*/
            this.TextLine = textLine;
            //setTextString(textLine);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }

    }

    public WOLongText() {
        initializeEntityProperties();
    }

    public WOLongText(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity);
    }

    public WOLongText(ZODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);
        ArrayList<String> woLongTexts = null;
        try {
            if (entities != null) {
                woLongTexts = new ArrayList<String>();
                for (ODataEntity entity : entities) {
                    //optimize to fetch line data without creating the entity to make the read process faster
                    if (entity != null) {
                        if (entity.getProperties() != null && entity.getProperties().size() > 0) {
                            try {
                                if (entity.getProperties().containsKey("TextLine")) {
                                    String strLine = entity.getProperties().get("TextLine").getValue().toString();
                                    woLongTexts.add(strLine);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                DliteLogger.WriteLog(WOLongText.class, ZAppSettings.LogLevel.Error, e.getMessage());
                            }
                        }
                    }
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "Success", woLongTexts);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WOLongText.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result.setMessage(e.getMessage());
        }
        return result;
    }

    private static ArrayList<String> getLongTextContent(String textLine) {
        ArrayList<String> strLongTextContent = null;
        String[] strNewLineSplitContent = null;
        try {
            strNewLineSplitContent = textLine.split("\\r\\n|\\n|\\r");//else try this: "\\r?\\n"
            strLongTextContent = new ArrayList<String>();
            for (String strNewLineContent : strNewLineSplitContent) {
                try {
                    if (strNewLineContent.length() > 0 && strNewLineContent.length() <= ZConfigManager.MAX_LONGTEXT_LINE_LENGTH) {
                        strLongTextContent.add(strNewLineContent);
                    } else if (strNewLineContent.length() > ZConfigManager.MAX_LONGTEXT_LINE_LENGTH) {
                        int index = 0;
                        while (index < strNewLineContent.length()) {
                            strLongTextContent.add(strNewLineContent.substring(index, Math.min(index + ZConfigManager.MAX_LONGTEXT_LINE_LENGTH, strNewLineContent.length())));
                            index = index + ZConfigManager.MAX_LONGTEXT_LINE_LENGTH;
                        }
                    }
                } catch (Exception e) {
                    DliteLogger.WriteLog(WOLongText.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WOLongText.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return strLongTextContent;
    }

    public static int getLongTextsCount(String WorkOrderNum, String RefNum, String oprCounter, ZConfigManager.Fetch_Object_Type Type) {
        ResponseObject result = null;
        try {
            FetchLevel = ZAppSettings.FetchLevel.Count;
            result = getLongTexts(WorkOrderNum, RefNum, oprCounter, Type);
            if (result != null && !result.isError()) {
                return Integer.parseInt(String.valueOf(result.Content()));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WOLongText.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return 0;
    }

    public static ResponseObject getOperationLongText(String workOrderNum, String operationNum, String operationCounter) {
        ResponseObject result = null;
        try {
            result = getLongTexts(workOrderNum, operationNum, operationCounter, ZConfigManager.Fetch_Object_Type.Operation);
        } catch (Exception e) {
            DliteLogger.WriteLog(WOLongText.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    public static ResponseObject getLongTexts(String RefWONum, String RefOprComponentNum, ZConfigManager.Fetch_Object_Type objType) {
        ResponseObject result = null;
        try {
            result = getLongTexts(RefWONum, RefOprComponentNum, null, objType);
        } catch (Exception e) {
            DliteLogger.WriteLog(WOLongText.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return result;
    }

    private static ResponseObject getLongTexts(String RefWONum, String RefOprComponentNum, String oprCounter, ZConfigManager.Fetch_Object_Type objType) {

        ResponseObject result = null;
        String resourcePath = null;
        String strLongTextEntitySet = null;
        String strTextName = null;
        String strOrderBy = "&$orderby=";
        String strOrderByField = "Item";
        String strOrderByQuery;

        try {
            strLongTextEntitySet = ZCollections.WO_LONG_TEXT_COLLECTION;
            strOrderByQuery = strOrderBy + strOrderByField;
            //Todo pending: Declare global for 12
            if (RefWONum != null && RefWONum.length() < 12)
                RefWONum = ZCommon.getFormattedInt(12, BigInteger.valueOf(Long.parseLong(RefWONum)));
            switch (objType) {
                case WorkOrder:
                    strTextName = RefWONum;
                    resourcePath = strLongTextEntitySet + (FetchLevel != null && FetchLevel.equals(ZAppSettings.FetchLevel.Count) ? "/$count" : "") + "?$filter=(TextName eq '" + strTextName + "' and TextObject eq '" + ZCollections.LONG_TEXT_TYPE_WO + "')" + strOrderByQuery;
                    break;
                case Operation:
                    strTextName = RefWONum + RefOprComponentNum;
                    resourcePath = strLongTextEntitySet + (FetchLevel != null && FetchLevel.equals(ZAppSettings.FetchLevel.Count) ? "/$count" : "") + "?$filter=(TextName eq '" + strTextName + "' and Counter eq '" + oprCounter + "' and TextObject eq '" + ZCollections.LONG_TEXT_TYPE_OPERATION + "')" + strOrderByQuery;
                    break;
                case Components:
                    strTextName = RefWONum + RefOprComponentNum;
                    resourcePath = strLongTextEntitySet + (FetchLevel != null && FetchLevel.equals(ZAppSettings.FetchLevel.Count) ? "/$count" : "") + "?$filter=(TextName eq '" + strTextName + "' and TextObject eq '" + ZCollections.LONG_TEXT_TYPE_COMPONENT + "')" + strOrderByQuery;
                    break;
                case WOHistoryPending:
                    strLongTextEntitySet = ZCollections.WO_HISTORY_PENDING_LONG_TEXT_COLLECTION;
                    strTextName = RefWONum;
                    resourcePath = strLongTextEntitySet + (FetchLevel != null && FetchLevel.equals(ZAppSettings.FetchLevel.Count) ? "/$count" : "") + "?$filter=(TextName eq '" + strTextName + "' and TextObject eq '" + ZCollections.LONG_TEXT_TYPE_WO + "')" + strOrderByQuery;
                    break;
                default:
                    return new ResponseObject(ZConfigManager.Status.Error, "Invalid Long Text fetch request", null);
            }
            result = DataHelper.getInstance().getEntities(strLongTextEntitySet, resourcePath);
            if (!result.isError() && (FetchLevel == null || !FetchLevel.equals(ZAppSettings.FetchLevel.Count))) {
                //parse data for Long Text
                result = FromEntity((List<ODataEntity>) result.Content());
            }
            FetchLevel = null;
        } catch (Exception e) {
            DliteLogger.WriteLog(WOLongText.class, ZAppSettings.LogLevel.Error, e.getMessage());
            return new ResponseObject(ZConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    public String getTextName() {
        return TextName;
    }

    public void setTextName(String textName) {
        TextName = textName;
        //TextName = textName.replaceFirst("^0+(?!$)", "");
    }

    public String getTextObject() {
        return TextObject;
    }

    public void setTextObject(String textObject) {
        TextObject = textObject;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getTagXolumn() {
        return TagColumn;
    }

    public void setTagColumn(String tagcolumn) {
        TagColumn = tagcolumn;
    }

    public String getTextLine() {
        return TextLine;
    }

    public void setTextLine(String textLine) {
        TextLine = textLine;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getTextID() {
        return TextID;
    }

    public void setTextID(String textID) {
        TextID = textID;
    }

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

   /* public String getTextStart() {
        return TextStart;
    }

    public void setTextStart(String textStart) {
        TextStart = textStart;
    }

    public String getTextEnd() {
        return TextEnd;
    }

    public void setTextEnd(String textEnd) {
        TextEnd = textEnd;
    }*/

    //End of Setters and Getters Method

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getOperationNum() {
        return OperationNum;
    }

    public void setOperationNum(String operationNum) {
        OperationNum = operationNum;
    }

    public String getComponentItem() {
        return ComponentItem;
    }

    public void setComponentItem(String componentItem) {
        ComponentItem = componentItem;
    }

    public String getPlannofOpera() {
        return PlannofOpera;
    }

    public void setPlannofOpera(String plannofOpera) {
        PlannofOpera = plannofOpera;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.WO_LONG_TEXT_COLLECTION);
        this.setEntityType(ZCollections.WO_LONG_TEXT_ENTITY_TYPE);
        this.addKeyFieldNames("TextName");
        this.addKeyFieldNames("Item");
        this.addKeyFieldNames("Counter");
        this.setParentEntitySetName(ZCollections.WO_COLLECTION);
        this.setParentForeignKeyFieldName(ZConfigManager.WO_KEY_FIELD);
        this.setParentKeyFieldName(ZConfigManager.WO_KEY_FIELD);
    }

    public ResponseObject SendOperationLongText(String textLine, Operation operation, int Count, String WOTempID, boolean autoFlush) {
        ResponseObject responseObject = null;
        WOLongText woLongText = null;
        ArrayList<WOLongText> woLongTexts = null;
        ArrayList<String> strLongTextContent = null;
        try {

            String oprNumber = operation.getSubOperation() == null || operation.getSubOperation().isEmpty() ? operation.getOperationNum()
                    : operation.getSubOperation();
            woLongText = new WOLongText(textLine, operation.getWorkOrderNum(), oprNumber, ZConfigManager.Fetch_Object_Type.Operation, Count, WOTempID);
            //woLongTexts.add(woLongText);
            woLongText.setCounter(operation.getCounter());
            woLongText.setPlannofOpera(operation.getPlannofOpera());
            if (isOnline)
                responseObject = woLongText.SaveToOnlineStore();
            else
                responseObject = woLongText.SaveToStore(autoFlush);
            //find number of instancer required
            /*strLongTextContent = getLongTextContent(textLine);
            int intInstances = strLongTextContent.size();

            woLongTexts = new ArrayList<WOLongText>();
            //Init number of Instances
            for(int intInstance = 0; intInstance < intInstances; intInstance++) {
                try
                {
                    String oprNumber = operation.getSubOperation() == null || operation.getSubOperation().isEmpty() ? operation.getOperationNum()
                            : operation.getSubOperation();
                    woLongText = new WOLongText(strLongTextContent.get(intInstance), operation.getWorkOrderNum(), oprNumber, ZConfigManager.Fetch_Object_Type.Operation, Count+intInstance, WOTempID);
                    //woLongTexts.add(woLongText);
                    woLongText.setCounter(operation.getCounter());
                    woLongText.setPlannofOpera(operation.getPlannofOpera());
                    if (isOnline)
                        responseObject = woLongText.saveToOnlineStore();
                    else
                    responseObject = woLongText.SaveToStore((intInstance + 1) == intInstances && autoFlush);
                    //woLongText.CreateLongText();
                }
                catch (Exception e)
                {
                    DliteLogger.WriteLog(WOLongText.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                }

            }*/
            //CreateLongText(woLongTexts);
            //responseObject = new ResponseObject(ZConfigManager.Status.Success,"Success",woLongTexts);
        } catch (Exception e) {
            DliteLogger.WriteLog(WOLongText.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            responseObject = new ResponseObject(ZConfigManager.Status.Error, "Error", e.getMessage());
        }
        return responseObject;
    }

    private void InitLongText(String WorkOrderNum, String RefNum, ZConfigManager.Fetch_Object_Type Type, int Count, String WOTempID) {
        try {
            if (WorkOrderNum != null) {
                if (WorkOrderNum.length() < 12)
                    TextName = ZCommon.getFormattedInt(12, BigInteger.valueOf(Long.parseLong(WorkOrderNum)));
                else
                    TextName = WorkOrderNum;
                if (RefNum != null) {
                    TextName = TextName + RefNum;
                }
            }
            switch (Type) {
                case WorkOrder:
                    TextObject = ZCollections.LONG_TEXT_TYPE_WO;
                    setWorkOrderNum(WorkOrderNum);
                    break;
                case Operation:
                    TextObject = ZCollections.LONG_TEXT_TYPE_OPERATION;
                    setWorkOrderNum(WorkOrderNum);
                    OperationNum = RefNum;
                    break;
                case Components:
                    TextObject = ZCollections.LONG_TEXT_TYPE_COMPONENT;
                    setWorkOrderNum(WorkOrderNum);
                    ComponentItem = RefNum;
                    break;
                case Notification:
                    TextObject = ZCollections.LONG_TEXT_TYPE_NOTIFICATION;
                    break;
            }

            Item = ZCommon.getFormattedInt(4, (Count + 1));
            TagColumn = "*";
            EnteredBy = ZAppSettings.strUser;
            TempID = WOTempID;
            TextID = "";
            setMode(ZAppSettings.EntityMode.Create);

        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public boolean isInitialized() {
        return (!TextUtils.isEmpty(this.TextName));
    }

    public ResponseObject SendLongText(String textLine, String TextObjectID, String RefNum, ZConfigManager.Fetch_Object_Type Type, int Count, String WOTempID, boolean autoFlush) {
        ResponseObject responseObject = null;
        WOLongText woLongText = null;
        ArrayList<WOLongText> woLongTexts = null;
        ArrayList<String> strLongTextContent = null;
        try {
            woLongText = new WOLongText(textLine, TextObjectID, RefNum, Type, Count, WOTempID);
            //woLongTexts.add(woLongText);
            woLongText.setCounter("");
            responseObject = woLongText.SaveToStore(autoFlush);
            //find number of instancer required
            /*strLongTextContent = getLongTextContent(textLine);
            int intInstances = strLongTextContent.size();

            woLongTexts = new ArrayList<WOLongText>();
            //Init number of Instances
            for(int intInstance = 0; intInstance < intInstances; intInstance++) {
                try
                {
                    woLongText = new WOLongText(strLongTextContent.get(intInstance), TextObjectID, RefNum, Type, Count+intInstance, WOTempID);
                    //woLongTexts.add(woLongText);
                    woLongText.setCounter("");
                    responseObject = woLongText.SaveToStore((intInstance + 1) == intInstances && autoFlush);
                    //woLongText.CreateLongText();
                }
                catch (Exception e)
                {
                    DliteLogger.WriteLog(WOLongText.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                }

            }*/
            //CreateLongText(woLongTexts);
            //responseObject = new ResponseObject(ZConfigManager.Status.Success,"Success",woLongTexts);
        } catch (Exception e) {
            DliteLogger.WriteLog(WOLongText.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            responseObject = new ResponseObject(ZConfigManager.Status.Error, "Error", e.getMessage());
        }
        return responseObject;
    }

    public String getTextString() {
        return TextString;
    }

    public void setTextString(String textString) {
        TextString = textString;
    }
}