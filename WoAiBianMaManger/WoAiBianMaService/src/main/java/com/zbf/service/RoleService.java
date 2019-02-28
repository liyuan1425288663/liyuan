package com.zbf.service;

import com.zbf.core.page.Page;
import com.zbf.core.utils.UID;
import com.zbf.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：LCG
 * 创建时间：2019/1/31 22:36
 * 描述：
 */
@Component
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 查询角色列表
     * @param map
     */
    public void getRolePage(Page<Map<String,Object>> map){
        List<Map<String, Object>> rolePage = roleMapper.getRolePage ( map );
        map.setResultList ( rolePage );
    }

    //删除一个角色
    @Transactional
    public void deleteByRoleId(Map<String,Object> map){

        //删除角色表的数据
        roleMapper.deleteByRoleId ( map );
        //删除角色菜单表的中间数据
        roleMapper.deleteRoleMenu ( map );

    }


    public void addRole(Map<String,Object> map){
        roleMapper.addRole ( map );
    }


    /**
     * 更新角色菜单中间表
     * @param map
     */
    @Transactional
    public void updateRole(Map<String,Object> map){

        //更新角色表的数据
        roleMapper.updateRole ( map );

        //删除角色菜单绑定关系表中的数据
        roleMapper.deleteRoleMenu ( map );
        //插入新的角色绑定关系
        List<String> listMenuIds= (List<String>) map.get ( "menuIds" );
        List<Map<String,Object>> roleMenu=new ArrayList<> (  );

        for(String menuId:listMenuIds){
            Map<String,Object> map1=new HashMap<> (  );
            map1.put ( "id",UID.getUUIDOrder () );
            map1.put ( "roleId",map.get ( "id" ) );
            map1.put ( "menuId",menuId);
            roleMenu.add ( map1 );
        }
        //开始插入角色菜单绑定数据
        roleMapper.addRoleMenu ( roleMenu );

    }


    public List<Map<String,Object>> getRoleListByQuery(Map<String,Object> map){
        return roleMapper.getRoleListByQuery(map);
    }

}
