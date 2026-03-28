package com.nitesh.taskmanager_api.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nitesh.taskmanager_api.security.JwtService;
import com.nitesh.taskmanager_api.user.User;
import com.nitesh.taskmanager_api.user.UserRepository;
import com.nitesh.taskmanager_api.user.dto.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public RegisterResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("User with the email already exists");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("User with the username already exists");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        User createdUser = userRepository.save(user);

        return new RegisterResponse(
                createdUser.getUsername(),
                createdUser.getEmail());
    }

    public String loginUser(LoginRequest request) {
        if (!userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email not found!");
        }

        User user = userRepository.findByEmail(request.email());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }

        return jwtService.generateToken(request.email());
    }
}
