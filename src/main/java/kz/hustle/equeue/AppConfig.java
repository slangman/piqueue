package kz.hustle.equeue;

import kz.hustle.equeue.entity.Terminal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
