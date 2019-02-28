package com.zbf.web;
import com.zbf.core.CommonUtils;
import com.zbf.common.ResponseResult;
import com.zbf.core.page.Page;
import com.zbf.core.utils.FileUploadDownUtils;
import com.zbf.core.utils.UID;
import com.zbf.entity.Shiti;
import com.zbf.entity.SolrPage;
import com.zbf.service.TiKuService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.zbf.enmu.MyRedisKey;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.zbf.enmu.ShiTiLeiXingEnmu;

/**
 * 作者：LCG
 * 创建时间：2019/2/14 11:00
 * 描述：
 */
@RequestMapping("tiku")
@RestController
public class TiKuGuanLiController {
    @Autowired
    private TiKuService tiKuService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SolrClient solrClient;


    /**
     * 添加题库信息
     * @param request
     * @return
     */
    @RequestMapping("toaddTiKuInfo")
    public ResponseResult toaddTiKuInfo(HttpServletRequest request){

        ResponseResult responseResult=ResponseResult.getResponseResult ();

        //获取数据
        Map<String, Object> parameterMap = CommonUtils.getParamsJsonMap ( request );
        //存入数据
        try {
            parameterMap.put ( "id",UID.getUUIDOrder () );
            tiKuService.addTiKuInfo ( parameterMap,redisTemplate);
            responseResult.setSuccess ( "ok" );
        }catch (Exception e){
            e.printStackTrace ();
            responseResult.setError ( "error" );
        }

        return responseResult;

    }

    /**
     * Mysql 题库列表
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("getTikuList")
    public ResponseResult getTikuList(HttpServletRequest httpServletRequest){

        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap ( httpServletRequest );

        Page<Map<String,Object>> page=new Page<> ();

        ResponseResult responseResult=ResponseResult.getResponseResult ();
        //设置查询参数
        page.setParams ( paramsJsonMap );

        Page.setPageInfo ( page, paramsJsonMap);

        //查询 题库
        tiKuService.getTikuList ( page );
        //将数据 存到   result 对象中
        responseResult.setResult ( page );
        return responseResult;

    }

    /**
     * 更新题库信息  并且更新redis
     * @param request
     * @return
     */
    @RequestMapping("updateTiKuInfo")
    public ResponseResult updateTiKuInfo(HttpServletRequest request){

        ResponseResult responseResult=ResponseResult.getResponseResult ();

        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap ( request );

        tiKuService.updateTiKuInfo ( paramsJsonMap,redisTemplate );

        responseResult.setSuccess ( "ok" );

        return responseResult;
    }
    /**
     *
     * delTiKu 删除题库  并且删除redis 中的 题库
     */
    @RequestMapping("delTiKu")
    public  ResponseResult delTiKu(HttpServletRequest request) throws IOException, SolrServerException{
        //获取前台数据
        Map<String, Object> parameterMap = CommonUtils.getParamsJsonMap(request);
        //创建实例
        ResponseResult responseResult=ResponseResult.getResponseResult ();
        tiKuService.delTiKu(parameterMap, redisTemplate);
        return  responseResult;
    }
    /**
     *
     * 从redis 获取题库信息
     */
    @RequestMapping("getTikuListFromRedis")
    public  ResponseResult getTikuListFromRedis(HttpServletRequest request){
        //List 集合
        // List<Map<String,Object>> range = redisTemplate.opsForList ().range ( MyRedisKey.TIKU.getKey (), 0, -1 );

        List<Map<String,Object>> values = redisTemplate.opsForHash().values(MyRedisKey.TIKU.getKey ());

        ResponseResult responseResult=ResponseResult.getResponseResult ();

        responseResult.setResult ( values );

        return responseResult;
    }
    /**
     * 手动添加试题
     * @return
     */
    @RequestMapping("toAddShiTi")
    public ResponseResult toAddShiTi(HttpServletRequest request) throws IOException, SolrServerException{

        ResponseResult responseResult=ResponseResult.getResponseResult ();
        //获取请求数据
        Map<String, Object> parameterMap = CommonUtils.getParameterMap ( request );
        //主键
        parameterMap.put("id", UID.next());
        //用户 id
        parameterMap.put("createuserid", parameterMap.get("userid"));


    HashMap<String,Object> tikuid = (HashMap<String, Object>) redisTemplate.opsForHash().get(MyRedisKey.TIKU.getKey(), parameterMap.get("tikuid").toString());

parameterMap.put("tikuname",tikuid.get("tikuname").toString());
        //添加试题表
        tiKuService.toAddShiTi(parameterMap);
        //填加 题目 ABCD
        tiKuService.toABCD(parameterMap);
        //添加题目解析表
        // tiKuService.toAddjiexi(parameterMap);
        responseResult.setSuccess("ok");
        return responseResult;
    }
    /**
     * 试题 展示
     */
    @RequestMapping("getshitiList")
    public   ResponseResult  shitiList(HttpServletRequest request){
        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap ( request );
        ResponseResult responseResult=ResponseResult.getResponseResult ();
        Page<Map<String,Object>> page=new Page<> ();

        //设置查询参数
        page.setParams ( paramsJsonMap );
        Page.setPageInfo ( page, paramsJsonMap);
        //查询 题目
        tiKuService.shitiList(page);
        //将数据 存到   result 对象中
        responseResult.setResult(page);

        return  responseResult;
    }

