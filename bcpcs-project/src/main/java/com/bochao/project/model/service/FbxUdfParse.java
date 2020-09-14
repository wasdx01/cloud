package com.bochao.project.model.service;


import com.bochao.bcedc.BcedcApplication;
import com.bochao.bcedc.analysis.IUDFFormatExtract;
import com.bochao.bcedc.model.*;
import com.bochao.bcedc.service.*;
import com.bochao.bcedc.utils.DirectoryUtils;
import com.bochao.bcedc.utils.UUIDUtils;
import com.bochao.bcedc.utils.UgdRelGuidXmlUtil;
import com.bochao.project.model.forkTask.ModelId;
import com.bochao.udf.IUDSpaceItem;
import com.bochao.udf.UDSpace;
import com.bochao.udf.UDSpaceItemType;
import com.bochao.udf.document.UDDocument;
import com.bochao.udf.entity.IUDEntityProperty;
import com.bochao.udf.entity.UDEntity;
import com.bochao.udf.entity.UDEntityComplexProperty;
import com.bochao.udf.entity.UDEntitySingleValueProperty;
import com.bochao.udf.graph.UDGraph;
import com.bochao.udf.schema.UDClass;
import com.bochao.udf.schema.UDRelation;
import com.bochao.udf.schema.UDSchema;
import com.bochao.udf.udfadapter.UDFFormatAdapter;
import com.bochao.udf.view.UDView;
import com.bochao.udf.view.UDViewNode;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Description FBX关联的UDF入库，需要重新修改entityID为属性中的句柄对应值/非标版GIM入库 按GIM规则修改entityName
 * @Description 由于原类方法都为私有 无法继承调用 复制BinaryFormatExtract类 并修改部分方法
 * @Author 陈晓峰
 * @Date 2019/12/5
 **/
public class FbxUdfParse implements IUDFFormatExtract {
    private String fileInfoId;//fbx解析需要用来区分entityId
    private BcedcApplication context;
    private UDF udf;
    private UDSpace udSpace;
    private Map<String, Entity> entityMap;
    private Map<String, String> documentMap;
    private GIMToUDFService gimToUDFService;
    private Map<String, String> propertyNameMap;
    private List<ModelId> modelIds;

    public FbxUdfParse(BcedcApplication context, String fileInfoId) {
        this.fileInfoId = fileInfoId;
        this.context = context;
        this.entityMap = new HashMap();
        this.documentMap = new HashMap();
        this.propertyNameMap = new HashMap();
        this.gimToUDFService = (GIMToUDFService)context.getService("GIMToUDFService");
    }

    public List<ModelId> getModelIds() {
        return modelIds;
    }

    public IUDFFormatExtract extract(UDF udf) {
        this.udf = udf;
        UDFFormatAdapter udfFormatAdapter = new UDFFormatAdapter();
        FileService fileService = (FileService)this.context.getService("FileService");
        String udfPath = (String)fileService.getFile(this.udf.getUdfPath()).getData();
        System.out.print(udfPath);
        this.udSpace = udfFormatAdapter.load(udfPath, this.generateExtractDir());
        return this;
    }

    public IUDFFormatExtract save() {
        this.saveEntity();
        this.saveView();
        this.saveGraph();
        this.saveDocument();
        this.saveSchema();
        return this;
    }

    private String generateExtractDir() {
        String extractDir = DirectoryUtils.generateDir(new String[]{this.context.getConfig().getWorkingDir(), "bcedc", this.udf.getProjectId(), "extract", this.udf.getUdfId()});
        return extractDir;
    }

    /**
     * @Description 获取每个Entity中属性为句柄的值
     * @Author 陈晓峰
     * @Date 2019/8/5
     */
    private void getPropertyId(IUDEntityProperty udEntityProperty, List<UDEntitySingleValueProperty> list) {
        UDEntityComplexProperty complexProperty = (UDEntityComplexProperty) udEntityProperty;
        Iterator var7 = complexProperty.getValues().iterator();
        while (var7.hasNext()) {
            IUDEntityProperty entityProperty = (IUDEntityProperty) var7.next();
            if (entityProperty.getClass() == UDEntityComplexProperty.class) {
                getPropertyId(entityProperty, list);
            } else if (entityProperty.getClass() == UDEntitySingleValueProperty.class) {
                UDEntitySingleValueProperty singleValueProperty = (UDEntitySingleValueProperty) entityProperty;
                list.add(singleValueProperty);
            }
        }
    }

