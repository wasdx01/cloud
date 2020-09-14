package com.bochao.project.model.util;

import com.bochao.common.util.TreeNode;

/**
 * @description 模型树结构
 * @author: 吕洋洋
 * @date: 2019-07-03 09:38
 */
public class ModelTreeNode extends TreeNode {
    private String udfId;
    private String gloableId;
    private String binded;
    private String isParent;

    public String getUdfId() {
        return udfId;
    }

    public void setUdfId(String udfId) {
        this.udfId = udfId;
    }

    public String getGloableId() {
        return gloableId;
    }

    public void setGloableId(String gloableId) {
        this.gloableId = gloableId;
    }

    public String getBinded() {
        return binded;
    }

    public void setBinded(String binded) {
        this.binded = binded;
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }
}
