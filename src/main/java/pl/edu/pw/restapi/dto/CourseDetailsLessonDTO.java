package pl.edu.pw.restapi.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CourseDetailsLessonDTO {

    private Long id;
    private String name;

}
