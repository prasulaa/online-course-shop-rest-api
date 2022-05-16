package pl.edu.pw.restapi.dto;

import lombok.Builder;
import lombok.Getter;
import pl.edu.pw.restapi.domain.CourseDifficulty;

import java.util.List;

@Builder
@Getter
public class CourseDetailsDTO {

    private Long id;
    private String title;
    private Double price;
    private List<String> categories;
    private CourseDifficulty difficulty;
    private List<String> scopes;
    private String description;
    private List<CourseDetailsSectionDTO> sections;

}
