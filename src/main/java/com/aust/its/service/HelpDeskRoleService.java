package com.aust.its.service;

import com.aust.its.entity.HelpDeskRole;
import com.aust.its.repository.HelpDeskRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelpDeskRoleService {

    private final HelpDeskRoleRepository helpDeskRoleRepository;

    public HelpDeskRole getHelpDeskRoleById(Long id) {
        return helpDeskRoleRepository.findById(id).orElse(null);
    }
}
