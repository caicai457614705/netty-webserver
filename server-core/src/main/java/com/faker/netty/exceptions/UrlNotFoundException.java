package com.faker.netty.exceptions;

/**
 * Created by faker on 18/4/16.
 */
public class UrlNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 4837911568273015507L;

    public UrlNotFoundException(String url, String method) {
        super(String.format("url not found , [url] : %s , [method] : %s", url, method));
    }

    public UrlNotFoundException(String url, String method, Throwable cause) {
        super(String.format("url not found , [url] : %s , [method] : %s", url, method), cause);
    }


}
