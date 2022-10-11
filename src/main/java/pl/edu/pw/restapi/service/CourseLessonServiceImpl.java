package pl.edu.pw.restapi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.CourseLesson;
import pl.edu.pw.restapi.domain.CourseSection;
import pl.edu.pw.restapi.domain.User;
import pl.edu.pw.restapi.dto.CourseLessonDTO;
import pl.edu.pw.restapi.dto.CreateCourseLessonDTO;
import pl.edu.pw.restapi.dto.UpdateCourseLessonDTO;
import pl.edu.pw.restapi.dto.mapper.CourseLessonMapper;
import pl.edu.pw.restapi.repository.CourseLessonRepository;
import pl.edu.pw.restapi.repository.CourseSectionRepository;
import pl.edu.pw.restapi.repository.UserRepository;
import pl.edu.pw.restapi.service.updater.CourseLessonUpdater;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseLessonServiceImpl implements CourseLessonService {

    private final CourseLessonRepository courseLessonRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final UserService userService;
    private final CourseLessonMapper courseLessonMapper;


    @Override
    public CourseLessonDTO getLesson(Long courseId, Long sectionId, Long lessonId, String username) {
        User user = (User) userService.loadUserByUsername(username);

        CourseLesson lesson = courseLessonRepository
                .findCourseLessonByCourseIdAndSectionIdAndLessonIdAndUserId(courseId, sectionId, lessonId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        return courseLessonMapper.map(lesson);
    }

    @Override
    public CourseLessonDTO createLesson(Long courseId, Long sectionId, CreateCourseLessonDTO lesson, String username) {
        User user = (User) userService.loadUserByUsername(username);

        CourseLesson createdLesson = courseLessonMapper.map(lesson);

        CourseSection section = courseSectionRepository
                .findReleasedCourseSectionByCourseIdAndSectionIdAndUserId(courseId, sectionId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course section not found"));

        checkIfSectionContainsLessonWithSameName(courseId, sectionId, lesson.getName());
        saveCourseLesson(createdLesson, section);

        return courseLessonMapper.map(createdLesson);
    }

    @Override
    public CourseLessonDTO updateLesson(Long courseId, Long sectionId, Long lessonId,
                                        UpdateCourseLessonDTO lessonDTO, String username) {
        User user = (User) userService.loadUserByUsername(username);

        CourseLesson lesson = courseLessonRepository
                .findReleasedCourseLessonByCourseIdAndSectionIdAndLessonIdAndUserId(courseId, sectionId, lessonId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course lesson not found"));

        updateCourseLessonInDb(courseId, sectionId, lesson, lessonDTO);

        return courseLessonMapper.map(lesson);
    }

    @Override
    public void deleteLesson(Long courseId, Long sectionId, Long lessonId, String username) {
        User user = (User) userService.loadUserByUsername(username);

        CourseSection section = courseSectionRepository
                .findReleasedCourseSectionByCourseIdAndSectionIdAndUserId(courseId, sectionId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course section not found"));

        deleteLessonFromSectionInDb(section, lessonId);
    }

    private void deleteLessonFromSectionInDb(CourseSection section, Long lessonId) {
        if (section.getLessons().removeIf(l -> l.getId().equals(lessonId))) {
            courseSectionRepository.save(section);
            courseLessonRepository.deleteById(lessonId);
        } else {
            throw new EntityNotFoundException("Course lesson not found");
        }
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
