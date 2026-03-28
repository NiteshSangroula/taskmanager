package com.nitesh.taskmanager_api.user.dto;

public record LoginRequest(
    String email,
    String password) {
}
