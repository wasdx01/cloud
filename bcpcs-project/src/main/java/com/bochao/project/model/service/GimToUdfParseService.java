package com.bochao.project.model.service;


import com.bochao.bcedc.graphcache.GIMFileConvert;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author 陈晓峰
 * @Description: 文档操作
 * @date 2018/12/14 13:15
 */
@Service
public class GimToUdfParseService implements GimToUdfParse {
    private Logger logger = LoggerFactory.getLogger(GimToUdfParseService.class);
    @Value("${convertTimes}")
    private String convertTimes;
    @Value("${saveGimDir:}")
    private String saveGimDir;
    @Value("${lib_root_linux:}")
    protected String libRootLinux;
    @Value("${lib_root_Windows:}")
    protected String libRootWindows;


    /**
     * @param stream gim文件流
     * @param type   0为变电 1为线路
     * @Description: gim转udf
     * @author 陈晓峰
     * @date 2019/1/14 10:29
     */
    @Override
    public String convert2udf(InputStream stream, String name, String type) {
        String filename = name.substring(name.lastIndexOf("\\") + 1);
        String uuid = UUID.randomUUID().toString();
        String currentCahceDir = saveGimDir + uuid + "/";
        String desPath = currentCahceDir + uuid + ".udf";
        File udfFile = new File(desPath);
        try {
            File modelLocalFile = new File(currentCahceDir + filename);
            FileUtils.copyInputStreamToFile(stream, modelLocalFile);
            logger.info("开始解析");
            String i_cFilePath = modelLocalFile.getAbsolutePath();
            String absolutePath = udfFile.getAbsolutePath();
            logger.info("文件获取路径：" + i_cFilePath);
            logger.info("文件保存路径：" + absolutePath);
            logger.info("工程类型：" + type);
            int times = Integer.parseInt(convertTimes);
            logger.info("转换超时时间：" + times);
            int i = GIMFileConvert.INSTANCE.convert2udf(i_cFilePath, absolutePath, type, times);
            logger.info("解析结束返回值：" + i + "");
            //删除解析文件生成的无用临时文件
            File temp_file = File.createTempFile("963852temp", ".modeltemp");
            recursiveDeleteFile(new File(temp_file.getParent() + "\\" + uuid + ".fbm"));
            if (temp_file.exists()) {
                boolean tempFileDeleteResult = temp_file.delete();
                if (!tempFileDeleteResult) {
                    logger.info("文件删除失败----temp_file");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return desPath;
    }

    /**
     * @param file 文件或者文件夹
     *             递归删除文件
     * @param file 文件
     * @author 陈晓峰
     * @date 2019/1/14 10:29
     */
    private void recursiveDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            //空文件夹
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                recursiveDeleteFile(f);
            }
            file.delete();
        }
    }
}
