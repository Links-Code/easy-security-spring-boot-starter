package com.cloud.exceptions;

import java.io.Serializable;

/**
 * 未登录异常
 */
public class UNLoginException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 9047156403347128327L;

    public UNLoginException(String message) {
        super(message);
    }
}
