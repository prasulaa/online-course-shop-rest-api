package pl.edu.pw.restapi.service;

import pl.edu.pw.restapi.dto.CourseSectionDTO;
import pl.edu.pw.restapi.dto.CreateCourseSectionDTO;
import pl.edu.pw.restapi.dto.UpdateCourseSectionDTO;

public interface CourseSectionService {

    CourseSectionDTO getCourseSection(Long courseId, Long sectionId, String username);

    CourseSectionDTO createCourseSection(Long courseId, CreateCourseSectionDTO section, String username);

    CourseSectionDTO updateCourseSection(Long courseId, Long sectionId, UpdateCourseSectionDTO section, String username);

    void deleteCourseSection(Long courseId, Long sectionId, String username);
}
