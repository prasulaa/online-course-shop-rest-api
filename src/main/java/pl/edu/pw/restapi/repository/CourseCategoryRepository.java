package pl.edu.pw.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.restapi.domain.CourseCategory;

@Repository
public interface CourseCategoryRepository extends JpaRepository<Long, CourseCategory> {
}
