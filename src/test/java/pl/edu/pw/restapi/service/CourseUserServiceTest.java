package pl.edu.pw.restapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.passay.Rule;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.pw.restapi.domain.CourseUser;
import pl.edu.pw.restapi.dto.RegisterCredentialsDTO;
import pl.edu.pw.restapi.repository.UserRepository;
import pl.edu.pw.restapi.security.SecurityConfig;
import pl.edu.pw.restapi.service.email.EmailNotificationService;
import pl.edu.pw.restapi.service.validator.UserCredentialsValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailNotificationService emailNotificationService;
    @Mock
    private List<Rule> passwordRules;
    @Spy
    private UserCredentialsValidator userCredentialsValidator = new UserCredentialsValidator(SecurityConfig.passwordRules());
    @InjectMocks
    private UserServiceImpl userService;


    // Tests for method loadByUsername

    @Test
    public void shouldReturnUserWhenUsernameIsInRepository() {
        CourseUser user = new CourseUser(1L, "username", "password", "email@email.com", List.of(), List.of());
        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        CourseUser actualUser = (CourseUser) userService.loadUserByUsername(user.getUsername());

        assertEquals(user, actualUser);
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenUsernameIsNotInRepository() {
        CourseUser user = new CourseUser(1L, "username", "password", "email@email.com", List.of(), List.of());
        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(user.getUsername())
        );
        assertEquals(user.getUsername() + " not found", exception.getMessage());
    }


    // Tests for method registerUser

    @Test
    public void shouldRegisterUserWhenCredentialsAreCorrect() {
        RegisterCredentialsDTO credentials = new RegisterCredentialsDTO("username", "Password123", "Password123", "email@email.com");

        when(userRepository.findByUsernameOrEmail(credentials.getUsername(), credentials.getEmail()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(credentials.getPassword()))
                .thenReturn("encodedPassword");

        userService.registerUser(credentials);

        verify(userRepository).save(any());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenUsernameIsTaken() {
        RegisterCredentialsDTO credentials = new RegisterCredentialsDTO("username", "Password123", "Password123", "email@email.com");
        CourseUser userWithSameName = new CourseUser(1L, credentials.getUsername(), "password", "email123", List.of(), List.of());

        when(userRepository.findByUsernameOrEmail(credentials.getUsername(), credentials.getEmail()))
                .thenReturn(Optional.of(userWithSameName));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(credentials)
        );
        assertEquals("Username is taken", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenPasswordIsNotStrongEnough() {
        RegisterCredentialsDTO credentials = new RegisterCredentialsDTO("username", "Password", "Password", "email@email.com");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(credentials)
        );
        assertEquals("[Password must contain at least 1 digit characters.]", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenPasswordAndRepeatedPasswordIsNotEqual() {
        RegisterCredentialsDTO credentials = new RegisterCredentialsDTO("username", "Password123", "Password321", "email@email.com");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(credentials)
        );
        assertEquals("Passwords are not equal", exception.getMessage());
    }

}
