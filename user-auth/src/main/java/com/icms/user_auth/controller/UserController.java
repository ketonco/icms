package com.icms.user_auth.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.icms.shared.dto.RestResponse;
import com.icms.user_auth.dto.user.CreateUserDto;
import com.icms.user_auth.service.daoservice.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1/auth/user")
public class UserController {

    @Autowired 
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RestResponse<CreateUserDto>> createUser(@RequestBody CreateUserDto createUserDto) {
        CreateUserDto createdUser = userService.createUserDto(createUserDto);
        return ResponseEntity.ok(RestResponse.ok(createdUser));
    }

}
