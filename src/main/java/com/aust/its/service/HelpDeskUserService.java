package com.aust.its.service;

import com.aust.its.entity.HelpDeskUser;
import com.aust.its.repository.HelpDeskUserRepository;
import org.springframework.stereotype.Service;

@Service
public class HelpDeskUserService {

    private final HelpDeskUserRepository helpDeskUserRepository;

    public HelpDeskUserService(HelpDeskUserRepository helpDeskUserRepository) {
        this.helpDeskUserRepository = helpDeskUserRepository;
    }

    public boolean isHelpDeskUserAlreadyExists(String userId) {
        return helpDeskUserRepository.existsHelpDeskUserByUserId(userId);
    }

    public HelpDeskUser saveHelpDeskUser(HelpDeskUser helpDeskUser) {
        return helpDeskUserRepository.save(helpDeskUser);
    }
}
