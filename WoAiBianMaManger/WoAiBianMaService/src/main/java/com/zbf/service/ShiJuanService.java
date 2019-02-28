package com.zbf.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zbf.core.page.Page;
import com.zbf.core.utils.UID;
import com.zbf.entity.Shiti;
import com.zbf.mapper.ShiJuanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ShiJuanService {

    @Autowired
    private ShiJuanMapper shiJuanMapper;


    public List<Map<String,Object>> getfenlei(Map<String, Object> paramsJsonMap){

    return     shiJuanMapper.getfenlei(paramsJsonMap);
    }

    public void getbumenList(Page<Map<String,Object>> page){


        List<Map<String, Object>> list = shiJuanMapper.getbumenList(page);

        page.setResultList ( list );
    }

    public void getuserList(Page<Map<String, Object>> page){


        List<Map<String, Object>> list = shiJuanMapper.getuserList(page);






        list.forEach((itam)->{


            StringBuffer stringBuffer = new StringBuffer();
            String lastlogintime = itam.get("Lastlogintime").toString();
            String cishu = itam.get("cishu").toString();
            stringBuffer.append("登录次数"+cishu+"次"+"    "+lastlogintime);
            itam.put("Lastlogintime",stringBuffer);
            stringBuffer=null;
            if(itam.get ( "userstatus" ).toString ().equals ( "0" )){
                itam.put ( "userstatus","锁定");
            }else if (itam.get ( "userstatus" ).toString ().equals ( "1" )){
                itam.put ( "userstatus","待审核");
            }else {
            itam.put ( "userstatus","正常");

        }
        });

        page.setResultList ( list );


    }

    public void Addshijuan(Map<String, Object> paramsJsonMap){

        paramsJsonMap.put("createtime", new Date());

            shiJuanMapper.Addshijuan(paramsJsonMap);
    }

    public void AddshijuanUser(Map<String, Object> paramsJsonMap){

        String user = paramsJsonMap.get("user").toString();

        List<String> userlist = JSON.parseArray(user).toJavaList(String.class);

        HashMap<String, Object> usermap = new HashMap<>();
        usermap.put("shijuanid",paramsJsonMap.get("shijuanid").toString());

        for (int i = 0; i < userlist.size(); i++) {
            Map<String,Object> parse = (Map<String, Object>) JSON.parse(userlist.get(i));
            usermap.put("id",UID.next());
            usermap.put("userid",parse.get("userid").toString());

            shiJuanMapper.AddshijuanUser(usermap);
        }

    }

    public void AddshijuanBumen(Map<String, Object> paramsJsonMap){

        String bumen = paramsJsonMap.get("bumen").toString();

        List<String> bumenlist = JSON.parseArray(bumen).toJavaList(String.class);

        HashMap<String, Object> bumenmap = new HashMap<>();
        bumenmap.put("shijuanid",paramsJsonMap.get("shijuanid").toString());

        for (int i = 0; i < bumenlist.size(); i++) {
            Map<String, Object> parse = (Map<String, Object>) JSON.parse(bumenlist.get(i));
            bumenmap.put("id", UID.next());
            bumenmap.put("bumenid", parse.get("bumenid").toString());

            shiJuanMapper.AddshijuanBumen(bumenmap);
        }

    }


    public void ShiJuanList(Page<Map<String, Object>> page){

   List<Map<String,Object>> list=  shiJuanMapper.ShiJuanList(page);



        //stringBuilder 在 堆 中  不会 在new 对象  Sring 类型会new 对象
        list.forEach((lis->{
               if (lis.get("zhuangtai").equals("1")){
                   lis.put("zhuangtai","开放");
               }else {
                   lis.put("zhuangtai","关闭");
               }
               if (lis.get("shijuanleixing").equals("1")){

                   lis.put("shijuanleixing","普通试卷");

               }else {
                   lis.put("shijuanleixing","随机试卷");
               }
               StringBuilder stringBuilder = new StringBuilder();
                   String shijian = lis.get("shijian").toString();
                        stringBuilder.append(shijian+"分钟");
                        lis.put("shijian",stringBuilder);



   }));







        page.setResultList(list);
    }
}
