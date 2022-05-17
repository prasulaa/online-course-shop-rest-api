package pl.edu.pw.restapi.dto.mapper;

import pl.edu.pw.restapi.domain.CourseLesson;
import pl.edu.pw.restapi.dto.CourseDetailsLessonDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CourseLessonMapper {

    public static List<CourseDetailsLessonDTO> map(List<CourseLesson> lessons) {
        if (lessons == null) {
            return null;
        } else {
            return lessons.stream()
                    .map(CourseLessonMapper::map)
                    .collect(Collectors.toList());
        }
    }

    public static CourseDetailsLessonDTO map(CourseLesson lesson) {
        if (lesson == null) {
            return null;
        } else {
            return CourseDetailsLessonDTO.builder()
                    .id(lesson.getId())
                    .name(lesson.getName())
                    .build();
        }
    }

}