    /**
     * @Description 为了与U3D模型ID对应且不重复 修改EntityID为（udf对应的fbx文件id+图元ID） ,
     * @Description Entity，RelDocument,Property等入库
     * @Description entity入库时按GIM规则修改名称
     * 先取“工程中名称”，再取“模型名称”，再取“建模类型”，如果建模类型为OTHERS或取不到，就取“设计类型”，
     * 都取不到就显示原始节点名称，如果原始节点名称为空就显示“其它”
     * @Author 陈晓峰
     * @Date 2019/8/5
     */
    private boolean saveEntity() {
        System.out.print("saveEntity");
        this.modelIds = new ArrayList();
        String gimType = this.udf.getUdfType();
        System.out.print(gimType);
        List<Entity> entities = new ArrayList();
        Collection<IUDSpaceItem> udSpaceItems = this.udSpace.getUDSpaceItems(UDSpaceItemType.UDEntity);
        System.out.print(udSpaceItems.size());
        List<RelDocument> relDocuments = new ArrayList();
        List<Relationship> relationships = new ArrayList();
        List<Property> properties = new ArrayList();
        List<FigureCoordinate> figureCoordinates = new ArrayList();
        FigureCoordinate figureCoordinate = new FigureCoordinate();
        Iterator var9 = udSpaceItems.iterator();

        while(true) {
            Entity entity;
            List<Property> entityProperties;
            do {
                UDEntity udEntity;
                do {
                    if (!var9.hasNext()) {
                        if (!CollectionUtils.isEmpty(figureCoordinates)) {
                            String xmlFilePath = DirectoryUtils.generateDir(new String[]{this.context.getConfig().getWorkingDir(), "bcedc", this.udf.getProjectId(), "FigureCoordinates.xml"});

                            try {
                                UgdRelGuidXmlUtil.createFigureCoordinates(xmlFilePath, figureCoordinates);
                            } catch (Exception var24) {
                                var24.printStackTrace();
                            }
                        }

                        relationships.stream().forEach((relationship) -> {
                            Entity existed = (Entity)this.entityMap.getOrDefault(relationship.getRelated(), null);
                            if (existed != null) {
                                relationship.setRelated(existed.getEntityId());
                            }

                        });
                        EntityService entityService = (EntityService)this.context.getService("EntityService");
                        ServiceResult<Boolean> executeResult = entityService.addEntities(entities);
                        if (executeResult.isSuccess()) {
                            PropertyService propertyService = (PropertyService)this.context.getService("PropertyService");
                            executeResult = propertyService.setProperty(properties);
                            if (executeResult.isSuccess()) {
                                executeResult = entityService.relDocument(relDocuments);
                                if (executeResult.isSuccess()) {
                                    RelationshipService relationshipService = (RelationshipService)this.context.getService("RelationshipService");
                                    executeResult = relationshipService.addRelationship(relationships);
                                }
                            }
                        }

                        return executeResult.isSuccess();
                    }

                    IUDSpaceItem spaceItem = (IUDSpaceItem)var9.next();
                    udEntity = (UDEntity)spaceItem;
                } while(udEntity == null);
                Iterator var4 = udEntity.getProperties().values().iterator();
                String id = null;
                String name = null;
                List<UDEntitySingleValueProperty> list = new ArrayList<>();
                while (var4.hasNext()) {
                    IUDEntityProperty udEntityProperty = (IUDEntityProperty) var4.next();
                    getPropertyId(udEntityProperty, list);
                }
                Map<String, String> map = list.stream().collect(Collectors.toMap(UDEntitySingleValueProperty::getPropertyName, UDEntitySingleValueProperty::getValue,
                        (value1, value2) -> {
                            return value2;
                        }));
                //fbx中模型Id区分
                id = map.get("句柄");
                //概预算和模型对应的Id
                String nodeId = map.get("NODEID");
                name = map.get("工程中名称");
                if (name == null || name.equals("-"))
                    name = map.get("模型名称");
                if (name == null || name.equals("-"))
                    name = map.get("名称");
                if (name == null || name.equals("-"))
                    name = map.get("建模类型");
                if (name == null || name.equals("OTHERS"))
                    name = map.get("设计类型");
                entity = new Entity();
                entity.setEntityId(id == null || fileInfoId == null ? UUIDUtils.generate() : fileInfoId + id);
                if (nodeId != null) {
                    ModelId modelId = new ModelId();
                    modelId.setEntityId(entity.getEntityId());
                    modelId.setGlobalId(udEntity.getId());
                    modelId.setNodeId(nodeId);
                    modelId.setProjectId(this.udf.getProjectId());
                    modelId.setUdfId(this.udf.getUdfId());
                    modelIds.add(modelId);
                }
                if (name != null) {
                    name = name.replaceAll("&", "");
                } else {
                    name = udEntity.getName();
                }
                String globalId = udEntity.getId().split("%")[0];
                entity.setGlobalId(globalId);
                entity.setName(name);
                entity.setUdfId(this.udf.getUdfId());
                entity.setClassType(udEntity.getClassInfo().getClassName());
                entity.setProjectId(this.udf.getProjectId());
                entities.add(entity);
                this.entityMap.put(udEntity.getId(), entity);
                List<RelDocument> entityRelDocuments = this.getRelDocument(entity, udEntity.getRelDocuments());
                if (entityRelDocuments != null) {
                    relDocuments.addAll(entityRelDocuments);
                }

                List<Relationship> entityRelationship = this.getRelationship(entity, udEntity.getRelationships());
                if (entityRelationship != null) {
                    relationships.addAll(entityRelationship);
                }

                entityProperties = this.getProperty(entity, udEntity.getProperties().values());
            } while(entityProperties == null);

            if ("0".equals(gimType)) {
                entityProperties.forEach(ep -> {
                    if (ep.getName().equals("建模类型") && (ep.getValue().equals("OTHERS") || ep.getValue().equals("&其他"))) {
                        ep.setValue((String)this.propertyNameMap.getOrDefault(ep.getEntityId(), "&其他"));
                    }

                });
            } else if ("1".equals(gimType)) {
                String code = "";
                String coordinate = "";
                String houseArea = "";
                Iterator iterator = entityProperties.iterator();

                while(iterator.hasNext()) {
                    Property property = (Property)iterator.next();
                    String var22 = property.getName();
                    byte var23 = -1;
                    switch(var22.hashCode()) {
                        case 2074093:
                            if (var22.equals("CODE")) {
                                var23 = 1;
                            }
                            break;
                        case 693801392:
                            if (var22.equals("地物坐标")) {
                                var23 = 0;
                            }
                            break;
                        case 773211577:
                            if (var22.equals("房屋面积")) {
                                var23 = 2;
                            }
                    }

                    switch(var23) {
                        case 0:
                            coordinate = property.getValue();
                            iterator.remove();
                            break;
                        case 1:
                            code = property.getValue();
                            break;
                        case 2:
                            houseArea = property.getValue();
                    }
                }

                try {
                    if (!StringUtils.isEmpty(coordinate)) {
                        FigureCoordinate figureCoordinateClone = figureCoordinate.clone();
                        figureCoordinateClone.setEntityId(entity.getEntityId());
                        figureCoordinateClone.setGlobalId(entity.getGlobalId());
                        figureCoordinateClone.setCoordinate(coordinate);
                        figureCoordinateClone.setCode(code);
                        figureCoordinateClone.setHouseArea(houseArea);
                        figureCoordinates.add(figureCoordinateClone);
                    }
                } catch (CloneNotSupportedException var25) {
                    var25.printStackTrace();
                }
            }

            this.propertyNameMap.clear();
            properties.addAll(entityProperties);
        }
    }
    private boolean saveSchema() {
        Collection<IUDSpaceItem> udSpaceItems = this.udSpace.getUDSpaceItems(UDSpaceItemType.UDSchema);
        Iterator var2 = udSpaceItems.iterator();

        while(true) {
            SchemaService schemaService;
            Collection udRelations;
            do {
                do {
                    UDSchema udSchema;
                    do {
                        if (!var2.hasNext()) {
                            return true;
                        }

                        IUDSpaceItem udSpaceItem = (IUDSpaceItem)var2.next();
                        schemaService = (SchemaService)this.context.getService("SchemaService");
                        udSchema = (UDSchema)udSpaceItem;
                    } while(udSchema == null);

                    Collection<UDClass> udClasses = udSchema.getClassies();
                    if (udClasses != null && udClasses.size() > 0) {
                        List<ClassInfo> classInfos = new ArrayList();
                        Iterator var8 = udClasses.iterator();

                        while(var8.hasNext()) {
                            UDClass udClass = (UDClass)var8.next();
                            ClassInfo classInfo = new ClassInfo();
                            classInfo.setAlias(udClass.getAliasName());
                            classInfo.setClassName(udClass.getClassName());
                            classInfo.setClassId(UUIDUtils.generate());
                            classInfo.setSuperType(udClass.getSuperType().getClassName());
                            classInfos.add(classInfo);
                        }

                        schemaService.addClass(this.udf.getProjectId(), classInfos);
                    }

                    udRelations = udSchema.getRelations();
                } while(udRelations == null);
            } while(udRelations.size() <= 0);

            List<Relation> relations = new ArrayList();
            Iterator var15 = udRelations.iterator();

            while(var15.hasNext()) {
                UDRelation udRelation = (UDRelation)var15.next();
                Relation relation = new Relation();
                relation.setRelationId(UUIDUtils.generate());
                relation.setAlias(udRelation.getAliasName());
                relation.setRelationName(udRelation.getRelationName());
                relation.setSuperType(udRelation.getSuperType().getRelationName());
                relations.add(relation);
            }

            schemaService.addRelation(this.udf.getProjectId(), relations);
        }
    }

