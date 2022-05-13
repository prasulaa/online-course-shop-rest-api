package pl.edu.pw.restapi.domain;

import javax.persistence.*;

@Entity
@Table
public class CourseLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String data;

    public CourseLesson(Long id, String name, String data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    public CourseLesson() {
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
