package com.icms.shared.exceptions;

public class BusinesRuleException extends BaseException {

    public BusinesRuleException(String code) {
        super(code);
    }

    public BusinesRuleException(String code,String message) {
        super(code, message);
    }

}
