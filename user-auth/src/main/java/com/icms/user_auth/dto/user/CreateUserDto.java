package com.icms.user_auth.dto.user;
import jakarta.validation.constraints.NotNull;
import com.icms.user_auth.dto.userprofile.UserProfileDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record CreateUserDto(

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") 
    String username,

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "invalid format")
    @Size(max = 100, message = "Email must be at most 100 characters")
    String email,

    @NotBlank(message = "Status cannot be blank")
    String status,

    @NotEmpty(message = "Roles cannot be empty")
    Set<String> types,

    @NotNull(message = "User profile cannot be null")
    @Valid 
    UserProfileDto profile
) {

}
