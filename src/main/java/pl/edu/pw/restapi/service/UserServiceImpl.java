package pl.edu.pw.restapi.service;

import lombok.RequiredArgsConstructor;
import org.passay.CharacterRule;
import org.passay.PasswordGenerator;
import org.passay.Rule;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.User;
import pl.edu.pw.restapi.dto.ChangePasswordDTO;
import pl.edu.pw.restapi.dto.RegisterCredentialsDTO;
import pl.edu.pw.restapi.repository.UserRepository;
import pl.edu.pw.restapi.service.email.EmailNotificationService;
import pl.edu.pw.restapi.service.validator.UserCredentialsValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCredentialsValidator userCredentialsValidator;
    private final EmailNotificationService notificationService;
    private final List<Rule> passwordRules;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    @Override
    public void registerUser(RegisterCredentialsDTO credentials) {
        userCredentialsValidator.validate(credentials);
        checkIfUsernameOrEmailIsTaken(credentials.getUsername(), credentials.getEmail());

        User user = mapToUser(credentials);
        userRepository.save(user);
        notificationService.sendRegistrationNotification(user);
    }

    @Override
    public void resetPassword(String email) {
        userCredentialsValidator.validateEmail(email);
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return;
        }

        String password = generatePassword();

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        notificationService.sendResetPasswordNotification(user, password);
    }

    @Override
    public void changePassword(String username, ChangePasswordDTO changePassword) {
        userCredentialsValidator.validatePasswords(changePassword.getNewPassword(), changePassword.getNewPasswordRepeat());
        User user = (User) loadUserByUsername(username);

        if (!passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }

        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        userRepository.save(user);
    }

    private String generatePassword() {
        PasswordGenerator generator = new PasswordGenerator();
        List<CharacterRule> rules = passwordRules.stream()
                .filter((rule) -> rule instanceof CharacterRule)
                .map((rule) -> (CharacterRule) rule)
                .toList();

        return generator.generatePassword(8, rules);
    }

    private User mapToUser(RegisterCredentialsDTO credentials) {
        return User.builder()
                .username(credentials.getUsername())
                .password(passwordEncoder.encode(credentials.getPassword()))
                .email(credentials.getEmail())
                .build();
    }

    private void checkIfUsernameOrEmailIsTaken(String username, String email) {
        User user = userRepository.findByUsernameOrEmail(username, email).orElse(null);

        if (user == null) {
            return;
        }

        if (user.getUsername().equals(username)) {
            throw new IllegalArgumentException("Username is taken");
        }

        if (user.getEmail().equals(email)) {
            throw new IllegalArgumentException("Email is taken");
        }
    }

}
