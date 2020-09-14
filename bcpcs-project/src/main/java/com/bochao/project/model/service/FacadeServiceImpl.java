package com.bochao.project.model.service;

import com.bochao.bcedc.ServiceType;
import com.bochao.bcedc.graphcache.UgxGenerateCache;
import com.bochao.bcedc.model.Coordinate;
import com.bochao.bcedc.model.Graph;
import com.bochao.bcedc.model.GraphCacheIndex;
import com.bochao.bcedc.model.UDF;
import com.bochao.bcedc.service.*;
import com.bochao.bcedc.utils.DirectoryUtils;
import com.bochao.project.model.event.LinePositionEvent;
import com.bochao.project.model.event.ModelAnalysisEvent;
import com.bochao.project.model.event.ModelDelFileEvent;
import com.bochao.project.model.event.ModelIdEvent;
import com.bochao.project.model.forkTask.CommandUtil;
import com.bochao.project.model.forkTask.GraphType;
import com.bochao.project.model.forkTask.ModelId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * @Author 郜卫平
 * @Description:
 * @Date:Created in 13:48 2018/11/8
 */
@Component
@Slf4j
public class FacadeServiceImpl implements FacadeService, ApplicationContextAware {
    @Autowired
    private UDFService udfService;
    @Autowired
    private GraphService graphService;
    @Autowired
    private GraphCacheService graphCacheService;
    @Autowired
    private ViewService viewService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Value("${convertTimes}")
    private Integer convertTimes;
    @Value("${lib_root_linux}")
    private String lib_root;
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }



    @Override
    public String addUdf(String projectId, String udfName, InputStream stream, String projectType) {
        ServiceResult<UDF> udfServiceResult = udfService.addUdf(projectId, udfName, stream);
        if (udfServiceResult.isSuccess()) {
            UDF udf = udfServiceResult.getData();
            log.debug("@@mwz@@" + udf.getUdfPath() + "模型地址");
            ServiceResult<Boolean> result = buildUdf(udf, projectId, projectType);
            HashMap map = new HashMap();
            map.put("success", result.isSuccess());
            map.put("udf", udf);
            applicationContext.publishEvent(new ModelAnalysisEvent(map));
            return udf.getUdfId();
        }
        return null;
    }

    /**
     * 切片缓存udf
     *
     * @return
     */
    private ServiceResult<Boolean> graphCache(List<Graph> list) {
        Iterator var5 = list.iterator();
        while (var5.hasNext()) {
            Graph graph = (Graph) var5.next();
            ServiceResult<EnvironmentResult> result = graphService.buildGraphCache(graph.getGraphId(), graph.getParameter());
            if (!result.isSuccess()) {
                return ServiceResult.internalErrorResult();
            }
        }
        return ServiceResult.successResult(true);
    }


    public Boolean extractUdf(String udfId) {
        boolean result = false;
        try {
            Query query = new Query(Criteria.where("_id").is(udfId));
            UDF existedUdf = mongoTemplate.findOne(query, UDF.class, "udf");
            existedUdf.setUdfId(udfId);
            FbxUdfParse udfFormatExtract = new FbxUdfParse(udfService.getContext(), null);
            if (udfFormatExtract != null && existedUdf != null) {
                udfFormatExtract.extract(existedUdf).save();
                List<ModelId> modelIds = udfFormatExtract.getModelIds();
                applicationContext.publishEvent(new ModelIdEvent(modelIds));
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }

    /**
     * @desc 解析模型前并修改入库viewnode方法
     * @author 陈晓峰
     * @date 2019-10-28
     */
    public ServiceResult<Boolean> buildUdf(UDF udf, String projectId, String projectType) {
        try {
            log.debug("@@mwz@@" + udf.getUdfId() + "模型数据入库开始");
            extractUdf(udf.getUdfId());
            log.debug("@@mwz@@" + udf.getUdfId() + "模型数据入库结束-----");
            Thread.sleep(1000);
            log.debug("@@mwz@@" + udf.getUdfId() + "模型数据切片开始");
            GraphService graphService = udfService.getContext().getService("GraphService");
            ServiceResult<List<Graph>> allGraphs = graphService.getGraphByUdfId(udf.getUdfId());
            if (allGraphs.isSuccess()) {
                List<Graph> list = allGraphs.getData();
                String path = list.get(0).getGraphPath();
                log.debug("@@mwz@@ graph地址" + path);
                String type = path.substring(path.lastIndexOf("."));
                log.debug("@@mwz@@ 模型类型： " + type);
                if (type.equals(".ugd")) {
                    log.debug("ive切片开始");
                    this.graphCache(list);
                } else {
                    log.debug("ugx切片开始");
                    this.generateGraphCache(udf.getUdfId(), projectId, projectType);
                }
            }
            log.debug("@@mwz@@" + udf.getUdfId() + "模型数据切片结束-----");
            if (projectType.equals("1")) {
                HashMap map = new HashMap();
                map.put("projectId", projectId);
                applicationContext.publishEvent(new LinePositionEvent(map));
            }
            return ServiceResult.successResult(true);
        } catch (Exception e) {
            HashMap map = new HashMap();
            map.put("udfId", udf.getUdfId());
            map.put("projectId", udf.getProjectId());
            applicationContext.publishEvent(new ModelDelFileEvent(map));
            return ServiceResult.successResult(false);
        }
    }

    public ServiceResult<Boolean> generateGraphCache(String udfId, String projectId, String projectType) throws Exception {
        if (projectId == null) {
            throw new Exception("构建ugx图形缓存未能找到projectId");
        }
        //工程id所在目录
        String projectDir = DirectoryUtils.generateDir(graphCacheService.getContext().getConfig().getWorkingDir(), DirectoryUtils.Root, projectId);
        log.info(projectDir);
        //解析udf后Graph所在目录
        String graphDir = DirectoryUtils.generateDir(projectDir, DirectoryUtils.Extract, udfId, "Graph");
        log.info(graphDir);
        File graphFileDir = new File(graphDir);
        if (graphFileDir.exists()) {
            GraphService graphService = graphCacheService.getContext().getService(ServiceType.Graph);
            //经纬度参数集合
            Map<String, Double> stringDoubleMap = new HashMap<>();
            //ugx原有名称与更改名称后的全路径集合
            Map<String, String> allUgxFilePathMap = getAllUgxPath(graphService, udfId, stringDoubleMap, projectType);

            //变电
            if (projectType.equals("0")) {
                //初始化土建、电气ugx图形文件路径集合
                List<String> ifcGraphPathList = new ArrayList<>();
                List<String> electricPathPathList = new ArrayList<>();
                initGraphPathListData(graphFileDir, allUgxFilePathMap, ifcGraphPathList, electricPathPathList);
                log.info("构建土建图形缓存开始");
                //构建土建图形缓存
                ServiceResult<Boolean> buildIfcCacheResult = buildIfcCache(projectId, udfId, ifcGraphPathList, stringDoubleMap);
                log.info("结果：" + buildIfcCacheResult.isSuccess() + "," + buildIfcCacheResult.getMessage());
                log.info("构建电气图形缓存开始");
                //构建电气图形缓存
                ServiceResult<Boolean> buildElectricCacheResult = buildElectricCache(projectId, udfId, electricPathPathList, stringDoubleMap);
                log.info("结果：" + buildElectricCacheResult.isSuccess() + "," + buildElectricCacheResult.getMessage());
                //清空集合
                stringDoubleMap.clear();
                allUgxFilePathMap.clear();
                ifcGraphPathList.clear();
                electricPathPathList.clear();
                return ServiceResult.customResult(buildIfcCacheResult.isSuccess() && buildElectricCacheResult.isSuccess(), null, buildIfcCacheResult.getMessage() + ";" + buildElectricCacheResult.getMessage());
                //输电
            } else if (projectType.equals("1")) {
                //构建线路图形缓存
                log.info("构建线路图形缓存开始");
                return buildLineCache(projectId, udfId, allUgxFilePathMap, stringDoubleMap);
            }
        }
        return ServiceResult.internalErrorResult();
    }

    /**
     * 构建电气图形缓存
     *
     * @param projectId            工程id
     * @param udfId                udf标识
     * @param electricPathPathList 图形集合
     * @param stringDoubleMap      经纬度等信息
     * @return bool
     */
    private ServiceResult<Boolean> buildElectricCache(String projectId, String udfId, List<String> electricPathPathList, Map<String, Double> stringDoubleMap) {

        if (electricPathPathList.size() > 0) {
            //工程id所在目录
            String projectDir = DirectoryUtils.generateDir(graphCacheService.getContext().getConfig().getWorkingDir(), DirectoryUtils.Root, projectId);

            UgxBuildCacheParams cacheParams = getUgxBuildCacheParams("STD", "substation");
            log.info("electricPathPathList:" + electricPathPathList.toString());
            //数组存放电气图形路径，加结束标志。
            String[] electricGraphPaths = new String[electricPathPathList.size() + 1];
            electricPathPathList.toArray(electricGraphPaths);
            electricGraphPaths[electricPathPathList.size()] = null;

            //构建缓存并存入数据库
            String electricUuid = UUID.randomUUID().toString();
            String electricCacheDir = DirectoryUtils.generateDir(projectDir, "cache", "electric", electricUuid);

            int electricUgxCacheResult = UgxGenerateCache.Instance.UgxGenerateEx(
                    electricGraphPaths,
                    electricCacheDir,
                    "electric",
                    cacheParams.getFloatLod(),
                    cacheParams.getFloatBoxSize(),
                    cacheParams.getLength(),
                    cacheParams.getWidth(),
                    cacheParams.getnMaxLodLevel(),
                    cacheParams.getnMaxTreeDepth(),
                    cacheParams.getnRootVolumeFactor(),
                    convertTimes);

            log.info("---electricUgxCacheResult:" + electricUgxCacheResult + "---");

            if (electricUgxCacheResult == 1) {
                if (new File(electricCacheDir + File.separator + "electric.bcm").exists()) {
                    String graphCacheIndexPath = DirectoryUtils.generateDir(DirectoryUtils.Root, projectId, "cache", "electric", electricUuid, "electric.bcm");
                    addGraphCacheIndexByMongodb(projectId, udfId, "electric.bcm", graphCacheIndexPath, stringDoubleMap, "electric");
                    return backResult(true, "electric构建缓存成功");
                } else {
                    return backResult(false, "electric.bcm不存在");
                }

            } else if (electricUgxCacheResult == -1) {
                return backResult(false, "electric模型构建缓存超时");
            } else {
                return backResult(false, "electric模型构建缓存失败");
            }
        } else {
            return backResult(true, "无电气模型");
        }
    }

    private void addGraphCacheIndexByMongodb(String projectId, String udfId, String graphCacheIndexName, String graphCacheIndexPath, Map<String, Double> stringDoubleMap, String graphType) {
        GraphCacheIndex graphCacheIndex = new GraphCacheIndex();
        graphCacheIndex.setProjectId(projectId);
        graphCacheIndex.setUdfId(udfId);
        graphCacheIndex.setGraphCacheIndexName(graphCacheIndexName);
        graphCacheIndex.setGraphCacheIndexPath(graphCacheIndexPath);
        graphCacheIndex.setGraphType(graphType);
        graphCacheIndex.setLon(stringDoubleMap.getOrDefault("lon", 0.0));
        graphCacheIndex.setLat(stringDoubleMap.getOrDefault("lat", 0.0));
        graphCacheIndex.setHeight(stringDoubleMap.getOrDefault("height", 0.0));
        graphCacheIndex.setAngle(stringDoubleMap.getOrDefault("angle", 0.0));
        graphCacheIndex.setxOffect(stringDoubleMap.getOrDefault("xOffect", 0.0));
        graphCacheIndex.setyOffect(stringDoubleMap.getOrDefault("yOffect", 0.0));
        graphCacheIndex.setzOffect(stringDoubleMap.getOrDefault("zOffect", 0.0));
        mongoTemplate.save(graphCacheIndex, "graphCacheIndex");
    }

    /**
     * 解析xml配置文件，获取构建缓存lod参数信息
     *
     * @param modelType STD:变电 TLD:输电
     * @param graphType IFC:土建 substation:变电站模型
     * @return 属性实体类
     */
    private UgxBuildCacheParams getUgxBuildCacheParams(String modelType, String graphType) {

        //ugx构建缓存配置文件目录
        String xmlFilePath = lib_root + "/config/UgtSet.xml";

        //处理ugx构建缓存参数
        if (new File(xmlFilePath).exists()) {
            Map<String, String> stringMap = CommandUtil.parseUgtSetXml(xmlFilePath, modelType, graphType);
            if (stringMap != null) {
                UgxBuildCacheParams buildCacheParams = new UgxBuildCacheParams();
                buildCacheParams.setFloatBoxSize(CommandUtil.getLodRangeList(stringMap.get("boxSize")));
                buildCacheParams.setFloatLod(CommandUtil.getLodRangeList(stringMap.get("lodRangeList")));
                buildCacheParams.setLength(stringMap.get("length") == null ? 0.0f : Float.parseFloat(stringMap.get("length")));
                buildCacheParams.setWidth(stringMap.get("width") == null ? 0.0f : Float.parseFloat(stringMap.get("width")));
                buildCacheParams.setnMaxLodLevel(3);
                buildCacheParams.setnMaxTreeDepth(5);
                buildCacheParams.setnRootVolumeFactor(stringMap.get("volumeFactor") == null ? 0.1f : Float.parseFloat(stringMap.get("volumeFactor")));
                return buildCacheParams;
            } else {
                log.info("读取构建缓存配置文件异常---" + xmlFilePath);
                return getDefaultParams(modelType);
            }
        } else {
            log.info("未读取到构建缓存配置文件---" + xmlFilePath);
            return getDefaultParams(modelType);
        }
    }

    /**
     * 根据gim类型设置默认参数
     *
     * @param modelType STD:变电 TLD:输电
     * @return 属性实体
     */
    private UgxBuildCacheParams getDefaultParams(String modelType) {
        UgxBuildCacheParams buildCacheParams = new UgxBuildCacheParams();
        buildCacheParams.setFloatBoxSize(null);
        buildCacheParams.setFloatLod(null);
        if (modelType.equals("STD")) {
            buildCacheParams.setLength(0.0f);
            buildCacheParams.setWidth(0.0f);
        } else {
            buildCacheParams.setLength(800f);
            buildCacheParams.setWidth(800f);
        }
        buildCacheParams.setnMaxLodLevel(3);
        buildCacheParams.setnMaxTreeDepth(5);
        buildCacheParams.setnRootVolumeFactor(0.1f);
        return buildCacheParams;
    }

    /**
     * 构建土建图形缓存
     *
     * @param projectId        工程id
     * @param udfId            udf标识
     * @param ifcGraphPathList 图形集合
     * @param stringDoubleMap  经纬度等信息
     * @return bool
     */
    private ServiceResult<Boolean> buildIfcCache(String projectId, String udfId, List<String> ifcGraphPathList, Map<String, Double> stringDoubleMap) {

        if (ifcGraphPathList.size() > 0) {

            UgxBuildCacheParams cacheParams = getUgxBuildCacheParams("STD", "IFC");

            //工程id所在目录
            String projectDir = DirectoryUtils.generateDir(graphCacheService.getContext().getConfig().getWorkingDir(), DirectoryUtils.Root, projectId);
            log.info("ifcGraphPathList:" + ifcGraphPathList.toString());
            //数组存放ifc土建ugx路径，加结束标志。
            String[] ifcGraphPaths = new String[ifcGraphPathList.size() + 1];
            ifcGraphPathList.toArray(ifcGraphPaths);
            ifcGraphPaths[ifcGraphPathList.size()] = null;

            //构建缓存并存入数据库
            String ifcUuid = UUID.randomUUID().toString();
            String ifcCacheDir = DirectoryUtils.generateDir(projectDir, "cache", "ifc", ifcUuid);
            log.info("ifcGraphPaths" + ifcGraphPaths[0] + "," + ifcGraphPaths[ifcGraphPaths.length - 1] + "," + ifcCacheDir + "," + cacheParams.getFloatLod()[1]);
            int ifcUgxCacheResult = UgxGenerateCache.Instance.UgxGenerateEx(ifcGraphPaths, ifcCacheDir, "ifc",
                    cacheParams.getFloatLod(),
                    cacheParams.getFloatBoxSize(),
                    cacheParams.getLength(),
                    cacheParams.getWidth(),
                    cacheParams.getnMaxLodLevel(),
                    cacheParams.getnMaxTreeDepth(),
                    cacheParams.getnRootVolumeFactor(),
                    convertTimes);

            log.info("---ifcUgxCacheResult:" + ifcUgxCacheResult + "---");
            if (ifcUgxCacheResult == 1) {
                if (new File(ifcCacheDir + File.separator + "ifc.bcm").exists()) {
                    String graphCacheIndexPath = DirectoryUtils.generateDir(DirectoryUtils.Root, projectId, "cache", "ifc", ifcUuid, "ifc.bcm");
                    addGraphCacheIndexByMongodb(projectId, udfId, "ifc.bcm", graphCacheIndexPath, stringDoubleMap, "ifc");
                    return backResult(true, "ifc构建缓存成功");
                } else {
                    return backResult(false, "ifc.bcm不存在");
                }
            } else if (ifcUgxCacheResult == -1) {
                return backResult(false, "ifc构建缓存超时");
            } else {
                return backResult(false, "ifc构建缓存失败");
            }
        } else {
            return backResult(true, "无土建模型");
        }
    }

    /**
     * 构建线路图形缓存
     *
     * @param projectId         工程id
     * @param udfId             udf标识
     * @param allUgxFilePathMap 图形文件集合
     * @param stringDoubleMap   经纬度等信息集合
     * @return ServiceResult
     */
    private ServiceResult<Boolean> buildLineCache(String projectId, String udfId, Map<String, String> allUgxFilePathMap, Map<String, Double> stringDoubleMap) {
        boolean isSuccess = false;
        String message = "";

        //电气模型切片
        Collection<String> stringCollection = allUgxFilePathMap.values();
        String[] lineGraphPaths = new String[stringCollection.size() + 1];
        stringCollection.toArray(lineGraphPaths);
        lineGraphPaths[stringCollection.size()] = null;

        if (lineGraphPaths.length > 1) {
            //工程id所在目录
            String projectDir = DirectoryUtils.generateDir(graphCacheService.getContext().getConfig().getWorkingDir(), DirectoryUtils.Root, projectId);
            UgxBuildCacheParams cacheParams = getUgxBuildCacheParams("TLD", "TLDData");

            //构建缓存并存入数据库
            String lineUuid = UUID.randomUUID().toString();
            String lineCacheDir = DirectoryUtils.generateDir(projectDir, "cache", "line", lineUuid);

            int lineUgxCacheResult = UgxGenerateCache.Instance.UgxGenerateEx(lineGraphPaths,
                    lineCacheDir,
                    "total",
                    cacheParams.getFloatLod(),
                    cacheParams.getFloatBoxSize(),
                    cacheParams.getLength(),
                    cacheParams.getWidth(),
                    cacheParams.getnMaxLodLevel(),
                    cacheParams.getnMaxTreeDepth(),
                    cacheParams.getnRootVolumeFactor(),
                    convertTimes);

            log.info("---lineUgxCacheResult:" + lineUgxCacheResult + "---");

            if (lineUgxCacheResult == 1) {
                if (new File(lineCacheDir + File.separator + "total.bcm").exists()) {
                    String graphCacheIndexName = "total.bcm";
                    String graphCacheIndexPath = DirectoryUtils.generateDir(DirectoryUtils.Root, projectId, "cache", "line", lineUuid, "total.bcm");
                    addGraphCacheIndexByMongodb(projectId, udfId, graphCacheIndexName, graphCacheIndexPath, stringDoubleMap, "line");
                    message = "线路构建缓存成功";
                    isSuccess = true;
                } else {
                    message = "结果状态正确但total.bcm不存在";
                    isSuccess = false;
                }
            } else if (lineUgxCacheResult == 0) {
                message = "线路构建缓存失败";
                isSuccess = false;
            } else if (lineUgxCacheResult == -1) {
                message = "线路构建缓存超时";
                isSuccess = false;
            }
        } else {
            message = "线路ugx不存在";
            isSuccess = false;
        }
        stringDoubleMap.clear();
        allUgxFilePathMap.clear();
        return ServiceResult.customResult(isSuccess, null, message);
    }


    /**
     * 获取土建和电气图形文件路径
     *
     * @param graphFileDir         图形文件所在父目录
     * @param allUgxFilePathMap    ugx原有名称与更改名称后的全路径集合
     * @param ifcGraphPathList     土建图形路径集合
     * @param electricPathPathList 电气图形路径集合
     */
    private void initGraphPathListData(File graphFileDir, Map<String, String> allUgxFilePathMap, List<String> ifcGraphPathList, List<String> electricPathPathList) {
        //遍历文件目录，获取电气和土建的ugx文件全路径
        Arrays.stream(Objects.requireNonNull(graphFileDir.listFiles())).forEach(file -> {
            //图形文件完整路径
            String graphFilePath = file.getAbsolutePath();
            //图形文件后缀
            String graphFileSuffix = graphFilePath.substring(graphFilePath.lastIndexOf(".") + 1);
            if (graphFileSuffix.toUpperCase().equals(GraphType.GRAPH_INDEX_SUFFIX)) {
                //bcx图形文件前缀(不带.)
                String bcxFilePrefix = graphFilePath.substring(0, graphFilePath.lastIndexOf("."));
                String bcxFileNameGuid = bcxFilePrefix.substring(bcxFilePrefix.lastIndexOf(File.separator) + 1);
                String electricUgxName = bcxFileNameGuid + ".ugx";
                String electricUgxPath = allUgxFilePathMap.getOrDefault(electricUgxName, "");

                if (new File(electricUgxPath).exists()) {
                    electricPathPathList.add(electricUgxPath);
                }
            } else if (graphFileSuffix.toUpperCase().equals(GraphType.UGX)) {
                ifcGraphPathList.add(graphFilePath);
            }
        });
        //去除重复的ifc土建ugx图形文件路径
        ifcGraphPathList.removeAll(electricPathPathList);
        //去除安全检测ugx
        ifcGraphPathList.remove(allUgxFilePathMap.getOrDefault("safeDistance.ugx", ""));
    }

    /**
     * 根据udfId获取图形文件原有名称与实际路径集合
     *
     * @param graphService    服务层
     * @param udfId           udf标识
     * @param stringDoubleMap 参数集合
     * @param gimType         gim类型
     * @return 集合
     */
    private Map<String, String> getAllUgxPath(GraphService graphService, String udfId, Map<String, Double> stringDoubleMap, String gimType) {

        Map<String, String> ugxPathMap = new HashMap<>();

        ServiceResult<List<Graph>> serviceResult = graphService.getGraphByUdfId(udfId);
        List<Graph> graphList = serviceResult.getData() == null ? new ArrayList<>() : serviceResult.getData();
        graphList.forEach(graph -> {

            Coordinate coordinate = graph.getCoordinate();
            if (gimType.equals("0")) {
                if (coordinate.getxOffset() != 0.0 || coordinate.getLongitude() != 0.0) {
                    putMapCoordinator(stringDoubleMap, coordinate);
                }
            } else if (gimType.equals("1")) {
                if (coordinate.getLongitude() != 0.0) {
                    putMapCoordinator(stringDoubleMap, coordinate);
                }
            }
            ugxPathMap.put(graph.getGraphName(), DirectoryUtils.generateDir(graphCacheService.getContext().getConfig().getWorkingDir(), graph.getGraphPath()));
        });
        return ugxPathMap;
    }

    /**
     * 将经纬度高度放到map集合中
     */
    public void putMapCoordinator(Map<String, Double> stringDoubleMap, Coordinate coordinate) {
        stringDoubleMap.put("xOffset", coordinate.getxOffset());
        stringDoubleMap.put("yOffset", coordinate.getyOffset());
        stringDoubleMap.put("zOffset", coordinate.getzOffset());
        stringDoubleMap.put("angle", coordinate.getAngle());
        stringDoubleMap.put("lon", coordinate.getLongitude());
        stringDoubleMap.put("lat", coordinate.getLatitude());
        stringDoubleMap.put("height", coordinate.getHeight());

    }


    /**
     * 生成boolean返回结果
     */
    private ServiceResult<Boolean> backResult(boolean result, String message) {
        return ServiceResult.customResult(result, null, message);
    }
    
    /**
     * @param projectId
     * @desc 通过工程ID删除所有相关mongodb数据
     * @author 陈晓峰
     * @date 2019-10-15
     */
    public void delDataByProjectID(String projectId) {
        Query query = new Query(Criteria.where("projectId").is(projectId));
        mongoTemplate.remove(query, "property");
        mongoTemplate.remove(query, "entity");
        mongoTemplate.remove(query, "udf");
        mongoTemplate.remove(query, "eelDocument");
        mongoTemplate.remove(query, "graph");
        mongoTemplate.remove(query, "document");
        mongoTemplate.remove(query, "graphCacheIndex");
        mongoTemplate.remove(query, "view");
        mongoTemplate.remove(query, "ViewNode");
        mongoTemplate.remove(query, "type");
    }

}
