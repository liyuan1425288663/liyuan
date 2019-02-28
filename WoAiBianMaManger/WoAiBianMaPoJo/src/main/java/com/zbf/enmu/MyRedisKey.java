package com.zbf.enmu;

/**
 * 作者：LCG
 * 创建时间：2019/2/16 10:01
 * 描述：
 */
public enum  MyRedisKey {

    TIKU("tiku"),SHI_JUAN("shijuan");

    private String key;

    private MyRedisKey(String key){
        this.key=key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
