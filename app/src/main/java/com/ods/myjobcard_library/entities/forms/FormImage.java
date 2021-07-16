package com.ods.myjobcard_library.entities.forms;

import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.entities.odata.ZODataEntity;

public class FormImage extends ZBaseEntity {

    private String Formid;
    private String Version;
    private String Question;
    private String FileName;
    private String Base64;

    public FormImage() {
        initializingEntityProperties();
    }

    public FormImage(ZODataEntity entity) {
        initializingEntityProperties();
        create(entity);
    }


    private void initializingEntityProperties() {
        this.setEntityType("FormImage");
        this.setEntitySetName("FormImageSet");
        this.addKeyFieldNames("Formid");
        this.addKeyFieldNames("Version");
        this.addKeyFieldNames("Question");
    }

    public String getFormid() {
        return Formid;
    }

    public void setFormid(String formid) {
        Formid = formid;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getBase64() {
        return Base64;
    }

    public void setBase64(String base64) {
        Base64 = base64;
    }
}
