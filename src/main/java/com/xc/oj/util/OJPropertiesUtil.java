package com.xc.oj.util;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.*;
import java.net.URLDecoder;
import java.util.Properties;

public class OJPropertiesUtil {
    private static String filePath=PropertiesUtil.class.getClassLoader().getResource("oj.properties").getFile();
    private static Properties prop;
    public static Properties getProperties(){
        if(prop==null||prop.isEmpty()) {
            try {
                prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("oj.properties"));
            } catch (IOException e) {
                prop=new Properties();
                e.printStackTrace();
            }
        }
        return prop;
    }
    public static String get(String key){
        return getProperties().getProperty(key);
    }
    public static void store(String key,String value){
        try {
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(URLDecoder.decode(filePath,"utf-8"))));
            getProperties().put(key,value);
            getProperties().store(bw, "");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        prop=null;
    }
}
