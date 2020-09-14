package com.bochao.project.model.util;

import com.bochao.project.engineeringManagement.entity.ModelAnalysis;
import com.bochao.project.engineeringManagement.service.ModelAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class DeleteRedundancyData {

    @Resource
    ModelAnalysisService modelAnalysisService;
    private Logger logger = LoggerFactory.getLogger(DeleteRedundancyData.class);

    /**
     * 超时进程获取
     * @param modelAnalysis
     */
    public void timeoutHandle(ModelAnalysis modelAnalysis){
        Map<String,Object> log = new HashMap<>();
        killProcess("BC_CDK_UDF");
        killProcess("CacheGenerator");
        modelAnalysis.setStatus(6);
        Boolean result = runMem();
        if(result){
            modelAnalysis.setStatus(7);
        }
        modelAnalysisService.update(modelAnalysis);
    }

    /**
     * 超时查看内存
     * @return
     */
    public Boolean runMem(){
        String path = System.getProperty("user.dir")+ "/sh/memuse.sh";
        Process process = CommandUtil.command("sh",path);
        String errMsg = CommandUtil.getErrMsg(process);
        String msg = CommandUtil.getMsg(process);
        logger.debug("runMem:"+msg+","+errMsg);
        if(errMsg==null&&msg!=null){
            String[]msgs = msg.split(",");
            logger.debug(msgs[0]+","+msgs[1]);
            int num = 100-(Integer.valueOf(msgs[1])/Integer.valueOf(msgs[0])*100);
            if(num >95){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    public void killProcess(String name){
        String path = System.getProperty("user.dir")+ "/sh/killProcess.sh";
        Process process = CommandUtil.command("sh", path,name);
        String errMsg = CommandUtil.getErrMsg(process);
        String msg = CommandUtil.getMsg(process);
        logger.debug("result:"+msg+","+errMsg);
    }

}
