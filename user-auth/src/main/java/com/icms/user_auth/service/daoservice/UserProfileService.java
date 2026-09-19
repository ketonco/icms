package com.icms.user_auth.service.daoservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import com.icms.user_auth.entity.User;
import com.icms.shared.service.BaseService;
import com.icms.user_auth.rules.dao.UserProfileRules;

import jakarta.transaction.Transactional;

import com.icms.user_auth.dto.userprofile.UserProfileDto;
import com.icms.user_auth.dto.userprofile.UserProfileMapper;
import com.icms.user_auth.entity.UserProfile;
import com.icms.user_auth.repository.UserProfileRepository;
import com.icms.user_auth.repository.UserRepository;

import java.util.UUID;

@Service 
public class UserProfileService extends BaseService<UserProfile, UserProfileDto, UUID, UserProfileRepository> {

    private UserProfileRepository repository;

    @Autowired 
    private UserRepository userRepository;

    private UserProfileMapper mapper;
    private UserProfileRules rules;

    public UserProfileService(UserProfileRepository repository, UserProfileMapper mapper, UserProfileRules rules) {
        super(repository, mapper, rules);
        this.repository = repository;
        this.mapper = mapper;
        this.rules = rules;
    }

    @Override 
    @Transactional 
    public UserProfileDto save(UserProfileDto dto) {
        UserProfile entity = toEntity(dto);
        rules.canSave(entity);
        entity = repository.save(entity);
        return getMapper().toDto(entity);
    }

    @Override
    @Transactional
    public UserProfileDto update(UserProfileDto dto) {
        UserProfile entity = toEntity(dto);
        rules.canUpdate(entity);
        entity = repository.save(entity);
        return getMapper().toDto(entity);
    }

    private UserProfile toEntity(UserProfileDto dto) {
        User user = userRepository.findByEmail(dto.email()).orElseThrow(() -> new EntityNotFoundException("Usr-009"));
        UserProfile entity = repository.findByUser(user).orElseThrow(() -> new EntityNotFoundException("UsrProf-001"));
        mapper.updateEntityFromDto(dto, entity);
        entity.setUser(user);
        return entity;
    }

    public UserProfile findByUser(User user) { 
        return repository.findByUser(user).orElse(null);
    }

    public UserProfileDto findDtoByUser(User user) {
        UserProfile profile = findByUser(user);
        return profile != null ? getMapper().toDto(profile) : null;
    }

}
