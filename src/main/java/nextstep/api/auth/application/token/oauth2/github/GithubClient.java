package nextstep.api.auth.application.token.oauth2.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
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
        final var githubAccessTokenRequest = new GithubAccessTokenRequest(
                code,
                clientId,
                clientSecret
        );

        final var headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        final HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
                githubAccessTokenRequest, headers);
        final var restTemplate = new RestTemplate();

        final var accessToken = restTemplate
                .exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody()
                .getAccessToken();
        if (accessToken == null) {
            throw new RuntimeException();
        }
        return accessToken;
    }

    public GithubProfileResponse getGithubProfileFromGithub(final String accessToken) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", "token " + accessToken);

        final var httpEntity = new HttpEntity<>(headers);
        final var restTemplate = new RestTemplate();

        try {
            return restTemplate
                    .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException();
        }
    }
}
