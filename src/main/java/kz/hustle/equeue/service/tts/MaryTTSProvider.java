package kz.hustle.equeue.service.tts;

import marytts.LocalMaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class MaryTTSProvider implements TTSProvider {

    private LocalMaryInterface maryTTS;

    public MaryTTSProvider() throws MaryConfigurationException {
        maryTTS = new LocalMaryInterface();
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