    private boolean saveDocument() {
        List<Document> documents = new ArrayList();
        Collection<IUDSpaceItem> udSpaceItems = this.udSpace.getUDSpaceItems(UDSpaceItemType.UDDocument);
        String relativeDir = DirectoryUtils.generateDir(new String[]{"bcedc", this.udf.getProjectId(), "extract", this.udf.getUdfId(), "Document"});
        Iterator var4 = udSpaceItems.iterator();

        while(var4.hasNext()) {
            IUDSpaceItem spaceItem = (IUDSpaceItem)var4.next();
            UDDocument udDocument = (UDDocument)spaceItem;
            if (udDocument != null) {
                Document document = new Document();
                String refresh;
                if (this.documentMap.containsKey(udDocument.getId())) {
                    refresh = (String)this.documentMap.get(udDocument.getId());
                } else {
                    refresh = UUIDUtils.generate();
                }

                document.setDocumentId(refresh);
                document.setDocumentName(udDocument.getDocumentName());
                document.setFilePath(DirectoryUtils.generateDir(new String[]{relativeDir, udDocument.getDocumentName()}));
                document.setFileSize((new File(udDocument.getFilePath())).length());
                document.setUdfId(this.udf.getUdfId());
                document.setProjectId(this.udf.getProjectId());
                document.setCreateTime(new Date());
                documents.add(document);
            }
        }

        DocumentService documentService = (DocumentService)this.context.getService("DocumentService");
        ServiceResult<Boolean> executeResult = documentService.addDocument(documents);
        return executeResult.isSuccess();
    }

