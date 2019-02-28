package com.zbf.entity;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

@Data
public class Shiti implements Serializable {


    @Field
    private String id;
    @Field
    private String nandu;
    @Field
    private String tixingname;
    @Field
    private String createuserid;
    @Field
    private String tigan;
    @Field
    private String shitizhuangtai;
    @Field
    private String createname;
    @Field
    private String tikuname;
    @Field
    private String daan;
    @Field
    private String xuanxiangbianhao;
    @Field
    private String xuanxiang;
    @Field
    private String createtime;
    @Field
    private  String tixingid;

}
