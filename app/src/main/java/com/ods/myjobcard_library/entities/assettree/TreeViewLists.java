package com.ods.myjobcard_library.entities.assettree;

/**
 * Created by cindyoakes on 9/11/16.
 */



import com.ods.myjobcard_library.ZAppSettings;
import com.ods.myjobcard_library.entities.highvolume.AssetHierarchy;
import com.ods.ods_sdk.utils.DliteLogger;

import java.util.ArrayList;

public class TreeViewLists {

   /* public static ArrayList<TreeViewData> LoadInitialData() {
        ArrayList<TreeViewData> data = FunctionalLocation.loadFLocTreeData();
//        data.addAll(Equipment.loadAssetTreeData());

        return data;
    }

    public static ArrayList<TreeViewNode> LoadInitialNodes(ArrayList<TreeViewData> dataList) {
        ArrayList<TreeViewNode> nodes = new ArrayList<TreeViewNode>();

        for (int i = 0; i < dataList.size(); i++) {
            TreeViewData data = dataList.get(i);
            if (data.getLevel() != 0) continue;

            Log.v("LoadInitialNodes", data.getName());

            TreeViewNode node = new TreeViewNode();
            node.setNodeLevel(data.getLevel());
            node.setIsExpanded(GlobalVariables.TRUE);
            node.setNodeName(data.getName());
            node.setNodeId(data.getID());
            node.setIsEquipment(data.isEquipment());
            int newLevel = data.getLevel() + 1;
            node.setNodeChildern(null);
            ArrayList<TreeViewNode> children = LoadChildrenNodes(dataList, newLevel, data.getID());
            //node.setNodeChildern(LoadChildrenNodes(dataList, newLevel, data.getID()));
            //if (node.getNodeChildren().size() == 0)
            if (children.size() == 0) {
                node.setNodeChildern(null);
            } else {
                node.setNodeChildern(children);
            }

            nodes.add(node);

        }


        return nodes;
    }

    private static ArrayList<TreeViewNode> LoadChildrenNodes(ArrayList<TreeViewData> dataList, int level, String parentID) {
        ArrayList<TreeViewNode> nodes = new ArrayList<TreeViewNode>();

        for (int i = 0; i < dataList.size(); i++) {
            TreeViewData data = dataList.get(i);
            if ((data.getLevel() != level) || (data.getParentID() != parentID)) continue;


            TreeViewNode node = new TreeViewNode();
            node.setNodeLevel(data.getLevel());
            node.setNodeName(data.getName());
            node.setIsExpanded(GlobalVariables.FALSE);
            node.setNodeId(data.getID());
            node.setIsEquipment(data.isEquipment());
            int newLevel = level + 1;
            node.setNodeChildern(null);
            ArrayList<TreeViewNode> children = LoadChildrenNodes(dataList, newLevel, data.getID());
            //node.setNodeChildern(LoadChildrenNodes(dataList, newLevel, data.getID()));
            if (children.size() == 0) {
                node.setNodeChildern(null);
            } else {
                node.setNodeChildern(children);
            }

            nodes.add(node);

            Log.v("LoadChildrenNodes", String.format("%s %d", data.getName(), children.size()));
        }


        return nodes;
    }*/

    public static ArrayList<AssetHierarchy> loadingInitialAssetData() {
        return AssetHierarchy.getAllAssetHierarchies();
    }

    public static ArrayList<TreeViewNode> loadInitialNodes(ArrayList<AssetHierarchy> assetHierarchies) {
        ArrayList<TreeViewNode> arrayList = new ArrayList<>();
        try {
            for (int i = 0; i < assetHierarchies.size(); i++) {
                AssetHierarchy currAsset = assetHierarchies.get(i);

                if (currAsset.getHierLevel() != 0)
                    continue;

                TreeViewNode currNode = new TreeViewNode();
                currNode.setNodeLevel(currAsset.getHierLevel());
                currNode.setIsExpanded(GlobalVariables.FALSE);
                currNode.setNodeName(currAsset.getDescription());
                currNode.setNodeId(currAsset.getObjectId());
                currNode.setIsEquipment(currAsset.getType().equalsIgnoreCase("EQ"));

                ArrayList<TreeViewNode> currNodeChildren = loadChildrenNodes(assetHierarchies, currAsset.getHierLevel() + 1, currAsset.getObjectId());

                currNode.setNodeChildern(currNodeChildren.size() > 0 ? currNodeChildren : null);
                arrayList.add(currNode);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(TreeViewLists.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return arrayList;
    }

    private static ArrayList<TreeViewNode> loadChildrenNodes(ArrayList<AssetHierarchy> initialNodes, int hierarchyLevel, String objectId) {
        ArrayList<TreeViewNode> arrayList = new ArrayList<>();
        try {
            for (int i = 0; i < initialNodes.size(); i++) {
                AssetHierarchy currAsset = initialNodes.get(i);
                if (currAsset.getHierLevel() != hierarchyLevel || !currAsset.getParentId().equalsIgnoreCase(objectId))
                    continue;

                TreeViewNode currNode = new TreeViewNode();
                currNode.setNodeLevel(currAsset.getHierLevel());
                currNode.setNodeName(currAsset.getDescription());
                currNode.setIsExpanded(GlobalVariables.FALSE);
                currNode.setNodeId(currAsset.getObjectId());
                currNode.setIsEquipment(currAsset.getType().equalsIgnoreCase("EQ"));

                ArrayList<TreeViewNode> currNodeChildren = loadChildrenNodes(initialNodes, currAsset.getHierLevel() + 1, currAsset.getObjectId());

                currNode.setNodeChildern(currNodeChildren.size() > 0 ? currNodeChildren : null);
                arrayList.add(currNode);
            }
        } catch (Exception e) {
            DliteLogger.WriteLog(TreeViewLists.class, ZAppSettings.LogLevel.Error, e.getMessage());
        }
        return arrayList;
    }
}
