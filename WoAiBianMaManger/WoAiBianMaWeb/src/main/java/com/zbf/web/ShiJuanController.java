package com.zbf.web;

import com.zbf.common.ResponseResult;
import com.zbf.core.CommonUtils;
import com.zbf.core.page.Page;
import com.zbf.core.utils.UID;
import com.zbf.service.ShiJuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("shijuan")
public class ShiJuanController {

    @Autowired
    private ShiJuanService shiJuanService;


    @RequestMapping("getfenlei")
    public ResponseResult getfenlei(HttpServletRequest request){


        ResponseResult responseResult = ResponseResult.getResponseResult();
        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap(request);

        List<Map<String, Object>> getfenlei = shiJuanService.getfenlei(paramsJsonMap);
        responseResult.setResult(getfenlei);
        return responseResult;
    }

    @RequestMapping("getbumenList")
    public ResponseResult getbumenList(HttpServletRequest request){

        ResponseResult responseResult = ResponseResult.getResponseResult();
        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap(request);
        Page<Map<String,Object>> page=new Page<> ();

        //设置查询参数
        page.setParams ( paramsJsonMap );
        Page.setPageInfo ( page, paramsJsonMap);



        shiJuanService.getbumenList(page);
        //将数据 存到   result 对象中
        responseResult.setResult(page);
        return   responseResult;
    }

    @RequestMapping("getuserList")
    public ResponseResult getuserList(HttpServletRequest request){

        ResponseResult responseResult = ResponseResult.getResponseResult();
        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap(request);
        Page<Map<String,Object>> page=new Page<> ();

        //设置查询参数
        page.setParams ( paramsJsonMap );
        Page.setPageInfo ( page, paramsJsonMap);


        shiJuanService.getuserList(page);
        //将数据 存到   result 对象中
        responseResult.setResult(page);
        return   responseResult;
    }
    @RequestMapping("toAddshijuan")
public  ResponseResult toAddshijuan(HttpServletRequest request){

        ResponseResult responseResult = ResponseResult.getResponseResult();

        Map<String, Object> paramsJsonMap = CommonUtils.getParameterMap(request);



        // 创建试卷 加三表
        //试卷id
        paramsJsonMap.put("shijuanid",UID.next());
        //添加试卷
        shiJuanService.Addshijuan(paramsJsonMap);

        // 用户 userid  试卷shijuanid  添加到 试卷用户中

        shiJuanService.AddshijuanUser(paramsJsonMap);
          //部门 bumenid   试卷shijuanid  添加到 试卷部门中
        shiJuanService.AddshijuanBumen(paramsJsonMap);


        return   responseResult;
    }


  @RequestMapping("ShiJuanList")
    public  ResponseResult ShiJuanList(HttpServletRequest request){

      ResponseResult responseResult = ResponseResult.getResponseResult();
      Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap(request);

      Page<Map<String, Object>> page = new Page<>();
      //设置查询参数
      page.setParams(paramsJsonMap);
      Page.setPageInfo(page,paramsJsonMap);
     shiJuanService.ShiJuanList(page);

      responseResult.setResult(page);

      return  responseResult;

  }






}
