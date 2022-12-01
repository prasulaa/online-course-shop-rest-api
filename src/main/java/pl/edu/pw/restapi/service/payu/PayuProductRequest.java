package pl.edu.pw.restapi.service.payu;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PayuProductRequest {

    private String name;
    private String unitPrice;
    private String quantity;

}
