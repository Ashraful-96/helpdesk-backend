package com.aust.its.config.security;

import com.aust.its.entity.HelpDeskRole;
import com.aust.its.entity.HelpDeskUser;
import com.aust.its.exception.UserNotFoundException;
import com.aust.its.repository.HelpDeskRoleRepository;
import com.aust.its.repository.HelpDeskUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final HelpDeskUserRepository helpDeskUserRepository;
    private final HelpDeskRoleRepository helpDeskRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<HelpDeskUser> user = helpDeskUserRepository.findHelpDeskUserByUserId(username);

        if (user.isEmpty()) {
            throw new UserNotFoundException("user not found");
        }

        Optional<HelpDeskRole> role = helpDeskRoleRepository.findById(user.get().getRoleId());
        return new CustomUserDetails(user.get(), role.orElse(null));
    }
}
