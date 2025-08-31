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

    public HelpDeskUser getHelpDeskUserByUserId(String userId) {
        return helpDeskUserRepository.findHelpDeskUserByUserId(userId).orElse(null);
    }

    public HelpDeskUser saveHelpDeskUser(HelpDeskUser helpDeskUser) {
        return helpDeskUserRepository.save(helpDeskUser);
    }
}
