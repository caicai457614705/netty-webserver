package com.faker.netty.exceptions;

/**
 * Created by faker on 18/4/17.
 */
public class InitializationException extends RuntimeException {
    private static final long serialVersionUID = -3061178910241257531L;

    public InitializationException(String msg) {
        super(msg);
    }

    public InitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
