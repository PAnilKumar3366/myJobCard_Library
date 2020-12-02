package com.ods.myjobcard_library.entities.assettree;

/**
 * Created by cindyoakes on 9/11/16.
 */

import java.util.ArrayList;

public class TreeViewNode
{
    private int _nodeLevel;
    private String _isExpanded;
    private String _nodeName;
    private String _nodeId;
    private boolean _isEquipment;
    private ArrayList<TreeViewNode> _nodeChildren;

    public TreeViewNode() {}

    public TreeViewNode(int nodeLevel, String isExpanded, String nodeName, String nodeId, boolean isEquipment, ArrayList<TreeViewNode> nodeChildren)
    {
        this._nodeLevel = nodeLevel;
        this._isExpanded = isExpanded;
        this._nodeName = nodeName;
        this._nodeId = nodeId;
        this._isEquipment = isEquipment;
        this._nodeChildren = nodeChildren;
    }

    public int getNodeLevel()
    {
        return _nodeLevel;
    }

    public void setNodeLevel(int nodeLevel)
    {
        this._nodeLevel = nodeLevel;
    }

    public String getIsExpanded()
    {
        return _isExpanded;
    }

    public void setIsExpanded(String isExpanded)
    {
        this._isExpanded = isExpanded;
    }

    public String getNodeName()
    {
        return _nodeName;
    }

    public void setNodeName(String nodeName)
    {
        this._nodeName = nodeName;
    }

    public ArrayList<TreeViewNode> getNodeChildren()
    {
        return _nodeChildren;
    }

    public void setNodeChildern(ArrayList<TreeViewNode> nodeChildren)
    {
        this._nodeChildren = nodeChildren;
    }

    public String getNodeId() {
        return _nodeId;
    }

    public void setNodeId(String _nodeId) {
        this._nodeId = _nodeId;
    }

    public boolean isEquipment() {
        return _isEquipment;
    }

    public void setIsEquipment(boolean _isEquipment) {
        this._isEquipment = _isEquipment;
    }

}
