package pl.edu.pw.restapi.dto.mapper;

import pl.edu.pw.restapi.domain.CourseFile;
import pl.edu.pw.restapi.dto.CourseFileDTO;
import pl.edu.pw.restapi.dto.CourseFileInfoDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CourseFileMapper {

    public static List<CourseFileInfoDTO> map(List<CourseFile> courseFiles) {
        if (courseFiles == null) {
            return null;
        } else {
            return courseFiles.stream()
                    .map(CourseFileMapper::map)
                    .collect(Collectors.toList());
        }
    }

    public static CourseFileInfoDTO map(CourseFile courseFile) {
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

    public static CourseFileDTO mapDetails(CourseFile courseFile) {
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
