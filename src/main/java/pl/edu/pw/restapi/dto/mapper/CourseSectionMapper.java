package pl.edu.pw.restapi.dto.mapper;

import pl.edu.pw.restapi.domain.CourseSection;
import pl.edu.pw.restapi.dto.CourseSectionDTO;
import pl.edu.pw.restapi.dto.CreateCourseSectionDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseSectionMapper {

    public static List<CourseSectionDTO> map(List<CourseSection> sections) {
        if (sections == null) {
            return null;
        } else {
            return sections.stream()
                    .map(CourseSectionMapper::map)
                    .collect(Collectors.toList());
        }
    }

    public static CourseSectionDTO map(CourseSection section) {
        if (section == null) {
            return null;
        } else {
            return CourseSectionDTO.builder()
                    .id(section.getId())
                    .name(section.getName())
                    .lessons(CourseLessonMapper.mapToCourseSectionLessonDTO(section.getLessons()))
                    .build();
        }
    }

    public static CourseSection map(CreateCourseSectionDTO section) {
        if (section == null) {
            return null;
        } else {
            return CourseSection.builder()
                    .name(section.getName())
                    .lessons(new ArrayList<>())
                    .build();
        }
    }

}
