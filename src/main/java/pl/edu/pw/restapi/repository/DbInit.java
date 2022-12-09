package pl.edu.pw.restapi.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DbInit implements CommandLineRunner {

    private final CourseCategoryRepository courseCategoryRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseFileRepository courseFileRepository;
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
        javaScopes.add("Scope java 1 Scope java 1 Scope java 1 Scope java 1 Scope java 1 Scope java 1 Scope java 1 Scope java 1");
        javaScopes.add("Scope java 2 Scope java 2 Scope java 2 Scope java 2 Scope java 2 Scope java 2 Scope java 2 Scope java 2");
        javaScopes.add("Scope java 3 Scope java 3 Scope java 3 Scope java 3 Scope java 3 Scope java 3 Scope java 3 Scope java 3");

        String lesson = Files.readString(Path.of("src/main/resources/initdata/text.md"));

        List<CourseLesson> javaLessons1 = new ArrayList<>();
        javaLessons1.add(new CourseLesson("1-1", lesson));
        javaLessons1.add(new CourseLesson("1-2", "Data java 2"));

        List<CourseLesson> javaLessons2 = new ArrayList<>();
        javaLessons2.add(new CourseLesson("2-1", "Data java 3"));
        javaLessons2.add(new CourseLesson("2-2", "Data java 4"));

        List<CourseSection> javaSections = new ArrayList<>();
        javaSections.add(new CourseSection("Section 1", javaLessons1));
        javaSections.add(new CourseSection("Section 2", javaLessons2));

        String thumbnail = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(Path.of("src/main/resources/initdata/thumbnail.png")));

        Course javaCourse = new Course(
                null,
                "Java",
                15.0,
                javaCategories,
                CourseDifficulty.EASY,
                javaScopes,
                "Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description Java description",
                javaSections,
                thumbnail);

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
                null,
                "Guitar course",
                0.0,
                guitarCategories,
                CourseDifficulty.MEDIUM,
                guitarScopes,
                "Guitar description",
                guitarSections,
                thumbnail);

        courseRepository.save(guitarCourse);

        User user = new User();
        user.setUsername("string");
        user.setPassword(passwordEncoder.encode("string"));
        user.setBoughtCourses(List.of(guitarCourse));
        user.setReleasedCourses(List.of(javaCourse));
        userRepository.save(user);

        CourseFile file1 = new CourseFile(null, "file1.jpg", "jpg", Files.readAllBytes(Path.of("src/main/resources/initdata/frog.jpg")), javaCourse);
        CourseFile file2 = new CourseFile(null, "file2.mp4", "mp4", Files.readAllBytes(Path.of("src/main/resources/initdata/earth.mp4")), javaCourse);
        courseFileRepository.saveAll(List.of(file1, file2));


        for (int i = 0; i < 10; i++) {
            courseRepository.save(new Course(
                    null,
                    "Guitar " + i + " course",
                    0.0,
                    guitarCategories,
                    CourseDifficulty.MEDIUM,
                    guitarScopes,
                    "Guitar description",
                    null,
                    thumbnail));
        }
    }

}
