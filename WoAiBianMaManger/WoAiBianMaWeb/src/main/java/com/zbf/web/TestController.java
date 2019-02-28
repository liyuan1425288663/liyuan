package com.zbf.web;




import com.zbf.zhongjian.webSocket.WebSocketServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.websocket.Session;

/**
 * 作者：LCG
 * 创建时间：2019/2/18 20:12
 * 描述：
 */
@RestController
@RequestMapping("test")
public class TestController {



   @RequestMapping("sendmessage")
    public void sendmessag(){

        System.out.println ("===========发送消息==============");
        System.out.println (WebSocketServer.sessionMap.size ());
        Session session =(Session)WebSocketServer.sessionMap.get ( "1" );

    WebSocketServer.sendMessage ( session,"12345！","1" );

    }


   /* @RequestMapping("sendTest")
    public void sendTest(){

        Session session = WebSocketServerTest.mapsession.get ( "1" );
        WebSocketServerTest.sendMessageFromServer ( session,"hi，i am server","1");
    }*/


}
