package banksystem.configuration;

import banksystem.entity.User;
import banksystem.enums.Role;
import banksystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminCreating {
    @Bean
    CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.email}") String email,
            @Value("${app.admin.password}") String password
    ) {
        return args -> {
            if (userRepository.findByEmail(email).isEmpty()) {
                User admin = new User();

                admin.setEmail(email);
                admin.setPassword(passwordEncoder.encode(password));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
            }
        };
    }
}

