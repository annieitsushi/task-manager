package com.example.task_manager.controller;

import com.example.task_manager.dto.UserDTO;
import com.example.task_manager.entity.User;
import com.example.task_manager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDTO createUser(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{username}")
    public UserDTO getUser(@PathVariable String username) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();
        if (!loggedUsername.equals(username)) {
            throw new AccessDeniedException("You're not allowed to access this user's data.");
        }
        return userService.getUser(username);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();
        if (!loggedUsername.equals(username)) {
            throw new AccessDeniedException("You're not allowed to access this user's data.");
        }
        userService.deleteUser(username);
    }

    @PutMapping("/{username}")
    public void updateUser(@PathVariable String username, @RequestBody User user) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();
        if (!loggedUsername.equals(user.getUsername())) {
            throw new AccessDeniedException("You're not allowed to access this user's data.");
        }
        userService.updateUser(user);
    }

}
