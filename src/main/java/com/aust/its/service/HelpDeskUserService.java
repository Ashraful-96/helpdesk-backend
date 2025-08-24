package com.aust.its.service;

import com.aust.its.entity.HelpDeskUser;
import com.aust.its.repository.HelpDeskUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelpDeskUserService {

    private final HelpDeskUserRepository helpDeskUserRepository;

    public HelpDeskUser getHelpDeskUserByIdAndPassword(String userId, String password) {
        HelpDeskUser user = helpDeskUserRepository.findHelpDeskUserByUserId(userId).orElse(null);

        if(user == null) {
            throw new RuntimeException("User not found");
        }

        if(user.getPassword() == null) {
            throw new RuntimeException("You are new to the system, you can register now");
        }

        user = helpDeskUserRepository.findHelpDeskUserByUserIdAndPassword(userId, password).orElse(null);

        if(user == null) {
            throw new RuntimeException("Username and password mismatched");
        }

        return user;
    }

    public HelpDeskUser getHelpDeskUserByUserId(String userId) {
        return helpDeskUserRepository.findHelpDeskUserByUserId(userId).orElse(null);
    }

    public HelpDeskUser saveHelpDeskUser(HelpDeskUser helpDeskUser) {
        return helpDeskUserRepository.save(helpDeskUser);
    }
}
