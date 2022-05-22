package pl.edu.pw.restapi.dto.mapper;

import pl.edu.pw.restapi.domain.CourseLesson;
import pl.edu.pw.restapi.dto.CourseSectionLessonDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CourseLessonMapper {

    public static List<CourseSectionLessonDTO> map(List<CourseLesson> lessons) {
        if (lessons == null) {
            return null;
        } else {
            return lessons.stream()
                    .map(CourseLessonMapper::map)
                    .collect(Collectors.toList());
        }
    }

    public static CourseSectionLessonDTO map(CourseLesson lesson) {
        if (lesson == null) {
            return null;
        } else {
            return CourseSectionLessonDTO.builder()
                    .id(lesson.getId())
                    .name(lesson.getName())
                    .build();
        }
    }

}
