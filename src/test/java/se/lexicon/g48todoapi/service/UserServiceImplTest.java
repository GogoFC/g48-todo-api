package se.lexicon.g48todoapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.lexicon.g48todoapi.domain.dto.RoleDTOForm;
import se.lexicon.g48todoapi.domain.dto.UserDTOForm;
import se.lexicon.g48todoapi.domain.dto.UserDTOView;
import se.lexicon.g48todoapi.domain.entity.Role;
import se.lexicon.g48todoapi.domain.entity.User;
import se.lexicon.g48todoapi.exception.DataDuplicateException;
import se.lexicon.g48todoapi.exception.DataNotFoundException;
import se.lexicon.g48todoapi.repository.RoleRepository;
import se.lexicon.g48todoapi.repository.UserRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
    }


    @Test
    void register_ValidUserDTOForm_ShouldReturnUserDTOView() {
        // Arrange
        // Create a UserDTOForm with a sample email, password, and associated role
        Set<RoleDTOForm> roleDTOForms = new HashSet<>();
        roleDTOForms.add(RoleDTOForm.builder().id(1L).name("ROLE_USER").build());
        UserDTOForm userDTOForm = UserDTOForm.builder()
                .email("test@example.com")
                .password("password")
                .roles(roleDTOForms)
                .build();

        // Create a sample role for your test
        Role roleEntity = new Role(1L, "ROLE_USER");

        // Mock the behavior of passwordEncoder.encode
        String pwEncoder = "encodedPassword";
        when(passwordEncoder.encode("password")).thenReturn(pwEncoder);

        // Create a user entity with the expected encoded password and associated role
        User userEntity = new User(userDTOForm.getEmail(), pwEncoder);
        userEntity.addRole(roleEntity);

        // Define behavior for mock objects
        // Mock the repository methods to return expected values
        when(userRepository.existsByEmail(userDTOForm.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(userEntity);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleEntity));
        when(emailService.sendRegistrationEmail(userDTOForm.getEmail())).thenReturn(HttpStatusCode.valueOf(200));


        // Act
        UserDTOView savedUser = userService.register(userDTOForm);

        // Verify that specific methods were called
        // Ensure that the userService interacts with the mock objects as expected
        verify(userRepository).existsByEmail(userDTOForm.getEmail());
        verify(userRepository).save(any(User.class));
        verify(roleRepository).findById(1L);
        verify(passwordEncoder).encode("password");
        verify(emailService).sendRegistrationEmail(userDTOForm.getEmail());

        // Assertions
        // Check the result returned by userService.register
        assertNotNull(savedUser);
        assertEquals(userDTOForm.getEmail(), savedUser.getEmail());

        // Add more specific assertions based on the expected behavior of userService.register
    }

    @Test
    public void register_DuplicationEmail_ShouldThrowDataDuplicationException() {

        //Arrange
        String duplicatedEmail = "test@example.com";
        Set<RoleDTOForm> roleForm = Collections.singleton(RoleDTOForm.builder().id(2L).name("USER").build());

        UserDTOForm userForm = UserDTOForm.builder()
                .email(duplicatedEmail)
                .password("password")
                .roles(roleForm)
                .build();

        //Mock, if service asks for this method provide this answer.
        when(userRepository.existsByEmail(duplicatedEmail)).thenReturn(true);

        //Act, Assert
        assertThrows(DataDuplicateException.class, () -> userService.register(userForm));

        //Verify - that userRepository.existsByEmail was called with a duplicated Email

        verify(userRepository).existsByEmail(duplicatedEmail);


    }

    @Test
    void register_InvalidRole_ShouldThrowDataNotFoundException() {
        // Arrange
        String validEmail = "test@example.com";

        // Create a UserDTOForm with an invalid role (a role that does not exist in the system)
        Set<RoleDTOForm> roleDTOForms = Collections.singleton(RoleDTOForm.builder().id(2L).name("ROLE_INVALID").build());
        UserDTOForm userDTOForm = UserDTOForm.builder()
                .email(validEmail)
                .password("password")
                .roles(roleDTOForms)
                .build();

        // Mock the behavior of userRepository.existsByEmail to return false, indicating a non-duplicate email
        when(userRepository.existsByEmail(validEmail)).thenReturn(false);

        // Mock the behavior of roleRepository.findById to return an empty Optional, indicating the role doesn't exist
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(DataNotFoundException.class, () -> userService.register(userDTOForm)
        );

        // Verify that userRepository.existsByEmail was called with the valid email
        verify(userRepository).existsByEmail(validEmail);

        // Verify that roleRepository.findById was called with the role ID from the UserDTOForm
        verify(roleRepository).findById(2L);

    }

}