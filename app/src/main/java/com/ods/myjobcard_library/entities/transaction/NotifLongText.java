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

public class NotifLongText extends ZBaseEntity {

    private static ZAppSettings.FetchLevel FetchLevel;
    //Fields
    DataHelper dbHelper;
    private String TextName;
    private String TextObject;
    private String Item;
    private String TagColumn;
    private String TextLine;
    private String EnteredBy;
    private String TextID;
    private String TempID;
    private String Notification;
    private String NotificationItem;
    private String TextString;
    private boolean isOnline;

    private NotifLongText(String textLine, String TextObjectID, String RefNum, ZConfigManager.Fetch_Object_Type Type, int Count, String NotifTempID) {
        String deviceTime = "";
        try {
            initializeEntityProperties();
            InitLongText(TextObjectID, RefNum, Type, Count, NotifTempID);
           /* Date timeStamp = ZCommon.getDeviceTime();
            if(timeStamp != null)
            {
                deviceTime = timeStamp.toString();
            }
            this.TextLine = deviceTime + " " + textLine;*/
            //this.TextLine = textLine;
            //setTextString(textLine);
            setTextLine(textLine);
        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }

    }

    public NotifLongText() {
        initializeEntityProperties();
    }


    /**
     * Added By Anil Kumar
     * constructo to create or map the new instance with the given ZODataEntity Object.
     *
     * @param entity ZODataEntity Contains the oDataEntity or EntityValue instance map to
     */

    public NotifLongText(ZODataEntity entity) {
        initializeEntityProperties();
        create(entity);
    }
    //Setters and Getters Method

    public NotifLongText(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        initializeEntityProperties();
        create(entity);
    }

