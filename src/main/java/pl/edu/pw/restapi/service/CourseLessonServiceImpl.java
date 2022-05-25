package pl.edu.pw.restapi.service;

import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.CourseLesson;
import pl.edu.pw.restapi.dto.CourseLessonDTO;
import pl.edu.pw.restapi.dto.mapper.CourseLessonMapper;
import pl.edu.pw.restapi.repository.CourseLessonRepository;

import javax.persistence.EntityNotFoundException;

@Service
public class CourseLessonServiceImpl implements CourseLessonService {

    private final CourseLessonRepository courseLessonRepository;

    public CourseLessonServiceImpl(CourseLessonRepository courseLessonRepository) {
        this.courseLessonRepository = courseLessonRepository;
    }

    @Override
    public CourseLessonDTO getLesson(Long courseId, Long sectionId, Long lessonId) {
        CourseLesson lesson = courseLessonRepository
                .getCourseLessonByCourseIdAndSectionIdAndLessonId(courseId, sectionId, lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        return CourseLessonMapper.map(lesson);
    }

}
