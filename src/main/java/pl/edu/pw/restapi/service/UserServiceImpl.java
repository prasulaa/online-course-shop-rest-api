package pl.edu.pw.restapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.User;
import pl.edu.pw.restapi.dto.RegisterCredentialsDTO;
import pl.edu.pw.restapi.repository.UserRepository;
import pl.edu.pw.restapi.service.email.EmailNotificationService;
import pl.edu.pw.restapi.service.validator.RegisterCredentialsValidator;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegisterCredentialsValidator registerCredentialsValidator;
    private final EmailNotificationService notificationService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    @Override
    public void registerUser(RegisterCredentialsDTO credentials) {
        checkIfUsernameIsTaken(credentials.getUsername());
        registerCredentialsValidator.validate(credentials);

        User user = mapToUser(credentials);
        userRepository.save(user);
        notificationService.sendRegistrationNotification(user);
    }

    private User mapToUser(RegisterCredentialsDTO credentials) {
        return User.builder()
                .username(credentials.getUsername())
                .password(passwordEncoder.encode(credentials.getPassword()))
                .email(credentials.getEmail())
                .build();
    }

    private void checkIfUsernameIsTaken(String username) {
        Optional<User> userSameUsername = userRepository.findByUsername(username);

        if (userSameUsername.isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }
    }

}
