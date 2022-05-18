package pl.edu.pw.restapi.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.Course;
import pl.edu.pw.restapi.domain.CourseCategory;
import pl.edu.pw.restapi.dto.CourseDTO;
import pl.edu.pw.restapi.dto.CourseDetailsDTO;
import pl.edu.pw.restapi.dto.CreateCourseDTO;
import pl.edu.pw.restapi.dto.UpdateCourseDTO;
import pl.edu.pw.restapi.dto.mapper.CourseMapper;
import pl.edu.pw.restapi.dto.updater.CourseUpdater;
import pl.edu.pw.restapi.repository.CourseCategoryRepository;
import pl.edu.pw.restapi.repository.CourseRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseCategoryRepository courseCategoryRepository;

    public CourseServiceImpl(CourseRepository courseRepository, CourseCategoryRepository courseCategoryRepository) {
        this.courseRepository = courseRepository;
        this.courseCategoryRepository = courseCategoryRepository;
    }

    @Override
    public List<CourseDTO> getCourses(String title, List<Long> categories, List<Long> difficulties, Double priceMin,
                                      Double priceMax, Integer pageNumber, Integer pageSize, Sort.Direction sort) {
        Pageable pageable = getPageable(pageNumber, pageSize, sort);
        List<Course> courses = courseRepository.findAll(title, categories, difficulties, priceMin, priceMax, pageable);
        return CourseMapper.map(courses);
    }

    @Override
    public CourseDetailsDTO getCourseDetails(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id + " not found"));
        return CourseMapper.mapDetails(course);
    }

    @Override
    public CourseDetailsDTO createCourse(CreateCourseDTO course) {
        List<CourseCategory> categories = getCourseCategories(course.getCategories());
        Course mappedCourse = CourseMapper.map(course, categories);

        courseRepository.save(mappedCourse);

        return CourseMapper.mapDetails(mappedCourse);
    }

    @Override
    public CourseDetailsDTO updateCourse(UpdateCourseDTO course, Long id) {
        Course courseToUpdate = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course id=" + id + " does not exist"));

        updateCourse(course, courseToUpdate);
        courseRepository.save(courseToUpdate);

        return CourseMapper.mapDetails(courseToUpdate);
    }

    private void updateCourse(UpdateCourseDTO course, Course courseToUpdate) {
        List<CourseCategory> categories = getCourseCategories(course.getCategories());
        CourseUpdater courseUpdater = new CourseUpdater();
        courseUpdater.update(course, courseToUpdate, categories);
    }

    private List<CourseCategory> getCourseCategories(List<Long> ids) {
        if (ids == null) {
            return null;
        } else {
            return ids.stream()
                    .map(id -> courseCategoryRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Category id=" + id + " does not exist")))
                    .collect(Collectors.toList());
        }
    }

    private Pageable getPageable(Integer pageNumber, Integer pageSize, Sort.Direction sort) {
        pageNumber = pageNumber != null && pageNumber >= 0 ? pageNumber : 0;
        pageSize = pageSize != null && pageSize > 0 ? pageSize : 24;
        sort = sort == null ? Sort.Direction.ASC : sort;

        return PageRequest.of(pageNumber, pageSize, Sort.by(sort, "price"));
    }
}
