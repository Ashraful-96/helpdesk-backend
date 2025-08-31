package com.aust.its.service;

import com.aust.its.entity.HelpDeskUser;
import com.aust.its.repository.HelpDeskUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HelpDeskUserService {

    private final HelpDeskUserRepository helpDeskUserRepository;
    private final PasswordEncoder passwordEncoder;

    public HelpDeskUserService(HelpDeskUserRepository helpDeskUserRepository,
                               @Qualifier("customPasswordEncoderBean") PasswordEncoder passwordEncoder) {
        this.helpDeskUserRepository = helpDeskUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public HelpDeskUser getHelpDeskUserByIdAndPassword(String userId, String password) {
        HelpDeskUser user = helpDeskUserRepository.findHelpDeskUserByUserId(userId).orElse(null);

        if(user == null) {
            throw new RuntimeException("User not found");
        }

        if(user.getPassword() == null) {
            throw new RuntimeException("You are new to the system, you can register now");
        }

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Username and password mismatched");
        }

        return user;
//        $2a$10$JpLt4gPkoBnFvJ0ad7ijee7kIt0GCrKSRAKdeZv9ew3zcdo.MMqX2
    }

    public HelpDeskUser getHelpDeskUserByUserId(String userId) {
        return helpDeskUserRepository.findHelpDeskUserByUserId(userId).orElse(null);
    }

    public HelpDeskUser saveHelpDeskUser(HelpDeskUser helpDeskUser) {
        return helpDeskUserRepository.save(helpDeskUser);
    }
}
