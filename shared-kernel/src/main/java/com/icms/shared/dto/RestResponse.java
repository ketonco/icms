package com.icms.shared.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from the JSON response
public class RestResponse<T> {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private String code; // Código de error o éxito
    private String error; // Mensaje de error detallado
    private T data; // Genérico para devolver cualquier dato extra  
    private Map<String, String> errors; // Map to hold field-specific error messages

    public static <T> RestResponse<T> ok(T data, String message) {
        RestResponse<T> response = new RestResponse<>();
        // TODO (pending P-23)
        response.setStatus(200);
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        response.setData(data);
        return response;
    }
    
    public static <T> RestResponse<T> ok(T data) {
        return ok(data, "OK");
    }

    public static <T> RestResponse<T> error(int status, String message, String code, String path, String error) {
        return error(status, message, code, path, error, null);
    }

    public static <T> RestResponse<T> error(int status, String message, String code, String path, String error, Map<String, String> errors) {
        RestResponse<T> response = new RestResponse<>();
        response.setStatus(status);
        response.setMessage(message);
        response.setCode(code);
        response.setTimestamp(LocalDateTime.now());
        response.setPath(path);
        response.setError(error); // Set the detailed error message
        response.setErrors(errors); // Set the field-specific error messages
        return response;
    }
}
