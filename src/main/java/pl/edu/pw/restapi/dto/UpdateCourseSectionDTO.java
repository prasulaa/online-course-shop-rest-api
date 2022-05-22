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
public class UpdateCourseSectionDTO {

    @NotEmpty(message = "Section name cannot be empty")
    private String name;

}
