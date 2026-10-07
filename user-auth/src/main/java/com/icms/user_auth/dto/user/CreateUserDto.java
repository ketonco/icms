package com.icms.user_auth.dto.user;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import com.icms.user_auth.dto.userprofile.UserProfileDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(

    @NotBlank(message = "{usr.username.blank}")
    @Size(min = 3, max = 50, message = "{usr.username.size}")
    String username,

    @NotBlank(message = "{usr.password.blank}")
    @Size(min = 6, max = 100, message = "{usr.password.size}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*\\-._])(?=.*[a-zA-Z])(?=.*[A-Z]).+$", message = "{usr.password.pattern}")
    String password,

    @NotBlank(message = "{usr.email.blank}")
    @Email(message = "{usr.email.format}")
    @Size(max = 100, message = "{usr.email.size}")
    String email,

    @NotNull(message = "{usr.profile.null}")
    @Valid 
    UserProfileDto profile
) {

}
