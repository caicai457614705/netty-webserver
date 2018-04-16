package com.faker.netty.model;

import java.io.Serializable;

/**
 * Created by faker on 18/4/12.
 */
public class Result implements Serializable {

    private static final long serialVersionUID = -4433035944344133720L;
    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
