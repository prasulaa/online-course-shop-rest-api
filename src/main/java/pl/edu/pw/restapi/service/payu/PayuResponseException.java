package pl.edu.pw.restapi.service.payu;

import lombok.Getter;

@Getter
public class PayuResponseException extends RuntimeException {

    private final int status;
    private final String body;

    public PayuResponseException(String message, int status, String body) {
        super(message);
        this.status = status;
        this.body = body;
    }

}
