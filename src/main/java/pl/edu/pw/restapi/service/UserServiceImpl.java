package pl.edu.pw.restapi.service;

import lombok.RequiredArgsConstructor;
import org.passay.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.User;
import pl.edu.pw.restapi.dto.RegisterCredentialsDTO;
import pl.edu.pw.restapi.repository.UserRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    @Override
    public void registerUser(RegisterCredentialsDTO credentials) {
        checkIfUsernameIsTaken(credentials.getUsername());
        validatePasswords(credentials.getPassword(), credentials.getPasswordRepeat());

        userRepository.save(User.builder()
            .username(credentials.getUsername())
            .password(passwordEncoder.encode(credentials.getPassword()))
            .build()
        );
    }

    private void checkIfUsernameIsTaken(String username) {
        Optional<User> userSameUsername = userRepository.findByUsername(username);

        if (userSameUsername.isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }
    }

    private void validatePasswords(String password, String passwordRepeat) {
        if (password.equals(passwordRepeat)) {
            PasswordValidator validator = passwordValidator();
            PasswordData passwordData = new PasswordData(password);
            RuleResult result = validator.validate(passwordData);
            if(!result.isValid()) {
                throw new IllegalArgumentException(validator.getMessages(result).toString());
            }
        } else {
            throw new IllegalArgumentException("Passwords are not equal");
        }
    }

    private PasswordValidator passwordValidator() {
        return new PasswordValidator(List.of(
                new LengthRule(6, 20),
                new UppercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new AlphabeticalCharacterRule(1)
        ));
    }
}
