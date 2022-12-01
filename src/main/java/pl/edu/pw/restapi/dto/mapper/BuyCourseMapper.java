package pl.edu.pw.restapi.dto.mapper;

import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.dto.BuyCourseResponseDTO;
import pl.edu.pw.restapi.service.payu.PayuResponse;

@Service
public class BuyCourseMapper {

    public BuyCourseResponseDTO map(PayuResponse response) {
        if (response == null) {
            return null;
        } else {
            return BuyCourseResponseDTO.builder()
                    .orderId(response.getOrderId())
                    .redirectUri(response.getRedirectUri())
                    .build();
        }
    }

}
