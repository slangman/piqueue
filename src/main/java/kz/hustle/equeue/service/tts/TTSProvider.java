package kz.hustle.equeue.service.tts;

import java.util.Locale;
import java.util.Set;

public interface TTSProvider {
    void setLocale(Locale locale);

    void setVoiceName(String voiceName);

    Set<String> getAvailableLocales();

    Set<String> getAvailableVoices();

    void generateAndPlayAudio(String text);
}
