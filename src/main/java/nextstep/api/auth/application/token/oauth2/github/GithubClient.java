package nextstep.api.auth.application.token.oauth2.github;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import nextstep.api.auth.application.token.oauth2.github.dto.GithubAccessTokenRequest;
import nextstep.api.auth.application.token.oauth2.github.dto.GithubAccessTokenResponse;
import nextstep.api.auth.application.token.oauth2.github.dto.GithubProfileResponse;

@Component
public class GithubClient {
    private final String clientId;
    private final String clientSecret;
    private final String tokenUrl;
    private final String profileUrl;

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public GithubClient(@Value("${github.client.id}") final String clientId,
                        @Value("${github.client.secret}") final String clientSecret,
                        @Value("${github.url.access-token}") final String tokenUrl,
                        @Value("${github.url.profile}") final String profileUrl
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenUrl = tokenUrl;
        this.profileUrl = profileUrl;
    }

    public String getAccessTokenFromGithub(final String code) {
        final var githubAccessTokenRequest = new GithubAccessTokenRequest(code, clientId, clientSecret);
        final var headers = makeHttpHeaders("Accept", MediaType.APPLICATION_JSON_VALUE);
        final var httpEntity = new HttpEntity<>(githubAccessTokenRequest, headers);

        return requestAccessToken(httpEntity);
    }

    private String requestAccessToken(final HttpEntity<GithubAccessTokenRequest> httpEntity) {
        final var responseEntity = request(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class);

        final var response = Optional.ofNullable(responseEntity.getBody()).orElseThrow(() -> {
            log.error("Github Access Token 요청에 실패했습니다");
            throw new RuntimeException();
        });

        return Optional.ofNullable(response.getAccessToken()).orElseThrow(() -> {
            log.error("Github Access Token 요청으로부터 accessToken 정보를 추출하지 못했습니다");
            throw new RuntimeException();
        });

    }

    public GithubProfileResponse getGithubProfileFromGithub(final String accessToken) {
        final var headers = makeHttpHeaders("Authorization", "token " + accessToken);
        final var httpEntity = new HttpEntity<>(headers);

        return request(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class).getBody();
    }

    private HttpHeaders makeHttpHeaders(final String name, final String value) {
        final var headers = new HttpHeaders();
        headers.add(name, value);
        return headers;
    }

    private <T> ResponseEntity<T> request(final String url, final HttpMethod method,
                                          final HttpEntity<?> requestEntity, final Class<T> clazz) {
        try {
            return REST_TEMPLATE.exchange(url, method, requestEntity, clazz);
        } catch (final HttpClientErrorException e) {
            log.error("Http 요청에 실패했습니다 (" + url + ")", e);
            throw new RuntimeException();
        }
    }
}
