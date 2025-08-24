package com.aust.its.dto;

public record IssueRejectPayload(
        String rejectedByRole,
        Long rejectedById,
        String rejectionReason
) { }