    private boolean saveGraph() {
        List<Graph> graphs = new ArrayList();
        Collection<IUDSpaceItem> udSpaceItems = this.udSpace.getUDSpaceItems(UDSpaceItemType.UDGraph);
        String relativeDir = DirectoryUtils.generateDir(new String[]{"bcedc", this.udf.getProjectId(), "extract", this.udf.getUdfId(), "Graph"});
        Iterator var4 = udSpaceItems.iterator();

        while(true) {
            UDGraph udGraph;
            String graphSuffix;
            do {
                do {
                    if (!var4.hasNext()) {
                        GraphService graphService = (GraphService)this.context.getService("GraphService");
                        ServiceResult<Boolean> executeResult = graphService.addGraph(graphs);
                        return executeResult.isSuccess();
                    }

                    IUDSpaceItem spaceItem = (IUDSpaceItem)var4.next();
                    udGraph = (UDGraph)spaceItem;
                } while(udGraph == null);

                graphSuffix = udGraph.getFilePath().substring(udGraph.getFilePath().lastIndexOf(".") + 1);
            } while(!graphSuffix.toUpperCase().equals("UGD") && !graphSuffix.toUpperCase().equals("UGX"));

            File targetUgd;
            if (graphSuffix.toUpperCase().equals("UGX")) {
                targetUgd = new File(udGraph.getFilePath());
            } else {
                targetUgd = this.renameUdfFile(udGraph.getFilePath());
            }

            Graph graph = new Graph();
            graph.setGraphId(UUIDUtils.generate());
            graph.setGraphName(udGraph.getGraphName());
            graph.setGraphPath(DirectoryUtils.generateDir(new String[]{relativeDir, targetUgd.getName()}));
            graph.setUdfId(this.udf.getUdfId());
            graph.setParameter(udGraph.getParameter());
            graph.setGraphSize(targetUgd.length());
            graph.setProjectId(this.udf.getProjectId());
            Coordinate coordinate = new Coordinate();
            if (udGraph.getCoordinate() != null) {
                coordinate.setAngle(udGraph.getCoordinate().getAngle());
                coordinate.setLatitude(udGraph.getCoordinate().getLatitude());
                coordinate.setLongitude(udGraph.getCoordinate().getLongitude());
                coordinate.setHeight(udGraph.getCoordinate().getHeight());
                coordinate.setxOffset(udGraph.getCoordinate().getxOffset());
                coordinate.setyOffset(udGraph.getCoordinate().getyOffset());
                coordinate.setzOffset(udGraph.getCoordinate().getzOffset());
            } else {
                coordinate.setAngle(0.0D);
                coordinate.setLatitude(0.0D);
                coordinate.setLongitude(0.0D);
                coordinate.setHeight(0.0D);
                coordinate.setxOffset(0.0D);
                coordinate.setyOffset(0.0D);
                coordinate.setzOffset(0.0D);
            }

            graph.setCoordinate(coordinate);
            graphs.add(graph);
        }
    }

