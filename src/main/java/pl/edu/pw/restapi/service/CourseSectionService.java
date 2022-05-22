package pl.edu.pw.restapi.service;

import pl.edu.pw.restapi.dto.CourseSectionDTO;
import pl.edu.pw.restapi.dto.CreateCourseSectionDTO;

public interface CourseSectionService {

    CourseSectionDTO createCourseSection(Long courseId, CreateCourseSectionDTO section);

    CourseSectionDTO getCourseSection(Long courseId, Long sectionId);

}
