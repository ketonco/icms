package com.icms.shared.Utils;
import org.springframework.stereotype.Component;

@Component
public class MessageResolver {

    public static String resolveMessage(String code) {
        switch (code) {
            case "000": // Unexpected error
                return "An unexpected error occurred. Please try again later.";
            case "001": // Entity not found
                return "Entity not found.";

            //Business rule exceptions

            // Add more business rule exception codes here as needed

            default:
                return "Unknown error";
        }
    }

}
