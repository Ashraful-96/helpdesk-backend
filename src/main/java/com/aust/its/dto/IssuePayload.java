package com.aust.its.dto;

import java.util.List;

public record IssuePayload(
    String description,
    String title,
    long userId,
    List<Long> categoryIds
) { }
