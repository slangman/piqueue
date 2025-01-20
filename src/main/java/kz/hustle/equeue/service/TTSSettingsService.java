package kz.hustle.equeue.service;

import kz.hustle.equeue.entity.Language;
import kz.hustle.equeue.entity.TTSSettings;
import kz.hustle.equeue.repository.TTSSettingsRepository;
import kz.hustle.equeue.service.tts.GoogleTTSProvider;
import kz.hustle.equeue.service.tts.MaryTTSProvider;
import kz.hustle.equeue.service.tts.TTSProvider;
import marytts.exceptions.MaryConfigurationException;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@DependsOn("dataInitializer")
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
    public void updateSettings(TTSSettings newSettings) {
        TTSSettings settings = getSettings();
        if (settings == null) {
            settings = new TTSSettings();
        }
        if (newSettings.getProvider() != null) {
            settings.setProvider(newSettings.getProvider());
        }
        if (newSettings.getVoiceName() != null) {
            settings.setVoiceName(newSettings.getVoiceName());
        }
        if (newSettings.getLanguage() != null) {
            settings.setLanguage(newSettings.getLanguage());
        }
        settings.setLastUpdated(LocalDateTime.now());
        settingsRepository.save(settings);
    }


    public List<String> getVoices(String provider, String language) throws MaryConfigurationException, IOException {
        TTSProvider ttsProvider = null;
        switch (provider) {
            case "MaryTTS":
                ttsProvider = new MaryTTSProvider();
                return ttsProvider.getAvailableVoices(language);
            case "GoogleTTS":
                ttsProvider = new GoogleTTSProvider();
                return new ArrayList<>(ttsProvider.getAvailableVoices(new Locale(language)));
            default:
                throw new IllegalArgumentException("Unsupported TTS provider: " + provider);
        }
    }

}
