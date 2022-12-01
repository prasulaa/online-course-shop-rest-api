package pl.edu.pw.restapi.dto.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.Course;
import pl.edu.pw.restapi.domain.CourseCategory;
import pl.edu.pw.restapi.dto.BuyCourseResponseDTO;
import pl.edu.pw.restapi.dto.CourseDTO;
import pl.edu.pw.restapi.dto.CourseDetailsDTO;
import pl.edu.pw.restapi.dto.CreateCourseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CourseMapper {

    private final CourseCategoryMapper courseCategoryMapper;
    private final CourseSectionMapper courseSectionMapper;

    public List<CourseDTO> map(List<Course> courses) {
        if (courses == null) {
            return null;
        } else {
            return courses.stream()
                    .map(this::map)
                    .collect(Collectors.toList());
        }
    }

    public CourseDTO map(Course course) {
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

    public CourseDetailsDTO mapDetails(Course course) {
        if (course == null) {
            return null;
        } else {
            return CourseDetailsDTO.builder()
                    .id(course.getId())
                    .title(course.getTitle())
                    .thumbnail(course.getThumbnail())
                    .price(course.getPrice())
                    .categories(courseCategoryMapper.mapNames(course.getCategories()))
                    .difficulty(course.getDifficulty())
                    .scopes(course.getScopes())
                    .description(course.getDescription())
                    .sections(courseSectionMapper.map(course.getSections()))
                    .build();
        }
    }

    public Course map(CreateCourseDTO course, List<CourseCategory> categories) {
        if (course == null) {
            return null;
        } else {
            return Course.builder()
                    .title(course.getTitle())
                    .thumbnail(course.getThumbnail())
                    .price(course.getPrice())
                    .categories(categories)
                    .difficulty(course.getDifficulty())
                    .scopes(course.getScopes())
                    .description(course.getDescription())
                    .sections(new ArrayList<>())
                    .build();
        }
    }
}
