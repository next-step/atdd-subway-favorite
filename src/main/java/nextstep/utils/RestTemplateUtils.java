package nextstep.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public final class RestTemplateUtils {

    private static final RestTemplate restTemplate = new RestTemplate();

    private RestTemplateUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T post(HttpEntity<?> httpEntity, String uri, Class<T> responseClass) {
        return restTemplate
                .postForEntity(uri, httpEntity, responseClass)
                .getBody();
    }

    public static <T> T getWithHeaders(HttpEntity<?> httpEntity, String uri, Class<T> responseClass) {
        return restTemplate
                .exchange(uri, HttpMethod.GET, httpEntity, responseClass)
                .getBody();
    }
}
