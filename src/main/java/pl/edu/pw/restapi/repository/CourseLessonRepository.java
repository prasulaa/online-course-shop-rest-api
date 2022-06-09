package pl.edu.pw.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.pw.restapi.domain.CourseLesson;

import java.util.Optional;

@Repository
public interface CourseLessonRepository extends JpaRepository<CourseLesson, Long> {

    @Query(nativeQuery = true, value =
            "select * from course_lesson cl " +
                    "inner join course_section_lessons csl on " +
                        "csl.lessons_id = cl.id and " +
                        "cl.id = :lessonId " +
                    "inner join course_sections css on " +
                        "csl.course_section_id = css.sections_id and " +
                        "css.sections_id = :sectionId and " +
                        "css.course_id = :courseId")
    Optional<CourseLesson> getCourseLessonByCourseIdAndSectionIdAndLessonId(@Param("courseId") Long courseId,
                                                                            @Param("sectionId") Long sectionId,
                                                                            @Param("lessonId") Long lessonId);

    @Query(nativeQuery = true, value =
            "select * from course_lesson cl " +
                    "inner join course_section_lessons csl on " +
                    "csl.lessons_id = cl.id and " +
                    "cl.name = :name " +
                    "inner join course_sections css on " +
                    "csl.course_section_id = css.sections_id and " +
                    "css.sections_id = :sectionId and " +
                    "css.course_id = :courseId")
    Optional<CourseLesson> getCourseLessonByCourseIdAndSectionIdAndName(@Param("courseId") Long courseId,
                                                                        @Param("sectionId") Long sectionId,
                                                                        @Param("name") String name);

}
