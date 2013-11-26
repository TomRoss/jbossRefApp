package org.app.minibank.minibankref;

public class AccountException extends Exception {
    static final long serialVersionUID = -528980424823441536L;

    /**
     * @param message
     */
    public AccountException(String message) {
        super(message);

    }

    /**
     * @param message
     * @param cause
     */
    public AccountException(String message, Throwable cause) {
        super(message, cause);

    }

}
