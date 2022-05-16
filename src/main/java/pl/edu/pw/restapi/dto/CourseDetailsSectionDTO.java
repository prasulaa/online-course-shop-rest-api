package pl.edu.pw.restapi.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CourseDetailsSectionDTO {

    private Long id;
    private String name;
    private List<CourseDetailsLessonDTO> lessons;

}
