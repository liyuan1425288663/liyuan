package com.zbf.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zbf.common.ResponseResult;
import com.zbf.core.CommonUtils;
import com.zbf.core.page.Page;
import com.zbf.core.utils.UID;
import com.zbf.mapper.TiKuMapper;
import com.zbf.service.TiKuService;
import org.apache.poi.xssf.usermodel.*;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 作者：LCG
 * 创建时间：2019/2/18 20:12
 * 描述：
 */
@RestController
@RequestMapping("test")
public class UploadsAnddownloads {

    @Autowired
    private TiKuMapper tiKuMapper;
    @Autowired
    private TiKuService tiKuService;




    /**
     * Excel文件上传，导入数据
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("toImportExcelData")
    public ResponseResult toImportExcelData(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException, SolrServerException{
        ResponseResult responseResult=ResponseResult.getResponseResult ();
         String canshu = request.getParameter("canshu");
         //转换为map

        Map<String, Object>  parameterMap = (Map) JSONObject.parse(canshu);



        //得到表格的输入流
        InputStream inputStream = file.getInputStream ();

        XSSFWorkbook xssfWorkbook=new XSSFWorkbook ( inputStream );

        XSSFSheet sheetAt = xssfWorkbook.getSheetAt ( 0 );

        int physicalNumberOfRows = sheetAt.getPhysicalNumberOfRows ();//获取数据的行数

        XSSFRow row1 = sheetAt.getRow ( 0 );

        XSSFCell cell = row1.getCell ( 0 );
        cell.getStringCellValue ();//获取字符数据
        List<Map<String,Object>> listdata=new ArrayList<> (  );
        for(int i=1;i<physicalNumberOfRows;i++){
            XSSFRow row = sheetAt.getRow ( i );
            row.getPhysicalNumberOfCells ();
            Map<String,Object> maprow=new HashMap<String,Object>();
            maprow.put ("tigan",row.getCell ( 0 ).getStringCellValue ());
            maprow.put ("kuanglist",row.getCell ( 1 ).getStringCellValue ());
            List<String> xuanxiangmiaoshu=new ArrayList<> (  );
            xuanxiangmiaoshu.add ( row.getCell ( 2 ).getStringCellValue () );
            xuanxiangmiaoshu.add ( row.getCell ( 3 ).getStringCellValue () );
            xuanxiangmiaoshu.add ( row.getCell ( 4 ).getStringCellValue () );
            xuanxiangmiaoshu.add ( row.getCell ( 5 ).getStringCellValue () );

            maprow.put ( "inputValues",JSON.toJSONString ( xuanxiangmiaoshu ) );
            long timuid = UID.next();

            maprow.put("id",timuid);
            //真确答案
            maprow.put ( "daan",row.getCell ( 6 ).getStringCellValue ());

            if(row.getCell ( 7 )!=null){
                /*row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);*/
                maprow.put ( "timujiexi",row.getCell ( 7 ).getStringCellValue ());
            }

            listdata.add ( maprow );
        }

        tiKuService.addAllShiti(listdata,parameterMap);


        return responseResult;

    }


    /**
     * Excel数据的导出 案例
     * @param request
     * @param response
     */
    @RequestMapping("exportExcelData")
    public void exportExcelData(HttpServletRequest request,HttpServletResponse response) throws IOException {

        //获取数据

        Page<Map<String,Object>> page=new Page<> ();

        Map<String, Object> MAP = CommonUtils.getParameterMap ( request );
        String canshu = MAP.get("canshu").toString();
        Map<String, Object> parameterMap = (Map<String, Object>) JSONObject.parse(canshu);





        page.setPageSize ( 30 );
        page.setParams ( parameterMap );
        Map<String, Object> params1 = page.getParams();
        Object params2 = params1.get("id");
        System.out.println(page.toString());


        List<Map<String, Object>> resultList = tiKuMapper.getShitiDataListByTiKu ( page );

        //POI的api的操作
        XSSFWorkbook xssfWorkbook=new XSSFWorkbook (  );


        XSSFSheet sheet = xssfWorkbook.createSheet ( parameterMap.get("tikuname").toString());
        XSSFRow row1 = sheet.createRow ( 0 );
        row1.createCell ( 0 ).setCellValue ( "ID" );
        row1.createCell ( 1 ).setCellValue ( "答案" );
        row1.createCell ( 2 ).setCellValue ( "答案解析" );
        row1.createCell ( 3 ).setCellValue ( "题干描述" );
        row1.createCell ( 4 ).setCellValue ( "试题类型" );

        for(int i=0;i<resultList.size ();i++){

            Map<String, Object> map = resultList.get ( i );
            XSSFRow row = sheet.createRow ( i+1 );

            List<String> collect = map.keySet ().stream ().collect ( Collectors.toList () );
            for(int j=0;j<collect.size ();j++){
                XSSFCell cell =row.createCell ( j );
                cell.setCellValue (map.get ( collect.get ( j ) )!=null?map.get ( collect.get ( j ) ).toString ():"");
            }

        }



        //输出工作簿
        String filename=new String("【实例】信息表.xlsx".getBytes (),"ISO8859-1");
        response.setContentType ( "application/octet-stream;charset=ISO8859-1" );
        response.setHeader("Content-Disposition", "attachment;filename="+filename);

        xssfWorkbook.write ( response.getOutputStream () );


    }



}
