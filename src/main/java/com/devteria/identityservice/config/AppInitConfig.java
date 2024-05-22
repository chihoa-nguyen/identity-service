package com.devteria.identityservice.config;

import com.devteria.identityservice.role.Role;
import com.devteria.identityservice.role.RoleRepository;
import com.devteria.identityservice.user.User;
import com.devteria.identityservice.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppInitConfig {
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()){
                Optional<Role> role = roleRepository.findById("ADMIN");
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(new HashSet<>())
                        .build();
                user.getRoles().add(role.get());
                userRepository.save(user);
                log.info("Admin user created with password is admin");
            }
        };
    }
}
