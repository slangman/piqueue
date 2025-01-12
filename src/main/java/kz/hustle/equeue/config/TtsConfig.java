package kz.hustle.equeue.config;

import kz.hustle.equeue.entity.Language;
import kz.hustle.equeue.entity.TTSSettings;
import kz.hustle.equeue.repository.TTSSettingsRepository;
import kz.hustle.equeue.service.tts.MaryTTSProvider;
import kz.hustle.equeue.service.tts.TTSProvider;
import marytts.exceptions.MaryConfigurationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class TtsConfig {
    @Bean
    public TTSProvider tts(TTSSettingsRepository repository) {
        TTSSettings settings = repository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("TTS settings not found in the database"));

        switch (settings.getProvider()) {
            case "MaryTTS":
                MaryTTSProvider maryTtsProvider = null;
                try {
                    maryTtsProvider = new MaryTTSProvider();
                } catch (MaryConfigurationException e) {
                    throw new RuntimeException(e);
                }
                if (settings.getLanguage().equals(Language.EN)) {
                    maryTtsProvider.setLocale(Locale.US);
                } else {
                    maryTtsProvider.setLocale(new Locale(settings.getLanguage().name()));
                }
                maryTtsProvider.setVoiceName(settings.getVoiceName());

                return maryTtsProvider;
            /*
            case "GoogleTTS":
                GoogleTtsProvider googleTtsProvider = new GoogleTtsProvider();
                googleTtsProvider.setVoice(settings.getVoice());
                googleTtsProvider.setLanguage(settings.getLanguage());
                return googleTtsProvider;
             */
            default:
                throw new IllegalArgumentException("Unsupported TTS provider: " + settings.getProvider());
        }
    }
}
