package com.aust.its.dto.model;

import com.aust.its.dto.CategoryDto;
import com.aust.its.enums.IssueStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record IssueDto(
        long issueId,
        String title,
        String description,
        IssueStatus status,
        String serialId,
        LocalDateTime createdAt,
        LocalDateTime completedAt,
        LocalDateTime rejectedAt,
        String completedReason,
        String rejectionReason,
        @JsonProperty("user")
        UserDto userDto,
        @JsonProperty("categories")
        List<CategoryDto> categoryDtoList
) { }
