package nextstep.member.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import nextstep.member.application.dto.GitHubAccessTokenRequest;
import nextstep.member.application.dto.GitHubAccessTokenResponse;

@Component
public class GitHubClient {

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.url.access-token}")
    private String accessTokenUrl;

    private final RestTemplate restTemplate;

    public GitHubClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getAccessTokenFromGithub(String code) {
        GitHubAccessTokenRequest gitHubAccessTokenRequest = new GitHubAccessTokenRequest(code, clientId, clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<GitHubAccessTokenRequest> httpEntity = new HttpEntity<>(gitHubAccessTokenRequest, headers);

        String accessToken = restTemplate.postForEntity(accessTokenUrl, httpEntity, GitHubAccessTokenResponse.class)
            .getBody()
            .getAccessToken();

        if (accessToken == null) {
            throw new RuntimeException();
        }

        return accessToken;
    }
}
