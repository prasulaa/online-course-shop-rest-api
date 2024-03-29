package pl.edu.pw.restapi.service;

import org.springframework.web.multipart.MultipartFile;
import pl.edu.pw.restapi.dto.CourseFileDTO;
import pl.edu.pw.restapi.dto.CourseFileInfoDTO;

import java.io.IOException;
import java.util.List;

public interface CourseFileService {

    List<CourseFileInfoDTO> getCourseFiles(Long courseId, String username);

    CourseFileDTO getCourseFile(Long courseId, Long fileId, String username);

    CourseFileInfoDTO createCourseFile(Long courseId, MultipartFile courseFile, String username) throws IOException;

    void deleteCourseFile(Long courseId, Long fileId, String username);

}
