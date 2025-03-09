package com.example.task_manager.service;

import com.example.task_manager.dto.UserDTO;
import com.example.task_manager.entity.User;
import com.example.task_manager.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO getUser(String username)  {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new UserDTO(user);
    }

    public UserDTO createUser(User user) {
        if (userRepository.existsById(user.getUsername())) {
            throw new IllegalArgumentException("Username " + user.getUsername() + " already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new UserDTO(userRepository.save(user));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    public void updateUser(User user) {
       userRepository.findById(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User with username " + user.getUsername() + " not found"));
        userRepository.save(user);
    }

}
