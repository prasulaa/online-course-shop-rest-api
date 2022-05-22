package pl.edu.pw.restapi.service;

import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.Course;
import pl.edu.pw.restapi.domain.CourseSection;
import pl.edu.pw.restapi.dto.CourseSectionDTO;
import pl.edu.pw.restapi.dto.CreateCourseSectionDTO;
import pl.edu.pw.restapi.dto.mapper.CourseSectionMapper;
import pl.edu.pw.restapi.repository.CourseRepository;
import pl.edu.pw.restapi.repository.CourseSectionRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CourseSectionServiceImpl implements CourseSectionService {

    private final CourseRepository courseRepository;
    private final CourseSectionRepository courseSectionRepository;

    public CourseSectionServiceImpl(CourseRepository courseRepository, CourseSectionRepository courseSectionRepository) {
        this.courseRepository = courseRepository;
        this.courseSectionRepository = courseSectionRepository;
    }

    @Override
    public CourseSectionDTO createCourseSection(Long courseId, CreateCourseSectionDTO section) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course id=" + courseId + " does not exist"));

        CourseSection createdSection = CourseSectionMapper.map(section);
        addCourseSectionToDB(course, createdSection);

        return CourseSectionMapper.map(createdSection);
    }

    @Override
    public CourseSectionDTO getCourseSection(Long courseId, Long sectionId) {
        CourseSection section = courseSectionRepository.findByCourseIdAndSectionId(courseId, sectionId)
                .orElseThrow(() -> new EntityNotFoundException("Course section not found"));

        return CourseSectionMapper.map(section);
    }

    @Transactional
    private void addCourseSectionToDB(Course course, CourseSection section) {
        checkIfCourseContainsSectionWithSameName(course.getId(), section.getName());

        course.getSections().add(section);

        courseSectionRepository.save(section);
        courseRepository.save(course);
    }

    private void checkIfCourseContainsSectionWithSameName(Long courseId, String sectionName) {
        Optional<CourseSection> courseSectionOpt = courseSectionRepository.findByCourseIdAndName(courseId, sectionName);

        if (courseSectionOpt.isPresent()) {
            throw new IllegalArgumentException("Section " + sectionName + " already exists");
        }
    }
}
