package com.icms.shared.Utils;
import org.springframework.stereotype.Component;

@Component
public class MessageResolver {

    public static String resolveMessage(String code) {
        switch (code) {
            case "000": // Unexpected error
                return "An unexpected error occurred. Please try again later.";
            
            // Add more general exception codes here as needed
            case "Ent-001": // Entity not found
                return "Entity not found.";
            case "Ent-002": // Entity already exists
                return "Entity already exists.";
            case "Ent-003": // Entity must have an id
                return "Entity must have an id.";
            case "Ent-004": // Entity must be new
                return "Entity must be new.";
            case "Ent-005": // Entity cannot be deleted
                return "Entity cannot be deleted.";
            case "Ent-006": // Entity cannot be updated
                return "Entity cannot be updated.";
            case "Ent-007": // Entity cannot be created
                return "Entity cannot be created.";
            case "Ent-008": // Entity must be new
                return "Entity must be new.";
            

            //Language-related exceptions
            case "Lan-001": // Language not supported
                return "The specified language is not supported.";
            case "Lan-002": // Language file missing
                return "The language file is missing.";
            case "Lan-003": // Language parsing error
                return "There was an error parsing the language file.";
            case "Lan-004": // Language translation missing
                return "The translation for the specified language is missing.";
            case "Lan-005": // Language configuration error
                return "There was an error in the language configuration.";
            

            // Add more business rule exception codes here as needed

            default:
                return "Unknown error";
        }
    }

}
