package com.faker.netty.exceptions;

/**
 * Created by faker on 18/4/16.
 */
public class IllegalUrlException extends RuntimeException {
    private static final long serialVersionUID = -8822762738624698730L;
    public IllegalUrlException(String msg) {
        super(msg);
    }

    public IllegalUrlException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
