package com.cloud.exceptions;

import java.io.Serializable;

/**
 * 未授权异常
 */
public class UnPermissionException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 6165890448184248121L;

    public UnPermissionException(String message) {
        super(message);
    }
}
