package pl.edu.pw.restapi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pw.restapi.domain.Course;
import pl.edu.pw.restapi.domain.CourseFile;
import pl.edu.pw.restapi.domain.CourseUser;
import pl.edu.pw.restapi.dto.CourseFileDTO;
import pl.edu.pw.restapi.dto.CourseFileInfoDTO;
import pl.edu.pw.restapi.dto.mapper.CourseFileMapper;
import pl.edu.pw.restapi.repository.CourseFileRepository;
import pl.edu.pw.restapi.repository.CourseRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseFileServiceImpl implements CourseFileService {

    private final CourseFileRepository courseFileRepository;
    private final CourseRepository courseRepository;
    private final UserService userService;
    private final CourseFileMapper courseFileMapper;

    @Override
    public List<CourseFileInfoDTO> getCourseFiles(Long courseId, String username) {
        CourseUser user = (CourseUser) userService.loadUserByUsername(username);

        Course course = courseRepository
                .findByCourseIdAndUserId(courseId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        List<CourseFile> courseFiles = courseFileRepository.findAllByCourseId(courseId);

        return courseFileMapper.map(courseFiles);
    }

    @Override
    public CourseFileDTO getCourseFile(Long courseId, Long fileId, String username) {
        CourseUser user = (CourseUser) userService.loadUserByUsername(username);

        Course course = courseRepository
                .findByCourseIdAndUserId(courseId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        CourseFile courseFile = courseFileRepository
                .findByIdAndCourseId(fileId, courseId)
                .orElseThrow(() -> new EntityNotFoundException("File not found"));

        return courseFileMapper.mapDetails(courseFile);
    }

    @Override
    public CourseFileInfoDTO createCourseFile(Long courseId, MultipartFile courseFile, String username) throws IOException {
        CourseUser user = (CourseUser) userService.loadUserByUsername(username);

        Course course = courseRepository
                .findReleasedCourseByCourseIdAndUserId(courseId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        CourseFile courseFileToSave = courseFileMapper.map(courseFile, course);

        courseFileRepository.save(courseFileToSave);

        return courseFileMapper.map(courseFileToSave);
    }

    @Override
    @Transactional
    public void deleteCourseFile(Long courseId, Long fileId, String username) {
        CourseUser user = (CourseUser) userService.loadUserByUsername(username);

        Course course = courseRepository
                .findReleasedCourseByCourseIdAndUserId(courseId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (courseFileRepository.existsByIdAndCourseId(fileId, courseId)) {
            courseFileRepository.deleteByIdAndCourseId(fileId, courseId);
        } else {
            throw new EntityNotFoundException("File not found");
        }
    }

}
