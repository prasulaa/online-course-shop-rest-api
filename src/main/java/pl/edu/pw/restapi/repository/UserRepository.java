package pl.edu.pw.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.restapi.domain.CourseUser;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CourseUser, Long> {

    Optional<CourseUser> findByUsername(String username);

    Optional<CourseUser> findByEmail(String email);

    Optional<CourseUser> findByUsernameOrEmail(String username, String email);

}
