package com.zbf.utils;

import com.zbf.entity.Menu;

import java.util.List;

/**
 * 作者：LCG
 * 创建时间：2019/2/4 17:20
 * 描述：
 */
public class MenuUtils {

    /**
     * 该方法返回一个需要回显的菜单ID列表
     * @return
     */
    public static List<Long> getCheckedNode(List<Menu> list, List<Long> ids){

        for(Menu menu:list){
            if(menu.getLeval ()!=1&&menu.getListMenu ()==null){
                ids.add ( menu.getId () );
            }else{
                getCheckedNode ( menu.getListMenu (),ids );
            }
        }

        return ids;
    }

}
