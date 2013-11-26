package org.app.minibank.minibankref;

public class SystemException extends RuntimeException {

    private static final long serialVersionUID = -1947933023991657937L;

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(String message) {
        super(message);
    }

}
