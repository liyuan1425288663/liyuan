package com.zbf.mapper;

import com.zbf.core.page.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：LCG
 * 创建时间：2019/2/14 11:01
 * 描述：
 */
@Mapper
public interface TiKuMapper {

    public int addTiKuInfo(Map<String,Object> map);

    public List<Map<String,Object>> getTikuList(Page<Map<String,Object>> page);

    public int updateTiKuInfo(Map<String,Object> map);

    void delTiKu(Map<String, Object> parameterMap);

    void toAddShiTi(Map<String, Object> parameterMap);


    void toAddjiexi(Map<String, Object> parameterMap);

    void toABCD(HashMap<Object, Object> parameterMap);

    List<Map<String, Object>> shitiList(Page<Map<String, Object>> page);

    void deltimu(Map<String, Object> parameterMap);

   public Map<String,Object> getShiTiById(Map<String,Object> map);

    void deltimushezhi(Map<String, Object> parameterMap);

    List<Map<String, Object>> getShitiDataListByTiKu(Page<Map<String, Object>> page);


    List<Map<String, Object>> getTiKUFenXi(Map<String, Object> map);
}
