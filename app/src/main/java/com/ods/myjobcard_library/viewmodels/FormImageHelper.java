package com.ods.myjobcard_library.viewmodels;

import com.ods.myjobcard_library.ZCollections;
import com.ods.myjobcard_library.entities.ZBaseEntity;
import com.ods.ods_sdk.StoreHelpers.DataHelper;
import com.ods.ods_sdk.entities.ResponseObject;
import com.ods.ods_sdk.entities.odata.ZODataEntity;
import com.sap.smp.client.odata.ODataEntity;

import java.util.ArrayList;
import java.util.List;

public class FormImageHelper {

    /**
     * @param formId Id of the selected form
     * @param version Version of the selected form
     * @param questionId Id of the question for which the images to be fetched
     * @return The list entities containing the image base64 content
     */
    public ArrayList<ZODataEntity> fetchFormQuestionImages(String formId, String version, String questionId){
        return getFormQuestionImages(formId, version, questionId);
    }

    /**
     * @param formId Id of the selected form
     * @param version Version of the selected form
     * @param questionId Id of the question for which the images to be fetched
     * @param imageName Name of the image in form XML
     * @return The single entity containing the image base64 content
     */
    public ZODataEntity fetchFormQuestionImage(String formId, String version, String questionId, String imageName){
        return getFormQuestionImage(formId, version, questionId, imageName);
    }

    private ArrayList<ZODataEntity> getFormQuestionImages(String formId, String version, String questionId){
        ArrayList<ZODataEntity> entities = new ArrayList<>();
        String entitySetName = ZCollections.FORM_IMAGE_ENTITY_SET;
        String resPath = entitySetName+"?$filter=Formid eq '"+ formId +"' and Version eq '"+ version +"' and Question eq '"+ questionId +"'";
        ResponseObject result = DataHelper.getInstance().getEntities(entitySetName,resPath);
        if(result != null && !result.isError()){
            List<ODataEntity> offlineEntities = ZBaseEntity.setODataEntityList(result.Content());
            if(offlineEntities != null && offlineEntities.size() > 0){
                for(ODataEntity entity : offlineEntities){
                    ZODataEntity zoDataEntity = new ZODataEntity(entity);
                    entities.add(zoDataEntity);
                }
            }
        }
        return entities;
    }

    private ZODataEntity getFormQuestionImage(String formId, String version, String questionId, String imageName){
        ZODataEntity entity = null;
        String entitySetName = ZCollections.FORM_IMAGE_ENTITY_SET;
        String resPath = entitySetName+"?$filter=Formid eq '"+ formId +"' and Version eq '"+ version +"' and FileName eq '"+ imageName +"'";
        ResponseObject result = DataHelper.getInstance().getEntities(entitySetName,resPath);
        if(result != null && !result.isError()){
            List<ODataEntity> offlineEntities = ZBaseEntity.setODataEntityList(result.Content());
            if(offlineEntities != null && offlineEntities.size() > 0){
                entity = new ZODataEntity(offlineEntities.get(0));
            }
        }
        return entity;
    }

}
