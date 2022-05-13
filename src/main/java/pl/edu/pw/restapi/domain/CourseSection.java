package pl.edu.pw.restapi.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class CourseSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private List<CourseLesson> lessons;

    public CourseSection(Long id, String name, List<CourseLesson> lessons) {
        this.id = id;
        this.name = name;
        this.lessons = lessons;
    }

    public CourseSection() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseLesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<CourseLesson> lessons) {
        this.lessons = lessons;
    }

}
