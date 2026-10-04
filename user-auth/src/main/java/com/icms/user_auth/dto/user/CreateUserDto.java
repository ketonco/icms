package com.icms.user_auth.dto.user;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import com.icms.user_auth.dto.userprofile.UserProfileDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") 
    String username,

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-zA-Z])(?=.*[A-Z]).+$", message = "Password must include at least one number, one special character, one letter and one uppercase letter")
    String password,

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "invalid format")
    @Size(max = 100, message = "Email must be at most 100 characters")
    String email,

    @NotNull(message = "User profile cannot be null")
    @Valid 
    UserProfileDto profile
) {

}
