package com.cloud.exceptions;

import java.io.Serializable;

/**
 * 未授权异常
 */
public class UNPermissionException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 6165890448184248121L;

    public UNPermissionException(String message) {
        super(message);
    }
}
