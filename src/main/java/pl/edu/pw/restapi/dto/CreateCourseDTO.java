package pl.edu.pw.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.pw.restapi.domain.CourseDifficulty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseDTO {

    @NotEmpty(message = "Title cannot be empty")
    private String title;
    @NotEmpty(message = "Thumbnail cannot be empty")
    private String thumbnail;
    @PositiveOrZero(message = "Price cannot be negative")
    @NotNull(message = "Price cannot be empty")
    private Double price;
    @NotEmpty(message = "Categories cannot be empty")
    private List<Long> categories;
    @NotNull(message = "Difficulty cannot be empty")
    private CourseDifficulty difficulty;
    @NotEmpty(message = "Scopes cannot be empty")
    private List<@NotEmpty(message = "Scope cannot be empty") String> scopes;
    @NotEmpty(message = "Description cannot be empty")
    private String description;

}
