package com.nitesh.api.task.dto;

import com.nitesh.api.task.TaskStatus;

import jakarta.validation.constraints.NotBlank;

public record UpdateTaskRequest(
    @NotBlank(message = "Title is required") String title,
    @NotBlank(message = "Description is required") String description,
    TaskStatus status) {
}
