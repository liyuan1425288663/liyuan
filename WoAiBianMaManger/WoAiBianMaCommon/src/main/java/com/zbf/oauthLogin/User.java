package com.zbf.oauthLogin;

import com.zbf.common.IdEntity;
import lombok.Data;

/**
 * 作者：LCG
 * 创建时间：2018/11/25 11:01
 * 描述：
 */
@Data
public class User extends IdEntity {

    private String username;

    private String loginname;

    private String password;

    private String code;

    private String tel;
    // 考生 角色 识别
    private String Useridentity;
    //考生 状态
    private String userstatus;
    private int status;

    private int sex;
    //用户的头像信息
    private String userImage;
    //用户当前登录ip
    private  String address;

}
