package com.bochao.project.config;

import com.bochao.bcedc.ApplicationConfig;
import com.bochao.bcedc.BcedcApplication;
import com.bochao.bcedc.data.IDataSource;
import com.bochao.bcedc.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;

/**
 * @Author
 * @Description:
 * @Date:Created in 11:13 2018/11/7
 */
@Configuration
public class BcedcConfig {
    private String name = "";//应用程序名称（可以为空）
    @Value("${mongo.host}")
    private String mongoHost;//数据库地址
    @Value("${mongo.port}")
    private String mongoPort;//数据库端口
    @Value("${mongo.database}")
    private String dataBase;//数据库名称
    @Value("${mongo.username}")
    private String mongoUser;
    @Value("${mongo.password}")
    private String mongoPwd;
    @Value("${fileDir}")
    private String workingDir ;//"E:/model/workingDir";//udf、文件的存储目录
    @Value("${lib_root_linux}")
    private String lib_root_linux ;
    private String cacheDir = "";//gd缓存的存储目录（可以为空，为空时会将缓存存储在workingDIr目录中）
    private BcedcApplication bcedcApplication;
    private BcedcApplication getBcedcApplication(){
        if(bcedcApplication == null){
            bcedcApplication = BcedcApplication.create(getApplicationConfig());
        }
        return bcedcApplication;
    }
    private ApplicationConfig getApplicationConfig(){
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(name);
        applicationConfig.setDataBase(dataBase);
        applicationConfig.setMongoHost(mongoHost);
        applicationConfig.setMongoPort(mongoPort);
        applicationConfig.setMongoUser(mongoUser);
        applicationConfig.setMongoPwd(mongoPwd);
        applicationConfig.setWorkingDir(workingDir);
        applicationConfig.setGimToUdfLibrary(lib_root_linux);
        applicationConfig.setCacheDir(cacheDir);
        return applicationConfig;
    }
    /** MongoDB数据源 */
    @Bean
    public IDataSource getIDataSource(){
        return getBcedcApplication().getDataSource();
    }
    /** 工程服务 */
    @Bean
    public ProjectService getProjectService(){
        return getBcedcApplication().getService("ProjectService");
    }
    /** 场景服务 */
    @Bean
    public SceneService getSceneService(){
        return getBcedcApplication().getService("SceneService");
    }
    /** UDF服务 */
    @Bean
    public UDFService getUdfService(){
        return getBcedcApplication().getService("UDFService");
    }
    /** 图形服务 */
    @Bean
    public GraphService getGraphService(){
        return getBcedcApplication().getService("GraphService");
    }
    /** 缓存服务 */
    @Bean
    public GraphCacheService getGraphCacheService(){
        return getBcedcApplication().getService("GraphCacheService");
    }
    /** 视图服务 */
    @Bean
    public ViewService getViewService(){
        return getBcedcApplication().getService("ViewService");
    }
    /** 属性服务 */
    @Bean
    public PropertyService getPropertyService(){
        return getBcedcApplication().getService("PropertyService");
    }
    /** 模型服务 */
    @Bean
    public EntityService getEntityService(){
        return getBcedcApplication().getService("EntityService");
    }
    /** 图档服务 */
    @Bean
    public DocumentService getDocumentService(){
        return getBcedcApplication().getService("DocumentService");
    }
    /** 文件服务 */
    @Bean
    public FileService getFileService(){
        return getBcedcApplication().getService("FileService");
    }
    /** 关系服务 */
    @Bean
    public RelationshipService getRelationshipService(){
        return getBcedcApplication().getService("RelationshipService");
    }
    /** 模式服务 */
    @Bean
    public SchemaService getSchemaService(){
        return getBcedcApplication().getService("SchemaService");
    }

    public static void main(String[] args) throws FileNotFoundException {
        BcedcConfig bcedcConfig = new BcedcConfig();
        /**
         * ProjectService projectService = bcedcConfig.getProjectService();
         * projectService.createProject("projectName");
         */

        UDFService udfService = bcedcConfig.getUdfService();
        /**
         * InputStream inputStream = new FileInputStream(new File("C:\\Users\\A\\Desktop\\辽宁上线\\丹东灌水AAA.udf"));
         * udfService.addUdf("0412ab76d6524da78541071bfb6b6ee3","丹东灌水AAA.udf",inputStream);
         */
        udfService.buildUdf("367bf898fdcf46e78ca01043b61e57bc");
    }
}
