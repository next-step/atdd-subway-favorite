package nextstep.member.application;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.domain.exception.AuthorizationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class GithubClient {
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.url.access-token}")
    private String tokenUrl;
    private final RestTemplate restTemplate;

    public GithubClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getAccessTokenFromGithub(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(code, clientId, clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<GithubTokenRequest> httpEntity = new HttpEntity(githubAccessTokenRequest, headers);

        try {
            return requestToGihub(httpEntity);
        } catch (HttpClientErrorException e) {
            throw new AuthorizationException(e);
        }
    }

    private String requestToGihub(HttpEntity<GithubTokenRequest> httpEntity) {
        GithubAccessTokenResponse response = restTemplate.exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class).getBody();

        if (Objects.isNull(response) || Objects.isNull(response.getAccessToken())) {
            throw new AuthorizationException();
        }

        return response.getAccessToken();
    }
}
