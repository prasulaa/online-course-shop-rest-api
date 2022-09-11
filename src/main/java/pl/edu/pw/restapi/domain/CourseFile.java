package pl.edu.pw.restapi.domain;

import lombok.*;
import javax.persistence.*;
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CourseFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @Lob
    private byte[] data;
    @ManyToOne
    private Course course;

}
