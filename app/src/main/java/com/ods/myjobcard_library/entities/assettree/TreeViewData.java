package com.ods.myjobcard_library.entities.assettree;

/**
 * Created by cindyoakes on 9/11/16.
 */

public class TreeViewData {
    private int _level;
    private String _name;
    private String _id;
    private String _parentId;
    private boolean _isEquipment;

    public TreeViewData() {
    }

    public TreeViewData(int level, String name, String id, String parentId, boolean isEquipment) {
        this._level = level;
        this._name = name;
        this._id = id;
        this._parentId = parentId;
        this._isEquipment = isEquipment;
    }

    public int getLevel() {
        return _level;
    }

    public void setLevel(int level) {
        this._level = level;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getID() {
        return _id;
    }

    public void setID(String id) {
        this._id = id;
    }

    public String getParentID() {
        return _parentId;
    }

    public void setParentID(String parentId) {
        this._parentId = parentId;
    }

    public boolean isEquipment() {
        return _isEquipment;
    }

    public void setIsEquipment(boolean isEquipment) {
        this._isEquipment = isEquipment;
    }
}
