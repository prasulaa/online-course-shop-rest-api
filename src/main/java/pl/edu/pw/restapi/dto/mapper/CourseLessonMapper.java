package pl.edu.pw.restapi.dto.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.CourseLesson;
import pl.edu.pw.restapi.dto.CourseLessonDTO;
import pl.edu.pw.restapi.dto.CourseSectionLessonDTO;
import pl.edu.pw.restapi.dto.CreateCourseLessonDTO;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CourseLessonMapper {

    public CourseLessonDTO map(CourseLesson lesson) {
        if (lesson == null) {
            return null;
        } else {
            return CourseLessonDTO.builder()
                    .id(lesson.getId())
                    .name(lesson.getName())
                    .data(lesson.getData())
                    .build();
        }
    }

    public CourseLesson map(CreateCourseLessonDTO lesson) {
        if (lesson == null) {
            return null;
        } else {
            return CourseLesson.builder()
                    .name(lesson.getName())
                    .data(lesson.getData())
                    .build();
        }
    }

    public List<CourseSectionLessonDTO> mapToCourseSectionLessonDTO(List<CourseLesson> lessons) {
        if (lessons == null) {
            return null;
        } else {
            return lessons.stream()
                    .map(this::mapToCourseSectionLessonDTO)
                    .collect(Collectors.toList());
        }
    }

    public CourseSectionLessonDTO mapToCourseSectionLessonDTO(CourseLesson lesson) {
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
