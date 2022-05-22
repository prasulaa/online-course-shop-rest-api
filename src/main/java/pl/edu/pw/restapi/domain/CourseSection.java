package pl.edu.pw.restapi.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CourseSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<CourseLesson> lessons;

    public CourseSection(String name, List<CourseLesson> lessons) {
        this.name = name;
        this.lessons = lessons;
    }

}
