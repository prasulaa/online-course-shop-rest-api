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
public class ChangePasswordDTO {

    @NotEmpty(message = "Old password cannot be empty")
    private String oldPassword;
    @NotEmpty(message = "New password cannot be empty")
    private String newPassword;
    @NotEmpty(message = "Repeated new password cannot be empty")
    private String newPasswordRepeat;

}
