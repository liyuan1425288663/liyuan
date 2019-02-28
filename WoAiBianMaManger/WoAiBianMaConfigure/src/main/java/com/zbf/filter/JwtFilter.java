package com.zbf.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zbf.jwt.JWTUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;

public class JwtFilter extends GenericFilterBean {

    private RedisTemplate redisTemplate;

    public JwtFilter(RedisTemplate redisTemplate){
        this.redisTemplate=redisTemplate;
    }

    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        // 如果http请求是 OPTIONS 然后直接返回状态码 200
        //OPTIONS性质的请求是探测性质的请求
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(req, res);
        }else {// 除了OPTIONS的其他请求都要JWT验证
            // 从request请求头中获取authorization
            // 从 authorization中获取 JWT的token
            final String authHeader = request.getHeader("Authorization");
            String token = authHeader.substring(0);
            //使用 jwt解析 验证signature的键secretkey是否是有用的
            try{
                Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
                // 将claims中的用户信息添加到请求头中
                request.setAttribute("userinfo", claims);
                JSONObject userinfo = JSON.parseObject ( claims.get ( "userinfo" ).toString () );
                Long userid = userinfo.getLong ( "id" );
                //从redis中取出User信息 否则登录失败
                Object userinfo1 = redisTemplate.opsForHash ().get ( userid, "userinfo" );
                if(userinfo1!=null){
                    String refreshtoken=JWTUtils.generateToken ( claims.get ( "userinfo" ).toString () );
                    response.setHeader ( "authorization",refreshtoken );
                    //要
                    response.setHeader ( "Access-Control-Expose-Headers","authorization");

                    chain.doFilter(request, response);
                }else{
                    throw new RuntimeException ( "token过期，登录失败，请重新登录" );
                }

            }catch (ExpiredJwtException e){//如果JWT过期则抛出过期异常
                e.printStackTrace ();
                //抛出异常
                throw new RuntimeException ( "token过期，登录失败，请重新登录" );
            }
        }
    }
}