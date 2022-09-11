package pl.edu.pw.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.pw.restapi.domain.CourseFile;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseFileRepository extends JpaRepository<CourseFile, Long> {

    List<CourseFile> findAllByCourseId(Long courseId);

    Optional<CourseFile> findByIdAndCourseId(Long id, Long courseId);

}
