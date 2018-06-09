package com.devbadger.security.exception;

public class SecurityAuthorizationException extends Exception {

    public SecurityAuthorizationException() {
        super();
    }

    public SecurityAuthorizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SecurityAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityAuthorizationException(String message) {
        super(message);
    }

    public SecurityAuthorizationException(Throwable cause) {
        super(cause);
    }
}
