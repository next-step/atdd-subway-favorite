package nextstep.member.application;

import nextstep.common.exception.AuthorizationException;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class GithubClient {
    public static final String TOKEN_PREFIX = "token ";
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.url.access-token}")
    private String tokenUrl;
    @Value("${github.url.profile}")
    private String profileUrl;
    private final RestTemplate restTemplate;

    public GithubClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getAccessTokenFromGithub(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(code, clientId, clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> httpEntity = new HttpEntity(githubAccessTokenRequest, headers);

        try {
            GithubAccessTokenResponse response = requestToGihub(tokenUrl, httpEntity, HttpMethod.POST, GithubAccessTokenResponse.class).getBody();

            if (Objects.isNull(response.getAccessToken())) {
                throw new AuthorizationException();
            }

            return response.getAccessToken();
        } catch (HttpClientErrorException e) {
            throw new AuthorizationException(e);
        }
    }

    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + accessToken);

        HttpEntity httpEntity = new HttpEntity<>(headers);

        try {
            GithubProfileResponse response = (GithubProfileResponse) requestToGihub(profileUrl, httpEntity, HttpMethod.GET, GithubProfileResponse.class).getBody();

            if (Objects.isNull(response.getEmail())) {
                throw new AuthorizationException();
            }

            return response;
        } catch (HttpClientErrorException e) {
            throw new AuthorizationException();
        }
    }

    private <T> ResponseEntity<T> requestToGihub(String apiUrl, HttpEntity<Object> httpEntity, HttpMethod httpMethod, Class<T> responseType) {
        ResponseEntity response = restTemplate.exchange(apiUrl, httpMethod, httpEntity, responseType);

        if (Objects.isNull(response)) {
            throw new AuthorizationException();
        }

        return response;
    }
}
