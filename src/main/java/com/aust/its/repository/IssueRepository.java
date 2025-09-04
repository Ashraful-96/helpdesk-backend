package com.aust.its.repository;

import com.aust.its.entity.Issue;
import com.aust.its.enums.IssueStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByUserIdAndStatus(String userId, IssueStatus status);
    List<Issue> findByStatus(IssueStatus status);
    Page<Issue> findByStatus(IssueStatus status, Pageable pageable);
}
