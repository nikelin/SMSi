package ru.nikita.platform.sms.processors;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 11:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessingException extends Exception {

    public ProcessingException() {
        this(null);
    }

    public ProcessingException(String message) {
        this(message, null);
    }

    public ProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
