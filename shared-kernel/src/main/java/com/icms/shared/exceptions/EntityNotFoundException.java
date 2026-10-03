package com.icms.shared.exceptions;

public class EntityNotFoundException extends BaseException {
    public EntityNotFoundException() {
        super("Ent-001");
    }

    public EntityNotFoundException(String code) {
        super(code);
    }
}
