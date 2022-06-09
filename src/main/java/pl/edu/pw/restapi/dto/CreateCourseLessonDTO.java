package pl.edu.pw.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CreateCourseLessonDTO {

    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotEmpty(message = "Data cannot be empty")
    private String data;

}
