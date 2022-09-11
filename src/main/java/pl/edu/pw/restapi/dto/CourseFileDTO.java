package pl.edu.pw.restapi.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseFileDTO {

    private Long id;
    private String name;
    private String type;
    private byte[] data;

}
