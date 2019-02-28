package com.zbf.enmu;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：LCG
 * 创建时间：2019/2/17 20:24
 * 描述：用来转换 将 编码转换为名称
 */
public class CodeToName {

    private static Map<String,String> map=new HashMap<> ();
    private static Map<String,String> nandumap=new HashMap<> ();
    private static Map<String,String> zhuangtaimap=new HashMap<> ();

    static{
        map.put ( "1","单选题" );
        map.put ( "2","多选题" );
        map.put ( "3","判断题" );
        map.put ( "4","填空题" );
        map.put ( "5","问答题" );

        nandumap.put ( "1","简单" );
        nandumap.put ( "2","一般" );
        nandumap.put ( "3","稍难" );
        nandumap.put ( "4","困难" );
        nandumap.put ( "5","汗颜难" );

        zhuangtaimap.put ( "1","开放" );
        zhuangtaimap.put ( "0","关闭" );
    }

    /**
     * 获取试题类型
     * @param id
     * @return
     */
    public static String getShiTiLeixingName(String id){
        return map.get ( id );
    }
    /**
     * 获取试题难度
     * @param id
     * @return
     */
    public static String getShiTiNanDuName(String id){
        return nandumap.get ( id );
    }
    /**
     * 获取试题状态
     * @param id
     * @return
     */
    public static String getShiTiZhuangTaiName(String id){
        return zhuangtaimap.get ( id );
    }
}
