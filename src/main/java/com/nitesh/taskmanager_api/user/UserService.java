package com.nitesh.taskmanager_api.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nitesh.taskmanager_api.user.dto.RegisterRequest;
import com.nitesh.taskmanager_api.user.dto.RegisterResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
}
