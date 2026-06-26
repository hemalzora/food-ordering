package com.foodapp.user.config;

import com.foodapp.user.entity.Role;
import com.foodapp.user.entity.User;
import com.foodapp.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Seeds a few demo users (with properly hashed passwords) on first startup if the table is empty.
 */
@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedUsers(UserRepository users, PasswordEncoder encoder) {
        return args -> {
            if (users.count() > 0) return;
            String encodedPassword = encoder.encode("password");   // demo password for all seeded users
            users.save(new User("Hemal Zora", "hzora@example.com", encodedPassword, Role.ADMIN, "9211223344", "MALE", "Patiala"));
            users.save(new User("Karan", "karan@example.com", encodedPassword, Role.CUSTOMER, "9988776655", "MALE", "YPS Colony, Patiala"));
            users.save(new User("Kohli Sweet Owner", "kohlisweet12@gmail.com", encodedPassword, Role.RESTAURANT, "1122334455", null, "Tripuri Town, Patiala"));
        };
    }
}
