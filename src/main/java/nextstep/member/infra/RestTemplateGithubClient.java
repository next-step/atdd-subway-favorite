package nextstep.member.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.domain.GithubClient;

public class RestTemplateGithubClient implements GithubClient {

    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private final String githubTokenUrl;
    private final String githubUserUrl;

    public RestTemplateGithubClient(RestTemplate restTemplate, @Value("${github.client-id}") String clientId,
        @Value("${github.client-secret}") String clientSecret,
        @Value("${github.token-url}") String githubTokenUrl,
        @Value("${github.user-url}") String githubUserUrl) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.githubTokenUrl = githubTokenUrl;
        this.githubUserUrl = githubUserUrl;
    }

    @Override
    public GithubAccessTokenResponse getAccessToken(GithubAccessTokenRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<GithubAccessTokenRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<GithubAccessTokenResponse> response = restTemplate.exchange(githubTokenUrl, HttpMethod.POST, entity, GithubAccessTokenResponse.class);

        return response.getBody();
    }

    @Override
    public GithubProfileResponse getUserProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<GithubProfileResponse> response = restTemplate.exchange(githubUserUrl, HttpMethod.GET, entity, GithubProfileResponse.class);

        return response.getBody();
    }
}

