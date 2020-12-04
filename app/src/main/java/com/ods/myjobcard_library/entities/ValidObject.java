package com.ods.myjobcard_library.entities;

import com.sap.smp.client.odata.ODataEntity;

/**
 * Created by lenovo on 17-11-2016.
 */
public class ValidObject extends ZBaseEntity {

    private String WoNum;
    private String EnteredBy;
    private String OnlineSearch;

    public ValidObject(ODataEntity entity) {
        create(entity);
    }

    public String getWoNum() {
        return WoNum;
    }

    public void setWoNum(String woNum) {
        WoNum = woNum;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public String getOnlineSearch() {
        return OnlineSearch;
    }

    public void setOnlineSearch(String onlineSearch) {
        OnlineSearch = onlineSearch;
    }
}
