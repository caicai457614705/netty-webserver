package com.faker.netty.exceptions;

/**
 * Created by faker on 18/4/16.
 */
public class IllegalRequestException extends RuntimeException {
    private static final long serialVersionUID = -734912569499111418L;

    public IllegalRequestException(String msg) {
        super(msg);
    }

    public IllegalRequestException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
