package nextstep.member.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import nextstep.exception.AuthorizationException;
import nextstep.member.application.dto.GitHubAccessTokenRequest;
import nextstep.member.application.dto.GitHubAccessTokenResponse;
import nextstep.member.application.dto.GitHubProfileResponse;

@Component
public class GitHubClient {

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.url.access-token}")
    private String accessTokenUrl;

    @Value("${github.url.profile}")
    private String profileUrl;

    private final RestTemplate restTemplate;

    public GitHubClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getAccessTokenFromGitHub(String code) {
        GitHubAccessTokenRequest gitHubAccessTokenRequest = new GitHubAccessTokenRequest(code, clientId, clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<GitHubAccessTokenRequest> httpEntity = new HttpEntity<>(gitHubAccessTokenRequest, headers);

        try {
            return requestGitHubAccessToken(httpEntity);
        } catch (HttpClientErrorException e) {
            throw new AuthorizationException(e);
        }
    }

    private String requestGitHubAccessToken(HttpEntity<GitHubAccessTokenRequest> httpEntity) {
        GitHubAccessTokenResponse response = restTemplate
            .postForEntity(accessTokenUrl, httpEntity, GitHubAccessTokenResponse.class)
            .getBody();

        if (response == null || response.getAccessToken() == null) {
            throw new AuthorizationException();
        }

        return response.getAccessToken();
    }

    public GitHubProfileResponse getGithubProfileFromGithub(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "token " + accessToken);

        try {
            return restTemplate
                .exchange(profileUrl, HttpMethod.GET, new HttpEntity<>(headers), GitHubProfileResponse.class)
                .getBody();
        } catch (HttpClientErrorException e) {
            throw new AuthorizationException(e);
        }
    }
}
