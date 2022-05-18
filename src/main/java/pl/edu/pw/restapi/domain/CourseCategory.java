package pl.edu.pw.restapi.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CourseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    @OneToMany
    private List<CourseCategory> subcategories;

    public CourseCategory(String category) {
        this.category = category;
    }

}
