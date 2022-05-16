package pl.edu.pw.restapi.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.pw.restapi.domain.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(nativeQuery = true, value = "select * from course c where " +
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

}