    private boolean saveView() {
        List<View> views = new ArrayList();
        Collection<IUDSpaceItem> udSpaceItems = this.udSpace.getUDSpaceItems(UDSpaceItemType.UDView);
        Iterator var3 = udSpaceItems.iterator();

        while(true) {
            UDView udView;
            do {
                do {
                    if (!var3.hasNext()) {
                        ViewService viewService = (ViewService)this.context.getService("ViewService");
                        ServiceResult<Boolean> executeResult = viewService.addView(views);
                        return executeResult.isSuccess();
                    }

                    IUDSpaceItem spaceItem = (IUDSpaceItem)var3.next();
                    udView = (UDView)spaceItem;
                } while(udView == null);
            } while(this.context.getConfig().getExtractViewSize() == 1 && udSpaceItems.size() > 1 && udView.getViewName().indexOf("基本视图") == -1);

            View view = new View();
            view.setViewId(UUIDUtils.generate());
            view.setAlias(udView.getAlias());
            view.setViewName(udView.getViewName());
            view.setUdfId(this.udf.getUdfId());
            view.setProjectId(this.udf.getProjectId());
            List<ViewNode> viewNodes = new ArrayList();
            Collection<UDViewNode> udViewNodes = udView.getViewNodes();
            if (udViewNodes != null) {
                ViewNode viewNode;
                for(Iterator var9 = udViewNodes.iterator(); var9.hasNext(); viewNodes.add(viewNode)) {
                    UDViewNode udViewNode = (UDViewNode)var9.next();
                    viewNode = new ViewNode();
                    viewNode.setProjectId(this.udf.getProjectId());
                    viewNode.setViewId(view.getViewId());
                    viewNode.setNodeName(udViewNode.getName());
                    Entity existed = (Entity)this.entityMap.getOrDefault(udViewNode.getGuid(), null);
                    Entity existedParent = (Entity)this.entityMap.getOrDefault(udViewNode.getParentId(), null);
                    if (existed != null) {
                        viewNode.setEntity(true);
                        viewNode.setViewNodeId(existed.getEntityId());
                    } else {
                        viewNode.setViewNodeId(udViewNode.getGuid());
                    }

                    if (existedParent != null) {
                        viewNode.setParentId(existedParent.getEntityId());
                    } else {
                        viewNode.setParentId(udViewNode.getParentId());
                    }
                }

                view.setViewNodes(viewNodes);
            }

            views.add(view);
        }
    }

