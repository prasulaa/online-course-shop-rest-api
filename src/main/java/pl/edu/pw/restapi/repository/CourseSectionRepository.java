package pl.edu.pw.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import pl.edu.pw.restapi.domain.CourseSection;

import java.util.Optional;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {

    @Query(nativeQuery = true, value =
            "select cs.* from course_section cs " +
            "join course_sections css on " +
                "cs.id = css.sections_id and " +
                "css.course_id = :courseId and " +
                "cs.name = :sectionName")
    Optional<CourseSection> findByCourseIdAndName(@Param("courseId") Long courseId,
                                                  @Param("sectionName") String sectionName);

    @Query(nativeQuery = true, value =
            "select cs.* from course_section cs " +
            "join course_sections css on " +
                "cs.id = css.sections_id and " +
                "css.course_id = :courseId and " +
                "cs.id = :sectionId")
    Optional<CourseSection> findByCourseIdAndSectionId(@Param("courseId") Long courseId,
                                                       @Param("sectionId") Long sectionId);

    @Query(nativeQuery = true, value =
            "select cs.* from course_section cs " +
            "join course_sections css on " +
                    "cs.id = css.sections_id " +
            "join ( " +
                    "select ubc.user_id as uid, ubc.bought_courses_id as cid from user_bought_courses ubc " +
                    "union " +
                    "select urc.user_id as uid, urc.released_courses_id as cid from user_released_courses urc " +
            ") uc on " +
                "uc.cid = css.course_id and " +
                "css.course_id = :courseId and " +
                "cs.id = :sectionId and " +
                "uc.uid = :userId")
    Optional<CourseSection> findByCourseIdAndSectionIdAndUserId(@Param("courseId") Long courseId,
                                                                @Param("sectionId") Long sectionId,
                                                                @Param("userId") Long userId);

    @Query(nativeQuery = true, value =
            "select cs.* from course_section cs " +
                    "join course_sections css on " +
                        "cs.id = css.sections_id " +
                    "join user_released_courses urc on " +
                        "urc.released_courses_id = css.course_id and " +
                        "css.course_id = :courseId and " +
                        "cs.id = :sectionId and " +
                        "urc.user_id = :userId")
    Optional<CourseSection> findReleasedCourseSectionByCourseIdAndSectionIdAndUserId(@Param("courseId") Long courseId,
                                                                                     @Param("sectionId") Long sectionId,
                                                                                     @Param("userId") Long userId);

}
