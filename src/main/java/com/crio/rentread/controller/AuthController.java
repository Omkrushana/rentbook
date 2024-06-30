package com.crio.rentread.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.crio.rentread.model.User;
import com.crio.rentread.repository.RoleRepository;
import com.crio.rentread.repository.UserRepository;
import com.crio.rentread.service.CustomUserDetailsService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomUserDetailsService userdetails;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        System.out.println("Register User hit------------------");
        // Check if user already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "User already exists";
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAdmin(false);
        // Save user
        userRepository.save(user);

        return "User registered successfully";
    }

    @PostMapping("/login")
    public UsernamePasswordAuthenticationToken loginUser(@RequestBody User loginUser) {
        System.out.println("Login endpoint hit");
        User user = userRepository.findByEmail(loginUser.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println(user.toString());

        // Compare the provided password with the encoded password in the database
        String rawPassword = loginUser.getPassword();
        String encodedPasswordFromDB = user.getPassword();

        System.out.println("Raw password from payload: " + rawPassword);
        System.out.println("Encoded password from DB: " + encodedPasswordFromDB);

        boolean matches = passwordEncoder.matches(rawPassword, encodedPasswordFromDB);

        System.out.println("Passwords match: " + matches);

        if (matches) {
            return new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    user.getPassword(),
                    new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                            new ArrayList<>()).getAuthorities());
            // return "Login successful";
        } else {
            throw new BadCredentialsException("Incorrect user credentials !!");
            // return "Login failed. Invalid credentials";
        }
    }

}
