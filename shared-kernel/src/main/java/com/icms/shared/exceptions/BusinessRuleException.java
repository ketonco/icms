package com.icms.shared.exceptions;

public class BusinessRuleException extends BaseException {

    public BusinessRuleException(String code) {
        super(code);
    }

    public BusinessRuleException(String code,String message) {
        super(code, message);
    }

}
