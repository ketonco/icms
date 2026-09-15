package com.icms.user_auth.cli.level2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.icms.user_auth.cli.DataSeed;
import com.icms.user_auth.entity.UserTypeTranslation;
import com.icms.user_auth.service.daoservice.LanguageService;
import com.icms.user_auth.service.daoservice.UserTypeService;
import com.icms.user_auth.service.daoservice.UserTypeTranslationService;


@Component
@Profile ("task")
public class UserTypeTranslationDataSeed implements DataSeed{

    private UserTypeTranslationService userTypeTranslationService;

    private UserTypeService userTypeService;

    private LanguageService languageService;

    public UserTypeTranslationDataSeed(UserTypeTranslationService userTypeTranslationService,
                                       UserTypeService userTypeService,
                                       LanguageService languageService) {
        this.userTypeTranslationService = userTypeTranslationService;
        this.userTypeService = userTypeService;
        this.languageService = languageService;
    }

    private List<UserTypeTranslation> userTypeTranslationList = new ArrayList<>();

    @Override 
    public int getOrder() {
        return 2;
    }

    @Override 
    public String getName() {
        return "UserTypeTranslationDataSeed";
    }

    @Override
    public void run() {
        fillUserTypeTranslationList();
        userTypeTranslationList.forEach(this::save);
    }

    public void save(UserTypeTranslation userTypeTranslation) {
        UserTypeTranslation existing = userTypeTranslationService.findByCatalogAndLanguage(userTypeTranslation.getCatalog(), userTypeTranslation.getLanguage());
        if (existing != null) {
            existing.setTranslation(userTypeTranslation.getTranslation());
            userTypeTranslationService.update(existing);
        } else {
            userTypeTranslationService.save(userTypeTranslation);
        }
    }

    public void fillUserTypeTranslationList() {
        UserTypeTranslation userTypeTranslation1 = new UserTypeTranslation();
        // Admin
        userTypeTranslation1.setTranslation("Admin");
        userTypeTranslation1.setCatalog(userTypeService.findByCode("ADM"));
        userTypeTranslation1.setLanguage(languageService.findByCode("en-US"));
        userTypeTranslationList.add(userTypeTranslation1);

        // Admin (Spanish)
        UserTypeTranslation userTypeTranslation2 = new UserTypeTranslation();
        userTypeTranslation2.setTranslation("Admin");
        userTypeTranslation2.setCatalog(userTypeService.findByCode("ADM"));
        userTypeTranslation2.setLanguage(languageService.findByCode("es-ES"));
        userTypeTranslationList.add(userTypeTranslation2);

        // User
        UserTypeTranslation userTypeTranslation3 = new UserTypeTranslation();
        userTypeTranslation3.setTranslation("User");
        userTypeTranslation3.setCatalog(userTypeService.findByCode("USR"));
        userTypeTranslation3.setLanguage(languageService.findByCode("en-US"));
        userTypeTranslationList.add(userTypeTranslation3);

        // Usuario (Spanish)
        UserTypeTranslation userTypeTranslation4 = new UserTypeTranslation();
        userTypeTranslation4.setTranslation("Usuario");
        userTypeTranslation4.setCatalog(userTypeService.findByCode("USR"));
        userTypeTranslation4.setLanguage(languageService.findByCode("es-ES"));
        userTypeTranslationList.add(userTypeTranslation4);

        // Moderator
        UserTypeTranslation userTypeTranslation5 = new UserTypeTranslation();
        userTypeTranslation5.setTranslation("Moderator");
        userTypeTranslation5.setCatalog(userTypeService.findByCode("MOD"));
        userTypeTranslation5.setLanguage(languageService.findByCode("en-US"));
        userTypeTranslationList.add(userTypeTranslation5);

        // Moderador (Spanish)
        UserTypeTranslation userTypeTranslation6 = new UserTypeTranslation();
        userTypeTranslation6.setTranslation("Moderador");
        userTypeTranslation6.setCatalog(userTypeService.findByCode("MOD"));
        userTypeTranslation6.setLanguage(languageService.findByCode("es-ES"));
        userTypeTranslationList.add(userTypeTranslation6);

        // Guest
        UserTypeTranslation userTypeTranslation7 = new UserTypeTranslation();
        userTypeTranslation7.setTranslation("Guest");
        userTypeTranslation7.setCatalog(userTypeService.findByCode("GUE"));
        userTypeTranslation7.setLanguage(languageService.findByCode("en-US"));
        userTypeTranslationList.add(userTypeTranslation7);

        // Invitado (Spanish)
        UserTypeTranslation userTypeTranslation8 = new UserTypeTranslation();
        userTypeTranslation8.setTranslation("Invitado");
        userTypeTranslation8.setCatalog(userTypeService.findByCode("GUE"));
        userTypeTranslation8.setLanguage(languageService.findByCode("es-ES"));
        userTypeTranslationList.add(userTypeTranslation8);
    }

}
