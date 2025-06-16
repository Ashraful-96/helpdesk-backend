package com.aust.its.service;

import com.aust.its.dto.*;
import com.aust.its.entity.Developer;
import com.aust.its.entity.Issue;
import com.aust.its.entity.User;
import com.aust.its.enums.IssueStatus;
import com.aust.its.enums.Role;
import com.aust.its.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private static final Logger logger = LoggerFactory.getLogger(IssueService.class);

    private final IssueRepository issueRepository;
    private final UserService userService;
    private final DeveloperService developerService;

    public List<Issue> getIssuesByUserIdAndStatus(Long userId, IssueStatus status) {
        List<Issue> issues = issueRepository.findByUserIdAndStatus(userId, status);
        logger.info("issues by userId and status : {}", issues);
        return issues;
    }

    public List<IssueByStatusResponse> getIssuesByStatus(IssueStatus status) {
        List<Issue> issues = issueRepository.findByStatus(status);

        List<IssueByStatusResponse> responses = new ArrayList<>();

        for (Issue issue : issues) {
            IssueByStatusResponse response =
                    IssueByStatusResponse
                            .builder()
                            .id(issue.getId())
                            .title(issue.getTitle())
                            .description(issue.getDescription())
                            .user(issue.getUser())
                            .status(issue.getStatus())
                            .createdAt(issue.getCreatedAt())
                            .completedAt(issue.getCompletedAt())
                            .serialId(issue.getSerialId())
                            .build();

            if(IssueStatus.PENDING.equals(issue.getStatus()) || IssueStatus.INPROGRESS.equals(issue.getStatus())) {
                if(issue.getAssignedTo() != null) {
                    response.setDeveloperName(issue.getAssignedTo().getUser().getUsername());
                }
            }
            if(IssueStatus.COMPLETED.equals(issue.getStatus())) {
                if(issue.getResolvedBy() != null) {
                    response.setDeveloperName(issue.getResolvedBy().getUser().getUsername());
                }
            }
            if(IssueStatus.REJECTED.equals(issue.getStatus())) {
                if(issue.getRejectedBy() != null) {
                    response.setDeveloperName(issue.getRejectedBy().getUser().getUsername());
                }
                else {
                    response.setDeveloperName(issue.getRejectedByAdmin());
                }
            }

            responses.add(response);
        }

        logger.info("issues by status : {}", issues);
        return responses;
    }

    public DeveloperAssignedResponse assignIssue(Long issueId, final IssueAssignPayload issueAssignPayload) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found with ID: " + issueId));

        Developer developer = developerService.getById(issueAssignPayload.developerId());
        issue.setAssignedTo(developer);

        List<Issue> assignedIssues = developer.getAssignedIssues();
        assignedIssues.add(issue);

        developer.setAssignedIssues(assignedIssues);

        issueRepository.save(issue);
        developerService.save(developer);

        return DeveloperAssignedResponse
                .builder()
                .developerName(developer.getUser().getUsername())
                .currentlyTotalTaskInHand(developer.getAssignedIssues().size())
                .build();
    }

    public IssueRejectResponse rejectIssue(final Long issueId, final IssueRejectPayload issueRejectPayload) {
        User user = new User();

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found with ID: " + issueId));

        issue.setStatus(IssueStatus.REJECTED);
        issue.setRejectionReason(issueRejectPayload.rejectionReason());

        if(Role.ADMIN.getName().equalsIgnoreCase(issueRejectPayload.rejectedByRole())) {
            user = userService.getById(issueRejectPayload.rejectedById());
            issue.setRejectedByAdmin(user.getUsername());
        }
        else if(Role.DEVELOPER.getName().equalsIgnoreCase(issueRejectPayload.rejectedByRole())) {
            Developer developer = developerService.getById(issueRejectPayload.rejectedById());
            user = developer.getUser();
            issue.setRejectedBy(developer);
        }

        Issue savedIssue = issueRepository.save(issue);
        logger.info("saved issue : {}", savedIssue);

        return IssueRejectResponse
                .builder()
                .rejectedByName(user.getUsername())
                .rejectedByRole(user.getRole())
                .status(savedIssue.getStatus())
                .build();
    }

    public Issue updateIssueByStatus(Long issueId, IssueStatusUpdatePayload issueStatusUpdatePayload) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found with ID: " + issueId));

        User user = userService.getById(issueStatusUpdatePayload.workedBy());
        Developer developer = developerService.getByUserId(user.getId());

        if(IssueStatus.REJECTED.equals(issueStatusUpdatePayload.toStatus())) {
            issue.setStatus(IssueStatus.REJECTED);
            issue.setRejectedBy(developer);
            issue.setRejectionReason(issueStatusUpdatePayload.rejectionReason());
        }
        else if(IssueStatus.PENDING.equals(issueStatusUpdatePayload.toStatus()) ||
                IssueStatus.INPROGRESS.equals(issueStatusUpdatePayload.toStatus())) {
            issue.setStatus(issueStatusUpdatePayload.toStatus());
            issue.setAssignedTo(developer);
        }
        else if(IssueStatus.COMPLETED.equals(issueStatusUpdatePayload.toStatus())) {
            issue.setStatus(issueStatusUpdatePayload.toStatus());
            issue.setResolvedBy(developer);
        }

        return issueRepository.save(issue);
    }
}
