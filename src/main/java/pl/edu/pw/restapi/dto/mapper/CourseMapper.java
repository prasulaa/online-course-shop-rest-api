package pl.edu.pw.restapi.dto.mapper;

import pl.edu.pw.restapi.domain.Course;
import pl.edu.pw.restapi.domain.CourseCategory;
import pl.edu.pw.restapi.dto.CourseDTO;
import pl.edu.pw.restapi.dto.CourseDetailsDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseMapper {

    public static List<CourseDTO> map(List<Course> courses) {
        if (courses == null) {
            return null;
        } else {
            return courses.stream()
                    .map(CourseMapper::map)
                    .collect(Collectors.toList());
        }
    }

    public static CourseDTO map(Course course) {
        if (course == null) {
            return null;
        } else {
            return CourseDTO.builder()
                    .id(course.getId())
                    .title(course.getTitle())
                    .thumbnail(course.getThumbnail())
                    .price(course.getPrice())
                    .build();
        }
    }

    public static CourseDetailsDTO mapDetails(Course course) {
        if (course == null) {
            return null;
        } else {
            return CourseDetailsDTO.builder()
                    .id(course.getId())
                    .title(course.getTitle())
                    .thumbnail(course.getThumbnail())
                    .price(course.getPrice())
                    .categories(CourseCategoryMapper.mapNames(course.getCategories()))
                    .difficulty(course.getDifficulty())
                    .scopes(course.getScopes())
                    .description(course.getDescription())
                    .sections(CourseSectionMapper.map(course.getSections()))
                    .build();
        }
    }

}
