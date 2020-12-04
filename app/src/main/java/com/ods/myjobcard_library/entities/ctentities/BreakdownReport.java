package com.ods.myjobcard_library.entities.ctentities;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.sap.client.odata.v4.EntityValue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BreakdownReport extends ZBaseEntity {

    private GregorianCalendar Date;
    private String Period;
    private String Equipment;
    private String FuncLocation;
    private BigInteger ActualBreakdns;
    private BigDecimal Downtime;
    private BigDecimal MTTR;
    private BigDecimal MTBR;
    private BigDecimal MeanTimeToRepair;
    private BigDecimal TimeBetweenRepairs;
    private BigDecimal MeanTimeBetweenRepairs;

    public BreakdownReport(EntityValue entity) {
        initializeEntityProperties();
        create(entity);
    }

    private void initializeEntityProperties() {
        this.setEntitySetName(ZCollections.BREAKDOWN_REPORT_ENTITY_COLLECTION);
        this.setEntityType(ZCollections.BREAKDOWN_REPORT_ENTITY_TYPE);
        this.addKeyFieldNames("Period");
        this.addKeyFieldNames("Equipment");
        this.addKeyFieldNames("FuncLocation");
    }

    public GregorianCalendar getDate() {
        return Date;
    }

    public void setDate(GregorianCalendar date) {
        Date = date;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getFormattedPeriod() {
        String formattedPeriod = Period;
        try {
            if (!Period.isEmpty()) {
                String strPattern = "^0+";
                formattedPeriod = Period.replaceAll(strPattern, "");
                String month = formattedPeriod.substring(0, 1);
                String year = formattedPeriod.substring(1, formattedPeriod.length());
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
                calendar.set(Calendar.DATE, 1);
                calendar.set(Calendar.YEAR, Integer.parseInt(year));
                String monthName = new SimpleDateFormat("MMMM").format(calendar.getTime());
                formattedPeriod = formattedPeriod.replace(month, monthName + " ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return formattedPeriod;
        }
        return formattedPeriod;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getFuncLocation() {
        return FuncLocation;
    }

    public void setFuncLocation(String funcLocation) {
        FuncLocation = funcLocation;
    }

    public BigInteger getActualBreakdns() {
        return ActualBreakdns;
    }

    public void setActualBreakdns(BigInteger actualBreakdns) {
        ActualBreakdns = actualBreakdns;
    }

    public BigDecimal getDowntime() {
        return Downtime;
    }

    public void setDowntime(BigDecimal downtime) {
        Downtime = downtime;
    }

    public BigDecimal getMTTR() {
        return MTTR;
    }

    public void setMTTR(BigDecimal MTTR) {
        this.MTTR = MTTR;
    }

    public BigDecimal getMTBR() {
        return MTBR;
    }

    public void setMTBR(BigDecimal MTBR) {
        this.MTBR = MTBR;
    }

    public BigDecimal getMeanTimeToRepair() {
        return MeanTimeToRepair;
    }

    public void setMeanTimeToRepair(BigDecimal meanTimeToRepair) {
        MeanTimeToRepair = meanTimeToRepair;
    }

    public BigDecimal getTimeBetweenRepairs() {
        return TimeBetweenRepairs;
    }

    public void setTimeBetweenRepairs(BigDecimal timeBetweenRepairs) {
        TimeBetweenRepairs = timeBetweenRepairs;
    }

    public BigDecimal getMeanTimeBetweenRepairs() {
        return MeanTimeBetweenRepairs;
    }

    public void setMeanTimeBetweenRepairs(BigDecimal meanTimeBetweenRepairs) {
        MeanTimeBetweenRepairs = meanTimeBetweenRepairs;
    }
}
