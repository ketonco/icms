package com.icms.user_auth.service.daoservice;

import org.springframework.stereotype.Service;
import com.icms.user_auth.dto.language.LanguageDto;
import com.icms.user_auth.repository.LanguageRepository;
import com.icms.user_auth.rules.dao.LanguageRules;
import com.icms.shared.entity.Language;
import com.icms.shared.service.BaseCatalogService;
import com.icms.user_auth.dto.language.LanguageMapper;

@Service 
public class LanguageService extends BaseCatalogService<Language, LanguageDto, LanguageRepository> {

    public LanguageService(
        LanguageRepository repository,
        LanguageMapper mapper,
        LanguageRules rules) {
        super(repository, mapper, rules);
    }

}
