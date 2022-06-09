package pl.edu.pw.restapi.service;

import pl.edu.pw.restapi.dto.CourseLessonDTO;
import pl.edu.pw.restapi.dto.CreateCourseLessonDTO;
import pl.edu.pw.restapi.dto.UpdateCourseLessonDTO;

public interface CourseLessonService {

    CourseLessonDTO getLesson(Long courseId, Long sectionId, Long lessonId);

    CourseLessonDTO createLesson(Long courseId, Long sectionId, CreateCourseLessonDTO lesson);

    CourseLessonDTO updateLesson(Long courseId, Long sectionId, Long lessonId, UpdateCourseLessonDTO lessonDTO);

    void deleteLesson(Long courseId, Long sectionId, Long lessonId);
}
