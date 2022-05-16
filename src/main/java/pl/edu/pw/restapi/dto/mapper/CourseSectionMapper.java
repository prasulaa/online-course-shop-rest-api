package pl.edu.pw.restapi.dto.mapper;

import pl.edu.pw.restapi.domain.CourseSection;
import pl.edu.pw.restapi.dto.CourseDetailsSectionDTO;

import java.util.ArrayList;
import java.util.List;

public class CourseSectionMapper {

    public static List<CourseDetailsSectionDTO> map(List<CourseSection> sections) {
        if (sections == null) {
            return null;
        } else {
            List<CourseDetailsSectionDTO> mappedSections = new ArrayList<>();

            for (CourseSection s: sections) {
                mappedSections.add(map(s));
            }

            return mappedSections;
        }
    }

    public static CourseDetailsSectionDTO map(CourseSection section) {
        if (section == null) {
            return null;
        } else {
            return CourseDetailsSectionDTO.builder()
                    .id(section.getId())
                    .name(section.getName())
                    .lessons(CourseLessonMapper.map(section.getLessons()))
                    .build();
        }
    }

}
