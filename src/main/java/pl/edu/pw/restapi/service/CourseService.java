package pl.edu.pw.restapi.service;

import org.springframework.data.domain.Sort;
import pl.edu.pw.restapi.dto.*;
import pl.edu.pw.restapi.service.payu.dto.PayuNotification;

import java.util.List;

public interface CourseService {

    List<CourseDTO> getCourses(String title, List<Long> categories, List<Long> difficulties, Double priceMin,
                               Double priceMax, Integer pageNumber, Integer pageSize, Sort.Direction sort);

    CourseDetailsDTO getCourseDetails(Long id);

    List<CourseDTO> getBoughtCourses(String username);

    List<CourseDTO> getReleasedCourses(String username);

    CourseDetailsDTO createCourse(CreateCourseDTO course, String username);

    CourseDetailsDTO updateCourse(UpdateCourseDTO course, Long id, String username);

    void deleteCourse(Long id, String username);

    BuyCourseResponseDTO buyCourse(Long id, String username, String ip);

    void boughtCourse(Long courseId, String username, PayuNotification notification);

}
