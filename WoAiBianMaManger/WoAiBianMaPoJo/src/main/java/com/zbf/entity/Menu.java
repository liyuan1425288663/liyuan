package com.zbf.entity;

import com.zbf.common.IdEntity;
import lombok.Data;

import java.util.List;

/**
 * 作者：LCG
 * 创建时间：2018/11/23 15:47
 * 描述：
 */
@Data
public class Menu extends IdEntity {

    //所有的角色IDS
    private String roleIds;

    //菜单等级
    private Integer leval;
    //菜单的等级以及第几个菜单 例如一级菜单下的第二个菜单1-2
    private String levalIndex;
    //菜单名称
    private String menuName;
    //菜单的链接地址
    private String url;
    //父级菜单的ID
    private long parentMenuId;
    //是否删除 1 删除 0没有删除
    private Integer isDelete;
    //用来存储当前菜单的子菜单
    private List<Menu> listMenu;
    //页面的路由路径
    private String routerPath;

    //需要回显选中的复选框
    private Long[] ids;

    private String label;


}
