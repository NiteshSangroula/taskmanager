package com.nitesh.taskmanager_api.task.dto;

public record TaskRequest(
        String title,
        String description) {
}
