package com.cloud.exceptions;

import java.io.Serializable;

/**
 * 未登录异常
 */
public class UnLoginException  extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 9047156403347128327L;

    public UnLoginException(String message) {
        super(message);
    }
}
