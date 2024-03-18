package se.lexicon.g48todoapi.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.lexicon.g48todoapi.domain.entity.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        userRepository.save(user);
    }

    @Test
    public void testUpdateExpiredByEmail() {
        // Arrange
        String email = "test@example.com";
        Optional<User> user1 = userRepository.findById(email);
        assertTrue(user1.isPresent());
        assertFalse(user1.get().isExpired());
        System.out.println("user1.get() = " + user1.get());

        // Act
        userRepository.updateExpiredByEmail(email, true);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Optional<User> user2 = userRepository.findById(email);
        assertTrue(user2.isPresent());
        assertTrue(user2.get().isExpired());
        System.out.println("user2.get() = " + user2.get());
    }

    @Test
    public void testUpdateExpiredByEmail_NonExistingUser() {
        // Arrange: Ensure that the user with the specified email does not exist
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
    }
}