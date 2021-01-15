package com.ods.myjobcard_library.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.ZConfigManager;
import com.ods.myjobcard_library.workers.ScheduleWork;
import com.ods.ods_sdk.AppSettings;
import com.ods.ods_sdk.StoreHelpers.BaseEntity;
import com.ods.ods_sdk.StoreHelpers.TableConfigSet;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.utils.ConfigManager;
import com.ods.ods_sdk.utils.DliteLogger;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.impl.ODataDurationDefaultImpl;
import com.sap.smp.client.odata.impl.ODataPropertyDefaultImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import static com.ods.ods_sdk.utils.DliteLogger.WriteLog;

public class ZBaseEntity extends BaseEntity implements Parcelable {

    public static final Creator<ZBaseEntity> CREATOR = new Creator<ZBaseEntity>() {
        @Override
        public ZBaseEntity createFromParcel(Parcel in) {
            return new ZBaseEntity(in);
        }

        @Override
        public ZBaseEntity[] newArray(int size) {
            return new ZBaseEntity[size];
        }
    };
    private static HashMap<String, String> ParentEntityErrors;

    public ZBaseEntity() {
    }

    protected ZBaseEntity(Parcel in) {
        this.setEntityResourcePath(in.readString());
        this.setEntitySetName(in.readString());
        this.setEntityType(in.readString());
        this.setNavigationPropertyName(in.readString());
        this.setNCopy(in.readInt());
       /* this.setHeaderText(in.readString());
        this.setDeliveryNote(in.readString());*/
        int tmpMode = in.readInt();
        this.setMode(tmpMode == -1 ? null : AppSettings.EntityMode.values()[tmpMode]);
        this.items = new ArrayList<BaseEntity>();
        in.readList(this.items, ZBaseEntity.class.getClassLoader());
        NCopy = in.readInt();
    }

