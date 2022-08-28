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
            "select cl.* from course_lesson cl " +
                    "join course_section_lessons csl on " +
                        "csl.lessons_id = cl.id and " +
                        "cl.id = :lessonId " +
                    "join course_sections css on " +
                        "csl.course_section_id = css.sections_id and " +
                        "css.sections_id = :sectionId and " +
                        "css.course_id = :courseId " +
                    "join ( " +
                        "select ubc.user_id as uid, ubc.bought_courses_id as cid from user_bought_courses ubc " +
                        "union " +
                        "select urc.user_id as uid, urc.released_courses_id as cid from user_released_courses urc " +
                    ") uc on " +
                        "uc.cid = css.course_id and " +
                        "uc.uid = :userId")
    Optional<CourseLesson> findCourseLessonByCourseIdAndSectionIdAndLessonIdAndUserId(@Param("courseId") Long courseId,
                                                                                      @Param("sectionId") Long sectionId,
                                                                                      @Param("lessonId") Long lessonId,
                                                                                      @Param("userId") Long userId);

    @Query(nativeQuery = true, value =
            "select cl.* from course_lesson cl " +
                    "join course_section_lessons csl on " +
                        "csl.lessons_id = cl.id and " +
                        "cl.id = :lessonId " +
                    "join course_sections css on " +
                        "csl.course_section_id = css.sections_id and " +
                        "css.sections_id = :sectionId and " +
                        "css.course_id = :courseId " +
                    "join user_released_courses urc on " +
                        "urc.released_courses_id = css.course_id and " +
                        "urc.user_id = :userId")
    Optional<CourseLesson> findReleasedCourseLessonByCourseIdAndSectionIdAndLessonIdAndUserId(@Param("courseId") Long courseId,
                                                                                              @Param("sectionId") Long sectionId,
                                                                                              @Param("lessonId") Long lessonId,
                                                                                              @Param("userId") Long userId);

    @Query(nativeQuery = true, value =
            "select * from course_lesson cl " +
                    "inner join course_section_lessons csl on " +
                        "csl.lessons_id = cl.id and " +
                        "cl.name = :name " +
                    "inner join course_sections css on " +
                        "csl.course_section_id = css.sections_id and " +
                        "css.sections_id = :sectionId and " +
                        "css.course_id = :courseId")
    Optional<CourseLesson> findCourseLessonByCourseIdAndSectionIdAndName(@Param("courseId") Long courseId,
                                                                         @Param("sectionId") Long sectionId,
                                                                         @Param("name") String name);

}