    public static ResponseObject getItemLongTexts(String notification, String itemNum, boolean isWoNotif) {

        //Set Lontext for the notification
        ResponseObject resultLongText = null;
        try {
            resultLongText = getLongTexts(notification, itemNum, itemNum, isWoNotif ? ZConfigManager.Fetch_Object_Type.WONotificationItems : ZConfigManager.Fetch_Object_Type.NotificationItems);
        } catch (Exception e) {
            DliteLogger.WriteLog(NotifLongText.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

        return resultLongText;
    }

    public static ResponseObject getItemsCauseLongTexts(String notification, String cause, String itemNum, boolean isWoNotif) {

        //Set Lontext for the notification
        ResponseObject resultLongText = null;
        try {
            resultLongText = getLongTexts(notification, itemNum + cause, itemNum, isWoNotif ? ZConfigManager.Fetch_Object_Type.WONotificationItemCauses : ZConfigManager.Fetch_Object_Type.NotificationItemCauses);
        } catch (Exception e) {
            DliteLogger.WriteLog(NotifLongText.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

        return resultLongText;
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
                    DliteLogger.WriteLog(NotifLongText.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(NotifLongText.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return strLongTextContent;
    }

    private static ResponseObject FromEntity(List<ODataEntity> entities) {
        ResponseObject result = new ResponseObject(ZConfigManager.Status.Error);
        ArrayList<String> noLongTexts = null;
        try {
            if (entities != null) {
                noLongTexts = new ArrayList<String>();
                for (ODataEntity entity : entities) {
                    //optimize to fetch line data without creating the entity to make the read process faster
                    try {
                        if (entity != null) {
                            if (entity.getProperties() != null && entity.getProperties().size() > 0) {
                                if (entity.getProperties().containsKey("TextLine")) {
                                    String strLine = entity.getProperties().get("TextLine").getValue().toString();
                                    noLongTexts.add(strLine);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        DliteLogger.WriteLog(NotifLongText.class, ZAppSettings.LogLevel.Error, e.getMessage());
                    }
                }
                result = new ResponseObject(ZConfigManager.Status.Success, "Success", noLongTexts);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WOLongText.class, ZAppSettings.LogLevel.Error, e.getMessage());
            result.setMessage(e.getMessage());
        }
        return result;
    }

    public static ResponseObject getTaskLongTexts(String notification, String taskNum, String itemNum, boolean isWoNotif) {

        //Set Lontext for the notification
        ResponseObject resultLongText = null;
        try {
            resultLongText = getLongTexts(notification, taskNum, itemNum, isWoNotif ? ZConfigManager.Fetch_Object_Type.WONotificationTasks : ZConfigManager.Fetch_Object_Type.NotificationTasks);
        } catch (Exception e) {
            DliteLogger.WriteLog(NotifLongText.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

        return resultLongText;
    }

    public static int getLongTextsCount(String notification, String RefNum, String notificationItem, ZConfigManager.Fetch_Object_Type Type) {
        ResponseObject result = null;
        try {
            FetchLevel = ZAppSettings.FetchLevel.Count;
            result = getLongTexts(notification, RefNum, notificationItem, Type);
            if (result != null && !result.isError()) {
                return Integer.parseInt(String.valueOf(result.Content()));
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(WOLongText.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return 0;
    }

    public static ResponseObject getItemCauseLongTexts(String notification, String causeNum, String itemNum, boolean isWoNotif) {

        //Set Lontext for the notification
        ResponseObject resultLongText = null;
        try {
            resultLongText = getLongTexts(notification, causeNum, itemNum, isWoNotif ? ZConfigManager.Fetch_Object_Type.WONotificationItemCauses : ZConfigManager.Fetch_Object_Type.NotificationItemCauses);
        } catch (Exception e) {
            DliteLogger.WriteLog(NotifLongText.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

        return resultLongText;
    }

    public static ResponseObject getActivityLongTexts(String notification, String activityNum, String itemNum, boolean isWoNotif) {

        //Set Lontext for the notification
        ResponseObject resultLongText = null;
        try {
            resultLongText = getLongTexts(notification, activityNum, itemNum, isWoNotif ? ZConfigManager.Fetch_Object_Type.WONotificationActivity : ZConfigManager.Fetch_Object_Type.NotificationActivity);
        } catch (Exception e) {
            DliteLogger.WriteLog(NotifLongText.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

        return resultLongText;
    }

    public static ResponseObject getLongTexts(String notification, ZConfigManager.Fetch_Object_Type objType) {

        //Set Lontext for the notification
        ResponseObject resultLongText = null;
        try {
            resultLongText = getLongTexts(notification, "", "", objType);
        } catch (Exception e) {
            DliteLogger.WriteLog(NotifLongText.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }

        return resultLongText;
    }

    private static ResponseObject getLongTexts(String RefNONum, String RefChildNum, String notificationItem, ZConfigManager.Fetch_Object_Type objType) {

        ResponseObject result = null;
        String resourcePath = null;
        String strLongTextEntitySet = null;
        String strTextName = null;
        String strObjType = null;
        String strOrderBy = "&$orderby=";
        String strOrderByField = "Item";
        String strOrderByQuery;

        try {
            strOrderByQuery = strOrderBy + strOrderByField;
            //Todo pending: Declare global for 12
            if (RefNONum != null && RefNONum.length() < 12)
                RefNONum = ZCommon.getFormattedInt(12, BigInteger.valueOf(Long.parseLong(RefNONum)));
            if (notificationItem == null || notificationItem.isEmpty())
                notificationItem = "0000";
            if (RefChildNum == null || RefChildNum.isEmpty())
                RefChildNum = "";
            strTextName = RefNONum + RefChildNum;
            switch (objType) {
                case WONotification:
                    strObjType = ZCollections.LONG_TEXT_TYPE_NOTIFICATION;
                    strLongTextEntitySet = ZCollections.WONOTIFICATION_LONG_TEXT_COLLECTION;
                    resourcePath = strLongTextEntitySet + (FetchLevel != null && FetchLevel.equals(ZAppSettings.FetchLevel.Count) ? "/$count" : "") + "?$filter=(TextName eq '" + strTextName + "' and TextObject eq '" + strObjType + "' and NotificationItem eq '" + notificationItem + "')" + strOrderByQuery;
                    break;
                case WONotificationItems:
                    strObjType = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_ITEM;
                    strLongTextEntitySet = ZCollections.WONOTIFICATION_LONG_TEXT_COLLECTION;
                    resourcePath = strLongTextEntitySet + (FetchLevel != null && FetchLevel.equals(ZAppSettings.FetchLevel.Count) ? "/$count" : "") + "?$filter=(TextName eq '" + strTextName + "' and TextObject eq '" + strObjType + "' and NotificationItem eq '" + notificationItem + "')" + strOrderByQuery;
                    break;
                case WONotificationActivity:
                    strObjType = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_ACTIVITY;
                    strLongTextEntitySet = ZCollections.WONOTIFICATION_LONG_TEXT_COLLECTION;
                    resourcePath = strLongTextEntitySet + (FetchLevel != null && FetchLevel.equals(ZAppSettings.FetchLevel.Count) ? "/$count" : "") + "?$filter=(TextName eq '" + strTextName + "' and TextObject eq '" + strObjType + "' and NotificationItem eq '" + notificationItem + "')" + strOrderByQuery;
                    break;
                case WONotificationTasks:
                    strObjType = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_TASK;
                    strLongTextEntitySet = ZCollections.WONOTIFICATION_LONG_TEXT_COLLECTION;
                    resourcePath = strLongTextEntitySet + (FetchLevel != null && FetchLevel.equals(ZAppSettings.FetchLevel.Count) ? "/$count" : "") + "?$filter=(TextName eq '" + strTextName + "' and TextObject eq '" + strObjType + "' and NotificationItem eq '" + notificationItem + "')" + strOrderByQuery;
                    break;
                case WONotificationItemCauses:
                    strObjType = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_ITEM_CAUSE;
                    strLongTextEntitySet = ZCollections.WONOTIFICATION_LONG_TEXT_COLLECTION;
                    resourcePath = strLongTextEntitySet + (FetchLevel != null && FetchLevel.equals(ZAppSettings.FetchLevel.Count) ? "/$count" : "") + "?$filter=(TextName eq '" + strTextName + "' and TextObject eq '" + strObjType + "' and NotificationItem eq '" + notificationItem + "')" + strOrderByQuery;
                    break;
                case Notification:
                    strObjType = ZCollections.LONG_TEXT_TYPE_NOTIFICATION;
                    strLongTextEntitySet = ZCollections.NOTIFICATION_LONG_TEXT_COLLECTION;
                    resourcePath = strLongTextEntitySet + (FetchLevel != null && FetchLevel.equals(ZAppSettings.FetchLevel.Count) ? "/$count" : "") + "?$filter=(TextName eq '" + strTextName + "' and TextObject eq '" + strObjType + "' and NotificationItem eq '" + notificationItem + "')" + strOrderByQuery;
                    break;
                case NotificationItems:
                    strObjType = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_ITEM;
                    strLongTextEntitySet = ZCollections.NOTIFICATION_LONG_TEXT_COLLECTION;
                    resourcePath = strLongTextEntitySet + (FetchLevel != null && FetchLevel.equals(ZAppSettings.FetchLevel.Count) ? "/$count" : "") + "?$filter=(TextName eq '" + strTextName + "' and TextObject eq '" + strObjType + "' and NotificationItem eq '" + notificationItem + "')" + strOrderByQuery;
                    break;
                case NotificationActivity:
                    strObjType = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_ACTIVITY;
                    strLongTextEntitySet = ZCollections.NOTIFICATION_LONG_TEXT_COLLECTION;
                    resourcePath = strLongTextEntitySet + (FetchLevel != null && FetchLevel.equals(ZAppSettings.FetchLevel.Count) ? "/$count" : "") + "?$filter=(TextName eq '" + strTextName + "' and TextObject eq '" + strObjType + "' and NotificationItem eq '" + notificationItem + "')" + strOrderByQuery;
                    break;
                case NotificationTasks:
                    strObjType = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_TASK;
                    strLongTextEntitySet = ZCollections.NOTIFICATION_LONG_TEXT_COLLECTION;
                    resourcePath = strLongTextEntitySet + (FetchLevel != null && FetchLevel.equals(ZAppSettings.FetchLevel.Count) ? "/$count" : "") + "?$filter=(TextName eq '" + strTextName + "' and TextObject eq '" + strObjType + "' and NotificationItem eq '" + notificationItem + "')" + strOrderByQuery;
                    break;
                case NotificationItemCauses:
                    strObjType = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_ITEM_CAUSE;
                    strLongTextEntitySet = ZCollections.NOTIFICATION_LONG_TEXT_COLLECTION;
                    resourcePath = strLongTextEntitySet + (FetchLevel != null && FetchLevel.equals(ZAppSettings.FetchLevel.Count) ? "/$count" : "") + "?$filter=(TextName eq '" + strTextName + "' and TextObject eq '" + strObjType + "' and NotificationItem eq '" + notificationItem + "')" + strOrderByQuery;
                    break;
                case NotificationHistoryPending:
                    strObjType = ZCollections.LONG_TEXT_TYPE_NOTIFICATION;
                    strLongTextEntitySet = ZCollections.NOTIFICATION_HISTORY_PENDING_LONG_TEXT_COLLECTION;
                    strTextName = RefNONum;
                    resourcePath = strLongTextEntitySet + (FetchLevel != null && FetchLevel.equals(ZAppSettings.FetchLevel.Count) ? "/$count" : "") + "?$filter=(TextName eq '" + strTextName + "' and TextObject eq '" + strObjType + "')" + strOrderByQuery;
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

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
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
//End of Setters and Getters Method

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

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }

    public String getNotificationItem() {
        return NotificationItem;
    }

    public void setNotificationItem(String notificationItem) {
        NotificationItem = notificationItem;
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.NOTIFICATION_LONG_TEXT_COLLECTION);
        this.setEntityType(ZCollections.NOTIFICATION_LONG_TEXT_ENTITY_TYPE);
        this.addKeyFieldNames("TextName");
        this.addKeyFieldNames("Item");
        this.addKeyFieldNames("TextObject");
        this.addKeyFieldNames("NotificationItem");
        this.setParentEntitySetName(ZCollections.NOTIFICATION_COLLECTION);
        this.setParentForeignKeyFieldName(ZConfigManager.NOTIFICATION_KEY_FIELD);
        this.setParentKeyFieldName(ZConfigManager.NOTIFICATION_KEY_FIELD);
    }

    public boolean isInitialized() {
        return (!TextUtils.isEmpty(this.TextName));
    }

    private void InitLongText(String NotifNum, String RefNum, ZConfigManager.Fetch_Object_Type Type, int Count, String NotifTempID) {
        try {
            if (NotifNum != null) {
                TextName = NotifNum.length() < 12 ? String.format("%012d", BigInteger.valueOf(Long.parseLong(NotifNum))) : NotifNum;
                if (RefNum != null) {
                    TextName = TextName + RefNum;
                }
                setNotification(NotifNum);
            }
            switch (Type) {
                case Notification:
                    TextObject = ZCollections.LONG_TEXT_TYPE_NOTIFICATION;
                    break;
                case WONotification:
                    TextObject = ZCollections.LONG_TEXT_TYPE_NOTIFICATION;
                    break;
                case NotificationActivity:
                    TextObject = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_ACTIVITY;
                    break;
                case WONotificationActivity:
                    TextObject = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_ACTIVITY;
                    break;
                case NotificationItems:
                    TextObject = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_ITEM;
                    break;
                case WONotificationItems:
                    TextObject = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_ITEM;
                    break;
                case NotificationTasks:
                    TextObject = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_TASK;
                    break;
                case WONotificationTasks:
                    TextObject = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_TASK;
                    break;
                case NotificationItemCauses:
                    TextObject = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_ITEM_CAUSE;
                    break;
                case WONotificationItemCauses:
                    TextObject = ZCollections.LONG_TEXT_TYPE_NOTIFICATION_ITEM_CAUSE;
                    break;
                default:
                    break;
            }

            Item = ZCommon.getFormattedInt(4, (Count + 1));
            TagColumn = "*";
            EnteredBy = ZAppSettings.strUser;
            TextID = "";
            TempID = NotifTempID;
            setMode(ZAppSettings.EntityMode.Create);

        } catch (Exception e) {
            DliteLogger.WriteLog(this.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
        }
    }

    public ResponseObject SendNotificationText(String textLine, String notification, String notifTempID, boolean autoFlush) {
        ResponseObject responseObject = null;
        NotifLongText notifLongText = null;
        ArrayList<NotifLongText> notifLongTexts = null;
        ArrayList<String> strLongTextContent = null;
        int count = 0;
        try {
            count = getLongTextsCount(notification, "", "0000", ZConfigManager.Fetch_Object_Type.Notification);
            notifLongText = new NotifLongText(textLine, notification, "", ZConfigManager.Fetch_Object_Type.Notification, count, notifTempID);
            notifLongText.setNotificationItem("0000");
            responseObject = notifLongText.SaveToStore(autoFlush);
            //find number of instancer required
            /*strLongTextContent = getLongTextContent(textLine);
            int intInstances = strLongTextContent.size();

            //Init number of Instances
            for(int intInstance = 0; intInstance < intInstances; intInstance++) {
                try
                {
                    notifLongText = new NotifLongText(strLongTextContent.get(intInstance), notification, "", ZConfigManager.Fetch_Object_Type.Notification, count+intInstance, notifTempID);
                    notifLongText.setNotificationItem("0000");
                    responseObject = notifLongText.SaveToStore((intInstance + 1) == intInstances && autoFlush);
                    //woLongText.CreateLongText();
                }
                catch (Exception e)
                {
                    DliteLogger.WriteLog(NotifLongText.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                }

            }*/
            //CreateLongText(woLongTexts);
            //responseObject = new ResponseObject(ZConfigManager.Status.Success,"Success",woLongTexts);
        } catch (Exception e) {
            DliteLogger.WriteLog(NotifLongText.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            responseObject = new ResponseObject(ZConfigManager.Status.Error, "Error", e.getMessage());
        }
        return responseObject;
    }

    public ResponseObject SendLongText(String textLine, String TextObjectID, String RefNum, String notificationItem, ZConfigManager.Fetch_Object_Type Type, String NotifTempID, boolean isWONotif, boolean autoFlush) {
        ResponseObject responseObject = null;
        NotifLongText notifLongText = null;
        ArrayList<String> strLongTextContent = null;
        try {
            //find number of instancer required
            int Count = getLongTextsCount(TextObjectID, RefNum, notificationItem, Type);
            notifLongText = new NotifLongText(textLine, TextObjectID, RefNum, Type, Count, NotifTempID);
            if (isWONotif) {
                notifLongText.setEntitySetName(ZCollections.WONOTIFICATION_LONG_TEXT_COLLECTION);
                notifLongText.setEntityType(ZCollections.WONOTIFICATION_LONG_TEXT_ENTITY_TYPE);
                notifLongText.setEntityResourcePath(ZCollections.WONOTIFICATION_LONG_TEXT_COLLECTION);
            }
            notifLongText.setNotificationItem(notificationItem);
            responseObject = notifLongText.SaveToStore(autoFlush);

            /*strLongTextContent = getLongTextContent(textLine);
            int intInstances = strLongTextContent.size();

            //Init number of Instances
            for (int intInstance = 0; intInstance < intInstances; intInstance++) {
                try {
                    *//* switch (Type) {
                        case WONotification:
                            //Ideally this case should never come as we are not processing this in backend
                            notifLongText.setEntitySetName(ZCollections.WONOTIFICATION_LONG_TEXT_COLLECTION);
                            notifLongText.setEntityType(ZCollections.WONOTIFICATION_LONG_TEXT_ENTITY_TYPE);
                            notifLongText.setEntityResourcePath(ZCollections.WONOTIFICATION_LONG_TEXT_COLLECTION);
                            break;
                        default:
                            break;
                    }*//*
                    //woLongText.CreateLongText();
                     responseObject = notifLongText.SaveToStore(autoFlush);
                } catch (Exception e) {
                    DliteLogger.WriteLog(NotifLongText.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
                }

            }*/
            //CreateLongText(woLongTexts);
            //responseObject = new ResponseObject(ZConfigManager.Status.Success,"Success",woLongTexts);
        } catch (Exception e) {
            DliteLogger.WriteLog(NotifLongText.class.getClass(), ZAppSettings.LogLevel.Error, e.getMessage());
            responseObject = new ResponseObject(ZConfigManager.Status.Error, "Error", e.getMessage());
        }
        return responseObject;
    }

    public String getTextString() {
        return TextString;
    }

    public void setTextString(String textString) {
        this.TextString = textString;
    }
}