package org.simon.autoet.exception;

/**
 * 定义项目异常
 * @author simon
 * @since 2017/10/28 12:34
 * @version V1.0
 */
public class AutoRuntimeException extends RuntimeException {
    public AutoRuntimeException(String message) {
        super(message);
    }

    public AutoRuntimeException(Throwable cause) {
        super(cause);
    }

    public AutoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
