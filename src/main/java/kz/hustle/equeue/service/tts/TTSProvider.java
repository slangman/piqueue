package kz.hustle.equeue.service.tts;

import kz.hustle.equeue.entity.Language;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public interface TTSProvider {
    void setLocale(Locale locale);

    void setVoiceName(String voiceName);

    List<Language> getAvailableLanguages();

    Set<String> getAvailableVoices();

    Set<String> getAvailableVoices(Locale locale);

    List<String> getAvailableVoices(String language);

    void generateAndPlayAudio(String text);
}
