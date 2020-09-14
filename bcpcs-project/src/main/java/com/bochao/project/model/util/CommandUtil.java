package com.bochao.project.model.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

}
