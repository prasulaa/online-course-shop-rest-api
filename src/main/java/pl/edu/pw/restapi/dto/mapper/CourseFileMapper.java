package pl.edu.pw.restapi.dto.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pw.restapi.domain.Course;
import pl.edu.pw.restapi.domain.CourseFile;
import pl.edu.pw.restapi.dto.CourseFileDTO;
import pl.edu.pw.restapi.dto.CourseFileInfoDTO;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CourseFileMapper {

    public List<CourseFileInfoDTO> map(List<CourseFile> courseFiles) {
        if (courseFiles == null) {
            return null;
        } else {
            return courseFiles.stream()
                    .map(this::map)
                    .collect(Collectors.toList());
        }
    }

    public CourseFileInfoDTO map(CourseFile courseFile) {
        if (courseFile == null) {
            return null;
        } else {
            return CourseFileInfoDTO.builder()
                    .id(courseFile.getId())
                    .name(courseFile.getName())
                    .type(courseFile.getType())
                    .build();
        }
    }

    public CourseFile map(MultipartFile courseFile, Course course) throws IOException {
        if (courseFile == null) {
            return null;
        } else {
            return CourseFile.builder()
                    .name(StringUtils.cleanPath(courseFile.getOriginalFilename()))
                    .type(courseFile.getContentType())
                    .data(courseFile.getBytes())
                    .course(course)
                    .build();
        }
    }

    public CourseFileDTO mapDetails(CourseFile courseFile) {
        if (courseFile == null) {
            return null;
        } else {
            return CourseFileDTO.builder()
                    .id(courseFile.getId())
                    .name(courseFile.getName())
                    .type(courseFile.getType())
                    .data(courseFile.getData())
                    .build();
        }
    }


}
