
package com.zbf.zhongjian.webSocket;

import com.zbf.utils.SpringUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


/**
 * 作者：LCG
 * 创建时间：2019/2/22 23:53
 * 描述：
 */

@Component
@ServerEndpoint( "/ws/websocket/{userid}" )
public class WebSocketServer {

    private static RedisTemplate redisTemplate=SpringUtil.getBean ( "redisTemplate",RedisTemplate.class);

    public static Map<String,Object> sessionMap=new ConcurrentHashMap<> (  );

    @OnOpen
    public void onOpen(@PathParam ("userid") String userid, Session session){

        sessionMap.put ( userid,session );

        System.out.println ("==========打开连接===》");

        //检查有没有离线的消息
        List<String> range = redisTemplate.opsForList ().range ( "user" + userid, 0, -1 );
        if(range.size ()>0){
            //发送离线消息
            range.forEach ( (msg)->{
                this.sendMessage ( session,msg,userid);
            });
        }
    }

    @OnMessage
    public void getMessageClient(@PathParam ( "userid" ) String userid,Session sesion,String message){
        System.out.println ("======接收到的消息====》"+message);
    }


/**
     * 发生错的时候调用
     * @param session
     * @param throwable
     */

    @OnError
    public void error(Session session,Throwable throwable){
        System.out.println ("===========出错了==");
    }


/**
     * 关闭的时候调用
     * @param session
     * @param userid
     */

    @OnClose
    public void close(Session session,@PathParam ( "userid" ) String userid){

        //删除session
        System.out.println ("========链接关闭");
    }



    //发送消息
    public static void sendMessage(Session session, String message,String userid) {
        try {


            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
            redisTemplate.opsForList ().leftPush ("user"+userid,message);
        }
     }

}

