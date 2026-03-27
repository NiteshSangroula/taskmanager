package com.nitesh.taskmanager_api.user.dto;

public record RegisterRequest(
        String username,
        String email,
        String password) {
}
