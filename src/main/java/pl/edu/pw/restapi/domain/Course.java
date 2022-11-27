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
    @Lob
    private String description;
    @OneToMany(cascade = CascadeType.ALL)
    private List<CourseSection> sections;
    @Lob
    private String thumbnail;

}
