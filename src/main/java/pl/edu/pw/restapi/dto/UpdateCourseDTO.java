package pl.edu.pw.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.pw.restapi.domain.CourseDifficulty;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseDTO {

    private String title;
    private String thumbnail;
    private Double price;
    private List<Long> categories;
    private CourseDifficulty difficulty;
    private List<@NotEmpty(message = "Scope cannot be empty") String> scopes;
    private String description;

}
