package com.zbf.core;

import com.alibaba.fastjson.JSON;

import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

public class CommonUtils {


    public static Random tandom = new Random ();

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


    public static Map<String,Object> getParamsJsonMap(HttpServletRequest request){

        Map<String, String[]> parameterMap = request.getParameterMap ();

        Object[] objects = parameterMap.keySet ().toArray ();
        Map<String,Object> map1=new HashMap<String,Object>();
        if(objects.length>0){
            map1 = JSON.parseObject ( objects[ 0 ].toString (), Map.class );
            Set<String> strings = map1.keySet ();
            List<Object> objects1 = Arrays.asList (strings.toArray ());
            for(Object str:objects1){
                if(map1.get ( str )==null||"".equals ( map1.get ( str ) )||"null".equals ( map1.get ( str ) )){
                    map1.remove ( str );
                }
            }
            strings=null;
        }
        parameterMap=null;
        objects=null;
        return map1;
    }


    public static synchronized Long getId(){

        return System.currentTimeMillis()+tandom.nextInt(1000);

    }


    public static String zhuanyiString(String string){
        String replace = string.replace ( "\"", "\\\"" );
        return replace;
    }

}
