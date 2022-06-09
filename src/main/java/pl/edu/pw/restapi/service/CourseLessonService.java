package pl.edu.pw.restapi.service;

import pl.edu.pw.restapi.dto.CourseLessonDTO;
import pl.edu.pw.restapi.dto.CreateCourseLessonDTO;

public interface CourseLessonService {

    CourseLessonDTO getLesson(Long courseId, Long sectionId, Long lessonId);

    CourseLessonDTO createLesson(Long courseId, Long sectionId, CreateCourseLessonDTO lesson);

}
