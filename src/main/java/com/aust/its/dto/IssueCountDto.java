package com.aust.its.dto;

import com.aust.its.enums.IssueStatus;

public record IssueCountDto(
    IssueStatus status,
    long count
) { }
