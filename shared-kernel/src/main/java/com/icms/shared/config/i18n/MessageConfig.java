package com.icms.shared.config.i18n;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.context.MessageSource;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class MessageConfig {

    @Bean 
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        // root messages (default)
        messageSource.setBasename("i18n/messages");
        // set default encoding for message files
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        // use code as default message if not found instead of throwing an NoSuchMessageException
        messageSource.setUseCodeAsDefaultMessage(true);

        return messageSource;
    }

}
