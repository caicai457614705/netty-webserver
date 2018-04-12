package com.faker.netty.model;

import java.io.Serializable;

/**
 * Created by faker on 18/4/12.
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -6672495589813170600L;
    private Long vipNo;
    private String nickName;
    private String level;

    public Long getVipNo() {
        return vipNo;
    }

    public void setVipNo(Long vipNo) {
        this.vipNo = vipNo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
