package com.zbf.mapper;

import com.zbf.core.page.Page;
import com.zbf.oauthLogin.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 作者：LCG
 * 创建时间：2019/1/23 18:02
 * 描述：
 */
@Mapper
public interface UserMapper {

  public User getUserByUserName(String loginnname);
  public User getBySchoolUserName(String loginnname);


  //根据用户Id获取用户的信息
  public Map<String,Object> getUserById(String userid);
  //查询用户的分页信息
  public List<Map<String,Object>> getUserList(Page<Map<String,Object>> page);

  public int toBangDingRoleForUser(List<Map<String,Object>> list);

  public int deleteRoleUser(String userId);

  void onlyUpdateUserInfo(Map<String, Object> parameterMap);

  Map<String, Object> getKaoShengInfo(String userid);
}
