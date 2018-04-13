package com.faker.netty.model;

/**
 * Created by faker on 18/4/13.
 */
public class MoreResult extends Result {
    private static final long serialVersionUID = 5042561154219570702L;
    private String extraName;

    public String getExtraName() {
        return extraName;
    }

    public void setExtraName(String extraName) {
        this.extraName = extraName;
    }
}
