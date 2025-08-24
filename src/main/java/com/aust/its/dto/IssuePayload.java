package com.aust.its.dto;

import java.util.List;

public record IssuePayload(
    String description,
    String title,
    String userId,
    List<Long> categoryIds
) { }
