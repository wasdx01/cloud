package com.bochao.project.model.service;

import com.bochao.project.engineeringManagement.entity.ModelAnalysis;
import com.bochao.project.engineeringManagement.mapper.ModelAnalysisMapper;
import com.bochao.project.engineeringManagement.service.ModelAnalysisService;
import com.bochao.project.model.util.DeleteRedundancyData;
import com.bochao.project.model.util.ThreadPoolUtil;
import com.mongodb.MongoInterruptedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.*;

@Service
@Slf4j
public class ModelService {
    @Resource
    private FacadeService facadeService;
    @Value("${fileDir:}")
    private String fileDir;
    @Resource
    ModelAnalysisService modelAnalysisService;
    @Resource
    GimToUdfParseService gimToUdfParseService;
    @Autowired
    private DeleteRedundancyData deleteRedundancyData;
    @Resource
    ModelAnalysisMapper modelAnalysisMapper;
    @Value("${modelUploadTimeOut}")
    private Long timeout;


    /**
     * 上传模型文件
     *
     * @param modelAnalysis 解析信息
     */
    public boolean addModelFile(ModelAnalysis modelAnalysis, String path, String name, String type) throws FileNotFoundException {
        String projectId = modelAnalysis.getProjectId();
        File file = new File(path);
        ExecutorService executor = ThreadPoolUtil.getInstance();
     //   modelAnalysisService.saveModelFile(modelAnalysis);
        final InputStream in = new FileInputStream(file);
        Future future = executor.submit(() -> {
            try {
                gimExecute(in, name, type, modelAnalysis);
            } catch (Exception e) {
                e.printStackTrace();
                //杀进程（可能进程卡住）
                deleteRedundancyData.timeoutHandle(modelAnalysis);
                //删除转换或者切片后的冗余数据
                try {
                    modelAnalysisService.delModelByProjectId(projectId);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        try {
            future.get(timeout, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.error("gim上传进程意外中断--" + projectId + "---" + name);
            e.printStackTrace();
            futureTimeout(modelAnalysis);
            future.cancel(true);
        } catch (ExecutionException e) {
            log.error("gim上传进程服务中断--" + projectId + "---" + name);
            e.printStackTrace();
            futureTimeout(modelAnalysis);
            future.cancel(true);
        } catch (TimeoutException e) {
            log.error("gim上传进程服务超时中断--" + projectId + "---" + name);
            e.printStackTrace();
            futureTimeout(modelAnalysis);
            future.cancel(true);
        } catch (Exception e) {
            log.error("gim上传异常中断--" + projectId + "---" + name);
            e.printStackTrace();
            futureTimeout(modelAnalysis);
            future.cancel(true);
        }
        return true;
    }

    public void futureTimeout(ModelAnalysis modelAnalysis) {
        //杀进程（可能进程卡住）
        deleteRedundancyData.timeoutHandle(modelAnalysis);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        //删除转换或者切片后的冗余数据
        try {
            modelAnalysisService.delModelByProjectId(modelAnalysis.getProjectId());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    synchronized public void gimExecute(InputStream stream, String name, String type, ModelAnalysis modelAnalysis) throws InterruptedException, MongoInterruptedException {
        log.debug("@@mwz@@文件" + name + "开始处理");
        //1.判断文件是gim还是UDF
        String fileType = name.substring(name.lastIndexOf(".") + 1);
        if ("gim".equalsIgnoreCase(fileType)) {
            String udfPath = "";
            //gim文件转换
            modelAnalysis.setStatus(1);
            modelAnalysis.setTransferTime(new Date());
            modelAnalysisService.update(modelAnalysis);
            log.debug("@@mwz@@文件" + name + "开始转换");
            // gim格式转udf格式
            udfPath = gimToUdfParseService.convert2udf(stream, name, type);
            ModelAnalysis modelAnalysis1 = modelAnalysisMapper.selectByPrimaryKey(modelAnalysis.getId());
            //如果转换过程中模型被删掉，则停止切片
            if (modelAnalysis1 == null) return;
            log.debug("@@mwz@@文件" + name + "转换完毕");
            if (StringUtils.isNotBlank(udfPath)) {
                Thread.sleep(1000);
                File files = new File(udfPath);
                name = udfPath.substring(udfPath.lastIndexOf("/"));
                log.debug("udfPath:" + udfPath);
                try {
                    InputStream input = new FileInputStream(files);
                    modelAnalysis.setStatus(3);
                    modelAnalysis.setModelTime(new Date());
                    modelAnalysisService.update(modelAnalysis);
                    log.debug("@@mwz@@文件" + name + "开始UDF解析");
                    name = udfPath.substring(udfPath.lastIndexOf("/") + 1);
                    String udfId = facadeService.addUdf(modelAnalysis.getProjectId(), name, input, type);
                    modelAnalysis.setStatus(4);
                    modelAnalysis.setUdfId(udfId);
                    modelAnalysisService.update(modelAnalysis);
                    log.debug("@@mwz@@文件" + name + "UDF解析切片成功");
                } catch (Exception e) {
                    log.error("UDF文件处理失败", e);
                    modelAnalysis.setStatus(5);
                    modelAnalysisService.update(modelAnalysis);
                }
            } else {
                //转换失败
                modelAnalysis.setStatus(2);
                modelAnalysisService.update(modelAnalysis);

            }
        } else if ("udf".equalsIgnoreCase(fileType)) {
            modelAnalysis.setStatus(3);
            modelAnalysis.setModelTime(new Date());
            modelAnalysisService.update(modelAnalysis);
            log.debug("@@mwz@@文件" + name + "开始UDF解析");
            try {
                String udfId = facadeService.addUdf(modelAnalysis.getProjectId(), name, stream, "0");
                modelAnalysis.setStatus(4);
                modelAnalysis.setUdfId(udfId);
                modelAnalysisService.update(modelAnalysis);
            } catch (Exception e) {
                log.error("UDF文件处理失败", e);
                modelAnalysis.setStatus(5);
                modelAnalysisService.update(modelAnalysis);
            }
            log.debug("@@cxf@@文件" + name + "UDF解析完毕");
        }
    }





}
