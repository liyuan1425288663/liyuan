package com.zbf.mapper;

import com.zbf.core.page.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface ShiJuanMapper {
    List<Map<String, Object>> getfenlei(Map<String, Object> paramsJsonMap);

    List<Map<String, Object>> getbumenList(Page<Map<String,Object>> page);

    List<Map<String, Object>> getuserList(Page<Map<String, Object>> page);

    void Addshijuan(Map<String, Object> paramsJsonMap);



    void AddshijuanBumen(Map<String, Object> paramsJsonMap);

    void AddshijuanUser(HashMap<String, Object> usermap);

    List<Map<String, Object>> ShiJuanList(Page<Map<String, Object>> page);
}
