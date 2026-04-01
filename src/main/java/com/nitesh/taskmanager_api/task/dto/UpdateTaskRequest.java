package com.nitesh.taskmanager_api.task.dto;

import com.nitesh.taskmanager_api.task.TaskStatus;

import jakarta.validation.constraints.NotBlank;

public record UpdateTaskRequest(
        @NotBlank(message = "Title is required") String title,
        @NotBlank(message = "Description is required") String description,
        TaskStatus status) {
}
