package pl.edu.pw.restapi.dto.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.CourseSection;
import pl.edu.pw.restapi.dto.CourseSectionDTO;
import pl.edu.pw.restapi.dto.CreateCourseSectionDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CourseSectionMapper {

    private final CourseLessonMapper courseLessonMapper;

    public List<CourseSectionDTO> map(List<CourseSection> sections) {
        if (sections == null) {
            return null;
        } else {
            return sections.stream()
                    .map(this::map)
                    .collect(Collectors.toList());
        }
    }

    public CourseSectionDTO map(CourseSection section) {
        if (section == null) {
            return null;
        } else {
            return CourseSectionDTO.builder()
                    .id(section.getId())
                    .name(section.getName())
                    .lessons(courseLessonMapper.mapToCourseSectionLessonDTO(section.getLessons()))
                    .build();
        }
    }

    public CourseSection map(CreateCourseSectionDTO section) {
        if (section == null) {
            return null;
        } else {
            return CourseSection.builder()
                    .name(section.getName())
                    .lessons(new ArrayList<>())
                    .build();
        }
    }

}
