package com.icms.shared.exceptions;

public class EntityNotFoundException extends BaseException {
    public EntityNotFoundException() {
        super("001");
    }

    public EntityNotFoundException(String message) {
        super("001", message);
    }
}
