package com.zbf.service;

import com.zbf.core.page.Page;
import com.zbf.entity.Menu;
import com.zbf.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 作者：LCG
 * 创建时间：2018/11/23 15:51
 * 描述：
 */
@Component
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 获取所有的菜单
     * @return
     */
    public List<Menu> getMenuTree(){

        Menu menu=new Menu ();
        menu.setLeval ( 1 );
        List<Menu> list1=menuMapper.getListMenu ( menu );
        this.getTree ( list1 );
        return list1;
    }

    /**
     * 递归方法
     * @return
     */
    public void getTree(List<Menu> list0){

        if(list0.size ()>0){
            for(Menu menu:list0){
                //查询下级菜单
                Menu m=new Menu ();
                m.setLeval ( menu.getLeval ()+1 );
                m.setParentMenuId ( menu.getId () );
                menu.setLabel ( menu.getMenuName () );
                List<Menu> list2= menuMapper.getListMenu ( m );
                if(list2.size ()==0){
                    continue;
                }else{
                    //存进当前的菜单下边
                    menu.setListMenu ( list2 );
                    //继续获取下级菜单
                    this.getTree (list2);
                }
            }
        }
    }
    /**
     * 根据角色ID获取所有的菜单
     * @return
     */
    public List<Menu> getMenuTreeByRole(String roleids){

        Menu menu=new Menu ();
        menu.setLeval ( 1 );
        menu.setRoleIds ( roleids );
        List<Menu> list1=menuMapper.getListMenuByRoleIds ( menu );
        this.getTreeByRole ( list1,roleids);
        return list1;
    }

    /**
     * 根据角色ID获取所有的菜单的递归方法
     * @return
     */
    public void getTreeByRole(List<Menu> list0,String roleIds){

        if(list0.size ()>0){
            for(Menu menu:list0){
                //查询下级菜单
                Menu m=new Menu ();
                m.setRoleIds ( roleIds );
                m.setLeval ( menu.getLeval ()+1 );
                m.setParentMenuId ( menu.getId () );
                menu.setLabel ( menu.getMenuName () );
                List<Menu> list2= menuMapper.getListMenuByRoleIds ( m );
                if(list2.size ()==0){
                    continue;
                }else{
                    //存进当前的菜单下边
                    menu.setListMenu ( list2 );
                    //继续获取下级菜单
                    this.getTreeByRole (list2,roleIds);
                }
            }
        }
    }


    /**
     * 分页查询
     * @param page
     */
    public void menuList(Page<Menu> page){

        List<Menu> list= menuMapper.menuList ( page );

        page.setResultList ( list );
    }

    public void addOneMenu(Menu menu){
        menuMapper.addOneMenuInfo ( menu );
    }

    public void updateMenu(Menu menu){
        menuMapper.updateMenu ( menu );
    }

    public void delMenu(Menu menu){
        menuMapper.delMenu ( menu );
    }
}
