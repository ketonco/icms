package com.icms.shared.rules;

import com.icms.shared.entity.BaseCatalogEntity;
import com.icms.shared.entity.BaseCatalogTranslationEntity;
import com.icms.shared.exceptions.BusinessRuleException;
import com.icms.shared.repository.BaseCatalogTranslationRepository;

public class BaseDaoCatalogTranslationRules<E extends BaseCatalogTranslationEntity<C>, R extends BaseCatalogTranslationRepository<E, C>, C extends BaseCatalogEntity>
        extends BaseDaoRules<E, R, Long> {

        public BaseDaoCatalogTranslationRules(R repository) {
            super(repository);  
        }

        @Override
        public void canSave(E translationEntity) {
            super.canSave(translationEntity);
            noRepeatedCatalogTranslation(translationEntity);
        }
        
        @Override
        public void canUpdate(E translationEntity) {
            super.canUpdate(translationEntity);
            noRepeatedCatalogTranslation(translationEntity); 
        }

        @Override
        public void canDelete(E entity) {
            super.canDelete(entity);
            atLeastTwoTranslations(entity.getCatalog());
        }

        public void noRepeatedCatalogTranslation(E translationEntity) {
            E existingTranslation = repository.findByCatalogAndLanguage(translationEntity.getCatalog(), translationEntity.getLanguage()).orElse(null);
            if (existingTranslation != null && existingTranslation.getId() != translationEntity.getId()) {
                throw new BusinessRuleException("Lan-007"); 
            }
        }

        /**
         * Ensures that the given catalog entity has at least two translations before one can be deleted.
         * @param entity the catalog entity to check for translations
         */
        public void atLeastTwoTranslations(C catalog) {
            int count = repository.countByCatalog(catalog);
            if (count < 2) {
                throw new BusinessRuleException("Lan-006"); 
            }
        }

}
