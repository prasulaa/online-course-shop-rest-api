package pl.edu.pw.restapi.service.validator;

import org.passay.*;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.dto.RegisterCredentialsDTO;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class RegisterCredentialsValidator {

    public void validate(RegisterCredentialsDTO credentials) {
        validateEmail(credentials.getEmail());
        validatePasswords(credentials.getPassword(), credentials.getPasswordRepeat());
    }

    private void validateEmail(String email) {
        String regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!Pattern.compile(regexPattern).matcher(email).matches()) {
            throw new IllegalArgumentException("Email is not valid");
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
