package com.aust.its.repository;

import com.aust.its.entity.HelpDeskRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HelpDeskRoleRepository extends JpaRepository<HelpDeskRole, Long> {

    Optional<HelpDeskRole> findHelpDeskRoleByRoleId(Long roleId);
}
