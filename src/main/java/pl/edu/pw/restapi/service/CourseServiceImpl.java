package pl.edu.pw.restapi.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.Course;
import pl.edu.pw.restapi.domain.CourseCategory;
import pl.edu.pw.restapi.domain.User;
import pl.edu.pw.restapi.dto.CourseDTO;
import pl.edu.pw.restapi.dto.CourseDetailsDTO;
import pl.edu.pw.restapi.dto.CreateCourseDTO;
import pl.edu.pw.restapi.dto.UpdateCourseDTO;
import pl.edu.pw.restapi.dto.mapper.CourseMapper;
import pl.edu.pw.restapi.repository.UserRepository;
import pl.edu.pw.restapi.service.updater.CourseUpdater;
import pl.edu.pw.restapi.repository.CourseCategoryRepository;
import pl.edu.pw.restapi.repository.CourseRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseCategoryRepository courseCategoryRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public List<CourseDTO> getCourses(String title, List<Long> categories, List<Long> difficulties, Double priceMin,
                                      Double priceMax, Integer pageNumber, Integer pageSize, Sort.Direction sort) {
        Pageable pageable = getPageable(pageNumber, pageSize, sort);
        List<Course> courses = courseRepository.findAll(title, categories, difficulties, priceMin, priceMax, pageable);
        return CourseMapper.map(courses);
    }

    @Override
    public CourseDetailsDTO getCourseDetails(Long id, String username) {
        User user = (User) userService.loadUserByUsername(username);

        Course course = courseRepository.findByCourseIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course " + id + " not found"));
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
    public CourseDetailsDTO updateCourse(UpdateCourseDTO course, Long id, String username) {
        User user = (User) userService.loadUserByUsername(username);

        Course courseToUpdate = courseRepository.findReleasedCoursesByCourseIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course " + id + " not found"));

        updateCourse(course, courseToUpdate);
        courseRepository.save(courseToUpdate);

        return CourseMapper.mapDetails(courseToUpdate);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id, String username) {
        User user = (User) userService.loadUserByUsername(username);

        Course courseToDelete = courseRepository.findReleasedCoursesByCourseIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course " + id + " not found"));

        user.getReleasedCourses().remove(courseToDelete);
        userRepository.save(user);
        courseRepository.delete(courseToDelete);
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
                            .orElseThrow(() -> new IllegalArgumentException("Category " + id + " not found")))
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
