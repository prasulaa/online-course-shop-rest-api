package pl.edu.pw.restapi.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BuyCourseResponseDTO {

    private String orderId;
    private String redirectUri;

}
