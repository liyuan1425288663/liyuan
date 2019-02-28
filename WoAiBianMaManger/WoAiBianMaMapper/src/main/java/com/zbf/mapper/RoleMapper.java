package com.zbf.mapper;

import com.zbf.core.page.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoleMapper {
    /**
     * 获取角色的列表
     * @param page
     * @return
     */
    public List<Map<String,Object>> getRolePage(Page<Map<String,Object>> page);

    /**
     * 删除角色信息
     * @param map
     * @return
     */
    public int deleteByRoleId(Map<String,Object> map);

    /**
     * 删除角色和菜单的绑定信息
     * @param map
     * @return
     */
    public int deleteRoleMenu(Map<String,Object> map);

    /**
     * 添加角色
     * @param map
     * @return
     */
    public int addRole(Map<String,Object> map);

    //批量的插入角色菜单绑定数据
    public int addRoleMenu(List<Map<String,Object>> list);

    public int updateRole(Map<String,Object> map);

    public List<Map<String,Object>> getRoleListByQuery(Map<String,Object> map);
}
