package com.zbf.service;

import com.alibaba.fastjson.JSON;
import com.zbf.core.page.Page;
import com.zbf.core.utils.UID;
import com.zbf.enmu.MyRedisKey;
import com.zbf.entity.Shiti;
import com.zbf.mapper.TiKuMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class TiKuService {
    @Autowired
    private TiKuMapper tiKuMapper;
    @Autowired
    private SolrClient solrClient;
    @Autowired
    private RedisTemplate redisTemplate;


  //试题 solr 实体类
    private   Shiti shiti = new Shiti();

    private   SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Transactional
    public int addTiKuInfo(Map<String,Object> map, RedisTemplate redisTemplate){
        //List 集合
        //       redisTemplate.opsForList ().rightPush ( MyRedisKey.TIKU.getKey (),map);
        redisTemplate.opsForHash().put(MyRedisKey.TIKU.getKey (),map.get("id").toString(),map);

        return tiKuMapper.addTiKuInfo ( map );
    }

    public void getTikuList(Page<Map<String,Object>> page){

        List<Map<String,Object>> list=tiKuMapper.getTikuList(page);

        list.forEach ( (item)->{
            if(item.get ( "tikuzhuangtai" ).toString ().equals ( "1" )){
                item.put ( "tikuzhuangtai","开放");
            }else{
                item.put ( "tikuzhuangtai","关闭");
            }
        } );

        page.setResultList ( list );
    }

    /**
     * 修改  redis 题库
     * @param parameterMap
     * @param redisTemplate
     */
    @Transactional
    public void updateTiKuInfo(Map<String,Object> parameterMap, RedisTemplate redisTemplate){
        //刪除redis
        Long id = redisTemplate.opsForHash().delete(MyRedisKey.TIKU.getKey (),parameterMap.get("id").toString());

        //添加redis
        redisTemplate.opsForHash().put(MyRedisKey.TIKU.getKey (),parameterMap.get("id").toString(),parameterMap);
        //修改数据库mysql
        tiKuMapper.updateTiKuInfo ( parameterMap );
    }

    /**
     * 删除  redis 题库
     * @param parameterMap
     * @param redisTemplate
     */
    @Transactional
    public void delTiKu(Map<String, Object> parameterMap, RedisTemplate redisTemplate) throws IOException, SolrServerException{
        //刪除redis
        Long id = redisTemplate.opsForHash().delete(MyRedisKey.TIKU.getKey (),parameterMap.get("id").toString());

        // id 大于0 代表成功
        tiKuMapper.delTiKu ( parameterMap );
    }



    /**
     * 添加解析   无用功能
     * @param parameterMap
     */
    public void toAddjiexi(Map<String, Object> parameterMap){

        tiKuMapper.toAddjiexi ( parameterMap );

    }

    /**
     *
     * ABCD 选项框 跟 选项的数据处理
     * @param parameterMap
     */

    public void toABCD(Map<String, Object> parameterMap) throws IOException, SolrServerException{

        String inputValues =  parameterMap.get("inputValues").toString();
        String kuanglist =  parameterMap.get("kuanglist").toString();
        //选项 A BC D
        List<String> ListinputValues = JSON.parseArray(inputValues).toJavaList(String.class);
        //框框
        List<String> kuanglistbianli = JSON.parseArray(kuanglist).toJavaList(String.class);

        HashMap<Object, Object> ABCDMap = new HashMap<>();

        ABCDMap.put("timuid",parameterMap.get("id").toString());
        for (int i = 0; i < kuanglistbianli.size(); i++) {
            ABCDMap.put("id",UID.next());
            //添加选项编号
            ABCDMap.put("xuanxiangbianhao",kuanglistbianli.get(i));
            //添加选项编号 对应的题目
            ABCDMap.put("xuanxiang",ListinputValues.get(i));
            tiKuMapper.toABCD ( ABCDMap );

        }

    }

    /**
     * 添加试题 +添加入solr 手动
     * @param parameterMap
     * @throws IOException
     * @throws SolrServerException
     */

    public void toAddShiTi(Map<String, Object> parameterMap) throws IOException, SolrServerException{
        String checkList = parameterMap.get("checkList").toString();
        StringBuilder newcheckList = new StringBuilder();
        List<String> strings = JSON.parseArray(checkList).toJavaList(String.class);
        for (int i = 0; i < strings.size(); i++) {
            newcheckList.append(strings.get(i));
        }
        //存入处理好的答案
        parameterMap.put("checkList", newcheckList.toString());
        //创建时间
        parameterMap.put("createtime", new Date());
        tiKuMapper.toAddShiTi ( parameterMap );
        //以下 是添加solr 数据

        //答案
        shiti.setDaan(newcheckList.toString());
        //时间
        shiti.setCreatetime(sd.format(new Date()));

        shiti.setCreatename(sd.format(new Date())+parameterMap.get("username").toString());
        shiti.setShitizhuangtai(parameterMap.get("shitizhuangtai").toString());
        shiti.setId(parameterMap.get("id").toString());
        shiti.setTigan(parameterMap.get("tigan").toString());
        shiti.setNandu(parameterMap.get("nanduname").toString());


        shiti.setTikuname(parameterMap.get("tikuname").toString());


        shiti.setTixingname(parameterMap.get("tixingname").toString());
        shiti.setXuanxiangbianhao(parameterMap.get("inputValues").toString());
        shiti.setXuanxiang(parameterMap.get("kuanglist").toString());
        solrClient.addBean(shiti);
        solrClient.commit();


    }



    public void shitiList(Page<Map<String,Object>> page){

        List<Map<String,Object>> list=tiKuMapper.shitiList(page);

        list.forEach ( (item)->{
            if(item.get ( "shitizhuangtai" ).toString ().equals ( "1" )){
                item.put ( "shitizhuangtai","开放");
            }else{
                item.put ( "shitizhuangtai","关闭");
            }
        } );

        page.setResultList ( list );
    }

    /**
     *
     * 删除+Solr题目
     * @param parameterMap
     */
    public void deltimu(Map<String, Object> parameterMap) throws IOException, SolrServerException{
        //  刪除Solr
        solrClient.deleteById(parameterMap.get("id").toString());
        solrClient.commit();

        tiKuMapper.deltimu(parameterMap);
        tiKuMapper.deltimushezhi(parameterMap);

    }

    /**
     * 批量添加试题  题干
     * @param listdata
     */
    public void addAllShiti(List<Map<String, Object>> listdata, Map<String, Object> parameterMap) throws IOException, SolrServerException{
        //处理答案格式

        //添加 题目
        for (int i = 0; i < listdata.size(); i++) {
            Map<String, Object> map = listdata.get(i);
            map.putAll(parameterMap);

            String checkList = map.get("daan").toString();

            List<String> strings = JSON.parseArray(checkList).toJavaList(String.class);
            StringBuilder newcheckList = new StringBuilder();
            for (int j = 0; j < strings.size(); j++) {

                newcheckList.append(strings.get(j));
            }
            map.put("checkList",newcheckList.toString());

            map.put("createtime",new Date());

            map.put("createuserid",parameterMap.get("userid").toString());
            map.put("timujiexi","暂无解析");

            tiKuMapper.toAddShiTi(map);



            // 批量添加到solr
            //答案
            shiti.setDaan(newcheckList.toString());
            //时间
            shiti.setCreatetime(sd.format(new Date()));
            shiti.setCreatename(sd.format(new Date())+parameterMap.get("username").toString());
            shiti.setShitizhuangtai(parameterMap.get("shitizhuangtai").toString());
           shiti.setId(map.get("id").toString());
            HashMap<String,Object> tikuid = (HashMap<String, Object>) redisTemplate.opsForHash().get(MyRedisKey.TIKU.getKey(),map.get("tikuid").toString());

            shiti.setTigan(map.get("tigan").toString());
            shiti.setNandu(parameterMap.get("nanduname").toString());
            shiti.setTikuname(tikuid.get("tikuname").toString());
            shiti.setTixingname(parameterMap.get("tixingname").toString());
            shiti.setXuanxiangbianhao(map.get("kuanglist").toString());
            shiti.setXuanxiang(map.get("checkList").toString());
            solrClient.addBean(shiti);
            solrClient.commit();


        }






        //添加 ABCD
        for (int i = 0; i < listdata.size(); i++) {
            Map<String, Object> map = listdata.get(i);

            String inputValues = map.get("inputValues").toString();
            String kuanglist = map.get("kuanglist").toString();
            //选项 A BC D
            List<String> ListinputValues = JSON.parseArray(inputValues).toJavaList(String.class);
            //框框
            List<String> kuanglistbianli = JSON.parseArray(kuanglist).toJavaList(String.class);

            HashMap<Object, Object> ABCDMap = new HashMap<>();

            ABCDMap.put("timuid", map.get("id").toString());
            for (int w = 0; w < kuanglistbianli.size(); w++) {
                ABCDMap.put("id", UID.next());
                //添加选项编号
                ABCDMap.put("xuanxiangbianhao", kuanglistbianli.get(w));
                //添加选项编号 对应的题目
                ABCDMap.put("xuanxiang", ListinputValues.get(w));
                tiKuMapper.toABCD(ABCDMap);

            }




        }

    }


    public List<Map<String,Object>> getTiKUFenxi(Map<String,Object> map){
        return tiKuMapper.getTiKUFenXi ( map );
    }



    public Map<String,Object> getShiTiById(Map<String,Object> map){
        Map<String, Object> params = tiKuMapper.getShiTiById ( map );
        if(params.get ( "tixingid" )!=null&&params.get ( "tixingid" ).toString ().equals ( "1" )){//单选题
            //试题的选项编号
            params.put ( "xuanxiangbianhao",params.get ( "xuanxiangbianhao" ).toString () );
            //试题的选项描述
            params.put ( "xuanxiangmiaoshu",params.get ( "xuanxiangmiaoshu" ).toString () );

        }else if(params.get ( "tixingid" )!=null&&params.get ( "tixingid" ).toString ().equals ( "2" )){//多选题
            //试题答案
            params.put ( "daan",params.get ( "daan" ).toString ()  );
            //试题的选项编号
            params.put ( "xuanxiangbianhao", params.get ( "xuanxiangbianhao" ).toString () );
            //试题的选项描述
            params.put ( "xuanxiangmiaoshu",params.get ( "xuanxiangmiaoshu" ).toString () );

        }else if(params.get ( "tixingid" )!=null&&params.get ( "tixingid" ).toString ().equals ( "3" )){//判断题
            //试题的判断描述
            Object miaoshu=params.get ( "xuanxiangmiaoshu" )!=null?params.get ( "xuanxiangmiaoshu" ):"";
            params.put ( "xuanxiangmiaoshu",miaoshu.toString ()  );

        }

        return params;
    }
}
