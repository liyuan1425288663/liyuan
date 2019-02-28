package com.zbf.core.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
/**
 * 分页类
 * @author lcg
 * @date 2017-7-26
 * @param <T>
 */
public class Page<T> {
    private static final int PAGE_FETCH_NUMBER =1;		// 每次读取的页数
    private int totalPage;
    private int totalCount;
    private int pageSize=10;
    private int pageNo=1;
    private int prePage=1;
    private int nextPage=1;
    private String url;

    private List<T> resultList=new ArrayList<T>();
    private Map<String, Object> params = new HashMap<String, Object>();//其他的参数我们把它分装成一个Map对象


    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getPrePage() {
        if((this.pageNo-1)==0){
            this.prePage=1;
        }else{
            this.prePage=this.pageNo-1;
        }
        return prePage;
    }
    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }
    public int getNextPage() {
        if((this.pageNo+1) > this.getTotalPage()){
            this.nextPage=this.getTotalPage();
        }else{
            this.nextPage=this.pageNo+1;
        }
        return nextPage;
    }
    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
    public Map<String, Object> getParams() {
        return params;
    }
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    public int getTotalPage() {

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
    public int getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPageNo() {
        return pageNo;
    }
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
    public List<T> getResultList() {
        return resultList;
    }
    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }
    /**
     * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
     */
    public int getFirst() {
        return ((pageNo - 1) * pageSize) + 1;
    }

    public RowBounds getRowBounds() {
        return new RowBounds(getFirst() - 1, getPageSize() * PAGE_FETCH_NUMBER);
    }

    /**
     * 设置page参数
     */
    public static void setPageInfo(Page page,Map<String,Object> mapp){
        if(mapp.get ( "pageNo" )!=null){
            page.setPageNo ( Integer.valueOf ( mapp.get ( "pageNo" ).toString () ) );
        }
        if(mapp.get ( "pageSize" )!=null){
            page.setPageSize ( Integer.valueOf ( mapp.get ( "pageSize" ).toString () ) );
        }
        page.setParams ( mapp );
    }

}
