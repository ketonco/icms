package com.icms.user_auth.service.daoservice;
import com.icms.user_auth.dto.userstatus.UserStatusTranslationDto;
import com.icms.user_auth.entity.UserStatus;
import com.icms.user_auth.repository.UserStatusRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.icms.shared.entity.Language;
import com.icms.shared.exceptions.EntityNotFoundException;
import com.icms.user_auth.dto.userstatus.UserStatusTranslationMapper;

import com.icms.shared.service.BaseCatalogTranslationService;
import com.icms.user_auth.entity.UserStatusTranslation;
import com.icms.user_auth.repository.UserStatusTranslationRepository;
import com.icms.user_auth.rules.UserStatusTranslationRules;

@Service 
public class UserStatusTranslationService extends BaseCatalogTranslationService<UserStatusTranslation, 
                                                                                UserStatusTranslationDto, 
                                                                                UserStatusTranslationRepository,
                                                                                UserStatus>{
    UserStatusTranslationRepository repository;
    UserStatusTranslationMapper mapper; 
    @Autowired
    UserStatusRepository userStatusRepository; 

    public UserStatusTranslationService(UserStatusTranslationRepository repository, UserStatusTranslationMapper mapper, UserStatusTranslationRules rules) {
        super(repository, mapper, rules);
        this.repository = repository;
        this.mapper = mapper;
    }

    public UserStatusTranslation findByCatalogAndLanguage(UserStatus catalog, Language language) {
        return repository.findByCatalogAndLanguage(catalog, language).orElse(null);
    }

    public UserStatusTranslationDto findByCatalogAndLanguageDto(UserStatus catalog, Language language) {
        return mapper.toDto(findByCatalogAndLanguage(catalog, language));
    }

    public List<UserStatusTranslationDto> findByCatalogId(Long catalogId) {
        UserStatus catalog = userStatusRepository.findById(catalogId).orElseThrow(() -> new EntityNotFoundException());
        return findByCatalog(catalog).stream().map(mapper::toDto).toList();
    }
}
