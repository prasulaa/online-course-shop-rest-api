package pl.edu.pw.restapi.service;

import pl.edu.pw.restapi.dto.CourseLessonDTO;

public interface CourseLessonService {

    CourseLessonDTO getLesson(Long courseId, Long sectionId, Long lessonId);

}
