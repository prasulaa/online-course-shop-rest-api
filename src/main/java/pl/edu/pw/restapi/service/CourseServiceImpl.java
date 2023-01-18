package pl.edu.pw.restapi.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.Course;
import pl.edu.pw.restapi.domain.CourseCategory;
import pl.edu.pw.restapi.domain.CourseUser;
import pl.edu.pw.restapi.dto.*;
import pl.edu.pw.restapi.dto.mapper.BuyCourseMapper;
import pl.edu.pw.restapi.dto.mapper.CourseMapper;
import pl.edu.pw.restapi.repository.UserRepository;
import pl.edu.pw.restapi.service.email.EmailNotificationService;
import pl.edu.pw.restapi.service.payu.PayuConnector;
import pl.edu.pw.restapi.service.payu.dto.PayuNotification;
import pl.edu.pw.restapi.service.payu.dto.PayuRequest;
import pl.edu.pw.restapi.service.payu.dto.PayuResponse;
import pl.edu.pw.restapi.service.updater.CourseUpdater;
import pl.edu.pw.restapi.repository.CourseRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CourseCategoryService courseCategoryService;
    private final CourseMapper courseMapper;
    private final BuyCourseMapper buyCourseMapper;
    private final PayuConnector payuConnector;
    private final EmailNotificationService notificationService;

    @Override
    @Transactional
    public List<CourseDTO> getCourses(String title, List<Long> categories, List<Long> difficulties, Double priceMin,
                                      Double priceMax, Integer pageNumber, Integer pageSize, Sort.Direction sort) {
        Pageable pageable = getPageable(pageNumber, pageSize, sort);
        List<Course> courses = courseRepository.findAll(title, categories, difficulties, priceMin, priceMax, pageable);
        return courseMapper.map(courses);
    }

    @Override
    public CourseDetailsDTO getCourseDetails(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return courseMapper.mapDetails(course);
    }

    @Override
    public List<CourseDTO> getBoughtCourses(String username) {
        CourseUser user = (CourseUser) userService.loadUserByUsername(username);

        List<Course> courses = courseRepository.findBoughtCoursesByUserId(user.getId());

        return courseMapper.map(courses);
    }

    @Override
    public List<CourseDTO> getReleasedCourses(String username) {
        CourseUser user = (CourseUser) userService.loadUserByUsername(username);

        List<Course> courses = courseRepository.findReleasedCoursesByUserId(user.getId());

        return courseMapper.map(courses);
    }

    @Override
    @Transactional
    public CourseDetailsDTO createCourse(CreateCourseDTO course, String username) {
        CourseUser user = (CourseUser) userService.loadUserByUsername(username);

        List<CourseCategory> categories = courseCategoryService.getCategories(course.getCategories());
        Course mappedCourse = courseMapper.map(course, categories);

        user.getReleasedCourses().add(mappedCourse);
        courseRepository.save(mappedCourse);
        userRepository.save(user);

        return courseMapper.mapDetails(mappedCourse);
    }

    @Override
    public CourseDetailsDTO updateCourse(UpdateCourseDTO course, Long id, String username) {
        CourseUser user = (CourseUser) userService.loadUserByUsername(username);

        Course courseToUpdate = courseRepository.findReleasedCourseByCourseIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        updateCourse(course, courseToUpdate);
        courseRepository.save(courseToUpdate);

        return courseMapper.mapDetails(courseToUpdate);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id, String username) {
        CourseUser user = (CourseUser) userService.loadUserByUsername(username);

        Course courseToDelete = courseRepository.findReleasedCourseByCourseIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        user.getReleasedCourses().remove(courseToDelete);
        userRepository.save(user);
        courseRepository.delete(courseToDelete);
    }

    @Override
    public BuyCourseResponseDTO buyCourse(Long id, String username, String ip) {
        CourseUser user = (CourseUser) userService.loadUserByUsername(username);

        courseRepository.findByCourseIdAndUserId(id, user.getId())
                .ifPresent((course) -> {
                    throw new IllegalArgumentException("User already have course");
                });

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (course.getPrice() == 0) {
            addCourseToBoughtCourses(user, course, null);
            return null;
        }

        PayuRequest request = payuConnector.createRequest(username, course, ip);
        PayuResponse response = payuConnector.createOrder(request);
        notificationService.sendPaymentNotification(user, course, response);

        return buyCourseMapper.map(response);
    }

    @Override
    public void boughtCourse(Long courseId, String username, PayuNotification notification) {
        try {
            String status = notification.getOrder().getStatus();
            if (status.equals("COMPLETED")) {
                CourseUser user = (CourseUser) userService.loadUserByUsername(username);

                if (courseRepository.findByCourseIdAndUserId(courseId, user.getId()).isPresent()) {
                    return;
                }

                Course course = courseRepository.findById(courseId)
                        .orElseThrow(() -> new EntityNotFoundException("Course not found"));

                addCourseToBoughtCourses(user, course, notification);
            }
        } catch (Exception e) {
            log.error("Cannot add course " + courseId + " to " + username + " bought courses", e);
        }
    }

    private void addCourseToBoughtCourses(CourseUser user, Course course, PayuNotification payuNotification) {
        user.getBoughtCourses().add(course);
        userRepository.save(user);
        notificationService.sendPurchaseNotification(user, course, payuNotification);
        log.info("Course " + course.getId() + " was bought by user " + user.getId() + " for price " + course.getPrice());
    }

    private void updateCourse(UpdateCourseDTO course, Course courseToUpdate) {
        List<CourseCategory> categories = courseCategoryService.getCategories(course.getCategories());
        CourseUpdater courseUpdater = new CourseUpdater();
        courseUpdater.update(course, courseToUpdate, categories);
    }

    private Pageable getPageable(Integer pageNumber, Integer pageSize, Sort.Direction sort) {
        pageNumber = pageNumber != null && pageNumber >= 0 ? pageNumber : 0;
        pageSize = pageSize != null && pageSize > 0 ? pageSize : 24;
        sort = sort == null ? Sort.Direction.ASC : sort;

        return PageRequest.of(pageNumber, pageSize, Sort.by(sort, "price"));
    }
}
