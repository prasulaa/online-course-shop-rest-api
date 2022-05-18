package pl.edu.pw.restapi.service;

import org.springframework.data.domain.Sort;
import pl.edu.pw.restapi.dto.CourseDTO;
import pl.edu.pw.restapi.dto.CourseDetailsDTO;
import pl.edu.pw.restapi.dto.CreateCourseDTO;

import java.util.List;

public interface CourseService {

    List<CourseDTO> getCourses(String title, List<Long> categories, List<Long> difficulties, Double priceMin,
                               Double priceMax, Integer pageNumber, Integer pageSize, Sort.Direction sort);

    CourseDetailsDTO getCourseDetails(Long id);

    CourseDetailsDTO createCourse(CreateCourseDTO course);

}
