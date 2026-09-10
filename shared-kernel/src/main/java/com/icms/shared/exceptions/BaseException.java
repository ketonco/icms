package com.icms.shared.exceptions;
import com.icms.shared.Utils.MessageResolver;

public abstract class BaseException extends RuntimeException {

    private final String code;

    public BaseException(String code) {
        super(MessageResolver.resolveMessage(code));
        this.code = code;
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return MessageResolver.resolveMessage(code);
    }

}
