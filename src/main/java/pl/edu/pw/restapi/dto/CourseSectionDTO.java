package pl.edu.pw.restapi.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CourseSectionDTO {

    private Long id;
    private String name;
    private List<CourseSectionLessonDTO> lessons;

}
