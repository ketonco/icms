package com.icms.user_auth.repository;
import org.springframework.stereotype.Repository;
import com.icms.shared.repository.BaseRepository;
import com.icms.shared.entity.Language;

@Repository
public interface LanguageRepository extends BaseRepository<Language, Long> {

}
