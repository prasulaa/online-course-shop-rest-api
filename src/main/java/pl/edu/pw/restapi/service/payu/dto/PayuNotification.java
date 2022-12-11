package pl.edu.pw.restapi.service.payu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PayuNotification {

    private Order order;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Order {

        private String orderId;
        private String orderCreateDate;
        private String customerIp;
        private String description;
        private String currencyCode;
        private String totalAmount;
        private String status;
        private List<PayuProduct> products;

    }

}
