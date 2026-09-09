package com.icms.shared.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

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
    private T data; // Genérico para devolver cualquier dato extra  

    public static <T> RestResponse<T> ok(T data, String message) {
        RestResponse<T> response = new RestResponse<>();
        response.setStatus(200);
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        response.setData(data);
        return response;
    }
    
    public static <T> RestResponse<T> ok(T data) {
        return ok(data, "OK");
    }

    public static <T> RestResponse<T> error(int status, String message) {
        RestResponse<T> response = new RestResponse<>();
        response.setStatus(status);
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
}
