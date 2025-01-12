package kz.hustle.equeue;

import marytts.LocalMaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class TTS {

    private LocalMaryInterface maryTTS;

    private String voice;

    public TTS() throws MaryConfigurationException {
        maryTTS = new LocalMaryInterface();
        maryTTS.setVoice("dfki-poppy-hsmm");
    }

    public LocalMaryInterface getMaryTTS() {
        return maryTTS;
    }

    public void generateAndPlayAudio(String text) throws UnsupportedEncodingException, SynthesisException {
        text = new String(text.getBytes("Windows-1251"), StandardCharsets.UTF_8);
        marytts.util.data.audio.AudioPlayer audioPlayer = new AudioPlayer();
        audioPlayer.setAudio(maryTTS.generateAudio(text));
        audioPlayer.start();
    }
}
