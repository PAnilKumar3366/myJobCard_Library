package com.ods.myjobcard_library.entities.forms;

public class FormListObject {

    private String formname;
    private String formid;

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public void setOccur(int occur) {
        this.occur = occur;
    }

    public void setMultipleSub(String multipleSub) {
        this.multipleSub = multipleSub;
    }

    private String version;
    private String mandatory;
    private int occur;
    private String multipleSub;
    private int filledForm;
    private String instanceId;
    private String isDraft;
    private boolean isGridTheme;
    private String oprNum;

    public FormListObject(String formname, String formid, String version, String mandatory,
                          int occur, String multipleSub, int filledForm, String instanceId, boolean isGridTheme) {

        this.formname = formname;
        this.formid = formid;
        this.version = version;
        this.mandatory = mandatory;
        this.occur = occur;
        this.multipleSub = multipleSub;
        this.filledForm = filledForm;
        this.instanceId = instanceId;
        this.isGridTheme = isGridTheme;
    }
    public FormListObject(String formname, String formid, String version, String mandatory,
                          int occur, String multipleSub, int filledForm, String instanceId, boolean isGridTheme,String oprNum) {

        this.formname = formname;
        this.formid = formid;
        this.version = version;
        this.mandatory = mandatory;
        this.occur = occur;
        this.multipleSub = multipleSub;
        this.filledForm = filledForm;
        this.instanceId = instanceId;
        this.isGridTheme = isGridTheme;
        this.oprNum=oprNum;
    }


    public String getFormname() {
        return formname;
    }

    public String getFormid() {
        return formid;
    }

    public String getVersion() {
        return version;
    }

    public String getMandatory() {
        return mandatory;
    }

    public int getOccur() {
        return occur;
    }

    public String getMultipleSub() {
        return multipleSub;
    }

    public int getFilledForm() {
        return filledForm;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public boolean isGridTheme() {
        return isGridTheme;
    }

    public String getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(String isDraft) {
        this.isDraft = isDraft;
    }

    public String getOprNum() {
        return oprNum;
    }

    public void setOprNum(String oprNum) {
        this.oprNum = oprNum;
    }
}
