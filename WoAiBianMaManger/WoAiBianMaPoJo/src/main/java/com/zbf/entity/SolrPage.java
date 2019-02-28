package com.zbf.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SolrPage<T> {

    private int pageNo=1;//页码

    private int pageSize=10;//页面大小

    private long totalPage;//总页数

    private long totalCount;//总条数

    private List<T> resultList;//结果集用来存储每页的数据

    public long getTotalPage() {

        if((totalCount%pageSize)==0){
            totalPage=totalCount/pageSize;
        }else{
            totalPage=totalCount/pageSize+1;
        }

        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * 设置page参数
     */
    public static void setPageInfo(SolrPage page, Map<String,Object> mapp){

        if(mapp.get ( "pageNo" )!=null){
            page.setPageNo ( Integer.valueOf ( mapp.get ( "pageNo" ).toString () ) );
        }
        if(mapp.get ( "pageSize" )!=null){
            page.setPageSize ( Integer.valueOf ( mapp.get ( "pageSize" ).toString () ) );
        }

    }


}
