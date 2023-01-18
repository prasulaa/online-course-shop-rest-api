package pl.edu.pw.restapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.edu.pw.restapi.domain.*;
import pl.edu.pw.restapi.dto.CourseLessonDTO;
import pl.edu.pw.restapi.dto.CreateCourseLessonDTO;
import pl.edu.pw.restapi.dto.UpdateCourseLessonDTO;
import pl.edu.pw.restapi.dto.mapper.CourseLessonMapper;
import pl.edu.pw.restapi.repository.CourseLessonRepository;
import pl.edu.pw.restapi.repository.CourseSectionRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseLessonServiceTest {

    @Spy
    private CourseLessonMapper courseLessonMapper;
    @Mock
    private CourseLessonRepository courseLessonRepository;
    @Mock
    private CourseSectionRepository courseSectionRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private CourseLessonServiceImpl courseLessonService;


    // Tests for method getLesson

    @Test
    public void shouldReturnLessonWhenUserAndLessonAreInRepository() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "Data123", "Name");
        CourseSection section = new CourseSection(1L, "Section", List.of(lesson));
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseLessonRepository.findCourseLessonByCourseIdAndSectionIdAndLessonIdAndUserId(course.getId(), section.getId(), lesson.getId(), user.getId()))
                .thenReturn(Optional.of(lesson));

        CourseLessonDTO actualLesson = courseLessonService.getLesson(course.getId(), section.getId(), lesson.getId(), user.getUsername());

        assertAll(
                () -> assertEquals(lesson.getId(), actualLesson.getId()),
                () -> assertEquals(lesson.getName(), actualLesson.getName()),
                () -> assertEquals(lesson.getData(), actualLesson.getData())
        );
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenGettingLessonAndUserDoesNotExists() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        String exceptionMsg = "Not found";

        when(userService.loadUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException(exceptionMsg));

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> courseLessonService.getLesson(1L, 1L, 1L, user.getUsername())
        );

        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenGettingLessonAndLessonDoesNotExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "Data123", "Name");
        CourseSection section = new CourseSection(1L, "Section", List.of(lesson));
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseLessonRepository.findCourseLessonByCourseIdAndSectionIdAndLessonIdAndUserId(course.getId(), section.getId(), lesson.getId(), user.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseLessonService.getLesson(1L, 1L, 1L, user.getUsername())
        );
        assertEquals("Lesson not found", exception.getMessage());
    }


    // Tests for method createLesson

    @Test
    public void shouldCreateLessonWhenUserAndCourseSectionExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "Data123", "Name");
        List<CourseLesson> lessons = new ArrayList<>();
        lessons.add(lesson);
        CourseSection section = new CourseSection(1L, "Section", lessons);
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");
        CreateCourseLessonDTO createLesson = new CreateCourseLessonDTO("Lesson name", "Data");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseSectionRepository.findReleasedCourseSectionByCourseIdAndSectionIdAndUserId(course.getId(), section.getId(), user.getId()))
                .thenReturn(Optional.of(section));

        CourseLessonDTO actCreatedLesson = courseLessonService.createLesson(course.getId(), section.getId(), createLesson, user.getUsername());

        verify(courseSectionRepository).save(any());
        verify(courseLessonRepository).save(any());
        assertAll(
                () -> assertEquals(createLesson.getName(), actCreatedLesson.getName()),
                () -> assertEquals(createLesson.getData(), actCreatedLesson.getData())
        );
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "Data123", "Name");
        CourseSection section = new CourseSection(1L, "Section", List.of(lesson));
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");
        CreateCourseLessonDTO createLesson = new CreateCourseLessonDTO("Lesson name", "Data");
        String exceptionMsg = "Not found";

        when(userService.loadUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException(exceptionMsg));

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> courseLessonService.createLesson(course.getId(), section.getId(), createLesson, user.getUsername())
        );
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenCreatingLessonAndCourseSectionDoesNotExists() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "Data123", "Name");
        CourseSection section = new CourseSection(1L, "Section", List.of(lesson));
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");
        CreateCourseLessonDTO createLesson = new CreateCourseLessonDTO("Lesson name", "Data");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseSectionRepository.findReleasedCourseSectionByCourseIdAndSectionIdAndUserId(course.getId(), section.getId(), user.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseLessonService.createLesson(course.getId(), section.getId(), createLesson, user.getUsername())
        );
        assertEquals("Course section not found", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenSectionAlreadyContainsLessonWithSameName() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "name", "data");
        CourseSection section = new CourseSection(1L, "Section", List.of(lesson));
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");
        CreateCourseLessonDTO createLesson = new CreateCourseLessonDTO(lesson.getName(), "Data");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseLessonRepository.findCourseLessonByCourseIdAndSectionIdAndName(course.getId(), section.getId(), lesson.getName()))
                .thenReturn(Optional.of(lesson));
        when(courseSectionRepository.findReleasedCourseSectionByCourseIdAndSectionIdAndUserId(course.getId(), section.getId(), user.getId()))
                .thenReturn(Optional.of(section));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> courseLessonService.createLesson(course.getId(), section.getId(), createLesson, user.getUsername())
        );
        assertEquals("Lesson " + lesson.getName() + " already exists", exception.getMessage());
    }


    // Tests for method updateLesson

    @Test
    public void shouldUpdateLessonWhenUserAndLessonExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "Data123", "Name");
        CourseSection section = new CourseSection(1L, "Section", List.of(lesson));
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");
        UpdateCourseLessonDTO updateLesson = new UpdateCourseLessonDTO("Lesson name", "Data");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseLessonRepository.findReleasedCourseLessonByCourseIdAndSectionIdAndLessonIdAndUserId(course.getId(), section.getId(), lesson.getId(), user.getId()))
                .thenReturn(Optional.of(lesson));

        CourseLessonDTO actUpdatedLesson = courseLessonService.updateLesson(course.getId(), section.getId(), lesson.getId(), updateLesson, user.getUsername());

        verify(courseLessonRepository).save(any());
        assertAll(
                () -> assertEquals(updateLesson.getName(), actUpdatedLesson.getName()),
                () -> assertEquals(updateLesson.getData(), actUpdatedLesson.getData())
        );
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenUpdatingLessonAndUserDoesNotExists() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "Data123", "Name");
        CourseSection section = new CourseSection(1L, "Section", List.of(lesson));
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");
        UpdateCourseLessonDTO updateLesson = new UpdateCourseLessonDTO("Lesson name", "Data");
        String exceptionMsg = "Not found";

        when(userService.loadUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException(exceptionMsg));

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> courseLessonService.updateLesson(course.getId(), section.getId(), lesson.getId(), updateLesson, user.getUsername())
        );
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenUpdatingLessonAndCourseSectionDoesNotExists() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "Data123", "Name");
        CourseSection section = new CourseSection(1L, "Section", List.of(lesson));
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");
        UpdateCourseLessonDTO updateLesson = new UpdateCourseLessonDTO("Lesson name", "Data");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseLessonRepository.findReleasedCourseLessonByCourseIdAndSectionIdAndLessonIdAndUserId(course.getId(), section.getId(), lesson.getId(), user.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseLessonService.updateLesson(course.getId(), section.getId(), lesson.getId(), updateLesson, user.getUsername())
        );
        assertEquals("Course lesson not found", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenUpdatingLessonAndSectionAlreadyContainsLessonWithSameName() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "Data123", "Name");
        CourseSection section = new CourseSection(1L, "Section", List.of(lesson));
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");
        UpdateCourseLessonDTO updateLesson = new UpdateCourseLessonDTO(lesson.getName(), "Data");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseLessonRepository.findReleasedCourseLessonByCourseIdAndSectionIdAndLessonIdAndUserId(course.getId(), section.getId(), lesson.getId(), user.getId()))
                .thenReturn(Optional.of(lesson));
        when(courseLessonRepository.findCourseLessonByCourseIdAndSectionIdAndName(course.getId(), section.getId(), lesson.getName()))
                .thenReturn(Optional.of(lesson));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> courseLessonService.updateLesson(course.getId(), section.getId(), lesson.getId(), updateLesson, user.getUsername())
        );
        assertEquals("Lesson " + updateLesson.getName() + " already exists", exception.getMessage());
    }

    // Tests for method deleteLesson
    //correct
    //username
    //sectionnotfound
    //lesson not found
    @Test
    public void shouldDeleteLessonWhenUserAndLessonExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "Data123", "Name");
        List<CourseLesson> lessons = new ArrayList<>();
        lessons.add(lesson);
        CourseSection section = new CourseSection(1L, "Section", lessons);
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseSectionRepository.findReleasedCourseSectionByCourseIdAndSectionIdAndUserId(course.getId(), section.getId(), user.getId()))
                .thenReturn(Optional.of(section));

        courseLessonService.deleteLesson(course.getId(), section.getId(), lesson.getId(), user.getUsername());

        verify(courseLessonRepository).deleteById(lesson.getId());
        verify(courseSectionRepository).save(any());
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenDeletingLessonAndUserDoesNotExists() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "Data123", "Name");
        CourseSection section = new CourseSection(1L, "Section", List.of(lesson));
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");
        String exceptionMsg = "Not found";

        when(userService.loadUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException(exceptionMsg));

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> courseLessonService.deleteLesson(course.getId(), section.getId(), lesson.getId(), user.getUsername())
        );
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenDeletingLessonAndCourseSectionDoesNotExists() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "Data123", "Name");
        CourseSection section = new CourseSection(1L, "Section", List.of(lesson));
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseSectionRepository.findReleasedCourseSectionByCourseIdAndSectionIdAndUserId(course.getId(), section.getId(), user.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseLessonService.deleteLesson(course.getId(), section.getId(), lesson.getId(), user.getUsername())
        );
        assertEquals("Course section not found", exception.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenDeletingLessonAndCourseLessonDoesNotExists() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        Long lessonId = 1L;
        CourseSection section = new CourseSection(1L, "Section", new ArrayList<>());
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseSectionRepository.findReleasedCourseSectionByCourseIdAndSectionIdAndUserId(course.getId(), section.getId(), user.getId()))
                .thenReturn(Optional.of(section));

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseLessonService.deleteLesson(course.getId(), section.getId(), lessonId, user.getUsername())
        );
        assertEquals("Course lesson not found", exception.getMessage());
    }

}
