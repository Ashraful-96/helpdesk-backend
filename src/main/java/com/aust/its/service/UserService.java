package com.aust.its.service;

import com.aust.its.dto.RegisterPayload;
import com.aust.its.entity.HelpDeskUser;
import com.aust.its.entity.User;
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
    private final PasswordEncoder passwordEncoder;

    public User getById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public HelpDeskUser register(RegisterPayload payload) {
        Optional<User> userOptional = userRepository.findById(payload.userId());
        if (userOptional.isPresent()) {

            HelpDeskUser helpDeskUser = helpDeskUserService.getHelpDeskUserByUserId(payload.userId());
            User user = userOptional.get();

            if(helpDeskUser == null) {
                HelpDeskUser helpDeskUser1 = new HelpDeskUser();
                helpDeskUser1.setUserId(payload.userId());
                helpDeskUser1.setPassword(passwordEncoder.encode(payload.password()));

                if(user.getEmployeeId() != null) {
                    helpDeskUser1.setRoleId(13000);
                } else {
                    helpDeskUser1.setRoleId(13001);
                }

                return helpDeskUserService.saveHelpDeskUser(helpDeskUser1);
            }
            return helpDeskUser;
        }

        // throw exception :: you are not an IUMS user
        return null;
    }

//    public User getByUsernameAndPassword(String username, String password) {
//        User user = userRepository.findByUsername(username).orElse(null);
//
//        if(user == null) {
//            throw new RuntimeException("User not found");
//        }
//
//        if(user.getPassword() == null) {
//            throw new RuntimeException("You are new to the system, you can register now");
//        }
//
//        user = userRepository.findByUsernameAndPassword(username, password).orElse(null);
//
//        if(user == null) {
//            throw new RuntimeException("Username and password mismatched");
//        }
//
//        return user;
//    }
}
