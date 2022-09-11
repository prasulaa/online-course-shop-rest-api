package pl.edu.pw.restapi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
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
import java.util.List;

@Service
@AllArgsConstructor
public class CourseFileServiceImpl implements CourseFileService {

    private final CourseFileRepository courseFileRepository;
    private final CourseRepository courseRepository;
    private final UserService userService;

    @Override
    public List<CourseFileInfoDTO> getCourseFiles(Long courseId, String username) {
        User user = (User) userService.loadUserByUsername(username);

        Course course = courseRepository
                .findByCourseIdAndUserId(courseId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        List<CourseFile> courseFiles = courseFileRepository.findAllByCourseId(courseId);

        return CourseFileMapper.map(courseFiles);
    }

    @Override
    public CourseFileDTO getCourseFile(Long courseId, Long fileId, String username) {
        User user = (User) userService.loadUserByUsername(username);

        Course course = courseRepository
                .findByCourseIdAndUserId(courseId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        CourseFile courseFile = courseFileRepository
                .findByIdAndCourseId(fileId, courseId)
                .orElseThrow(() -> new EntityNotFoundException("File not found"));

        return CourseFileMapper.mapDetails(courseFile);
    }

    @Override
    public List<CourseFileInfoDTO> createCourseFile(Long courseId, MultipartFile courseFile, String username) {
        return null;
    }

    @Override
    public void deleteCourseFile(Long courseId, Long fileId, String username) {

    }

}
