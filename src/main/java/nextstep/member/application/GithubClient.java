package nextstep.member.application;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubClient {

    private final GithubClientProperties githubClientProperties;
    private final RestTemplate restTemplate;

    public GithubClient(final GithubClientProperties githubClientProperties, final RestTemplateBuilder restTemplateBuilder) {
        this.githubClientProperties = githubClientProperties;
        this.restTemplate = restTemplateBuilder
                .errorHandler(new GithubClientErrorHandler())
                .build();
    }

    public String requestGithubToken(final String code) {
        final GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
                code,
                githubClientProperties.getClientId(), // client id
                githubClientProperties.getClientSecret() // client secret
        );

        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        final HttpEntity<GithubAccessTokenRequest> httpEntity = new HttpEntity<>(githubAccessTokenRequest, headers);

        return restTemplate
                .exchange(githubClientProperties.getTokenUrl(), HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody()
                .getAccessToken();
    }

    public GithubProfileResponse requestGithubProfile(final String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken));

        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        return restTemplate
                .exchange(githubClientProperties.getProfileUrl(), HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                .getBody();
    }
}
