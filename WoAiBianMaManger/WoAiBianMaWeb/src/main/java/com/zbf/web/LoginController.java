package com.zbf.web;

import com.alibaba.fastjson.JSON;
import com.zbf.common.ResponseResult;
import com.zbf.core.CommonUtils;
import com.zbf.core.utils.AESUtils;
import com.zbf.core.utils.MD5;
import com.zbf.core.utils.UID;
import com.zbf.jwt.JWTUtils;
import com.zbf.oauthLogin.User;
import com.zbf.service.UserService;
import com.zbf.yanZhengCode.VerifyCodeUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.InetAddress;
import java.util.Map;

/**
 * 作者：LCG
 * 创建时间：2018/11/23 22:36
 * 描述：
 */
@RestController
public class LoginController {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    /**
     * 获取滑动验证的验证码
     * @return
     */
    @RequestMapping("getCode")
    public ResponseResult getCode(HttpServletRequest request, HttpServletResponse response){


        String code= VerifyCodeUtils.generateVerifyCode (5);
        ResponseResult responseResult=ResponseResult.getResponseResult ();
        responseResult.setResult ( code );

        return responseResult;
    }

    //登录
    //登陆接口
    @RequestMapping("/login")
    public ResponseResult login(HttpServletRequest request) throws Exception {
        //获取参数
        Map<String, Object> parameterMap = CommonUtils.getParameterMap ( request );
        String canshu=parameterMap.get ( "canshu" ).toString ();
        //AES解密参数
        String decrypt = AESUtils.desEncrypt ( canshu );//json的参数

        User user=null;
        User userget=null;

        ResponseResult responseResult = ResponseResult.getResponseResult ();


/*
        InetAddress address = InetAddress.getLocalHost();
        System.out.println("IP地址为=====================:"+address);
        userget.setAddress(address.toString());*/
        if(decrypt!=null){
            user = JSON.parseObject ( decrypt, User.class );
            //前台传递过来的密码
            String password=user.getPassword ();
            //1 学员   0管理员
            if (user.getUseridentity().equals("1")==true){
                //暂时不加密简单来
                // password=MD5.encryptPassword ( password,password );

                //从数据库认证一下用户信息
                userget=userService.getBySchoolUserName (user.getLoginname ());


                if(userget==null){
                    responseResult.setError ( "用户名或密码错误！" );
                    return responseResult;
                }
                //验证密码
                if(!userget.getPassword ().equals ( password )){
                    responseResult.setError ( "用户名或密码错误！" );
                    return responseResult;
                }
                if (userget.getUserstatus().equals("0")){
                    responseResult.setError ( "该用户被锁定请联系管理员！" );
                    return responseResult;
                }

                responseResult.setSuccess ( "yhok" );

            }else if (user.getUseridentity().equals("0")==true){
                //加密
                password=MD5.encryptPassword ( password,password );
                //从数据库认证一下用户信息
                userget=userService.getByUserName (user.getLoginname ());
                if(userget==null){
                    responseResult.setError ( "用户名或密码错误！" );
                    return responseResult;
                }
                //验证密码
                if(!userget.getPassword ().equals ( password )){
                    responseResult.setError ( "用户名或密码错误！" );
                    return responseResult;
                }
                    responseResult.setSuccess ( "glyok" );

            }


        }
        //根据用户信息生成Token
        String token = JWTUtils.generateToken ( JSON.toJSONString ( userget ) );
        responseResult.setToken ( token );

        responseResult.setResult ( userget );
        //将用户信息放进redis中
        redisTemplate.opsForHash ().put ( userget.getId (),"userinfo", userget);
        return responseResult;
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @RequestMapping("loginout")
    public ResponseResult loginout(HttpServletRequest request){

        Claims claims=(Claims)request.getAttribute ( "userinfo" );
        if(claims!=null&&claims.get ( "userinfo" )!=null){
            User userinfo = JSON.parseObject ( claims.get ( "userinfo" ).toString (), User.class );
            if(userinfo!=null){
                //清除用户信息
                redisTemplate.delete ( userinfo.getId () );
            }
        }
        ResponseResult responseResult = ResponseResult.getResponseResult ();
        responseResult.setSuccess ( "ok" );

        return responseResult;
    }

}
