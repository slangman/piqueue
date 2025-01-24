package kz.hustle.equeue.service.tts;

import kz.hustle.equeue.entity.Language;
import marytts.LocalMaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class MaryTTSProvider implements TTSProvider {

    private LocalMaryInterface maryTTS;

    public MaryTTSProvider() throws MaryConfigurationException {
        maryTTS = new LocalMaryInterface();
    }

    @Override
    public String getName() {
        return "MaryTTS";
    }

    @Override
    public String getVoiceName() {
        return maryTTS.getVoice();
    }

    @Override
    public String getLanguageCode() {
        return maryTTS.getLocale().toLanguageTag();
    }

    @Override
    public void setLocale(Locale locale) {
        maryTTS.setLocale(locale);
    }

    @Override
    public void setVoiceName(String voiceName) {
        maryTTS.setVoice(voiceName);
    }

    @Override
    public List<Language> getAvailableLanguages() {
        List<Language> result = new ArrayList<>();
        result.add(new Language("en_US", "English (US)"));
        result.add(new Language("en_GB", "English (UK)"));
        result.sort(Comparator.comparing(Language::getDisplayName));
        return result;
    }

    public Set<String> getAvailableLocales() {
        return maryTTS.getAvailableLocales()
                .stream()
                .map(Locale::toString)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getAvailableVoices() {
        if (maryTTS.getLocale() != null) {
            return maryTTS.getAvailableVoices(maryTTS.getLocale());
        } else {
            return maryTTS.getAvailableVoices();
        }
    }

    @Override
    public Set<String> getAvailableVoices(Locale locale) {
        return maryTTS.getAvailableVoices(locale);
    }

    @Override
    public List<String> getAvailableVoices(String language) {
        switch (language) {
            case "en_GB":
                return new ArrayList<>(getAvailableVoices(Locale.UK));
            case "en_US":
                return new ArrayList<>(getAvailableVoices(Locale.US));
            default:
                return new ArrayList<>(getAvailableVoices(new Locale(language)));
        }
    }

    @Override
    public void generateAndPlayAudio(String text) {
        try {
            text = new String(text.getBytes("Windows-1251"), StandardCharsets.UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        marytts.util.data.audio.AudioPlayer audioPlayer = new AudioPlayer();
        try {
            audioPlayer.setAudio(maryTTS.generateAudio(text));
        } catch (SynthesisException e) {
            throw new RuntimeException(e);
        }
        audioPlayer.start();
    }
}
