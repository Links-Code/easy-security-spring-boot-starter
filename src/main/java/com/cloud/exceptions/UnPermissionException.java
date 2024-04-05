package com.cloud.exceptions;

import java.io.Serializable;

public class UnPermissionException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 6165890448184248121L;

    public UnPermissionException(String message) {
        super(message);
    }
}
