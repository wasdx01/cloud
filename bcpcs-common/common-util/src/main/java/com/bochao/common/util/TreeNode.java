package com.bochao.common.util;


import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class TreeNode {
    private String id;
    private String parentId;
    private String name;
    private Integer level;
    private Integer type=0;
    private List<TreeNode> children = new ArrayList<>();

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public TreeNode() {
    }

    public TreeNode(String id, String parentId, String name, Integer type) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
    }

    public TreeNode(String id, String parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }

    /**
     * list排序(递归遍历方式)
     */
    public static List<TreeNode> turnList(List<TreeNode> list){
        TreeNode root = null;
        for(TreeNode node : list){
            if(StringUtils.isEmpty(node.getParentId())){
                root = listToTree(node, list);
                break;
            }
        }
        List<TreeNode> nodeList = new ArrayList<>();
        if (root == null){
            return nodeList;
        }
        return treeToList(root, nodeList);
    }

    private static TreeNode listToTree(TreeNode node, List<TreeNode> list) {
        for (TreeNode child : list) {
            if (node.getId().equals(child.getParentId())) {
                node.getChildren().add(listToTree(child, list));
            }
        }
        return node;
    }
    public static List<TreeNode> treeToList(TreeNode node, List<TreeNode> list) {
        list.add(node);
        if (node.getChildren().size() > 0) {
            node.getChildren().forEach(child -> treeToList(child, list));
            node.setChildren(null);
        }
        return list;
    }
    public static List<TreeNode> treeToList1(TreeNode node, List<TreeNode> list) {
        list.add(node);
        if (node.getChildren()!=null&&node.getChildren().size() > 0) {
            node.getChildren().forEach(child -> treeToList1(child, list));
        }
        return list;
    }
    /**
     * list排序(循环遍历方式)
     * @author 吕洋洋
     * @date 2019-07-03 10:42
     * @param list
     * @return List<TreeNode>
     */
    public static <T extends TreeNode> List<T> turnListByLoop(List<T> list) {
        T root = null;
        Map<String, T> map = list.stream().collect(Collectors.toMap(T::getId, node -> node));
        //        //        // list转tree
        for (T parent : list) {
            // 找到根，默认根节点唯一
            if (root == null && (StringUtils.isEmpty(parent.getParentId()) || "0".equals(parent.getParentId()))) {
                root = parent;
            }else if(map.get(parent.getParentId()) != null){
                // 找到父
                map.get(parent.getParentId()).getChildren().add(parent);
            }
        }
        // tree转list
        return treeToList(root);
    }

    /**
     * list 转 tree
     * @author 吕洋洋
     * @date 2020-02-05 13:42
     * @param list 节点集合
     * @param rootId 根节点Id
     * @return T extends TreeNode
     */
    public static <T extends TreeNode> T listToTree(List<T> list, String rootId) {
        T root = null, parent;
        Map<String, T> map = list.parallelStream().collect(Collectors.toMap(T::getId, node -> node));
        // list转tree
        for (T node : list) {
            // 找到根，默认根节点唯一
            if (root == null && StringUtils.equals(rootId, node.getId())) {
                root = node;
            }else if((parent = map.get(node.getParentId())) != null){
                if(parent.getChildren()==null)
                    parent.setChildren(new ArrayList<>());
                // 找到父
                parent.getChildren().add(node);
            }
        }
        return root;
    }

    public static <T extends TreeNode> List<T> treeToList(T root) {
        List<T> list = new ArrayList<>();
        if (root == null) {
            return list;
        }
        // 使用Stack进行深度遍历（先进后出）
        Stack<T> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            T tree = stack.pop();
            list.add(tree);
            // 子节点倒序入栈
            Collections.reverse(tree.getChildren());
            tree.getChildren().forEach(child -> stack.push((T)child));
            tree.setChildren(null);
        }
        return list;
    }

}
