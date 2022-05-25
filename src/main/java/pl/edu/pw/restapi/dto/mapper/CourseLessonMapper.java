package pl.edu.pw.restapi.dto.mapper;

import pl.edu.pw.restapi.domain.CourseLesson;
import pl.edu.pw.restapi.dto.CourseLessonDTO;
import pl.edu.pw.restapi.dto.CourseSectionLessonDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CourseLessonMapper {

    public static CourseLessonDTO map(CourseLesson lesson) {
        if (lesson == null) {
            return null;
        } else {
            return CourseLessonDTO.builder()
                    .name(lesson.getName())
                    .data(lesson.getData())
                    .build();
        }
    }

    public static List<CourseSectionLessonDTO> mapToCourseSectionLessonDTO(List<CourseLesson> lessons) {
        if (lessons == null) {
            return null;
        } else {
            return lessons.stream()
                    .map(CourseLessonMapper::mapToCourseSectionLessonDTO)
                    .collect(Collectors.toList());
        }
    }

    public static CourseSectionLessonDTO mapToCourseSectionLessonDTO(CourseLesson lesson) {
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
