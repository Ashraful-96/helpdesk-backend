package com.aust.its.controller;

import com.aust.its.dto.IssuesOfDeveloperDto;
import com.aust.its.entity.Developer;
import com.aust.its.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/developers")
@RequiredArgsConstructor
public class DeveloperController {

    private static final Logger logger = LoggerFactory.getLogger(DeveloperController.class);
    private final DeveloperService developerService;

    @GetMapping
    public ResponseEntity<List<Developer>> getDevelopers() {
        logger.info("getDevelopers from controller");
        return ResponseEntity.ok(developerService.getAll());
    }

    @GetMapping("{id}/issues")
    public ResponseEntity<IssuesOfDeveloperDto> getIssuesOfDeveloper(@PathVariable("id") String userId) {
        Developer developer = developerService.getByUserId(userId);
        logger.info("Finding issues of developer : {}", developer.getId());

        return ResponseEntity.ok(developerService.getIssuesOfDeveloper(developer.getId()));
    }
}
