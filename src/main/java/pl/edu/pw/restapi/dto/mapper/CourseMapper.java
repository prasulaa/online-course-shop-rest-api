package pl.edu.pw.restapi.dto.mapper;

import pl.edu.pw.restapi.domain.Course;
import pl.edu.pw.restapi.dto.CourseDTO;

import java.util.ArrayList;
import java.util.List;

public class CourseMapper {

    public static List<CourseDTO> map(List<Course> courses) {
        if (courses == null) {
            return null;
        } else {
            List<CourseDTO> mappedCourses = new ArrayList<>();

            for (Course c: courses) {
                mappedCourses.add(map(c));
            }

            return mappedCourses;
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

}
