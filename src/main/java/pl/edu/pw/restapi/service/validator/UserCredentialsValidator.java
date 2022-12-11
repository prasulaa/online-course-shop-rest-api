package pl.edu.pw.restapi.service.validator;

import lombok.AllArgsConstructor;
import org.passay.*;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.dto.RegisterCredentialsDTO;

import java.util.List;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserCredentialsValidator {

    private final List<Rule> passwordRules;

    public void validate(RegisterCredentialsDTO credentials) {
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials cannot be null");
        }
        validateEmail(credentials.getEmail());
        validatePasswords(credentials.getPassword(), credentials.getPasswordRepeat());
    }

    public void validateEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        String regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!Pattern.compile(regexPattern).matcher(email).matches()) {
            throw new IllegalArgumentException("Email is not valid");
        }
    }

    public void validatePasswords(String password, String passwordRepeat) {
        if (password == null || passwordRepeat == null) {
            throw new IllegalArgumentException("Passwords cannot be null");
        }

        if (password.equals(passwordRepeat)) {
            PasswordValidator validator = new PasswordValidator(passwordRules);
            PasswordData passwordData = new PasswordData(password);
            RuleResult result = validator.validate(passwordData);
            if(!result.isValid()) {
                throw new IllegalArgumentException(validator.getMessages(result).toString());
            }
        } else {
            throw new IllegalArgumentException("Passwords are not equal");
        }
    }

}
