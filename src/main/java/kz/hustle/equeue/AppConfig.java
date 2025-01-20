package kz.hustle.equeue;

import kz.hustle.equeue.entity.Operator;
import kz.hustle.equeue.entity.TTSSettings;
import kz.hustle.equeue.entity.Terminal;
import kz.hustle.equeue.entity.User;
import kz.hustle.equeue.repository.OperatorRepository;
import kz.hustle.equeue.repository.TTSSettingsRepository;
import kz.hustle.equeue.repository.UserRepository;
import kz.hustle.equeue.service.UserService;
import kz.hustle.equeue.service.tts.GoogleTTSProvider;
import kz.hustle.equeue.service.tts.MaryTTSProvider;
import kz.hustle.equeue.service.tts.TTSProvider;
import marytts.exceptions.MaryConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Locale;

@Configuration
public class AppConfig {

    @Bean
    public Terminal terminal() {
        return new Terminal("Terminal 1");
    }

    @Bean
    public SwingApp swingApp() {
        SwingApp swingUI = new SwingApp();
        swingUI.createAndShowGUI(); // Show the window on the app start
        return swingUI;
    }



}
