package kz.hustle.equeue;

import kz.hustle.equeue.entity.Operator;
import kz.hustle.equeue.entity.User;
import kz.hustle.equeue.entity.TTSSettings;
import kz.hustle.equeue.repository.OperatorRepository;
import kz.hustle.equeue.repository.TTSSettingsRepository;
import kz.hustle.equeue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private TTSSettingsRepository ttsSettingsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("admin") == null) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setRole("ADMIN");

            userRepository.save(adminUser);
        }

        if (userRepository.findByUsername("operator1") == null) {
            User user = new User();
            user.setUsername("operator1");
            user.setPassword(passwordEncoder.encode("123"));
            user.setDisplayName("1");
            user.setRole("USER");

            userRepository.save(user);

            user = userRepository.findByUsername("operator1");
            Operator operator = new Operator();
            operator.setUser(user);

            operatorRepository.save(operator);
        }

        if (ttsSettingsRepository.findAll().isEmpty()) {
            TTSSettings voiceSettings = new TTSSettings();
            voiceSettings.setProvider("MaryTTS");
            voiceSettings.setVoiceName("cmu-rms-hsmm");
            voiceSettings.setLanguage("en");
            ttsSettingsRepository.save(voiceSettings);
        }
    }
}
