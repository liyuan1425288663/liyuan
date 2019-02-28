package com.zbf.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zbf.core.page.Page;
import com.zbf.core.utils.UID;
import com.zbf.mapper.UserMapper;
import com.zbf.oauthLogin.User;
import org.apache.ibatis.mapping.ParameterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：LCG
 * 创建时间：2019/1/23 18:40
 * 描述：
 */
@Component
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getByUserName(String loginnname){
        return userMapper.getUserByUserName ( loginnname );
    }
    public User getBySchoolUserName(String loginnname){
        return userMapper.getBySchoolUserName ( loginnname );
    }



    public Map<String,Object> getUserById(String userid){
        return userMapper.getUserById ( userid );
    }

    public Map<String, Object> getKaoShengInfo(String userid){
        return userMapper.getKaoShengInfo ( userid );
    }

    public void getUserList(Page<Map<String,Object>> page){
        List<Map<String, Object>> userList = userMapper.getUserList ( page );

        for(Map<String,Object> mmp:userList){
            if(mmp.get ( "sex" ).toString ().equals ( "1" )){
                mmp.put ( "sex","男" );
            }else{
                mmp.put ( "sex","女" );
            }
        }
        page.setResultList ( userList );
    }

    /**
     * 添加用户信息
     */
    public void toAddUserInfo(MultipartFile[] multipartFiles,Map<String,Object> mapUserInfo){

        //上传图片

        //添加用户信息

    }

    /**
     * 绑定用户和角色
     * @param params
     * @return
     */
    @Transactional
    public int toBangDingRoleForUser(Map<String,Object> params){
        //先删除用户绑定的角色
        userMapper.deleteRoleUser ( params.get ( "userId" ).toString () );
        //绑定用户和角色
        JSONArray jsonArray=(JSONArray) params.get ( "roleIds" );
        List<String> strings = jsonArray.toJavaList ( String.class );
        List<Map<String,Object>> list=new ArrayList<> (  );
        for(String roleid:strings){
            Map<String,Object> map=new HashMap<> (  );
            map.put ( "id", UID.getUUIDOrder ());
            map.put ( "roleId",roleid );
            map.put ( "userId",params.get ( "userId" ).toString () );
            list.add ( map );
            map=null;
        }
        int i=0;
        if(list.size ()>0){
            i = userMapper.toBangDingRoleForUser ( list );
        }
        list=null;
        return i;
    }

    /**
     *
     * 修改 用户信息
     * @param parameterMap  用户 信息
     */
    public void onlyUpdateUserInfo(Map<String, Object> parameterMap){

        Object sex = parameterMap.get("sex");
        if (sex.toString().equals("男")){
            parameterMap.put("sex",1);
        } if(sex.toString().equals("女")){
            parameterMap.put("sex",0);
        }


        userMapper.onlyUpdateUserInfo(parameterMap);

    }


}
