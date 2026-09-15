package com.icms.user_auth.service.daoservice;
import org.springframework.stereotype.Service;
import com.icms.user_auth.dto.usertype.UserTypeMapper;
import com.icms.user_auth.rules.dao.UserTypeRules;
import com.icms.user_auth.entity.UserType;
import com.icms.user_auth.repository.UserTypeRepository;
import com.icms.shared.service.BaseCatalogService;
import com.icms.user_auth.dto.usertype.UserTypeDto;

@Service 
public class UserTypeService extends BaseCatalogService<UserType, UserTypeDto, UserTypeRepository>{

    public UserTypeService(UserTypeRepository repository, UserTypeMapper mapper, UserTypeRules rules) {
        super(repository, mapper, rules);
    }

}
