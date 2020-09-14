package com.bochao.project.model.forkTask;

import com.bochao.bcedc.model.ViewNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 *  郜卫平
 *  2019-1-8
 *  带返回结果的分治任务类
 *  以大任务分解小任务的思想处理mogodb转java对象
 */
public class ViewTask extends RecursiveTask<List<ViewNode>> {
    private List<org.bson.Document> documents;
    private int start;
    private int end;
    public ViewTask(List<org.bson.Document> documents,int start,int end){
        this.documents = documents;
        this.start=start;
        this.end=end;
    }
    /** 主要逻辑处理 ，RecursiveTask 为模板模式*/
    @Override
    protected List<ViewNode> compute() {
        List<ViewNode> result = new ArrayList<>();
        /** 不大于1500条的小任务*/
        if(end-start<=1500){
            org.bson.Document d;
            for(int i=start;i<end && i<documents.size();i++){
                d = documents.get(i);
                /** 转换ViewNode对象逻辑*/
                ViewNode viewNode = new ViewNode();
                viewNode.setViewNodeId((String)d.get("viewNodeId"));
                viewNode.setNodeName((String)d.get("nodeName"));
                viewNode.setViewId((String)d.get("viewId"));
                viewNode.setParentId((String)d.get("parentId"));
                viewNode.setEntity((boolean)d.get("isEntity"));
                result.add(viewNode);
            }
        /** 大于1500条的数据分两部分处理，时间复杂度为lgn*/
        }else {
            int mid=(start+end)/2;
            ViewTask left= new ViewTask(documents,start,mid);
            ViewTask right=new ViewTask(documents,mid,end);
            left.fork();
            right.fork();
            result.addAll(left.join());
            result.addAll(right.join());
        }
        return result;
    }
}
