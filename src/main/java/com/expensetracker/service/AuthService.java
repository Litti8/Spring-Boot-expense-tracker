package com.expensetracker.service;

import com.expensetracker.dto.request.LoginRequest;
import com.expensetracker.dto.request.RegisterRequest;
import com.expensetracker.entity.Role;
import com.expensetracker.entity.User;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public User registerUser(RegisterRequest registerRequest) {
        // Check if user with the same email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Create and set a new User
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());


        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Set default role to USER
        user.setRole(Role.USER);
        user.setActive(true);

        // Save the new user to the database
        return userRepository.save(user);
    }

    public String loginUser(LoginRequest loginRequest) {

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Set context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        return tokenProvider.generateToken(authentication);
    }
}
