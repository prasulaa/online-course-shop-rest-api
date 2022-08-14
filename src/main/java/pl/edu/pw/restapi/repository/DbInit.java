package pl.edu.pw.restapi.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DbInit implements CommandLineRunner {

    private final CourseCategoryRepository courseCategoryRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        //COURSE CATEGORIES
        CourseCategory categoryProgramming = new CourseCategory("Programming");
        List<CourseCategory> programmingCategories = new ArrayList<>();
        CourseCategory categoryProgrammingJava = new CourseCategory("Java");
        programmingCategories.add(categoryProgrammingJava);
        CourseCategory categoryProgrammingCpp = new CourseCategory("C++");
        programmingCategories.add(categoryProgrammingCpp);
        CourseCategory categoryProgrammingPython = new CourseCategory("Python");
        programmingCategories.add(categoryProgrammingPython);
        categoryProgramming.setSubcategories(programmingCategories);

        courseCategoryRepository.save(categoryProgrammingJava);
        courseCategoryRepository.save(categoryProgrammingCpp);
        courseCategoryRepository.save(categoryProgrammingPython);
        courseCategoryRepository.save(categoryProgramming);

        CourseCategory categoryMusic = new CourseCategory("Music");
        List<CourseCategory> musicCategories = new ArrayList<>();
        CourseCategory categoryMusicSinging = new CourseCategory("Singing");
        musicCategories.add(categoryMusicSinging);
        CourseCategory categoryMusicGuitar = new CourseCategory("Guitar");
        musicCategories.add(categoryMusicGuitar);
        categoryMusic.setSubcategories(musicCategories);

        courseCategoryRepository.save(categoryMusicSinging);
        courseCategoryRepository.save(categoryMusicGuitar);
        courseCategoryRepository.save(categoryMusic);

        //COURSE - PROGRAMMING IN JAVA
        List<CourseCategory> javaCategories = new ArrayList<>();
        javaCategories.add(categoryProgrammingJava);
        List<String> javaScopes = new ArrayList<>();
        javaScopes.add("Scope java 1");
        javaScopes.add("Scope java 2");
        javaScopes.add("Scope java 3");

        List<CourseLesson> javaLessons1 = new ArrayList<>();
        javaLessons1.add(new CourseLesson("1-1", "Data java 1"));
        javaLessons1.add(new CourseLesson("1-2", "Data java 2"));

        List<CourseLesson> javaLessons2 = new ArrayList<>();
        javaLessons2.add(new CourseLesson("2-1", "Data java 3"));
        javaLessons2.add(new CourseLesson("2-2", "Data java 4"));

        List<CourseSection> javaSections = new ArrayList<>();
        javaSections.add(new CourseSection("Section 1", javaLessons1));
        javaSections.add(new CourseSection("Section 2", javaLessons2));

        Course javaCourse = new Course(
                1L,
                "Java",
                15.0,
                javaCategories,
                CourseDifficulty.EASY,
                javaScopes,
                "Java description",
                javaSections,
                "base64 thumbnail");

        courseRepository.save(javaCourse);

        //COURSE - PROGRAMMING GUITAR
        List<CourseCategory> guitarCategories = new ArrayList<>();
        guitarCategories.add(categoryMusic);
        guitarCategories.add(categoryProgrammingPython);
        List<String> guitarScopes = new ArrayList<>();
        javaScopes.add("Scope guitar 1");
        javaScopes.add("Scope guitar 2");

        List<CourseLesson> guitarLessons1 = new ArrayList<>();
        guitarLessons1.add(new CourseLesson("1-1", "Data guitar 1"));
        guitarLessons1.add(new CourseLesson("1-2", "Data guitar 2"));

        List<CourseLesson> guitarLessons2 = new ArrayList<>();
        guitarLessons1.add(new CourseLesson("2-1", "Data guitar 3"));
        guitarLessons1.add(new CourseLesson("2-2", "Data guitar 4"));

        List<CourseSection> guitarSections = new ArrayList<>();
        guitarSections.add(new CourseSection("Section 1", guitarLessons1));
        guitarSections.add(new CourseSection("Section 2", guitarLessons2));

        Course guitarCourse = new Course(
                2L,
                "Guitar",
                0.0,
                guitarCategories,
                CourseDifficulty.MEDIUM,
                guitarScopes,
                "Guitar description",
                guitarSections,
                "base64 thumbnail");

        courseRepository.save(guitarCourse);

        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("test"));
        userRepository.save(user);
    }

}
