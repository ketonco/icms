package com.icms.user_auth.service.daoservice;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.icms.user_auth.repository.UserRepository;
import com.icms.user_auth.repository.UserStatusRepository;
import com.icms.user_auth.repository.UserTypeRepository;
import com.icms.shared.exceptions.BusinessRuleException;
import com.icms.user_auth.dto.user.CreateUserDto;
import com.icms.user_auth.dto.user.UserMapper;
import com.icms.user_auth.entity.User;
import com.icms.user_auth.entity.UserType;
import com.icms.user_auth.rules.dao.UserRules;

import jakarta.transaction.Transactional;

@Service 
public class UserService {

    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;
    private UserTypeRepository userTypeRepository;
    private UserMapper userMapper;
    private UserRules userRules;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserStatusRepository userStatusRepository, UserTypeRepository userTypeRepository, UserMapper userMapper, UserRules userRules, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userRules = userRules;
        this.userStatusRepository = userStatusRepository;
        this.userTypeRepository = userTypeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional 
    public User createUser(User user) {
        userRules.canCreate(user);
        return userRepository.save(user);
    }

    public CreateUserDto createUserDto(CreateUserDto userDto) {
        User entity = userMapper.toEntity(userDto);
        Set<UserType> roles = new HashSet<>(List.of(userTypeRepository.findByCode("USR").orElseThrow(() -> new BusinessRuleException("Cat-001")))); 

        entity.setPassword(passwordEncoder.encode(userDto.password()));
        entity.setTypes(roles); // Set default roles
        entity.setStatus(userStatusRepository.findByCode("PEN").orElseThrow(() -> new BusinessRuleException("Cat-001"))); // Set default status

        User savedUser = createUser(entity);
        savedUser.setPassword(null); // Clear the password before returning the DTO
        return userMapper.toCreateUserDto(savedUser);
    }



}
