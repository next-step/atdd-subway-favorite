package nextstep.subway.auth.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Base64;

public class AuthorizationTestUtils {

    private AuthorizationTestUtils() {
    }

    private static final String REGEX = ":";

    public static MockHttpServletRequest addBasicAuthorizationHeader(MockHttpServletRequest request, String email, String password) {
        byte[] targetBytes = (email + REGEX + password).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + credentials);
        return request;
    }

    public static MockHttpServletRequest setBearerAuthorizationHeader(MockHttpServletRequest request, String token) {
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return request;
    }
}
