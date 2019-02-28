package com.zbf.enmu;

import lombok.Data;

/**
 * 作者：LCG
 * 创建时间：2019/2/15 15:04
 * 描述：
 */
public enum  SexEnmu {

    MAN("男",1),WOMEN("女",0);

    private String value;

    private Integer code;

    private SexEnmu(String value,Integer code){
        this.code=code;
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
