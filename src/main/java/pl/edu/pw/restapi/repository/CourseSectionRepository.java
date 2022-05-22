package pl.edu.pw.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.pw.restapi.domain.CourseSection;

import java.util.Optional;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {

    @Query(nativeQuery = true, value =
            "select * from course_section cs " +
            "inner join course_sections css on " +
                "cs.id = css.sections_id and " +
                "css.course_id = :courseId and " +
                "cs.name = :sectionName")
    Optional<CourseSection> findByCourseIdAndName(@Param("courseId") Long courseId,
                                                  @Param("sectionName") String sectionName);

    @Query(nativeQuery = true, value =
            "select * from course_section cs " +
            "inner join course_sections css on " +
                    "cs.id = css.sections_id and " +
                    "css.course_id = :courseId and " +
                    "cs.id = :sectionId")
    Optional<CourseSection> findByCourseIdAndSectionId(@Param("courseId") Long courseId,
                                                       @Param("sectionId") Long sectionId);

}
