package com.icms.user_auth.cli.level2;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.icms.user_auth.cli.DataSeed;
import com.icms.user_auth.entity.UserStatusTranslation;
import com.icms.user_auth.service.daoservice.UserStatusTranslationService;
import com.icms.user_auth.service.daoservice.UserStatusService;
import com.icms.user_auth.service.daoservice.LanguageService;

@Component
@Profile ("task")
public class UserStatusTranslationDataSeed implements DataSeed{

    private UserStatusTranslationService userStatusTranslationService;

    private UserStatusService userStatusService;

    private LanguageService languageService;

    public UserStatusTranslationDataSeed(UserStatusTranslationService userStatusTranslationService,
                                         UserStatusService userStatusService,
                                         LanguageService languageService) {
        this.userStatusTranslationService = userStatusTranslationService;
        this.userStatusService = userStatusService;
        this.languageService = languageService;
    }

    List<UserStatusTranslation> userStatusTranslationList = new ArrayList<>();

    @Override 
    public int getOrder() {
        return 2;
    }

    @Override 
    public String getName() {
        return "UserStatusTranslationDataSeed";
    }

    @Override
    public void run() {
        fillUserStatusTranslationList();
        userStatusTranslationList.forEach(this::save);
    }

    public void save(UserStatusTranslation userStatusTranslation) {
        UserStatusTranslation existing = userStatusTranslationService.findByCatalogAndLanguage(userStatusTranslation.getCatalog(), userStatusTranslation.getLanguage());
        if (existing != null) {
            existing.setTranslation(userStatusTranslation.getTranslation());
            userStatusTranslationService.save(existing);
        } else {
            userStatusTranslationService.save(userStatusTranslation);
        }
    }

    public void fillUserStatusTranslationList() {
        UserStatusTranslation userStatusTranslation1 = new UserStatusTranslation();
        // Active
        userStatusTranslation1.setTranslation("Active");
        userStatusTranslation1.setCatalog(userStatusService.findByCode("ACT"));
        userStatusTranslation1.setLanguage(languageService.findByCode("en-US"));
        userStatusTranslationList.add(userStatusTranslation1);

        // Activo
        UserStatusTranslation userStatusTranslation2 = new UserStatusTranslation();
        userStatusTranslation2.setTranslation("Activo");
        userStatusTranslation2.setCatalog(userStatusService.findByCode("ACT"));
        userStatusTranslation2.setLanguage(languageService.findByCode("es-ES"));
        userStatusTranslationList.add(userStatusTranslation2);

        // Inactive
        UserStatusTranslation userStatusTranslation3 = new UserStatusTranslation();
        userStatusTranslation3.setTranslation("Inactive");
        userStatusTranslation3.setCatalog(userStatusService.findByCode("INA"));
        userStatusTranslation3.setLanguage(languageService.findByCode("en-US"));
        userStatusTranslationList.add(userStatusTranslation3);

        // Inactivo
        UserStatusTranslation userStatusTranslation4 = new UserStatusTranslation();
        userStatusTranslation4.setTranslation("Inactivo");
        userStatusTranslation4.setCatalog(userStatusService.findByCode("INA"));
        userStatusTranslation4.setLanguage(languageService.findByCode("es-ES"));
        userStatusTranslationList.add(userStatusTranslation4);

        // Suspended
        UserStatusTranslation userStatusTranslation5 = new UserStatusTranslation();
        userStatusTranslation5.setTranslation("Suspended");
        userStatusTranslation5.setCatalog(userStatusService.findByCode("SUS"));
        userStatusTranslation5.setLanguage(languageService.findByCode("en-US"));
        userStatusTranslationList.add(userStatusTranslation5);

        // Suspendido
        UserStatusTranslation userStatusTranslation6 = new UserStatusTranslation();
        userStatusTranslation6.setTranslation("Suspendido");
        userStatusTranslation6.setCatalog(userStatusService.findByCode("SUS"));
        userStatusTranslation6.setLanguage(languageService.findByCode("es-ES"));
        userStatusTranslationList.add(userStatusTranslation6);
    }

}
