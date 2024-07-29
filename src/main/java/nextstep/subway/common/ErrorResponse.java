package nextstep.subway.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ErrorResponse {

    public static ResponseEntity<String> badRequest(String message) {
        return new ResponseEntity<>(message, makeJsonHeader() , HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<String> notAcceptable(String message) {
        return new ResponseEntity<>(message, makeJsonHeader(), HttpStatus.NOT_ACCEPTABLE);
    }

    public static ResponseEntity<String> serverError(String message) {
        return new ResponseEntity<>(message, makeJsonHeader(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static HttpHeaders makeJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
    }
}
