package com.nitesh.api.task.dto;

import com.nitesh.api.task.TaskStatus;

public record TaskResponse(
    Long id,
    String title,
    String description,
    TaskStatus status) {
}
