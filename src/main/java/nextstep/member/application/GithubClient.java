package nextstep.member.application;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.GithubTokenResponse;
import nextstep.member.domain.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class GithubClient {
    private static final String TOKEN_PREFIX = "token ";

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.url.access-token}")
    private String tokenUrl;
    @Value("${github.url.profile}")
    private String profileUrl;

    public String getAccessTokenFromGithub(final String code) {
        final var tokenRequest = new GithubTokenRequest(code, clientId, clientSecret);

        final HttpHeaders headers = new HttpHeaders();
        headers.add(ACCEPT, APPLICATION_JSON_VALUE);
        final var httpEntity = new HttpEntity(tokenRequest, headers);

        final RestTemplate restTemplate = new RestTemplate();

        try {
            final var tokenResponse = restTemplate
                    .exchange(tokenUrl, POST, httpEntity, GithubTokenResponse.class)
                    .getBody();

            Objects.requireNonNull(tokenResponse);

            final String token = tokenResponse.getAccessToken();
            if (Objects.isNull(token)) {
                throw new InvalidTokenException();
            }

            return token;
        } catch (HttpStatusCodeException e) {
            throw new InvalidTokenException();
        }
    }

    public GithubProfileResponse getGithubProfileFromGithub(final String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, TOKEN_PREFIX + accessToken);

        final HttpEntity httpEntity = new HttpEntity<>(headers);
        final RestTemplate restTemplate = new RestTemplate();

        try {
            return restTemplate
                    .exchange(profileUrl, GET, httpEntity, GithubProfileResponse.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException();
        }
    }
}
