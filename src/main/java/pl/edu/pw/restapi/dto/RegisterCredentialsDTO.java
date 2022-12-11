package pl.edu.pw.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCredentialsDTO {

    @NotEmpty(message = "Username cannot be empty")
    private String username;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    @NotEmpty(message = "Repeated password cannot be empty")
    private String passwordRepeat;
    @NotEmpty(message = "Email cannot be empty")
    private String email;

}
