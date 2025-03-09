package com.example.task_manager;

import com.example.task_manager.dto.UserDTO;
import com.example.task_manager.entity.User;
import com.example.task_manager.repository.UserRepository;
import com.example.task_manager.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    public UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser() {
        User user = new User("annie", "1234", "Annie", "Nyagolova", "annie@gmail.com");
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO resultUser = userService.createUser(user);

        assertNotNull(resultUser);
        assertEquals("annie", resultUser.getUsername());
        assertEquals("annie@gmail.com", resultUser.getEmail());

        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    public void testCreateUser_DuplicateUsername() {
        User newUser = new User("annie", "5678", "Another", "User", "newemail@gmail.com");
        Mockito.when(userRepository.existsById("annie")).thenReturn(true);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
           userService.createUser(newUser);
        });
    }

    @Test
    public void testPasswordHashing() {
        User user = new User("annie", "plaintextPassword", "Annie", "Nyagolova", "annie@gmail.com");

        Mockito.when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            assertNotEquals("plaintextPassword", savedUser.getPassword(), "Password should be hashed!");
            return savedUser;
        });

        userService.createUser(user);
    }
}