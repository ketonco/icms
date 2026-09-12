package com.icms.shared.config.jackson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

@AutoConfiguration 
public class SharedJacksonConfig {

    // Define el patrón exacto deseado
    private static final String DATETIME_FORMAT = "dd/MM/yyyy - HH:mm:ss";
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 1. Registrar serializador/deserializador sin depender de jackson-datatype-jsr310
        SimpleModule dateTimeModule = new SimpleModule();
        dateTimeModule.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator generator,
                    SerializerProvider serializers) throws java.io.IOException {
                generator.writeString(DATETIME_FORMATTER.format(value));
            }
        });
        dateTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser parser,
                    DeserializationContext context) throws java.io.IOException {
                return LocalDateTime.parse(parser.getText(), DATETIME_FORMATTER);
            }
        });

        // 2. Registrar el módulo en ObjectMapper
        mapper.registerModule(dateTimeModule);
        // 3. Desactivar la serialización como timestamps numéricos
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }
}
