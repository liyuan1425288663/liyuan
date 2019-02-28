package com.zbf.core.utils;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 作者：LCG
 * 创建时间：2019/2/18 14:27
 * 描述：用来处理一些上传下载的工具类
 */
public class FileUploadDownUtils {

    /**
     * 根据资源路径获取文件资料
     * @param path  注意传入的路径不需要 / 开头
     * @return
     */

    public static File getExcelTemplate(String path) throws FileNotFoundException {
        File file = ResourceUtils.getFile ( "classpath:" + path );
        return file;
    }
    /**
     * 写出文件到客户端
     * @param response
     * @param file
     * @param filename
     */
    public static void responseFileBuilder(HttpServletResponse response,File file,String filename){
        FileInputStream inputStream=null;
        ByteArrayOutputStream bos=null;
        ServletOutputStream outputStream=null;
        try{
            inputStream=new FileInputStream ( file );
            //处理文件名
            filename=new String(filename.getBytes ("utf-8"),"ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment;fileName="+ filename);
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("UTF-8");
            bos=new ByteArrayOutputStream (  );
            byte[] buffer=new byte[1024*4];
            int n=0;
            while ( (n=inputStream.read(buffer)) !=-1) {
                bos.write(buffer,0,n);
            }
            outputStream = response.getOutputStream ();
            outputStream.write (bos.toByteArray ());
        }catch (Exception e){
            e.printStackTrace ();
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
            if(bos!=null){
                try {
                    bos.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
            if(inputStream!=null){
                try {
                    inputStream.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }

    }

    /**
     * excel表格数据导入
     * @param file
     */
    public static void excelTemplateImport(MultipartFile file){



    }


}
