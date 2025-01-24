package kz.hustle.equeue.service.tts;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import kz.hustle.equeue.entity.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class GoogleTTSProvider implements TTSProvider {

    private String languageCode;

    private String voiceName;

    private SsmlVoiceGender voiceGender;

    private TextToSpeechSettings settings;

    private static final Logger logger = LoggerFactory.getLogger(GoogleTTSProvider.class);

    //TODO: Hardcode, fix
    private final String GOOGLE_CREDENTIALS_FILE = "c:/work/custom-router-443713-t5-80c81ccab6e2.json";

    public GoogleTTSProvider() throws IOException {
        settings = TextToSpeechSettings.newBuilder()
                .setCredentialsProvider(() -> GoogleCredentials.fromStream(new FileInputStream(GOOGLE_CREDENTIALS_FILE)))
                .build();
    }

    @Override
    public String getName() {
        return "GoogleTTS";
    }

    @Override
    public String getVoiceName() {
        return voiceName;
    }

    @Override
    public String getLanguageCode() {
        return languageCode;
    }

    @Override
    public void setLocale(Locale locale) {
        this.languageCode = locale.getLanguage();
        if (locale.getCountry() != null) {
            this.languageCode = this.languageCode + "-" + locale.getCountry();
        }
    }

    @Override
    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }

    public void setVoiceGender(SsmlVoiceGender voiceGender) {
        this.voiceGender = voiceGender;
    }

    public List<Language> getAvailableLanguages() {
        List<Language> result = new ArrayList<>();
        result.add(new Language("en-US", "English (US)"));
        result.add(new Language("en-GB", "English (UK)"));
        result.add(new Language("ru-RU", "Russian"));
        result.sort(Comparator.comparing(Language::getDisplayName));
        return result;
    }

    public Set<String> getAvailableLocales() {
        Set<String> result = new HashSet<>();
        for (Voice voice : getVoices()) {
            voice.getLanguageCodesList().iterator().forEachRemaining(result::add);
        }
        return result;
    }

    @Override
    public Set<String> getAvailableVoices() {
        Set<String> result = new HashSet<>();
        for (Voice voice : getVoices()) {
            result.add(voice.getName());
        }
        return result;
    }

    @Override
    public Set<String> getAvailableVoices(Locale locale) {
        Set<String> result = new HashSet<>();
        for (Voice voice : getVoices(locale)) {
            result.add(voice.getName());
        }
        return result;
    }

    //TODO: implement
    @Override
    public List<String> getAvailableVoices(String language) {
        return Collections.emptyList();
    }

    @Override
    public void generateAndPlayAudio(String text) {
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(settings)) {
            //SynthesisInput input = SynthesisInput.newBuilder().setTextBytes(ByteString.copyFrom((text.getBytes(StandardCharsets.UTF_8)))).build();
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
            VoiceSelectionParams voice = getVoiceSelectionParams();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.LINEAR16) // WAV format
                    .build();

            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            ByteString audioContents = response.getAudioContent();

            playAudio(audioContents.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Voice> getVoices() {
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(settings)) {
            ListVoicesRequest request = ListVoicesRequest.newBuilder().build();

            ListVoicesResponse response = textToSpeechClient.listVoices(request);

            return response.getVoicesList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Voice> getVoices(Locale locale) {
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(settings)) {
            String languageCode = locale.getLanguage();
            if (!locale.getCountry().isEmpty()) {
                languageCode = languageCode + "-" + locale.getCountry();
            }
            ListVoicesRequest request = ListVoicesRequest.newBuilder()
                    .setLanguageCode(languageCode)
                    .build();

            ListVoicesResponse response = textToSpeechClient.listVoices(request);

            return response.getVoicesList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void playAudio(byte[] audioData) {
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audioData))) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Start playing the clip
            clip.start();
            logger.info("Playing audio...");

            // Block the main thread until the clip finishes playing
            while (!clip.isRunning()) {
                Thread.sleep(10); // Wait for the clip to start
            }
            while (clip.isRunning()) {
                Thread.sleep(10); // Wait for the clip to finish
            }

            clip.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private VoiceSelectionParams getVoiceSelectionParams() {
        VoiceSelectionParams.Builder result = VoiceSelectionParams
                .newBuilder()
                .setLanguageCode(languageCode);
        if (voiceName != null) {
            result = result.setName(voiceName);
        }
        if (voiceGender != null) {
            result = result.setSsmlGender(voiceGender);
        }
        return result.build();
    }
}
