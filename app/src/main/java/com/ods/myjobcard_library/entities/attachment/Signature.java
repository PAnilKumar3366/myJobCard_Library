package com.ods.myjobcard_library.entities.attachment;

import com.ods.ods_sdk.StoreHelpers.BaseEntity;

public class Signature extends BaseEntity {

    private String DocCount;
    private String DocID;
    private String DocVerNo;
    private String DocVarID;
    private String DocVarTg;
    private String CompCount;
    private String PropName;
    private String PropValue;

    public String getDocCount() {
        return DocCount;
    }

    public void setDocCount(String docCount) {
        DocCount = docCount;
    }

    public String getDocID() {
        return DocID;
    }

    public void setDocID(String docID) {
        DocID = docID;
    }

    public String getDocVerNo() {
        return DocVerNo;
    }

    public void setDocVerNo(String docVerNo) {
        DocVerNo = docVerNo;
    }

    public String getDocVarID() {
        return DocVarID;
    }

    public void setDocVarID(String docVarID) {
        DocVarID = docVarID;
    }

    public String getDocVarTg() {
        return DocVarTg;
    }

    public void setDocVarTg(String docVarTg) {
        DocVarTg = docVarTg;
    }

    public String getCompCount() {
        return CompCount;
    }

    public void setCompCount(String compCount) {
        CompCount = compCount;
    }

    public String getPropName() {
        return PropName;
    }

    public void setPropName(String propName) {
        PropName = propName;
    }

    public String getPropValue() {
        return PropValue;
    }

    public void setPropValue(String propValue) {
        PropValue = propValue;
    }


    //End of Setter & getter Methods

}