    /**
     *   删除试题    刪除Solr
     */
    @RequestMapping("deltiMu")
    public  ResponseResult deltimu(HttpServletRequest request) throws IOException, SolrServerException{
        ResponseResult responseResult = ResponseResult.getResponseResult();
        Map<String, Object> parameterMap = CommonUtils.getParamsJsonMap(request);

        tiKuService.deltimu(parameterMap);

        responseResult.setSuccess("ok");

        return responseResult;
    }


    /**
     * 根据ID获取试题信息
     * @param request
     * @return
     */
    @RequestMapping("getShitiById")
    public ResponseResult getShitiById(HttpServletRequest request){



        ResponseResult responseResult=ResponseResult.getResponseResult ();

        Map<String, Object> parameterMap = CommonUtils.getParameterMap ( request );

        Map<String, Object> shiTiById = tiKuService.getShiTiById ( parameterMap );

        responseResult.setResult ( shiTiById );

        return responseResult;

    }






    /**
     * 下载 模板
     * @param request
     * @param response
     * @throws Exception
     */

    @RequestMapping("getExceltemplate")
    public void getExceltemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {

        File excelTemplate = FileUploadDownUtils.getExcelTemplate ( "exceltemplate/timu.xlsx" );

        FileUploadDownUtils.responseFileBuilder ( response,excelTemplate,"数据模板【题目】.xlsx" );

    }




    /**
     *
     * solr 展示
     */
    @RequestMapping("solrshitiList")
    public  ResponseResult solrList(HttpServletRequest request) throws IOException, SolrServerException{
        ResponseResult responseResult = ResponseResult.getResponseResult();
        SolrPage<Shiti> solrPage = new SolrPage<Shiti>();
        Map<String, Object> parameterMap = CommonUtils.getParamsJsonMap(request);



        SolrPage.setPageInfo(solrPage, parameterMap);

        SolrQuery solrQuery = new SolrQuery();
        //设置查询参数
        if (parameterMap.get("copyzhshiti") != null) {
            solrQuery.set("q", "fuzhiyu:" + parameterMap.get("copyzhshiti"));
        } else {
            solrQuery.set("q", "*:*");
        }
        //开启高亮
        solrQuery.setHighlight(true);
        //设置前缀
        solrQuery.setHighlightSimplePre("<em style='color:blue'>");
        //设置后缀
        solrQuery.setHighlightSimplePost("</em>");
        //添加高亮的字段
        solrQuery.addHighlightField("tikuname");

        //设置分页
        solrQuery.setStart((solrPage.getPageNo() - 1) * solrPage.getPageSize());
        //设置每页返回多少条
        solrQuery.setRows(solrPage.getPageSize());
        //查询
        QueryResponse queryResponse = solrClient.query(solrQuery);

        //获取查询的数据
        List<Shiti> listBeans = queryResponse.getBeans(Shiti.class);
        //获取总条数，设置给solrPage
        solrPage.setTotalCount(queryResponse.getResults().getNumFound());
        //获取高亮的数据
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        //遍历高亮数据
        listBeans.forEach((item) -> {
            if(item.getShitizhuangtai().equals("1")){
                item.setShitizhuangtai ("开放");
            }else{
                item.setShitizhuangtai ("关闭");
            }


            highlighting.entrySet().forEach((entry1) -> {
                /*System.out.println(entry1);*/
            });
        });


        solrPage.setResultList(listBeans);

        responseResult.setResult(solrPage);

        return  responseResult;

    }

    @RequestMapping("toFenxiTiku")
    public ResponseResult toFenxiTiku(HttpServletRequest request){

        ResponseResult responseResult=ResponseResult.getResponseResult ();

        Map<String, Object> parameterMap = CommonUtils.getParamsJsonMap ( request );

        List<Map<String, Object>> tiKUFenxi = tiKuService.getTiKUFenxi ( parameterMap );
        List<String> listName =new ArrayList<>(  );
        int countnum=0;
        Map<String,Object> mmp=new HashMap<> (  );
        if(tiKUFenxi.size ()>0){
            for(Map<String,Object> map:tiKUFenxi){
                String tixigid=map.get ( "name" ).toString ();
                map.put ( "name",ShiTiLeiXingEnmu.getNameFromId ( tixigid ) );
                listName.add ( ShiTiLeiXingEnmu.getNameFromId ( tixigid ) );
                countnum=countnum+Integer.valueOf ( map.get ( "value" ).toString () );
            }
            mmp.put ( "tubiaoData",tiKUFenxi );
            mmp.put ( "data",listName);
            mmp.put ( "countnum",countnum);
        }else{
            mmp.put ( "tubiaoData",tiKUFenxi );
            mmp.put ( "data",listName);
            mmp.put ( "countnum",0);
        }

        responseResult.setResult ( mmp );

        mmp=null;
        tiKUFenxi=null;
        listName=null;

        return responseResult;
    }

}
