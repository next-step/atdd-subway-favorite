package nextstep.auth.infra;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.config.exception.MissingTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static nextstep.auth.config.message.AuthError.*;

@Component
public class GithubClient {

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.url.access-token}")
    private String tokenUrl;
    @Value("${github.url.profile}")
    private String profileUrl;

    private final RestTemplate restTemplate;

    public GithubClient(final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getAccessTokenFromGithub(final String code) {
        final GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
                code,
                clientId,
                clientSecret
        );
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        final HttpEntity<GithubAccessTokenRequest> httpEntity = new HttpEntity<>(githubAccessTokenRequest, headers);

        try {
            final GithubAccessTokenResponse response = restTemplate.postForEntity(tokenUrl, httpEntity, GithubAccessTokenResponse.class).getBody();
            return response.getAccessToken();
        } catch (HttpClientErrorException e) {
            throw new MissingTokenException(NOT_MISSING_TOKEN);
        }
    }

    public GithubProfileResponse getGithubProfileFromGithub(final String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + accessToken);

        try {
            return restTemplate
                    .exchange(profileUrl, HttpMethod.GET, new HttpEntity<>(headers), GithubProfileResponse.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException();
        }
    }
}