package com.ods.myjobcard_library.entities.transaction;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.sap.smp.client.odata.ODataEntity;

/**
 * Created by lenovo on 27-12-2015.
 */
public class NotificationLongText extends ZBaseEntity {

    //header details
    private String TextName;
    private String TextObject;
    private String Item;
    private String TagColumn;
    private String TextLine;
    private String EnteredBy;
    private String TextID;

    //constructor
    public NotificationLongText(String textName, String item) {
        super();
        this.TextName = textName;
        this.Item = item;
    }

    public NotificationLongText(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        create(entity);
    }


//Setter and Getter methods

    public String getTextName() {
        return TextName;
    }

    public void setTextName(String textName) {
        TextName = textName;
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

    public String getTagColumn() {
        return TagColumn;
    }

    public void setTagColumn(String tagColumn) {
        TagColumn = tagColumn;
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
}
