package pl.edu.pw.restapi.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import pl.edu.pw.restapi.domain.Course;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(nativeQuery = true, value =
            "select * from course c where " +
            "(:title is null or c.title like CONCAT('%', :title, '%')) and " +
            "((:difficulties) is null or c.difficulty in (:difficulties)) and " +
            "(:priceMin is null or c.price >= :priceMin) and " +
            "(:priceMax is null or c.price <= :priceMax) and " +
            "((:categories) is null or c.id in (" +
                "select course_id from course_categories cc where " +
                "cc.categories_id in (:categories)" +
            "))")
    List<Course> findAll(@Param("title") String title,
                         @Param("categories") List<Long> categories,
                         @Param("difficulties") List<Long> difficulties,
                         @Param("priceMin") Double priceMin,
                         @Param("priceMax") Double priceMax,
                         Pageable pageable);

    Optional<Course> findById(Long id);

    @Query(nativeQuery = true, value =
            "select c.* from course c " +
                    "inner join (" +
                    "select ubc.course_user_id as uid, ubc.bought_courses_id as cid from user_bought_courses ubc " +
                    "union " +
                    "select urc.course_user_id as uid, urc.released_courses_id as cid from user_released_courses urc" +
                    ") uc on " +
                    "uc.cid = c.id and " +
                    "c.id = :courseId and " +
                    "uc.uid = :userId")
    Optional<Course> findByCourseIdAndUserId(@Param("courseId") Long courseId,
                                             @Param("userId") Long userId);

    @Query(nativeQuery = true, value =
            "select c.* from course c " +
                    "join course_user_released_courses urc on " +
                    "urc.released_courses_id = c.id and " +
                    "c.id = :courseId and " +
                    "urc.course_user_id= :userId")
    Optional<Course> findReleasedCourseByCourseIdAndUserId(@Param("courseId") Long courseId,
                                                           @Param("userId") Long userId);

    @Query(nativeQuery = true, value =
            "select c.* from course c " +
                    "join course_user_bought_courses ubc on " +
                    "ubc.bought_courses_id = c.id and " +
                    "ubc.course_user_id= :userId")
    List<Course> findBoughtCoursesByUserId(Long userId);

    @Query(nativeQuery = true, value =
            "select c.* from course c " +
                    "join course_user_released_courses urc on " +
                    "urc.released_courses_id = c.id and " +
                    "urc.course_user_id= :userId")
    List<Course> findReleasedCoursesByUserId(Long userId);
}
