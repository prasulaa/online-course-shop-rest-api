package pl.edu.pw.restapi.service.payu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PayuAuthorizationResponse {

    private String access_token;
    private String token_type;
    private Long expires_in;
    private String grant_type;

}
