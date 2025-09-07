package com.aust.its.service;

import com.aust.its.dto.RegisterPayload;
import com.aust.its.entity.HelpDeskUser;
import com.aust.its.entity.User;
import com.aust.its.exception.UserAlreadyExistsException;
import com.aust.its.exception.UserNotFoundException;
import com.aust.its.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final HelpDeskUserService helpDeskUserService;
    private final PasswordEncoder customPasswordEncoder;

    public User getById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public HelpDeskUser register(RegisterPayload payload) {
        Optional<User> userOptional = userRepository.findById(payload.userId());

        if(userOptional.isEmpty()) {
            throw new UserNotFoundException("the user is not an IUMS user");
        }

        if(helpDeskUserService.isHelpDeskUserAlreadyExists(payload.userId())) {
            throw new UserAlreadyExistsException("the user is already exists in the helpDesk system");
        }

        User user = userOptional.get();
        HelpDeskUser helpDeskUserToSave = new HelpDeskUser();
        helpDeskUserToSave.setUserId(payload.userId());
        helpDeskUserToSave.setEmail(payload.email());
        helpDeskUserToSave.setPassword(customPasswordEncoder.encode(payload.password()));

        if(user.getEmployeeId() != null) {
            helpDeskUserToSave.setRoleId(13000);
        } else {
            helpDeskUserToSave.setRoleId(13001);
        }

        return helpDeskUserService.saveHelpDeskUser(helpDeskUserToSave);
    }
}
