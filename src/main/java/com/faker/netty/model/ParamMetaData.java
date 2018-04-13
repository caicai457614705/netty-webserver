package com.faker.netty.model;

/**
 * Created by faker on 18/4/12.
 */
public class ParamMetaData {
    private String name;
    private int index;
    private Class type;

    public ParamMetaData(String name, int index, Class clz) {
        this.name = name;
        this.index = index;
        this.type = clz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
