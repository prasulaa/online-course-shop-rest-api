package pl.edu.pw.restapi.service.payu;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.Course;
import pl.edu.pw.restapi.service.payu.dto.PayuAuthorizationResponse;
import pl.edu.pw.restapi.service.payu.dto.PayuProduct;
import pl.edu.pw.restapi.service.payu.dto.PayuRequest;
import pl.edu.pw.restapi.service.payu.dto.PayuResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@Slf4j
public class PayuConnector {

    private final String url;
    private final String posId;
    private final String clientId;
    private final String clientSecret;
    private final String notifyUrl;
    private final ObjectMapper objectMapper;

    public PayuConnector(@Value("${payu.url}") String url,
                         @Value("${payu.posId}") String posId,
                         @Value("${payu.clientId}") String clientId,
                         @Value("${payu.clientSecret}") String clientSecret,
                         @Value("${payu.notifyUrl}") String notifyUrl,
                         ObjectMapper objectMapper) {
        this.url = url;
        this.posId = posId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.notifyUrl = notifyUrl;
        this.objectMapper = objectMapper;
    }

    public PayuResponse createOrder(PayuRequest request) {
        try {
            String authToken = getAuthorizationToken();

            HttpResponse<String> response = sendOrderRequest(authToken, request);

            if (response.statusCode() != 302) {
                throw new PayuResponseException("Payu order response status not equal 302",
                        response.statusCode(), response.body());
            }

            return objectMapper.readValue(response.body(), PayuResponse.class);
        } catch (IOException | InterruptedException e) {
            log.error("Error during connecting to Payu", e);
            throw new RuntimeException(e);
        }
    }

    private HttpResponse<String> sendOrderRequest(String authToken, PayuRequest request) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder(buildOrderURI())
                .header("Authorization", "Bearer " + authToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(request)))
                .build();

        return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public PayuRequest createRequest(String username, Course course, String ip) {
        return PayuRequest.builder()
                .notifyUrl(notifyUrl + "?id=" + course.getId() + "&user=" + username)
                .customerIp(ip)
                .merchantPosId(posId)
                .description(course.getTitle())
                .currencyCode("PLN")
                .totalAmount(payuPrice(course.getPrice()))
                .products(List.of(createProductRequest(course)))
                .build();
    }

    private PayuProduct createProductRequest(Course course) {
        return PayuProduct.builder()
                .name(course.getTitle())
                .unitPrice(payuPrice(course.getPrice()))
                .quantity("1")
                .build();
    }

    private String payuPrice(double price) {
        return Integer.toString((int) (price * 100));
    }

    private String getAuthorizationToken() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(buildAuthorizationURI())
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new PayuResponseException("Payu authorization response status not equal 200", response.statusCode(), response.body());
        }

        PayuAuthorizationResponse authResponse = objectMapper.readValue(response.body(), PayuAuthorizationResponse.class);
        return authResponse.getAccess_token();
    }

    private URI buildOrderURI() {
        return URI.create(url + "/api/v2_1/orders");
    }

    private URI buildAuthorizationURI() {
        return URI.create(
                url +
                        "/pl/standard/user/oauth/authorize" +
                        "?grant_type=client_credentials" +
                        "&client_id=" + clientId +
                        "&client_secret=" + clientSecret
        );
    }

}
