package com.ods.myjobcard_library.entities.transaction;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.sap.smp.client.odata.ODataEntity;

import java.sql.Time;
import java.util.GregorianCalendar;

public class NotificationPending extends BaseEntity {

    private String Notification;
    private String Notifictntype;

    //WO child elements
    //Fields
    private String Priority;
    private String Description;
    private GregorianCalendar Completndate;
    private String EnteredBy;
    private Time Completiontime;
    private boolean Breakdown;
    private GregorianCalendar Malfunctstart;
    private Time StartMalfnT;
    private GregorianCalendar Malfunctend;
    private Time Malfunctionend;
    private String Equipment;
    private String Functionalloc;
    private GregorianCalendar Reqstart;
    private Time Reqstarttime;
    private GregorianCalendar RequiredEnd;
    private Time Reqendtime;
    private String ContactPerson;
    private String LastFirstName;
    private String EmplApplName;
    private String Objectnumber;

    public NotificationPending(String notification) {
        this.Notification = notification;
    }

    public NotificationPending(ODataEntity entity, ZAppSettings.FetchLevel fetchLevel) {
        create(entity);
    }


    //Setters and Getters Method

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }

    public String getNotifictntype() {
        return Notifictntype;
    }

    public void setNotifictntype(String notifictntype) {
        Notifictntype = notifictntype;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public GregorianCalendar getCompletndate() {
        return Completndate;
    }

    public void setCompletndate(GregorianCalendar completndate) {
        Completndate = completndate;
    }

    public String getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        EnteredBy = enteredBy;
    }

    public Time getCompletiontime() {
        return Completiontime;
    }

    public void setCompletiontime(Time completiontime) {
        Completiontime = completiontime;
    }

    public boolean isBreakdown() {
        return Breakdown;
    }

    public void setBreakdown(boolean breakdown) {
        Breakdown = breakdown;
    }

    public GregorianCalendar getMalfunctstart() {
        return Malfunctstart;
    }

    public void setMalfunctstart(GregorianCalendar malfunctstart) {
        Malfunctstart = malfunctstart;
    }

    public Time getStartMalfnT() {
        return StartMalfnT;
    }

    public void setStartMalfnT(Time startMalfnT) {
        StartMalfnT = startMalfnT;
    }

    public GregorianCalendar getMalfunctend() {
        return Malfunctend;
    }

    public void setMalfunctend(GregorianCalendar malfunctend) {
        Malfunctend = malfunctend;
    }

    public Time getMalfunctionend() {
        return Malfunctionend;
    }

    public void setMalfunctionend(Time malfunctionend) {
        Malfunctionend = malfunctionend;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getFunctionalloc() {
        return Functionalloc;
    }

    public void setFunctionalloc(String functionalloc) {
        Functionalloc = functionalloc;
    }

    public GregorianCalendar getReqstart() {
        return Reqstart;
    }

    public void setReqstart(GregorianCalendar reqstart) {
        Reqstart = reqstart;
    }

    public Time getReqstarttime() {
        return Reqstarttime;
    }

    public void setReqstarttime(Time reqstarttime) {
        Reqstarttime = reqstarttime;
    }

    public GregorianCalendar getRequiredEnd() {
        return RequiredEnd;
    }

    public void setRequiredEnd(GregorianCalendar requiredEnd) {
        RequiredEnd = requiredEnd;
    }

    public Time getReqendtime() {
        return Reqendtime;
    }

    public void setReqendtime(Time reqendtime) {
        Reqendtime = reqendtime;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }

    public String getLastFirstName() {
        return LastFirstName;
    }

    public void setLastFirstName(String lastFirstName) {
        LastFirstName = lastFirstName;
    }

    public String getEmplApplName() {
        return EmplApplName;
    }

    public void setEmplApplName(String emplApplName) {
        EmplApplName = emplApplName;
    }

    public String getObjectnumber() {
        return Objectnumber;
    }

    public void setObjectnumber(String objectnumber) {
        Objectnumber = objectnumber;
    }


    //End of Setters and Getters Method

}