    @Override
    public ResponseObject toEntity(ODataEntity entity) {
        ResponseObject result;
        Class<?> cls;
        ArrayList<Field> declaredFields = new ArrayList<>();
        Method method;
        try {
            cls = this.getClass();

            declaredFields.addAll(Arrays.asList(cls.getDeclaredFields()));
            declaredFields.addAll(Arrays.asList(cls.getSuperclass().getDeclaredFields()));


            for (Field field : declaredFields) {
                //field.setAccessible(true);
                //classFields.add(field.getName());

                if (!field.getType().getSimpleName().equalsIgnoreCase("Creator") && !field.getType().getSimpleName().equalsIgnoreCase("DateFormat") && !field.getType().getSimpleName().equalsIgnoreCase("AppSettings.EntityMode")) {
                    try {
                        method = cls.getMethod("get" + field.getName());

                        Object obj = method.invoke(this);
                        if (obj == null)
                            continue;

                        if (field.getType() != Time.class) {

                            if (field.getType() == GregorianCalendar.class) {
                                GregorianCalendar dt = (GregorianCalendar) obj;
                                if (dt.getTimeInMillis() == ConfigManager.getDefaultCalendarVal().getTimeInMillis())
                                    continue;

                                if(!entity.getEntityType().equalsIgnoreCase(ZCollections.USER_TIMESHEET_ENTITY_TYPE)
                                        && !entity.getEntityType().equalsIgnoreCase(ZCollections.WO_CONFIRMATION_ENTITY_TYPE)) {
                                    dt.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    dt.get(Calendar.HOUR_OF_DAY);
                                }
                                entity.getProperties().put(field.getName(),
                                        new ODataPropertyDefaultImpl(field.getName(),
                                                dt));
                                continue;
                            }
                            entity.getProperties().put(field.getName(),
                                    new ODataPropertyDefaultImpl(field.getName(),
                                            obj));
                        } else {
                            Time timeValue;
                            timeValue = (Time) obj;
                            GregorianCalendar dtCal;
                            dtCal = (GregorianCalendar) GregorianCalendar.getInstance();
                            dtCal.setTimeInMillis(timeValue.getTime());
                            com.sap.smp.client.odata.ODataDuration oDataDuration = new ODataDurationDefaultImpl();
                            oDataDuration.setDays(0);
                            oDataDuration.setHours(dtCal.get(Calendar.HOUR_OF_DAY));
                            oDataDuration.setMinutes(dtCal.get(Calendar.MINUTE));
                            oDataDuration.setSeconds(new BigDecimal(dtCal.get(Calendar.SECOND)));
                            entity.getProperties().put(field.getName(),
                                    new ODataPropertyDefaultImpl(field.getName(),
                                            oDataDuration));

                        }

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        WriteLog(getClass(), AppSettings.LogLevel.Error, e.getLocalizedMessage());
                    }

                }
            }
            result = new ResponseObject(ConfigManager.Status.Success, "", entity);
        } catch (Exception e) {
            WriteLog(getClass(), AppSettings.LogLevel.Error, e.getLocalizedMessage());
            result = new ResponseObject(ConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    @Override
    public ResponseObject SaveToStore(boolean autoFlush) {
        ResponseObject result = super.SaveToStore(autoFlush);
        try {
            if (!result.isError()) {
                //Update Backend if Bg Sync is enabled
                if (autoFlush && ZConfigManager.EventBased_Sync.equalsIgnoreCase("x") && !ZAppSettings.IS_DEMO_MODE) {
                    ScheduleWork scheduleWork = new ScheduleWork(getEntitySetName());
                    scheduleWork.workSchedule();
                }
            }
        } catch (Exception e) {
            WriteLog(getClass(), AppSettings.LogLevel.Error, e.getLocalizedMessage());
            result = new ResponseObject(ConfigManager.Status.Error, e.getMessage(), null);
        }
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      /*  dest.writeByte(this.isPostError ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMsg);*/
        dest.writeString(getEntityResourcePath());
        dest.writeString(getEntitySetName());
        dest.writeString(getEntityType());
        dest.writeString(getNavigationPropertyName());
        dest.writeInt(this.getNCopy());
        /*dest.writeString(this.HeaderText);
        dest.writeString(this.DeliveryNote);*/
        dest.writeInt(this.getMode() == null ? -1 : this.getMode().ordinal());
        dest.writeList(this.items);
    }

    public void setObjFieldValue(String fieldName, Object fieldValue) {
        try {
            Class<?> cls = this.getClass();
            Field field = cls.getDeclaredField(fieldName);
            Class<?> type = field.getType();
            //set value for all fields
            Method method = cls.getDeclaredMethod("set" + fieldName, field.getType());
            if (fieldValue != null) {
                if (type == Time.class) {
                    //method.invoke(this, setTime((String.valueOf(pr.getValue()))));
                    method.invoke(this, getTimeInstance(String.valueOf(fieldValue)));
                } else if (type == GregorianCalendar.class) {
                    method.invoke(this, getDateInstance(String.valueOf(fieldValue)));
                } else
                    method.invoke(this, fieldValue);
            } else {
                if (type == GregorianCalendar.class) {
                    method.invoke(this, ZConfigManager.getDefaultCalendarVal());
                } else if (type == String.class) {
                    method.invoke(this, "");
                } else if (type == Time.class) {
                    method.invoke(this, setTime(ZConfigManager.getDefaultCalendarVal().getTimeInMillis()));
                } else if (type == BigDecimal.class || type == float.class) {
                    method.invoke(this, new BigDecimal(0));
                }
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(getClass(), AppSettings.LogLevel.Warning, e.getMessage());
        }
    }

    public Time getTimeInstance(String timeString) {
        try {
            if (timeString.contains(".000"))
                timeString = timeString.split(".000")[0];
            return Time.valueOf(timeString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GregorianCalendar getDateInstance(String dateString) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        GregorianCalendar gregorianCalendar = (GregorianCalendar) Calendar.getInstance();
        try {
            Date date = sdf.parse(dateString);
            gregorianCalendar.setTime(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gregorianCalendar;
    }

    public String parseErrorMsg(String errorMsg) {
        String parsedErrorMsg = "";
        try {
            if ((errorMsg == null || errorMsg.isEmpty()) && ParentEntityErrors != null)
                errorMsg = ParentEntityErrors.get(this.getEntityResourcePath());
            if (errorMsg != null && !errorMsg.isEmpty()) {
                String[] errorMsgs = errorMsg.split(";");
                int msgEndIndex, resPathEndIndex, keyValuesIndex = 0, counter = 0;
                String msg, entityName, keyValues = "";
                for (String error : errorMsgs) {
                    msgEndIndex = error.indexOf("/");
                    resPathEndIndex = error.length();
                    if (error.contains("("))
                        keyValuesIndex = error.indexOf("(");
                    msg = error.substring(0, msgEndIndex);
                    entityName = error.substring(msgEndIndex + 1, keyValuesIndex == 0 ? resPathEndIndex : (keyValuesIndex - 1));
                    if (keyValuesIndex != 0)
                        keyValues = error.substring(keyValuesIndex);
                    parsedErrorMsg += "Error in " + TableConfigSet.getDisplayName(entityName) + (!keyValues.isEmpty() ? keyValues : "") + "\n";
                    parsedErrorMsg += "Error: " + msg + "\n";
                    if (errorMsgs.length > 1 && counter < (errorMsgs.length - 1))
                        parsedErrorMsg += "\n";
                }
            }
        } catch (Exception e) {
            WriteLog(ZBaseEntity.class, AppSettings.LogLevel.Error, e.getMessage());
        }
        return parsedErrorMsg;
    }

}
