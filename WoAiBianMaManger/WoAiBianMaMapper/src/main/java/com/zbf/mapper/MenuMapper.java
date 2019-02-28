package com.zbf.mapper;

import com.zbf.core.page.Page;
import com.zbf.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 作者：LCG
 * 创建时间：2018/11/23 15:52
 * 描述：
 */
@Mapper
public interface MenuMapper {

    public List<Menu> getListMenu(Menu menu);

    public List<Menu> getListMenuByRoleIds(Menu menu);

    public List<Menu> menuList(Page<Menu> page);

    void addOneMenuInfo(Menu menu);

    void updateMenu(Menu menu);

    void delMenu(Menu menu);
}
