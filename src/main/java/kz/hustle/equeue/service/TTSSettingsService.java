package kz.hustle.equeue.service;

import kz.hustle.equeue.entity.Language;
import kz.hustle.equeue.entity.TTSSettings;
import kz.hustle.equeue.repository.TTSSettingsRepository;
import kz.hustle.equeue.service.tts.TTSProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TTSSettingsService {
    private final TTSSettingsRepository settingsRepository;
    private final TTSProvider tts;

    public TTSSettingsService(TTSSettingsRepository settingsRepository, TTSProvider tts) {
        this.settingsRepository = settingsRepository;
        this.tts = tts;
    }

    public TTSSettings getSettings() {
        return settingsRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("TTS settings not found in the database")); // Assuming a single record for simplicity
    }

    @Transactional
    public void updateSettings(String voice, Language language) {
        TTSSettings settings = getSettings();
        if (settings == null) {
            settings = new TTSSettings();
        }
        if (voice != null) {
            settings.setVoiceName(voice);
            tts.setVoiceName(voice);
        }
        if (language != null) {
        settings.setLanguage(language);
        }
        settings.setLastUpdated(LocalDateTime.now());
        settingsRepository.save(settings);
    }

    public List<String> getVoices() {
        return new ArrayList<>(tts.getAvailableVoices());
    }
}
