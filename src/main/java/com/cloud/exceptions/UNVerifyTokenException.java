package com.cloud.exceptions;

import java.io.Serializable;

public class UNVerifyTokenException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 8179898539698950697L;

    public UNVerifyTokenException(String message) {
        super(message);
    }
}
