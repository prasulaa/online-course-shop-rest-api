package pl.edu.pw.restapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.edu.pw.restapi.domain.*;
import pl.edu.pw.restapi.dto.*;
import pl.edu.pw.restapi.dto.mapper.CourseLessonMapper;
import pl.edu.pw.restapi.dto.mapper.CourseSectionMapper;
import pl.edu.pw.restapi.repository.CourseRepository;
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
public class CourseSectionServiceTest {

    private final CourseLessonMapper courseLessonMapper = new CourseLessonMapper();
    @Spy
    private CourseSectionMapper courseSectionMapper = new CourseSectionMapper(courseLessonMapper);
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CourseSectionRepository courseSectionRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private CourseSectionServiceImpl courseSectionService;


    // Tests for method getCourseSection

    @Test
    public void shouldReturnCourseSectionWhenSectionAndUserExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "Data123", "Name");
        CourseSection section = new CourseSection(1L, "Section", List.of(lesson));
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseSectionRepository.findByCourseIdAndSectionIdAndUserId(course.getId(), section.getId(), user.getId()))
                .thenReturn(Optional.of(section));

        CourseSectionDTO actualSection = courseSectionService.getCourseSection(course.getId(), section.getId(), user.getUsername());
        CourseSectionLessonDTO actualLesson = actualSection.getLessons().get(0);

        assertAll(
                () -> assertEquals(section.getId(), actualSection.getId()),
                () -> assertEquals(section.getName(), actualSection.getName()),
                () -> assertEquals(section.getLessons().size(), actualSection.getLessons().size()),
                () -> assertEquals(lesson.getId(), actualLesson.getId()),
                () -> assertEquals(lesson.getName(), actualLesson.getName())
        );
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenGettingSectionAndUserDoesNotExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "Data123", "Name");
        CourseSection section = new CourseSection(1L, "Section", List.of(lesson));
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");
        String exceptionMsg = "Not found";

        when(userService.loadUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException(exceptionMsg));

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> courseSectionService.getCourseSection(course.getId(), section.getId(), user.getUsername())
        );
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenGettingSectionAndSectionDoesNotExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseLesson lesson = new CourseLesson(1L, "Data123", "Name");
        CourseSection section = new CourseSection(1L, "Section", List.of(lesson));
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseSectionRepository.findByCourseIdAndSectionIdAndUserId(course.getId(), section.getId(), user.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseSectionService.getCourseSection(course.getId(), section.getId(), user.getUsername())
        );
        assertEquals("Course section not found", exception.getMessage());
    }


    // Tests for method createCourseSection

    @Test
    public void shouldCreateSectionWhenCourseAndUserExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CreateCourseSectionDTO section = new CreateCourseSectionDTO("Section");
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", new ArrayList<>(), "");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.of(course));

        CourseSectionDTO createdSection = courseSectionService.createCourseSection(course.getId(), section, user.getUsername());

        verify(courseSectionRepository).save(any());
        verify(courseRepository).save(any());
        assertEquals(section.getName(), createdSection.getName());
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenCreatingSectionAndUserDoesNotExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CreateCourseSectionDTO section = new CreateCourseSectionDTO("Section");
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", new ArrayList<>(), "");
        String exceptionMsg = "Not found";

        when(userService.loadUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException(exceptionMsg));

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> courseSectionService.createCourseSection(course.getId(), section, user.getUsername())
        );
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenCreatingSectionAndCourseDoesNotExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CreateCourseSectionDTO section = new CreateCourseSectionDTO("Section");
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", new ArrayList<>(), "");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseSectionService.createCourseSection(course.getId(), section, user.getUsername())
        );
        assertEquals("Course not found", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenCreatingSectionAndSectionWithSameNameAlreadyExists() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseSection section = new CourseSection(1L, "Section", List.of());
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");
        CreateCourseSectionDTO createSection = new CreateCourseSectionDTO(section.getName());

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.of(course));
        when(courseSectionRepository.findByCourseIdAndName(course.getId(), section.getName()))
                .thenReturn(Optional.of(section));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> courseSectionService.createCourseSection(course.getId(), createSection, user.getUsername())
        );
        assertEquals("Section " + section.getName() + " already exists", exception.getMessage());
    }


    // Tests for method updateCourseSection

    @Test
    public void shouldUpdateSectionWhenSectionAndUserExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseSection section = new CourseSection(1L, "Section", List.of());
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");
        UpdateCourseSectionDTO updateSection = new UpdateCourseSectionDTO("Section");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseSectionRepository.findReleasedCourseSectionByCourseIdAndSectionIdAndUserId(course.getId(), section.getId(), user.getId()))
                .thenReturn(Optional.of(section));

        CourseSectionDTO updatedSection = courseSectionService.updateCourseSection(course.getId(), section.getId(), updateSection, user.getUsername());

        verify(courseSectionRepository).save(any());
        assertEquals(section.getName(), updatedSection.getName());
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenUpdatingSectionAndUserDoesNotExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseSection section = new CourseSection(1L, "Section", List.of());
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");
        UpdateCourseSectionDTO updateSection = new UpdateCourseSectionDTO("Section");
        String exceptionMsg = "Not found";

        when(userService.loadUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException(exceptionMsg));

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> courseSectionService.updateCourseSection(course.getId(), section.getId(), updateSection, user.getUsername())
        );
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenUpdatingSectionAndCourseDoesNotExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseSection section = new CourseSection(1L, "Section", List.of());
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");
        UpdateCourseSectionDTO updateSection = new UpdateCourseSectionDTO("Section");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseSectionRepository.findReleasedCourseSectionByCourseIdAndSectionIdAndUserId(course.getId(), section.getId(), user.getId()))
                .thenReturn(Optional.empty());


        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseSectionService.updateCourseSection(course.getId(), section.getId(), updateSection, user.getUsername())
        );
        assertEquals("Course section not found", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenUpdatingSectionAndSectionWithSameNameAlreadyExists() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseSection section = new CourseSection(1L, "Section", List.of());
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(section), "");
        UpdateCourseSectionDTO updateSection = new UpdateCourseSectionDTO(section.getName());

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseSectionRepository.findReleasedCourseSectionByCourseIdAndSectionIdAndUserId(course.getId(), section.getId(), user.getId()))
                .thenReturn(Optional.of(section));
        when(courseSectionRepository.findByCourseIdAndName(course.getId(), section.getName()))
                .thenReturn(Optional.of(section));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> courseSectionService.updateCourseSection(course.getId(), section.getId(), updateSection, user.getUsername())
        );
        assertEquals("Section " + section.getName() + " already exists", exception.getMessage());
    }


    // Tests for method deleteCourseSection

    @Test
    public void shouldDeleteSectionWhenSectionAndUserExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseSection section = new CourseSection(1L, "Section", List.of());
        List<CourseSection> sections = new ArrayList<>();
        sections.add(section);
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", sections, "");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.of(course));

        courseSectionService.deleteCourseSection(course.getId(), section.getId(), user.getUsername());

        verify(courseRepository).save(any());
        verify(courseSectionRepository).deleteById(section.getId());
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenDeletingSectionAndUserDoesNotExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseSection section = new CourseSection(1L, "Section", List.of());
        List<CourseSection> sections = new ArrayList<>();
        sections.add(section);
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", sections, "");
        String exceptionMsg = "Not found";

        when(userService.loadUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException(exceptionMsg));

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> courseSectionService.deleteCourseSection(course.getId(), section.getId(), user.getUsername())
        );
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenDeletingSectionAndCourseDoesNotExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseSection section = new CourseSection(1L, "Section", List.of());
        List<CourseSection> sections = new ArrayList<>();
        sections.add(section);
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", sections, "");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseSectionService.deleteCourseSection(course.getId(), section.getId(), user.getUsername())
        );
        assertEquals("Course not found", exception.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenDeletingSectionAndSectionDoesNotExist() {
        CourseUser user = new CourseUser(1L, "username", "password", "email", List.of(), List.of());
        CourseSection section = new CourseSection(1L, "Section", List.of());
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", new ArrayList<>(), "");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.of(course));

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseSectionService.deleteCourseSection(course.getId(), section.getId(), user.getUsername())
        );
        assertEquals("Course section not found", exception.getMessage());
    }

}
