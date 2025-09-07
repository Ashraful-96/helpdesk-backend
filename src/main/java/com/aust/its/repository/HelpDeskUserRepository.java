package com.aust.its.repository;

import com.aust.its.entity.HelpDeskUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HelpDeskUserRepository extends JpaRepository<HelpDeskUser, String> {

    Optional<HelpDeskUser> findHelpDeskUserByUserId(String userId);
    Optional<HelpDeskUser> findHelpDeskUserByUserIdAndPassword(String userId, String password);
    boolean existsHelpDeskUserByUserId(String userId);
}
