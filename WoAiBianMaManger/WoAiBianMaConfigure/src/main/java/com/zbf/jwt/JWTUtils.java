package com.zbf.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：LCG
 * 创建时间：2019/1/25 9:49
 * 描述：
 */
public class JWTUtils {

    //生成JWT 加密信息
    public static String generateToken(String userinfo) {
        Map<String, Object> map = new HashMap<> ();
        map.put("userinfo", userinfo);
        map.put("created", new Date ());

        return Jwts.builder().setClaims(map)//payload 设置信息
                .setExpiration(new Date(System.currentTimeMillis() + 30*60*1000L))  //过期时间
                //加密算法名称             //签名的键
                .signWith(SignatureAlgorithm.HS512,"secretkey").compact();  //加密方式
    }

}
