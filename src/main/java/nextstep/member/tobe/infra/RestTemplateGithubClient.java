package nextstep.member.tobe.infra;

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
import nextstep.member.tobe.domain.GithubClient;

@Component
public class RestTemplateGithubClient implements GithubClient {
    private static final String AUTHORIZATION_HTTP_HEADER_NAME = "Authorization";
    private static final String ACCEPT_HTTP_HEADER_NAME = "Accept";
    private static final String BEARER_PREFIX = "Bearer";

    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private final String githubTokenUrl;
    private final String githubUserUrl;

    public RestTemplateGithubClient(
        RestTemplate restTemplate,
        @Value("${github.client-id}") String clientId,
        @Value("${github.client-secret}") String clientSecret,
        @Value("${github.token-url}") String githubTokenUrl,
        @Value("${github.user-url}") String githubUserUrl
    ) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.githubTokenUrl = githubTokenUrl;
        this.githubUserUrl = githubUserUrl;
    }

    @Override
    public GithubAccessTokenResponse getAccessToken(GithubAccessTokenRequest request) {
        HttpHeaders headers = buildGetAccessTokenHttpHeaders();

        ResponseEntity<GithubAccessTokenResponse> response = restTemplate.exchange(
            githubTokenUrl,
            HttpMethod.POST,
            buildGetAccessTokenHttpEntity(request, headers),
            GithubAccessTokenResponse.class
        );

        return response.getBody();
    }

    private HttpHeaders buildGetAccessTokenHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(ACCEPT_HTTP_HEADER_NAME, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private HttpEntity<GithubAccessTokenRequest> buildGetAccessTokenHttpEntity(
        GithubAccessTokenRequest request,
        HttpHeaders headers
    ) {
        return new HttpEntity<>(request, headers);
    }

    @Override
    public GithubProfileResponse getUserProfile(String accessToken) {
        HttpHeaders headers = buildGetUserProfileHttpHeaders(accessToken);

        ResponseEntity<GithubProfileResponse> response = restTemplate.exchange(
            githubUserUrl,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            GithubProfileResponse.class
        );

        return response.getBody();
    }

    private HttpHeaders buildGetUserProfileHttpHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HTTP_HEADER_NAME, String.format("%s %s", BEARER_PREFIX, accessToken));
        return headers;
    }
}

