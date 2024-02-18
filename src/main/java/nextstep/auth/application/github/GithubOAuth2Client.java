package nextstep.auth.application.github;

import nextstep.auth.application.dto.OAuth2Response;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubOAuth2Client {

    private final GithubOAuth2ClientProperties githubClientProperties;
    private final RestTemplate restTemplate;

    public GithubOAuth2Client(final GithubOAuth2ClientProperties githubClientProperties, final RestTemplateBuilder restTemplateBuilder) {
        this.githubClientProperties = githubClientProperties;
        this.restTemplate = restTemplateBuilder
                .errorHandler(new GithubOAuth2ClientErrorHandler())
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

    public OAuth2Response requestGithubProfile(final String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken));

        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        return restTemplate
                .exchange(githubClientProperties.getProfileUrl(), HttpMethod.GET, httpEntity, OAuth2Response.class)
                .getBody();
    }
}
