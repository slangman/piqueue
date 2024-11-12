package kz.hustle.equeue.db;

import kz.hustle.equeue.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

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
            User operator = new User();
            operator.setUsername("operator1");
            operator.setPassword(passwordEncoder.encode("123"));
            operator.setDisplayName("1");
            operator.setRole("USER");

            userRepository.save(operator);
        }
    }

}
