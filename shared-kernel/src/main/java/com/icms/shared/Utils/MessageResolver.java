package com.icms.shared.Utils;
import org.springframework.stereotype.Component;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@Component
public class MessageResolver {

    private static MessageSource messageSource;

    public MessageResolver(MessageSource messageSource) {
        MessageResolver.messageSource = messageSource;
    }

    public static String resolveMessage(String code) {
        return resolveMessage(code, (Object[]) null);
    }

    public static String resolveMessage(String code, Object... args) {
        if (messageSource == null) {
            return code + " context"; // Fallback si el contexto de Spring aún no se ha cargado (ej. pruebas unitarias aisladas)
        }
        // LocaleContextHolder toma automáticamente el header Accept-Language enviado por el cliente
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

}
