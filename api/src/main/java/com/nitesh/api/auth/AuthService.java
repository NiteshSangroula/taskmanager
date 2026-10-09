package com.nitesh.api.auth;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nitesh.api.exception.DuplicateResourceException;
import com.nitesh.api.exception.ResourceNotFoundException;
import com.nitesh.api.security.JwtService;
import com.nitesh.api.user.User;
import com.nitesh.api.user.UserRepository;
import com.nitesh.api.user.dto.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public RegisterResponse registerUser(RegisterRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new DuplicateResourceException("User with the email already exists");
    }
    if (userRepository.existsByUsername(request.username())) {
      throw new DuplicateResourceException("User with the username already exists");
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

    User user = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new ResourceNotFoundException("User not Found"));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new BadCredentialsException("Invalid password");
    }

    return jwtService.generateToken(request.email());
  }
}
