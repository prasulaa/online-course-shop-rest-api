package pl.edu.pw.restapi.service.payu.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PayuProduct {

    private String name;
    private String unitPrice;
    private String quantity;

}
