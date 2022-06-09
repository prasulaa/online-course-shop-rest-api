package pl.edu.pw.restapi.service;

import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.CourseLesson;
import pl.edu.pw.restapi.domain.CourseSection;
import pl.edu.pw.restapi.dto.CourseLessonDTO;
import pl.edu.pw.restapi.dto.CreateCourseLessonDTO;
import pl.edu.pw.restapi.dto.UpdateCourseLessonDTO;
import pl.edu.pw.restapi.dto.mapper.CourseLessonMapper;
import pl.edu.pw.restapi.repository.CourseLessonRepository;
import pl.edu.pw.restapi.repository.CourseSectionRepository;
import pl.edu.pw.restapi.service.updater.CourseLessonUpdater;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CourseLessonServiceImpl implements CourseLessonService {

    private final CourseLessonRepository courseLessonRepository;
    private final CourseSectionRepository courseSectionRepository;

    public CourseLessonServiceImpl(CourseLessonRepository courseLessonRepository, CourseSectionRepository courseSectionRepository) {
        this.courseLessonRepository = courseLessonRepository;
        this.courseSectionRepository = courseSectionRepository;
    }

    @Override
    public CourseLessonDTO getLesson(Long courseId, Long sectionId, Long lessonId) {
        CourseLesson lesson = courseLessonRepository
                .findCourseLessonByCourseIdAndSectionIdAndLessonId(courseId, sectionId, lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        return CourseLessonMapper.map(lesson);
    }

    @Override
    public CourseLessonDTO createLesson(Long courseId, Long sectionId, CreateCourseLessonDTO lesson) {
        CourseLesson createdLesson = CourseLessonMapper.map(lesson);

        CourseSection section = courseSectionRepository.findByCourseIdAndSectionId(courseId, sectionId)
                .orElseThrow(() -> new EntityNotFoundException("Course section not found"));

        checkIfSectionContainsLessonWithSameName(courseId, sectionId, lesson.getName());
        saveCourseLesson(createdLesson, section);

        return CourseLessonMapper.map(createdLesson);
    }

    @Override
    public CourseLessonDTO updateLesson(Long courseId, Long sectionId, Long lessonId, UpdateCourseLessonDTO lessonDTO) {
        CourseLesson lesson = courseLessonRepository
                .findCourseLessonByCourseIdAndSectionIdAndLessonId(courseId, sectionId, lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Course lesson not found"));

        updateCourseLessonInDb(courseId, sectionId, lesson, lessonDTO);

        return CourseLessonMapper.map(lesson);
    }

    private void updateCourseLessonInDb(Long courseId, Long sectionId,
                                        CourseLesson lessonToUpdate, UpdateCourseLessonDTO lesson) {
        checkIfSectionContainsLessonWithSameName(courseId, sectionId, lesson.getName());

        new CourseLessonUpdater().update(lessonToUpdate, lesson);

        courseLessonRepository.save(lessonToUpdate);
    }

    private void checkIfSectionContainsLessonWithSameName(Long courseId, Long sectionId, String name) {
        if (name != null && !name.isEmpty()) {
            Optional<CourseLesson> lessonOpt = courseLessonRepository.findCourseLessonByCourseIdAndSectionIdAndName(
                    courseId,
                    sectionId,
                    name);

            if (lessonOpt.isPresent()) {
                throw new IllegalArgumentException("Lesson " + name + " already exists");
            }
        }
    }

    @Transactional
    private void saveCourseLesson(CourseLesson lesson, CourseSection section) {
        section.getLessons().add(lesson);

        courseLessonRepository.save(lesson);
        courseSectionRepository.save(section);
    }
}
