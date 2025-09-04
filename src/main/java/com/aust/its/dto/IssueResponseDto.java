package com.aust.its.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueResponseDto {
    private long id;
    private String title;
    private String description;
    private String username;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private LocalDateTime rejectedAt;
    private String serialId;
    private String assignedTo;
    private String resolvedBy;
    private String rejectedBy;
    private List<CategoryDto> categories;
}