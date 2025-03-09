package com.example.task_manager;

import com.example.task_manager.entity.User;
import com.example.task_manager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindUser() {
        User user = new User("annie", "hashedPassword", "Annie", "Nyagolova", "annie@gmail.com");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById("annie");
        assertTrue(foundUser.isPresent());
        assertEquals("annie", foundUser.get().getUsername());
        assertEquals("annie@gmail.com", foundUser.get().getEmail());

    }

    @Test
    public void testUserExistsById() {
        User user = new User("annie", "hashedPassword", "Annie", "Nyagolova", "annie@gmail.com");
        userRepository.save(user);

        boolean exists = userRepository.existsById("annie");
        assertTrue(exists);
    }
}
