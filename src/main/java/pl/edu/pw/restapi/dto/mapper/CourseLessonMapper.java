package pl.edu.pw.restapi.dto.mapper;

import pl.edu.pw.restapi.domain.CourseLesson;
import pl.edu.pw.restapi.dto.CourseDetailsLessonDTO;

import java.util.ArrayList;
import java.util.List;

public class CourseLessonMapper {

    public static List<CourseDetailsLessonDTO> map(List<CourseLesson> lessons) {
        if (lessons == null) {
            return null;
        } else {
            List<CourseDetailsLessonDTO> mappedLessons = new ArrayList<>();

            for (CourseLesson l : lessons) {
                mappedLessons.add(map(l));
            }

            return mappedLessons;
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
