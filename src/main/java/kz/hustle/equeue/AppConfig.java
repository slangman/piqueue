package kz.hustle.equeue;

import kz.hustle.equeue.entity.HustleQueue;
import kz.hustle.equeue.entity.Operator;
import kz.hustle.equeue.entity.Terminal;
import kz.hustle.equeue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Autowired
    UserService userService;

    @Bean
    public HustleQueue queue() {
        return new HustleQueue();
    }

    @Bean
    public Terminal terminal(HustleQueue queue) {
        return new Terminal("Terminal 1", queue);
    }

    @Bean
    public OperatorManager operatorManager(HustleQueue queue) {
        OperatorManager operatorManager = new OperatorManager();

        userService.getAllUsers().forEach(user -> {
            Operator operator = new Operator(user, queue);
            operatorManager.addOperator(operator);
        });

        return operatorManager;
    }

    @Bean
    public SwingApp swingApp() {
        SwingApp swingUI = new SwingApp();
        swingUI.createAndShowGUI(); // Отображаем окно при старте приложения
        return swingUI;
    }
}
