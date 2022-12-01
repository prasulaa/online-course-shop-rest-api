package pl.edu.pw.restapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PayuNotificationDTO {

    private Order order;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Order {

        private String orderId;
        private String status;

    }

}