    private List<RelDocument> getRelDocument(Entity entity, List<String> documentIds) {
        if (documentIds != null && documentIds.size() != 0) {
            List<RelDocument> relDocuments = new ArrayList();
            Iterator var4 = documentIds.iterator();

            while(var4.hasNext()) {
                String documentId = (String)var4.next();
                String refresh = UUIDUtils.generate();
                if (!this.documentMap.containsKey(documentId)) {
                    this.documentMap.put(documentId, refresh);
                }

                RelDocument relDocument = new RelDocument();
                relDocument.setDocumentId(refresh);
                relDocument.setEntityId(entity.getEntityId());
                relDocuments.add(relDocument);
            }

            return relDocuments;
        } else {
            return null;
        }
    }

    private List<Relationship> getRelationship(Entity entity, Map<String, List<String>> relationshipMap) {
        if (relationshipMap != null && relationshipMap.size() != 0) {
            List<Relationship> relationships = new ArrayList();
            Iterator var4 = relationshipMap.keySet().iterator();

            while(var4.hasNext()) {
                String relationType = (String)var4.next();
                Iterator var6 = ((List)relationshipMap.get(relationType)).iterator();

                while(var6.hasNext()) {
                    String entityId = (String)var6.next();
                    Relationship relationship = new Relationship();
                    relationship.setRelationType(relationType);
                    relationship.setRelating(entity.getEntityId());
                    relationship.setRelated(entityId);
                    relationship.setUdfId(entity.getUdfId());
                    relationships.add(relationship);
                }
            }

            return relationships;
        } else {
            return null;
        }
    }

    private List<Property> getProperty(Entity entity, Collection<IUDEntityProperty> udEntityProperties) {
        if (udEntityProperties != null && udEntityProperties.size() != 0) {
            List<Property> properties = new ArrayList();
            Iterator var4 = udEntityProperties.iterator();

            while(var4.hasNext()) {
                IUDEntityProperty udEntityProperty = (IUDEntityProperty)var4.next();
                List<Property> property = this.generateProperties(entity, (String)null, udEntityProperty);
                if (property != null && property.size() > 0) {
                    properties.addAll(property);
                }
            }

            return properties;
        } else {
            return null;
        }
    }

    private List<Property> generateProperties(Entity entity, String parentId, IUDEntityProperty udEntityProperty) {
        if (udEntityProperty == null) {
            return null;
        } else {
            List<Property> properties = new ArrayList();
            Property property = new Property();
            property.setSourceType("0");
            property.setParentId(parentId);
            property.setPropertyId(UUIDUtils.generate());
            property.setEntityId(entity.getEntityId());
            property.setProjectId(entity.getProjectId());
            property.setName(udEntityProperty.getPropertyName());
            property.setUdfId(this.udf.getUdfId());
            if (udEntityProperty.getClass() == UDEntitySingleValueProperty.class) {
                UDEntitySingleValueProperty singleValueProperty = (UDEntitySingleValueProperty)udEntityProperty;
                property.setPropertyType("Single");
                property.setValue(singleValueProperty.getValue());
                if (property.getName().equals("设计类型")) {
                    this.propertyNameMap.put(property.getEntityId(), property.getValue());
                }
            } else if (udEntityProperty.getClass() == UDEntityComplexProperty.class) {
                property.setPropertyType("Complex");
                UDEntityComplexProperty complexProperty = (UDEntityComplexProperty)udEntityProperty;
                Iterator var7 = complexProperty.getValues().iterator();

                while(var7.hasNext()) {
                    IUDEntityProperty entityProperty = (IUDEntityProperty)var7.next();
                    List<Property> entityProperties = this.generateProperties(entity, property.getPropertyId(), entityProperty);
                    if (entityProperties != null && entityProperties.size() > 0) {
                        properties.addAll(entityProperties);
                    }
                }
            }

            properties.add(property);
            return properties;
        }
    }

    private File renameUdfFile(String ugdFile) {
        int index = ugdFile.lastIndexOf(".");
        String fileExtension = null;
        if (index != -1 && index != ugdFile.length() - 1) {
            fileExtension = "." + ugdFile.substring(index + 1);
        } else {
            fileExtension = "";
        }

        File file = new File(ugdFile);
        String fileName = UUIDUtils.generate();
        String targetFileName = file.getParent() + File.separator + fileName + fileExtension;
        file.renameTo(new File(targetFileName));
        return new File(targetFileName);
    }
}