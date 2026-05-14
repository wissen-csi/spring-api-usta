package com.usta.edu.co.MedicineRotationManager.exceptions_handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import com.usta.edu.co.MedicineRotationManager.dto.Err;

@RestControllerAdvice
public class ExceptionHandlerExternalApi {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerExternalApi.class);

    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    public ResponseEntity<Err> manyRequestExternalApi(HttpClientErrorException.TooManyRequests exception) {
        LOG.warn("Many request in external api", exception);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new Err(429, "Many request in external api", HttpStatus.TOO_MANY_REQUESTS.name()));
    }

    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public ResponseEntity<Err> conflictExternalApi(HttpClientErrorException.Conflict exception) {
        LOG.warn("Conflict with External api", exception);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new Err(409, "Conflict with External api", HttpStatus.CONFLICT.name()));
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<Err> notFoundExternalApi(HttpClientErrorException.NotFound exception) {
        LOG.warn("Not Found external api", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Err(404, "Not Found external api", HttpStatus.NOT_FOUND.name()));
    }

    @ExceptionHandler(HttpClientErrorException.NotAcceptable.class)
    public ResponseEntity<Err> notAcceptable(HttpClientErrorException.NotAcceptable exception) {
        LOG.warn("Not Acceptable external api", exception);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(new Err(406, "Not Acceptable external api", HttpStatus.NOT_ACCEPTABLE.name()));
    }

    @ExceptionHandler(HttpServerErrorException.BadGateway.class)
    public ResponseEntity<Err> badGateWayExternalApi(HttpServerErrorException.BadGateway exception) {
        LOG.warn("Bad Gateway External Api", exception);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new Err(502, "Bad Gateway External Api", HttpStatus.BAD_GATEWAY.name()));
    }

    @ExceptionHandler(HttpServerErrorException.GatewayTimeout.class)
    public ResponseEntity<Err> gatewatTimeoutExternalApi(HttpServerErrorException.GatewayTimeout exception) {
        LOG.warn("Gateway Timeout External api", exception);
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                .body(new Err(504, "Gateway Timeout External api", HttpStatus.GATEWAY_TIMEOUT.name()));
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Err> genericApiServerException(HttpServerErrorException exception) {
        LOG.warn("Err generic", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Err(500, "Err generic", HttpStatus.INTERNAL_SERVER_ERROR.name()));
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Err> genericApiException(RestClientException exception) {
        LOG.warn("Err generic", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Err(400, "Err generic", HttpStatus.BAD_REQUEST.name()));
    }
}
