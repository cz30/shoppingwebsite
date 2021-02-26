package com.bjf.service.ex;


public class ExpireException extends ServiceException {


    private static final long serialVersionUID = -9089374824732551868L;

    public ExpireException() {
    }

    public ExpireException(String message) {
        super(message);
    }

    public ExpireException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpireException(Throwable cause) {
        super(cause);
    }

    public ExpireException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}


