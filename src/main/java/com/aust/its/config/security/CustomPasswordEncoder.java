package com.aust.its.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CustomPasswordEncoder {

    @Bean("customPasswordEncoderBean")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
