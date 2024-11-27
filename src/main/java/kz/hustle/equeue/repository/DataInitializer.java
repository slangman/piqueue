package kz.hustle.equeue.repository;

import kz.hustle.equeue.entity.Operator;
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
    private OperatorRepository operatorRepository;

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
    }

}
