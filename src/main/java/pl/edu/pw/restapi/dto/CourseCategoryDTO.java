package pl.edu.pw.restapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
public class CourseCategoryDTO {

    private Long id;
    private String name;
    private List<CourseCategoryDTO> subcategories;

}
