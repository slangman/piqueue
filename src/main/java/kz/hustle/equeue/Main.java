package kz.hustle.equeue;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    /*
    @Bean
    public CommandLineRunner run() {
        return args -> {
            // Инициализация и запуск Swing GUI
            SwingApp swingApp = new SwingApp();
            swingApp.start();
        };
    }
    */
}