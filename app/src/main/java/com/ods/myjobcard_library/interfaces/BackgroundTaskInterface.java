package com.ods.myjobcard_library.interfaces;

import com.ods.ods_sdk.entities.odata.ZODataEntity;

import java.util.ArrayList;

/*Created By Anil Kumar*/
public interface BackgroundTaskInterface {
    public void onTaskPostExecute(ArrayList<ZODataEntity> zoDataEntities, boolean isError, String errorMsg);

    public void onTaskPreExecute();

    public void onTaskProgressUpdate();
}
