package com.zbf.core.utils;

import com.zbf.core.XssShieldUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

public class CommonUtils {


    public static Random tandom=new Random();
	/**
	 * 获取request中的参数
	 * @param request
	 * @return
	 */
	public static Map<String,Object> getParameterMap(HttpServletRequest request) {  
        // 参数Map  
        Map<?,?> properties = request.getParameterMap();  
        // 返回值Map  
        Map<String,Object> returnMap = new HashMap<String,Object>();  
        Iterator<?> entries = properties.entrySet().iterator();  
        Entry<?, ?> entry;  
        String name = "";  
        String value = "";  
        while (entries.hasNext()) {  
            entry = (Entry<?, ?>) entries.next();  
            //name = (String) entry.getKey();  
            name =XssShieldUtil.stripXss((String) entry.getKey());
            Object valueObj = entry.getValue();  
            if(null == valueObj){  
                value = "";  
            }else if(valueObj instanceof String[]){  
                String[] values = (String[])valueObj;  
                for(int i=0;i<values.length;i++){  
                    value = values[i] + ",";  
                }  
                value = value.substring(0, value.length()-1);  
                value = XssShieldUtil.stripXss(value);
            }else{  
                value = valueObj.toString();
                value = XssShieldUtil.stripXss(value);
            }  
            returnMap.put(name, value);  
        }  
        return returnMap;  
    }


    public static synchronized Long getId(){
       return System.currentTimeMillis()+tandom.nextInt(1000);
    }



}
