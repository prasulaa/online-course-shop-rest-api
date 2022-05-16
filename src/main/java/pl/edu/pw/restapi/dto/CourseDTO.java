package pl.edu.pw.restapi.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseDTO {

    private Long id;
    private String title;
    private String thumbnail;
    private Double price;

}
