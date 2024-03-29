package pl.edu.pw.restapi.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CourseLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Lob
    private String data;

    public CourseLesson(String name, String data) {
        this.name = name;
        this.data = data;
    }

}
