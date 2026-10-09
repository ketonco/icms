package com.icms.shared.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.icms.shared.Utils.MessageResolver;

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

    /* 
     * Utility methods to create standardized REST responses.
     * success: Generic success response with custom status.
     * ok: 200 OK response.
     * created: 201 Created response.
     * error: Error response with detailed information.
     */
    public static <T> RestResponse<T> success(T data, String message, int status) {
        RestResponse<T> response = new RestResponse<>();
        // TODO (pending P-23)
        response.setStatus(status);
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        response.setData(data);
        return response;
    }
    
    // 200 OK response utility method
    public static <T> RestResponse<T> ok(T data, String message) {
        return success(data, message, 200);
    }

    
    public static <T> RestResponse<T> ok(T data) {
        return ok(data, MessageResolver.resolveMessage("G-001"));
    }
    
    public static <T> RestResponse<T> ok() {
        return ok(null, MessageResolver.resolveMessage("G-001"));
    }

    // 201 Created response utility method
    public static <T> RestResponse<T> created(T data, String messageCode) {
        return success(data, MessageResolver.resolveMessage(messageCode), 201);
    }

    public static <T> RestResponse<T> created() {
        return created(null, "S-001");
    }

    // is ok but not content (used for responses where the request was successful but there's no content to return)
    public static <T> RestResponse<T> noContent() {
        return success(null, MessageResolver.resolveMessage("G-002"), 204);
    }

    // Error response utility method with detailed information
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
