package pl.edu.pw.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.restapi.domain.Course;

@Repository
public interface CourseRepository extends JpaRepository<Long, Course> {
}
