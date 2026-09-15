package com.icms.user_auth.service.daoservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.icms.user_auth.entity.UserTypeTranslation;
import com.icms.shared.exceptions.EntityNotFoundException;
import com.icms.user_auth.entity.UserType;
import com.icms.user_auth.repository.UserTypeTranslationRepository;
import com.icms.user_auth.rules.dao.UserTypeTranslationRules;
import com.icms.user_auth.repository.UserTypeRepository;
import com.icms.shared.service.BaseCatalogTranslationService;
import java.util.List;
import com.icms.user_auth.dto.usertype.UserTypeTranslationDto;
import com.icms.user_auth.dto.usertype.UserTypeTranslationMapper;

@Service 
public class UserTypeTranslationService extends BaseCatalogTranslationService<UserTypeTranslation, UserTypeTranslationDto, UserTypeTranslationRepository, UserType> {

    UserTypeTranslationRepository repository;
    UserTypeTranslationMapper mapper; 

    @Autowired 
    UserTypeRepository userTypeRepository; 

    public UserTypeTranslationService(UserTypeTranslationRepository repository, UserTypeTranslationMapper mapper, UserTypeTranslationRules rules) {
        super(repository, mapper, rules);
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<UserTypeTranslationDto> findByCatalogId(Long catalogId) {
        UserType catalog = userTypeRepository.findById(catalogId).orElseThrow(() -> new EntityNotFoundException());
        return findByCatalog(catalog).stream().map(mapper::toDto).toList();
    }



}
