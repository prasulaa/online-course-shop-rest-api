package pl.edu.pw.restapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Double price;
    @ManyToMany
    private List<CourseCategory> categories;
    private CourseDifficulty difficulty;
    @ElementCollection
    private List<String> scopes;
    private String description;
    @OneToMany(cascade = CascadeType.ALL)
    private List<CourseSection> sections;
    private String thumbnail;

}
