package com.nitesh.taskmanager_api.task.dto;

import com.nitesh.taskmanager_api.task.TaskStatus;

public record TaskResponse(
    Long id,
    String title,
    String description,
    TaskStatus status) {
}
