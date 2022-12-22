package pl.edu.pw.restapi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.Course;
import pl.edu.pw.restapi.domain.CourseSection;
import pl.edu.pw.restapi.domain.CourseUser;
import pl.edu.pw.restapi.dto.CourseSectionDTO;
import pl.edu.pw.restapi.dto.CreateCourseSectionDTO;
import pl.edu.pw.restapi.dto.UpdateCourseSectionDTO;
import pl.edu.pw.restapi.dto.mapper.CourseSectionMapper;
import pl.edu.pw.restapi.repository.CourseRepository;
import pl.edu.pw.restapi.repository.CourseSectionRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseSectionServiceImpl implements CourseSectionService {

    private final CourseRepository courseRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final UserService userService;
    private final CourseSectionMapper courseSectionMapper;

    @Override
    public CourseSectionDTO getCourseSection(Long courseId, Long sectionId, String username) {
        CourseUser user = (CourseUser) userService.loadUserByUsername(username);

        CourseSection section = courseSectionRepository
                .findByCourseIdAndSectionIdAndUserId(courseId, sectionId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course section not found"));

        return courseSectionMapper.map(section);
    }

    @Override
    public CourseSectionDTO createCourseSection(Long courseId, CreateCourseSectionDTO section, String username) {
        CourseUser user = (CourseUser) userService.loadUserByUsername(username);

        Course course = courseRepository.findReleasedCourseByCourseIdAndUserId(courseId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        CourseSection createdSection = courseSectionMapper.map(section);
        addCourseSectionToDB(course, createdSection);

        return courseSectionMapper.map(createdSection);
    }

    @Override
    public CourseSectionDTO updateCourseSection(Long courseId, Long sectionId,
                                                UpdateCourseSectionDTO section, String username) {
        CourseUser user = (CourseUser) userService.loadUserByUsername(username);

        CourseSection sectionToUpdate = courseSectionRepository
                .findReleasedCourseSectionByCourseIdAndSectionIdAndUserId(courseId, sectionId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course section not found"));

        updateCourseSectionInDB(courseId, sectionToUpdate, section);

        return courseSectionMapper.map(sectionToUpdate);
    }

    @Override
    public void deleteCourseSection(Long courseId, Long sectionId, String username) {
        CourseUser user = (CourseUser) userService.loadUserByUsername(username);

        Course course = courseRepository.findReleasedCourseByCourseIdAndUserId(courseId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        deleteSectionFromCourseInDB(course, sectionId);
    }

    @Transactional
    private void deleteSectionFromCourseInDB(Course course, Long sectionId) {
        if (course.getSections().removeIf(s -> s.getId().equals(sectionId))) {
            courseRepository.save(course);
            courseSectionRepository.deleteById(sectionId);
        } else {
            throw new EntityNotFoundException("Course section not found");
        }
    }

    private void updateCourseSectionInDB(Long courseId, CourseSection sectionToUpdate, UpdateCourseSectionDTO section) {
        checkIfCourseContainsSectionWithSameName(courseId, section.getName());

        sectionToUpdate.setName(section.getName());

        courseSectionRepository.save(sectionToUpdate);
    }

    @Transactional
    private void addCourseSectionToDB(Course course, CourseSection section) {
        checkIfCourseContainsSectionWithSameName(course.getId(), section.getName());

        course.getSections().add(section);

        courseSectionRepository.save(section);
        courseRepository.save(course);
    }

    private void checkIfCourseContainsSectionWithSameName(Long courseId, String sectionName) {
        if (sectionName != null && !sectionName.isEmpty()) {
            Optional<CourseSection> courseSectionOpt = courseSectionRepository.findByCourseIdAndName(courseId, sectionName);

            if (courseSectionOpt.isPresent()) {
                throw new IllegalArgumentException("Section " + sectionName + " already exists");
            }
        }
    }
}
