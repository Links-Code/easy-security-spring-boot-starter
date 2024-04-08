package com.cloud.exceptions;

import java.io.Serializable;

/**
 * 用户信息过期异常
 */
public class UserInfoOverTimeException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 3543866002979328047L;

    public UserInfoOverTimeException(String message) {
        super(message);
    }
}
