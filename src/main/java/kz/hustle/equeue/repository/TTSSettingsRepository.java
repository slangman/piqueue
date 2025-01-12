package kz.hustle.equeue.repository;

import kz.hustle.equeue.entity.TTSSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TTSSettingsRepository extends JpaRepository<TTSSettings, Long> {
    Optional<TTSSettings> findFirstByOrderByIdAsc();
}
