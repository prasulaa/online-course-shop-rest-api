package pl.edu.pw.restapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.edu.pw.restapi.domain.*;
import pl.edu.pw.restapi.dto.CourseDTO;
import pl.edu.pw.restapi.dto.CourseDetailsDTO;
import pl.edu.pw.restapi.dto.CreateCourseDTO;
import pl.edu.pw.restapi.dto.UpdateCourseDTO;
import pl.edu.pw.restapi.dto.mapper.CourseCategoryMapper;
import pl.edu.pw.restapi.dto.mapper.CourseLessonMapper;
import pl.edu.pw.restapi.dto.mapper.CourseMapper;
import pl.edu.pw.restapi.dto.mapper.CourseSectionMapper;
import pl.edu.pw.restapi.repository.CourseRepository;
import pl.edu.pw.restapi.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    private final CourseCategoryMapper courseCategoryMapper = new CourseCategoryMapper();
    private final CourseLessonMapper courseLessonMapper = new CourseLessonMapper();
    private final CourseSectionMapper courseSectionMapper = new CourseSectionMapper(courseLessonMapper);

    @Spy
    private CourseMapper courseMapper = new CourseMapper(courseCategoryMapper, courseSectionMapper);
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CourseCategoryService courseCategoryService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private CourseServiceImpl courseService;


    // Tests for method getCourses

    @Test
    public void shouldReturnMappedCoursesFromRepository() {
        Course course1 = new Course(1L, "Name1", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(), "thumbnail1");
        Course course2 = new Course(2L, "Name2", 10.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(), "thumbnail2");
        List<Course> courses = List.of(course1, course2);

        when(courseRepository.findAll(any(), any(), any(), any(), any(), any()))
                .thenReturn(courses);

        List<CourseDTO> actualCourses = courseService.getCourses(null, null, null, null, null, null, null, null);

        assertAll(
                () -> assertEquals(courses.size(), actualCourses.size()),
                () -> assertEqualsCourse(course1, actualCourses.get(0)),
                () -> assertEqualsCourse(course2, actualCourses.get(1))
        );
    }

    @Test
    public void shouldReturnEmptyListWhenNoCourseIsInRepository() {
        List<Course> courses = List.of();

        when(courseRepository.findAll(any(), any(), any(), any(), any(), any()))
                .thenReturn(courses);

        List<CourseDTO> actualCourses = courseService.getCourses(null, null, null, null, null, null, null, null);

        assertEquals(0, actualCourses.size());
    }


    // Tests for method getCourseDetails

    @Test
    public void shouldReturnCourseDetailsWhenUserAndCourseExist() {
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(), "thumbnail1");

        when(courseRepository.findById(course.getId()))
                .thenReturn(Optional.of(course));

        CourseDetailsDTO actualCourse = courseService.getCourseDetails(course.getId());

        assertAll(
                () -> assertEquals(course.getId(), actualCourse.getId()),
                () -> assertEquals(course.getTitle(), actualCourse.getTitle()),
                () -> assertEquals(course.getThumbnail(), actualCourse.getThumbnail()),
                () -> assertEquals(course.getPrice(), actualCourse.getPrice()),
                () -> assertEquals(course.getDifficulty(), actualCourse.getDifficulty()),
                () -> assertEquals(course.getScopes(), actualCourse.getScopes()),
                () -> assertEquals(course.getDescription(), actualCourse.getDescription())
        );
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenGettingCourseAndCourseDoesNotExist() {
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(), "thumbnail1");

        when(courseRepository.findById(course.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseService.getCourseDetails(course.getId())
        );
        assertEquals("Course not found", exception.getMessage());
    }


    // Tests for method getBoughtCourses

    @Test
    public void shouldReturnBoughtCoursesWhenUserIsInRepository() {
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(), "thumbnail1");
        List<Course> boughtCourses = List.of(course);
        User user = new User(1L, "username", "password", List.of(), boughtCourses);

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findBoughtCoursesByUserId(user.getId()))
                .thenReturn(boughtCourses);

        List<CourseDTO> actualCourses = courseService.getBoughtCourses(user.getUsername());

        assertAll(
                () -> assertEquals(1, actualCourses.size()),
                () -> assertEqualsCourse(course, actualCourses.get(0))
        );
    }

    @Test
    public void shouldReturnEmptyListWhenUserDoesNotHaveBoughtCourses() {
        List<Course> boughtCourses = List.of();
        User user = new User(1L, "username", "password", List.of(), boughtCourses);

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findBoughtCoursesByUserId(user.getId()))
                .thenReturn(boughtCourses);

        List<CourseDTO> actualCourses = courseService.getBoughtCourses(user.getUsername());

        assertEquals(0, actualCourses.size());
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenGettingBoughtCoursesAndUserDoesNotExist() {
        User user = new User(1L, "username", "password", List.of(), List.of());

        when(userService.loadUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException(""));

        assertThrows(
                UsernameNotFoundException.class,
                () -> courseService.getBoughtCourses(user.getUsername())
        );
    }


    // Tests for method getReleasedCourses

    @Test
    public void shouldReturnReleasedCoursesWhenUserIsInRepository() {
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(), "thumbnail1");
        List<Course> releasedCourses = List.of(course);
        User user = new User(1L, "username", "password", List.of(), releasedCourses);

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCoursesByUserId(user.getId()))
                .thenReturn(releasedCourses);

        List<CourseDTO> actualCourses = courseService.getReleasedCourses(user.getUsername());

        assertAll(
                () -> assertEquals(1, actualCourses.size()),
                () -> assertEqualsCourse(course, actualCourses.get(0))
        );
    }

    @Test
    public void shouldReturnEmptyListWhenUserDoesNotHaveReleasedCourses() {
        List<Course> releasedCourses = List.of();
        User user = new User(1L, "username", "password", List.of(), releasedCourses);

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCoursesByUserId(user.getId()))
                .thenReturn(releasedCourses);

        List<CourseDTO> actualCourses = courseService.getReleasedCourses(user.getUsername());

        assertEquals(0, actualCourses.size());
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenGettingReleasedCoursesAndUserDoesNotExist() {
        User user = new User(1L, "username", "password", List.of(), List.of());

        when(userService.loadUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException(""));

        assertThrows(
                UsernameNotFoundException.class,
                () -> courseService.getReleasedCourses(user.getUsername())
        );
    }


    // Tests for method createCourse

    @Test
    public void shouldCreateCourseWhenUserIsInRepository() {
        CreateCourseDTO course = new CreateCourseDTO("Name", "thumbnail", 10.0, List.of(), CourseDifficulty.MEDIUM, List.of(), "Description");
        List<Course> releasedCoursesMock = Mockito.mock(List.class);
        User user = new User(1L, "username", "password", List.of(), releasedCoursesMock);
        List<CourseCategory> categories = List.of(new CourseCategory(1L, "category", null));

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseCategoryService.getCategories(any()))
                .thenReturn(categories);

        CourseDetailsDTO createdCourse = courseService.createCourse(course, user.getUsername());

        verify(releasedCoursesMock).add(any());
        verify(userRepository).save(user);
        verify(courseRepository).save(any());
        assertAll(
                () -> assertEquals(course.getTitle(), createdCourse.getTitle()),
                () -> assertEquals(course.getThumbnail(), createdCourse.getThumbnail()),
                () -> assertEquals(course.getPrice(), createdCourse.getPrice()),
                () -> assertEquals(course.getDifficulty(), createdCourse.getDifficulty()),
                () -> assertEquals(course.getScopes(), createdCourse.getScopes()),
                () -> assertEquals(course.getDescription(), createdCourse.getDescription())
        );
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenCreatingCourseAndUserDoesNotExist() {
        CreateCourseDTO course = new CreateCourseDTO("Name", "thumbnail", 10.0, List.of(), CourseDifficulty.MEDIUM, List.of(), "Description");
        List<Course> releasedCoursesMock = Mockito.mock(List.class);
        User user = new User(1L, "username", "password", List.of(), new ArrayList<>());
        List<CourseCategory> categories = List.of(new CourseCategory(1L, "category", null));

        when(userService.loadUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException(""));

        assertThrows(
                UsernameNotFoundException.class,
                () -> courseService.createCourse(course, user.getUsername())
        );
    }


    // Tests for method updateCourse

    @Test
    public void shouldUpdateCourseWhenUserAndCourseIsInRepository() {
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(), "thumbnail1");
        User user = new User(1L, "username", "password", List.of(), List.of());
        CourseCategory category = new CourseCategory(1L, "category", null);
        UpdateCourseDTO updateCourse = new UpdateCourseDTO("Title", "thumbnail", 5.0, List.of(category.getId()), CourseDifficulty.MEDIUM, List.of("Scope1"), "description");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.of(course));
        when(courseCategoryService.getCategories(any()))
                .thenReturn(List.of(category));

        CourseDetailsDTO actualCourse = courseService.updateCourse(updateCourse, course.getId(), user.getUsername());

        verify(courseRepository).save(any());
        assertAll(
                () -> assertEquals(course.getId(), actualCourse.getId()),
                () -> assertEquals(updateCourse.getTitle(), actualCourse.getTitle()),
                () -> assertEquals(updateCourse.getThumbnail(), actualCourse.getThumbnail()),
                () -> assertEquals(updateCourse.getPrice(), actualCourse.getPrice()),
                () -> assertEquals(updateCourse.getDifficulty(), actualCourse.getDifficulty()),
                () -> assertEquals(updateCourse.getScopes(), actualCourse.getScopes()),
                () -> assertEquals(updateCourse.getDescription(), actualCourse.getDescription()),
                () -> assertEquals(1, actualCourse.getCategories().size()),
                () -> assertEquals(category.getCategory(), actualCourse.getCategories().get(0))
        );
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenUpdatingCourseAndUserDoesNotExist() {
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(), "thumbnail1");
        User user = new User(1L, "username", "password", List.of(), List.of());
        CourseCategory category = new CourseCategory(1L, "category", null);
        UpdateCourseDTO updateCourse = new UpdateCourseDTO("Title", "thumbnail", 5.0, List.of(category.getId()), CourseDifficulty.MEDIUM, List.of("Scope1"), "description");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException(""));

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> courseService.updateCourse(updateCourse, course.getId(), user.getUsername())
        );
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenUpdatingCourseAndCourseDoesNotExist() {
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(), "thumbnail1");
        User user = new User(1L, "username", "password", List.of(), List.of());
        CourseCategory category = new CourseCategory(1L, "category", null);
        UpdateCourseDTO updateCourse = new UpdateCourseDTO("Title", "thumbnail", 5.0, List.of(category.getId()), CourseDifficulty.MEDIUM, List.of("Scope1"), "description");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseService.updateCourse(updateCourse, course.getId(), user.getUsername())
        );
        assertEquals("Course not found", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenUpdatingCourseAndCategoryDoesNotExist() {
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(), "thumbnail1");
        User user = new User(1L, "username", "password", List.of(), List.of());
        UpdateCourseDTO updateCourse = new UpdateCourseDTO("Title", "thumbnail", 5.0, List.of(1L), CourseDifficulty.MEDIUM, List.of("Scope1"), "description");

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.of(course));
        when(courseCategoryService.getCategories(any()))
                .thenThrow(new IllegalArgumentException("Category not found"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> courseService.updateCourse(updateCourse, course.getId(), user.getUsername())
        );
        assertEquals("Category not found", exception.getMessage());
    }


    // Tests for method deleteCourse

    @Test
    public void shouldDeleteCourseWhenCourseIsInRepository() {
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(), "thumbnail1");
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        User user = new User(1L, "username", "password", List.of(), courses);

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.of(course));

        courseService.deleteCourse(course.getId(), user.getUsername());

        verify(userRepository).save(user);
        verify(courseRepository).delete(course);
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(), "thumbnail1");
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        User user = new User(1L, "username", "password", List.of(), courses);

        when(userService.loadUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException(""));

        assertThrows(
                UsernameNotFoundException.class,
                () -> courseService.deleteCourse(course.getId(), user.getUsername())
        );
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenDeletingCourseAndCourseDoesNotExist() {
        Course course = new Course(1L, "Name", 0.0, List.of(), CourseDifficulty.EASY, List.of(), "", List.of(), "thumbnail1");
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        User user = new User(1L, "username", "password", List.of(), courses);

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseService.deleteCourse(course.getId(), user.getUsername())
        );
        assertEquals("Course not found", exception.getMessage());
    }

    private void assertEqualsCourse(Course expected, CourseDTO actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getTitle(), actual.getTitle()),
                () -> assertEquals(expected.getThumbnail(), actual.getThumbnail()),
                () -> assertEquals(expected.getPrice(), actual.getPrice())
        );
    }

}
