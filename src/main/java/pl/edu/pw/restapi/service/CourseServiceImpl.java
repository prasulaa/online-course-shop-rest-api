package pl.edu.pw.restapi.service;

import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.Course;
import pl.edu.pw.restapi.dto.CourseDTO;
import pl.edu.pw.restapi.dto.mapper.CourseMapper;
import pl.edu.pw.restapi.repository.CourseRepository;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<CourseDTO> getCourses(String title, List<Long> categories, List<Long> difficulties, Double priceMin, Double priceMax) {
        List<Course> courses = courseRepository.findAll(title, categories, difficulties, priceMin, priceMax);
        return CourseMapper.map(courses);
    }
}
