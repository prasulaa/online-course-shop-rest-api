package pl.edu.pw.restapi.service.payu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PayuResponse {

    private StatusCode status;
    private String redirectUri;
    private String orderId;

    @NoArgsConstructor
    @Setter
    @Getter
    public static class StatusCode {

        private String statusCode;

    }
}
