package com.aust.its.mapper;

import com.aust.its.dto.CategoryDto;
import com.aust.its.dto.IssuePayload;
import com.aust.its.dto.model.IssueDto;
import com.aust.its.dto.model.UserDto;
import com.aust.its.entity.Category;
import com.aust.its.entity.Issue;
import com.aust.its.entity.User;
import com.aust.its.enums.IssueStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class IssueMapper {

    public static Issue payloadToEntity(final IssuePayload payload, final User user, final List<Category> categories) {

        return
                Issue
                        .builder()
                        .title(payload.title())
                        .description(payload.description())
                        .user(user)
                        .status(IssueStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .serialId(UUID.randomUUID().toString())
                        .categories(categories)
                        .build();
    }

    public static IssueDto entityToDto(final Issue issue, final User user, final List<Category> categories) {
        UserDto userDto = UserMapper.entityToDto(user);
        List<CategoryDto> categoryDtoList = categories.stream().map(CategoryMapper::entityToDto).toList();

        return IssueDto
                .builder()
                .issueId(issue.getId())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .status(issue.getStatus())
                .serialId(issue.getSerialId())
                .createdAt(issue.getCreatedAt())
                .completedAt(issue.getCompletedAt())
                .rejectedAt(issue.getRejectedAt())
                .completedReason(issue.getCompletedReason())
                .rejectionReason(issue.getRejectionReason())
                .userDto(userDto)
                .categoryDtoList(categoryDtoList)
                .build();
    }
}
