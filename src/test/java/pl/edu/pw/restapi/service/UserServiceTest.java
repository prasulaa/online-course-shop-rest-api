package pl.edu.pw.restapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.pw.restapi.domain.User;
import pl.edu.pw.restapi.dto.RegisterCredentialsDTO;
import pl.edu.pw.restapi.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;


    // Tests for method loadByUsername

    @Test
    public void shouldReturnUserWhenUsernameIsInRepository() {
        User user = new User(1L, "username", "password", List.of(), List.of());
        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        User actualUser = (User) userService.loadUserByUsername(user.getUsername());

        assertEquals(user, actualUser);
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenUsernameIsNotInRepository() {
        User user = new User(1L, "username", "password", List.of(), List.of());
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
        RegisterCredentialsDTO credentials = new RegisterCredentialsDTO("username", "Password123", "Password123");

        when(userRepository.findByUsername(credentials.getUsername()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(credentials.getPassword()))
                .thenReturn("encodedPassword");

        userService.registerUser(credentials);

        verify(userRepository).save(any());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenUsernameIsTaken() {
        RegisterCredentialsDTO credentials = new RegisterCredentialsDTO("username", "Password123", "Password123");
        User userWithSameName = new User(1L, credentials.getUsername(), "password", List.of(), List.of());

        when(userRepository.findByUsername(credentials.getUsername()))
                .thenReturn(Optional.of(userWithSameName));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(credentials)
        );
        assertEquals("Username is already taken", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenPasswordIsNotStrongEnough() {
        RegisterCredentialsDTO credentials = new RegisterCredentialsDTO("username", "Password", "Password");

        when(userRepository.findByUsername(credentials.getUsername()))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(credentials)
        );
        assertEquals("[Password must contain at least 1 digit characters.]", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenPasswordAndRepeatedPasswordIsNotEqual() {
        RegisterCredentialsDTO credentials = new RegisterCredentialsDTO("username", "Password123", "Password321");

        when(userRepository.findByUsername(credentials.getUsername()))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(credentials)
        );
        assertEquals("Passwords are not equal", exception.getMessage());
    }

}
