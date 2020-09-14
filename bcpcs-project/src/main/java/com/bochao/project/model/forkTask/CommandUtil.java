package com.bochao.project.model.forkTask;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * linux 命令工具类
 */
public class CommandUtil {
    public static Process command(String command) {
        Process process = null;
        try {
            System.out.println(command);
            process = new ProcessBuilder(command.split(" ")).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return process;
    }

    public static Process command(String... command){
        Process process = null;
        try {
            String a = "";
            for (String s : command) {
                a +=s+" ";
            }
                //System.out.println(a);
            process = new ProcessBuilder(command).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return process;
    }

    public static String getErrMsg(Process process) {
        StringBuilder sb = new StringBuilder();
        String s;
        BufferedReader errors = new BufferedReader(new InputStreamReader(
                process.getErrorStream()));
        try {
            if(errors != null){
                while ((s = errors.readLine()) != null) {
                    sb.append(s);
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                if(errors != null){
                    errors.close();
                }
            } catch (IOException e) {
            }
        }
        return sb.toString().equals("") ? null : sb.toString();
    }

    public static String getMsg(Process process) {
        StringBuilder sb = new StringBuilder();
        String s;
        BufferedReader results = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        try {
            if(results != null){
                while ((s = results.readLine()) != null) {
                    sb.append(s);
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                if(results != null){
                    results.close();
                }
            } catch (IOException e) {
            }
        }
        return sb.toString().equals("") ? null : sb.toString();
    }
    public static List getMsg1(Process process) {
        List<String> list = new ArrayList<String>();
        String s;
        BufferedReader results = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        try {
            if(results != null){
                while ((s = results.readLine()) != null) {
                    list.add(s);
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                if(results != null){
                    results.close();
                }
            } catch (IOException e) {
            }
        }
        return list.size()==0 ? null : list;
    }

    /**
     *
     * @param xmlFilePath xml路径
     * @param graphType 模型类型（STD:变电 TLD:输电）
     * @param graphCategory IFC:土建 substation:变电站模型 TLDData:线路
     * @return
     */
    public static Map<String, String> parseUgtSetXml(String xmlFilePath, String graphType, String graphCategory){
        Map<String, String> stringMap = new HashMap<>();
        try {
            SAXReader reader = new SAXReader();
            //通过reader对象的read方法加载xml文件，获取document对象。
            Document document  = reader.read(new File(xmlFilePath));
            //通过document对象获取根节点rootElement
            Element rootElement = document.getRootElement();
            Iterator<Element> iteratorRoot = rootElement.elementIterator();
            while (iteratorRoot.hasNext()) {
                Element elementRoot = iteratorRoot.next();
                Iterator<Element> iteratorEle = elementRoot.elementIterator();
                while (iteratorEle.hasNext()) {
                    Element element = iteratorEle.next();
                    if (elementRoot.getName().equals(graphType) && element.getName().equals(graphCategory)) {
                        Iterator<Element> iterator = element.elementIterator();
                        while (iterator.hasNext()){
                            Element elementEle = iterator.next();
                            String lodRangeList = null ,volumeFactor = null,length = null,width = null,boxSize = null;
                            if("LodRangeList".equals(elementEle.getName())){
                                lodRangeList = elementEle.getStringValue();
                            }
                            if("VolumeFactor".equals(elementEle.getName())){
                                volumeFactor = elementEle.getStringValue();
                            }
                            if("Length".equals(elementEle.getName())){
                                length = elementEle.getStringValue();
                            }
                            if("Width".equals(elementEle.getName())){
                                width = elementEle.getStringValue();
                            }
                            if("BoxSize".equals(elementEle.getName())){
                                boxSize = elementEle.getStringValue();
                            }
                            stringMap.put("lodRangeList",lodRangeList == null ? "0,300,800,1000,1800,2400,3000" : lodRangeList);
                            stringMap.put("volumeFactor",volumeFactor == null ? "0.1" : volumeFactor);
                            stringMap.put("length",length == null ? "0" : length);
                            stringMap.put("width",width == null ? "0" : width);
                            stringMap.put("boxSize",boxSize == null ? "0,0,0" : boxSize);
                            return stringMap;
                        }
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * lod字符串切割成float数组
     *
     * @param lodRangeList 以，分隔的字符串
     * @return float数组
     */
    public static float[] getLodRangeList(String lodRangeList) {
        if (lodRangeList == null) {
            return null;
        }

        String[] lodRanges = lodRangeList.split(",");
        float[] floats = new float[lodRanges.length + 1];

        int lotRangesSize = lodRanges.length;
        for (int i = 0; i < lotRangesSize; i++) {
            floats[i] = Float.parseFloat(lodRanges[i]);
        }
        floats[lotRangesSize] = -1f;
        return floats;
    }
}
