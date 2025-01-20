package kz.hustle.equeue.config;

import kz.hustle.equeue.entity.TTSSettings;
import kz.hustle.equeue.repository.TTSSettingsRepository;
import kz.hustle.equeue.service.tts.GoogleTTSProvider;
import kz.hustle.equeue.service.tts.MaryTTSProvider;
import kz.hustle.equeue.service.tts.TTSProvider;
import marytts.exceptions.MaryConfigurationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.Locale;

@Configuration
public class TTSConfig {

    @Bean
    @Scope("singleton")
    public TTSProvider tts(TTSSettingsRepository repository) throws IOException {
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
                if (settings.getLanguage().equalsIgnoreCase("en") || settings.getLanguage().equalsIgnoreCase("en_us")) {
                    maryTtsProvider.setLocale(Locale.US);
                } else if (settings.getLanguage().equalsIgnoreCase("en_gb")) {
                    maryTtsProvider.setLocale(Locale.UK);
                } else {
                    maryTtsProvider.setLocale(new Locale(settings.getLanguage()));
                }
                maryTtsProvider.setVoiceName(settings.getVoiceName());

                return maryTtsProvider;

            case "GoogleTTS":
                GoogleTTSProvider googleTtsProvider = new GoogleTTSProvider();
                googleTtsProvider.setVoiceName(settings.getVoiceName());
                googleTtsProvider.setLocale(new Locale(settings.getLanguage()));
                return googleTtsProvider;

            default:
                throw new IllegalArgumentException("Unsupported TTS provider: " + settings.getProvider());
        }
    }
}
