package pl.edu.pw.restapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pw.restapi.domain.Course;
import pl.edu.pw.restapi.domain.CourseFile;
import pl.edu.pw.restapi.domain.User;
import pl.edu.pw.restapi.dto.CourseFileDTO;
import pl.edu.pw.restapi.dto.CourseFileInfoDTO;
import pl.edu.pw.restapi.dto.mapper.CourseFileMapper;
import pl.edu.pw.restapi.repository.CourseFileRepository;
import pl.edu.pw.restapi.repository.CourseRepository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseFileServiceTest {

    @Spy
    private CourseFileMapper courseFileMapper;
    @Mock
    private CourseFileRepository courseFileRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private CourseFileServiceImpl courseFileService;


    //Tests for method getCourseFiles

    @Test
    public void shouldReturnFilesWhenFilesAndUsernameArePresentInRepository() {
        Course course = Course.builder().id(1L).build();
        User user = new User(1L, "username", "password", List.of(course), List.of());
        CourseFile file1 = new CourseFile(1L, "File1", "jpg", new byte[8], course);
        CourseFile file2 = new CourseFile(2L, "File2", "jpg", new byte[8], course);
        List<CourseFile> filesFromRepo = List.of(file1, file2);

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.of(course));
        when(courseFileRepository.findAllByCourseId(course.getId()))
                .thenReturn(filesFromRepo);

        List<CourseFileInfoDTO> actualFiles = courseFileService.getCourseFiles(course.getId(), user.getUsername());

        assertAll(
                () -> assertEquals(filesFromRepo.size(), actualFiles.size()),
                () -> assertEqualsCourseFileInfo(filesFromRepo.get(0), actualFiles.get(0)),
                () -> assertEqualsCourseFileInfo(filesFromRepo.get(1), actualFiles.get(1))
        );
    }

    @Test
    public void shouldReturnEmptyListWhenFilesAreNotPresentAndUsernameIsPresent() {
        Course course = Course.builder().id(1L).build();
        User user = new User(1L, "username", "password", List.of(course), List.of());
        List<CourseFile> filesFromRepo = List.of();

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.of(course));
        when(courseFileRepository.findAllByCourseId(course.getId()))
                .thenReturn(filesFromRepo);

        List<CourseFileInfoDTO> actualFiles = courseFileService.getCourseFiles(course.getId(), user.getUsername());

        assertAll(
                () -> assertEquals(0, actualFiles.size())
        );
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenGettingFilesListsAndUsernameIsNotPresentInRepository() {
        String username = "NotPresentInRepo";
        String exceptionMsg = "Not found";
        when(userService.loadUserByUsername(username))
                .thenThrow(new UsernameNotFoundException(exceptionMsg));

        UsernameNotFoundException thrownException = assertThrows(
                UsernameNotFoundException.class,
                () -> courseFileService.getCourseFiles(1L, username)
        );
        assertEquals(exceptionMsg, thrownException.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenUsernameIsPresentAndCourseIsNotPresent() {
        User user = new User(1L, "username", "password", List.of(), List.of());
        Long courseId = 1L;
        List<CourseFile> filesFromRepo = List.of();

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findByCourseIdAndUserId(courseId, user.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrownException = assertThrows(
                EntityNotFoundException.class,
                () -> courseFileService.getCourseFiles(courseId, user.getUsername())
        );

        assertEquals("Course not found", thrownException.getMessage());
    }

    private void assertEqualsCourseFileInfo(CourseFile expected, CourseFileInfoDTO actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getType(), actual.getType())
        );
    }


    //Tests for method getCourseFile

    @Test
    public void shouldReturnFileWhenUsernameAndCourseAndFileAreInRepository() {
        Course course = Course.builder().id(1L).build();
        User user = new User(1L, "username", "password", List.of(course), List.of());
        CourseFile file = new CourseFile(1L, "File1", "jpg", new byte[8], course);

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.of(course));
        when(courseFileRepository.findByIdAndCourseId(file.getId(), course.getId()))
                .thenReturn(Optional.of(file));

        CourseFileDTO actualFile = courseFileService.getCourseFile(course.getId(), file.getId(), user.getUsername());

        assertEquals(file.getId(), actualFile.getId());
        assertEquals(file.getName(), actualFile.getName());
        assertEquals(file.getType(), actualFile.getType());
        assertEquals(file.getData(), actualFile.getData());
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenGettingFileAndUsernameIsNotPresentInRepository() {
        String username = "Not present";
        String exceptionMsg = "Not found";

        when(userService.loadUserByUsername(username))
                .thenThrow(new UsernameNotFoundException(exceptionMsg));

        UsernameNotFoundException thrownException = assertThrows(
                UsernameNotFoundException.class,
                () ->courseFileService.getCourseFile(1L, 1L, username)
        );

        assertEquals(exceptionMsg, thrownException.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenCourseIsNotInRepository() {
        Course course = Course.builder().id(1L).build();
        User user = new User(1L, "username", "password", List.of(course), List.of());

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrownException = assertThrows(
                EntityNotFoundException.class,
                () ->courseFileService.getCourseFile(course.getId(), 1L, user.getUsername())
        );

        assertEquals("Course not found", thrownException.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenFileIsNotInRepository() {
        Course course = Course.builder().id(1L).build();
        User user = new User(1L, "username", "password", List.of(course), List.of());
        CourseFile file = new CourseFile(1L, "File1", "jpg", new byte[8], course);

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.of(course));
        when(courseFileRepository.findByIdAndCourseId(file.getId(), course.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrownException = assertThrows(
                EntityNotFoundException.class,
                () ->courseFileService.getCourseFile(course.getId(), file.getId(), user.getUsername())
        );

        assertEquals("File not found", thrownException.getMessage());
    }


    //Tests for method createCourseFile

    @Test
    public void shouldSaveFileToRepositoryWhenUsernameAndCourseIsInRepository() throws IOException {
        Course course = Course.builder().id(1L).build();
        User user = new User(1L, "username", "password", List.of(course), List.of());
        MultipartFile file = mock(MultipartFile.class);
        String fileName = "file.jpg";
        String fileType = "jpg";
        byte[] fileData = new byte[8];

        when(file.getOriginalFilename()).thenReturn(fileName);
        when(file.getContentType()).thenReturn(fileType);
        when(file.getBytes()).thenReturn(fileData);
        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.of(course));

        CourseFileInfoDTO actualCourse = courseFileService.createCourseFile(course.getId(), file, user.getUsername());

        verify(courseFileRepository).save(any());
        assertAll(
                () -> assertEquals(fileName, actualCourse.getName()),
                () -> assertEquals(fileType, actualCourse.getType())
        );
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenCreatingFileAndUsernameIsNotPresentInRepository() {
        Course course = Course.builder().id(1L).build();
        String username = "Not present";
        MultipartFile file = mock(MultipartFile.class);
        String exceptionMsg = "Not found";

        when(userService.loadUserByUsername(username))
                .thenThrow(new UsernameNotFoundException(exceptionMsg));

        UsernameNotFoundException thrownException = assertThrows(
                UsernameNotFoundException.class,
                () -> courseFileService.createCourseFile(course.getId(), file, username)
        );
        assertEquals(exceptionMsg, thrownException.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenCreatingFileAndCourseIsNotPresentInRepository() {
        Course course = Course.builder().id(1L).build();
        User user = new User(1L, "username", "password", List.of(course), List.of());
        MultipartFile file = mock(MultipartFile.class);

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrownException = assertThrows(
                EntityNotFoundException.class,
                () -> courseFileService.createCourseFile(course.getId(), file, user.getUsername())
        );
        assertEquals("Course not found", thrownException.getMessage());
    }


    //Tests for method deleteCourseFile

    @Test
    public void shouldDeleteFileWhenFileExistsInReleasedCourses() {
        Course course = Course.builder().id(1L).build();
        User user = new User(1L, "username", "password", List.of(course), List.of());
        Long fileId = 1L;

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.of(course));
        when(courseFileRepository.existsByIdAndCourseId(fileId, course.getId()))
                .thenReturn(true);

        courseFileService.deleteCourseFile(course.getId(), fileId, user.getUsername());

        verify(courseFileRepository).deleteByIdAndCourseId(fileId, course.getId());
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenDeletingFileAndUsernameDoesNotExists() {
        Course course = Course.builder().id(1L).build();
        User user = new User(1L, "username", "password", List.of(course), List.of());
        Long fileId = 1L;
        String exceptionMsg = "Not found";

        when(userService.loadUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException(exceptionMsg));

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> courseFileService.deleteCourseFile(course.getId(), fileId, user.getUsername())
        );

        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenDeletingFileAndCourseIsNotPresentInReleasedCourses() {
        Course course = Course.builder().id(1L).build();
        User user = new User(1L, "username", "password", List.of(course), List.of());
        Long fileId = 1L;

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseFileService.deleteCourseFile(course.getId(), fileId, user.getUsername())
        );

        assertEquals("Course not found", exception.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenDeletingFileAndFileDoesNotExist() {
        Course course = Course.builder().id(1L).build();
        User user = new User(1L, "username", "password", List.of(course), List.of());
        Long fileId = 1L;

        when(userService.loadUserByUsername(user.getUsername()))
                .thenReturn(user);
        when(courseRepository.findReleasedCourseByCourseIdAndUserId(course.getId(), user.getId()))
                .thenReturn(Optional.of(course));
        when(courseFileRepository.existsByIdAndCourseId(fileId, course.getId()))
                .thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseFileService.deleteCourseFile(course.getId(), fileId, user.getUsername())
        );

        assertEquals("File not found", exception.getMessage());
    }

}