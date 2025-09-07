package com.aust.its.controller;

import com.aust.its.dto.*;
import com.aust.its.entity.Issue;
import com.aust.its.enums.IssueStatus;
import com.aust.its.service.CategoryService;
import com.aust.its.service.IssueService;
import com.aust.its.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private static final Logger logger = LoggerFactory.getLogger(IssueController.class);
    private final IssueService issueService;
    private final UserService userService;
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> submitIssue(@RequestBody IssuePayload issuePayload) {
        logger.info("Issue Payload :: {}", issuePayload);
        return ResponseEntity.ok(issueService.createIssue(issuePayload));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getIssues(@PathVariable("id") String userId,
                                 @RequestParam IssueStatus status) {

        logger.info("finding issues of userId :: {} for status :: {}", userId, status);
        return ResponseEntity.ok(issueService.getIssuesByUserIdAndStatus(userId, status));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getIssuesByStatus(@PathVariable("status") IssueStatus status) {
        logger.info("finding issues of status :: {}", status);
        return ResponseEntity.ok(issueService.getIssuesByStatus(status));
    }

    @PostMapping("{id}/assign")
    public ResponseEntity<?> assignIssueToDeveloper(@PathVariable Long id,
                                                            @RequestBody IssueAssignPayload issueAssignPayload) {
        logger.info("Assigning issue {} to developer", id);
        return ResponseEntity.ok(issueService.assignIssue(id, issueAssignPayload));
    }

    @PostMapping("{id}/reject")
    public ResponseEntity<?> rejectIssue(@PathVariable Long id,
                                           @RequestBody IssueRejectPayload issueRejectPayload) {
        return ResponseEntity.ok(issueService.rejectIssue(id, issueRejectPayload));
    }

    @PutMapping("{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                              @RequestBody IssueStatusUpdatePayload issueStatusUpdatePayload) {
        return ResponseEntity.ok(issueService.updateIssueByStatus(id, issueStatusUpdatePayload));
    }

    @PutMapping("{id}/assign")
    public ResponseEntity<?> updateAssignee(@PathVariable("id") Long issueId,
                                                @RequestBody AssignDeveloperPayload assignDeveloperPayload) {
        return ResponseEntity.ok(issueService.updateAssignee(issueId, assignDeveloperPayload.developerId()));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getIssueCount() {
        return ResponseEntity.ok(issueService.getAllIssueCount());
    }

    @GetMapping("/{status}/count")
    public ResponseEntity<?> getIssueCountByStatus(@PathVariable("status") IssueStatus issueStatus) {
        return ResponseEntity.ok(issueService.getIssueCountByStatus(issueStatus));
    }


    //new controller for file:
    @PostMapping("/with-files")
    public ResponseEntity<?> createIssueWithFiles(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("userId") String userId,
            @RequestParam("category") String category,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) {
        try {
            List<String> savedFileNames = new ArrayList<>();

            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    String originalFilename = file.getOriginalFilename();
                    if (originalFilename == null || originalFilename.isBlank()) continue;

                    String uploadDir = "D:/iums_images/" + userId;
                    File dir = new File(uploadDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    File dest = new File(uploadDir + "/" + originalFilename);
                    file.transferTo(dest);

                    savedFileNames.add(originalFilename);
                }
            }

            Issue newIssue = issueService.createIssueWithFiles(
                    title, description, userId, category, savedFileNames
            );

            return ResponseEntity.ok(newIssue);

        } catch (Exception e) {
            logger.error("file exception {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving issue: " + e.getMessage());
        }
    }


    @GetMapping("/files/{userId}/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String userId, @PathVariable String filename) {
        try {
            Path path = Paths.get("C:/Users/austi/Downloads/iums_images" + userId).resolve(filename);
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String mimeType = Files.probeContentType(path);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllIssues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) IssueStatus status) {
        return ResponseEntity.ok(issueService.getAllIssues(page, size, status));
    }
}